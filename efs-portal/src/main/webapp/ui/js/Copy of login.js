var myvalue;

$(document).ready(function(){
	myvalue = new myapp();
	//绑定事件
	//验证码刷新
	$("verifyCodeServlet").click(function(event){
		var obj = $(event.target);
		if(obj.attr('src')=='') return;
		obj.hide();
		obj.attr('src','../VerifyCodeServlet?timestamp=' + new Date().getTime() );
		obj.fadeIn(1000);
	});
	
	//登录按钮
	$("loginBtn").click(function(event){
		
	});
});


//验证账号密码正确性
function logincheck()
{					
	companyid=$("#inputCompanyID").val();
	loginid=$("#inputLoginID").val();	
	passoword=$("#inputPassWord").val();
	var verifycode=$("#verifycode").val();
		
	myvalue.ajax(
		{			
			url: "/efs-portal/rest/ui/main/loginCheck.action?companyid="+companyid
				+"&loginid="+loginid
				+"&password="+hex_sha1(passoword)
				+"&verifycode="+verifycode,			
			type: "post",
			success: function (text) {        	
				var getData=JSON.parse(text);
				var code=getData.code;
				var msg=getData.msg;
				
				if (code==1)
				{
					var token=getData.token;
					var username=getData.username;
					var companyid=getData.companyid;					
					var companyname=getData.companyname;					
					
					var shopname="";
					if ("shopname" in getData)
					{
						shopname=getData.shopname;
					}
					
					if (shopname != "")
					{
						//直接跳转到管理页面...
						location.href ="main.html?token="+token+"&username="+username+"&shopname="+shopname+"&companyid="+companyid+"&companyname="+companyname;
					} else {
						//跳转机构选择页面...
						location.href ="shop.html?token="+token+"&username="+username+"&companyid="+companyid+"&companyname="+companyname;
					}										
					
				} else {
					document.getElementById("VerifyCodeServlet").src = "../VerifyCodeServlet?" + Math.random();  
					alert("验证失败:"+msg);
				}				
			},        
			error: function (jqXHR, textStatus, errorThrown) {
				//alert("访问后台失败!"+jqXHR.responseText);
				alert("访问后台失败!");
			}
		}
	);

}

function getUrlParameter(param,url)
{
	var params=(url.substr(url.indexOf("?")+1)).split("&");
	if (params!=null)
	{
		for(var i=0;i<params.length;i++)
		{
			var strs=params[i].split("=");
			if (strs[0]==param)
			{
				return strs[1];
			}
		}
	}
}


function init()
{
	
	var companyid=getUrlParameter("companyid",window.location.search);						//操作者帐号		
	var companyname=decodeURI(getUrlParameter("companyname",window.location.search));		//操作者登陆的自营机构编码或者第三方供货商编码
		
	if ((companyid != null) && (companyid != ""))
	{		
		$("#inputCompanyID").val(companyid);		
		$("#inputCompanyName").val(companyname);	
		$("#PCompanyName").css("display","block");	
	} else {
		$("#PCompanyID").css("display","block");				
	}
	
}


function refreshVerifyCode(){  
	$("#verifyCodeServlet").src = "../VerifyCodeServlet?" + Math.random();  
}  
  
function mouseover(obj){  
    obj.style.cursor = "pointer";  
}  
