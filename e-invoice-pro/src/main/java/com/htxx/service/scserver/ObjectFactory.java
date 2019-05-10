
package com.htxx.service.scserver;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.htxx.service.scserver package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetData_QNAME = new QName("com.htxx.service", "getData");
    private final static QName _GetDataResponse_QNAME = new QName("com.htxx.service", "getDataResponse");
    private final static QName _SendData_QNAME = new QName("com.htxx.service", "sendData");
    private final static QName _SendDataResponse_QNAME = new QName("com.htxx.service", "sendDataResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.htxx.service.scserver
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetData }
     * 
     */
    public GetData createGetData() {
        return new GetData();
    }

    /**
     * Create an instance of {@link GetDataResponse }
     * 
     */
    public GetDataResponse createGetDataResponse() {
        return new GetDataResponse();
    }

    /**
     * Create an instance of {@link SendData }
     * 
     */
    public SendData createSendData() {
        return new SendData();
    }

    /**
     * Create an instance of {@link SendDataResponse }
     * 
     */
    public SendDataResponse createSendDataResponse() {
        return new SendDataResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "com.htxx.service", name = "getData")
    public JAXBElement<GetData> createGetData(GetData value) {
        return new JAXBElement<GetData>(_GetData_QNAME, GetData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "com.htxx.service", name = "getDataResponse")
    public JAXBElement<GetDataResponse> createGetDataResponse(GetDataResponse value) {
        return new JAXBElement<GetDataResponse>(_GetDataResponse_QNAME, GetDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "com.htxx.service", name = "sendData")
    public JAXBElement<SendData> createSendData(SendData value) {
        return new JAXBElement<SendData>(_SendData_QNAME, SendData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "com.htxx.service", name = "sendDataResponse")
    public JAXBElement<SendDataResponse> createSendDataResponse(SendDataResponse value) {
        return new JAXBElement<SendDataResponse>(_SendDataResponse_QNAME, SendDataResponse.class, null, value);
    }

}
