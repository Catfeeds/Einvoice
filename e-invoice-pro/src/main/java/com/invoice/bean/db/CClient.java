package com.invoice.bean.db;import java.util.HashMap;import java.util.Map;import org.apache.commons.logging.Log;import org.apache.commons.logging.LogFactory;import org.springframework.util.StringUtils;import com.alibaba.fastjson.JSONObject;import com.invoice.util.DESedeAPIUtil;import com.invoice.util.HttpClientCommon;import com.invoice.util.SHA1;/******************************************************************************* * javaBeans c_client --> CClient <table explanation> *  * @author 2017-07-27 15:48:48 *  */public class CClient implements java.io.Serializable {	static public final Log log = LogFactory.getLog(CClient.class);	private Map<String, String> headMap;		public void initHeadMap() {		if(headMap==null) {			headMap =  new HashMap<String, String>();		}		headMap.clear();		String time = System.currentTimeMillis() + "";		String passwordSC = SHA1.sha1(entid + clientid + password + time);		headMap.put("entid", entid);		headMap.put("clientid", clientid);		headMap.put("password", passwordSC);		headMap.put("time", time);	}		public String getMessage(String action) {		String res = HttpClientCommon.post(null, headMap, url + "/rest/api/"+action, 0, 0, "utf-8");		return cookMessage(res);	}	public String cookMessage(String res) {		if (StringUtils.isEmpty(res)) {			throw new RuntimeException("获取数据为空:" + clientid);		}				// 解密		try {			res = DESedeAPIUtil.decodeCBC(clientid, res);		} catch (Exception e) {			// 尝试直接读取			JSONObject r = JSONObject.parseObject(res);			String msg = r.getString("message");			if (msg == null && r.containsKey("msg")) {				msg = r.getString("msg");			}			if (r.getIntValue("code") != 0) {				log.error("获取数据异常:" + clientid + ",MSG:" + msg);			} else {				log.error("解密失败：" + res);			}		}		log.info("解密数据：" + res);		return res;	}	// field	/**  **/	private String entid;	/**  **/	private String clientid;	/**  **/	private String clientname;	/**  **/	private String mac;	/**  **/	private String ip;	/**  **/	private String password;	/** 客户端服务地址 **/	private String url;	// method	public String getEntid() {		return entid;	}	public void setEntid(String entid) {		this.entid = entid;	}	public String getClientid() {		return clientid;	}	public void setClientid(String clientid) {		this.clientid = clientid;	}	public String getClientname() {		return clientname;	}	public void setClientname(String clientname) {		this.clientname = clientname;	}	public String getMac() {		return mac;	}	public void setMac(String mac) {		this.mac = mac;	}	public String getIp() {		return ip;	}	public void setIp(String ip) {		this.ip = ip;	}	public String getPassword() {		return password;	}	public void setPassword(String password) {		this.password = password;	}	public String getUrl() {		return url;	}	public void setUrl(String url) {		this.url = url;	}	public Map<String, String> getHeadMap() {		return headMap;	}	public void setHeadMap(Map<String, String> headMap) {		this.headMap = headMap;	}}