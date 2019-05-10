package com.efuture.portal.carrefour;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CarrefourSystem {
	private static final String charset = "UTF-8";

	private static final String separator = System
			.getProperty("line.separator");

	public static void main(String[] args) throws Exception {
		String path = System.getProperty("user.dir");
		String commonpath = path.replace("portal", "common");
		new File(commonpath
				+ "\\src\\main\\resources\\mybatis\\mybatis-db-one.xml")
				.delete();
		new File(
				commonpath
						+ "\\src\\main\\resources\\mybatis\\mybatis-db-one-carrefourSystem.xml")
				.renameTo(new File(commonpath
						+ "\\src\\main\\resources\\mybatis\\mybatis-db-one.xml"));

		new File(path + "\\src\\main\\resources\\conf\\jdbc.properties")
				.delete();
		new File(
				path
						+ "\\src\\main\\resources\\conf\\jdbc_carrefourSystem.properties")
				.renameTo(new File(path
						+ "\\src\\main\\resources\\conf\\jdbc.properties"));

		new File(path + "\\src\\main\\webapp\\ui\\login.html").delete();
		new File(path + "\\src\\main\\webapp\\ui\\login_carrefourSystem.html")
				.renameTo(new File(path + "\\src\\main\\webapp\\ui\\login.html"));

		new File(path + "\\src\\main\\webapp\\ui\\main.html").delete();
		new File(path + "\\src\\main\\webapp\\ui\\main_carrefourSystem.html")
				.renameTo(new File(path + "\\src\\main\\webapp\\ui\\main.html"));

		renameProject(path + "\\pom.xml");

		renameProjectDir(path + "\\src\\main\\webapp\\ui\\js");
		renameProjectDir(path + "\\src\\main\\webapp\\ui\\login");
		renameProjectDir(path + "\\src\\main\\webapp\\ui\\module");
	}

	private static void renameProjectDir(String path) {
		if (path.contains("svn")) {
			return;
		}
		File file = new File(path);
		if (file.isFile()) {
			renameProject(path);
		} else {
			for (File item : file.listFiles()) {
				renameProjectDir(item.getAbsolutePath());
			}
		}
	}

	private static void renameProject(String path) {
		String pomstr = readFileByLines(path);
		writeFile(path, pomstr.replaceAll("efs-portal", "carrefourSystem"));
	}

	public static String readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), charset));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				sb.append(separator).append(tempString);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString().replaceFirst(separator, "");
	}

	/**
	 * 写入文本到文件
	 * 
	 * @return
	 */
	public static void writeFile(String fileName, String content) {
		File file = new File(fileName);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), charset));
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}
