package baiwang.invoice.service;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baiwang.baiwangcloud.client.BaiwangCouldAPIClient;
import com.invoice.rtn.data.RtnData;

public class BaiWangClientImpl implements BaiWangClient {
	private static final Logger log = LoggerFactory.getLogger(BaiWangClientImpl.class);
	
	private BaiwangCouldAPIClient client = new BaiwangCouldAPIClient();
	
	/* 登陆百旺方法
	 * serviceUrl 请求路径
	 * user 登陆税号
	 * key 税号对应的接入码
	 * */
	@Override
	public void login(String serviceUrl,String user,String key,RtnData rtn)  {
		try {
			if(serviceUrl==null||"".equals(serviceUrl)){
				rtn.setCode(-1);
				rtn.setMessage("登陆百旺系统 serviceUrl 参数不能为空");
			}else
			if(user==null||"".equals(user)){
				rtn.setCode(-1);
				rtn.setMessage("登陆百旺系统 user 参数不能为空");
			}else
			if(key==null||"".equals(key)){
				rtn.setCode(-1);
				rtn.setMessage("登陆百旺系统 key 参数不能为空");
			}else{
				Date loginStart = new Date();
				
				client.login(serviceUrl, user, key);
				
				Date loginEnd = new Date();
				log.info("login interval  time: " + (loginEnd.getTime() - loginStart.getTime()));
			}
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			log.info("登陆百旺系统出错！");
			e.printStackTrace();
		}
		
	}
	/*访问百旺数据方法
	 * xml 请求报文
	 * return String 返回xml报文
	 * */
	public String rpc(String xml,RtnData rtn) {
		//接收返回的数据
		String rtnXml = null;
		log.info("send data: " +xml);
		try{
			if(xml==null||"".equals(xml)){
				rtn.setCode(-1);
				rtn.setMessage("请求报文不能为空");
			}else{
				Date loginStart = new Date();
				
				rtnXml =client.rpc(xml);
				log.info("return data: " +rtnXml);
				Date loginEnd = new Date();
				log.info("access interval  time: " + (loginEnd.getTime() - loginStart.getTime()));
				
			}
		}catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage("登陆开票系统出错"+e.getMessage()==null?"":e.getMessage());
			log.info("登陆百旺系统出错！");
			e.printStackTrace();
		}
		
		return rtnXml;
	}
	/*退出百旺登陆
	 * */
	public void logout(RtnData rtn){
		try{
			Date loginStart = new Date();
			
			client.logout();
			
			Date loginEnd = new Date();
			log.info("logout time: " + (loginEnd.getTime() - loginStart.getTime()));
		}catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage("退出开票系统出错"+e.getMessage()==null?"":e.getMessage());
			log.info("退出百旺系统出错！");
			e.printStackTrace();
		}
		
	}

	
}
