package com.einvoice.sell.tungkong;

import java.io.StringReader;
import java.security.Key; 
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InvoicePrivate {
    private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
    static final Log log = LogFactory.getLog(InvoicePrivate.class);
 
    public static String AES_Encrypt(String keyStr, String plainText) 
    { 
        byte[] encrypt = null; 
        try
        { 
            Key key = generateKey(keyStr); 
            
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            
            encrypt = cipher.doFinal(plainText.getBytes());     
        }
        catch(Exception e)
        { 
            e.printStackTrace(); 
            
            return "";
        }
        
        return new String(Base64.encodeBase64(encrypt)); 
    } 
 
    public static String AES_Decrypt(String keyStr, String encryptData) 
    {
        byte[] decrypt = null; 
        try
        { 
            Key key = generateKey(keyStr); 
            
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            
            cipher.init(Cipher.DECRYPT_MODE, key); 
            
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData)); 
            
            return new String(decrypt,"utf-8").trim(); 
        }
        catch(Exception e)
        { 
            e.printStackTrace(); 
            
            return "";
        }
    } 
	 
    public static Key generateKey(String key) throws Exception
    { 
        try
        {            
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); 
            
            return keySpec; 
        }
        catch(Exception e)
        { 
            e.printStackTrace(); 
            return null; 
        } 
    }
    
    public static String ResponseXML(String paraTaxNo,String paraSheetId,String paraInfo)
    {
    	StringBuilder respText = (new StringBuilder())
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<business id=\"DDCX\" comment=\"" + "订单查询" + "\">")
			    .append("<NSRSBH>" + paraTaxNo + "</NSRSBH>")
			    .append("<FPQQLSH>" + paraSheetId + "</FPQQLSH>")
			    .append("<RETURNCODE>9999</RETURNCODE>")
			    .append("<RETURNMSG>" + paraInfo + "</RETURNMSG>")
			    .append("</business>");
    	
    	return respText.toString();
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String xml, T t) 
	{
	  try
	  {
	    JAXBContext context = JAXBContext.newInstance(t.getClass());  
	    
	    Unmarshaller um = context.createUnmarshaller();  
	      
	    StringReader sr = new StringReader(xml);  
	      
	    t = (T) um.unmarshal(sr);  
	      
	    return t;
	  }
	  catch (Exception ex)
	  {
	   	log.error(ex.getMessage());
	   	
	    return null;
	  }
	} 
}
