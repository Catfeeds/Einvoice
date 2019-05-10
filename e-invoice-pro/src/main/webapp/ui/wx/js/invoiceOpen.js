var animaProcessing = false; // tab页动画进度的控制,防止多个滑动同时执行,出现一个页面显示两个tab的情况
var sheettype =1;

var previewData = {};

var checkedData = [];

var weixinNote;

var isLoading=false;

function isWeix(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		return true;
	} else {
		return false;
	}
}

$(document).ready(function(){
	getConfig();
	
	$("#buttonCheck").click(function(){
		var str;
		if(sheettype==1){
			var shopid = $("#requestShopid").val();
			var syjid = $("#requestSyjid").val();
			var billno = $("#requestShinvno").val();
			var je = $("#requestJe").val();
			if(billno=='' || je=='' ||shopid=='' ||syjid=='' ){
				return;
			}
			str = shopid+"-"+syjid+"-"+billno+"-"+je;
		}else if(sheettype==5){
			str = $("#requestSheetid").val();
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
		
		getbillInfo(data);
	});
	
	//验证
	if ($.AMUI && $.AMUI.validator) {
		$.AMUI.validator.patterns.mobile = /^\s*1\d{10}\s*$/;
	}
	$('#div_info').validator();
	
	// 翻页按钮
	$("#btn_next , #btn_prev").click(function() {
		changeTab(this);
	});
	
	//提交
	$("#btn_submit").click(function(){
		if(isLoading) return;
		$.myConfirm({title:"提醒",msg:"电子发票一经生成不得修改，不得换开，请核对无误后点击确认开具",onConfirm:commit});
	});
	
	//绑定点击
	$("#invoiceHeadPersonText,#invoiceHeadPerson").click(function() {
		geren();
	});
	$("#invoiceHeadEntText,#invoiceHeadEntTaxno,#invoiceHeadEntAddr,#invoiceHeadEntBank,#invoiceHeadEnt").click(function() {
		danwei();
	});
	
	$("#requestShopid").change(function(){
		var str = $("#requestShopid").val();
		console.log(str);
		if(str!=''){
			var ss = str.split("-");
			if(ss.length==3){
				$("#requestShopid").val(ss[0]);
				$("#requestSyjid").val(ss[1]);
				$("#requestShinvno").val(ss[2]);
			}
		}
	});
	
});

function geren(){
	$("#invoiceHeadPerson").prop("checked",true);
	$("#liPerson").removeClass("am-hide");
	$("#liText").addClass("am-hide");
	$("#liTaxno").addClass("am-hide");
	$("#liAddr").addClass("am-hide");
	$("#liBank").addClass("am-hide");
	if($("#invoiceHeadPersonText").val()==''){
		$("#invoiceHeadPersonText").val('个人')
	}
}
function danwei(){
	$("#invoiceHeadEnt").prop("checked",true);
	$("#invoiceHeadPersonText").val("");
	$("#liPerson").addClass("am-hide");
	$("#liText").removeClass("am-hide");
	$("#liTaxno").removeClass("am-hide");
	$("#liAddr").removeClass("am-hide");
	$("#liBank").removeClass("am-hide");
}

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
				
				weixinNote = dataJson.data.weixinNote;
				
				//是否展示帮助图
				if(dataJson.data.weixinHelp!=undefined){
					$("#wxHelp figure").removeClass("am-hide");
					$("#wxHelp figure img").attr("data-rel",dataJson.data.weixinHelp);
					
					$("#requestShopid").attr("placeholder","门店号");
					$("#requestSyjid").attr("placeholder","收银号");
					$("#requestShinvno").attr("placeholder","流水号");
					$("#requestJe").attr("placeholder","金额");
				}else{
					$("#wxHelp label").removeClass("am-hide");
				}
				
				//是否启用扫码
				var useQR = dataJson.data.useQR;
				if(useQR==1){
					initQR();
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
				
				//填入小票信息
				var pqr = dataJson.data.qr;
				if(pqr!=null && sheettype==1 && pqr.split("-").length==4){
					var ss = pqr.split("-");
					$("#requestShopid").val(ss[0]);
					$("#requestSyjid").val(ss[1]);
					$("#requestShinvno").val(ss[2]);
					$("#requestJe").val(ss[3]);
					$("#buttonCheck").click();
				}else if(pqr!=null && pqr.split("-").length==2){
					var ss = pqr.split("-");
					$("#requestSheetid").val(ss[0]);
					$("#requestSheetJe").val(ss[1]);
					 $("#buttonCheck").click();
				}else if(pqr!=null && sheettype==5){
					 $("#requestSheetid").val(pqr);
					 $("#buttonCheck").click();
				}else{
					$("#inputHead").css("display","block");
				}
			}else{
				$.myAlert("请从菜单进入");
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

var initQR = function(){
	if(isWeix()){
		$.myAjax({
			url:$.getService("wx","getWeixTicket?weburl="+window.location.href),
			//dataType:"jsonp",
			success: function (dataJson) {
				if(dataJson.code!=0){
					//$.myAlert("微信授权失败，扫描功能受限："+dataJson.message);
					console.log("微信授权失败，扫描功能受限："+dataJson.message);
					return;
				}
				initWeixinJS(dataJson);
			},
			error: function (jqXHR, textStatus, errorThrown) {
//				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
				console.log("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}else{
		Qrcode.init($('[node-type=qr-btn]'));
		$("#qr-div").removeClass("am-hide");
	}
};


var initWeixinJS = function(dataJson){
	wx.config({  
	    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
	    appId: dataJson.data.appId, // 必填，公众号的唯一标识  
	    timestamp: dataJson.data.timestamp, // 必填，生成签名的时间戳  
	    nonceStr: dataJson.data.noncestr, // 必填，生成签名的随机串  
	    signature: dataJson.data.signature,// 必填，签名，见附录1  
	    jsApiList: ['checkJsApi',
	                'scanQRCode'// 微信扫一扫接口
	               ],
	    beta:true //调用未公开方法
	});
	wx.ready(function(){
		Qrcode.init($('[node-type=qr-btn]'));
		$("#qr-div").css("display","block");
		$("#chooseInvoiceTitleDiv").css("display","block");
		$("#title-text").text("微信开票申请");
		console.log($("#title-text").text());
	});
	wx.error(function(res){
		//$.myAlert("获取微信授权失败："+res.errMsg);
		console.log("微信授权失败，扫描功能受限："+res.errMsg);
	});
};

var chooseInvoiceTitle = function(){
	wx.invoke("chooseInvoiceTitle", {
		"scene" : "1"
	}, function(msg) {
		var res = JSON.parse(msg.choose_invoice_title_info);
		if(res.type=='0'){
			$("#invoiceHeadEntText").val(res.title);
			$("#invoiceHeadEntTaxno").val(res.taxNumber);
			$("#invoiceHeadEntAddr").val(res.companyAddress + res.telephone);
			$("#invoiceHeadEntBank").val(res.bankName + res.bankAccount);
			$("#invoiceHeadEnt").attr("checked",true);
		}else{
			$("#invoiceHeadPersonText").val(res.title);
			$("#invoiceHeadPerson").attr("checked",true);
		}
	});
};

//获取二维码
var getQrCode = function(data){
	if(data == "error decoding QR Code"){
		$.myAlert("无法识别，请重新扫描");
	}else{
		if(data.indexOf("http")>=0){
			var startIdx = data.indexOf("qr=");
			if(startIdx>0){
				startIdx=startIdx+3;
				data = data.substr(startIdx);
			}
		}
		var postData ={ticketQC:data,sheettype:sheettype};
		getbillInfo(postData);
	}
};

function getbillInfo(postData){
	if(checkedData.length>0){
		$.myAlert("每次开票只能选择一张");
		return;
	}
	
	for ( var i = 0; i < checkedData.length; i++) {
		if(postData.ticketQC == checkedData[i]){
			$.myAlert("该单已经加载");
			return;
		}
	}
	
	$.myAjax({
		url:$.getService("api","getInvoiceBillInfoWithDetail"),
		data:JSON.stringify(postData),
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			
			checkedData.push(postData.ticketQC);
			
			if(dataJson.data.flag == 0){
				//设置控件id
				dataJson.data.id = checkedData.length;
				dataJson.data.weixinNote = weixinNote;
				$.myRowHTML([dataJson.data],"rowPiao1","piaoList",0);
				var a="";
				var b="";
				for ( var i = 0; i < dataJson.data.invoiceSaleDetail.length; i++) {
					var data = dataJson.data.invoiceSaleDetail[i];
					if(data.isinvoice=='Y'){
						a+="<div class='am-u-sm-8' >"+data.goodsname+"</div><div class='am-u-sm-4' >"+data.amt+"</div>";
					}else{
						b+="<div class='am-u-sm-8' >"+data.goodsname+"</div><div class='am-u-sm-4' >"+data.amt+"</div>";
					}
				}
				$("#detailList"+dataJson.data.id).append("<div class='am-u-sm-12 am-margin-top-sm am-badge am-badge-success' >可开票商品</div>"+a);
				if(b!="")
					$("#detailList"+dataJson.data.id).append("<div class='am-u-sm-12 am-margin-top-sm am-badge am-badge-secondary' >不可开票商品</div>"+b);
				
				var c="";
				var d="";
				for ( var i = 0; i < dataJson.data.invoiceSalePay.length; i++) {
					var data = dataJson.data.invoiceSalePay[i];
					if(data.isinvoice=='Y'){
						c+="<div class='am-u-sm-8' >"+data.payname+"</div><div class='am-u-sm-4' >"+data.amt+"</div>";
					}else{
						d+="<div class='am-u-sm-8' >"+data.payname+"</div><div class='am-u-sm-4' >"+data.amt+"</div>";
					}
				}
				$("#payList"+dataJson.data.id).append("<div class='am-u-sm-12 am-margin-top-sm am-badge am-badge-success' >可开票支付方式</div>"+c);
				if(d!="")
					$("#payList"+dataJson.data.id).append("<div class='am-u-sm-12 am-margin-top-sm am-badge am-badge-secondary' >不可开票支付方式</div>"+d);
				
				$("#requestShopid").val("");
				$("#requestSyjid").val("");
				$("#requestShinvno").val("");
				$("#requestJe").val("");
				
				//如果金额小于等于0，不能开票
				if(dataJson.data.invoiceamount<=0){
					$("#checkText"+dataJson.data.sheetid).text("不可开票");
					$("#checkText"+dataJson.data.sheetid).removeClass("am-app-piao-can");
					$("#checkText"+dataJson.data.sheetid).addClass("am-app-piao-uncan");
					$("#check"+dataJson.data.sheetid).removeAttr("checked");
					$("#check"+dataJson.data.sheetid).attr("disabled","true");
				}
			}else if(dataJson.data.flag == 1 && dataJson.data.iqstatus==30){
				$.myRowHTML([dataJson.data],"rowPiao3","overPiaoList",0);
			}else{
				$.myRowHTML([dataJson.data],"rowPiao2","overPiaoList",0);
			}
			//成功后隐藏输入控件
			$("#inputHead").css("display","none");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function changeTab(t) {
	if (animaProcessing) {
		return false;
	}
	var tValue=$(t).val();
	var pIndex=$(".panel-enabled").attr("data-index");
	var pId=$(".panel-enabled").attr("id");
	var nIndex=parseInt(pIndex)+parseInt(tValue);
	var nId=$(".tbl-panel").filter("div[data-index='"+nIndex+"']").attr("id");
	
	if(!btnControl(nIndex)){
		return false;
	}else{
		animaProcessing = true;
	}
	
	//按钮控制
	if(nIndex==2){
		$("#btn_submit").css("display","");
		$("#btn_next").css("display","none");
	}else{
		$("#btn_submit").css("display","none");
		$("#btn_next").css("display","");
	}

	if (pIndex < nIndex) {
		$("#" + pId).addClass("am-animation-slide-top am-animation-reverse").
		one($.AMUI.support.animation.end,function() { // 当前table向上滑动
			// 去除当前table的动画效果,并隐藏
			$("#" + pId).removeClass("am-animation-slide-top am-animation-reverse").css("display", "none").removeClass("panel-enabled");
			// 下一个table向上滑出并显示
			$("#" + nId).addClass("am-animation-slide-bottom panel-enabled").css("display", "block").one($.AMUI.support.animation.end,function() {
				$("#" + nId).removeClass("am-animation-slide-bottom");
				animaProcessing = false;
			});
		});
	} else if (pIndex > nIndex) {
		$("#" + pId).addClass("am-animation-slide-bottom am-animation-reverse").
		one($.AMUI.support.animation.end,function() { // 当前table向下滑动	去除当前table的动画效果,并隐藏
			$("#" + pId).removeClass("am-animation-slide-bottom am-animation-reverse").css("display", "none").removeClass("panel-enabled");
			// 下一个table向下滑出并显示
			$("#" + nId).addClass("am-animation-slide-top panel-enabled").css("display", "block").one($.AMUI.support.animation.end,function() {
				$("#" + nId).removeClass("am-animation-slide-top");
				animaProcessing = false;
			});
		});
	} else {
		animaProcessing = false;
	}
}

function btnControl(nIndex) {
	if (nIndex == 0) {
		$("#btn_prev").attr("disabled", "true");
		$("#btn_next").removeAttr("disabled");
		return true;
	} else if (nIndex == 1 &&  setup1()) {
		$("#btn_prev").removeAttr("disabled");
		$("#btn_next").removeAttr("disabled");
		return true;
	} else if (nIndex == 2 &&  setup2()) {
		$("#btn_next").attr("disabled", "true");
		$("#btn_prev").removeAttr("disabled");
		return true;
	} 
	return false;
}

//获取用户初始化信息电话邮箱抬头等
function getCustomerInfo(){
	$.myAjax({
		url:$.getService("api","getCustomerInfo"),
		data:null,
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code==0 && dataJson.data){
				$("#invRecvPhone").val(dataJson.data.phone);
				$("#invRecvMail").val(dataJson.data.email);
				if(dataJson.data.taxno=='' || dataJson.data.taxno == undefined){
					var title = dataJson.data.taxname;
					if(title==null || title=='') title = '个人';
					$("#invoiceHeadPersonText").val(title);
					geren();
				}else{
					$("#invoiceHeadEntText").val(dataJson.data.taxname);
					$("#invoiceHeadEntTaxno").val(dataJson.data.taxno);
					$("#invoiceHeadEntAddr").val(dataJson.data.taxaddr);
					$("#invoiceHeadEntBank").val(dataJson.data.taxbank);
					danwei();
				}
			}else{
				//尝试从cookie读取
				var phone = $.AMUI.utils.cookie.get("phone");
				var email = $.AMUI.utils.cookie.get("email");
				var taxno = $.AMUI.utils.cookie.get("taxno");
				var taxname = $.AMUI.utils.cookie.get("taxname");
				var addr = $.AMUI.utils.cookie.get("addr");
				var bank = $.AMUI.utils.cookie.get("bank");
				
				if(phone=='undefined' || phone==null) phone='';
				if(email=='undefined' || email==null) email='';
				if(taxno=='undefined' || taxno==null) taxno='';
				if(taxname=='undefined' || taxname==null) taxname='';
				if(addr=='undefined' || addr==null) addr='';
				if(bank=='undefined' || bank==null) bank='';
				
				$("#invRecvPhone").val(phone);
				$("#invRecvMail").val(email);
				
				
				if(taxno==''){
					if(taxname=='') title = '个人';
					$("#invoiceHeadPersonText").val(taxname);
					geren();
				}else{
					$("#invoiceHeadEntText").val(taxname);
					$("#invoiceHeadEntTaxno").val(taxno);
					$("#invoiceHeadEntAddr").val(addr);
					$("#invoiceHeadEntBank").val(bank);
					danwei();
				}
				
				$("#kaipiaojilu").css("display","none");
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function updateCustomerInfo(){
	var data={};
	if($("#invoiceHeadPerson").is(":checked")){
		data.taxname = $("#invoiceHeadPersonText").val();
	}else{
		data.taxname = $("#invoiceHeadEntText").val();
		data.taxno = $("#invoiceHeadEntTaxno").val();
		data.taxaddr =  $("#invoiceHeadEntAddr").val();
		data.taxbank = $("#invoiceHeadEntBank").val();
	}
	data.phone = $("#invRecvPhone").val();
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
	
	$.AMUI.utils.cookie.set("phone",data.phone);
	$.AMUI.utils.cookie.set("email",data.email);
	if(data.taxno!=''){
		$.AMUI.utils.cookie.set("taxno",data.taxno);
		$.AMUI.utils.cookie.set("taxname",data.taxname);
		$.AMUI.utils.cookie.set("addr",data.contact);
		$.AMUI.utils.cookie.set("bank",data.bankaccount);
	}
	
}

//校验选中的发票
function setup1(){
	//找到所有选中的小票
	var checked =[];
	var boxes = $("#piaoList input[type=checkbox]");
	$.each( boxes, function(i, n){
		var m = $(n);
		if(m.is(":checked")) {
			checked.push(i);
		}
	});
	if(checked.length==0){
		$.myAlert("至少选中一张小票");
		return false;
	}
	return  true;
}

//校验开票抬头
function setup2(){
	var requestBill ={};
	requestBill.requestInvoicePreviewItem = [];
	
	//找到所有选中的小票
	var boxes = $("#piaoList input[type=checkbox]");
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
		return false;
	}
	
	if(!$('#div_info').validator("isFormValid")) return;
	
	if($("#invoiceHeadPerson").is(":checked")){
		requestBill.gmfMc = $("#invoiceHeadPersonText").val();
	}else{
		requestBill.gmfNsrsbh = $("#invoiceHeadEntTaxno").val();
		if(requestBill.gmfNsrsbh==''){
			$.myAlert("单位抬头发票必须输入纳税人识别号");
			return false;
		}
		if(requestBill.gmfNsrsbh.length<15 || requestBill.gmfNsrsbh.length>20){
			$.myAlert("纳税人识别号长度不正确请检查");
			return false;
		}
		
		requestBill.gmfMc = $("#invoiceHeadEntText").val();
		requestBill.gmfDzdh = $("#invoiceHeadEntAddr").val();
		requestBill.gmfYhzh = $("#invoiceHeadEntBank").val();
	}
	requestBill.recvPhone = $("#invRecvPhone").val();
	requestBill.recvEmail = $("#invRecvMail").val();
	
	
	if($("#taxNoteType1").is(":checked")){
		requestBill.invType = "2";
	}else{
		requestBill.invType = "1";
	}
	if(requestBill.gmfMc == ''){
		$.myAlert("请输入单位名称");
		return false;
	}
	//开票申请人
	requestBill.sheettype = sheettype;
	askPreView(requestBill);
	return  true;
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
				$("#previewPhone").text(dataJson.data[i].recvPhone);
				$("#previewMail").text(dataJson.data[i].recvEmail);
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
	isLoading=true;
	$.myAjax({
		url:$.getService("api","askInvoice"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		showModal:true,
		success: function (dataJson) {
			isLoading=false;
			if(dataJson.code != 0){
				$.myAlert("发票正在开具，成功后将发送到您的邮箱。"+dataJson.message);
				return;
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			$.myConfirm({title:"成功",msg:"稍后电子发票将发送到您的手机和邮箱，请注意查收",btn1:"继续开票",btn2:"查看开票",onCancel:function(){window.location.reload();},onConfirm:function(){$.gotoUrl('./invoiceView.html?iqseqno='+iqseqno);}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			isLoading=false;
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
	
	//提交后保存用户的开票信息
	updateCustomerInfo();
}

function tiao1(){
	if($("#requestShopid").val().length==4){
		$("#requestSyjid").focus();
	   }
	if($("#requestShopid").val().length > 4){
		$.myAlert("长度超过4位数");
	   }
}
function tiao2(){
	if($("#requestSyjid").val().length==8){
		$("#requestShinvno").focus();
	   }
	if($("#requestSyjid").val().length > 8){
		$.myAlert("长度超过4位数");
	   }
}