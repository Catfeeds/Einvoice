package com.invoice.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.invoice.apiservice.service.impl.PrivateparaService;

/**
 * @author Baij Client调用
 */
@Controller
@RequestMapping(value = "/client")
public class ClientRest {
	private final Log log = LogFactory.getLog(ClientRest.class);

	@Autowired
	PrivateparaService privateparaService;

	@RequestMapping(value = "/getShopConnectList")
	@ResponseBody
	public String getShopConnectList(@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time){
		log.debug("entid:"+entid+",clientid:"+clientid+",password:"+password);
		
		return privateparaService.getShopConnectList(entid,clientid,password,time);
	}

}
