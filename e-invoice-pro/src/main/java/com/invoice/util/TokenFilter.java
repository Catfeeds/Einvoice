package com.invoice.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Shop;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.rest.WxRest;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.ShopService;
import com.invoice.uiservice.service.TaxinfoService;

public class TokenFilter implements Filter {
	private final Log log = LogFactory.getLog(TokenFilter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");

		String url = httpServletRequest.getRequestURI();
		String referer = httpServletRequest.getHeader("referer");

		String mode = httpServletRequest.getParameter("mode");
		if ("test".equalsIgnoreCase(mode)) {
			Token token = new Token();
			token.setEntid("A00001002");
			List<Map<String, Object>> shoplist = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("shopid", "0103");
			map.put("shopname", "测试门店");
			shoplist.add(map);
			token.setTokenid("");
			token.setShoplist(shoplist);
			token.setLoginid("test");
			token.setUsername("测试用户");
			httpServletRequest.getSession(true).setAttribute("token", token);
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		log.info("request url:"+url+"; referer:"+referer);
		//微信入口跳过验证
		//客户端服务入口跳过验证
		//开票调用入口，跳过验证
		if (url.contains("/rest/wx/")||
			url.contains("/client/")||
			url.contains("/openapi/")||
			url.contains("/rest/demo")||
			url.contains("/rest/que/")) {
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		// 判断哪些服务或网页需要模拟登录权限
		if (referer==null || referer.contains("/ui/wx/")) {
			// 来自微信的请求创建一个Token，里的只有entid和loginid信息
			HttpSession session = httpServletRequest.getSession(false);
			if (session == null) {
				RtnData rtn = new RtnData(-1, "请从认证页进入");
				PrintWriter writer = httpServletResponse.getWriter();
				writer.write(rtn.toString());
				writer.flush();
				return;
			} else {
				Token token = Token.getToken();
				if (token == null) {
					RtnData rtn = new RtnData(-1, "请从认证页进入");
					PrintWriter writer = httpServletResponse.getWriter();
					writer.write(rtn.toString());
					writer.flush();
					return;
				}
			}
		} else {
			// 其它的都统一校验登录
			String tokenid = httpServletRequest.getParameter("token");
			String changeshopid = httpServletRequest.getParameter("changeshopid");

			// 先在session查找token信息，找不到则通过portal查找token
			Token token = null;
			HttpSession session = httpServletRequest.getSession(false);
			if (session != null) {
				token = (Token) session.getAttribute("token");
				if((changeshopid==null||"".equals(changeshopid)) && token != null){
					changeshopid = token.getShopid();
				}
				if(token==null) {
					log.info("token is null");
				}else {
					tokenid = tokenid==null?"":tokenid;
					
					if(token.getTokenid()==null)
						token.setTokenid(tokenid);
					
					if(tokenid.equals("null") || StringUtils.isEmpty(tokenid))
						tokenid = token.getTokenid();
					
					log.debug("token is "+JSONObject.toJSONString(token));
				}
			}else {
				log.info("session is null");
			}
			
			if (token == null || 
				!token.getTokenid().equals(tokenid)  || 
				token.getShopid()==null || 
				!token.getShopid().equals(changeshopid)) {
				
				// 通过访问portal的服务的方式找到token对应的值...
				PostMethod postMethod = new PostMethod(FGlobal.portalurl
						+ "rest/ui/main/getUserInfoByToken.action?token=" + tokenid);

				postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
				postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
				postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
				int statusCode = 0;
				HttpClient httpClient = new HttpClient();
				try {
					 
					statusCode = httpClient.executeMethod(postMethod);
				} catch (Exception e) {
					RtnData rtn = new RtnData(-1, "获取token信息异常：" + e.getMessage());
					PrintWriter writer = httpServletResponse.getWriter();
					writer.write(rtn.toString());
					writer.flush();
					return;
				}

				if (statusCode == 200) {
					String tokeninfo = postMethod.getResponseBodyAsString();
					log.debug(tokeninfo);
					if (StringUtils.isEmpty(tokeninfo)) {
						RtnData rtn = new RtnData(-1, "token验证失败");
						PrintWriter writer = httpServletResponse.getWriter();
						writer.write(rtn.toString());
						writer.flush();
						return;
					}

					JSONObject tokenjson = new JSONObject();
					tokenjson = JSONObject.parseObject(tokeninfo);

					if (tokenjson.getString("code").equals("0")) {
						RtnData rtn = new RtnData(-1, "登录超时,请重新登录");
						PrintWriter writer = httpServletResponse.getWriter();
						writer.write(rtn.toString());
						writer.flush();
						return;
					}

					token = tokenjson.toJavaObject(Token.class);
					//获取门店对应的纳税号,目前一个帐号对应一个门店
					try {
						Shop shop=new Shop();
						shop.setEntid(token.getEntid());
						if(token.getShopid() == null || "null".equals(token.getShopid()) || "".equals(token.getShopid())){
							shop.setShopid(token.getShoplist().get(0).get("shopid").toString());
						}else{
							shop.setShopid(token.getShopid());
						}
					
						Shop sh =SpringContextUtil.getBean(ShopService.class).getShopById(shop);
						if(sh != null){
							Taxinfo taxinfo = new Taxinfo();
							taxinfo.setEntid(token.getEntid());
							taxinfo.setTaxno(sh.getTaxno());
							Taxinfo tax = SpringContextUtil.getBean(TaxinfoService.class).getTaxinfoByNo(taxinfo);
								token.setTaxinfo(tax);
								if(token.getKpd()==null||"".equals(token.getKpd())){
									//token.setTaxinfo(tax);
								}else{
									Taxinfo taxinfoitem = new Taxinfo();
									taxinfoitem.setEntid(token.getEntid());
									taxinfoitem.setTaxno(sh.getTaxno());
									taxinfoitem.setItfkpd(token.getKpd());
									try {
										taxinfoitem = SpringContextUtil.getBean(TaxinfoService.class).getTaxinfoItemByNo(taxinfoitem);
										if(taxinfoitem!=null){
											if(taxinfoitem.getTaxmode()==null) taxinfoitem.setTaxmode("");
											if(!"".equals(taxinfoitem.getTaxmode())){
												token.setTaxinfo(taxinfoitem);
											}
										}
									} catch (Exception e) {
										throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
									}
								}
					
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					log.debug("put token to session "+JSONObject.toJSONString(token));
					httpServletRequest.getSession(true).setAttribute("token", token);

				} else if (statusCode == 500) {
					RtnData rtn = new RtnData(-1, "获取token信息返回500");
					PrintWriter writer = httpServletResponse.getWriter();
					writer.write(rtn.toString());
					writer.flush();
					return;
				} else {
					RtnData rtn = new RtnData(-1, "获取token未知状态返回:" + String.valueOf(statusCode));
					PrintWriter writer = httpServletResponse.getWriter();
					writer.write(rtn.toString());
					writer.flush();
					return;
				}
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

	}

}
