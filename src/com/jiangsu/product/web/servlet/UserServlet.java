package com.jiangsu.product.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.jiangsu.product.domain.User;
import com.jiangsu.product.exception.UserException;
import com.jiangsu.product.service.UserService;

public class UserServlet extends BaseServlet {

//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		String method = request.getParameter("method");
//		if (!"".equals(method)) {
//			if ("login".equals(method)) {
//				login(request, response);
//			}else if ("register".equals(method)) {
//				register(request, response);
//			}else if ("logout".equals(method)) {
//				logout(request, response);
//			}else if ("findUserById".equals(method)) {
//				findUserById(request, response);
//			}
//		}
//	}
//	
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		doGet(request, response);
//	}

	public void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserService us = new UserService();
		try {
			String pathString = "/index.jsp";
			User user = us.login(username,password);
			if ("admin".equals(user.getRole())) {  //管理员登陆
				pathString = "/admin/login/home.jsp";
			}
			request.getSession().setAttribute("user", user);
			request.getRequestDispatcher(pathString).forward(request, response);
		} catch (UserException e) {
			e.printStackTrace();
			request.setAttribute("user_msg", e.getMessage());
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
	
	public void register(HttpServletRequest request, HttpServletResponse response)
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
	
	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().invalidate();  //销毁session，跳转到首页
		response.sendRedirect(request.getContextPath()+"/index.jsp");
		
	}
	
	public void findUserById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		UserService us = new UserService();
		try {
			User user = us.findUserById(id);
			request.setAttribute("u", user);
			request.getRequestDispatcher("/modifyuserinfo.jsp").forward(request, response);
		} catch (UserException e) {
			e.printStackTrace();
			response.getWriter().write(e.getMessage());
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}
		
	}
}
