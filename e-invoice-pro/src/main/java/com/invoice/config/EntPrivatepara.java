package com.invoice.config;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoice.apiservice.service.impl.BillInvoiceService;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.Privatepara;
import com.invoice.util.NewHashMap;

@Component
public class EntPrivatepara {
	private final Log log = LogFactory.getLog(BillInvoiceService.class);
	
	@Autowired
	PrivateparaService service;
	
	private Map<String,Map<String,String>> entPrivateparaMap;
	
	public void init(){
		entPrivateparaMap = new NewHashMap<String, Map<String,String>>();
		
		List<Privatepara> list = service.getPrivatepara(null);
		for (Privatepara privatepara : list) {
			String entid = privatepara.getEntid();
			if(!entPrivateparaMap.containsKey(entid)){
				log.info("读取企业私有参数:"+entid);
				Map<String, String> map = new NewHashMap<String, String>();
				map.put(privatepara.getPparaid(),privatepara.getPparavalue());
				entPrivateparaMap.put(entid, map);
				log.info("读取企业:"+entid+",私有参数值："+privatepara.getPparaid()+"="+privatepara.getPparavalue());
			}else{
				Map<String, String> map = entPrivateparaMap.get(entid);
				map.put(privatepara.getPparaid(),privatepara.getPparavalue());
				log.info("读取企业:"+entid+",私有参数值："+privatepara.getPparaid()+"="+privatepara.getPparavalue());
			}
		}
	}
	
	synchronized public String get(String entid, String paraid){
		if(entPrivateparaMap==null) init();
		
		if(!entPrivateparaMap.containsKey(entid)) return null;
		Map<String, String> map = entPrivateparaMap.get(entid);
		return map.get(paraid);
	}
	
	synchronized public void set(String entid, String key, String value){
		if(entPrivateparaMap==null) init();
		if(!entPrivateparaMap.containsKey(entid)) return;
		entPrivateparaMap.get(entid).put(key, value);
		
		//更新db
		Privatepara p = new Privatepara();
		p.setEntid(entid);
		p.setPparaid(key);
		p.setPparavalue(value);
		service.updatePrivatepara(p);
	}

}
