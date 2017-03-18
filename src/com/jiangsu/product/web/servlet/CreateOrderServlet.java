package com.jiangsu.product.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.jiangsu.product.domain.Order;
import com.jiangsu.product.domain.OrderItem;
import com.jiangsu.product.domain.Product;
import com.jiangsu.product.domain.User;
import com.jiangsu.product.service.OrderService;

public class CreateOrderServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				//封装Order对象
				Order order = new Order();
				User user =(User)request.getSession().getAttribute("user");
				if (user==null) {
					response.sendRedirect(request.getContextPath()+"/login.jsp");
					return;
				}
				try {
					BeanUtils.populate(order, request.getParameterMap());
					order.setId(UUID.randomUUID().toString());
					order.setUser(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//获得session对象中的购物车数据
				Map<Product, String> cart= (Map<Product, String>) request.getSession().getAttribute("cart");
				//遍历购物车数据，添加到orderItem对象中，同时把多个item对象添加到list集合中
				List<OrderItem> list = new ArrayList<OrderItem>();
				for (Product p : cart.keySet()) {
					OrderItem oi = new OrderItem();
					oi.setOrder(order);//把订单对象添加到订单项中
					oi.setP(p);//把商品对象添加到订单项中
					oi.setBuynum(Integer.parseInt(cart.get(p)));//购物车中的商品数量
					list.add(oi);//把每个订单项添加到集合中
				}
				//把集合放入order对象中
				order.setOrderItems(list);
				//调用业务逻辑
				OrderService orderService = new OrderService();
				orderService.addOrder(order);
				
				request.setAttribute("orderid", order.getId());
				request.setAttribute("money", order.getMoney());
				request.getRequestDispatcher("/pay.jsp").forward(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
