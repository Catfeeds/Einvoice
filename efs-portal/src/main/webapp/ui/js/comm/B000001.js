//单条处理数据模块,数据量不大,比较重要的数据适用于本模块......

//系统变量
var isAdd=false;
var isEdit=false;
var myvalue= new myapp();
var mydialog=new AppDialog();
var grid;
var currentRowIndex=0;

//初始化页面....
init();

function init()
{
	mini.parse();	
	grid = mini.get("datagrid1");
	grid.load();		

	//绑定表单
	var db = new mini.DataBinding();
	db.bindForm("editform", grid);		
	
	//手动绑定数据
	grid.on("select", function (e) {
		
		row=grid.getSelected();
		
		currentRowIndex= grid.indexOf(row);				
		
		for(var i=0;grid.columns.length;i++)
		{
			fieldname=grid.columns[i].field;
			$("#editform #"+fieldname).val(row[fieldname]);			
		}	
		
	});
	
	//每次刷新选择上次的行...
	grid.on("load", function(e){		
		
		grid.select(currentRowIndex,true);
		
		if (grid.getSelected()==null)
		{
			grid.select(0,true);		
		}
		
		//代表没有数据....
		if (grid.getSelected()==null)
		{			
			$("#editform input:text").val("");
			$("#editform select").val("");
		}
	});
	
	//初始化按钮
	$("#btn-save").attr("disabled", true);
	$("#btn-cancel").attr("disabled", true);
	$("#editform input:text").attr("disabled", true);
	$("#editform select").attr("disabled", true);	
	$("#datagrid1").attr("disabled", true);	

}

function closeiframe(moduleid) {
	
	//有需要的时候在这里做一些询问判断
	
	//返回true代表可以关闭窗体，返回false代表不可以关闭窗体 
	return true;
}

//新增数据
function sys_add()
{
	grid.loading();	
	$("#datagrid1").attr("disabled", true); 
	$("#btn-add").attr("disabled", true); 
	$("#btn-edit").attr("disabled", true); 
	$("#btn-delete").attr("disabled", true);
	$("#btn-outfile").attr("disabled", true);	
	$("#btn-save").attr("disabled", false);
	$("#btn-cancel").attr("disabled", false);
	$("#editform input:text").attr("disabled", false);
	$("#editform select").attr("disabled", false);	
	$("#editform input:text").val("");
	$("#editform select").val("");
	
	isAdd=true;
}

//修改数据
function sys_edit()
{	
	//必须选中一行...
	if (grid.getSelected()==null) 
	{
		mydialog.showMsgDialog("请先选择需要编辑的数据!");
		return;
	}		
	
	grid.loading();	
	
	$("#datagrid1").attr("disabled", true);	
	$("#btn-add").attr("disabled", true); 
	$("#btn-edit").attr("disabled", true); 
	$("#btn-delete").attr("disabled", true);
	$("#btn-outfile").attr("disabled", true);	
	$("#btn-save").attr("disabled", false);
	$("#btn-cancel").attr("disabled", false);
	$("#editform input:text").attr("disabled", false);
	$("#editform select").attr("disabled", false);
	isEdit=true;
}


//保存数据(新增/修改)
function sys_save()
{				
	
	
	//新增
	if (isAdd)
	{
		mydialog.showAskDialog("确认新增记录?","addRow()");
	}
		
	//修改
	if (isEdit)
	{				
		mydialog.showAskDialog("确认修改记录?","updateRow()");
	}
	
}

//取消操作(新增/修改)
function sys_cancel()
{		
	grid.unmask();
	$("#btn-add").attr("disabled", false); 
	$("#btn-edit").attr("disabled", false); 
	$("#btn-delete").attr("disabled", false);
	$("#btn-outfile").attr("disabled", false);	
	$("#btn-save").attr("disabled", true);
	$("#btn-cancel").attr("disabled", true);
	$("#editform input:text").attr("disabled", true);
	$("#editform select").attr("disabled", true);
	isEdit=false;
	isAdd=false;
	
	//重新选择,以更新绑定的数据.........
	var row=grid.getSelected();
	var rowIndex = grid.indexOf(row); 
	grid.select(rowIndex,true);	
}

//删除数据
function sys_delete()
{	
	//必须选中一行...
	if (grid.getSelected()==null) 
	{
		mydialog.showMsgDialog("请先选择需要删除的数据!");
		return;
	}	
	
	mydialog.showAskDialog("确认删除此行记录?","deleteRow()");
}

function sys_saveover()
{
	grid.reload();
	grid.unmask();
	$("#datagrid1").attr("disabled", false); 
	$("#btn-add").attr("disabled", false); 
	$("#btn-edit").attr("disabled", false); 
	$("#btn-delete").attr("disabled", false);
	$("#btn-outfile").attr("disabled", false);	
	$("#btn-save").attr("disabled", true);
	$("#btn-cancel").attr("disabled", true);
	$("#editform input:text").attr("disabled", true);
	$("#editform select").attr("disabled", true);
	isEdit=false;
	isAdd=false;
}
