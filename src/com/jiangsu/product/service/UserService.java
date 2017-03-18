package com.jiangsu.product.service;

import java.sql.SQLException;

import com.jiangsu.product.dao.UserDao;
import com.jiangsu.product.domain.User;
import com.jiangsu.product.exception.UserException;
import com.jiangsu.product.util.SendJMail;

public class UserService {

	private UserDao userDao = new UserDao();

	//ע���û�
	public void regist(User user) throws UserException {
		try {
			UserDao.addUser(user);
			String emailMsg = "ע��ɹ�������<a href='http://localhost/Products/activeServlet?activeCode="+user.getActiveCode()+"'>����</a>���½";
			SendJMail.sendMail(user.getEmail(), emailMsg);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("ע��ʧ��");
		}
	}

	//�����û�
	public void activeUser(String activeCode) throws UserException {
		User user;
		try {
			user = userDao.findUserByActiveCode(activeCode);
			if (user!=null) {
				userDao.activeCode(activeCode);
				return;
			}
			throw new UserException("����ʧ��");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("����ʧ��");
		}
		
	}

	//�û���½
	public User login(String username, String password) throws UserException {
		User user = null;
		try {
			user = userDao.findUserByUserNameAndPassword(username,password);
			if (user==null) {
				throw new UserException("�û������������");
			}
			if (user.getState()==0) {
				throw new UserException("�û�δ����뼤��");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("�û������������");
		}
		return user;
	}

	public User findUserById(String id) throws UserException {
		User user=null;
		try {
			user = userDao.findUserById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("�û���Ϣ��ȡʧ��");
		}
		return user;
	}

	public void modifyUser(User user) throws UserException {
		try {
			userDao.modifyUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("�޸�ʧ�ܣ�");
		}
	}

}
