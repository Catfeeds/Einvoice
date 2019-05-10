var channel = "app";

function search(page){
	var params = {};
	params.channel = channel;
	params.gmf_Nsrsbh = $("#nash").val();
	params.fphm = $("#fphm").val();
	params.fpdm = $("#fpdm").val();
	$.myAjax({
		url:$.getService("ui","queryInvoiceList"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myRowHTML(dataJson.data,"modelrows","searchRows");
			$.myPage(page,{jump:function(page){
				search(page);
			}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function detail(invoiceid){
	var params = {};
	params.invoiceid=invoiceid;
	$.myAjax({
		url:$.getService("ui","getInvoiceHeadById"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myRowHTML(dataJson.data,"modeldetailrows","detailRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

$(document).ready(function(){
	 search(0);
});

