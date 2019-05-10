var companyid;
var companyname;

function windowHeight() {
    var de = document.documentElement;
    return self.innerHeight||(de && de.clientHeight)||document.body.clientHeight;
}

function getUrlParameter(param,url)
{
	url = url ? url : window.location.search;
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

window.onload=function() {		
	checkCompanyid();
}


function checkCompanyid() {
	
	companyid=getUrlParameter("companyid",window.location.search);	
	if (companyid==null || companyid=='') {
		alert("非法地址，请带上企业编码!");
		return;
	}
	
	var data={};
	data.companyid=companyid;
	
	$.ajax({     
		url:'/efs-portal/rest/ui/module/queryCompany.action',     
		type:'post',     
		data:{data:JSON.stringify(data)},
		async : false, //默认为true 异步     
		error:function(){     
		   alert("访问接口失败!");
		},     
		success:function(data){     
		   handleCompanyid(JSON.parse(data),companyid);
		}  
	}); 
}

function handleCompanyid(data,companyid) 
{
	if (data.code==0) 
	{
		alert("编号为"+companyid+"的企业不存在!");
		return;
	}
	
	companyname=data.data.companyname;
	$("#companyname").html(companyname);	
	
}

function login() 
{	
	var account = document.getElementById("account").value;
	var pwd = document.getElementById("pwd").value;
	
	if (account==null || account=='')
	{
		alert("亲，用户名不能为空呃!");
		return;
	}
	
	if (pwd==null || pwd=='')
	{
		alert("亲，密码不能为空呃!");
		return;
	}
	
	var data={};
	data.account=account;
	data.pwd=pwd;
	data.companyid=companyid;
	data.type="srm";
	
	event.preventDefault();
	
	$('form').fadeOut(500);
	$('.wrapper').addClass('form-success');

	setTimeout(function() {
		
		$.ajax({     
			url:'/efs-portal/rest/ui/module/loginPartner.action',     
			type:'post',     
			data:{data:JSON.stringify(data)},
			async : false, //默认为true 异步     
			error:function(){     
			   alert("访问接口失败!");
			},     
			success:function(data){     
			   handleLogin(JSON.parse(data),companyid);
			}  
		}); 
		
	},500);
	
	
	
}


function handleLogin(data,companyid) 
{
	if (data.code==0) 
	{
		alert(data.msg);
		$('form').fadeIn(500);
		$('.wrapper').removeClass('form-success');
		
		return;
	}
	
	self.location.href=data.main_url + "?token="+data.token+"&name="+data.name +"&msg="+encodeURIComponent(location.href);
	//直接跳转页面...
	//myvalue.closeLoadingDialog();
}

document.onkeydown=function(event){
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if(e && e.keyCode==13){ // enter 键
	 login();
	}
};