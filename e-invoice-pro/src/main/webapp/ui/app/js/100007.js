var params = {};
var pagesize=10;
var channel = "app";
params.channel = channel;
$(document).ready(function(){
	search(0);
    $("#btnSearch").click(function(event){
		search(0);
	});

});

function search(page){
	params.goodsid = $("#goodsId").val();
	params.goodsname = $("#goodsName").val();
	params.page=page;
	params.pagesize = pagesize;
	
		 $.myAjax({
		  		url: $.getService("ui","queryGoodsdis?"+$.cookGetUrlParam(params)),			
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
	    	  params.goodsid = $("#goodsid").val();
	    	  params.goodsname = $("#goodsname").val();
	    	  params.pkname = $("#pkname").val();
		    if(params.goodsid==""){
		    	$.myAlert('商品编码不能为空');
		    	return false;
		    }
		    if(params.goodsname==""){
		    	$.myAlert('商品名称不能为空');
		    	return false;
		    }
		    
	        $.myAjax({
	      		url: $.getService("ui","insertGoodsdis?"+$.cookGetUrlParam(params)),			
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
		    	$("#goodsid").val("");
		    	$("#goodsname").val("");
		    	$("#pkname").val("");
       }
	    });
	 });
});


function dataEdit(goodsid) {
	params.goodsid=goodsid;
	
	$.myAjax({
  		 url: $.getService("ui","getGoodsdisById?"+$.cookGetUrlParam(params)),			
  		 contentType:"application/json;charset=UTF-8",
		 data:JSON.stringify(params),
		 success: function (data) { 
			$("#goodsid2").val(data.data.goodsid);
			$("#goodsname2").val(data.data.goodsname);
			$("#pkname2").val(data.data.pkname);
		    },
	 });

	    $('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.goodsid = $("#goodsid2").val();
	    	  params.goodsname = $("#goodsname2").val();
	    	  params.pkname = $("#pkname2").val();
	    	   if(params.goodsid==""){
			    	$.myAlert('商品编码不能为空');
			    	return false;
			    }
			    if(params.goodsname==""){
			    	$.myAlert('商品名称不能为空');
			    	return false;
			    }
			    
		        $.myAjax({
		     		url: $.getService("ui","updateGoodsdis?"+$.cookGetUrlParam(params)),			
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
			url:$.getService("ui","deleteGoodsdis"),
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

function checkdouble(){
	var param={};
	param.goodsid=$("#goodsid").val();
	$.myAjax({
		url:$.getService("ui","getGoodsdisById"),
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