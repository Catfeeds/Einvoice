package com.invoice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author wanghuajun
 * 
 */

@Component
public class HttpClientCommon {

	private static Logger mLogger = Logger.getLogger(HttpClientCommon.class);

	public static Map<String, String> headMapUtf8 = new HashMap<String, String>();
	public static Map<String, String> headMapGBK = new HashMap<String, String>();
	public static String UTF = "UTF-8";
	public static String GBK = "GBK";
	private static String APPLICATION_JSON = "application/json";

	static {
		headMapUtf8.put("Content", "text/html,charset=utf-8");
		headMapUtf8.put("user-agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");
		headMapUtf8.put("Cache-Control", "no-cache");
		headMapUtf8.put("If-Modified-Since", "Thu, 29 Jul 2004 02:24:49 GMT");
	}

	static {
		headMapGBK.put("Content", "text/html,charset=GBK");
		headMapGBK.put("user-agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)");
		headMapGBK.put("Cache-Control", "no-cache");
		headMapGBK.put("If-Modified-Since", "Thu, 29 Jul 2004 02:24:49 GMT");
	}

	public static String doPost(Map<String, Object> paramMap, Map<String, String> headMap, String url, int connTimeOut,
			int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}
		String backString = "";
		HttpClient httpClient = null;
		PostMethod method = null;
		try {
			httpClient = new HttpClient();
			method = new PostMethod(url);

			for (Entry<String, Object> entry : paramMap.entrySet()) {
				((PostMethod) method).addParameter(entry.getKey(), entry.getValue() + "");
			}
			HttpMethodParams param = method.getParams();
			param.setSoTimeout(60000);
			param.setContentCharset(charset);

			int status = httpClient.executeMethod(method);
			if (status == 200) {
				// log.info("[请求接口" + url + "返回状态]status=" + status
				// + "[status=200请求成功]");

				backString = method.getResponseBodyAsString();
			} else if (status == 500) {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				return json.toString();
			} else {
				JSONObject json = new JSONObject();
				json.put("status", status);
				return json.toString();
			}
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			method.releaseConnection();
			method = null;
			httpClient = null;
		}
		// System.out.println("backString=" + backString);
		return backString;
	}

	public static String doGet(Map<String, Object> paramMap, Map<String, String> headMap, String url, int connTimeOut,
			int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}
		// HttpConnectionManager httpManager = null;
		String backString = "";
		GetMethod method = null;
		try {
			HttpClient httpClient = new HttpClient();
			method = new GetMethod(url);
			HttpMethodParams httpParam = method.getParams();

			// 设置链接超时时间
			httpParam.setSoTimeout(60000);
			httpParam.setContentCharset(charset);

			int status = httpClient.executeMethod(method);

			if (status == 200) {
				// log.info("[请求接口" + url + "返回状态]status=" + status
				// + "[status=200请求成功]");

				backString = method.getResponseBodyAsString();
			} else {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				// log.info("[请求接口" + url + "返回状态]status=" + status +
				// "[status=]"
				// + status + "请求成功]");
				return json.toString();
			}

			//

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// 释放连接
			if (method != null) {
				try {
					method.releaseConnection();
				} catch (Exception e) {
				}
			}
		}
		// System.out.println("========" + backString);
		return backString;
	}

	/**
	 * @Title:getPostParam
	 * @Description:设置请求头
	 * @param paramMap
	 * @return HttpMethodParams
	 */
	private static HttpMethodBase setPostHead(HttpMethodBase method, Map<String, String> paramMap) {
		if (paramMap != null && paramMap.size() > 0) {
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				method.setRequestHeader(entry.getKey(), entry.getValue());
			}
		}

		return method;
	}

	/**
	 * @Title:setQueryPair
	 * @Description:设置参数
	 * @param method
	 * @param paramMap
	 * @return EntityEnclosingMethod
	 * @author kyj
	 * @date Sep 28, 2012
	 */
	protected static HttpMethodBase setQueryPair(HttpMethodBase method, Map<String, Object> paramMap, String charset) {
		if (paramMap != null && paramMap.size() > 0) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				if (entry.getKey() != null) {
					NameValuePair name = new NameValuePair();
					name.setName(entry.getKey());
					try {
						name.setValue(entry.getValue() != null ? entry.getValue().toString() : "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(name);
				}
			}

			if (list != null && list.size() > 0) {
				NameValuePair[] pairArr = new NameValuePair[list.size()];

				pairArr = list.toArray(pairArr);
				method.setQueryString(EncodingUtil.formUrlEncode(pairArr, charset));
			}

		}

		return method;
	}

	public static String doPostStream(String json, Map<String, String> headMap, String url, int connTimeOut,
			int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 60 * 1000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 60 * 1000;
		}
		HttpConnectionManager httpManager = null;

		StringBuffer backString = new StringBuffer();
		PostMethod method = null;
		try {
			HttpClient httpClient = new HttpClient();
			method = new PostMethod(url);
			httpManager = new SimpleHttpConnectionManager();
			HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

			// 设置链接超时时间
			httpParam.setConnectionTimeout(connTimeOut);
			// 设置访问超时时间
			httpParam.setSoTimeout(soTimeOut);
			httpManager.setParams(httpParam);
			// 设置http访问管理机制
			httpClient.setHttpConnectionManager(httpManager);

			// 设置post请求的请求头
			method = (PostMethod) setPostHead(method, headMap);

			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);

			// 设置请求的参数
			try {
				RequestEntity requestEntity = new StringRequestEntity(json, APPLICATION_JSON, charset);
				method.setRequestEntity(requestEntity);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// 设置http的重复访问机制
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(0, false));

			int status = httpClient.executeMethod(method);

			if (status == 200) {
				// log.info("[请求接口" + url + "返回状态]status=" + status
				// + "[status=200请求成功]");

				InputStream backInputStream = method.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(backInputStream, charset));
				String str = null;
				while ((str = br.readLine()) != null) {
					backString.append(str);
				}
			} else if (status == 500) {
				JSONObject j = new JSONObject();
				j.put("status", 500);
				return j.toString();
			}
			//

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
			httpManager.closeIdleConnections(0);
			method = null;
			httpManager = null;
		}

		return backString.toString();
	}

	static public String postFile(String name, File file, String url, int connTimeOut, int soTimeOut, String charset) {
		if (StringUtils.isEmpty(charset)) {
			charset = "utf-8";
		}
		if (connTimeOut == 0) {
			connTimeOut = 60 * 1000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 60 * 1000;
		}

		HttpConnectionManager httpManager = null;

		StringBuffer backString = new StringBuffer();
		PostMethod method = null;
		try {
			HttpClient httpClient = new HttpClient();
			method = new PostMethod(url);
			httpManager = new SimpleHttpConnectionManager();
			HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

			// 设置链接超时时间
			httpParam.setConnectionTimeout(connTimeOut);
			// 设置访问超时时间
			httpParam.setSoTimeout(soTimeOut);
			httpManager.setParams(httpParam);
			// 设置http访问管理机制
			httpClient.setHttpConnectionManager(httpManager);

			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);

			// 设置请求的参数
			try {
				FilePart fp = new FilePart(name, file, "application/pdf", charset);
				Part[] parts = { fp };
				MultipartRequestEntity entity = new MultipartRequestEntity(parts, new HttpMethodParams());
				method.setRequestEntity(entity);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// 设置http的重复访问机制
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(0, false));

			int status = httpClient.executeMethod(method);

			if (status == 200) {
				// log.info("[请求接口" + url + "返回状态]status=" + status
				// + "[status=200请求成功]");

				InputStream is = method.getResponseBodyAsStream();
				
				FileOutputStream fos = new FileOutputStream(file); 
				byte[] buffer = new byte[4096];
		        int len = -1;
		        while((len = is.read(buffer) )!= -1){
		            fos.write(buffer, 0, len);
		        }
		        fos.close();
		        is.close();
			} else if (status == 500) {
				JSONObject j = new JSONObject();
				j.put("status", 500);
				return j.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
			httpManager.closeIdleConnections(0);
			method = null;
			httpManager = null;
		}

		return backString.toString();
	}

	public static String getFile(File file, String url, int connTimeOut, int soTimeOut, String charset) {
		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}
		// HttpConnectionManager httpManager = null;
		String backString = "";
		GetMethod method = null;
		try {
			HttpClient httpClient = new HttpClient();
			method = new GetMethod(url);
			HttpMethodParams httpParam = method.getParams();

			// 设置链接超时时间
			httpParam.setSoTimeout(60000);
			httpParam.setContentCharset(charset);

			int status = httpClient.executeMethod(method);

			if (status == 200) {
				InputStream backInputStream = method.getResponseBodyAsStream();
			} else {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				// log.info("[请求接口" + url + "返回状态]status=" + status +
				// "[status=]"
				// + status + "请求成功]");
				return json.toString();
			}

			//

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// 释放连接
			if (method != null) {
				try {
					method.releaseConnection();
				} catch (Exception e) {
				}
			}
		}
		// System.out.println("========" + backString);
		return backString;
	}

	public static String post(Map<String, Object> paramMap, Map<String, String> headMap, String url, int connTimeOut,
			int soTimeOut, String charset) {

		if (connTimeOut == 0) {
			connTimeOut = 10 * 3000;
		}

		if (soTimeOut == 0) {
			soTimeOut = 20 * 3000;
		}

		HttpConnectionManager httpManager = null;
		String backString = "";
		PostMethod method = null;
		StringBuffer info = new StringBuffer();
		try {
			HttpClient httpClient = new HttpClient();
			method = new PostMethod(url);
			httpManager = new SimpleHttpConnectionManager();
			HttpConnectionManagerParams httpParam = new HttpConnectionManagerParams();

			// 设置链接超时时间
			httpParam.setConnectionTimeout(connTimeOut);
			// 设置访问超时时间
			httpParam.setSoTimeout(soTimeOut);
			httpManager.setParams(httpParam);
			// 设置http访问管理机制
			httpClient.setHttpConnectionManager(httpManager);

			// 设置post请求的请求头
			method = (PostMethod) HttpClientCommon.setPostHead(method, headMap);
			// 设置请求的参数
			method = (PostMethod) setQueryPair(method, paramMap, charset);
			// 设置http的重复访问机制
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(0, false));

			int status = httpClient.executeMethod(method);

			if (status == 200) {
				backString = method.getResponseBodyAsString();
				info.append("[请求成功][请求接口:").append(url).append("][请求参数:").append(getParamMap(paramMap)).append("]");
				info.append("[返回结果:").append(backString).append("]");
				mLogger.info(info);
			} else {
				info.append("[请求#失败,Http状态:" + status + "#][请求接口:").append(url).append("][请求参数:")
						.append(getParamMap(paramMap)).append("]");
				mLogger.info(info);
				backString = "{\"code\":1,\"msg\":\"处理异常" + status + "\"}";
				return backString;
			}

		} catch (Exception e) {
			info.append("[请求异常#][请求接口:").append(url).append("][请求参数:").append(getParamMap(paramMap)).append("]");
			e.printStackTrace();
			mLogger.error(info, e);
			backString = "{\"code\":1,\"msg\":\"处理异常" + e.getMessage() + "\"}";
			return backString;
		} finally {
			try {
				if (method != null) {
					method.releaseConnection();
				}
			} catch (Exception e) {
			}

			try {
				if (httpManager != null) {
					httpManager.closeIdleConnections(0);
				}
			} catch (Exception e) {
			}
			method = null;
			httpManager = null;
		}
		return backString;
	}

	public static StringBuffer getParamMap(Map<String, Object> paramMap) {

		StringBuffer param = new StringBuffer();
		if (paramMap == null)
			return param;

		Set<String> keys = paramMap.keySet();
		for (String key : keys) {
			String value = String.valueOf(paramMap.get(key));
			param.append(key).append("=").append(value).append(";");
		}
		return param;
	}
}
