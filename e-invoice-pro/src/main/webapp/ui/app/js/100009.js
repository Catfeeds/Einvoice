var params = {};
var pagesize=10;
var channel = "app";
params.channel = channel;
$(document).ready(function(){
	search(0);
$("#btnSearch").click(function(event){
		search(0);
	});

$.mySelect({
	tragetId:"selectA",
	id:"paystatus",
	url:$.getService("ui","getLookupSelect?lookupid=5&"+$.cookGetUrlParam(params)),
	onChange:function(e){
		//console.log(e);
	}
});
$.mySelect({
	tragetId:"selectB",
	id:"paystatus2",
	url:$.getService("ui","getLookupSelect?lookupid=5&"+$.cookGetUrlParam(params)),
	onChange:function(e){
		//console.log(e);
	}
});

});

function search(page){
	params.page=page;
	params.pagesize = pagesize;
	params.payid = $("#payId").val();
	params.payname = $("#payName").val();

		 $.myAjax({
		  		url: $.getService("ui","queryPaymode?"+$.cookGetUrlParam(params)),			
		  		contentType:"application/json;charset=UTF-8",
		  		data:JSON.stringify(params),
			    success: function (data) { 
				
				   if(data.code == "0"){
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
