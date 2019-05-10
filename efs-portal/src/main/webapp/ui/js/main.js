
var myvalue= new myapp();
loadmenu();
search();
//$("#homepage").attr("src",getUrlParameter("home_url",window.location.search)+"?token="+myvalue.token);

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


function setUrlShop(shopid,shopname)
{
	myvalue.ajax(
			{			
				url: "../rest/ui/main/modifyShopId_session.action?shopid="+shopid,			
				type: "post",			
				success: function (text) {  
					refashshop(shopid,shopname);
				},				
				error: function (jqXHR, textStatus, errorThrown) {
					alert("访问后台失败!");
				}				
			}
		);
	
}

function refashshop(shopid,shopname){
	var params=window.location.search.split("&shopid=");
	top.location.href = "main.html"+params[0]+"&shopid="+shopid+"&shopname="+shopname;
}


function passwordLevel(password) {
  var Modes = 0;
  for (i = 0; i < password.length; i++) {
    Modes |= CharMode(password.charCodeAt(i));
  }
  return Modes;
  //CharMode函数
  function CharMode(iN) {
    if (iN >= 48 && iN <= 57)//数字
      return 1;
    if (iN >= 65 && iN <= 90)
    	return 2;
    if (iN >= 97 && iN <= 122)
 			//大小写
      return 4;
    return 8; //特殊字符
  }
}


function modifyPassword() {

	var title;
	
	title = '修改密码';

	// 设置标题
	$("#editform6 #edit-title").html(title);

	
	document.querySelectorAll("#editform6 #myform6")[0].reset();
	

	// 只有先删除了这个属性之后,后面才回再次验证是否为空等信息.......
	$("#editform6 :input").removeData("validity");
	$("#editform6 :input").removeClass(
			"am-active am-field-valid am-field-warning am-field-error");
	$("#editform6 .am-form-group").removeClass("am-form-error am-form-success");
	$("#editform6 .am-form-group .successinputicon").css("display", "none");
	$("#editform6 .am-form-group .errorinputicon").css("display", "none");

	var value = {};
	value.editform="editform6";
	
	value.url="../rest/ui/main/modifyPassword.action";

	// 设置保存事件...
	// 解绑事件再绑定事件
	$('#editform6 #edit-save-btn').unbind("click");
	$('#editform6 #edit-save-btn').bind(
		"click",
		function() {
			if (!$('#'+value.editform+' #myform6').data('amui.validator').isFormValid()) {
				alert('存在未填数据');
			}
			
			var oldpwd=$("#"+value.editform+" #oldpwd").val();
			var newpwd1=$("#"+value.editform+" #newpwd1").val();	
			var newpwd2=$("#"+value.editform+" #newpwd2").val();
			
			if (newpwd1!=newpwd2) {
				alert('新密码不一致');
				return;
			}
			
			if (newpwd1.length < 8)
			{
				alert('密码长度不能小于8位!');
				return;
			}
			
			
//			var modes = passwordLevel(newpwd1);
//			if ((modes & 6) != 6)
//			{
//				alert('密码必须包括大小写字母');
//				return;
//			}
//			
//			if ((modes & 8) != 8)
//			{
//				alert('密码必须包含特殊字符');
//				return;
//			}
		
			$("#"+value.editform+"").modal("close");
			var form = document.getElementById("myform6");
			
			var data = myvalue.getData(form);
			data.oldpwd = hex_sha1(oldpwd);
			data.newpwd1 = hex_sha1(newpwd1);
			data.newpwd2 = hex_sha1(newpwd2);
			
			$.ajax({     
				url:value.url,     
				type:'post',     
				data:data,
				async : false, //默认为true 异步     
				error:function(){     
				   myvalue.updateLoadingDialog("亲，访问接口失败，请联系管理员!");
				},     
				success:function(data){     
				    var jsondata = eval("("+data+")");  
	          if(jsondata.code == "1"){  
							alert("密码修改成功!");
	          }else{  
	            alert(jsondata.msg);
	          }  
				}  
			}); 
      
      form.reset(); 
		}
	);
	
	// 设置不能编辑项
	$('#editform6').modal({
		relatedTarget : this,
		closeViaDimmer : true,
		onCancel : function() {
			// 取消更新

		}
	});

}

