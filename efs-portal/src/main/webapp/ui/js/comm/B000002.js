//单条处理数据模块,数据量不大,比较重要的数据适用于本模块......


//系统变量
var myvalue= new myapp();
var grid;
var editform;
var currentRowIndex=0;
var prerowid=-1;
var newWindow;

//初始化页面....
init();

function init()
{		
	 
	mini.parse();	
	
	grid = mini.get("datagrid1");
	
	grid.load();
			
	editform = document.getElementById("editform");		
	
	//绑定表单项目
	var db = new mini.DataBinding();
	db.bindForm("editform", grid);		
	
	//手动绑定表单数据
	grid.on("select", function (e) {	
		
		row=grid.getSelected();
		
		currentRowIndex= grid.indexOf(row);								
									
	});
	
	//每次刷新选择上次的行...
	grid.on("load", function(e){		
		
		grid.select(currentRowIndex,true);
		
		if (grid.getSelected()==null)
		{
			grid.select(0,true);		
		}
				
	});
	
	newWindow = mini.get("newWindow");
	
	if ($("#newform").hasClass("useedit"))
	{
		$("#newform").html($("#editform").html());
	}
		
	$("#datagrid1").attr("disabled", true);
		
	//新增加弹出页-保存按钮	
	$("#newform #btn-save").live("click",function (){
		try
		{
			
			if (!beforeAddRowSave($("#newform")))
			{
				return;
			}
		}  catch (e){}		
		
		addRow($("#newform"));
	});
	
	//新增加弹出页-取消按钮			
	$("#newform #btn-cancel").live("click",function (){		
		newWindow.hide();
	});
	
	//新增加弹出页-取消按钮	
	$("#editform #btn-cancel").die('click');		
	$("#editform #btn-cancel").live("click",function (){
		canceledit();		
	});
	
}

function closeiframe(moduleid) {
	
	//有需要的时候在这里做一些询问判断
	
	//返回true代表可以关闭窗体，返回false代表不可以关闭窗体 
	return true;
}

//新增数据
function sys_add()
{			
	
    for(var i=0;i<grid.columns.length;i++)
	{
		fieldname=grid.columns[i].field;
		$("#newform #"+fieldname).val("");					
	}        
    
	newWindow.show();
	
	//用于设置新增数据的默认值和是否可读
	try
	{
		beforeAppendRow($("#newform"));
	}  catch (e){}
			
}	
	

//修改数据
function sys_edit(rowid)
{					
	grid.hideAllRowDetail();
	
	if (rowid==prerowid)
	{		
		$("#btn-add").attr("disabled", false); 	
		$("#btn-outfile").attr("disabled", false);	
		
		prerowid=-1;
		return;
	} else {
		prerowid=rowid;
	
	}
		
	row=grid.getSelected();
		
    if (row) {    	    
    	
        //显示行详细        
        grid.showRowDetail(row);
                        
        //将editForm元素，加入行详细单元格内
        var td = grid.getRowDetailCellEl(row);
        td.appendChild(editform);
        
        for(var i=0;i<grid.columns.length;i++)
		{
			fieldname=grid.columns[i].field;
			$("#editform #"+fieldname).val(row[fieldname]);					
		}	
        
        //编辑行数据前执行更新预处理...        
        try
    	{
        	beforeUpdateRow($("#editform"));
    	}  catch (e){}                
       
        //新增加弹出页-保存按钮	
        $("#editform #btn-save").die("click");
    	$("#editform #btn-save").live("click",function (){    		
    		try
    		{
    			if (!beforeUpdateRowSave($("#editform")))
    			{
    				return;
    			}
    		}  catch (e){}
    		
    		updateRow($("#editform"),rowid);
    	});
       	
    }

    grid.doLayout();
	
	$("#datagrid1").attr("disabled", true);	
	$("#btn-add").attr("disabled", true); 
	$("#btn-outfile").attr("disabled", true);		
	
}

//删除数据
function sys_delete(rowid)
{					
	//必须选中一行...		
	myvalue.showAskDialog("确认删除此行记录?","suredeleteRow("+rowid+")");
}

function suredeleteRow(rowid)
{	
	deleteRow(rowid);
	$("#btn-add").attr("disabled", false); 	
	$("#btn-outfile").attr("disabled", false);	
}

function sys_saveover()
{
	grid.hideAllRowDetail();	
	prerowid=-1;
	grid.unmask();
	$("#datagrid1").attr("disabled", false); 
	$("#btn-add").attr("disabled", false); 	
	$("#btn-outfile").attr("disabled", false);		
	grid.reload();
}



//取消修改
function canceledit()
{			
	grid.unmask();
	$("#btn-add").attr("disabled", false); 	
	$("#btn-outfile").attr("disabled", false);			
	
	//重新选择,以更新绑定的数据.........
	var row=grid.getSelected();
	var rowIndex = grid.indexOf(row); 
	grid.hideAllRowDetail();
	prerowid=-1;
	//grid.select(rowIndex,true);			
}
