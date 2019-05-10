var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
var print_iqsen;
$(document).ready(function(){
	geren();
	getuser();
	searchKB();
	//验证
	$('#div_info').validator();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
	//打印
	$("#btn_print").click(function(){
		fp_print();
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
	var billno = $("#requestbillno").val();
	data.channel = channel;
	data.sheettype = 2;//批发单
	if(billno=='' ){
		return;
	}
	data.ticketQC = billno+"-0";
	
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
							$.myAlert("已经查询过该批发单");
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
							$.myAlert("已经查询过该批发单");
							issearch = false;
							return false;
						}
					});
					if(issearch){
						$.myRowHTML([dataJson.data],"rowmo","row2",1);
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("没有查询到该批发单! status:"+textStatus+" msg:"+jqXHR.responseText);
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
//	$("#invoiceHeadEntText").attr("disabled","disabled");
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
	var dqfpdm = $("#kbfpdm").text();//空白发票代码
	var dqfphm = $("#kbfphm").text();//空白发票号码
	var iqfplxdm = $('#iqfplxdm').val();
	if(isNaN(dqfpdm) || isNaN(dqfphm)){
		$.myAlert("请先查询空白票");
		return ;
	}
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
			var item = {};
			item.sheetid=m.val();
			requestBill.requestInvoicePreviewItem.push(item);
		}
	});
	if(requestBill.requestInvoicePreviewItem.length==0){
		$.myAlert("至少选中一张批发单");
		return ;
	}
	$('#my-yulan').modal();
	requestBill.gmfNsrsbh = invoiceHeadEntTaxno;
	requestBill.gmfMc = invoiceHeadEntText;
	requestBill.gmfDzdh = invoiceHeadDzdh;
	requestBill.gmfYhzh = invoiceHeadYhzh;
	requestBill.dqfpdm = dqfpdm;
	requestBill.dqfphm = dqfphm;
	requestBill.iqfplxdm = iqfplxdm;
	requestBill.invType = "1";
	requestBill.sheettype = 2;
	askPreView(requestBill);
}

function askPreView(requestBill){
	print_iqsen="";
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
			$("#dqfpdm").text(requestBill.dqfpdm);
			$("#dqfphm").text(requestBill.dqfphm);
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
			print_iqsen=iqseqno;
			$.myAlert("开票成功,请放入发票纸打印");
			$('#btn_submit').attr("disabled","disabled");
			$("#btn_print").removeAttr("disabled");
			searchKB();
			$("#row1").html("");
			$("#row2").html("");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function searchKB(){
	var iqfplxdm = $('#iqfplxdm').val();
	var data ={};
	//004:专票 007:普票 
	data.iqfplxdm=iqfplxdm;
	 $.myAjax({
			url:$.getService("api","getBlankInvoice"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myRowHTML([dataJson.data],"fpdmmo","fpdm");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("空白票查询错误! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
}

function fp_print(){
	var data ={};
	data.iqseqno=print_iqsen;
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
	data.iqseqno=print_iqsen;
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

function clearlist(){
	$("#row1").html("");
	$("#row2").html("");
}

function zuofei(){
	var data={};
	var iqfplxdm = $('#iqfplxdm').val();
	data.zflx=0;
	data.iqfplxdm=iqfplxdm;//发票类型（如普票）
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
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}