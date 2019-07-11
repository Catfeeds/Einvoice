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
			var myData = dataJson.data[0];
			
			$("#rtfpdm").text(myData.rtfpdm);
			$("#rtfphm").text(myData.rtfphm);
			$("#iqdate").text(TimeObjectUtil.longMsTimeConvertToDateTime(myData.iqdate));
			$("#iqtotje").text(myData.iqtotje);
			$("#iqtotse").text(myData.iqtotse);
			$("#iqstatus").text(myData.iqstatus>=40?"已开票":"开票中，如未收到，请电话咨询");
			
			if(myData.iqstatus>=40 && myData.iqpdf!=''){
				//下载PDF
				var downpdf = $("#downpdf");
				downpdf.html("<i class=\"am-icon-file-pdf-o\"></i>下载电子发票（PDF）");
				downpdf.attr("href",myData.iqpdf);
				downpdf.attr("target","_blank");
				downpdf.css("display","block");
				//发送邮件
				if(myData.extUrl!=undefined && myData.extUrl!=''){
					var sendpdf = $("#sendpdf");
					sendpdf.css("display","block");
					sendpdf.click(function(){
						$.myPrompt(
							{msg:"请确认接收邮箱",title:"发送到邮箱",value:myData.iqemail,onConfirm:function(e){
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
				//插微信卡包
				if(myData.iqtype == 0) {
					var wechatcard = $("#wechatcard");
					wechatcard.html("<i class=\"am-icon-file-pdf-o\"></i>插入微信卡包");
					wechatcard.attr("target","_blank");
					wechatcard.css("display","block");
					wechatcard.attr('disabled',"true");
					if (myData.iqinvoicetimes != -1)
					{
						var iqseqno = $.getUrlParam("iqseqno");
						var data ={};
						data.iqseqno = iqseqno;
						$.myAjax({
							url:$.getService("openapi","wechatcard"),
							data:JSON.stringify(data),
							contentType:"application/json;charset=UTF-8",
							success: function (dataJson) {
								if(dataJson.code != 0){
									$.myAlert(dataJson.message);
								}
								else{
									var wechaUrl = dataJson.message;
									wechatcard.removeAttr('disabled');
									wechatcard.attr("href",wechaUrl)	
								}
							},
							error: function() {wechatcard.attr('disabled',"true");}
						});
					}
				}
			}
			else{
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


