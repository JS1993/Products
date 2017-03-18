package com.jiangsu.product.service;

import java.sql.SQLException;
import java.util.List;

import com.jiangsu.product.dao.OrderDao;
import com.jiangsu.product.dao.OrderItemDao;
import com.jiangsu.product.dao.ProductDao;
import com.jiangsu.product.domain.Order;
import com.jiangsu.product.exception.OrderException;
import com.jiangsu.product.util.ManagerThreadLocal;

public class OrderService {
	OrderDao orderDao = new OrderDao();
	OrderItemDao orderItemDao = new OrderItemDao();
	ProductDao productDao = new ProductDao();
	/**
	 * 添加订单
	 * @param order
	 */
	public void addOrder(Order order) {
		try {
			ManagerThreadLocal.startTransacation();
			orderDao.addOrder(order);
			orderItemDao.addOrderItem(order);
			productDao.updateProductNum(order);
			ManagerThreadLocal.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ManagerThreadLocal.rollback();
		}
	}
	
	/**
	 * 根据用户id查找订单
	 * @param id
	 * @return
	 */
	public List<Order> findOrdersByUserId(int id) {
		try {
			return orderDao.findOrders(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Order findOrdersByOrderId(String orderidString) {
		
		try {
			return orderDao.findOrdersByOrderId(orderidString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void midifyOrderState(String r6_Order) throws OrderException {
		try {
			orderDao.midifyOrderState(r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new OrderException("修改失败");
		}
		
	}
	
	
}
