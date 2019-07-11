var channel = "app";
var pagesize=5;
var params = {};

$(document).ready(function(){
	search(0);
   $("#btnSearch").click(function(event){
		search(0);
	});
});


function search(page){
	params.taxitemid=$("#taxitemidfind").val();
	params.goodsName=$("#goodsNamefind").val();
	params.page=page;
	params.pagesize = pagesize;
    
	   $.myAjax({
		   url: $.getService("ui","queryBillTax"),
		   data:JSON.stringify(params),
		   contentType:"application/json;charset=UTF-8",					
		   success: function (data) { 

			   if(data.code == "0"){ 	   
				 //首次查询page
					$.myPage(page,{rows:data.count,pagesize:pagesize,jump:function(page){
						search(page);
					}});
				   
					$.myRowHTML(data.data,"rowSample","rowTraget");
				}
			   if(data.count==0){
					$.myAlert("没有数据信息");
					return;
				}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	  });	

	
}


$(function() {
	  $('#btnNew').on('click', function() {

	    $('#my-add').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.taxitemid = $("#taxitemid").val();
	    	  params.taxitemname = $("#taxitemname").val();
	    	  params.goodsName = $("#goodsName").val();
	    	  params.taxRate = $("#taxRate").val();

                dataAdd();
	      },
	      onCancel: function(e) {
    	        $("#taxitemid").val("");
		    	$("#taxitemname").val("");
		    	$("#goodsName").val("");
		    	$("#taxRate").val("");
           }
	    });
	  });
	  $.mySelect({
			tragetId:"selectA",
			id:"arithmetic",
			url:$.getService("ui","getLookupSelect?lookupid=100&"+$.cookGetUrlParam(params)),
			onChange:function(e){
				//console.log(e);
			}
		});
	});


function dataAdd(){

    $.myAjax({
 	    url: $.getService("ui","insertBillTax"),		
 	    contentType:"application/json;charset=UTF-8",
 	    data: JSON.stringify(params),
 	    success: function (data) { 

 		   if(data.code == "0"){
 			    $.myAlert("新增成功");
 				search(0);
 			}	
 	    },
 	   error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
    });
	
}


function dataEdit(taxitemid,goodsName,taxRate,taxitemname) {
	params.taxitemid=taxitemid;
	params.goodsName=goodsName;
	params.taxRate=taxRate;
	params.taxitemname=taxitemname;
	$.mySelect({
		tragetId:"selectB",
		id:"arithmetic2",
		url:$.getService("ui","getLookupSelect?lookupid=100&"+$.cookGetUrlParam(params)),
		onChange:function(e){
			//console.log(e);
		}
	});
	
	$.myAjax({
 	    url: $.getService("ui","getBillTaxById?"+$.cookGetUrlParam(params)),		
 	    contentType:"application/json;charset=UTF-8",
  	    data:JSON.stringify(params),
  	    success: function (data) { 

  		$("#taxitemid2").val(data.data.taxitemid);
  		$("#taxitemname2").val(data.data.taxitemname);
  		$("#goodsName2").val(data.data.goodsName);
  		$("#taxRate2").val(data.data.taxRate);
  		$("#arithmetic2").val(data.data.arithmetic);
  		$('select').trigger('changed.selected.amui');
  	    },
     });
	
	    $('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.taxitemid = $("#taxitemid2").val();
	    	  params.taxitemname = $("#taxitemname2").val();
	    	  params.goodsName = $("#goodsName2").val();
	    	  params.taxRate = $("#taxRate2").val();
	    	
		        
		        $.myAjax({
		     	    url: $.getService("ui","updateBillTax"),		
		     	    contentType:"application/json;charset=UTF-8",
		     	    data: JSON.stringify(params),
		     	    success: function (data) { 
		     		   if(data.code == "0"){
		     			  $.myAlert("修改成功");
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

function dataDelete(taxitemid,goodsName,taxRate,taxitemname){
	params.taxitemid=taxitemid;
	params.goodsName=goodsName;
	params.taxRate=taxRate;
	params.taxitemname=taxitemname;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deleteBillTaxByid"),
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

