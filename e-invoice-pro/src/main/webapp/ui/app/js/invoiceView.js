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
			$("#iqstatus").text(data.iqstatus>=40?"已开票":"正在开票，稍后再来");
			var downpdf = $("#downpdf");
			if(data.iqstatus>=40){
				downpdf.html("<i class=\"am-icon-file-pdf-o\"></i>下载电子发票（PDF）");
				downpdf.attr("href",data.iqpdf);
				downpdf.attr("target","_blank");
			}else{
				downpdf.text("电子发票开票中");
			}
			
			$.myAjax({
				url:$.getService("api","getInvqueList"),
				data:JSON.stringify(params),
				contentType:"application/json;charset=UTF-8",
				success: function (dataJson) {
					if(dataJson.code==0){
						var html = "";
						$.each( dataJson.data, function(i, n){
							html += "<div>小票号："+n.sheetid+" 开票金额："+n.invoiceamount+" </div>";
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

