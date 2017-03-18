package com.jiangsu.product.service;

import java.sql.SQLException;

import com.jiangsu.product.dao.UserDao;
import com.jiangsu.product.domain.User;
import com.jiangsu.product.exception.UserException;
import com.jiangsu.product.util.SendJMail;

public class UserService {

	private UserDao userDao = new UserDao();

	//注册用户
	public void regist(User user) throws UserException {
		try {
			UserDao.addUser(user);
			String emailMsg = "注册成功，请点击<a href='http://localhost/Products/activeServlet?activeCode="+user.getActiveCode()+"'>激活</a>后登陆";
			SendJMail.sendMail(user.getEmail(), emailMsg);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("注册失败");
		}
	}

	//激活用户
	public void activeUser(String activeCode) throws UserException {
		User user;
		try {
			user = userDao.findUserByActiveCode(activeCode);
			if (user!=null) {
				userDao.activeCode(activeCode);
				return;
			}
			throw new UserException("激活失败");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("激活失败");
		}
		
	}

	//用户登陆
	public User login(String username, String password) throws UserException {
		User user = null;
		try {
			user = userDao.findUserByUserNameAndPassword(username,password);
			if (user==null) {
				throw new UserException("用户名或密码错误");
			}
			if (user.getState()==0) {
				throw new UserException("用户未激活，请激活");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("用户名或密码错误");
		}
		return user;
	}

	public User findUserById(String id) throws UserException {
		User user=null;
		try {
			user = userDao.findUserById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("用户信息读取失败");
		}
		return user;
	}

	public void modifyUser(User user) throws UserException {
		try {
			userDao.modifyUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("修改失败！");
		}
	}

}
