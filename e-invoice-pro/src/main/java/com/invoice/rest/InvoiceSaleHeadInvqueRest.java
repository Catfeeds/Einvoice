package com.invoice.rest;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.InvoiceSaleHeadInvque;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.InvoiceSaleHeadInvqueService;
import com.invoice.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/ui")
public class InvoiceSaleHeadInvqueRest {
    @Autowired
    private InvoiceSaleHeadInvqueService invoiceSaleHeadInvqueService;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @ResponseBody
    public String GetAll(@RequestBody String data){
        JSONObject jo = JSONObject.parseObject(data);
        try {
            Token token = Token.getToken();
            jo.put("entid", token.getEntid());
            jo.put("shopid", token.getEntid());
            Page.cookPageInfo(jo);
            int count = invoiceSaleHeadInvqueService.getCount(jo);
            List<InvoiceSaleHeadInvque> tfo = invoiceSaleHeadInvqueService.getAll(jo);
            return new RtnData(tfo,count).toString();
        } catch (Exception e) {
            return new RtnData(-1, e.getMessage()).toString();
        }
    }

}
