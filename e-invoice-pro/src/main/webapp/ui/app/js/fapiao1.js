var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
$(document).ready(function(){
	geren();
	getuser();
	//验证
	$('#div_info').validator();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
});

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
			$("#requestShopid").val(user.shopid);
			$("#requestShopname").val(user.shopname);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function search(){
	var str;
	var issearch = true;
	var data ={};
	var shopid = $("#requestShopid").val();
	var syjid = $("#requestSyjid").val();
	var billno = $("#requestbillno").val();
	data.channel = channel;
	data.sheettype = 1;
	if(shopid=='' ||syjid=='' ||billno=='' ){
		return;
	}
	str = shopid+"-"+syjid+"-"+billno;
	data.ticketQC = str;
	
		$.myAjax({
			url:$.getService("api","getInvoiceBillInfo"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data.flag == 0){
					var weilist = $("#weilist input[type=checkbox]");
					$.each( weilist, function(i, n){
						var m = $(n);
						if(dataJson.data.sheetid == m.val()){
							$.myAlert("已经查询过该小票");
							issearch = false;
							return false;
						}
					});
					if(issearch){
						$.myRowHTML([dataJson.data],"rowmo","row1",1);
					}
				}else{
					var yilist = $("#yilist input[type=checkbox]");
					$.each( yilist, function(i, n){
						var m = $(n);
						if(dataJson.data.sheetid == m.val()){
							$.myAlert("已经查询过该小票");
							issearch = false;
							return false;
						}
					});
					if(issearch){
						//查询发票信息
						var params = {};
						params.iqseqno = dataJson.data.iqseqno;
						$.myAjax({
							url:$.getService("api","getInvque"),
							data:JSON.stringify(params),
							contentType:"application/json;charset=UTF-8",
							success: function (dataJson2) {
								if(dataJson2.code!=0){
									$.myAlert(dataJson2.message);
									return;
								}
								var data = dataJson2.data[0];
								dataJson.data.pdf = data.iqpdf;
								dataJson.data.extUrl = data.extUrl;
								dataJson.data.iqemail = data.iqemail;
								$.myRowHTML([dataJson.data],"rowmo2","row2",1);
							}
						});
						
						
						
						
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("没有查询到该小票! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}

function geren(){
	isdanwei=0;
	$("#geren").addClass("am-active");
	$("#danwei").removeClass("am-active");
	$("#invoiceHeadEntText").val('个人');
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadEntTaxno").attr("disabled","disabled");
}
function danwei(){
	isdanwei=1;
	$("#danwei").addClass("am-active");
	$("#geren").removeClass("am-active");
	$("#invoiceHeadEntText").val('');
	$("#invoiceHeadEntTaxno").removeAttr("disabled"); 
	$("#invoiceHeadEntText").removeAttr("disabled");
}

function add(){
    var tr=$("#table1 tr:last");
    tr.append($("#addnew").innerHtml);
 }

function kaipiao(){
	var invoiceHeadEntText = $("#invoiceHeadEntText").val();//公司名称，抬头
	var invoiceHeadEntTaxno = $("#invoiceHeadEntTaxno").val();//纳税人识别号
	var invoiceHeadDzdh = $("#invoiceHeadDzdh").val();//地址电话
	var invoiceHeadYhzh = $("#invoiceHeadYhzh").val();//银行账户
	var invRecvPhone = $("#invRecvPhone").val();//发票接收人电话
	var invRecvMail = $("#invRecvMail").val();//发票接收人邮箱
	
	if(invoiceHeadEntText == '' || invoiceHeadEntText == null){
		$.myAlert("请输入单位名称");
		return ;
	}
	//个人
	if(isdanwei == 0){
	}else{
		if(invoiceHeadEntTaxno == null || invoiceHeadEntTaxno == ''){
			$.myAlert("请输入纳税人识别号");
			return ;
		}
	}
	if(invRecvMail == '' || invRecvMail == null){
		$.myAlert("请输入邮箱");
		return ;
	}
	if(!$('#div_info').validator("isFormValid")) return;
	
	var requestBill ={};
	//1有小票发票，0手工录入发票
	requestBill.source = 1;
	requestBill.channel=channel;
	requestBill.requestInvoicePreviewItem = [];
	//找到所有选中的小票
	var boxes = $("#weilist input[type=checkbox]");
	$.each( boxes, function(i, n){
		
		var m = $(n);
		if(m.is(":checked")) {
			var v = m.val();
			var oo = v.split('-');
			var item = {};
			item.shopid = oo[0];
			item.syjid = oo[1];
			item.billno = oo[2];
			item.sheetid=item.shopid+"-"+item.syjid+"-"+item.billno;
			requestBill.requestInvoicePreviewItem.push(item);
		}
	});
	if(requestBill.requestInvoicePreviewItem.length==0){
		$.myAlert("至少选中一张小票");
		return ;
	}
	$('#my-yulan').modal();
	requestBill.gmfNsrsbh = invoiceHeadEntTaxno;
	requestBill.gmfMc = invoiceHeadEntText;
	requestBill.gmfDzdh = invoiceHeadDzdh;
	requestBill.gmfYhzh = invoiceHeadYhzh;
	requestBill.recvPhone = invRecvPhone;
	requestBill.recvEmail = invRecvMail;
	requestBill.invType = "1";
	requestBill.sheettype = 1;
	askPreView(requestBill);
}

function askPreView(requestBill){
	$("#preview").html("");
	var postData =JSON.stringify(requestBill);
	$.myAjax({
		url:$.getService("api","getInvoicePreview"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			//把预览的数据存下来，正式提交发票时要用到
			previewData = dataJson.data;
			$.myRowHTML(dataJson.data,"rowPreview","preview",1);
			$("#previewPhone").text(requestBill.recvPhone);
			$("#previewMail").text(requestBill.recvEmail);
			//写入明细信息
			for ( var i = 0; i < dataJson.data.length; i++) {
				$.myRowHTML(dataJson.data[i].invoicePreviewItem,"rowPreviewDetail","previewDetail"+i,2);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

//提交开票申请
function commit(){
	var postData =JSON.stringify(previewData);
	$.myAjax({
		url:$.getService("api","askInvoice"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			if(previewData[0].gmfNsrsbh != null && previewData[0].gmfNsrsbh != ''){
				updateCustomerInfo();
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			$("#row1").html("");
			$("#row2").html("");
			$.myConfirm({title:"成功",msg:"稍后电子发票将发送到您的短信和邮箱，请注意查收",btn1:"继续开票",btn2:"查看开票",onCancel:function(){window.location.reload();},onConfirm:function(){$.gotoUrl('./invoiceView.html?iqseqno='+iqseqno);}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
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

function sendPdf(iqseqno,iqemail){
	$.myPrompt(
		{msg:"请确认接收邮箱",title:"发送到邮箱",value:iqemail,onConfirm:function(e){
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
}
