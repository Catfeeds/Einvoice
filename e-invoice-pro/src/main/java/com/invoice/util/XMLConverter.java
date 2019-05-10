package com.invoice.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.alibaba.fastjson.JSONObject;

public class XMLConverter {
	public static String objectToXml(Object object, String encoding){
		 String result = null;  
		    try {  
		        JAXBContext context = JAXBContext.newInstance(object.getClass());  
		        Marshaller marshaller = context.createMarshaller();  
		        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
		        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
		        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);  //不要头 <?xml version="1.0" encoding="UTF-8"?>
		        
		        StringWriter writer = new StringWriter();  
		        marshaller.marshal(object, writer);  
		        result = writer.toString();  
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }
		    	 return result; 
		   
		    
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T  xmlToObject(String xml, Class<T> c){
		  T t = null;  
	        try {  
	            JAXBContext context = JAXBContext.newInstance(c);  
	            Unmarshaller unmarshaller = context.createUnmarshaller();  
	            t = (T) unmarshaller.unmarshal(new StringReader(xml));  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	  
	        return t;  

		    
	}
	
	public static Marshaller objectToXmlObject(Object object, String encoding){
		Marshaller result = null;  
		    try {  
		        JAXBContext context = JAXBContext.newInstance(object.getClass());  
		        Marshaller marshaller = context.createMarshaller();  
		        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
		        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
		        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);//不要头信息
		        StringWriter writer = new StringWriter();  
		        marshaller.marshal(object, writer); 
		        result = marshaller;
		        //result = writer.toString();  
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }
		    	 return result; 
		   
		    
	}
	public static String JSONToXml(JSONObject object){
		String reStr = new String();
		
		return reStr;
	}

}
