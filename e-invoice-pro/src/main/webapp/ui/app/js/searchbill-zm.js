var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
$(document).ready(function(){
	getuser();
});


var shop;
var checkshopid;
function getuser(){
	$.myAjax({
		url:$.getService("ui","getcuruser"),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			user = dataJson.data;
	/*		if (user.shoplist.length == 1) {
				$("#requestbillno").val(user.shoplist[0].shopid+"-");
		    }*/
			shop=user.shoplist;
			checkshopid = user.shopid;
//			$("#requestbillno").val(user.shoplist[0].shopid+"-");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
var isresearch=null;
var reserialid;
function search(){
	var isbendian=false;
	var issearch = true;
	var data ={};
	var shopid = $("#requestShopid").val();
	var billno = $("#requestbillno").val();
	data.channel = channel;
	data.sheettype = 6;
	if(billno=='' ){
		return;
	}
	if(billno.indexOf("-")>0){
		for(i=0;i<shop.length;i++){
			if(billno.substr(0, 4) == shop[i].shopid){
				isbendian=true;
				break;
			}
		}
		
		if(!isbendian){
			$.myAlert("不是本店小票");
			return;
		}
	}
	
	data.ticketQC = billno;
	if(issearch){
		$.myAjax({
			url:$.getService("api","getInvoiceBillInfoWithDetail"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				
				/*for(i=0;i<shop.length;i++){
					if(data.data.shopid == shop[i].shopid){
						isbendian=true;
						break;
					}
				}*/
			 
				if(checkshopid!=dataJson.data.shopid){
					$.myAlert("不是本店小票");
					return;
				}
				
				if(dataJson.data.flag  == 1 && dataJson.data.iqseqno != null){
					isresearch=1;
					dataJson.data.flag="已开票";
					var params = {};
					params.iqseqno=dataJson.data.iqseqno;
					$.myAjax({
						url:$.getService("api","getOneInvque"),
						data:JSON.stringify(params),
						contentType:"application/json;charset=UTF-8",
						success: function (data) {
							if(data.code!=0){
								$.myAlert(data.message);
								return;
							}
							
							$.each( data.data, function(i, n){
								if(n.iqfplxdm=="025"){
									dataJson.data.iqfplxdm="卷票";
									n.iqfplxdm=="卷票";
								}else if(n.iqfplxdm=="026"){
									n.iqfplxdm=="电子票";
									dataJson.data.iqfplxdm="电子票";
								}else if(n.iqfplxdm=="004"){
									n.iqfplxdm=="专票";
									dataJson.data.iqfplxdm="专票";
								}else if(n.iqfplxdm=="007"){
									n.iqfplxdm=="普票";
									dataJson.data.iqfplxdm="普票";
								}
							});
							$.myRowHTML([dataJson.data],"rowmo","row1");
						},
						error: function (jqXHR, textStatus, errorThrown) {
							$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
						}
					});
				}else{
					isresearch=0;
					reserialid=dataJson.data.serialid;
					dataJson.data.flag="未开票";
					$.myRowHTML([dataJson.data],"rowmo","row1");
				}
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("没有查询到该小票! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}
	}

function research(){
	if(isresearch == 1){
		$.myAlert("已经开票无法重置");
	}else if(isresearch == 0){
		var params = {};
		params.serialid=reserialid;
		params.flag=-1;
		$.myAjax({
			url:$.getService("api","reset_bill"),
			data:JSON.stringify(params),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myAlert("重置成功");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("重置小票错误:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		
	}else{
		$.myAlert("无此小票信息");
	}
}
