var user;
var channel = "app";
$(document).ready(function(){
	//searchAdmin(0);
});


function search(page){
	var data ={};
	data.page=page;
	var requestfphm = $("#requestfphm").val();
	var requestsheetid = $("#requestsheetid").val();
	if(requestfphm !='' && requestfphm!=null){
		data.fphm=requestfphm;
	}
	if(requestsheetid !='' && requestsheetid!=null){
		data.sheetid=requestsheetid;
		data.sheettype=1;
	}
	
	$.myAjax({
		url:$.getService("ui","queryInvoiceHead"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			for (var i=0;i<dataJson.data.length;i++){
				if(dataJson.data[i].fprq != ''){
					dataJson.data[i].fprq=dataJson.data[i].fprq.substr(0,4)+"-"+dataJson.data[i].fprq.substr(4,2)+"-"+dataJson.data[i].fprq.substr(6,2);
				}
				if(dataJson.data[i].invoicetype == '0'){
					dataJson.data[i].invoicetype="蓝票";
				}else if(dataJson.data[i].invoicetype == '1'){
					dataJson.data[i].invoicetype="红冲票";
				}
				if(dataJson.data[i].status==90){
					dataJson.data[i].statusmsg='已红冲';
				}else if(dataJson.data[i].status==99){
					dataJson.data[i].statusmsg='已作废';
				}else{
					dataJson.data[i].statusmsg='正常';
				}
			}
			$.myPage(page,{rows:dataJson.count,jump:function(page){
				search(page);
			}});
			$.myRowHTML(dataJson.data,"rowmo","row1");
			$("#pagecount").html("共" +dataJson.count+ "条数据");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

var showinvoiceid;
function showModal(invoiceid){
	showinvoiceid=invoiceid;
	$('#taxmodal').modal({width:'800',height:'500'});
	showDetail(0);
}

function showDetail(page){
	var data={};
	data.page=page;
	data.pagesize=8;
	data.invoiceid=showinvoiceid;
	$.myAjax({
		url:$.getService("ui","getInvoiceDetail"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myPage(page,{rows:dataJson.count,pagesize:8,targetId:"page1",jump:function(page){
				showDetail(page);
			}});
			$.myRowHTML(dataJson.data,"rowmo2","searchRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function hongchong(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条数据");
		 return;
	}
	
	var ss = selectrow[0].value.split(",");
	if(ss[4]!=100){
		$.myAlert("仅正常状态发票可操作");
		return;
	}
	var iqseqno = ss[0];
	var yfpdm = ss[1];
	var yfphm = ss[2];
	data.iqseqno=iqseqno;
	data.yfpdm=yfpdm;
	data.yfphm=yfphm;
	
	$.myAjax({
		url:$.getService("api","hongchongInvoice"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myAlert("红冲成功");
			searchAdmin(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function zuofei(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条数据");
		 return;
	}
	
	var ss = selectrow[0].value.split(",");
	if(ss[4]!=100){
		$.myAlert("仅正常状态发票可操作");
		return;
	}
	var iqseqno = ss[0];
	var yfpdm = ss[1];
	var yfphm = ss[2];
	var lxdm = ss[3];
	if(lxdm=='电子票' || lxdm=='026'){
		$.myAlert("电子票不能作废");
		 return;
	}
	data.iqseqno=iqseqno;
	data.yfpdm=yfpdm;
	data.yfphm=yfphm;
	data.zflx=1;
	$.myAjax({
		url:$.getService("api","fp_zuofei"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myAlert("作废成功");
			searchAdmin(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function re_print(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条数据");
		 return;
	}
	var iqseqno = selectrow[0].value.split(",")[0];
	data.iqseqno=iqseqno;
	$.myAjax({
		url:$.getService("api","fp_print"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.data.isqingdan != '' && dataJson.data.isqingdan == 1){
				$.myConfirm({title:"提醒",msg:"请放入清单纸",onConfirm:fp_printforQd});
			}else{
				$('#my-yulan').modal('close');
				window.location.href=window.location.href;
			}
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("发票打印错误! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
	
}

function fp_printforQd(){
	var data ={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	var iqseqno = selectrow[0].value.split(",")[0];
	data.iqseqno=iqseqno;
	$.myAjax({
		url:$.getService("api","fp_printforQd"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$('#my-yulan').modal('close');
			window.location.href=window.location.href;
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("清单打印错误! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function download(url){
	if(url != null && url != ''){
		var link= $('<a href="'+url+'" target="_blank"></a>');
		link.get(0).click();
	}else{
		$.myAlert("仅电票可下载");
	}
}

function sendPdf(iqseqno,iqemail,lxdm){
	if(lxdm!='电子票') return;
	$.myPrompt(
		{msg:"请确认接收邮箱",title:"发送到邮箱",value:iqemail,onConfirm:function(e){
			var email = e.data;
			if(email==''){alert("邮箱不能空");return false;}
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
}
