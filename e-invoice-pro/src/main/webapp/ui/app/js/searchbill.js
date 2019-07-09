var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
$(document).ready(function(){
	getuser();
});
var iqseqno;

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
			if (user.shoplist.length == 1) {
				$("#requestbillno").val(user.shoplist[0].shopid+"-");
		    }
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
	data.sheettype = 1;
	if(billno=='' ){
		return;
	}
	if(billno.indexOf("-")>0){
		 
		for(i=0;i<shop.length;i++){
			if(billno.split("-")[0] == shop[i].shopid){
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
					//if(dataJson.data.iqstatus==30){ //如果是失败队列记录到全局变量iqseqno,解锁小票时队列标记为99
						iqseqno=dataJson.data.iqseqno;
						reserialid=dataJson.data.serialid;
					//}
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
							if(dataJson.data.isauto=="1"){ dataJson.data.isauto ="是";}else{dataJson.data.isauto="否"; }
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

function unlockpiao(){
	if(isresearch == null||isresearch==''){
		$.myAlert("请先查询小票信息");
	}else
	if(isresearch == 1){
		var data ={};
		var billno = $("#requestbillno").val();
		if(billno=='' ){
			return;
		}
		if(billno !='' && billno!=null){
			data.sheetid=billno;
		}
		 isret="0";//如果有发票数据return   0为该小票没有发票 1为有发票
		$.myAjax({
			url:$.getService("ui","getInvoiceHeadByAdmin"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				//按查找该小票是否有有效正数发票判断
/*				for (var i=0;i<dataJson.data.length;i++){
					if(dataJson.data[i].invoicetype == '0'&&dataJson.data[i].status==100){
						$.myAlert("小票已经开票不能解锁！");
						isret ='1';
						break;
					} 
				}
				if(isret =='1') return;*/
				//按查找该小票是否有开过发票判断
				if(dataJson.count>0){
					$.myAlert("该小票已经开过发票不能解锁");
				}else{
					var params = {};
					params.serialid=reserialid;
					params.flag=0;
					params.iqseqno = iqseqno;
					$.myAjax({
						url:$.getService("api","reset_bill"),
						data:JSON.stringify(params),
						contentType:"text/html;charset=UTF-8",
						success: function (dataJson) {
							if(dataJson.code!=0){
								$.myAlert(dataJson.message);
								return;
							}
							$.myAlert("解锁成功");
						},
						error: function (jqXHR, textStatus, errorThrown) {
							$.myAlert("解锁小票错误:"+textStatus+" msg:"+jqXHR.responseText);
						}
					});
				}
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}else{
		$.myAlert("小票已是解锁状态");
	}
}
