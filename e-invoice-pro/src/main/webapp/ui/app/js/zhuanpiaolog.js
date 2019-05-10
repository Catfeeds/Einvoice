var params = {};
var pagesize=10;
var channel = "app";
params.channel = channel;
$(document).ready(function(){
	search(0);
	loaddate();
$("#btnSearch").click(function(event){
		search(0);
	});

});

function search(page){
	params.page=page;
	params.pagesize = pagesize;
	params.sheetid = $("#sheetid").val();

	params.sdate=$("#startdate").val();
	
	params.edate=$("#enddate").val();
		 $.myAjax({
		  		url: $.getService("ui","queryInvoiceFlaglog?"+$.cookGetUrlParam(params)),			
		  		contentType:"application/json;charset=UTF-8",
		  		data:JSON.stringify(params),
			    success: function (data) { 
				
				   if(data.code == "0"){
						for (var i=0;i<data.data.length;i++){
							if(data.data[i].invoicetype=="025"){
								data.data[i].invoicetype="卷票";
							}else if(data.data[i].invoicetype=="026"){
								data.data[i].invoicetype="电子票";
							}else if(data.data[i].invoicetype=="004"){
								 
								data.data[i].invoicetype="专票";
							}else if(data.data[i].invoicetype=="007"){
								data.data[i].invoicetype="普票";
							}
							
							if(data.data[i].flag=="0"){
								data.data[i].flag="未开票";
							}else if(data.data[i].flag=="1"){
								data.data[i].flag="已开票";
							}
							
							if(data.data[i].processtime != ''){
								data.data[i].processtime=TimeObjectUtil.longMsTimeConvertToDateTime(data.data[i].processtime);
							}
						}	   
					   //首次查询page
					$.myPage(page,{rows:data.count,jump:function(page){
						search(page);
					}});
					$.myRowHTML(data.data,"rowSample","rowTraget");
					}else{
						$.myAlert(data.msg,'信息');
					}
			},
		  });
	
}

$(function() {
	  $('#btnNew').on('click', function() {

	    $('#my-add').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.payid = $("#payid").val();
	    	  params.payname = $("#payname").val(); 
	    	  params.paystatus = $("#paystatus").val();
	    	    
		   if(params.payid==""){
			   $.myAlert("id 不能为空！");
		    	return false;
		    }

	        $.myAjax({
	      		url: $.getService("ui","insertPaymode?"+$.cookGetUrlParam(params)),			
	      		contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) { 

	     		   if(data.code == "0"){
	     			  $.myAlert('新增成功');
	     			 search(0);
	     			}else{
	    				$.myAlert(data.msg,'信息');
	    			}
	     	    },
	        });        
	      },
	      onCancel: function(e) {
		    	$("#payid").val("");
		    	$("#payname").val("");
		    	$("#paystatus").val("");

     }
	    });
	  });
	 
	});



function dataEdit(payid) {
	params.payid=payid;
	
	$.myAjax({
  		url: $.getService("ui","getPaymodeById?"+$.cookGetUrlParam(params)),			
  		contentType:"application/json;charset=UTF-8",
	    data:JSON.stringify(params),
	    success: function (data) { 

		$("#payid2").val(data.data.payid);
		$("#payname2").val(data.data.payname);
		$("#paystatus2").val(data.data.paystatus);
		$('select').trigger('changed.selected.amui');
	    },
 });

    $('#my-edit').modal({
      relatedTarget: this,
      onConfirm: function(e) {

    	  params.payid = $("#payid2").val();
    	  params.payname = $("#payname2").val();
    	  params.paystatus = $("#paystatus2").val();
	    	
	    	$.myAjax({
	      		url: $.getService("ui","updatePaymode?"+$.cookGetUrlParam(params)),			
	      		contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) { 
	     		   if(data.code == "0"){
	     			  $.myAlert('修改成功');
	     			 search(0);
	     			}else{
	    				$.myAlert(data.msg,'信息');
	    			}
	     	    },
	        });
    	  
      },
      onCancel: function(e) {
    	
      }
    });
    
}

function dataDelete(payid){
	params.payid = payid;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deletePaymode"),
			data:JSON.stringify(params),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myAlert("删除成功");
				search(0);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}});
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