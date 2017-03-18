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
			if ("admin".equals(user.getRole())) {  //����Ա��½
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

		//������֤��
		String ckcode =  request.getParameter("ckcode");
		String checkcode_session =(String)request.getSession().getAttribute("checkcode_session");
		
		if (!checkcode_session.equals(ckcode)) {  //���������֤�벻һ�£�����ע��
			request.setAttribute("ckcode_msg", "��֤�����");
			request.getRequestDispatcher("/register.jsp").forward(request, response);
			return;
		}
		//��ȡ������
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());
			user.setActiveCode(UUID.randomUUID().toString());  //�ֶ����ü�����
			//����ҵ���߼�
			UserService us = new UserService();
			us.regist(user);
			//�ַ�ת��
			//Ҫ���û��������ܵ�½�����Բ��ܰ��û���Ϣ������session��
			//request.getSession().setAttribute("user", user); //���û���Ϣ��װ��session
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
		request.getSession().invalidate();  //����session����ת����ҳ
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
