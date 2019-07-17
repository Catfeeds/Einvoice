package com.invoice.port.nbbanji.invoice.service;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.SheetInvqueBJDao;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.CClient;
import com.invoice.config.EntPrivatepara;
import com.invoice.port.nbbanji.invoice.bean.InvoiceResultReceiveBean;
import com.invoice.util.NewHashMap;

//阪急定制化预开票方法 on 2019.06.10
@Service("BJInvoiceService")
public class BanJiInvoiceService {
	static public final Log log = LogFactory.getLog(BanJiSheetService.class);

	@Autowired
	EntPrivatepara entPrivatepara;
	
	@Autowired
	SheetInvqueBJDao inqueDao;
	
	@Autowired
	PrivateparaService privateparaService;
	
	// 主函数，用于外部调用
	public String setInvoiceInvqueInfo(InvoiceResultReceiveBean bill){
		String logInfo = "";
		
		Map<String,String> map = new NewHashMap<String,String>();
		map.put("rtfpdm",bill.getFPDM()); //发票代码
		map.put("rtfphm",bill.getFPHM()); //发票号码
		map.put("rtkprq",bill.getKPRQ()); //开票日期
		map.put("iqgmftax",bill.getGFSH()); //购方税号
		map.put("iqgmfname",bill.getGFMC()); //购方名称
		map.put("iqgmfadd",bill.getGFDZDH()); //购方地址、电话
		map.put("iqgmfbank",bill.getGFYHZH()); //购方银行、账号
		map.put("iqfplxdm",bill.getFPZL()); //发票种类 01 ：增值税专用发票  04 ：增值税普通发票  10 ：增值税电子普通发票
		map.put("iqtype",bill.getKPLX()); //发票类型 0 ：蓝票  1 ：红票
		map.put("iqmsg",bill.getKPJG()); //开票结果
		map.put("zsfs",bill.getFPZT()); // 发票状态  0：正常  1：作废  2：红冲
		map.put("entid",bill.getQYH()); //企业号
		map.put("sheetid",bill.getFPQQLSH()); //小票流水号
		map.put("shopid", bill.getMDBH()); //门店
		map.put("sheettype", bill.getDPZPBZ().equals("电票")?"1":"7"); //发票类型
		
		//开票状态
		if (bill.getKPZT().equals("0"))
			map.put("iqstatus","10"); //申请
		else if (bill.getKPZT().equals("1"))
			map.put("iqstatus","40"); //开票中
		else if (bill.getKPZT().equals("2"))
			map.put("iqstatus","50"); //成功
		else
			map.put("iqstatus","30"); //失败

		try {
			if (bill.getKPLX().equals("0")) 
			{
				//保存到开具表(INVQUE)
				inqueDao.updateForInvqueBanJI(map);
				map.put("flag","1");
				map.put("invoicelx",bill.getFPZL());
				inqueDao.updateForSaleHeadBanJI(map);
			}
			else
			{
				//保存到退货表(invoice_invque_head_return)
				if (bill.getFPZT().equals("1"))
					map.put("status","200");
				else if (bill.getFPZT().equals("2"))
					map.put("status","300");
				else
					map.put("status","100");
				inqueDao.updateForReturnBanJI(map);
			}
			
			// 初始化远程连接
			CClient client =  privateparaService.getClientUrlByShopid(bill.getQYH(), bill.getMDBH());
						
			if(client==null){
				if (bill.getMDBH() == null || bill.getMDBH().isEmpty())
					logInfo = "阪急－数据不存在entid:"+bill.getQYH();
				else
					logInfo = "阪急－不存在此门店shopid:"+bill.getMDBH();
			
				log.info(logInfo);
							
				return "更新数据不存在";
			}
					
			client.initHeadMap();
			
			if (bill.getDPZPBZ().equals("电票"))
			{
				Map<String, String> headMap = client.getHeadMap();
				headMap.put("shopid", bill.getMDBH());
				headMap.put("sheetid", bill.getFPQQLSH());
				headMap.put("invoicecode",bill.getFPDM());
				headMap.put("invoiceno",bill.getFPHM());
				headMap.put("invoicedate",bill.getKPRQ());
				
				if (bill.getFPZT().equals("1"))
					headMap.put("status","200");
				else if (bill.getFPZT().equals("2"))
					headMap.put("status","300");
				else
					headMap.put("status","100");
				
				headMap.put("sheetname", "NBBJ");
				
				client.setHeadMap(headMap);

				String res = client.getMessage("callBackSheetBJ");
				
				log.info("callBackSheetBJ:" + res);
				
				JSONObject jo = JSONObject.parseObject(res);
				
				if(jo.getIntValue("code") !=0 ) return jo.getString("message");
			}
			else
			{
				Map<String, String> headMap = client.getHeadMap();
				headMap.put("entid", bill.getQYH());
				headMap.put("shopid", bill.getMDBH());
				headMap.put("sheetid", bill.getFPQQLSH());
				headMap.put("sheettype","7");
				headMap.put("flagmsg",bill.getKPJG());
				headMap.put("invoicecode",bill.getFPDM());
				headMap.put("invoiceno",bill.getFPHM());
				headMap.put("invoicedate",bill.getKPRQ());
				
				if (bill.getFPZT().equals("1"))
					headMap.put("flag","200");
				else if (bill.getFPZT().equals("2"))
					headMap.put("flag","300");
				else
					headMap.put("flag","100");

				headMap.put("sheetname", "NBBJ");
				
				client.setHeadMap(headMap);

				String res = client.getMessage("callProvSheetBJ");
				
				log.info("callProvSheetBJ:" + res);
				
				JSONObject jo = JSONObject.parseObject(res);
				
				if(jo.getIntValue("code") !=0 ) return jo.getString("message");
			}

			return "OK";
			
		} catch (Exception ex) {
			logInfo = "阪急－更新开票状态失败：" + ex.toString();
			log.error(logInfo);
			return logInfo;
		}
	}
}
 