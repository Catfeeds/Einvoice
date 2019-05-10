package com.invoice.port.bwgf.invoice.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlFactory {
	public static String setUrl (String ur,String param) throws IOException {
		URL url=new URL(ur);
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStreamWriter os=new OutputStreamWriter(conn.getOutputStream());
		os.write(param);
		os.flush();
		BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line ;
		String result="";
		while((line=br.readLine())!=null){
			 result += line;
		}
		if(os!=null){
			os.close();
		}
		if(br!=null){
			br.close();
		}
		return result;
	}

}
