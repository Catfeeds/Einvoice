package baiwang.invoice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.baiwang.bop.request.impl.bizinfo.CompanySearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.apiservice.service.impl.InvoiceAnddetailService;
import com.invoice.bean.db.InvoicePrintHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.db.Taxprintinfo;
import com.invoice.port.bwjs.invoice.service.BwjsdjzpGenerateBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.util.SpringContextUtil;

import baiwang.invoice.bean.BaiWangRtBlankBean;
import baiwang.invoice.bean.BaiWangRtInvoiceBean;
import baiwang.invoice.bean.FpttBean;
import baiwang.invoice.bean.RtInvoiceHeadList;
/*@Service("BaiWangServiceImpl")*/
public class BaiWangServiceImpl implements BaiWangService {
/*	@Autowired
	TaxinfoDao taxinfoDao;*/
	static public final Log log = LogFactory.getLog(BaiWangServiceImpl.class);
	private   String serviceUrl;
	private   String nsrsbh;
	private   String jrdm;
	BaiWangGenerateBean bw;
	public BaiWangServiceImpl(String serviceUrl,String nsrsbh,String jrdm ){
		this.serviceUrl = serviceUrl;
		this.nsrsbh = nsrsbh;
		this.jrdm = jrdm;
		bw = new BaiWangGenerateBean(nsrsbh,jrdm);
	}
	
