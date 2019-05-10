package com.invoice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.invoice.config.FGlobal;

public class Serial {

	public static String lastdateString = "";
	private static int lastSuffixQ = 100;
	
	private static int lastSuffixS = 100;
	
	private static int lastSuffixV = 100;
	
	public static synchronized String getInvqueSerial(){
		String res = null;
		String dateString = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		//每秒最多8999单
//		int suffix = 1000;
//		if(dateString.equalsIgnoreCase(lastdateString)){
//			suffix = lastSuffixQ + 1 ;
//		}else{
//			suffix = 1000;
//		}
		String uuid = UUID.randomUUID().toString().substring(0, 5);
		res = "Q"+FGlobal.sid+dateString + uuid;
//		lastSuffixQ = suffix;
//		lastdateString = dateString;
		return res;
	}

	public static synchronized String getSheetSerial(){
		String res = null;
		String dateString = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		//每秒最多8999单
		int suffix = 1000;
		if(dateString.equalsIgnoreCase(lastdateString)){
			suffix = lastSuffixS + 1 ;
		}else{
			suffix = 1000;
		}
		
		res = "S"+FGlobal.sid + dateString + suffix;
		lastSuffixS = suffix;
		lastdateString = dateString;
		return res;
	}
	
	public static synchronized String getInvoiceSerial(){
		String res = null;
		String dateString = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		//每秒最多8999单
		int suffix = 1000;
		if(dateString.equalsIgnoreCase(lastdateString)){
			suffix = lastSuffixV + 1 ;
		}else{
			suffix = 1000;
		}
		
		res = "V"+FGlobal.sid + dateString + suffix;
		lastSuffixV = suffix;
		lastdateString = dateString;
		return res;
	}
}
