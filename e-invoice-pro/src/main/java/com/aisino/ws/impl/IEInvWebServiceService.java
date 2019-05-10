package com.aisino.ws.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
@WebServiceClient(name = "IEInvWebServiceService", 
wsdlLocation = "http://hnspt.hnhtxx.cn:8080/spt_zzssync/webservice/eInvWS?wsdl",
targetNamespace = "http://impl.ws.aisino.com/") 
public class IEInvWebServiceService extends Service {
	 public final static URL WSDL_LOCATION;

	    public final static QName SERVICE = new QName("http://impl.ws.aisino.com/", "IEInvWebServiceService");
	    public final static QName IEInvWebServicePort = new QName("http://impl.ws.aisino.com/", "IEInvWebServicePort");
	    static {
	        URL url = null;
	        try {
	            url = new URL("http://hnspt.hnhtxx.cn:8080/spt_zzssync/webservice/eInvWS?wsdl");
	        } catch (MalformedURLException e) {
	            java.util.logging.Logger.getLogger(IEInvWebServiceService.class.getName())
	                .log(java.util.logging.Level.INFO, 
	                     "Can not initialize the default wsdl from {0}", "http://hnspt.hnhtxx.cn:8080/spt_zzssync/webservice/eInvWS?wsdl");
	        }
	        WSDL_LOCATION = url;
	    }

	    public IEInvWebServiceService(URL wsdlLocation) {
	        super(wsdlLocation, SERVICE);
	    }

	    public IEInvWebServiceService(URL wsdlLocation, QName serviceName) {
	        super(wsdlLocation, serviceName);
	    }

	    public IEInvWebServiceService() {
	        super(WSDL_LOCATION, SERVICE);
	    }
	    
	    
	    /**
	     *
	     * @return
	     *     returns IEInvWebService
	     */
	    @WebEndpoint(name = "IEInvWebServicePort")
	    public IEInvWebService getWbServiceImplPort() {
	        return super.getPort(IEInvWebServicePort, IEInvWebService.class);
	    }

	    /**
	     * 
	     * @param features
	     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
	     * @return
	     *     returns IEInvWebService
	     */
	    @WebEndpoint(name = "IEInvWebServicePort")
	    public IEInvWebService getWbServiceImplPort(WebServiceFeature... features) {
	        return super.getPort(IEInvWebServicePort, IEInvWebService.class, features);
	    }

}
