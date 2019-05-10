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
	params.entid=$("#entId").val();
	params.entname=$("#entName").val();
	params.page=page;
	params.pagesize = pagesize;
    
	   $.myAjax({
		   url: $.getService("ui","queryEnterprise?"+$.cookGetUrlParam(params)),
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
					$.myAlert("没有企业信息");
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
	    	  params.entid = $("#entid").val();
	    	  params.entname = $("#entname").val();
	    	  params.phone = $("#phone").val();
	    	  params.address = $("#address").val();
	    	  params.taxid = $("#taxid").val();
	    	  params.arithmetic = $("#arithmetic").val();
	    		var entid = $("#entid").val();
	    		var phone=$("#phone").val();
	    	  if(entid==""){
	    		  $.myAlert("企业id不能为空");
	    		  return ;
	    	  }
                dataAdd();
	      },
	      onCancel: function(e) {
    	        $("#entid").val("");
		    	$("#entname").val("");
		    	$("#phone").val("");
		    	$("#address").val("");
		    	$("#taxid").val("");
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
 	    url: $.getService("ui","addEnterprise?"+$.cookGetUrlParam(params)),		
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


function dataEdit(entid) {
	params.entid=entid;
	$.mySelect({
		tragetId:"selectB",
		id:"arithmetic2",
		url:$.getService("ui","getLookupSelect?lookupid=100&"+$.cookGetUrlParam(params)),
		onChange:function(e){
			//console.log(e);
		}
	});
	
	$.myAjax({
 	    url: $.getService("ui","getEnterpriseById?"+$.cookGetUrlParam(params)),		
 	    contentType:"application/json;charset=UTF-8",
  	    data:JSON.stringify(params),
  	    success: function (data) { 

  		$("#entid2").val(data.data.entid);
  		$("#entname2").val(data.data.entname);
  		$("#phone2").val(data.data.phone);
  		$("#address2").val(data.data.address);
  		$("#taxid2").val(data.data.taxid);
  		$("#arithmetic2").val(data.data.arithmetic);
  		$('select').trigger('changed.selected.amui');
  	    },
     });
	
	    $('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.entid = $("#entid2").val();
	    	  params.entname = $("#entname2").val();
	    	  params.phone = $("#phone2").val();
	    	  params.address = $("#address2").val();
	    	  params.taxid = $("#taxid2").val();
	    	  params.arithmetic = $("#arithmetic2").val();
	    	  var phone=$("#phone").val();
		        
		        $.myAjax({
		     	    url: $.getService("ui","updateEnterprise?"+$.cookGetUrlParam(params)),		
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

function dataDelete(entid){
	params.entid = entid;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deleteEnterprise"),
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

