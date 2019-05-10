var curdtable="role";
var currentdata=[];

//模块初始化
moduleinit();

function moduleinit()
{			
	loaddata("");	
}

function loaddata(msg)
{		
	if (msg!="")
	{
		msg=msg+"<br>";
	}
	
	showLoadingDialog(msg+"正在获取最新数据,请稍候...");

	myvalue.ajax(
		{						
			url: "/efs-portal/rest/ui/module/getRoleInfo.action?",			
			type: "post",					
			success: function (text) {  
				
				var result=JSON.parse(text);													
				
				if (result.code=="1") {			
					redata=JSON.parse(result.data);							
					currentdata=redata.data;						
					dataHTML=getRowHTML(currentdata,$("#rowsample").html());						
					$("#tabledata").html(dataHTML.replace(/<tbody>/g,"").replace(/<\/tbody>/g,""));							
					$("#totalcount").html(redata.total);
										
					if (msg=="")
					{					
						updateLoadingDialog("加载数据完成");
						
						//这里太快关闭就会影响到遮罩层,所以改成一秒钟关闭														
						closeLoadingDialog();						
						
					} else {
						updateLoadingDialog(msg);
					}
										
				} else if (result.code==-1) {
					alert("您无此模块操作权限,自动退出");
					window.parent.gotologin();
				} else {					
					updateLoadingDialog(msg+"获取数据失败!<br>失败原因:"+result.msg);
				}				
			},        
			error: function (jqXHR, textStatus, errorThrown) {
				updateLoadingDialog(msg+"获取数据失败:  网络中断或接口异常!");
			}					
		}
	);	
	
	//异步获取效果可能比较好....
	myvalue.ajax(
		{						
			url: "/efs-portal/rest/ui/module/getModuleInfo.action?",			
			type: "post",					
			success: function (text) {  				
				var result=JSON.parse(text);					
				
				menulist=result.data.menu;
				modulelist=result.data.module;	
				var modulecontrol=result.data.modulecontrol;
				
				dataHTML=getRowHTML(menulist,$("#menusample").html());						
				$("#menu-list").html(dataHTML.replace(/<tbody>/g,"").replace(/<\/tbody>/g,""));
				
				for(var i=0;i<modulelist.length;i++)
				{
					module=[];
					module.push(modulelist[i]);										
					dataHTML=getRowHTML(module,$("#modulesample").html());
					$("#menu_"+module[0].menuid).append(dataHTML.replace(/<tbody>/g,"").replace(/<\/tbody>/g,""));
				}	
				
				
				//for(var i=0;i<modulecontrol.length;i++)
				//{
				    $("#div_modulecontorl").html("");
					dataHTML=getRowHTML(modulecontrol,$("#modulecontrolsample").html());
					$("#div_modulecontorl").append(dataHTML.replace(/<tbody>/g,"").replace(/<\/tbody>/g,""));
				//}
			},        
			error: function (jqXHR, textStatus, errorThrown) {
				//updateLoadingDialog(msg+"获取数据失败:  网络中断或接口异常!");
			}					
		}
	);

}

function addrow()
{
	
	$("#newform #rolename").val("");
	$("#newform #note").val("");
		
	$("#newform").modal({
		relatedTarget: this,
		closeViaDimmer:false,
        onConfirm: function(e) { 
        	
			rolename=$(e.target).find("#rolename").val();
			note=$(e.target).find("#note").val();
			
			value={};
						
			value.rolename=rolename;
			value.note=note;	                         
			valuelist=[];		
			valuelist.push(value);		        	        	        	
			
			showLoadingDialog();
			
			myvalue.ajax(
				{			
					url: "/efs-portal/rest/ui/module/insertStandardTable.action?tablename="+curdtable,			
					type: "post",		
					data: JSON.stringify(valuelist),
					success: function (text) {
						var data=JSON.parse(text);					
						
						if (data.code=="1") {										
							//重新载入数据....
					        loaddata("新增数据成功!");				        		
						} else {
							updateLoadingDialog("新增数据失败!<br>失败原因:"+data.msg);						
						}						
					},        
					error: function (jqXHR, textStatus, errorThrown) {
						updateLoadingDialog("新增失败: 网络中断或接口异常!");
					}					
				}
			);					
		},
		error: function (jqXHR, textStatus, errorThrown) {
			updateLoadingDialog(msg+"获取数据失败:  网络中断或接口异常!");
		}
	});		
}

