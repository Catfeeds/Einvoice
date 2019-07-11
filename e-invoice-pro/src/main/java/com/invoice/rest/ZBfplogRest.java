package com.invoice.rest;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.ZBfplogService;
import com.invoice.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ui")
public class ZBfplogRest {
    @Autowired
    private ZBfplogService zBfplogService;

    @RequestMapping("/getAllZBfplog")
    @ResponseBody
    public String getAllZBfplog(@RequestBody String data){
        JSONObject jsonObject=JSONObject.parseObject(data);
        try {
            Token token=Token.getToken();
            jsonObject.put("entid",token.getEntid());
            jsonObject.put("shopid",token.getShopid());
            Page.cookPageInfo(jsonObject);
            int count=zBfplogService.getAllZBfplogCount(jsonObject);
            List<Map<String,Object>> list=zBfplogService.getAllZBfplog(jsonObject);
            return new RtnData(list,count).toString();
        }catch (Exception e){
            return new RtnData(-1, e.getMessage()).toString();
        }
    }
}