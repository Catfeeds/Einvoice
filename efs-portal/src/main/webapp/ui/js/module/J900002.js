var curdtable="login";

init();

function init()
{
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/module/getCompanyInfo.action?",			
				type: "post",						
				success: function (text) {  
					var data=JSON.parse(text).data;
					$("#newform #companyname").append("<option value=''></option>");
					for(var i=0;i<data.length;i++)
					{
						$("#newform #companyname").append("<option value="+data[i].companyid+">"+data[i].companyname+"</option>");
					}
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					
				}					
			}
	);
}

function selectCompany()
{
	$("#newform #companyid").val($("#newform #companyname").val());
}

function deleteRow(rowid)
{	
	row=grid.getRow(rowid);		
	value={};		
	value.companyid=row.companyid;
	value.loginid=row.loginid;	
	valuelist=[];		
	valuelist.push(value);	
		
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/module/deleteStandardTable.action?tablename="+curdtable,			
				type: "post",		
				data: JSON.stringify(valuelist),
				success: function (text) {  
					var data=JSON.parse(text);					
					if (data.code=="1") {		
						alert("删除数据成功!");
						grid.reload();
					} else {
						alert("删除数据失败!<br>失败原因:"+data.msg);
					}
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("失败:  网络中断或接口异常!");
				}					
			}
	);	

}

function addRow(m)
{			
	value={};
	value.companyid=$(m).find("#companyid").val();
	value.loginid=$(m).find("#loginid").val();	
	value.username=$(m).find("#username").val();	
	value.password=$(m).find("#password").val();
	value.logintype=$(m).find("#logintype").val();
	value.idcard=$(m).find("#idcard").val();
	value.contacttel=$(m).find("#contacttel").val();
	value.isstop=$(m).find("#isstop").val();
	value.logintimes=$(m).find("#logintimes").val();
	value.note=$(m).find("#note").val();	
	valuelist=[];		
	valuelist.push(value);
	
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/module/registerCompanyLogin.action?",			
				type: "post",		
				data: JSON.stringify(valuelist),
				success: function (text) {  
					var data=JSON.parse(text);					
					if (data.code=="1") {		
						alert("新增数据成功!");	
						//关闭弹出框
						newWindow.hide();
					} else {
						alert("新增数据失败!<br>失败原因:"+data.msg);
					}
					sys_saveover();
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("失败:  网络中断或接口异常!");
				}					
			}
	);	
}


function updateRow(m,rowid)
{	
	row=grid.getRow(rowid);
	value={};	
	         
	value.old_companyid=row.companyid;		
	value.old_loginid=row.loginid;		
		
	value.username=$(m).find("#username").val();
	value.password=$(m).find("#password").val();	                    
	value.logintype=$(m).find("#logintype").val();
	value.idcard=$(m).find("#idcard").val();
	value.contacttel=$(m).find("#contacttel").val();
	value.isstop=$(m).find("#isstop").val();
	value.logintimes=$(m).find("#logintimes").val();
	value.note=$(m).find("#note").val();	
	valuelist=[];		
	valuelist.push(value);		
		
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/module/updateStandardTable.action?tablename="+curdtable,			
				type: "post",		
				data: JSON.stringify(valuelist),
				success: function (text) {  
					var data=JSON.parse(text);					
					if (data.code=="1") {		
						alert("修改数据成功!");						
					} else {
						alert("修改数据失败!<br>失败原因:"+data.msg);						
					}
					sys_saveover();
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("失败:  网络中断或接口异常!");
				}					
			}
	);	
}

//处理默认值和不允许操作的列
function beforeAppendRow(m)
{	
	$(m).find("#companyid").attr("disabled", true);
	$(m).find("#logintimes").attr("disabled", true);	
	$(m).find("#logintype").attr("disabled", true);
	$(m).find("#isstop").val(0);
	$(m).find("#logintype").val(1);
	
}

//处理不允许操作的列
function beforeUpdateRow(m)
{	
	$(m).find("#companyid").attr("disabled", true);
	$(m).find("#logintype").attr("disabled", true);
	$(m).find("#companyname").attr("disabled", true);
	$(m).find("#loginid").attr("disabled", true);
	$(m).find("#logintimes").attr("disabled", true);		
}

//处理保存新增时的检查
function beforeAddRowSave(m)
{	
	if (!myvalue.checkinput($(m).find("#companyid")))
	{
		mydialog.showMsgDialog("请选择企业!");
		return false;
	}
	
	if (!myvalue.checkinput($(m).find("#loginid")))
	{
		mydialog.showMsgDialog("账号不能为空!");
		return false;
	}
	
	if (!myvalue.checkinput($(m).find("#username")))
	{
		mydialog.showMsgDialog("姓名不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#password")))
	{
		mydialog.showMsgDialog("密码不能为空!");
		return false;
	}
	
	return true;
}

//处理保存更改时的检查
function beforeUpdateRowSave(m)
{
	if (!myvalue.checkinput($(m).find("#loginid")))
	{
		mydialog.showMsgDialog("账号不能为空!");
		return false;
	}
	
	if (!myvalue.checkinput($(m).find("#username")))
	{
		mydialog.showMsgDialog("姓名不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#password")))
	{
		mydialog.showMsgDialog("密码不能为空!");
		return false;
	}
	
	return true;
}


function outfile()
{
	mydialog.showMsgDialog("正在导出文件!");
}