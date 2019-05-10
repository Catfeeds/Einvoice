var user;
var channel = "app";
$(document).ready(function(){
	$.mySelect({
		tragetId:"shopname",
		id:"shopid",
		url:$.getService("ui","getShopList"),
		onChange:function(e){
			//console.log(e);
		}
	});
	loaddate();
});


function search(page){
	var data ={};
	data.page=page;
	data.shopid=$("#shopid").val();
	var sheetid = $("#sheetid").val();
	data.startdate=$("#startdate").val();
	data.enddate=$("#enddate").val();
	if(data.shopid =='' || data.shopid==null){
		$.myAlert("请选择门店");
		return;
	}
	if(sheetid !='' && sheetid!=null){
		data.sheetid=sheetid;
	}
	if(data.startdate !='' && data.startdate!=null){
		data.startdate=data.startdate.replace(/-/g,"");
	}
	if(data.enddate !='' && data.enddate!=null){
		data.enddate=data.enddate.replace(/-/g,"")+"9999";
	}
		$.myAjax({
			url:$.getService("api","querySheetLog"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				for (var i=0;i<dataJson.data.length;i++){
					if(dataJson.data[i].logtime != ''){
						dataJson.data[i].logtime=TimeObjectUtil.longMsTimeConvertToDateTime(dataJson.data[i].logtime);
					}
				}
				$.myPage(page,{rows:dataJson.count,jump:function(page){
					search(page);
				}});
				$.myRowHTML(dataJson.data,"rowmo","row1");
				$("#pagecount").html("共：" +dataJson.count+ " 条数据,&nbsp;&nbsp;");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert(" status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}


function loaddate(){ 
	var now = new Date;
	var now1=new Date;
	var now2=new Date;
	var enddate=TimeObjectUtil.longMsTimeConvertToDate(now);
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
	    defaultDate:enddate,//默认日期  
	    minDate:mindate,//最小日期  
	    maxDate:enddate,//最大日期  
	    onSelect: function(selectedDate) {//选择日期后执行的操作  
	    }  
	    }); 
    
    $("#startdate").datepicker( "setDate", enddate );
	$("#enddate").datepicker( "setDate", enddate);
} 



