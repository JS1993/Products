package com.jiangsu.product.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.jiangsu.product.domain.Order;
import com.jiangsu.product.domain.OrderItem;
import com.jiangsu.product.domain.Product;
import com.jiangsu.product.util.C3P0Util;
import com.jiangsu.product.util.ManagerThreadLocal;
import com.sun.swing.internal.plaf.basic.resources.basic;

public class OrderDao {
	
	/**
	 * 添加订单
	 * @param order
	 * @throws SQLException 
	 */
	public void addOrder(Order order) throws SQLException {
		QueryRunner qr = new QueryRunner();
		qr.update(ManagerThreadLocal.getConnection(),"INSERT INTO orders VALUES(?,?,?,?,?,?,?,?)",order.getId(),order.getMoney()
				,order.getReceiverAddress(),order.getReceiverName(),
				order.getReceiverPhone(),order.getPaystate(),
				order.getOrdertime(),order.getUser().getId());
	}

	/**
	 * 根据id查询所有
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public List<Order> findOrders(int id) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from orders where user_id=?",new BeanListHandler<Order>(Order.class), id);
	}

	public Order findOrdersByOrderId(String orderidString) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		//得到一个订单
		Order  order = qr.query("select * from orders where id=?", new BeanHandler<Order>(Order.class),orderidString);
		//得到当前订单中的所有订单项
		//List<OrderItem> orderItems = qr.query("select * from orderItem where order_id=?", new BeanListHandler<OrderItem>(OrderItem.class),orderidString);
		//得到所有订单项中的商品信息
		//List<Product> products = qr.query("select * from products where id in(select product_id from orderItem where order_id=?)",new BeanListHandler<Product>(Product.class),orderidString);
		
		List<OrderItem> orderItems2 = qr.query("select * from orderitem o,products p where p.id=o.product_id and order_id=?",new ResultSetHandler<List<OrderItem>>(){

			@Override
			public List<OrderItem> handle(ResultSet rs) throws SQLException {
				List<OrderItem> orderItems = new ArrayList<OrderItem>();
				while (rs.next()) {
					//封装OrderItem对象
					OrderItem oi = new OrderItem();
					oi.setBuynum(rs.getInt("buynum"));
					//封装Product对象
					Product p = new Product();
					p.setName(rs.getString("name"));
					p.setPrice(rs.getDouble("price"));
					
					//把每个p对象封装到OrderItem中
					oi.setP(p);
					//把每个orderItem对象封装到集合中
					orderItems.add(oi);
				}
				return orderItems;
			}},orderidString);
		//把所有的订单项封装到主单中
		order.setOrderItems(orderItems2);
		
		return order;
	}

	/**
	 * 修改订单支付状态
	 * @param r6_Order
	 * @throws SQLException
	 */
	public void midifyOrderState(String r6_Order) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		qr.update("update orders set paystate=1 where id=?",r6_Order);
		
	}
	
}