function deleterow(roleid)
{
	for (var i=0;i<currentdata.length;i++)
	{
		if (currentdata[i].roleid==roleid)
		{
			rolename=currentdata[i].rolename;			
		}
	}
	
	//直接用roleid的话会出现数据不对的情况...
	linkroleid=roleid;
	$("#my-modal-confirm #msgcontent").html("确认删除["+rolename+"]角色的信息?");
	$("#my-modal-confirm").modal({
        relatedTarget: this,
        onConfirm: function(e) {        	        	        	
        	
        	value={};        		        			        
        	value.roleid=linkroleid;		        			        	        		                        
        	valuelist=[];		
        	valuelist.push(value);		        	        	        	
        	                	
        	showLoadingDialog();
        	myvalue.ajax(
        			{			
        				url: "/efs-portal/rest/ui/module/deleteStandardTable.action?tablename="+curdtable,			
        				type: "post",		
        				data: JSON.stringify(valuelist),
        				success: function (text) {
        					var data=JSON.parse(text);					
        					if (data.code=="1") {		        								
        					    //重新载入数据....
        			        	loaddata("删除数据成功!");        			        	
        					} else {
        						updateLoadingDialog("删除数据失败!<br>失败原因:"+data.msg);						
        					}        					
        				},        
        				error: function (jqXHR, textStatus, errorThrown) {
        					updateLoadingDialog("失败: 网络中断或接口异常!");
        				}					
        			}
        	);	        	        	
        },
        onCancel: function() {        	
        	//取消更新
        }
    });	
}



function updaterow(roleid)
{	
	for (var i=0;i<currentdata.length;i++)
	{
		if (currentdata[i].roleid==roleid)
		{
			$('#editform #rolename').val(currentdata[i].rolename);
			$('#editform #note').val(currentdata[i].note);
		}
	}
	
	//直接用roleid的话会出现数据不对的情况...
	linkroleid=roleid;
	
	$('#editform').modal({
        relatedTarget: this,
        closeViaDimmer:false,
        onConfirm: function(e) {        	        	
        	rolename=$(e.target).find("#rolename").val();
        	note=$(e.target).find("#note").val();        	
        	value={};
        	value.old_roleid=linkroleid;		        			        	
        	value.rolename=rolename;
        	value.note=note;	                         
        	valuelist=[];		
        	valuelist.push(value);		        	        	        	
        	
        	showLoadingDialog();
        	
        	myvalue.ajax(
        			{			
        				url: "/efs-portal/rest/ui/module/updateStandardTable.action?tablename="+curdtable,			
        				type: "post",		
        				data: JSON.stringify(valuelist),
        				success: function (text) {
        					var data=JSON.parse(text);					
        					if (data.code=="1") {		        						
        						//重新载入数据....
        			        	loaddata("修改数据成功!");
        					} else {
        						updateLoadingDialog("修改数据失败!<br>失败原因:"+data.msg);						
        					}        					
        				},        
        				error: function (jqXHR, textStatus, errorThrown) {
        					updateLoadingDialog("失败: 网络中断或接口异常!");
        				}					
        			}
        	);	
        	
        	
        },
        onCancel: function() {        	
        	//取消更新
        }
    });
    	
}


function showLoadingDialog()
{		
	$("#my-modal-loading #msgcontent").html("正在处理，请稍候...");
	$("#my-modal-loading #loadingicon").show();
	
	if ($("#my-modal-loading").is(":hidden"))
	{		
		$("#my-modal-loading").modal("open");	
	}		
}

function closeLoadingDialog()
{			
	setTimeout(function()
		{			
			if (!$("#my-modal-loading").is(":hidden"))
			{					
				$("#my-modal-loading").modal("close");	
			}
		}
		,300
	);			
} 

function updateLoadingDialog(msg)
{	
	$("#my-modal-loading #msgcontent").html(msg);
	$("#my-modal-loading #loadingicon").hide();
	if ($("#my-modal-loading").is(":hidden"))
	{		
		//如果结果返回的时候，遮罩层已经被点击关闭了则会进入这里.....		
	}		
}

function showMsgDialog(msg)
{			
	$("#my-modal-msg #msgcontent").html(msg);		
	if ($("#my-modal-msg").is(":hidden"))
	{		
		$("#my-modal-msg").modal("open");	
	}
}



function getRowHTML(datajson,subHTML)
{
	var subList="";											
	var HTMLArray = subHTML.split('||');						
	
	for (var i = 0; i <= datajson.length-1; i++)
	{														
			datajson[i].serialid=i+1;												
						
			jsonflag=false;
			
			for (var j = 0; j <= HTMLArray.length-1; j++)
			{
				
				//只要html里面不直接用||作为开头，那么用基/偶数就就可以判断是否是变量了....
				if (jsonflag==true)
				{
					subList += datajson[i][HTMLArray[j]]; 				
				} else {
					subList += HTMLArray[j];
				}	
				
				//取反
				jsonflag=!jsonflag;
			}						
	}
	
	return subList;
}

