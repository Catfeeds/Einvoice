package com.invoice.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.PrivateparaDao;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Privatepara;
import com.invoice.bean.db.PurchaserInfo;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.CompanyNameService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/ui")
public class CompanyNameRest {
    @Autowired
    private CompanyNameService companyNameService;
    
    @Autowired
	PrivateparaDao dao;

	public List<Privatepara> getPrivatepara(Privatepara p) {
		return dao.getPrivatepara(p);
	}

    @RequestMapping(value = "/getEnterpriseByIdTitle", method = RequestMethod.POST)
    @ResponseBody
    public String getEnterpriseByIdTitle() {
    	try
    	{
	    	Token myToken = Token.getToken();
	        String entids = myToken.getEntid();
	        Enterprise enterprise = companyNameService.getEnterpriseByIdTitle(entids);

	        return JSON.toJSONString(enterprise);
    	}
    	catch (Exception e) 
		{
			return JSON.toJSONString(e.getMessage());
		}
    }

    @RequestMapping(value = "/getInvoicerListByName")
	@ResponseBody
	public String getInvoicerListByName(@RequestBody String data) {
		try {
			Privatepara myPrivatepara = new Privatepara();
			myPrivatepara.setEntid(Token.getToken().getEntid());
			List<Privatepara> p = getPrivatepara(myPrivatepara);
			
			PurchaserInfo c = JSONObject.parseObject(data, PurchaserInfo.class);
			List<PurchaserInfo> res = companyNameService.getPurchaserListByClass(c,p);
			return new RtnData(res).toString();
		}
		catch (Exception e) 
		{
			return new RtnData(-1, e.toString()).toString();
		}
	}
}
