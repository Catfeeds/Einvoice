var myvalue= new myapp();

init();


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

//加载菜单.
function init()
{	
	username=getUrlParameter("username",window.location.search);
			
	myvalue.ajax(
		{			
			url: "/efs-portal/rest/ui/main/getMyShopList.action?",			
			type: "post",			
			success: function (text) {  
				var getData=JSON.parse(text);				
				var data=getData.data;		
				
				var navhtml="";
				for(var i=0;i<data.length;i++)
				{
					//增加一级菜单
					navhtml=navhtml+'<li class="am-g"><a href="javascript:selectshop(\''+data[i].shopid+'\',\''+data[i].shopname+'\');" class="am-list-item-hd ">'+data[i].shopname+'</a></li>';
					
				}				 
				$("#shoplist").append(navhtml);					
			},        
			error: function (jqXHR, textStatus, errorThrown) {
				alert("访问后台失败!");
			}					
		}
	);	    
}


function selectshop(shopid,shopname)
{	
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/main/loginshop.action?shopid="+shopid,			
				type: "post",			
				success: function (text) {  						
					
					var getData=JSON.parse(text);
					var code=getData.code;
					var msg=getData.msg;
					
					username=getUrlParameter("username",window.location.search);
					companyid=getUrlParameter("companyid",window.location.search);
					companyname=getUrlParameter("companyname",window.location.search);
					
					token=getUrlParameter("token",window.location.search);
					
					if (code==1)
					{																	
						location.href ="main.html?token="+token+"&username="+username+"&shopname="+shopname+"&companyid="+companyid+"&companyname="+companyname;																					
					} else {
						alert("登陆失败:"+msg);
					}					
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("访问后台失败!");
				}					
			}
		);	    
}