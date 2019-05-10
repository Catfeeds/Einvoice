package com.einvoice.dynamicdb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.springframework.core.io.Resource;
public class DynamicResource implements Resource {  
    private DynamicBean dynamicBean;  
  
    public DynamicResource(DynamicBean dynamicBean) {  
        this.dynamicBean = dynamicBean;  
    }  
  
    /* 
     * (non-Javadoc) 
     *  
     * @see org.springframework.core.io.InputStreamSource#getInputStream() 
     */  
    public InputStream getInputStream() throws IOException {  
        return new ByteArrayInputStream(dynamicBean.getXml().getBytes("UTF-8"));  
    }  
    // 其他实现方法省略  
  
    @Override  
    public long contentLength() throws IOException {  
        return 0;  
    }  
  
    @Override  
    public Resource createRelative(String arg0) throws IOException {  
        return null;  
    }  
  
    @Override  
    public boolean exists() {  
        return false;  
    }  
  
    @Override  
    public String getDescription() {  
        return null;  
    }  
  
    @Override  
    public File getFile() throws IOException {  
        return null;  
    }  
  
    @Override  
    public String getFilename() {  
        return null;  
    }  
  
    @Override  
    public URI getURI() throws IOException {  
        return null;  
    }  
  
    @Override  
    public URL getURL() throws IOException {  
        return null;  
    }  
  
    @Override  
    public boolean isOpen() {  
        return false;  
    }  
  
    @Override  
    public boolean isReadable() {  
        return false;  
    }  
  
    @Override  
    public long lastModified() throws IOException {  
        return 0;  
    }  
}  
