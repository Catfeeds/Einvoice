package com.invoice.util;

import java.io.File;

public class FileUtil {
	static private String RunPath; 
	static{
		RunPath = new File(FileUtil.class.getResource("/").getFile()).getAbsolutePath();
	}
	/**
	 *  获取运行时的绝对路径
	 * @return
	 */
	static public String getRunPath(){
		return RunPath;
	}
}