//加载菜单.
function loadmenu()
{		
	
	username=getUrlParameter("username",window.location.search);
	var shopname1=getUrlParameter("shopname",window.location.search);
	$("#username").html(decodeURI(username));

	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/main/getMyShopList.action?",			
				type: "post",			
				success: function (text) {  
					var getData=JSON.parse(text);				
					var data=getData.data;	
					var obj = $('<input class="am-radius" type="text" id="searchShops" onkeydown="if(event.keyCode==13){searchShops();}"/><ul class="am-text-xs"></ul>');
					for(var i =0; i<data.length;i++){
						var d = data[i].shopname;
						var o = $("<li></li>");
						var s= $("<a href=\"javascript:setUrlShop('"+data[i].shopid+"','"+data[i].shopname+"')\"></a>");
						s.text(d);
						o.append(s);
						obj.append(o);
					}
					$("#shopname").html(decodeURI(shopname1));
					$("#shopcontent").html(obj);
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("访问后台失败!");
				}					
			}
		);
	
	$("#nav").html("");	
		
	myvalue.ajax(
		{			
			url: "../rest/ui/main/getMenu.action?",			
			type: "post",			
			success: function (text) {  
				var getData=JSON.parse(text);
				
				var data=getData.data;		
				
				$("#homepage").attr("src",getData.home_url+(getData.home_url.indexOf("?")==-1 ? ("?token="+myvalue.token) : ("&token="+myvalue.token)));
				
				navhtml="";
				for(var i=0;i<data.menu.length;i++)
				{
					//增加一级菜单
					navhtml=navhtml+'<li class="has_sub" id="menu_'+data.menu[i].menuid+'" ><a href="#"><i class="am-icon-paper-plane" style="padding-top:12px;"></i><span style="font-size:16px;margin-left:10px;font-weight:bold;">'+data.menu[i].menuname+'</span><span class="pull-right"></span></a>';
					navhtml=navhtml+'<ul>';
					//增加二级菜单
					for(var j=0;j<data.module.length;j++)
					{						
						if (data.menu[i].menuid==data.module[j].menuid)
						{							
							navhtml=navhtml+'<li><a href="javascript:module(\''+data.module[j].moduleid+'\',\''+data.module[j].modulename+'\',\''+data.module[j].url+'\')"><span style="font-size:16px;">'+data.module[j].modulename+'</span></a></li>';														
						}
					}
					navhtml=navhtml+'</ul>';
					navhtml=navhtml+'</li>';
				}				 
				$("#nav").append(navhtml);	
				
				//动态控件需要动态绑定js				
				$("#nav > li > a").on('click',function(e){
				      if($(this).parent().hasClass("has_sub")) {
				        e.preventDefault();
				      }   

				      if(!$(this).hasClass("subdrop")) {
				        // hide any open menus and remove all other classes
				        $("#nav li ul").slideUp(350);
				        $("#nav li a").removeClass("subdrop");
				        
				        // open our new menu and add the open class
				        $(this).next("ul").slideDown(350);
				        $(this).addClass("subdrop");
				      }
				      
				      else if($(this).hasClass("subdrop")) {
				        $(this).removeClass("subdrop");
				        $(this).next("ul").slideUp(350);
				      } 
				      
				  });				
			},        
			error: function (jqXHR, textStatus, errorThrown) {
				alert("访问后台失败!");
			}					
		}
	);	    
}
//获取当前登录用户的到期时间
function search(){

	myvalue.ajax(
			{			
				url: "../rest/ui/module/getEnterprise.action?",			
				type: "post",			
				success: function (text) {  
					 
					var getData=JSON.parse(text);
					if(getData.code=="1"){
						//$.myAlert(getData.msg);
						$.myConfirm({msg:getData.msg,onConfirm:xjfy,onCancel:function(){}});
						 
					}
					
				},
				error: function (jqXHR, textStatus, errorThrown) {
					alert("访问后台失败!");
				}	
			});
}

