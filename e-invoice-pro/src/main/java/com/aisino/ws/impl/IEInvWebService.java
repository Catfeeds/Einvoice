package com.aisino.ws.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://impl.ws.aisino.com/", name = "IEInvWebService")
@XmlSeeAlso({ObjectFactory.class})
public interface IEInvWebService {
    
    @WebResult(name = "return", targetNamespace = "http://impl.ws.aisino.com/")
    @RequestWrapper(localName = "eiInterface", targetNamespace = "http://impl.ws.aisino.com/", className = "com.aisino.ws.impl.EiInterface")
    @WebMethod
    @ResponseWrapper(localName = "eiInterfaceResponse", targetNamespace = "http://impl.ws.aisino.com/", className = "com.aisino.ws.impl.EiInterfaceResponse")
    public java.lang.String eiInterface(
        @WebParam(name = "requestMessage", targetNamespace = "http://impl.ws.aisino.com/")
        java.lang.String requestMessage
    );
    
}
