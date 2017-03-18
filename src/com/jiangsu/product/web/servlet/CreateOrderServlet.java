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
				//��װOrder����
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
				//���session�����еĹ��ﳵ����
				Map<Product, String> cart= (Map<Product, String>) request.getSession().getAttribute("cart");
				//�������ﳵ���ݣ���ӵ�orderItem�����У�ͬʱ�Ѷ��item������ӵ�list������
				List<OrderItem> list = new ArrayList<OrderItem>();
				for (Product p : cart.keySet()) {
					OrderItem oi = new OrderItem();
					oi.setOrder(order);//�Ѷ���������ӵ���������
					oi.setP(p);//����Ʒ������ӵ���������
					oi.setBuynum(Integer.parseInt(cart.get(p)));//���ﳵ�е���Ʒ����
					list.add(oi);//��ÿ����������ӵ�������
				}
				//�Ѽ��Ϸ���order������
				order.setOrderItems(list);
				//����ҵ���߼�
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
