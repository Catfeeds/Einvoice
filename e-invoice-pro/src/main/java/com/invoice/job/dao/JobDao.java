package com.invoice.job.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.BillSaleLog;
import com.invoice.util.NewHashMap;

public interface JobDao {
	public List<Map<String, Object>> getJobs(Map<String, Object> paramMap);

	public int update(Map<String, Object> paramMap);

	public void initBillSale();

	public List<BillSaleLog> getBillSaleLog();
	
	public void setBillSaleLog(BillSaleLog log);

	public void replaceBillSale(JSONObject data);

	public void setTaxItemidByGoods(BillSaleLog log);

	public void setTaxItemidByCate(BillSaleLog log);

	public void setDisTaxItemidByGoods(BillSaleLog log);

	public void setDisTaxItemidByCate(BillSaleLog log);

	public void setAmt(NewHashMap<String, Object> map);

	public double getSumOldAmt(BillSaleLog log);

	public void replaceBillSalePay(JSONObject data);

	public void insertBillTaxReport(BillSaleLog log);

}