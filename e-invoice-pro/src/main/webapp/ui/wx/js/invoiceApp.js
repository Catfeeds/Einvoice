var sheettype =1;
var isdanwei=0;//0个人，1单位
var previewData = {};
var checkedData = [];

$(document).ready(function(){
	//读取配置信息
	getConfig();
	//验证
	$('#div_info').validator();
	//提交
	$("#btn_submit").click(function(){
		$.myConfirm({title:"提醒",msg:"电子发票一经生成不得修改，不得换开，请核对无误后点击确认开具",onConfirm:commit});
	});
});

function getConfig(){
	$.myAjax({
		url:$.getService("wx","getWeixConfig"),
		data:null,
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code==0 && dataJson.data){
				sheettype=dataJson.data.sheettype;
				if(sheettype==''){
					$.myAlert("无法获取到业务类型");
					return;
				}else if(sheettype==1){
					$("#inputPiao").css("display","block");
				}else{
					$("#inputOther").css("display","block");
				}
				
				//录入电话或邮箱
				var usePhone = dataJson.data.usePhone;
				var useEmail = dataJson.data.useEmail;
				if(usePhone==1){
					$("#usePhone").css("display","block");
					$("#previewPhoneDiv").css("display","block");
				}
				if(useEmail==1){
					$("#useEmail").css("display","block");
					$("#previewMailDiv").css("display","block");
				}
				//读取用户默认信息
				getCustomerInfo();
				
			}else{
				$.myAlert("请从菜单进入");
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function getCustomerInfo(){
	$.myAjax({
		url:$.getService("api","getCustomerInfo"),
		data:null,
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code==0 && dataJson.data){
				$("#invRecvPhone").val(dataJson.data.phone);
				$("#invRecvMail").val(dataJson.data.email);
				if(dataJson.data.invoicetaxno=='' || dataJson.data.taxno == undefined){
					var title = dataJson.data.taxname;
					if(title==null || title=='') title = '个人';
					$("#invoiceHeadEntText").val(title);
					geren();
				}else{
					$("#invoiceHeadEntText").val(dataJson.data.taxname);
					$("#invoiceHeadEntTaxno").val(dataJson.data.taxno);
					$("#invoiceHeadEnt").attr("checked","checked");
					danwei();
				}
			}else{
				//尝试从cookie读取
				var phone = $.AMUI.utils.cookie.get("phone");
				var email = $.AMUI.utils.cookie.get("email");
				var taxno = $.AMUI.utils.cookie.get("taxno");
				var title = $.AMUI.utils.cookie.get("title");
				var addr = $.AMUI.utils.cookie.get("addr");
				var bank = $.AMUI.utils.cookie.get("bank");
				
				$("#invRecvPhone").val(phone);
				$("#invRecvMail").val(email);
				
				if(taxno==null || taxno==''){
					if(title==null || title=='') title = '个人';
					$("#invoiceHeadEntText").val(title);
					geren();
				}else{
					$("#invoiceHeadEntText").val(title);
					$("#invoiceHeadEntTaxno").val(taxno);
					$("#invoiceHeadDzdh").val(addr);
					$("#invoiceHeadYhzh").val(bank);
					danwei()
				}
				
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function search(){
	var str;
	if(sheettype==1){
		var shopid = $("#requestShopid").val();
		var syjid = $("#requestSyjid").val();
		var billno = $("#requestBillno").val();
		var je = $("#requestJe").val();
		if(billno=='' || je=='' ||shopid=='' ||syjid=='' ){
			return;
		}
		str = shopid+"-"+syjid+"-"+billno+"-"+je;
	}else{
		var sheetid = $("#requestSheetid").val();
		var je = $("#requestSheetJe").val();
		if(sheetid=='' || je==''){
			return;
		}
		str = sheetid+"-"+je;
	}
	
	var data ={};
	data.ticketQC = str;
	data.sheettype = sheettype;
	
	var issearch = true;

	for ( var i = 0; i < checkedData.length; i++) {
		if(str == checkedData[i]){
			$.myAlert("该单已经加载");
			return;
		}
	}
	
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
				
				checkedData.push(str);
				
				if(dataJson.data.flag == 0){
					$.myRowHTML([dataJson.data],"rowmo","row1",1);
				}else{
					$.myRowHTML([dataJson.data],"rowmo","row2",1);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("没有查询到该小票! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}
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
	requestBill.requestInvoicePreviewItem = [];
	//找到所有选中的小票
	var boxes = $("#weilist input[type=checkbox]");
	$.each( boxes, function(i, n){
		
		var m = $(n);
		if(m.is(":checked")) {
			var v = m.val();
			var oo = v.split('-');
			var item = {};
			if(sheettype==1){
				item.shopid = oo[0];
				item.syjid = oo[1];
				item.billno = oo[2];
				item.je = oo[3];
				item.sheetid=item.shopid+"-"+item.syjid+"-"+item.billno;
			}else{
				item.sheetid=oo[0];
				item.je = oo[1];
			}
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
	requestBill.sheettype = sheettype;
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
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			$.myConfirm({title:"成功",msg:"稍后电子发票将发送到您的短信和邮箱，请注意查收",btn1:"继续开票",btn2:"查看开票",onCancel:function(){window.location.reload();},onConfirm:function(){$.gotoUrl('./invoiceView.html?iqseqno='+iqseqno);}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