	/**
	 * 开具发票
	 * **/
	public BaiWangRtInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn){
		
		if(nsrsbh==null||"".equals(nsrsbh)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票时纳税人识别号为空");
			return null;
		}
		
		if(jrdm==null||"".equals(jrdm)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票时接入代码号为空");
			return null;
		}
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		bw.generateBaiWangBean(invque,taxinfo,detailList,"1000", rtn);
		//登陆百旺获取数据
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		BaiWangRtInvoiceBean rtinvoicebean = new BaiWangRtInvoiceBean();
		//返回后的数据做装换
		if(rtn.getCode()==0){
			rtinvoicebean= bw.rtOpenToBean(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		
		return rtinvoicebean;
	}

	/**
	 * 发票查询(不分页)
	 * **/
	public RtInvoiceHeadList findInvoice(Invque invque,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		bw.findInvoice(invque,"1002", rtn);
		
		if(rtn.getCode()==0){
		 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		
		RtInvoiceHeadList list = new RtInvoiceHeadList();
		if(rtn.getCode()==0){
			list= bw.rtFindToBean(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("查询发票返回数据有问题");
			return null;
		}
		return list;
	}
	
	/**
	 * 空白发票作废
	 * **/
	public BaiWangRtInvoiceBean invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		
		bw.generateInvoiceblankInValid(invque,taxinfo,"1008", rtn);
		
		BaiWangRtInvoiceBean rtInvoice = new BaiWangRtInvoiceBean();
		
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		
		if(rtn.getCode()==0){
			bw.rtInvoiceInValid(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("发票作废返回的数据有问题");
			return null;
		}
		
		return rtInvoice;
	}
	
	/**
	 * 已开发票作废
	 * **/
	public BaiWangRtInvoiceBean invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		
		bw.generateInvoiceYkInValid(invque,taxinfo,"1008", rtn);
		
		BaiWangRtInvoiceBean rtInvoice = new BaiWangRtInvoiceBean();
		
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		
		if(rtn.getCode()==0){
			rtInvoice =  bw.rtInvoiceInValid(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("发票作废返回的数据有问题");
			return null;
		}
		log.info("invoiceYkInValid end !"+rtn.getCode());
		return rtInvoice;
	}
	
	/**
	 * 发票打印
	 * **/
	
	public void invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		Taxprintinfo taxprint = new Taxprintinfo();
		taxprint.setEntid(invque.getIqentid());
		taxprint.setTaxno(taxinfo.getTaxno());
		taxprint.setKpd(invque.getIqtaxzdh());
		try{
			taxprint = SpringContextUtil.getBean(TaxinfoDao.class).getTaxprintinfoByNo(taxprint);	
			//taxprint=taxinfoDao.getTaxprintinfoByNo(taxprint);
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		if(taxprint==null){
			bw.generateInvoicePrint(invque,taxinfo,"1011",rtn);
			//System.out.println(rtn.getMessage());
			if(rtn.getCode()==0){
				 login(rtn.getMessage(),rtn);
			}else{
				rtn.setCode(-1);
				//rtn.setMessage("数据装换为XML出错");
				return;
			}
		}else{
			BwjsdjzpGenerateBean	bw = new BwjsdjzpGenerateBean();
			
			Map<String, Object> p=new HashMap<String, Object>();
			p.put("yfpdm", invque.getRtfpdm());
			p.put("yfphm", invque.getRtfphm());
			List<InvoiceSaleDetail> detailList = SpringContextUtil.getBean(InvoiceAnddetailService.class).getHongInvoiceDetail(p);	
			InvoicePrintHead printhead = bw.zp_print(invque, taxinfo, detailList, rtn);
			if(printhead==null){
				rtn.setCode(-1);
				rtn.setMessage("没有获取到打印的发票信息");
				return;
			}
			printhead.setTaxprintinfo(taxprint);
			invque.setData(printhead);
		}
		/*	
		if(rtn.getCode()==0){
			rtInvoice =  bw.rtBlankInvoice(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("查询空白发票返回的数据有问题");
			return;
		}
		*/
	}
	
	/**
	 * 空白发票查询
	 * **/
	
	public BaiWangRtBlankBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		bw.blankInvoice(invque,taxinfo,"1006",rtn);
		
		BaiWangRtBlankBean rtInvoice = new BaiWangRtBlankBean();
		
		
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		
		if(rtn.getCode()==0){
			rtInvoice =  bw.rtBlankInvoice(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("查询空白发票返回的数据有问题");
			return null;
		}
		
		
		return rtInvoice;
	}
	
	/**
	 * 获取发票PDF连接(含推送)
	 * **/
	public String getPdf(Invque invque,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		
		bw.generatePdf(invque,"1025", rtn);
		
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		String rt = "";
		if(rtn.getCode()==0){
			rt = bw.rtPdfToString(rtn.getMessage(), rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		return rt;
	}
	
	/**
	 * 版式文件查询
	 * **/
	public String getsearchPdf(Invque invque,String cxlx ,String fhbz,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		
		bw.searchPdf(invque,"1015",cxlx ,fhbz,rtn);
		
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		String rt = "";
		if(rtn.getCode()==0){
			rt = bw.rtPdfToString(rtn.getMessage(), rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		return rt;
	}
	
	public String access_fptt(FpttBean fptt,RtnData rtn){
		String ret = "";
		try{
			fptt.setToken(access_token(fptt));
			fptt.setSign(new GetSignaTure(fptt).getST() );
			
			ObjectMapper mapper = new ObjectMapper();
			
			CompanySearchRequest searchRequest = new CompanySearchRequest();
			CompanySearchRequest request = new CompanySearchRequest();
			request.setCompanyName("百望金税");
			request.setAccuracy("false");
			request.setSort("{\"frequency\": 0}");
			request.setTaxId("");
			request.setAppId("str");
			String jackSon=mapper.writeValueAsString(request);

			ret = UrlFactory.setUrl(fptt.getUrl()+"?method=baiwang.bizinfo.companySearch"
					+ "&version=2.0"
					+ "&appKey="+fptt.getClient_id()
					+ "&format=json"
					+ "&timestamp="+fptt.getTimestamp()
					+ "&token="+fptt.getToken()
					+ "&type=sync"
					+ "&sign="+fptt.getSign(),jackSon);
			System.out.println(ret);
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
			
		}
		return ret;
	}
	
	public String access_token(FpttBean fptt){
		String ret = "";
		String url = fptt.getUrl()+"?timestamp="+fptt.getTimestamp()+"&username="+fptt.getUsername()+"&client_secret="+fptt.getClient_secret()+
					 "&grant_type="+fptt.getGrant_type()+"&method="+fptt.getMethod()+"&client_id="+fptt.getClient_id()+"&password="+fptt.getPassword()+
					 "&version="+fptt.getVersion();
			 
		try{
					ret = JSONObject.parseObject(JSONObject.parseObject(UrlFactory.setUrl(url, fptt.getClient_id())).getString("response")).getString("access_token");
					 
		}catch(Exception e){
			e.printStackTrace();
		}
		 return ret;
	}
	
	
	/**
	 * 登陆百旺系统，发送数据信息
	 * **/
	public void login(String xml,RtnData rtn ){
		
		if(serviceUrl==null||"".equals(serviceUrl)){
			rtn.setCode(-1);
			rtn.setMessage("URL为空");
			return;
		}
		
		if(nsrsbh==null||"".equals(nsrsbh)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票时纳税人识别号为空");
			return;
		}
		
		if(jrdm==null||"".equals(jrdm)){
			rtn.setCode(-1);
			rtn.setMessage("接入代码号为空");
			return;
		}
		
		BaiWangClient client = new BaiWangClientImpl();
		//String serviceUrl  ="http://36.110.112.203:8031/api/service/bwapi";

		client.login(serviceUrl, nsrsbh, jrdm, rtn);
		String rtxml =  client.rpc(xml, rtn);
		if(rtn.getCode()==0){
			rtn.setMessage(rtxml);
		}/*else{
			rtn.setCode(-1);
			rtn.setMessage("向百旺系统发送数据时出现错误");
		}*/
		//System.out.println(rtxml);
		
		client.logout(rtn);
		
	}

	
}
