package com.invoice.rest;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.Invque;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCallBackAcceptBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCallBackReturnBean;
import com.invoice.util.NewHashMap;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/")
public class TechWebRest {
	private final Log log = LogFactory.getLog(TechWebRest.class);

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	InvqueDao inqueDao;

	@RequestMapping(value = "/recv/callforgd")
	@ResponseBody
	public String TechWebBackWriteInvoice(@RequestBody String data)
	{
		log.info(data);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SzTechWebCallBackReturnBean callBack = new SzTechWebCallBackReturnBean();

		try {
			if (data == null || data.length() == 0)
			{
				callBack.setCode(-1);
				callBack.setMsg("发票数据回写内容为空!");
				callBack.setData(null);
				log.info("发票数据回写内容为空!");
				return callBack.toString();
			}

			SzTechWebCallBackAcceptBean queryReponse = (SzTechWebCallBackAcceptBean)JSONObject.toBean(JSONObject.fromObject(data), SzTechWebCallBackAcceptBean.class);
			if (queryReponse.getTicket_status() == 1)
			{
				callBack.setCode(-1);
				callBack.setMsg("开票中的数据请不要乱调!");
				callBack.setData(null);
				log.error("开票中的数据请不要乱调!");
				return callBack.toString();
			}

			if (queryReponse.getTicket_status() == 2 && (queryReponse.getTicket_sn()==null || queryReponse.getTicket_sn().trim().length()==0 ||
					queryReponse.getTicket_code()==null || queryReponse.getTicket_code().trim().length()==0 ||
					queryReponse.getPdf_url()==null || queryReponse.getTicket_code().trim().length()==0))
			{
				callBack.setCode(-1);
				callBack.setMsg("发票信息不完整!");
				callBack.setData(null);
				log.error("发票信息不完整!");
				return callBack.toString();
			}


			Map<String, Object> p = new NewHashMap<>();
			p.put("iqseqno", queryReponse.getOrder_id());

			List<Invque> quelist = inqueDao.getInvque(p);

			for (Invque myInvque : quelist)
			{
				if (myInvque != null && myInvque.getIqstatus() <= 40)
				{
					//开票失败－必须在此进行处理，否则就不正确，请后面修改注意
					if (queryReponse.getTicket_status() == -2)
					{
						myInvque.setIqstatus(30);
						myInvque.setIqmsg(queryReponse.getMessage()==null?"高灯返回开票失败":queryReponse.getMessage());
						inqueDao.updateForCallGD(myInvque);
					}
					else
					{
						myInvque.setRtskm(queryReponse.getG_unique_id());
						myInvque.setRtkprq(sdf.format(queryReponse.getTicket_date()).replace("-","").replace(":","").replace(" ",""));
						myInvque.setRtfphm(queryReponse.getTicket_sn());
						myInvque.setRtfpdm(queryReponse.getTicket_code());
						myInvque.setIqpdf(queryReponse.getPdf_url());
						myInvque.setRtjym(queryReponse.getCheck_code());

						myInvque.setIqstatus(50);
						inqueDao.updateForCallGD(myInvque);
					}
				}
			}

			//开票失败则视同处理成功
			if  (queryReponse.getTicket_status() == -2)
			{
				callBack.setCode(0);
				callBack.setMsg("处理成功!");
				callBack.setData(null);
				log.info(queryReponse.getMessage()==null?"高灯返回开票失败":queryReponse.getMessage());
				return callBack.toString();
			}

			List<InvoiceHead> headlist = invoiceService.queryInvoiceHeadByCallGD(p);
			for (InvoiceHead myHead : headlist)
			{
				myHead.setFpskm(queryReponse.getG_unique_id());
				myHead.setFprq(sdf.format(queryReponse.getTicket_date()).replace("-","").replace(":","").replace(" ",""));
				myHead.setFphm(queryReponse.getTicket_sn());
				myHead.setFpdm(queryReponse.getTicket_code());
				myHead.setPdf(queryReponse.getPdf_url());
				myHead.setFpjym(queryReponse.getCheck_code());

				myHead.setStatus(100);
				invoiceService.updateInvoiceHeadForCallGD(myHead);
			}

			callBack.setCode(0);
			callBack.setMsg("发票信息回写成功!");
			callBack.setData(null);

			return callBack.toString();
		}
		catch (Exception ex)
		{
			callBack.setCode(-1);
			callBack.setMsg(ex.getMessage());
			callBack.setData(null);
			log.error(ex.getMessage());
			return callBack.toString();
		}
	}
}
