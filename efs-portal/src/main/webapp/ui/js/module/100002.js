var appID="wxb86bdf5edea4de69";
var userID="weixin-18995647365";
var timestamp = Date.parse(new Date());
var animaProcessing = false; // tab页动画进度的控制,防止多个滑动同时执行,出现一个页面显示两个tab的情况
var taxInfo = {}; //开票抬头信息
taxInfo.ticketQC = []; //选中的小票

function isWeix(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		return true;
	} else {
		return false;
	}
}

$(document).ready(function(){
	initWeix();
	
	if ($.AMUI && $.AMUI.validator) {
		$.AMUI.validator.patterns.mobile = /^\s*1\d{10}\s*$/;
	 }
	
	Qrcode.init($('[node-type=qr-btn]'));
	
	//验证
	$('#div_info').validator();
	
	// 翻页按钮
	$("#btn_next , #btn_prev").click(function() {
		changeTab(this);
	});
	
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
	
	//绑定点击
	$("#invoiceHeadPersonText").click(function() {
		$("#invoiceHeadPerson").prop("checked",true);
	});
	$("#invoiceHeadEntText").click(function() {
		$("#invoiceHeadEnt").prop("checked",true);
	});
	
});


var initWeix = function(){
	$.myAjax({
		url:$.getService("portal","main/getWeixTicket.action?weburl="+window.location.href),
		dataType:"jsonp",
		success: function (dataJson) {
			if(isWeix()){
				wx.config({  
				    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
				    appId: dataJson.appId, // 必填，公众号的唯一标识  
				    timestamp: dataJson.timestamp, // 必填，生成签名的时间戳  
				    nonceStr: dataJson.noncestr, // 必填，生成签名的随机串  
				    signature:dataJson.signature,// 必填，签名，见附录1  
				    jsApiList: ['checkJsApi',
				                  'scanQRCode'// 微信扫一扫接口
				               ]
				});

				wx.ready(function(){
										
				});
				wx.error(function(res){
					 alert("获取微信授权失败："+res.errMsg);
				});
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
};

//获取二维码
var getQrCode = function(data){
	if(data == "error decoding QR Code"){
		$.myAlert("无法识别，请重新扫描");
	}else{
		var postData =JSON.stringify({ticketQC:data,entid:"A00001002",billType:1,appID:appID,userid:userID,shopid:"0106",syjid:"P619",billno:"185",je:63.53});
		$.myAjax({
			url:$.getService("invoice","getbillInfo?")+"channel=wx",
			data:postData,
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				
				dataJson.data.isQue = 0;
				if(dataJson.data.isQue == 0){
					$.myRowHTML([dataJson.data],"rowPiao1","piaoList",0);
				}else{
					$.myRowHTML([dataJson.data],"rowPiao2","overPiaoList",0);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}
};

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
		getUserInfo();
		return true;
	} else if (nIndex == 2 &&  setup2()) {
		$("#btn_next").attr("disabled", "true");
		$("#btn_prev").removeAttr("disabled");
		return true;
	} 
	return false;
}

//获取用户初始化信息电话邮箱抬头等
function getUserInfo(){
	//如果没有电话，则从服务器读取默认信息
	if($("#invRecvPhone").val() != ""){
		return;
	}
	var postData ={data:JSON.stringify({appID:appID,userID:userID})};
	$.myAjax({
		url:$.getService("portal","main/getServerDate.action"),
		data:postData,
		success: function (dataJson) {
			dataJson.tel = "18995647365";
			dataJson.mail = "123@qq.com";
			$("#invRecvPhone").val("18995647365");
			$("#invRecvMail").val("123@qq.com");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

//校验选中的发票
function setup1(){
	//找到所有选中的小票
	var boxes = $("#piaoList input[type=checkbox]");
	taxInfo.ticketQC = [];
	$.each( boxes, function(i, n){
		var m = $(n);
		if(m.is(":checked")) taxInfo.ticketQC.push(m.val());
	});
	if(taxInfo.ticketQC.length==0){
		$.myAlert("至少选中一张小票");
		return false;
	}
	return  true;
}

//校验开票抬头
function setup2(){
	if(!$('#div_info').validator("isFormValid")) return;
	
	if($("#invoiceHeadPerson").is(":checked")){
		taxInfo.invName = "个人";
	}else{
		taxInfo.invName = $("#invoiceHeadEntText").val();
	}
	taxInfo.invRecvPhone = $("#invRecvPhone").val();
	taxInfo.invRecvMail = $("#invRecvMail").val();
	if($("#taxNoteType1").is(":checked")){
		taxInfo.invType = "2";
	}else{
		taxInfo.invType = "1";
	}
	if(taxInfo.invName == ''){
		 $.myAlert("请输入单位名称");
		return false;
	}
	askPreView(taxInfo);
	return  true;
}

function askPreView(taxInfo){
	$("#preview").html("");
	
	taxInfo.appID = appID;
	taxInfo.userID = userID;
	
	var postData ={data:JSON.stringify(taxInfo)};
	$.myAjax({
		url:$.getService("portal","main/getServerDate.action"),
		data:postData,
		success: function (dataJson) {
			dataJson.data=[{XFZ_YX:"18995647365",XFZ_SJH:"123@qq.com",GMF_MC:taxInfo.invName,JSHJDX:"壹佰元整",JSHJ:"100.00",XSF_MC:"华润万家深圳投资有限公司",XSF_NSRSBH:"994213473823G",XSF_DZDH:"武汉市硚口区XX街道",XSF_YHZH:"中国银行XX支行",detail:[{XMMC:"办公用品",XMJE:"85.6",SL:"3%",SE:"14.4"}]}];
			$.myRowHTML(dataJson.data,"rowPreview","preview",1);
			
			//写入明细信息
			for ( var i = 0; i < dataJson.data.length; i++) {
				$.myRowHTML(dataJson.data[i].detail,"rowPreviewDetail","previewDetail"+i,2);
				$("#previewPhone").text(dataJson.data[i].XFZ_YX);
				$("#previewMail").text(dataJson.data[i].XFZ_SJH);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

//提交开票申请
function commit(){
	var postData ={data:JSON.stringify(taxInfo)};
	$.myAjax({
		url:$.getService("portal","main/getServerDate.action"),
		data:postData,
		success: function (dataJson) {
			$.myConfirm({title:"成功",msg:"稍后电子发票将发送到您的短信和邮箱，请注意查收",btn1:"继续开票",btn2:"查看开票",onCancel:function(){window.location.reload();},onConfirm:function(){$.gotoUrl('./100003.html?mode=test');}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}