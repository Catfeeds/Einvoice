package com.invoice.port.szyipiaoyun.invoice.service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunTokenRequestBean;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunTokenResponseBean;

public class SzYiPiaoYunGenerateBean {
	private Log log = LogFactory.getLog(SzYiPiaoYunGenerateBean.class);
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 7000;

	//获取Token
	public String getToken(String url,String userName,String passWord)
	{
		YiPiaoYunTokenRequestBean tokenBean = new YiPiaoYunTokenRequestBean();
		
		tokenBean.setUsername(userName);
		tokenBean.setPassword(passWord);
		String strJson = tokenBean.getRequestText();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/x-www-form-urlencoded");
		
		String strResult = sendHttpRequest(url,strJson,map);
		
		if (strResult.startsWith("[ERROR]") || strResult.startsWith("[FATAL]")) return "";
		
		if (strResult.indexOf("\"error\":") >= 0)
		{
			YiPiaoYunTokenResponseBean.ErrorBean errorBean = JSON.parseObject(strResult, YiPiaoYunTokenResponseBean.ErrorBean.class);
			
			String strTmp = errorBean.getError() + ":" + errorBean.getError_description();
			
			log.error(strTmp);
			
			return "";
		}
		else
		{
			YiPiaoYunTokenResponseBean tokenResponse = JSON.parseObject(strResult, YiPiaoYunTokenResponseBean.class);
			
			if (tokenResponse == null) 
				return "";
			else
				return tokenResponse.getToken_type() + " " + tokenResponse.getAccess_token();
		}
	}

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(300);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
		
		// 设置配置信息
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		requestConfig = configBuilder.build();
	}

	public String sendHttpRequest(String url, String xmlRequest, Map<String, String> map) {
		String responseContent = "";
		HttpPost httpPost = null;
		HttpClient httpClient = null;
		
		try 
	    {	
			if (url.startsWith("https")) 
			{
				httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
			} 
			else 
			{
				httpClient = HttpClients.createDefault();
			}

			log.info("Request Text: " + xmlRequest);
			
			//实例化httpPost
			httpPost = new HttpPost(url);
			
			for (String key : map.keySet()) 
			{
				httpPost.setHeader(key, map.get(key));
			}
			
			//数进行编码，防止中文乱码
			StringEntity reqEntity = new StringEntity(xmlRequest,Charset.forName("UTF-8"));
			reqEntity.setContentEncoding("UTF-8");
			
			//设置请求体
			httpPost.setEntity(reqEntity);
			
			//发送请求
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				responseContent = EntityUtils.toString(resEntity, "UTF-8");
				log.info("Response Text: " + responseContent);
			}
			else 
			{
				responseContent = "[ERROR]" + "Returns Empty Data!";
				log.error("Response Text: " + responseContent);
			}
			
			return responseContent;
	    }
	    catch (Exception ex) {
	    	log.error(ex.getMessage());
	    	
	    	return "[FATAL]" + ex.getMessage();
	    }
	    finally 
	    {
	    	if (httpPost != null) httpPost.releaseConnection(); 
	    	
	    	httpPost = null;
	    	httpClient = null;
	    }
	}

	private SSLConnectionSocketFactory createSSLConnSocketFactory() throws Exception 
	{
		SSLConnectionSocketFactory sslsf = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null).build();
			  
			sslsf = new SSLConnectionSocketFactory(sslContext);
		} 
		catch (Exception ex) {
			log.error(ex.getMessage());
			
			throw new Exception(ex);
		}

		return sslsf;
	}
}
