package com.jiangsu.product.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiangsu.product.domain.User;

public class RoleFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//强转
		HttpServletRequest request2 = (HttpServletRequest)request;
		HttpServletResponse response2 = (HttpServletResponse)response;
		//处理业务
		User user = (User)request2.getSession().getAttribute("user");
		if (user!=null) {
			if (!"admin".equals(user.getRole())) {
				response.getWriter().write("权限不足，无法访问！");
				response2.setHeader("refresh", "2,url="+request2.getContextPath()+"/index.jsp");
				return;
			}
			//放行
			chain.doFilter(request2, response2);
		}
		response2.sendRedirect(request2.getContextPath()+"/login.jsp");
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


}
