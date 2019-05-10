var companyid;
var myvalue= new myapp();
function windowHeight() {
    var de = document.documentElement;
    return self.innerHeight||(de && de.clientHeight)||document.body.clientHeight;
}

window.onload=function() {
	resize();
	backimg();
	checkCompanyid();
}

function backimg() 
{
	//document.querySelectorAll(".banner")[0].style.backgroundImage='url(http://www.vpoint365.com/efs-ui/rest/ui/file/show.action?path=images/3/ticket/1506040000000000000500.jpg)';
	document.querySelectorAll(".banner")[0].style.backgroundImage='url(./img/storeimg.jpg)';
}

function checkCompanyid() {
	companyid=myvalue.getUrlParameter("companyid",window.location.search);	
	if (companyid==null || companyid=='') {
		myvalue.showMsgDialog("非法地址，请带上企业编码!");
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
		   myvalue.showMsgDialog("访问接口失败!");
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
		myvalue.showMsgDialog("编号为"+companyid+"的企业不存在!");
		return;
	}
	
	$(".companyname").html(data.data.companyname);	
}

function resize() {
	var height=(windowHeight()-document.querySelectorAll(".head_box")[0].clientHeight
		-document.querySelectorAll("#footer")[0].clientHeight-5);
	document.querySelectorAll(".mycontent")[0].style.height=height+'px';
}

function login() 
{
	var account = document.getElementById("account").value;
	var pwd = document.getElementById("pwd").value;
	
	if (account==null || account=='')
	{
		myvalue.showMsgDialog("亲，用户名不能为空呃!");
		return;
	}
	
	if (pwd==null || pwd=='')
	{
		myvalue.showMsgDialog("亲，密码不能为空呃!");
		return;
	}
	
	var data={};
	data.account=account;
	data.pwd=pwd;
	data.companyid=companyid;
	myvalue.showLoadingDialog('正在登陆,请稍候...');
	$.ajax({     
		url:'/efs-portal/rest/ui/module/loginPartner.action',     
		type:'post',     
		data:{data:JSON.stringify(data)},
		async : false, //默认为true 异步     
		error:function(){     
		   myvalue.updateLoadingDialog("访问接口失败!");
		},     
		success:function(data){     
		   handleLogin(JSON.parse(data),companyid);
		}  
	}); 
}


function handleLogin(data,companyid) 
{
	if (data.code==0) 
	{
		myvalue.updateLoadingDialog(data.msg);
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