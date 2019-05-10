package com.invoice.port.sztechweb.invoice.service;

import java.nio.charset.Charset;
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

public class SzTechWebGenerateImpl {
    private Log log = LogFactory.getLog(SzTechWebGenerateImpl.class);
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 30000;

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

	public String sendHttpRequest(String url, String xmlRequest) {
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
			
			httpPost.setHeader("Accept", "application/json;charset=UTF-8");
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			
			//进行编码，防止中文乱码
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
