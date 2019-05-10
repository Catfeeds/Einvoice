package com.invoice.port.nbbwjf.invoice.util;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class RequestUtils {
	
	private static PoolingHttpClientConnectionManager cm =null;
	private static int connectionTimeOut = 600000;
	private static int socketTimeOut = 600000;
	//链接管理器容量，将每个路由的最大连接数增加到200
	private static int maxTotal =100; 
	 //将每个路由的默认最大并发数。超过最大并发数的线程将进入阻塞状态。直到有链接过期或被关闭。
	private static int maxPerRoute = 20;
	
	static{
		//创建链接管理器
		cm=new PoolingHttpClientConnectionManager(); 
	    //链接管理器容量，将每个路由的最大连接数增加到200
	    cm.setMaxTotal(maxTotal);
	    //将每个路由的默认最大并发数。超过最大并发数的线程将进入阻塞状态。直到有链接过期或被关闭。
	    cm.setDefaultMaxPerRoute(maxPerRoute);  
	}
	
	/**
	 * 从连接管理器中获取CloseableHttpClient对象
	 * @return
	 */
	public static CloseableHttpClient getCloseableHttpClient(){
		return HttpClients.custom().setConnectionManager(cm).build();
	}
	
	/**
	 * 以UTF8的方式发送报文。当目标服务器采用UTF8时，需用该方法。否则会导致请求参数乱码，目标服务器无法解析
	 * @param url  连接地址
	 * @param xml   报文
	 * @param decode  响应报文的编码
	 * @return
	 */
	public static String httpPostForXMLUTF8(String url,String xml,String decode){
		String result=null;
		CloseableHttpClient client=getCloseableHttpClient();//创建会话
		HttpPost httppost= new HttpPost(url);
		httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");//设置请求头
		StringEntity entity = new StringEntity(xml,  
                ContentType.create("text/xml", Consts.UTF_8));//定义请求消息实体
		entity.setChunked(true);
		RequestConfig requestConfig = RequestConfig.custom().
        		setSocketTimeout(socketTimeOut).setConnectTimeout(connectionTimeOut).build();//设置连接超时和socket超时时间
		httppost.setConfig(requestConfig);
		httppost.setEntity(entity);
		CloseableHttpResponse response=null;
		try {
			 response=client.execute(httppost);//执行请求
			 HttpEntity ety=response.getEntity();//获取响应实体
			 result=EntityUtils.toString(ety,decode);//由于税控服务器的编码格式为GBK，故需将返回内容按GBK的方式来解码
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(response!=null){
					response.close();//关闭响应流
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
