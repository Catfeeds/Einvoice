var channel = "";
$(document).ready(function(){
	 loaddate();
	 search(0);
	
});

function search(page){
	var params = {};
	params.channel = channel;
	params.page=page;
	var sheetid = $("#sheetid").val();
	
	if(sheetid !='' && sheetid!=null){
		params.sheetid=sheetid;
	}
	
	var isauto = $("#isauto").val();
	params.isauto = isauto;
	params.startdate=$("#startdate").val();
	
	params.enddate=$("#enddate").val();
	$.myAjax({
		url:$.getService("api","getInvquelistForSheetid"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.data.length==0){
				$.myAlert("没有查找到数据");
				return;
			}
			$.myPage(page,{rows:dataJson.count,jump:function(page){
				search(page);
			}});
			$.myRowHTML(dataJson.data,"modelrows","searchRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function detail(iqseqno){
	var params = {};
	params.iqseqno=iqseqno;
	$('#detailmodal').modal({width:'800',height:'500'});
	$.myAjax({
		url:$.getService("api","getInvqueList"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myRowHTML(dataJson.data,"modeldetailrows","detailRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}


function loaddate(){ 
	var now = new Date;
	var now1=new Date;
	var now2=new Date;
	var enddate=TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now.getDate() +1));
	var defaultdate=TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now.getDate() -2));
	var mindate=TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now.getDate() - 192));
	$("#enddate").datepicker({//添加日期选择功能  
	    showButtonPanel:true,//是否显示按钮面板  
	    dateFormat: 'yy-mm-dd',//日期格式  
	    clearText:"清除",//清除日期的按钮名称  
	    closeText:"关闭",//关闭选择框的按钮名称  
	    yearSuffix: '年', //年的后缀  
	    showMonthAfterYear:true,//是否把月放在年的后面  
	    defaultDate:enddate,//默认日期  
	    minDate:mindate,//最小日期  
	    maxDate:enddate,//最大日期  
	    onSelect: function(selectedDate) {//选择日期后执行的操作  
	    }  
	    });
    $("#startdate").datepicker({//添加日期选择功能  
	    showButtonPanel:true,//是否显示按钮面板  
	    dateFormat: 'yy-mm-dd',//日期格式  
	    clearText:"清除",//清除日期的按钮名称  
	    closeText:"关闭",//关闭选择框的按钮名称  
	    yearSuffix: '年', //年的后缀  
	    showMonthAfterYear:true,//是否把月放在年的后面  
	    defaultDate:defaultdate,//默认日期  
	    minDate:mindate,//最小日期  
	    maxDate:enddate,//最大日期  
	    onSelect: function(selectedDate) {//选择日期后执行的操作  
	    }  
	    }); 
    
    $("#startdate").datepicker( "setDate", defaultdate );
	$("#enddate").datepicker( "setDate", enddate);
} 
