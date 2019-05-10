var params = {};
var pagesize=5;

$(document).ready(function(){
	getCount();
$("#btnSearch").click(function(event){
		search(0);
	});
});

var getCount = function(){
	
	$.myAjax({
		url:$.getService("ui","getCatetaxCount?mode=test"),
		contentType:"application/json;charset=UTF-8",
		data:JSON.stringify(params),
		success: function (data) {
			if(data.code!=0){
				$.myAlert(data.message);
				return;
			}
			if(data.data.count==0){
				$.myAlert("没有该企业纳税号信息");
				return;
			}
			
			$.myPage(page,{rows:data.data.count,pagesize:pagesize,jump:function(page){
				search(page);
			}});
			search(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
};


function search(page){	
	var cateid=$("#cateId").val();
	var catename=$("#cateName").val();
	params.cateid=cateid;
	params.catename=catename;
	params.page=page;
	params.pagesize = pagesize;
	
	 $.myAjax({
			url: $.getService("ui","queryCatetax?mode=test"),			
			contentType:"application/json;charset=UTF-8",
			data:JSON.stringify(params),
		    success: function (data) { 

			   if(data.code == "0"){
				   //首次查询page
				$.myPage(page,{rows:data.count,pagesize:pagesize,jump:function(page){
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
	    	  params.taxrate = $("#taxrate").val();
	    	  params.taxitemid = $("#taxitemid").val();
		   if(params.cateid==""){
			   $.myAlert('类别编码不能为空');
		    	return false;
		    }

	        $.myAjax({
	    		url: $.getService("ui","insertCatetax?mode=test"),			
	    		contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) { 
	     		   if(data.code == "0"){
	     			  $.myAlert('新增成功');
	     				$.myRowHTML(data.data,"rowSample","rowTraget");
	     			}else{
	    				$.myAlert(data.msg,'信息');
	    			}
	     	    },
	        });
	        
	      },
	      onCancel: function(e) {
		    	$("#cateid").val("");
		    	$("#catename").val("");
		    	$("#taxrate").val("");
		    	$("#taxitemid").val("");
         }
	    });
	 });
});


function dataEdit(cateid) {

	params.cateid=cateid;
	 $.myAjax({
 		url: $.getService("ui","getCatetaxByNo?mode=test"),			
 		contentType:"application/json;charset=UTF-8",
	    data: JSON.stringify(params),
	    success: function (data) { 
		$("#cateid2").val(data.data.cateid);
		$("#catename2").val(data.data.catename);
		$("#taxrate2").val(data.data.taxrate);
		$("#taxitemid2").val(data.data.taxitemid);
	    },
 });

    $('#my-edit').modal({
      relatedTarget: this,
      onConfirm: function(e) {
    	  params.cateid = $("#cateid2").val();
    	  params.catename = $("#catename2").val();
    	  params.taxrate = $("#taxrate2").val();
    	  params.taxitemid = $("#taxitemid2").val();
    	  if(params.cateid==""){
			   $.myAlert('类别编码不能为空');
		    	return false;
		    }
		    
	        $.myAjax({
	     		url: $.getService("ui","updateCatetax?mode=test"),			
	     		contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) { 
	     		   if(data.code == "0"){
	     			  $.myAlert('修改成功');
	     			  $.myRowHTML(data.data,"rowSample","rowTraget");
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

function dataDelete(index){
$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
	$.myPrompt("删除需要输入授权码","",function(e){
		$.myAlert("授权码："+e.data+" 校验失败");
	});
}});
}