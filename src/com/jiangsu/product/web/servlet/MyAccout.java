package com.jiangsu.product.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiangsu.product.domain.User;

public class MyAccout extends HttpServlet {

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
		//从session中取出user对象
		User user = (User)request.getSession().getAttribute("user");
		//判断user是否为null
		if (user==null) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}else {
			//默认是普通用户页面
			String pathString = "/myAccount.jsp";
			if ("admin".equals(user.getRole())) {
				//管理员页面
				pathString = "admin/login/home.jsp";
			}
			request.getRequestDispatcher(pathString).forward(request, response);
		}
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
