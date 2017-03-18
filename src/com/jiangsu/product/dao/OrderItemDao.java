package com.jiangsu.product.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.jiangsu.product.domain.Order;
import com.jiangsu.product.util.C3P0Util;
import com.jiangsu.product.util.ManagerThreadLocal;

public class OrderItemDao {
	/**
	 * ��Ӷ�����
	 * @param order
	 * @throws SQLException 
	 */
	public void addOrderItem(Order order) throws SQLException {
		QueryRunner qr = new QueryRunner();
		Object[][] params = new Object[order.getOrderItems().size()][];
		for (int i = 0; i < params.length; i++) {
			//�����еĵ�һ��������������id���ڶ�����������Ʒid�������������ǹ�������
			params[i] = new Object[]{order.getId(),order.getOrderItems().get(i).getP().getId(),order.getOrderItems().get(i).getBuynum()};
		}
		qr.batch(ManagerThreadLocal.getConnection(),"INSERT INTO orderitem VALUES(?,?,?)", params );
	}
}
