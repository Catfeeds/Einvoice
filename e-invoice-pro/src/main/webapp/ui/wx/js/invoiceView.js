$(document).ready(function(){
	var iqseqno = $.getUrlParam("iqseqno");
	var params = {};
	params.iqseqno = iqseqno;
	$.myAjax({
		url:$.getService("api","getInvque"),
		data:JSON.stringify(params),
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.data.length==0){
				$.myAlert("没有获取到数据，请稍后再试");
				return;
			}
			var data = dataJson.data[0];
			
			$("#rtfpdm").text(data.rtfpdm);
			$("#rtfphm").text(data.rtfphm);
			$("#iqdate").text(TimeObjectUtil.longMsTimeConvertToDateTime(data.iqdate));
			$("#iqtotje").text(data.iqtotje);
			$("#iqtotse").text(data.iqtotse);
			$("#iqstatus").text(data.iqstatus>=40?"已开票":"开票中，如未收到，请电话咨询");
			if(data.iqstatus>=40 && data.iqpdf!=''){
				var downpdf = $("#downpdf");
				downpdf.html("<i class=\"am-icon-file-pdf-o\"></i>下载电子发票（PDF）");
				downpdf.attr("href",data.iqpdf);
				downpdf.attr("target","_blank");
				downpdf.css("display","block");
//				if(data.extUrl!=undefined && data.extUrl!=''){
//					downpdf.html("<i class=\"am-icon-file-pdf-o\"></i>查看发票，插入微信或支付宝卡包");
//					downpdf.attr("href",data.extUrl);
//					downpdf.attr("target","_blank");
//					downpdf.css("display","block");
//				}
				
				if(data.extUrl!=undefined && data.extUrl!=''){
					var sendpdf = $("#sendpdf");
					sendpdf.css("display","block");
					sendpdf.click(function(){
						$.myPrompt(
							{msg:"请确认接收邮箱",title:"发送到邮箱",value:data.iqemail,onConfirm:function(e){
								var iqseqno = $.getUrlParam("iqseqno");
								var email = e.data;
								var data ={};
								data.email = email;
								data.iqseqno = iqseqno;
								$.myAjax({
									url:$.getService("api","sendPdf"),
									data:JSON.stringify(data),
									contentType:"application/json;charset=UTF-8",
									success: function (dataJson) {
										if(dataJson.code!=0){
											$.myAlert(dataJson.message);
										}else{
											$.myAlert("发送中，稍后请在邮箱查收");
										}
									}
								});
							}
						});
					});
				}
				
			}else{
				if("SDYZ"==data.iqentid){
					var sendpdf = $("#sendpdf");
					sendpdf.css("display","block");
					sendpdf.click(function(){
						$.myPrompt(
							{msg:"请确认接收邮箱",title:"发送到邮箱",value:data.iqemail,onConfirm:function(e){
								var iqseqno = $.getUrlParam("iqseqno");
								var email = e.data;
								var data ={};
								data.email = email;
								data.iqseqno = iqseqno;
								$.myAjax({
									url:$.getService("api","sendPdf"),
									data:JSON.stringify(data),
									contentType:"application/json;charset=UTF-8",
									success: function (dataJson) {
										if(dataJson.code!=0){
											$.myAlert(dataJson.message);
										}else{
											$.myAlert("发送中，稍后请在邮箱查收");
										}
									}
								});
							}
						});
					});
				}
				//downpdf.text("电子发票开票中");
			}
			
			$.myAjax({
				url:$.getService("api","getInvqueList"),
				data:JSON.stringify(params),
				contentType:"application/json;charset=UTF-8",
				success: function (dataJson) {
					if(dataJson.code==0){
						var html = "";
						$.each( dataJson.data, function(i, n){
							html += "<div>单据号："+n.sheetid+" 单据金额："+n.totalamount+" </div>";
						});
						$("#quelist").html(html);
					}
				}
			});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
});


