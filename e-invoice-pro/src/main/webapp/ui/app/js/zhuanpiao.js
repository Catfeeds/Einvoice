var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
$(document).ready(function(){
	getuser();
});

var shop;
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
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
var isflag=false;//未开，可标识
var iszhuan=false;//是否专票
var ispupiao=false;//是否普票
var isewmzuofei=false;//是否二维码作废
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
/*	for(i=0;i<shop.length;i++){
		if(billno.substr(0, 4) == shop[i].shopid){
			isbendian=true;
			break;
		}
	}
	
	if(!isbendian){
		$.myAlert("不是本店小票");
		return;
	}*/
	
	
	data.ticketQC = billno;
	if(issearch){
		$.myAjax({
			url:$.getService("api","getInvoiceBillInfo"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
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
                                    ispupiao=true;
									dataJson.data.iqfplxdm="普票";
								}else if(n.iqfplxdm=="100"){
                                    isewmzuofei=true;
                                    dataJson.data.iqfplxdm="二维码已作废";
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
                    ispupiao=false;
                    isewmzuofei=false;
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
	if(weilist.length == 0){
		$.myAlert("没有查询到此小票号，不能标识");
		return;
	}
	if(isflag){
		$.myAlert("此小票号已开过发票，不能标识");
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
			$.myAlert("标识已开专票设置成功");
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
		$.myAlert("没有查询到此小票号，不能标识取消");
		return;
	}
    if(!isflag){
        $.myAlert("此小票不是已开票，不能标识取消！");
        return;
    }
    if (!(iszhuan||isewmzuofei||ispupiao)){
        $.myAlert("此小票状态不正确，不能标识取消");
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
			$.myAlert("标识取消设置成功");
			search();
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function ykpupiao(){
    var params = {};
    params.isLock=1;
    params.invoiceType="007";

    var weilist = $("#weilist input[type=checkbox]");
    if(weilist.length == 0){
        $.myAlert("没有查询到此小票号，不能标识");
        return;
    }
    if(isflag){
        $.myAlert("此小票号已开过发票，不能标识");
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
            $.myAlert("标识已开普票设置成功");
            search();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
        }
    });

}
function ewmzuofei(){
    var params = {};
    params.isLock=1;
    params.invoiceType="100";

    var weilist = $("#weilist input[type=checkbox]");
    if(weilist.length == 0){
        $.myAlert("没有查询到此小票号，不能标识");
        return;
    }
    if(isflag){
        $.myAlert("此小票号已开过发票，不能标识");
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
            $.myAlert("标识二维码作废设置成功");
            search();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
        }
    });

}