function setmodule(roleid,rolename)
{	
	
	linkroleid=roleid;
	$("#role-module-title").html(rolename);
	showLoadingDialog();		
	myvalue.ajax({						
		url: "/efs-portal/rest/ui/module/getRoleModule.action?roleid="+linkroleid,			
		type: "post",					
		success: function (text) {					
			$("#allot-module-form .module input:checkbox").attr("checked",false);
			
			var result=JSON.parse(text);					
			if (result.code=="1") {
				//取消所有全选的勾选...
				
				$(".allcheck").attr("checked",false);
				
				//默认勾上已有模块权限...				
				modulelist=[];
				modulelist=result.data;
				
				
				
				for(var i=0;i<modulelist.length;i++)
				{
					
					moduleid=modulelist[i].moduleid;
					
					$("#module_"+moduleid).attr("checked",true);
				
					modulecheckboxclick(modulelist[i].menuid);
				}		
				
			    /*填充用户按纽权限*/
				var  usercontrol =  result.usercontroldata;
				$(".control_check").attr("checked",false);
				for(var j=0;j<usercontrol.length;j++)
				{
					var controlid=usercontrol[j].controlid;
					$("#control_"+controlid).attr("checked",true);
				}
				
				//关闭加载框,如果关闭得太快...则会造成遮罩层被取消....	
				closeLoadingDialog();
				
				$("#allot-module-form").modal({
					width:"900px",
					relatedTarget: this,
					closeViaDimmer:false,
			        onConfirm: function(e) {
			        		        	
			        	modulelist=[];
			        	
			        	$("#allot-module-form .module input:checkbox").each(function(){ 
			        		if ($(this).attr("checked"))
			        		{
			        			module={};
			        			module.moduleid=$(this).data("moduleid");
			        			modulelist.push(module);
			        		}
			        	});
			        	
			        	
                        controllist=[];
			        	$("#allot-module-form .control input:checkbox").each(function(){ 
			        		if ($(this).attr("checked"))
			        		{
			        			control={};
			        			control.controlid=$(this).data("controlid");
			        			controllist.push(control);
			        		}
			        	});
			        	
						showLoadingDialog();
						
					
						myvalue.ajax(
							{			
								url: "/efs-portal/rest/ui/module/setRoleModule.action?roleid="+linkroleid,			
								type: "post",		
								data: JSON.stringify(modulelist),
								success: function (text) {
									var data=JSON.parse(text);
																			
									if (data.code=="1") {										
										//重新载入数据....
							        	//loaddata("更新数据成功!");	
										updatecontrol(linkroleid,controllist);
									} else {
										updateLoadingDialog("更新数据失败!<br>失败原因:"+data.msg);						
									}       																									 			
								},        
								error: function (jqXHR, textStatus, errorThrown) {
									updateLoadingDialog("更新数据失败: 网络中断或接口异常!");
								}					
							}
						);	
						
						
						
						        	
			        	//更新角色模块权限...			        
					},
					onCancel: function() {        	
						closeLoadingDialog();
					}
				});			        		
			} else {
				updateLoadingDialog("获取角色模块数据失败!<br>失败原因:"+data.msg);						
			}					
		},
		error: function (jqXHR, textStatus, errorThrown) {
			updateLoadingDialog("失败: 网络中断或接口异常!");
		}
	});
	
}

//更新用户对应的功能权限
function updatecontrol(linkroleid,controllist)
{
    myvalue.ajax
    (
          {			
				url: "/efs-portal/rest/ui/module/setRoleControl.action?roleid="+linkroleid,			
				type: "post",		
				data: JSON.stringify(controllist),
				success: function (text) {
					var data=JSON.parse(text);
															
					if (data.code=="1") {										
						//重新载入数据....
			        	loaddata("更新数据成功!");				        		
					} else {
						updateLoadingDialog("更新数据失败!<br>失败原因:"+data.msg);						
					}       																									 			
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					updateLoadingDialog("更新数据失败: 网络中断或接口异常!");
				}					
			}
	);		
}
function menucheckboxclick(obj,moduleobj)
{			
	if (obj.checked)
	{
		$(moduleobj).find("input:checkbox").attr("checked",true);
		
	} else {	
		$(moduleobj).find("input:checkbox").attr("checked",false);
	}
}

function modulecheckboxclick(menuid)
{
	
	//判断如果所有的子元素都为已勾选,则全选为选中状态,否则为未选中....
	allcheck=true;
	
	$("#menu_"+menuid+" input:checkbox").each(function(){ 
		
		if (!$(this).attr("checked"))
		{
			allcheck=false;
			return;
		}
	});
	
	$("#menu_all_"+menuid).attr("checked",allcheck);

}

$('.am-modal').on('opened.modal.amui', function () {	
	$(".am-dimmer,.am-active").show();
});