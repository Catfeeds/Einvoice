package com.invoice.port.nbbwjf.invoice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	

	
	/**
	 * 获取指定格式时间(yyyy-MM-dd)
	 * @return
	 */
	public static String formatToDay(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return sdf.format((new Date()));
	}
	public static String formatToDateTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format((new Date()));
	}
	
	/**
	 * 获取指定格式时间(yyyyMMddHHmmss)
	 * @return
	 */
	public static String formatToTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format((new Date()));
	}
	
	/************************************************************************
	 * 获取9位随机数
	 */
	public static String randNineData(){
		return randData()+randFiveData();
	}
	
	/************************************************************************
	 * 获取5位随机数
	 */
	public static String randFiveData(){
		return String.valueOf((int)(Math.random()*90000+10000));
	}
	
	/************************************************************************
	 * 获取4位随机数
	 */
	public static String randData(){
		return String.valueOf((int)(Math.random()*9000+1000));
	}
}
