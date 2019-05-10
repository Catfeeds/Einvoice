
package com.aisino.ws.qz;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.aisino.ws.qz package. 
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

    private final static QName _EiInterface_QNAME = new QName("http://ws.aisino.com", "eiInterface");
    private final static QName _EiInterfaceResponse_QNAME = new QName("http://ws.aisino.com", "eiInterfaceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.aisino.ws.qz
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EiInterfaceResponse }
     * 
     */
    public EiInterfaceResponse createEiInterfaceResponse() {
        return new EiInterfaceResponse();
    }

    /**
     * Create an instance of {@link EiInterface }
     * 
     */
    public EiInterface createEiInterface() {
        return new EiInterface();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EiInterface }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.aisino.com", name = "eiInterface")
    public JAXBElement<EiInterface> createEiInterface(EiInterface value) {
        return new JAXBElement<EiInterface>(_EiInterface_QNAME, EiInterface.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EiInterfaceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.aisino.com", name = "eiInterfaceResponse")
    public JAXBElement<EiInterfaceResponse> createEiInterfaceResponse(EiInterfaceResponse value) {
        return new JAXBElement<EiInterfaceResponse>(_EiInterfaceResponse_QNAME, EiInterfaceResponse.class, null, value);
    }

}
