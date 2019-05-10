package baiwang.invoice.service;

import com.invoice.rtn.data.RtnData;

public interface BaiWangClient {
	/* 登陆百旺方法
	 * serviceUrl 请求路径
	 * user 登陆税号
	 * key 税号对应的接入码
	 * */
	public void login(String serviceUrl,String user,String key,RtnData rtn);
	/*访问百旺数据方法
	 * xml 请求报文
	 * return String 返回xml报文
	 * */
	public String rpc(String xml,RtnData rtn);
	/*退出百旺登陆
	 * */
	public void logout(RtnData rtn);
	
}
