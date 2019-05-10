var appID="11223344";
var userID="weixin-18995647365";

$(document).ready(function(){
	var iqseqno = $.getUrlParam("iqseqno");
	var params = {};
	params.iqperson = userID;
	params.iqseqno = iqseqno;
	params.pageSize=1;
	params.page=1;
	$.myAjax({
		url:$.getService("portal","main/getServerDate.action")+$.cookGetUrlParam(params),
		success: function (dataJson) {
			dataJson.data=[{}];
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
});

