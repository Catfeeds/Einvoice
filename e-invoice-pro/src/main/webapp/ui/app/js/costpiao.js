var user;
var channel = "app";
var previewData = {};
$(document).ready(function(){
	loaddate();
	getuser();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
	
	$("#checkall").click(function(){
		var checked = $("#checkall").prop("checked");
		$("input[name='checkrow']").each(function() {
				$(this).prop("checked", checked);
		});
	});
});

function getuser(){
	$.myAjax({
		url:$.getService("ui","getcuruser"),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			user = dataJson.data;
			//search(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function search(page){
	var data ={};
	data.pagesize=1000;
	data.page=page;
	data.sheettype=4;
	data.channel=channel;
	data.shopid=user.shopid;
	data.taxrate=$("#taxrate").val();
	data.itemname=$("#itemname").val();
	data.gmfname=$("#gmfname").val();
	
	data.mintradedate=$("#startdate").val();
	data.maxtradedate=$("#enddate").val();
	if(data.mintradedate !='' && data.mintradedate!=null){
		data.mintradedate=data.mintradedate.replace(/-/g,"");
	}
	if(data.maxtradedate !='' && data.maxtradedate!=null){
		data.maxtradedate=data.maxtradedate.replace(/-/g,"");
	}
		$.myAjax({
			url:$.getService("api","getInvoiceBillDetailList"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myPage(page,{pagesize:1000,rows:dataJson.count,jump:function(page){
					search(page);
				}});
				$.myRowHTML(dataJson.data,"rowmo","row1");
				$("#pagecount").html("共" +dataJson.count+ "条数据");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}

function loaddate(){ 
	var now = new Date;
	var enddate=TimeObjectUtil.longMsTimeConvertToDate(now);
	var mindate=TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now.getDate() - 120));
	$("#enddate").datepicker({//添加日期选择功能  
	    showButtonPanel:true,//是否显示按钮面板  
	    dateFormat: 'yy-mm-dd',//日期格式  
	    clearText:"清除",//清除日期的按钮名称  
	    closeText:"关闭",//关闭选择框的按钮名称  
	    yearSuffix: '年', //年的后缀  
	    showMonthAfterYear:true,//是否把月放在年的后面  
	    defaultDate:enddate,//默认日期  
	    //minDate:mindate,//最小日期  
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
	    defaultDate:mindate,//默认日期  
//	    minDate:mindate,//最小日期  
	    maxDate:enddate,//最大日期  
	    onSelect: function(selectedDate) {//选择日期后执行的操作  
	    }  
	    }); 
    
    $("#startdate").datepicker( "setDate", mindate );
	$("#enddate").datepicker( "setDate", enddate);
} 

function yulan(){
	var requestBill =[];
	//找到所有选中的小票
	var boxes = $("#row1 input[name='checkrow']:checked");
	if(boxes == null || boxes.length == 0){
		$.myAlert("至少选中一条费用");
		return ;
	}
	for(var i=0;i<boxes.length;i++){
		var item={};
		item.sheetid=boxes[i].value.split(",")[0];
		item.sheettype=4;
		item.rowno=boxes[i].value.split(",")[1];
		item.serialid=boxes[i].value.split(",")[2];
		item.channel=channel;
		item.iqfplxdm='026';
		requestBill.push(item);
	}
	$('#my-yulan').modal();
	askPreView(requestBill);
}

function askPreView(requestBill){
	$("#preview").html("");
	var postData =JSON.stringify(requestBill);
	$.myAjax({
		url:$.getService("api","getInvoicePreview4Detail"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			//把预览的数据存下来，正式提交发票时要用到
			previewData = dataJson.data;
			$.myRowHTML(dataJson.data,"rowPreview","preview",1);
			//写入明细信息
			for ( var i = 0; i < dataJson.data.length; i++) {
				$.myRowHTML(dataJson.data[i].invoicePreviewItem,"rowPreviewDetail","previewDetail"+i,2);
			}
			
			$("#btn_submit").addClass("am-active");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

//提交开票申请
function commit(){
	var postData =JSON.stringify(previewData);
	$.myAjax({
		url:$.getService("api","askInvoice"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			$.myAlert("提交成功，本次共开具"+dataJson.data.length+"张发票");
			$('#my-yulan').modal('close');
			search(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