function xjfy(){

	// 设置标题
	$("#editform #edit-title").html("缴费清单");

	// 设置编辑模态框默认显示的内容......
	$('#editform #loginid').val("");
	$('#editform #username').val("");
	$('#editform #idcard').val("");
	$('#editform #isstop').val(0);
	$('#editform #contacttel').val("");
	$('#editform #kpd').val("");
	

	// 只有先删除了这个属性之后,后面才回再次验证是否为空等信息.......
	$("#editform :input").removeData("validity");
	$("#editform :input").removeClass(
			"am-active am-field-valid am-field-warning am-field-error");
	$("#editform .am-form-group").removeClass("am-form-error am-form-success");
	$(".am-form-group .successinputicon").css("display", "none");
	$(".am-form-group .errorinputicon").css("display", "none");

	// 设置能编辑项
	$('#editform #loginid').attr("disabled", false);

	// 设置保存事件...
	// 解绑事件再绑定事件
	$('#edit-save-btn').unbind("click");
	$('#edit-save-btn').bind(
		"click",
		function() {
			if ($('#my-form').data('amui.validator').isFormValid()) {
				var loginid = $.trim($("#editform #loginid").val());
				//var password = $.trim($("#editform #password").val());
				var username = $.trim($("#editform #username")
						.val());
				var idcard = $.trim($("#editform #idcard").val());
				var kpd = $.trim($("#editform #kpd").val());
				var isstop = $.trim($("#editform #isstop").val());
				var contacttel = $.trim($("#editform #contacttel")
						.val());
 
				// 检查不能为空的项.....
				if (loginid == "") {
					return true;
				}
				
				var value = {};
				value.loginid = loginid;
				value.password = hex_sha1("abcd1234");
				value.username = username;
				value.idcard = idcard;
				value.kpd = kpd;
				value.isstop = 0;
				value.logintype = 0;
				value.contacttel = contacttel;
			 
				var valuelist = [];
				valuelist.push(value);
	
				$("#editform").modal("close");
				showLoadingDialog();
	
				myvalue
						.ajax({
							url : "/efs-portal/rest/ui/module/insertStandardTable.action?tablename="
									+ curdtable,
							type : "post",
							data : JSON.stringify(valuelist),
							success : function(text) {
								var data = JSON.parse(text);
								if (data.code == "1") {
									// 重新载入数据....
									loaddata("新增数据成功!");
								} else {
									updateLoadingDialog("新增数据失败!<br>失败原因:"
											+ data.msg);
								}
							},
							error : function(jqXHR, textStatus,
									errorThrown) {
								updateLoadingDialog("失败: 网络中断或接口异常!");
							}
						});
			} else {
				$(".am-form-success .successinputicon").css(
						"display", "inline");
				$(".am-form-success .errorinputicon").css(
						"display", "none");
				$(".am-form-error .errorinputicon").css("display",
						"inline");
				$(".am-form-error .successinputicon").css(
						"display", "none");
				alert("表单输入错误!");
			}
		}
	);

	// 设置不能编辑项
	$('#editform').modal({
		relatedTarget : this,
		closeViaDimmer : true,
		onCancel : function() {
			// 取消更新

		}
	});


}

//使用frime来解决
function module(moduleid,modulename,url)
{				
	
	//如果已经有此页面，则转到此页面即可.....
	if ($("#T"+moduleid).length >0)
	{
		$("#pagetabs li").each(function(){		 
			$(this).removeClass("active");
			$(this).find(".pageclosebtn").css("color","#AAA");
		});  		
		
		$("#tabcontents .tab-pane").each(function(){		 
			$(this).removeClass("active");		
		}); 
		
		$("#T"+moduleid).each(function(){		 
			$(this).addClass("active");		
		});  
				
		$("#P"+moduleid).each(function(){		 
			$(this).addClass("active");
			$(this).find(".pageclosebtn").css("color","#AAA");
		});				
		
		return;
	}
	
	//测试 
//	if (1==1)
//	{
//		moduleHTML="<div class='tab-pane active' style='height:100%' id='P"+moduleid+"'><iframe id='i"+moduleid+"' src='"+url+"?token="+myvalue.token+"' style='width:100%;height:100%;overflow:hidden' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='auto' allowtransparency='yes'></iframe><div>";
//		
//		//先删除，避免重复
//		$("#P"+moduleid).remove();
//		
//		//动态增加一个分页内容
//		$("#tabcontents").append(moduleHTML);
//					
//		//动态增加一个分页.....
//		$("#pagetabs").append('<li id="T'+moduleid+'" class="tab-title active"><a href="#P'+moduleid+'" data-toggle="tab"><span style="font:100%  微软雅黑, 宋体, 新宋体;margin-left:10px;margin-right:10px">'+modulename+'</span><span class="pageclosebtn" onclick="javascript:askclosepage('+moduleid+')" style="cursor:pointer;font:宋体" title="关闭窗口" class="dialogcloseBtn" onmouseover="$(this).css({color:\'black\'})" onmouseout="$(this).css({color:\'#AAA\'})"">X</span></a></li>');
//		    
//		$("#pagetabs li").each(function(){		 
//			$(this).removeClass("active");	
//			$(this).find(".pageclosebtn").css("color","#AAA");
//		});  	
//		
//		$("#tabcontents .tab-pane").each(function(){		 
//			$(this).removeClass("active");		
//		}); 
//		
//		$("#T"+moduleid).each(function(){		 
//			$(this).addClass("active");		
//		});  
//				
//		$("#P"+moduleid).each(function(){		 
//			$(this).addClass("active");		
//		});
//		
//		return;
//	}
	
	//去后台获取url	
	myvalue.ajax(
		{			
			url: "../rest/ui/module/checkLoginModule.action?moduleid="+moduleid,			
			type: "post",			
			success: function (text) {  					
				var result=JSON.parse(text);								
				
				if (result.code=="1")
				{
					var moduleid=result.moduleid;
					var modulename=result.modulename;
					var url=result.url;
					
					moduleHTML="<div class='tab-pane active' style='height:100%' id='P"+moduleid+"'><iframe id='i"+moduleid+"' src='"
					+url+(url.indexOf("?")==-1 ? ("?token="+myvalue.token) : ("&token="+myvalue.token))+
					"' style='width:100%;height:100%;overflow:hidden' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='auto' allowtransparency='yes'></iframe><div>";
					
					//先删除，避免重复
					$("#P"+moduleid).remove();
					
					//动态增加一个分页内容
					$("#tabcontents").append(moduleHTML);
								
					//动态增加一个分页.....
					$("#pagetabs").append('<li id="T'+moduleid+'" class="tab-title active"><a href="#P'+moduleid+'" data-toggle="tab"><span style="font:100%  微软雅黑, 宋体, 新宋体;margin-left:10px;margin-right:10px">'+modulename+'</span><span class="pageclosebtn" onclick="javascript:askclosepage('+moduleid+')" style="cursor:pointer;font:宋体" title="关闭窗口" class="dialogcloseBtn" onmouseover="$(this).css({color:\'black\'})" onmouseout="$(this).css({color:\'#AAA\'})"">X</span></a></li>');
					    
					$("#pagetabs li").each(function(){		 
						$(this).removeClass("active");	
						$(this).find(".pageclosebtn").css("color","#AAA");
					});  	
					
					$("#tabcontents .tab-pane").each(function(){		 
						$(this).removeClass("active");		
					}); 
					
					$("#T"+moduleid).each(function(){		 
						$(this).addClass("active");		
					});  
							
					$("#P"+moduleid).each(function(){		 
						$(this).addClass("active");		
					});
				} else if (result.code=="-1") {
					alert("您没有此模块权限!");						
				} else {
					alert("未知返回!");
				}
				
			},				
			error: function (jqXHR, textStatus, errorThrown) {
				alert("访问后台失败!");
			}					
		}
	);
			
}

