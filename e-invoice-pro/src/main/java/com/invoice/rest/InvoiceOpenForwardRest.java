package com.invoice.rest;

import com.invoice.bean.ui.Token;
import com.invoice.util.AESUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.net.URLEncoder;


@Controller
@RequestMapping("/ui")
public class InvoiceOpenForwardRest {

    @RequestMapping(value = "/clickNext", method = RequestMethod.POST)
    @ResponseBody
    public String getNext(@RequestBody String data) {
        String url = "";
        try {
            Token token = Token.getToken();
            String entid = token.getEntid();
            String sheetid = data;
            String appkey = "JCFiz8ap";
            String code = "5ZB9H8";
            String prefix = "https://nnfp.jss.com.cn/i/s/m.do";
            String order1 = URLEncoder.encode(entid + "-","UTF-8") ;
            AESUtils util = new AESUtils("E777A63D300440C0");
            String order2 = URLEncoder.encode(util.encryptData(sheetid),"UTF-8");
            url = prefix + "?p=" + appkey + "!" + code + "!" + order1 + "!" + order2 + "!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

}
