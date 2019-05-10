package com.invoice.util;

import java.util.Map;

public class Page {
	static public int DEFAUT_PAGE_SIZE = 10;
	static public void cookPageInfo(Map<String,Object> p){
		if(p.containsKey("pagesize")){
			p.put("pagesize", Integer.valueOf(p.get("pagesize").toString()));
		}else{
			p.put("pagesize",DEFAUT_PAGE_SIZE);
		}
		
		if(p.containsKey("pagestart")){
			p.put("pagestart", Integer.valueOf(p.get("pagestart").toString()));
		}
		
		if(p.containsKey("page")){
			String strPage = p.get("page").toString();
			int page = Integer.parseInt(strPage);
			if(page>0){
				p.put("pagestart",(page-1)*Integer.parseInt(p.get("pagesize").toString()));
			}else{
				p.put("pagestart",0);
			}
		}
	}
}
