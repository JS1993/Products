package com.jiangsu.product.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.weld.resolution.Resolvable;

import com.jiangsu.product.domain.User;
import com.jiangsu.product.exception.UserException;
import com.jiangsu.product.service.UserService;

public class RegisterServlet extends HttpServlet {

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

		//处理验证码
		String ckcode =  request.getParameter("ckcode");
		String checkcode_session =(String)request.getSession().getAttribute("checkcode_session");
		
		if (!checkcode_session.equals(ckcode)) {  //如果两个验证码不一致，跳回注册
			request.setAttribute("ckcode_msg", "验证码错误");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}
		//获取表单数据
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());
			user.setActiveCode(UUID.randomUUID().toString());  //手动设置激活码
			//调用业务逻辑
			UserService us = new UserService();
			us.regist(user);
			//分发转向
			//要求用户激活后才能登陆，所以不能把用户信息保存在session中
			//request.getSession().setAttribute("user", user); //把用户信息封装到session
			request.getRequestDispatcher("/registersuccess.jsp").forward(request, response);
		} catch (UserException e) {
			request.setAttribute("user_msg", e.getMessage());
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
