var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
var params = {};
params.channel = channel;
$(document).ready(function(){
	geren();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
});

function geren(){
	isdanwei=0;
	$("#geren").addClass("am-active");
	$("#danwei").removeClass("am-active");
	$("#invoiceHeadEntText").val('个人');
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadDzdh").val('');//地址电话
	$("#invoiceHeadYhzh").val('');//银行账户
	$("#invRecvMail").val('');//发票接收人邮箱
	$("#invoiceHeadEntTaxno").attr("disabled","disabled"); 
}
function danwei(){
	isdanwei=1;
	$("#danwei").addClass("am-active");
	$("#geren").removeClass("am-active");
	$("#invoiceHeadEntText").val('');
	$("#invoiceHeadDzdh").val('');//地址电话
	$("#invoiceHeadYhzh").val('');//银行账户
	$("#invRecvMail").val('');//发票接收人邮箱
	$("#invoiceHeadEntTaxno").removeAttr("disabled"); 
	$("#invoiceHeadEntText").removeAttr("disabled");
}
function add(){
    var tr=$("#table1 tr:last");
    
    var add=$("#addnew").clone();
    $("#table1").append(add);
 }
function del(m){
	m.parentNode.parentNode.remove(); 
}
function kaipiao(){
	params.sheettype = 3;
	params.requestBillItem=[];
	var ischeck = true;
	
	$("#table1").find("tr").each(function(i, n){
		 if(i==0){return true;}
        var tdArr = $(this).children();
        var item = {};
        item.taxitemid = tdArr.eq(1).find('input').val();//税目
        item.goodsname = tdArr.eq(2).find('select').val();//名称
        item.unit = tdArr.eq(3).find('input').val();//单位
        item.qty = tdArr.eq(4).find('input').val();//数量
        item.price = tdArr.eq(5).find('input').val();//单价
        item.amt = tdArr.eq(6).find('input').val();//含税金额
        item.taxrate = tdArr.eq(7).find('input').val();//税率
        item.taxpre = tdArr.eq(8).find('select').val();//是否享受优惠
        item.taxprecon = tdArr.eq(9).find('input').val();//优惠类型
        item.zerotax = tdArr.eq(10).find('select').val();//零税率标识
        item.tradeDate="";
        if(item.taxitemid == null || item.taxitemid == ''){
        	$.myAlert("请选择税目");
        	ischeck=false;
        	return false;
        }
        if(item.goodsname == null || item.goodsname == ''){
        	$.myAlert("请输入名称");
        	ischeck=false;
        	return false;
        }
        if(item.qty == null || item.qty == ''){
        	$.myAlert("请输入数量");
        	ischeck=false;
        	return false;
        }
        if(item.amt == null || item.amt == ''){
        	$.myAlert("请输入金额");
        	ischeck=false;
        	return false;
        }
        if(item.taxrate == null || item.taxrate == ''){
        	$.myAlert("请输入税率");
        	ischeck=false;
        	return false;
        }
        params.requestBillItem.push(item);
    });
	if(!ischeck){
		return;
	}
	var invoiceHeadEntText = $("#invoiceHeadEntText").val();//公司名称，抬头
	var invoiceHeadEntTaxno = $("#invoiceHeadEntTaxno").val();//纳税人识别号
	var invoiceHeadDzdh = $("#invoiceHeadDzdh").val();//地址电话
	var invoiceHeadYhzh = $("#invoiceHeadYhzh").val();//银行账户
	var iqfplxdm = $('#iqfplxdm').val();
	var invRecvMail = $("#invRecvMail").val();//发票接收人邮箱
	
	if(invoiceHeadEntText == '' || invoiceHeadEntText == null){
		$.myAlert("请输入单位名称");
		return ;
	}
	//个人
/*	if(isdanwei == 0){
	}else{
		if(invoiceHeadEntTaxno == null || invoiceHeadEntTaxno == ''){
			$.myAlert("请输入纳税人识别号");
			return ;
		}
	}*/
	if(iqfplxdm == '026' && invRecvMail==''){
		$.myAlert("请输入邮箱");
		return ;
	}
	
	if(!$('#div_info').validator("isFormValid")) return;
	
	var requestBill ={};
	//1有小票发票，0手工录入发票
	requestBill.source = 0;
	requestBill.channel=channel;
	$('#my-yulan').modal();
	requestBill.gmfNsrsbh = invoiceHeadEntTaxno;
	requestBill.gmfMc = invoiceHeadEntText;
	requestBill.gmfDzdh = invoiceHeadDzdh;
	requestBill.gmfYhzh = invoiceHeadYhzh;
	requestBill.iqfplxdm = iqfplxdm;
	//开票类型 0蓝字、1红字、2作废
	requestBill.invType = 0;
	requestBill.sheettype = 3;
	requestBill.recvEmail=invRecvMail;
	requestBill.requestInvoicePreviewItem = [];
	$.myAjax({
		url:$.getService("api","getInvoiceBillInfo"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			params.action="update";
			params.sheetid=dataJson.data.sheetid;
			timepre(requestBill);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
//	$('#btn_print').attr("disabled","disabled");
	$("#btn_submit").removeAttr("disabled");
}
function timepre(requestBill){
	var itemss={};
	itemss.sheetid=params.sheetid;
	itemss.je=0;
	requestBill.requestInvoicePreviewItem.push(itemss);
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
			$.myAlert("开票成功");
			$('#btn_submit').attr("disabled","disabled");
			params.action="";
			params.sheetid="";
			$('#my-yulan').modal('close');
			window.location.href=window.location.href;
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function jisuan(e){
	var selectTr=$(e).parents("tr");
	var qty = selectTr.find("input[name='qty']").val();
	var price = selectTr.find("input[name='price']").val();
	if(isNaN(qty)){
		$.myAlert("输入的数量不是数字");
		return;
	}
	if(isNaN(price)){
		$.myAlert("输入的单价不是数字");
		return;
	}
	selectTr.find("input[name='amount']").val(qty*price);
}


function clearkehu(){
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadDzdh").val('');//地址电话
	$("#invoiceHeadYhzh").val('');//银行账户
	$("#invRecvMail").val('');//发票接收人邮箱
}
//获取用户初始化信息电话邮箱抬头等
function getCustomerInfo(){
	var data={};
	data.taxname=$("#invoiceHeadEntText").val();
	if(!data.taxname){
		clearkehu();
	}
	if(data.taxname != ''){
		$( "#invoiceHeadEntText" ).autocomplete({
			 source: function( request, response ) {
				 $.myAjax({
						url:$.getService("api","getWebCustomerInfo"),
						data:JSON.stringify(data),
						contentType:"application/json;charset=UTF-8",
						success: function (dataJson) {
							if(dataJson.code==0 && dataJson.data){
								var myArray=new Array();
								for(var i=0;i<dataJson.data.length;i++){
									myArray[i]=dataJson.data[i].taxname;
								}
								 response($.map(dataJson.data,function(item){  
			                            var name = item.name;  
			                            var id = item.id;  
			                            return {  
			                                label:item.taxname,//下拉框显示值  
			                                value:item.taxname,//选中后，填充到下拉框的值  
			                                taxno:item.taxno,
			                                taxaddr:item.taxaddr,
			                                taxbank:item.taxbank,
			                                email:item.email
			                            }  
			                        }));  
							}
						},
						error: function (jqXHR, textStatus, errorThrown) {
							$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
						}
					});
				 
			 },
			 select:function changesel(event, ui ){
				 changeCustomerInfo(ui.item);
			 }
		    });
		
	}
	
}

function changeCustomerInfo(data){
//			$("#invoiceHeadEntText").val(data.taxname);
			$("#invoiceHeadEntTaxno").val(data.taxno);
			$("#invoiceHeadDzdh").val(data.taxaddr);
			$("#invoiceHeadYhzh").val(data.taxbank);
			$("#invRecvMail").val(data.email);
		}


function updateCustomerInfo(){
	var data={};
	data.taxname = $("#invoiceHeadEntText").val();
	data.taxno = $("#invoiceHeadEntTaxno").val();
	data.taxaddr = $("#invoiceHeadDzdh").val();
	data.taxbank = $("#invoiceHeadYhzh").val();
	data.email = $("#invRecvMail").val();
	
	var postData =JSON.stringify(data);
	$.myAjax({
		url:$.getService("api","updateCustomerInfo"),
		data:postData,
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
		},
		error: function (jqXHR, textStatus, errorThrown) {
		}
	});
}

