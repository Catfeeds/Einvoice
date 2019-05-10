package com.efuture.portal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.RegisterBean;
import com.efuture.portal.beans.RegisterLogBean;
import com.efuture.portal.dao.db.one.RegisterDao;
import com.efuture.portal.service.RegisterService;
import com.efuture.portal.utils.AES;
import com.efuture.portal.utils.SHA1;

@Service("RegisterService")
public class RegisterServiceImpl implements RegisterService{
	
	@Resource(name = "RegisterDao")
	RegisterDao registerDao;

	
	public String findEnterpriseRegister(String entid,String userid){
		
		RegisterBean bean = new RegisterBean();
		bean = registerDao.findEnterpriseRegister(entid);
		JSONObject rejson = new JSONObject();
		try{
			
			if(bean==null){
				rejson.put("code", "-1");
				rejson.put("msg", "没有查询到企业注册信息");
				return null;
			}
			
			if(bean.getRegisterid()==null||"".endsWith(bean.getRegisterid())){
				
				rejson.put("code", "-1");
				rejson.put("msg", "没有查询到企业注册信息");
				return null;
			}
			
			byte[] rtsecret = AES.parseHexStr2Byte(bean.getRegisterid());
			
			byte[] ret = AES.decrypt(rtsecret, SHA1.sha1(entid));
			
			String rtRegister = new String(ret);
			
			if(rtRegister==null||"".equals(rtRegister)){
				rejson.put("code", "-1");
				rejson.put("msg", "没有查询到企业注册信息");
				return null;
				
			}
			else
			{
				
				String[] rtRegisterarray =  rtRegister.split(",");
				
				if(rtRegisterarray.length==5){
					
					if(rtRegisterarray[0]==null||"".equals(rtRegisterarray[0])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业ID号为空!");
						return null;
					}else{
						bean.setEntid(rtRegisterarray[0]);
					}
					
					if(rtRegisterarray[1]==null||"".equals(rtRegisterarray[1])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到规则为空!");
						return null;
					}else{
						bean.setRule(rtRegisterarray[1]);

					}
					
					if(rtRegisterarray[2]==null||"".equals(rtRegisterarray[2])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业纳税号数量为空!");
						return null;
					}else{
						bean.setTaxnonum(Integer.parseInt(rtRegisterarray[2]));

					}
					
					if(rtRegisterarray[3]==null||"".equals(rtRegisterarray[3])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业门店数量为空!");
						return null;
					}else{
						bean.setShopnum(Integer.parseInt(rtRegisterarray[3]));

					}
					
					if(rtRegisterarray[4]==null||"".equals(rtRegisterarray[4])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业使用截止日期为空!");
						return null;
					}else{
												
						bean.setEnddate(rtRegisterarray[4]);

					}
					
				}else{
					rejson.put("code", "-1");
					rejson.put("msg", "查询企业注册信息里面的字段内容不全!");
					return null;
				}
				//System.out.println(bean.toString());	
				rejson.put("code", "1");
				rejson.put("msg", "sucess");
				rejson.put("data", JSONObject.toJSON(bean));
				//向数据库记录日志
				RegisterLogBean logBean = new RegisterLogBean();
				logBean.setEntid(bean.getEntid());
				logBean.setFlag("S");
				logBean.setRegisterid(bean.getRegisterid());
				logBean.setSdate(bean.getSdate());
				logBean.setUserid(userid);
				registerDao.insertRegisterLog(logBean);
				
				
			}
			
		}catch(Exception e){
			rejson.put("code", "-1");
			rejson.put("msg", "查询企业注册信息出现异常"); 
			e.printStackTrace();
		}

		return rejson.toString();
	}
	
