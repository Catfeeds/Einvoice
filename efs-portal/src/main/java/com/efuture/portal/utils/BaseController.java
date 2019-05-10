/*
 * Copyright (C), 1996-2015
 * FileName: BaseController.java
 * Author:   王华君
 * Date:     Feb 9, 2015 4:36:06 PM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.xml.sax.InputSource;

import com.efuture.portal.beans.User;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
@Controller
public class BaseController {

	private final static String USER = "user";
	private final static String WXOpenID = "wxopenid";

	@Resource
	HttpServletRequest request;

	public User getUser() {
		request.getSession().setMaxInactiveInterval(-1);
		User user = (User) (StringUtils.isEmpty(request) ? null : request
				.getSession().getAttribute(USER));
		return user;
	}

	public void setUser(User user) {
		request.getSession().setAttribute(USER, user);
	}

	public void putMobileVerify(String verifycode, String verify_prefix) {
		request.getSession().setAttribute(
				verify_prefix + Constant.MOBILE_VERIFY, verifycode);
	}

	public String getMobileVerify(String verify_prefix) {
		String key = verify_prefix + Constant.MOBILE_VERIFY;
		String temp = request.getSession().getAttribute(key) + "";
		request.getSession().setAttribute(key, "");
		return temp;
	}

	public Map<String, Object> getData() {
		Map<String, Object> tmap1 = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream(), Constant.UTF8));
			String ksk = null;
			while ((ksk = br.readLine()) != null) {
				sb.append(ksk);
			}
			if (sb.toString().startsWith("<")) {
				parseMsgForNotice(sb.toString(), tmap1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set set = request.getParameterMap().entrySet();
		for (Object item : set) {
			Entry entry = (Entry) item;
			String value = "";
			Object _value = entry.getValue();
			if (_value instanceof String[]) {
				value = ((String[]) _value)[0];
			} else {
				value = _value + "";
			}
			String key = (entry.getKey() + "").toLowerCase();

			if ("data".equals(key)) {
				try {
					tmap1.putAll((Map<String, Object>) JSONValue
							.parseWithException(value));
				} catch (ParseException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				continue;
			}

			tmap1.put(key, value);
		}
//		FLog.getInstance().addObject(tmap1).setDelay(1).log();
		return tmap1;
	}

	public void parseMsgForNotice(String msg, Map<String, Object> param)
			throws Exception {
		StringReader read = new StringReader(msg);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder builder = new SAXBuilder(false);
		// 通过输入源构造一个Document
		Document doc = builder.build(source);

		Element xml = doc.getRootElement();

		for (Object item : xml.getChildren()) {
			Element els = (Element) item;
			param.put(els.getName().toLowerCase(), els.getValue());
		}
	}

	public static void main(String[] args) throws Exception {
		Object obj = "";
		System.out.println(obj instanceof String[]);
	}

	public User getMerchantUser() {
		request.getSession().setMaxInactiveInterval(-1);
		User user = (User) (StringUtils.isEmpty(request) ? null : request
				.getSession().getAttribute(Constant.MERCHANT_USER));
		return user;
	}

	public String getVerifyCode() {
		request.getSession().setMaxInactiveInterval(-1);
		Object obj = request.getSession().getAttribute(Constant.VERIFYCODE);
		String verifycode = obj == null ? "" : obj.toString();
		request.getSession().setAttribute(Constant.VERIFYCODE,"");
		return verifycode;
	}
}
