/*
 * Copyright (C), 1996-2015
 * FileName: TokenFilter.java
 * Author:   王华君
 * Date:     Feb 9, 2015 3:35:44 PM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.efuture.portal.beans.User;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TokenFilter implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse
				.setHeader("Content-type", "text/html;charset=UTF-8");
		 
		String url = httpServletRequest.getRequestURI();
		
		// 调试模式
		if ("test".equals(httpServletRequest.getParameter("mode"))) {
			User user = new User();
			user.setWxopenid("oJKy6s2_y1MW_g8Xuy78VlvfdqZ0");
			user.setCompanyid("3");
			user.setWxcode("123");
			user.setCrmflag("0");
			user.setLoginid("fzc");
			user.setShopid("A00A");
			user.setCardno("61062280982");
			user.setCompanyname("富基融通");
			user.setPartnerid("333");
			user.setPartner(true);
			httpServletRequest.getSession().setAttribute("user", user);
			
			user = new User();
			user.setWxopenid("oJKy6s2_y1MW_g8Xuy78VlvfdqZ0");
			user.setCompanyid("3");
			user.setWxcode("123");
			user.setCrmflag("0");
			user.setLoginid("fzc");
			user.setShopid("A00A");
			user.setCardno("333");
			user.setCompanyname("富基融通");
			user.setPartnerid("333");
			user.setPartner(true);
			httpServletRequest.getSession().setAttribute(
					Constant.MERCHANT_USER, user);

			chain.doFilter(httpServletRequest, httpServletResponse);

			return;
		}	
		
		if (url.indexOf("modifyPassword.html") > 0
				|| url.indexOf("login.html") > 0
				|| url.indexOf("storelogin.html") > 0
				|| url.indexOf("myDialogDemo.html") > 0
				|| url.indexOf("DialogDemo.html") > 0
				|| url.indexOf("registerUserModule.action") > 0
				|| url.indexOf("registerShop.action") > 0
				|| url.indexOf("findEnterpriseRegister.action") > 0
				|| url.indexOf("weixin/") > 0) {
			// 登录页面跳过
		} else if (url.indexOf("druid") > 0) {
		} else {
			String token = httpServletRequest.getParameter("token");

			if (StringUtils.isEmpty(token)) {
				PrintWriter writer = httpServletResponse.getWriter();
				writer.write("token 不能为空..");
				writer.flush();
				return;
			}

			User user = (User) httpServletRequest.getSession().getAttribute("user");

			// gzt 已经实现token控制了,暂时注释掉
			if (StringUtils.isEmpty(user) || !user.getToken().equals(token)) {
				PrintWriter writer = httpServletResponse.getWriter();
				writer.write("token 失效..");
				writer.flush();
				return;
			}
		}

		chain.doFilter(httpServletRequest, httpServletResponse);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
