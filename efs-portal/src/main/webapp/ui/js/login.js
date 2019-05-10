var companyid;
var companyname;
$(document).ready(function(){
	//绑定事件
	//验证码刷新
	$("#verifyCodeServlet").click(function(event){
		var obj = $(event.target);
		if(!obj.attr('src')) return;
		obj.attr('src',$.getService("verifyCode","VerifyCodeServlet")+'?timestamp=' + new Date().getTime() );
	});
	
	//登录按钮
	$("#loginBtn").click(function(event){
		login();
	});
	
	//初始化公司数据
	companyid = $.getUrlParam("companyid");
	companyname = $.getUrlParam("companyname");
	if(!companyid) companyid=2;
	if(companyname){
		$("#inputCompanyID").val(companyname);
	}
	
	//初始化表单验证
	initValidator();
	
	//记住用户名
	var remberUserName = $.AMUI.utils.cookie.get("remberUserName");
	var isRemberUserName = new Boolean(remberUserName);
	if(isRemberUserName && remberUserName!='false') {
		$("#inputLoginID").val(remberUserName);
		$("#inputRemberUserName").bootstrapSwitch('state',true);
	}
});

function login(){
	if(!$('#form-vld').validator("isFormValid")) return;
	$("#loginBtn").button('loading');
	var params = {};
	params.companyid=companyid;
	params.loginid=$("#inputLoginID").val();	
	params.password=hex_sha1($("#inputPassWord").val());
	params.verifycode=$("#inputVerifyCode").val();
	$.myAjax({
		url:$.getService("portal","main/loginCheck.action?")+ $.cookGetUrlParam(params),
		progress:true,
		success: function (dataJson) {
			
			$("#loginBtn").button('reset');
			if(dataJson.code == 0){
				$.myAlert(dataJson.msg);
				$("#verifyCodeServlet").click();
			}else{
				var isRemberUserName = $("#inputRemberUserName").bootstrapSwitch('state');
				var remberUserName = isRemberUserName?params.loginid:false;
				$.AMUI.utils.cookie.set("remberUserName",remberUserName,new Date(new Date().valueOf() + 7*24*60*60*1000));
				
				if(dataJson.shoplist != 'undefine' && dataJson.shoplist.length>0){
					dataJson.shopid = dataJson.shoplist[0].shopid;
					dataJson.shopname = dataJson.shoplist[0].shopname;
					dataJson.shoplist = [];
				}
				
				location.href ="main.html?"+$.cookGetUrlParam(dataJson);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$("#loginBtn").button('reset');
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function initValidator(){
	var $form = $('#form-vld');
	var $tooltip = $('<div id="vld-tooltip">提示信息！</div>');
	$tooltip.appendTo(document.body);

	$form.validator();

	/*var validator = $form.data('amui.validator');
		$form.on('focusin focusout', 'input', function(e) {
		if (e.type === 'focusin') {
			var $this = $(this);
			//如果验证通过直接返回
			if($this.hasClass('am-field-valid')) return;
			
			var offset = $this.offset();
			var h =  $(this).outerHeight();
			var msg = $this.data('foolishMsg') || validator.getValidationMessage($this.data('validity'));
			$tooltip.text(msg).css({
				left: offset.left,
	        	top: offset.top + h
			}).show();
		} else {
			$tooltip.hide();
		}
	});*/
}