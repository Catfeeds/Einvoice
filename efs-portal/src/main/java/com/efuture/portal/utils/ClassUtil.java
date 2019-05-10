package com.efuture.portal.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
/**
 * 
 * @author wanghuajun
 *
 */
@SuppressWarnings("all")
public class ClassUtil {

	public static List<Class> getAllClassByInterface(Class c) {
		List<Class> clazzList = new ArrayList<Class>();
			String packageName = c.getPackage().getName();
			try {
				List<Class> allClazz = getClazzes(packageName);
				for (int i = 0; i < allClazz.size(); i++) {
					if (!c.equals(allClazz.get(i))) {
						clazzList.add(allClazz.get(i));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		return clazzList;
	}

	private static List<Class> getClazzes(String packageName)
			throws IOException, ClassNotFoundException {
		ClassLoader clazzLoader = Thread.currentThread()
				.getContextClassLoader();
		String path = packageName.replace(".", "/");
		Enumeration<URL> urls = clazzLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			dirs.add(new File(url.getFile()));
		}

		ArrayList<Class> clazzes = new ArrayList<Class>();
		for (File direcotory : dirs) {
			clazzes.addAll(findClazzes(direcotory, packageName));
		}
		return clazzes;
	}

	private static List<Class> findClazzes(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class> clazzes = new ArrayList<Class>();
		if (!directory.exists()) {
			return clazzes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				clazzes.addAll(findClazzes(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				clazzes.add(Class.forName(packageName
						+ "."
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return clazzes;
	}
	

	public static <T> List<T> ListMap2Clazz(List<Map<String, Object>> list, Class<T> c){
		List<T> returnList = new ArrayList<T>();
		Field[] fields = c.getDeclaredFields();
		for (Map<String, Object> map : list) {
			try {
			T t = c.newInstance();
				for (Field field : fields) {
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						String _key = key.replace("_", "").toLowerCase();
						String name = field.getName();

						if (_key.equalsIgnoreCase(field.getName().toLowerCase())) {
							// field.set(t, entry.getValue());
							try {
								Class ft=field.getType();
								
								Method method = c.getDeclaredMethod(
										"set" + name.substring(0, 1).toUpperCase()
												+ name.substring(1), ft);
								
								if (StringUtils.isEmpty(entry.getValue())) continue;
								
								if (ft==String.class) {									
									method.invoke(t, entry.getValue().toString());}
								else if (ft==int.class) {									
									method.invoke(t, Integer.parseInt(entry.getValue().toString()));}
								else if (ft==float.class) {									
									method.invoke(t, Float.parseFloat(entry.getValue().toString()));}
								else if (ft==Date.class) {									
									SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date datevalue=SDF.parse(SDF.format(entry.getValue()));									
									method.invoke(t,datevalue);
								}else if (ft==Double.class) {									
									method.invoke(t, Double.parseDouble(entry.getValue().toString()));}
								 else {
									method.invoke(t,entry.getValue());
								}
								
								
							} catch (Exception e) {
								System.err.println("set" + name.substring(0, 1).toUpperCase()+ name.substring(1));
								System.err.println(field.getType().getName());
								System.err.println("数据库字段和实体类字段不相同,大小写可以有,下横线可以有,但顺序要一样..");
								e.printStackTrace();
							}
						}
					}
				}
				returnList.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnList;
	}
	
	
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);//左补0			
				str = sb.toString();
				strLen = str.length();
			}
		}

		return str;
	}
	
	 public static boolean isMobileNO(String mobiles){
	        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	        Matcher m = p.matcher(mobiles);
	        return m.matches();
	 }

	 public static boolean isPhone(String Phone){
	        Pattern p = Pattern.compile("(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$");
	        Matcher m = p.matcher(Phone);
	        return m.matches();
	 }	
	
}



