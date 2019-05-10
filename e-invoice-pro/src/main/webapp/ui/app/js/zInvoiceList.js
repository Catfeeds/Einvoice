var user;
var channel = "app";
$(document).ready(function(){
//	$.mySelect({
//		tragetId:"shopname",
//		id:"shopid",
//		url:$.getService("ui","getShopList"),
//		onChange:function(e){
//			//console.log(e);
//		}
//	});
	loaddate();
	getuser();
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
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function search(page){
	var data ={};
	data.page=page;
	data.shopid=user.shopid;
	var requestfphm = $("#requestfphm").val();
	var sheetid = $("#sheetid").val();
	var goodsid = $("#goodsid").val();
	data.startdate=$("#startdate").val();
	data.enddate=$("#enddate").val();
	if(data.shopid =='' || data.shopid==null){
		$.myAlert("请选择门店");
		return;
	}
	if(requestfphm !='' && requestfphm!=null){
		data.fphm=requestfphm;
	}
	if(goodsid !='' && goodsid!=null){
		data.goodsid=goodsid;
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
	data.goodsname=$("#goodsname").val();
		$.myAjax({
			url:$.getService("ui","getInvoiceDetailForSum"),
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
				}
				$.myPage(page,{rows:dataJson.count,jump:function(page){
					search(page);
				}});
				$.myRowHTML(dataJson.data,"rowmo","row1");
				$("#pagecount").html("共：" +dataJson.count+ " 条数据,&nbsp;&nbsp;"+"数量合计："+dataJson.zqty+",&nbsp;&nbsp;总金额合计："+dataJson.zamt);
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


function download(){
    var form = $("<form></form>").attr("action", $.getService("ui","exportInvoiceDetailForSum")).attr("method", "post");
	var data ={};
	data.shopid=user.shopid;
	var requestfphm = $("#requestfphm").val();
	var goodsid = $("#goodsid").val();
	data.startdate=$("#startdate").val();
	data.enddate=$("#enddate").val();
	if(data.shopid =='' || data.shopid==null){
		$.myAlert("请选择门店");
		return;
	}
	if(requestfphm !='' && requestfphm!=null){
		data.fphm=requestfphm;
	}
	if(goodsid !='' && goodsid!=null){
		data.goodsid=goodsid;
	}
	if(data.startdate !='' && data.startdate!=null){
		data.startdate=data.startdate.replace(/-/g,"");
	}
	if(data.enddate !='' && data.enddate!=null){
		data.enddate=data.enddate.replace(/-/g,"")+"9999";
	}
	data.goodsname=$("#goodsname").val();


    form.append($("<input></input>").attr("type", "hidden").attr("name", "data").attr("value", JSON.stringify(data)));
    form.appendTo('body').submit().remove();
}

