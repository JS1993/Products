package com.jiangsu.product.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.jiangsu.product.domain.User;
import com.jiangsu.product.util.C3P0Util;

public class UserDao {

	//����û�
	public static void addUser(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		String sql = "INSERT INTO USER(username,PASSWORD,gender,email,telephone,introduce,activeCode,state,registTime) VALUES(?,?,?,?,?,?,?,?,?);";
		qr.update(sql,user.getUsername(),user.getPassword(),user.getGender(),user.getEmail(),user.getTelephone(),user.getIntroduce(),user.getActiveCode(),user.getState(),user.getRegistTime());
	}

	//���ݼ��������
	public User findUserByActiveCode(String activeCode) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from user where activeCode=?", new BeanHandler<User>(User.class),activeCode);
	}

	//���ݼ����뼤���û�
	public void activeCode(String activeCode) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		qr.update("update user set state=1 where activecode=?",activeCode);
	}

	//ͨ���û�������������û�
	public User findUserByUserNameAndPassword(String username, String password) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from user where username=? and PASSWORD=?", new BeanHandler<User>(User.class),username,password);
	}

	//����id�����û�
	public User findUserById(String id) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		return qr.query("select * from user where id=?", new BeanHandler<User>(User.class),id);
	}

	public void modifyUser(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		qr.update("update user set password=?,gender=?,email=?,telephone=? where id=?",user.getPassword(),user.getGender(),user.getEmail(),user.getTelephone(),user.getId());
	}

}
