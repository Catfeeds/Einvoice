//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.06.18 时间 02:20:10 PM CST 
//


package com.invoice.port.nbbanji.invoice.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NSRSBH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZDH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MDBH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FPQQLSH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DPZPBZ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FPDM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FPHM" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KPRQ" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GFSH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GFMC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GFDZDH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GFYHZH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FPZL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KPLX" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FPZT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KPZT" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="KPJG" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"qyh",
    "nsrsbh",
    "zdh",
    "mdbh",
    "fpqqlsh",
    "dpzpbz",
    "fpdm",
    "fphm",
    "kprq",
    "gfsh",
    "gfmc",
    "gfdzdh",
    "gfyhzh",
    "fpzl",
    "kplx",
    "fpzt",
    "kpzt",
    "kpjg"
})
@XmlRootElement(name = "business")
public class InvoiceResultReceiveBean {
	@XmlElement(name = "QYH", required = true)
	protected String qyh;
    @XmlElement(name = "NSRSBH", required = true)
    protected String nsrsbh;
    @XmlElement(name = "ZDH", required = true)
    protected String zdh;
    @XmlElement(name = "MDBH", required = true)
    protected String mdbh;
    @XmlElement(name = "FPQQLSH", required = true)
    protected String fpqqlsh;
    @XmlElement(name = "DPZPBZ", required = true)
    protected String dpzpbz;
    @XmlElement(name = "FPDM", required = true)
    protected String fpdm;
    @XmlElement(name = "FPHM", required = true)
    protected String fphm;
    @XmlElement(name = "KPRQ", required = true)
    protected String kprq;
    @XmlElement(name = "GFSH", required = true)
    protected String gfsh;
    @XmlElement(name = "GFMC", required = true)
    protected String gfmc;
    @XmlElement(name = "GFDZDH", required = true)
    protected String gfdzdh;
    @XmlElement(name = "GFYHZH", required = true)
    protected String gfyhzh;
    @XmlElement(name = "FPZL", required = true)
    protected String fpzl;
    @XmlElement(name = "KPLX", required = true)
    protected String kplx;
    @XmlElement(name = "FPZT", required = true)
    protected String fpzt;
    @XmlElement(name = "KPZT", required = true)
    protected String kpzt;
    @XmlElement(name = "KPJG", required = true)
    protected String kpjg;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "comment")
    protected String comment;
    
    /**
     * 获取entid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQYH() {
        return qyh;
    }

    /**
     * 设置entid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQYH(String value) {
        this.qyh = value;
    }
    
    /**
     * 获取nsrsbh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNSRSBH() {
        return nsrsbh;
    }

    /**
     * 设置nsrsbh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNSRSBH(String value) {
        this.nsrsbh = value;
    }

    /**
     * 获取zdh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZDH() {
        return zdh;
    }

    /**
     * 设置zdh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZDH(String value) {
        this.zdh = value;
    }

    /**
     * 获取mdbh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDBH() {
        return mdbh;
    }

    /**
     * 设置mdbh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDBH(String value) {
        this.mdbh = value;
    }

    /**
     * 获取fpqqlsh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFPQQLSH() {
        return fpqqlsh;
    }

    /**
     * 设置fpqqlsh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFPQQLSH(String value) {
        this.fpqqlsh = value;
    }

    /**
     * 获取dpzpbz属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDPZPBZ() {
        return dpzpbz;
    }

    /**
     * 设置dpzpbz属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDPZPBZ(String value) {
        this.dpzpbz = value;
    }

    /**
     * 获取fpdm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFPDM() {
        return fpdm;
    }

    /**
     * 设置fpdm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFPDM(String value) {
        this.fpdm = value;
    }

    /**
     * 获取fphm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFPHM() {
        return fphm;
    }

    /**
     * 设置fphm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFPHM(String value) {
        this.fphm = value;
    }

    /**
     * 获取kprq属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKPRQ() {
        return kprq;
    }

    /**
     * 设置kprq属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKPRQ(String value) {
        this.kprq = value;
    }

    /**
     * 获取gfsh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGFSH() {
        return gfsh;
    }

    /**
     * 设置gfsh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGFSH(String value) {
        this.gfsh = value;
    }

    /**
     * 获取gfmc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGFMC() {
        return gfmc;
    }

    /**
     * 设置gfmc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGFMC(String value) {
        this.gfmc = value;
    }

    /**
     * 获取gfdzdh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGFDZDH() {
        return gfdzdh;
    }

    /**
     * 设置gfdzdh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGFDZDH(String value) {
        this.gfdzdh = value;
    }

    /**
     * 获取gfyhzh属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGFYHZH() {
        return gfyhzh;
    }

    /**
     * 设置gfyhzh属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGFYHZH(String value) {
        this.gfyhzh = value;
    }

    /**
     * 获取fpzl属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFPZL() {
        return fpzl;
    }

    /**
     * 设置fpzl属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFPZL(String value) {
        this.fpzl = value;
    }

    /**
     * 获取kplx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKPLX() {
        return kplx;
    }

    /**
     * 设置kplx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKPLX(String value) {
        this.kplx = value;
    }

    /**
     * 获取fpzt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFPZT() {
        return fpzt;
    }

    /**
     * 设置fpzt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFPZT(String value) {
        this.fpzt = value;
    }

    /**
     * 获取kpzt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKPZT() {
        return kpzt;
    }

    /**
     * 设置kpzt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKPZT(String value) {
        this.kpzt = value;
    }

    /**
     * 获取kpjg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKPJG() {
        return kpjg;
    }

    /**
     * 设置kpjg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKPJG(String value) {
        this.kpjg = value;
    }

    /**
     * 获取id属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * 获取comment属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置comment属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

}
