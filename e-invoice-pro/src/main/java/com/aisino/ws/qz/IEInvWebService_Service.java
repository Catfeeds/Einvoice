package com.aisino.ws.qz;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2017-09-14T23:25:28.890+08:00
 * Generated source version: 2.7.18
 * 
 */
@WebServiceClient(name = "IEInvWebService", 
                  wsdlLocation = "file:/D:/work/eclipse.work.invoice/efs-e-invoice-sell/e-invoice-pro/src/main/java/hangxin/invoice/bean/qz.wsdl",
                  targetNamespace = "http://ws.aisino.com") 
public class IEInvWebService_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ws.aisino.com", "IEInvWebService");
    public final static QName IEInvWebServicePort = new QName("http://ws.aisino.com", "IEInvWebServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/work/eclipse.work.invoice/efs-e-invoice-sell/e-invoice-pro/src/main/java/hangxin/invoice/bean/qz.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(IEInvWebService_Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/D:/work/eclipse.work.invoice/efs-e-invoice-sell/e-invoice-pro/src/main/java/hangxin/invoice/bean/qz.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public IEInvWebService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public IEInvWebService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IEInvWebService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IEInvWebService_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IEInvWebService_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public IEInvWebService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns IEInvWebService
     */
    @WebEndpoint(name = "IEInvWebServicePort")
    public IEInvWebService getIEInvWebServicePort() {
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
    public IEInvWebService getIEInvWebServicePort(WebServiceFeature... features) {
        return super.getPort(IEInvWebServicePort, IEInvWebService.class, features);
    }

}
