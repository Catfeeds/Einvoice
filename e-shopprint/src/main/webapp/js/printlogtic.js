init();

function init()
{	
	var shopid=getUrlParameter("shopid",window.location.search);			//操作者帐号		
	var taskid=getUrlParameter("taskid",window.location.search);
	var expressno=getUrlParameter("expressno",window.location.search);
	var token=getUrlParameter("token",window.location.search);//操作者登陆的自营机构编码或者第三方供货商编码
	
	$.ajax({
		url: "./services/print/askLogisticsprint?shopid="+shopid+"&taskid="+taskid+"&expressno="+expressno+"&token="+token,
		type : "post",
		success: function (text){		
			$("#info").html("调用打印程序完成！");
			window.close();
		},
		error: function (text){
			$("#info").html("调用打印程序异常！");
		}
	});
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