	public RegisterBean findEnterpriseRegister(String entid){
		
		RegisterBean bean = new RegisterBean();
		bean = registerDao.findEnterpriseRegister(entid);
		JSONObject rejson = new JSONObject();
		try{
			
			if(bean==null){
				rejson.put("code", "-1");
				rejson.put("msg", "没有查询到企业注册信息");
				return null;
			}
			
			if(bean.getRegisterid()==null||"".endsWith(bean.getRegisterid())){
				
				rejson.put("code", "-1");
				rejson.put("msg", "没有查询到企业注册信息");
				return null;
			}
			
			byte[] rtsecret = AES.parseHexStr2Byte(bean.getRegisterid());
			
			byte[] ret = AES.decrypt(rtsecret, SHA1.sha1(entid));
			
			String rtRegister = new String(ret);
			
			if(rtRegister==null||"".equals(rtRegister)){
				rejson.put("code", "-1");
				rejson.put("msg", "没有查询到企业注册信息");
				return null;
				
			}
			else
			{
				
				String[] rtRegisterarray =  rtRegister.split(",");
				
				if(rtRegisterarray.length==5){
					
					if(rtRegisterarray[0]==null||"".equals(rtRegisterarray[0])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业ID号为空!");
						return null;
					}else{
						bean.setEntid(rtRegisterarray[0]);
					}
					
					if(rtRegisterarray[1]==null||"".equals(rtRegisterarray[1])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到规则为空!");
						return null;
					}else{
						bean.setRule(rtRegisterarray[1]);

					}
					
					if(rtRegisterarray[2]==null||"".equals(rtRegisterarray[2])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业纳税号数量为空!");
						return null;
					}else{
						bean.setTaxnonum(Integer.parseInt(rtRegisterarray[2]));

					}
					
					if(rtRegisterarray[3]==null||"".equals(rtRegisterarray[3])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业门店数量为空!");
						return null;
					}else{
						bean.setShopnum(Integer.parseInt(rtRegisterarray[3]));

					}
					
					if(rtRegisterarray[4]==null||"".equals(rtRegisterarray[4])){
						rejson.put("code", "-1");
						rejson.put("msg", "查询到企业使用截止日期为空!");
						return null;
					}else{
												
						bean.setEnddate(rtRegisterarray[4]);

					}
					
				}else{
					rejson.put("code", "-1");
					rejson.put("msg", "查询企业注册信息里面的字段内容不全!");
					return null;
				}
				
				//System.out.println(bean.toString());	
				rejson.put("code", "1");
				rejson.put("msg", "sucess");
				rejson.put("data", JSONObject.toJSON(bean));
				//向数据库记录日志
	/*			RegisterLogBean logBean = new RegisterLogBean();
				logBean.setEntid(bean.getEntid());
				logBean.setFlag("S");
				logBean.setRegisterid(bean.getRegisterid());
				logBean.setSdate(bean.getSdate());
				logBean.setUserid(userid);
				registerDao.insertRegisterLog(logBean);*/
				
				
			}
			
		}catch(Exception e){
			rejson.put("code", "-1");
			rejson.put("msg", "查询企业注册信息出现异常"); 
			e.printStackTrace();
		}
			
		return bean;
	}
	
