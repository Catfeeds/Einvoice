package com.invoice.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Zbgoodstax;
import com.invoice.uiservice.service.ZbgoodtaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/ui")
public class ZbgoodstaxRest {
    @Autowired
    private ZbgoodtaxService zbgoodtaxService;

    @RequestMapping(value = "/GetGoodsBarcodes",method = RequestMethod.POST)
    @ResponseBody
    public String GetGoodsBarcodes(@RequestBody String data){
        JSONObject jsonObject=new JSONObject();
        Zbgoodstax zbgoodstax=JSONObject.parseObject(data, Zbgoodstax.class);
        try {
            List<Zbgoodstax> list=zbgoodtaxService.getZbGoodsBarcodes(zbgoodstax);
            jsonObject.put("code","1");
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("message",e.getMessage());
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/GetGoodsNames",method = RequestMethod.POST)
    @ResponseBody
    public String getGoodsNames(@RequestBody String data){
        JSONObject jsonObject=new JSONObject();
        Zbgoodstax zbgoodstax=JSONObject.parseObject(data, Zbgoodstax.class);
        try {
            List<Zbgoodstax> list=zbgoodtaxService.getZbGoodsNames(zbgoodstax);
            jsonObject.put("code","1");
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("message",e.getMessage());
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/GetGoodsInfoByBarcode",method = RequestMethod.POST)
    @ResponseBody
    public String GetGoodsNamesByBarcode(@RequestBody String data){
        JSONObject jsonObject = new JSONObject();
        Zbgoodstax zbgoodstax=JSONObject.parseObject(data, Zbgoodstax.class);
        try {
            Zbgoodstax list=zbgoodtaxService.getGoodsInfoByBarcode(zbgoodstax);
            if(list==null){
                jsonObject.put("code","2");
            }else{
                jsonObject.put("code","1");
                jsonObject.put("data",list);
            }
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("message",e.getMessage());
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/GetGoodsInfoByName",method = RequestMethod.POST)
    @ResponseBody
    public String GetGoodsInfoByName(@RequestBody String data){
        JSONObject jsonObject = new JSONObject();
        Zbgoodstax zbgoodstax=JSONObject.parseObject(data, Zbgoodstax.class);
        try {
            Zbgoodstax list=zbgoodtaxService.getGoodsInfoByName(zbgoodstax);
            if(list==null){
                jsonObject.put("code","2");
            }else {
                jsonObject.put("code","1");
                jsonObject.put("data",list);
            }
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("message",e.getMessage());
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    

}
