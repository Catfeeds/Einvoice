var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
$(document).ready(function(){
	getuser();
});

var shop;
var checkshopid
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
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
var isflag=false;//未开，可标识
var iszhuan=false;//是否专票
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
				
				if(checkshopid!=dataJson.data.shopid){
					$.myAlert("不是本店小票");
					return;
				}
				
				if(dataJson.data.flag == 1){
					isflag=true;
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
								}else if(n.iqfplxdm=="026"){
									dataJson.data.iqfplxdm="电子票";
								}else if(n.iqfplxdm=="004"){
									iszhuan=true;
									dataJson.data.iqfplxdm="专票";
								}else if(n.iqfplxdm=="007"){
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
					isflag=false;
					iszhuan=false;
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

function yikai(){
	var params = {};
	params.isLock=1;
	
	var weilist = $("#weilist input[type=checkbox]");
	var fpdm = $("#requestfpdm").val();
	var fphm = $("#requestfphm").val();
	if(weilist.length == 0){
		$.myAlert("没有查询到此小票号，不能标识");
		return;
	}
	if(isflag){
		$.myAlert("此小票号已开过发票，不能标识");
		return;
	}
	if(fpdm == ''){
		$.myAlert("请填写发票代码！");
		return;
	}
	
	if(fphm == ''){
		$.myAlert("请填写发票号码！");
		return;
	}
	
	params.sheetid=weilist.val();
	params.sheettype='6';
	params.requestfpdm= fpdm;
	params.requestfphm= fphm;
	$.myAjax({
		url:$.getService("api","lockedSheetInvoice"),
		data:JSON.stringify(params),
		contentType:"application/json;charset=UTF-8",
		success: function (data) {
			if(data.code!=0){
				$.myAlert(data.message);
				return;
			}
			$.myAlert("设置成功");
			search();
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
	
}
function weikai(){
	var params = {};
	params.isLock=0;
	
	var weilist = $("#weilist input[type=checkbox]");
	if(weilist.length == 0){
		$.myAlert("没有查询到此小票号，不能标识");
		return;
	}
	if(!isflag){
		$.myAlert("此小票号不是专票，不能标识");
		return;
	}
	if(!iszhuan){
		$.myAlert("此小票号不是专票，不能标识");
		return;
	}
	params.sheetid=weilist.val();
	$.myAjax({
		url:$.getService("api","lockedSheetInvoice"),
		data:JSON.stringify(params),
		contentType:"application/json;charset=UTF-8",
		success: function (data) {
			if(data.code!=0){
				$.myAlert(data.message);
				return;
			}
			$.myAlert("设置成功");
			search();
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}


