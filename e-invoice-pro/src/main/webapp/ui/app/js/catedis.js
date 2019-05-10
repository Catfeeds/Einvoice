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
	params.cateid = $("#quecateid").val();
	params.catename = $("#quecatename").val();
	params.page=page;
	params.pagesize = pagesize;
	
		 $.myAjax({
		  		url: $.getService("ui","queryCatedis?"+$.cookGetUrlParam(params)),			
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
	    	  params.cateid = $("#cateid").val();
	    	  params.catename = $("#catename").val();
		    if(params.cateid==""){
		    	$.myAlert('类别编码不能为空');
		    	return false;
		    }
		    
	        $.myAjax({
	      		url: $.getService("ui","insertCateDis?"+$.cookGetUrlParam(params)),			
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
		    	$("#id").val("");
		    	$("#catename").val("");
       }
	    });
	 });
});


function dataEdit(cateid) {
	params.cateid=cateid;
	
	$.myAjax({
  		 url: $.getService("ui","getGoodsdisById?"+$.cookGetUrlParam(params)),			
  		 contentType:"application/json;charset=UTF-8",
		 data:JSON.stringify(params),
		 success: function (data) { 
			$("#cateid2").val(data.data.cateid);
			$("#catename2").val(data.data.catename);
s		    },
	 });

	    $('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.cateid = $("#cateid2").val();
	    	  params.catename = $("#catename2").val();
	    	   if(params.goodsid==""){
			    	$.myAlert('类别编码不能为空');
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

function dataDelete(cateid){
	params.cateid = cateid;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deleteCateDisByid"),
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
	param.cateid=$("#cateid").val();
	$.myAjax({
		url:$.getService("ui","getCatedisById"),
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