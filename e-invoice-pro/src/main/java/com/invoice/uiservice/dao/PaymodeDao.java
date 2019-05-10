package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Paymode;


@Component("PaymodeDao")
public interface PaymodeDao {
	    public void insertPaymode(Paymode paymode) throws Exception;
	    
	    public void updatePaymode(Paymode paymode) throws Exception;
	    
	    public void deletePaymode(Paymode paymode) throws Exception;
	    
	    public List<HashMap<String,String>> queryPaymode(Map<String, Object> p) throws Exception;
	    
	    public Paymode getPaymodeById(Paymode paymode) throws Exception;
	    
	    int getPaymodeCount(Map<String, Object> p);
	    
	    public List<HashMap<String,String>> queryPaymodelog(Map<String, Object> p) throws Exception;
	    
	    int getPaymodeCountlog(Map<String, Object> p);
	    
	    public void insertPaymodelog(Paymode paymode) throws Exception;
	    
	    
}