//调用模块页面模块页面
function askclosepage(moduleid)
{			
	canclose=true;
	
	//加入异常处理，当页面没有closeiframe方法时认为可以关闭.....
	try
	{
		canclose=window.frames["i100001"].closeiframe(moduleid);
	} catch(e)
	{
		
	}
	
	//关闭iframe
	if (canclose || canclose==null)
	{
		//获取上一个标签，关闭后默认为选择上一个标签
		
		preid="T000001";
		
		$("#pagetabs li").each(function(){
					
			
			if ($(this).attr("id")=="T"+moduleid)
			{
				$("#pagetabs #T"+moduleid).remove();
				$("#tabcontents #P"+moduleid).remove();				
				return false;
			}
			
			preid=$(this).attr("id");
			
			
		});  	
		
		
		//选中前一个模块....
		module(preid.substring(1,100));
	}
}

//返回主菜单
function gotologin()
{
	top.location.href="../login.html";
}

function resetLogin()
{	
	var companyid=getUrlParameter("companyid",window.location.search);
	var companyname=getUrlParameter("companyname",window.location.search);
	
	top.location.href="login.html?companyid="+companyid+"&companyname="+companyname;
}

(function($) {
	  'use strict';
	  $(function() {
	    var $fullText = $('.admin-fullText');
	    $('#admin-fullscreen').on('click', function() {
	      $.AMUI.fullscreen.toggle();
	    });

	    $(document).on($.AMUI.fullscreen.raw.fullscreenchange, function() {
	      $.AMUI.fullscreen.isFullscreen ? $fullText.text('关闭全屏') : $fullText.text('开启全屏');
	    });
	  });
})(jQuery);


window.addEventListener('load',myload);

function myload()
{
	heartbeat_detect();
}

function heartbeat_detect ()
{
	window.setInterval(detect_func,60 * 1000); 
}

function detect_func()
{
	
	$.ajax({     
		url:'../rest/ui/module/heartbeat_detection.action',     
		type:'post',     
		data:{data:JSON.stringify({})},
		async : false, //默认为true 异步     
		error:function(){     
		   myvalue.updateLoadingDialog("亲，访问接口失败，请联系管理员!");
		},     
		success:function(data){     
		   // do nothing, just heartbeat detection;
		}  
	}); 
}

function searchShops(){
	var data={};
	data.searchShop=$("#searchShops").val();
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/main/getMyShopListByName.action?",			
				type: "post",	
				data:JSON.stringify(data),
				success: function (text) {  
					var getData=JSON.parse(text);				
					var data=getData.data;	
					var obj = $('<input class="am-radius" type="text" id="searchShops" onkeydown="if(event.keyCode==13){searchShops();}"/><ul class="am-text-xs"></ul>');
					for(var i =0; i<data.length;i++){
						var d = data[i].shopname;
						var o = $("<li></li>");
						var s= $("<a href=\"javascript:setUrlShop('"+data[i].shopid+"','"+data[i].shopname+"')\"></a>");
						s.text(d);
						o.append(s);
						obj.append(o);
					}
					$("#shopcontent").html(obj);
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("访问后台失败!");
				}					
			}
		);
}