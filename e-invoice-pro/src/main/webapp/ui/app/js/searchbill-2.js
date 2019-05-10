var user;
var channel = "app";
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
			shop=user.shoplist;
			checkshopid = user.shopid;
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function search(){
	var isbendian=false;
	var data ={};
	data.channel = channel;
	data.sheettype = 2;
	var requestbillno = $("#requestbillno").val();
	var shopid = $("#requestShopid").val();
	var syjid = $("#syjid").val();
	var billno = $("#billno").val();
	if(requestbillno!=null && requestbillno!=''){
		data.sheetid = requestbillno;
	}
	if(shopid!=null && shopid!=''){
		data.shopid = shopid;
	}
	if(syjid!=null && syjid!=''){
		data.syjid = syjid;
	}
	if(billno!=null && billno!=''){
		data.billno = billno;
	}
	
	$.myAjax({
		url:$.getService("api","getInvoiceSaleHeadList"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			
			for (var i = 0; i < dataJson.data.length; i++) {
				var n = dataJson.data[i];
				if(n.flag==1){
					n.flagmsg="已开票";
					if(n.invoicelx=="025"){
						n.invoicelx=="卷票";
					}else if(n.invoicelx=="026"){
						n.invoicelx=="电子票";
					}else if(n.invoicelx=="004"){
						n.invoicelx=="专票";
					}else if(n.invoicelx=="007"){
						n.invoicelx=="普票";
					}
					
					if(n.iqstatus==50){
						n.iqstatusmsg="成功";
					}else if(n.iqstatus==30){
						n.iqstatusmsg="失败";
					}else {
						n.iqstatusmsg="开票中"+iqstatus;
					}
				}else {
					n.flagmsg="未开票";
				}
			}
		 
			$.myRowHTML(dataJson.data,"rowmo","row1");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("服务异常 status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function research(serialid,flag){
	if(flag == 1){
		$.myAlert("已经开票，只能红冲发票后再次开具");
	}else if(flag == 0){
		var params = {};
		params.serialid=serialid;
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

function unlockpiao(serialid,iqseqno,flag,iqstatus){
	if(flag == 1 && iqstatus==30){
		var params = {};
		params.serialid=serialid;
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
	}else if(flag == 1 && iqstatus==50){
		$.myAlert("小票已开票，只能红冲原发票");
	}else if(flag == 1 && iqstatus<30){
		$.myAlert("小票正在开票，请等待开票结果后操作");
	}else{
		$.myAlert("小票已是解锁状态");
	}
}

function markBillno(sheetid){
	$("#requestbillno").val(sheetid);
}