var myvalue= new myapp();

var curdtable="company";

function deleteRow(rowid)
{	
	row=grid.getRow(rowid);		
	value={};		
	value.companyid=row.companyid;	
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
	value.companytype=$(m).find("#companytype").val();	
	value.companyname=$(m).find("#companyname").val();
	value.shortname=$(m).find("#shortname").val();
	value.address=$(m).find("#address").val();
	value.companytel=$(m).find("#companytel").val();
	value.contacttel=$(m).find("#contacttel").val();
	value.email=$(m).find("#email").val();
	value.fax=$(m).find("#fax").val();
	value.taxno=$(m).find("#taxno").val();
	value.industries=$(m).find("#industries").val();
	value.legalperson=$(m).find("#legalperson").val();
	value.depositbank=$(m).find("#depositbank").val();
	value.bankno=$(m).find("#bankno").val();	
	
	valuelist=[];		
	valuelist.push(value);
	
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/module/registerCompany.action?",
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
	         	
	value.old_companyid=$(m).find("#companyid").val();	
	value.companytype=$(m).find("#companytype").val();	
	value.companyname=$(m).find("#companyname").val();
	value.shortname=$(m).find("#shortname").val();
	value.address=$(m).find("#address").val();
	value.companytel=$(m).find("#companytel").val();
	value.contacttel=$(m).find("#contacttel").val();
	value.email=$(m).find("#email").val();
	value.fax=$(m).find("#fax").val();
	value.taxno=$(m).find("#taxno").val();
	value.industries=$(m).find("#industries").val();
	value.legalperson=$(m).find("#legalperson").val();
	value.depositbank=$(m).find("#depositbank").val();
	value.bankno=$(m).find("#bankno").val();
	
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
	$(m).find("#intime").attr("disabled", true);		
	$(m).find("#companytype").val("私企");	
}

//处理不允许操作的列
function beforeUpdateRow(m)
{	
	$(m).find("#companyid").attr("disabled", true);
	$(m).find("#intime").attr("disabled", true);
}

//处理保存新增时的检查
function beforeAddRowSave(m)
{				
	if (!myvalue.checkinput($(m).find("#companytype")))		
	{
		myvalue.showMsgDialog("企业类型不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#companyname")))
	{
		myvalue.showMsgDialog("企业名称不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#shortname")))
	{
		myvalue.showMsgDialog("企业简称不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#address")))
	{
		myvalue.showMsgDialog("企业地址不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#companytel")))
	{
		myvalue.showMsgDialog("企业电话不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#contacttel")))
	{
		myvalue.showMsgDialog("联系方式不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#email")))
	{
		myvalue.showMsgDialog("电子邮箱不能为空!");
		return false;
	}
	
	return true;
}

//处理保存更改时的检查
function beforeUpdateRowSave(m)
{
	if (!myvalue.checkinput($(m).find("#companytype")))		
	{
		myvalue.showMsgDialog("企业类型不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#companyname")))
	{
		myvalue.showMsgDialog("企业名称不能为空!");
		return false;
	}
	
	
	if (!myvalue.checkinput($(m).find("#shortname")))
	{
		myvalue.showMsgDialog("企业简称不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#address")))
	{
		myvalue.showMsgDialog("企业地址不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#companytel")))
	{
		myvalue.showMsgDialog("企业电话不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#contacttel")))
	{
		myvalue.showMsgDialog("联系方式不能为空!");
		return false;
	}
		
	if (!myvalue.checkinput($(m).find("#email")))
	{
		myvalue.showMsgDialog("电子邮箱不能为空!");
		return false;
	}
	
	return true;
}


function outfile()
{
	myvalue.showMsgDialog("正在导出文件!");
}