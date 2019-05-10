init();

function init()
{	
	var shopid=getUrlParameter("shopid",window.location.search);			//操作者帐号		
	var taskid=getUrlParameter("taskid",window.location.search);
	
	$.ajax({
		url: "./services/print/askOfflinePrint?shopid="+shopid+"&taskid="+taskid,
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