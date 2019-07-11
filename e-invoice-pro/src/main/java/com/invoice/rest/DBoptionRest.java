package com.invoice.rest;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Shop;
import com.invoice.bean.ui.Token;
import com.invoice.uiservice.service.DBoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ui")
public class DBoptionRest {
    @Autowired
    private DBoptionService dBoptionService;

    @RequestMapping(value = "/getAllByshopid",method = RequestMethod.POST)
    @ResponseBody
    public String getAllByshopid(@RequestBody String data){
        Token token=Token.getToken();
        JSONObject jsonObject=new JSONObject();
        Shop shop=JSONObject.parseObject(data,Shop.class);
        shop.setEntid(token.getEntid());
        try {
            List<Map<String,Object>> list=dBoptionService.getAllByshopid(shop);
            if(list.size()==0){
                jsonObject.put("code","1");
            }else{
                jsonObject.put("code","2");
                jsonObject.put("data",list);
            }

        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getClientinfoByshopid",method = RequestMethod.POST)
    @ResponseBody
    public String getClientinfoByshopid(@RequestBody String data){
        Token token=Token.getToken();
        JSONObject jsonObject=new JSONObject();
        CClient cClient=JSONObject.parseObject(data,CClient.class);
        cClient.setEntid(token.getEntid());
        try {
            List<CClient> list=dBoptionService.getClientinfoByshopid(cClient);
            jsonObject.put("code","1");
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getClientAllinfoByshopid",method = RequestMethod.POST)
    @ResponseBody
    public String getClientAllinfoByshopid(@RequestBody String data){
        Token token=Token.getToken();
        JSONObject jsonObject=new JSONObject();
        CClient cClient=JSONObject.parseObject(data,CClient.class);
        cClient.setEntid(token.getEntid());
        try {
            List<CClient> list=dBoptionService.getClientAllinfoByshopid(cClient);
            if(list.size()==0){
                jsonObject.put("code","2");
            }else{
                jsonObject.put("code","1");
                jsonObject.put("data",list);
            }
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getCconectinfoByshopid",method = RequestMethod.POST)
    @ResponseBody
    public String getCconectinfoByshopid(@RequestBody String data){
        Token token=Token.getToken();
        JSONObject jsonObject=new JSONObject();
        CConnect cConnect=JSONObject.parseObject(data,CConnect.class);
        cConnect.setEntid(token.getEntid());
        try {
            List<CConnect> list=dBoptionService.getCconectinfoByshopid(cConnect);
            jsonObject.put("code","1");
            jsonObject.put("data",list);
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/getCconectAllinfoByshopid",method = RequestMethod.POST)
    @ResponseBody
    public String getCconectAllinfoByshopid(@RequestBody String data){
        Token token=Token.getToken();
        JSONObject jsonObject=new JSONObject();
        CConnect cConnect=JSONObject.parseObject(data,CConnect.class);
        cConnect.setEntid(token.getEntid());
        try {
            List<CConnect> list=dBoptionService.getCconectAllinfoByshopid(cConnect);
            if(list.size()==0){
                jsonObject.put("code","2");
            }else{
                jsonObject.put("code","1");
                jsonObject.put("data",list);
            }
        }catch (Exception e){
            jsonObject.put("code","0");
            jsonObject.put("msg",e.getMessage());
        }
        return jsonObject.toString();
    }
}
