var myvalue= new myapp();


//验证账号密码正确性
function modifyPassword()
{					
	var oldpwd=$("#oldpwd").val();
	var newpwd1=$("#newpwd1").val();	
	var newpwd2=$("#newpwd2").val();
	
	if (newpwd1!=newpwd2) {
		alert('新密码不一致');
		return;
	}
	
	myvalue.ajax(
		{			
			url: "/efs-portal/rest/ui/main/modifyPassword.action?oldpwd=" + hex_sha1(oldpwd)
				+"&newpwd1=" + hex_sha1(newpwd1),	
			type: "post",
			success: function (text) {        	
				var getData=JSON.parse(text);
				var code=getData.code;
				var msg=getData.msg;
				
				if (code==1)
				{
					
					location.href ="login.html";
					
				} else {
					alert("修改失败:"+msg);
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


function refresh(obj){  
     obj.src = "../VerifyCodeServlet?" + Math.random();  
}  
  
function mouseover(obj){  
    obj.style.cursor = "pointer";  
}  
