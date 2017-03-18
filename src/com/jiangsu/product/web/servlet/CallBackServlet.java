package com.jiangsu.product.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.OSEnvironment;

import com.jiangsu.product.exception.OrderException;
import com.jiangsu.product.service.OrderService;
import com.jiangsu.product.util.PaymentUtil;

public class CallBackServlet extends HttpServlet {

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
		response.setContentType("text/html;charset=UTF-8");
		//处理用户订单，支付返回数据
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");  //支付结果，1表示成功
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");  //商户订单号
		String r7_Uid = request.getParameter("r7_Uid"); //客户的在易宝的id
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String hmac = request.getParameter("hmac");
		
		boolean paySuccess = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
		if (!paySuccess) {
			response.getWriter().print("支付数据异常！");
		}else {
			//修改订单状态
			if ("1".equals(r1_Code)) {
				if ("2".equals(r9_BType)) {
					response.getWriter().print("success");
				}
				response.getWriter().print("支付成功，等待发货！");
				//修改订单状态
				OrderService orderService = new OrderService();
				try {
					orderService.midifyOrderState(r6_Order);
				} catch (OrderException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				
				//重定向
				response.sendRedirect(request.getContextPath()+"/paysuccess.jsp");
			}
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
