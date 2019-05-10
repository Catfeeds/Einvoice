package com.efuture.portal.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
	public static final String COMMON_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT1 = "yyyy.MM.dd HH:mm:ss";
	public static final String DATE_FORMAT2 = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_FORMAT3 = "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String DATE_FORMAT4 = "yyyy-MM-dd";

	public static Date parseDate(String date, String format) {
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static String formatDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static Date parseDateReg(String date) {
		if (date.matches("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			return parseDate(date, FormatUtil.DATE_FORMAT2);
		} else if (date
				.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			return parseDate(date, FormatUtil.COMMON_FORMAT);
		} else if (date
				.matches("\\d{4}.\\d{1,2}.\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			return parseDate(date, FormatUtil.DATE_FORMAT1);
		} else {
			throw new RuntimeException("not matches registed date format!!!!");
		}
	}
	
	public static void main(String[] args) {
		float scale = 34.236323f; 
		DecimalFormat fnum = new DecimalFormat("##0.00"); 
		String dd=fnum.format(scale); 
		System.out.println(dd);
	}
}