	public void insertEnterpriseRegister(RegisterBean bean,String userid,JSONObject rejson){
		
		try{
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
			
			if(bean.getEntid()==null||"".equals(bean.getEntid())){
				rejson.put("code", "-1");
				rejson.put("msg", "企业ID信息出现异常"); 
				return;
			}
			else
			if(bean.getRule()==null||"".equals(bean.getRule())){
				rejson.put("code", "-1");
				rejson.put("msg", "企业规则信息出现异常");
				return;
			}else{
				
			int count =	registerDao.findcompanyByentid(bean.getEntid());
			
			
				if(count>0){
					
				
					if(bean.getRule().length()==3){
						
						if("0".equals(bean.getRule().substring(0,1))){
							bean.setTaxnonum(99999999);
						}else{
							if(bean.getTaxnonum()==0){
								rejson.put("code", "-1");
								rejson.put("msg", "企业税号数量不能为空");
								return;
							}
						}
						
						if("0".equals(bean.getRule().substring(1,2))){
							bean.setShopnum(99999999);
						}else{
							if(bean.getShopnum()==0){
								rejson.put("code", "-1");
								rejson.put("msg", "企业门店数量不能为空");
								return;
							}
						}
						
						if("0".equals(bean.getRule().substring(2))){
							bean.setEnddate("9999-12-31");
						}else{
							if(bean.getEnddate()==null||"".equals(bean.getEnddate())){
								rejson.put("code", "-1");
								rejson.put("msg", "企业有效期不能为空");
								return;
							}
						}
	
					}else{
						rejson.put("code", "-1");
						rejson.put("msg", "企业规则信息格式由问题");
						return;
					}
					
				
					//System.out.println(SHA1.sha1(bean.getEntid()));
					String str =bean.getEntid()+","+bean.getRule()+","+bean.getTaxnonum()+","+bean.getShopnum()+","+bean.getEnddate();
					byte[] secret = AES.encrypt(str,SHA1.sha1(bean.getEntid()));
					String hexStr =AES.parseByte2HexStr(secret);
					//System.out.println(hexStr);
					if(hexStr==null||"".equals(hexStr)){
						rejson.put("code", "-1");
						rejson.put("msg", "生成的数据有问题");
						return;
					}else{
						bean.setRegisterid(hexStr);
					}
					
					RegisterBean regBean ;
					regBean = registerDao.findEnterpriseRegister(bean.getEntid());
					//向数据库记录日志
					if(regBean ==null )  regBean = new RegisterBean();
					RegisterLogBean logBean = new RegisterLogBean();
					logBean.setEntid(bean.getEntid());
					logBean.setFlag("I");
					logBean.setNewregisterid(hexStr);
					logBean.setRegisterid(regBean.getRegisterid()==null?"":regBean.getRegisterid());
					logBean.setSdate(regBean.getSdate()==null?sdf.format(new Date()):regBean.getSdate());
					logBean.setUserid(userid);
					registerDao.insertRegisterLog(logBean);
					
					
				    registerDao.deleteEnterpriseRegister(bean.getEntid());
					
					int i = registerDao.insertEnterpriseRegister(bean);
					if(i>0){
						rejson.put("code", "1");
						rejson.put("msg", "保存数据成功");
					}
					
				}else{
					rejson.put("code", "-1");
					rejson.put("msg", "没有查询到ID号为"+bean.getEntid()+"相关企业信息");
					return;
				}
				
			}
			//System.out.println(bean.toString());
			
		}catch(Exception e){
			
			rejson.put("code", "-1");
			rejson.put("msg", e.getMessage());
			
			e.printStackTrace();
		}
		
	}
	
	public void updateEnterpriseRegister(RegisterBean bean,JSONObject rejson){
		
		try{

			if(bean.getEntid()==null||"".equals(bean.getEntid())){
				rejson.put("code", "-1");
				rejson.put("msg", "企业ID信息出现异常"); 
				return;
			}
			else
			if(bean.getRule()==null||"".equals(bean.getRule())){
				rejson.put("code", "-1");
				rejson.put("msg", "企业规则信息出现异常");
				return;
			}else{
				
				if(bean.getRule().length()==3){
					
					if("0".equals(bean.getRule().substring(0,1))){
						bean.setTaxnonum(99999999);
					}else{
						if(bean.getTaxnonum()==0){
							rejson.put("code", "-1");
							rejson.put("msg", "企业税号数量不能为空");
							return;
						}
					}
					
					if("0".equals(bean.getRule().substring(1,2))){
						bean.setShopnum(99999999);
					}else{
						if(bean.getShopnum()==0){
							rejson.put("code", "-1");
							rejson.put("msg", "企业门店数量不能为空");
							return;
						}
					}
					
					if("0".equals(bean.getRule().substring(2))){
						bean.setEnddate("9999-12-31");
					}else{
						if(bean.getEnddate()==null||"".equals(bean.getEnddate())){
							rejson.put("code", "-1");
							rejson.put("msg", "企业有效期不能为空");
							return;
						}
					}

				}else{
					rejson.put("code", "-1");
					rejson.put("msg", "企业规则信息格式由问题");
					return;
				}
				
			
				 
				String str =bean.getEntid()+","+bean.getRule()+","+bean.getTaxnonum()+","+bean.getShopnum()+","+bean.getEnddate();
				byte[] secret = AES.encrypt(str,SHA1.sha1(bean.getEntid()));
				String hexStr =AES.parseByte2HexStr(secret);
			//	System.out.println(hexStr);
				
			}
			//System.out.println(bean.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
}
