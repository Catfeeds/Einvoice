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
//var upload=$('#event').AmazeuiUpload({
//	 url : $.getService("ui","uploadGoodstax?"), 
//	 dropType: false, //是否允许拖拽 
//	 pasteType: false //是否允许粘贴 
//	 }); 
//upload.destory(); //对象销毁
//upload.setResult(); //置入已上传的对象
//upload.selectResult(); //获取当前已经完成上传的对象
$.mySelect({
	tragetId:"selectC",
	id:"zerotax",
	url:$.getService("ui","getLookupSelect?lookupid=10&"+$.cookGetUrlParam(params)),
	onChange:function(e){
		//console.log(e);
	}
});
$.mySelect({
	tragetId:"selectD",
	id:"taxpre2",
	url:$.getService("ui","getLookupSelect?lookupid=9&"+$.cookGetUrlParam(params)),
	onChange:function(e){
		//console.log(e);
	}
});
$.mySelect({
	tragetId:"selectE",
	id:"zerotax2",
	url:$.getService("ui","getLookupSelect?lookupid=10&"+$.cookGetUrlParam(params)),
	dataArray:"",
	onChange:function(e){
		//console.log(e);
	}
});

});

function search(page){
	params.goodsid = $("#goodsId").val();
	params.goodsname = $("#goodsName").val();
	params.loginid = $("#loginid").val();
	params.startdate=$("#startdate").val();
	params.enddate=$("#enddate").val();
	params.page=page;
	params.pagesize = pagesize;
	
		$.myAjax({
	  		url: $.getService("ui","queryGoodstaxlog?"+$.cookGetUrlParam(params)),			
	  		contentType:"application/json;charset=UTF-8",
	  		data:JSON.stringify(params),
		    success: function (data) { 
			   if(data.code == "0"){
					for (var i=0;i<data.data.length;i++){
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
	    	  params.goodsid = $("#goodsid").val();
	    	  params.goodsname = $("#goodsname").val();
	    	  params.taxitemid = $("#taxitemid").val();
	    	  params.taxpre = $("#taxpre").val();
	    	  params.taxprecon = $("#taxprecon").val();
	    	  params.zerotax = $("#zerotax").val();
	    	  if(params.goodsid==""){
				   $.myAlert('商品编码不能为空');
			    	return false;
			    }
			   if(params.goodsname==""){
				   $.myAlert('商品名称不能为空');
			    	return false;
			    }
			   if(params.taxitemid==""){
				   $.myAlert('税目代码不能为空');
			    	return false;
			    }
	        $.myAjax({
	      		url: $.getService("ui","insertGoodstax?"+$.cookGetUrlParam(params)),			
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
	    	  
     }
	    });
	 });
	 
});


function dataEdit(goodsid) {
	params.goodsid=goodsid;
	
	 $.myAjax({
   		url: $.getService("ui","getGoodstaxById?"+$.cookGetUrlParam(params)),			
   		contentType:"application/json;charset=UTF-8",
		data: JSON.stringify(params),
		success: function (data) { 
			$("#goodsid2").val(data.data.goodsid);
			$("#goodsname2").val(data.data.goodsname);
			$("#taxitemid2").val(data.data.taxitemid);
			getTaxitemById($("#taxitemid2").val(),"taxitemname2");
			$("#taxpre2").val(data.data.taxpre);
			$("#taxprecon2").val(data.data.taxprecon);
			$("#zerotax2").val(data.data.zerotax);
			$('select').trigger('changed.selected.amui');
		    },
	 });
	 
	 

	    $('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.goodsid = $("#goodsid2").val();
	    	  params.goodsname = $("#goodsname2").val();
	    	  params.taxitemid = $("#taxitemid2").val();
	    	  params.taxpre = $("#taxpre2").val();
	    	  params.taxprecon = $("#taxprecon2").val();
	    	  params.zerotax = $("#zerotax2").val();
			   if(params.goodsid==""){
				   $.myAlert('商品编码不能为空');
			    	return false;
			    }
			   if(params.goodsname==""){
				   $.myAlert('商品名称不能为空');
			    	return false;
			    }
			   if(params.taxitemid==""){
				   $.myAlert('税目代码不能为空');
			    	return false;
			    }

		        $.myAjax({
		       		url: $.getService("ui","updateGoodstax?"+$.cookGetUrlParam(params)),			
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

	function dataDelete(goodsid){
	params.goodsid = goodsid;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deleteGoodstax"),
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
	
	function taxsearch(e){
		$('#taxmodal').modal({width:'800',height:'500'});
		searchtax(0);
	}
	function searchtax(page){
		var params = {};
		params.channel = channel;
		params.page=page;
		params.pagesize=8;
		var taxitemname = $("#requestname").val();
		var taxitemid = $("#requestid").val();
		params.taxitemname = taxitemname;
		params.taxitemid = taxitemid;
		$.myAjax({
			url:$.getService("ui","queryTaxitem"),
			data:JSON.stringify(params),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myRowHTML(dataJson.data,"rowmodel","searchRows");
				$.myPage(page,{rows:dataJson.count,pagesize:8,targetId:"page1",jump:function(page){
					searchtax(page);
				}});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}
	function instax(){
		var selectrow = $("input[type='checkbox']:checked");
		if(selectrow == null || selectrow == ''){
			return;
		}
		if(selectrow != null && selectrow.length >1){
			$.myAlert("只能选择一条税目");
			 return;
		}
		var taxitemid = selectrow[0].value.split(",")[0];
		var taxite = selectrow[0].value.split(",")[1];
		var taxitemname = selectrow[0].value.split(",")[2];
		$('#taxitemid').val(taxitemid);
		$('#taxitemid2').val(taxitemid);
		$('#taxitemname').val(taxitemname);
		$('#taxitemname2').val(taxitemname);
		$('#taxmodal').modal('close');
	}
	
	function searchcatetax(){
		var pam={};
		pam.cateid=$('#goodscate').val();
		$.myAjax({
			url:$.getService("ui","getCatetaxById"),
			data:JSON.stringify(pam),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data != ''){
					$('#taxitemid').val(dataJson.data[0].taxitemid);
					getTaxitemById($("#taxitemid").val(),"taxitemname");
				}
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		
	}
	
	function searchcatetax2(){
		var pam={};
		pam.cateid=$('#goodscate2').val();
		$.myAjax({
			url:$.getService("ui","getCatetaxById"),
			data:JSON.stringify(pam),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data != ''){
					$('#taxitemid2').val(dataJson.data[0].taxitemid);
					getTaxitemById($("#taxitemid2").val(),"taxitemname2");
				}
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		
	}
	
	function seachtatemByid(){
		getTaxitemById($("#taxitemid").val(),"taxitemname");
	}
	
	function seachtatemByid2(){
		getTaxitemById($("#taxitemid2").val(),"taxitemname2");
	}
	
	function getTaxitemById(taxitemid,input){
		var param={};
		param.taxitemid=taxitemid;
		$.myAjax({
			url:$.getService("ui","getTaxitemById"),
			data:JSON.stringify(param),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data != '' && dataJson.data != null){
					$('#'+input).val(dataJson.data[0].taxitemname);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}
	
	function checkdouble(){
		var param={};
		param.goodsid=$("#goodsid").val();
		$.myAjax({
			url:$.getService("ui","getGoodstaxById"),
			data:JSON.stringify(param),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data != '' && dataJson.data != null){
					$.myAlert("系统内已存在，请重新输入");
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		
	}
	
	function changeTaxpre(value){
		if(value==0){
			$("#zerotax").val("");
			$("#zerotax2").val("");
			$("#zerotax").trigger('changed.selected.amui');
		}
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
	