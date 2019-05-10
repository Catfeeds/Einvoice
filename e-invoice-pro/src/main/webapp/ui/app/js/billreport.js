var user;
var channel = "app";
$(document).ready(function(){
	search(0);
	loaddate();
});


function search(page){
	var data ={};
	data.page=page;
	var requestfphm = $("#requestfphm").val();
	var sheetid = $("#requestsheetid").val();
	data.startdate=$("#startdate").val();
	data.enddate=$("#enddate").val();
	if(requestfphm !='' && requestfphm!=null){
		data.fphm=requestfphm;
	}
	if(sheetid !='' && sheetid!=null ){
		data.sheetid=sheetid;
	}
	if(data.startdate !='' && data.startdate!=null){
		data.startdate=data.startdate.replace(/-/g,"");
	}
	if(data.enddate !='' && data.enddate!=null){
		data.enddate=data.enddate.replace(/-/g,"")+"9999";
	}
		$.myAjax({
			url:$.getService("ui","getInvoiceHeadReport"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				for (var i=0;i<dataJson.data.length;i++){
					if(dataJson.data[i].fprq != ''){
						dataJson.data[i].fprq=dataJson.data[i].fprq.substr(0,4)+"-"+dataJson.data[i].fprq.substr(4,2)+"-"+dataJson.data[i].fprq.substr(6,2);
					}
					if(dataJson.data[i].invoicetype == '0'){
						dataJson.data[i].invoicetype="蓝票";
					}else if(dataJson.data[i].invoicetype == '1'){
						dataJson.data[i].invoicetype="红冲票";
					}
				}
				$.myPage(page,{rows:dataJson.count,jump:function(page){
					search(page);
				}});
				$.myRowHTML(dataJson.data,"rowmo","row1");
				$("#pagecount").html("共" +dataJson.count+ "条数据,&nbsp;&nbsp;"+"总金额合计："+dataJson.zamt);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}


var showinvoiceid;
function showModal(invoiceid){
	showinvoiceid=invoiceid;
	$('#taxmodal').modal({width:'800',height:'500'});
	showDetail(0);
}

function showDetail(page){
	var data={};
	data.page=page;
	data.pagesize=8;
	data.invoiceid=showinvoiceid;
	$.myAjax({
		url:$.getService("ui","getInvoiceDetail"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myPage(page,{rows:dataJson.count,pagesize:8,targetId:"page1",jump:function(page){
				showDetail(page);
			}});
			$.myRowHTML(dataJson.data,"rowmo2","searchRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}




function showFapiao(url){
	if(url != null && url != ''){
		var link= $('<a href="'+url+'" target="_blank"></a>');
		link.get(0).click();
	}
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

function download(){
	var data ={};
	data.page=page;
	var requestfphm = $("#requestfphm").val();
	var sheetid = $("#requestsheetid").val();
	data.startdate=$("#startdate").val();
	data.enddate=$("#enddate").val();
	if(requestfphm !='' && requestfphm!=null){
		data.fphm=requestfphm;
	}
	if(sheetid !='' && sheetid!=null ){
		data.sheetid=sheetid;
	}
	if(data.startdate !='' && data.startdate!=null){
		data.startdate=data.startdate.replace(/-/g,"");
	}
	if(data.enddate !='' && data.enddate!=null){
		data.enddate=data.enddate.replace(/-/g,"")+"9999";
	}
	
	//创建Form
    var submitfrm = document.createElement("form");
    submitfrm.action = "/e-invoice-pro/rest/ui/exportInvoiceHeadReport?data="+JSON.stringify(data);
    submitfrm.method = "post";
    document.body.appendChild(submitfrm);
    submitfrm.submit();
    setTimeout(function () {
        submitfrm.parentNode.removeChild(submitfrm);
        params.sheetid="";
        window.location.href=window.location.href;
    }, 2000);
    
}
