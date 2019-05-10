package com.invoice.job.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.BillSaleLog;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.Paymode;
import com.invoice.job.dao.JobDao;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.MathCal;
import com.invoice.util.NewHashMap;

@Service
public class BillSaleServiceImpl {
	static public final Log log = LogFactory.getLog(BillSaleServiceImpl.class);

	@Autowired
	JobDao jobDao;
	
	@Autowired
	CalculateDao calculateDao;
	
	@Autowired
	PrivateparaService privateparaService;

	/**
	 * 写入log
	 */
	public void initBillSale() {
		jobDao.initBillSale();
	}
	
	public void setTaxItemid(BillSaleLog log){
		jobDao.setDisTaxItemidByGoods(log);
		jobDao.setDisTaxItemidByCate(log);
		jobDao.setTaxItemidByGoods(log);
		jobDao.setTaxItemidByCate(log);
		//jobDao.insertBillTaxReport(log);
	}
	
	public List<BillSaleLog> getBillSaleLog(){
		List<BillSaleLog> list = jobDao.getBillSaleLog();
		return list;
	}
	
	public double executeLog(BillSaleLog log){
		String shopid = log.getShopid();
		String entid = log.getEntid();
		String sdate = (new SimpleDateFormat("yyyyMMdd")).format(log.getSdate());
		CClient client =  privateparaService.getClientUrlByShopid(entid, shopid);
		if(client==null){
			if (shopid == null || shopid.isEmpty() || shopid.equalsIgnoreCase("null"))
				throw new RuntimeException("数据不存在或门店未注册");
			else
				throw new RuntimeException("不存在此门店编码:"+shopid);
		}
		
		String res = HttpClientCommon.post(null, null , client.getUrl()+"/rest/api/getStat/bill/"+shopid+"/"+sdate, 60000, 60000, "utf-8");
		
		JSONObject r = JSONObject.parseObject(res);
		if(r.getIntValue("code")!=0){
			String msg = r.getString("message");
			throw new RuntimeException("获取数据异常:"+client.getClientid()+",MSG:"+msg);
		}else{
			JSONObject dataJson = r.getJSONObject("data");
			//支付金额
			JSONArray payArray = dataJson.getJSONArray("pay");
			double sumPayAmount = 0.0;
			for (int i = 0; i < payArray.size(); i++) {
				JSONObject data = payArray.getJSONObject(i);
				Paymode p = new Paymode();
				//如果是银座4位支付方式，自动在后面补位#店号第一个字符
				String payid = data.getString("payid");
				if(entid.equals("SDYZ") && payid.length()==4){
					payid = payid+"#"+shopid.substring(0,1);
				}
				
				p.setEntid(entid);
				p.setPayid(payid);
				Paymode paymode = calculateDao.getPaymodeById(p);
				if (paymode!=null && paymode.getPaystatus() == 1) {
					sumPayAmount = MathCal.add(sumPayAmount, data.getDoubleValue("amt"), 2);
					data.put("isinvoice", "Y");
				}else{
					data.put("isinvoice", "N");
				}
				if(paymode!=null){
					data.put("payname", paymode.getPayname());
				}
				
				data.put("sdate", log.getSdate());
				data.put("entid", entid);
				data.put("shopid", shopid);
				jobDao.replaceBillSalePay(data);
			}
			
			JSONArray saleArray = dataJson.getJSONArray("sale");
			for (int i = 0; i < saleArray.size(); i++) {
				JSONObject data = saleArray.getJSONObject(i);
				data.put("sdate", log.getSdate());
				data.put("entid", entid);
				data.put("shopid", shopid);
				data.put("oldamt", data.getDoubleValue("amt"));
				data.put("sumpayamt", sumPayAmount);
				if(data.getDouble("qty")==null || data.getDoubleValue("qty")==0){
					data.put("price",0);
				}else{
					data.put("price", data.getDoubleValue("oldamt")/data.getDoubleValue("qty"));
				}
				data.put("amt", 0);
				
				//类别取前6位
				String cateid = data.getString("categoryid");
				if(cateid.length()>6) data.put("categoryid", cateid.substring(0, 6));
				
				jobDao.replaceBillSale(data);
			}
			
			return sumPayAmount;
		}
	}
	
	public void execute(){
		List<BillSaleLog> logList = getBillSaleLog();
		for (BillSaleLog salelog : logList) {
			salelog.setProcesstime(new Date());
			try {
				double sumPayAmt = executeLog(salelog);
				
				if(sumPayAmt>0.0){
					setTaxItemid(salelog);
					double sumOldAmt = jobDao.getSumOldAmt(salelog);
					
					log.info("小票金额："+sumOldAmt+" 支付金额："+sumPayAmt);
					
					NewHashMap<String, Object> map = new NewHashMap<String, Object>();
					map.put("shopid", salelog.getShopid());
					map.put("sdate", salelog.getSdate());
					map.put("sumoldamt",sumOldAmt);
					if(sumOldAmt>sumPayAmt){
						log.info("使用支付金额作为基数计算："+sumPayAmt);
						map.put("sumamt", sumPayAmt);
					}else{
						log.info("使用销售金额作为基数计算："+sumOldAmt);
						map.put("sumamt", sumOldAmt);
					}
					jobDao.setAmt(map);
					jobDao.insertBillTaxReport(salelog);
				}
				salelog.setFlag(100);
				salelog.setProcessmsg("complate");
			} catch (Exception e) {
				salelog.setFlag(99);
				String msg = e.getMessage();
				if(msg!=null && msg.length()>500) msg = msg.substring(0, 500);
				salelog.setProcessmsg("Error:"+msg);
			}
			jobDao.setBillSaleLog(salelog);
		}
		
	}
	
}