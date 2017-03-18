package com.jiangsu.product.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.jiangsu.product.domain.User;
import com.jiangsu.product.util.C3P0Util;

public class UserDao {

	//添加用户
	public static void addUser(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		String sql = "INSERT INTO USER(username,PASSWORD,gender,email,telephone,introduce,activeCode,state,registTime) VALUES(?,?,?,?,?,?,?,?,?);";
		qr.update(sql,user.getUsername(),user.getPassword(),user.getGender(),user.getEmail(),user.getTelephone(),user.getIntroduce(),user.getActiveCode(),user.getState(),user.getRegistTime());
	}

	//根据激活码查找
	public User findUserByActiveCode(String activeCode) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from user where activeCode=?", new BeanHandler<User>(User.class),activeCode);
	}

	//根据激活码激活用户
	public void activeCode(String activeCode) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		qr.update("update user set state=1 where activecode=?",activeCode);
	}

	//通过用户名和密码查找用户
	public User findUserByUserNameAndPassword(String username, String password) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from user where username=? and PASSWORD=?", new BeanHandler<User>(User.class),username,password);
	}

	//根据id查找用户
	public User findUserById(String id) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from user where id=?", new BeanHandler<User>(User.class),id);
	}

	public void modifyUser(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		qr.update("update user set password=?,gender=?,email=?,telephone=? where id=?",user.getPassword(),user.getGender(),user.getEmail(),user.getTelephone(),user.getId());
	}

}
