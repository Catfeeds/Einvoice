var curdtable="shop";
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
	var searchid = $("#edt-search").val();
	
	showLoadingDialog(msg+"正在获取最新数据,请稍候...");

	myvalue.ajax(
		{						
			url: "/efs-portal/rest/ui/module/getShopInfo.action?data="+searchid,			
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
					} else {
						updateLoadingDialog(msg);
					}
					closeLoadingDialog();		
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
}

function addrow()
{
	
	$("#newform #shopid").val("");
	$("#newform #shopname").val("");
	$("#newform #note").val("");
		
	$("#newform").modal({
		relatedTarget: this,
		closeViaDimmer:false,
        onConfirm: function(e) { 
        	
        	var shopid=$(e.target).find("#shopid").val();
			var shopname=$(e.target).find("#shopname").val();
			var shoptype=0;
			var note=$(e.target).find("#note").val();
			
			value={};
						
			value.shopid=shopid;
			value.shopname=shopname;
			value.shoptype=shoptype;
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
		onCancel: function() {        	
			//取消更新
		}
	});		
}

function deleterow(shopid)
{
	for (var i=0;i<currentdata.length;i++)
	{
		if (currentdata[i].shopid==shopid)
		{
			shopname=currentdata[i].shopname;			
		}
	}
	
	//直接用shopid的话会出现数据不对的情况...
	linkshopid=shopid;
	$("#my-modal-confirm #msgcontent").html("确认删除["+shopname+"]角色的信息?");
	$("#my-modal-confirm").modal({
        relatedTarget: this,
        onConfirm: function(e) {        	        	        	
        	
        	value={};        		        			        
        	value.shopid=linkshopid;		        			        	        		                        
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



function updaterow(shopid)
{		
	for (var i=0;i<currentdata.length;i++)
	{
		if (currentdata[i].shopid==shopid)
		{
			$('#editform #shopid').val(currentdata[i].shopid);
			$('#editform #shopname').val(currentdata[i].shopname);
			$('#editform #note').val(currentdata[i].note);
		}
	}
	
	//直接用shopid的话会出现数据不对的情况...
	linkshopid=shopid;
	
	$('#editform').modal({
        relatedTarget: this,
        closeViaDimmer:false,
        onConfirm: function(e) {
        	shopid=$(e.target).find("#shopid").val();
        	shopname=$(e.target).find("#shopname").val();
        	note=$(e.target).find("#note").val();        	
        	value={};
        	value.old_shopid=linkshopid;		        			        	
        	value.shopname=shopname;
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


$('.am-modal').on('opened.modal.amui', function () {	
	$(".am-dimmer,.am-active").show();
});