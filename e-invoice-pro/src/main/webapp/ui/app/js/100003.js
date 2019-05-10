var channel = "app";
var pagesize=10;
var params = {};

$(document).ready(function(){
	search(0);
    $("#btnSearch").click(function(event){
		search(0);
	});
    $.mySelect({
		tragetId:"selectB",
		id:"shopstatus2",
		url:$.getService("ui","getLookupSelect?lookupid=6"),
		onChange:function(e){
		}
	});
    $.mySelect({
		tragetId:"selectD",
		id:"taxno2",
		url:$.getService("ui","getTaxnoSelect?"),
		onChange:function(e){
			//console.log(e);
		}
	});
    
    $.mySelect({
		tragetId:"selectA",
		id:"shopstatus",
		url:$.getService("ui","getLookupSelect?lookupid=6"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectC",
		id:"taxno",
		url:$.getService("ui","getTaxnoSelect?"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectE",
		id:"iseinvoice1",
		url:$.getService("ui","getLookupSelect?lookupid=12"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectF",
		id:"iseinvoice2",
		url:$.getService("ui","getLookupSelect?lookupid=12"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectG",
		id:"iseinvoice",
		url:$.getService("ui","getLookupSelect?lookupid=12"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"isopenallselectE",
		id:"isopenall1",
		url:$.getService("ui","getLookupSelect?lookupid=12"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"isopenallselectF",
		id:"isopenall2",
		url:$.getService("ui","getLookupSelect?lookupid=12"),
		onChange:function(e){
			//console.log(e);
		}
	});
});


function search(page){
	params.channel = channel;
	params.shopid=$("#shopId").val();
	params.shopname=$("#ShopName").val();
	params.iseinvoice=$("#iseinvoice").val();
	params.page=page;
	params.pagesize = pagesize;
	
		$.myAjax({
			url: $.getService("ui","queryShop"),			
			contentType:"application/json;charset=UTF-8",
			data:JSON.stringify(params),
		    success: function (data) { 
			
			   if(data.code == "0"){
				 //首次查询page
				$.myPage(page,{rows:data.count,pagesize:pagesize,jump:function(page){
						search(page);
					}});
			
				$.myRowHTML(data.data,"rowSample","rowTraget");
				}
			   if(data.count==0){
					$.myAlert("没有该企业机构信息");
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
	    	  params.shopid = $("#shopid").val();
	    	  params.shopname = $("#shopname").val();
	    	  params.shopstatus = $("#shopstatus").val();
	    	  params.shoptype = $("#shoptype").val();
	    	  params.taxno = $("#taxno").val();
	    	  params.manager = $("#manager").val();
	    	  params.telephone = $("#telephone").val();
	    	  params.iseinvoice = $("#iseinvoice1").val();  
	    	  params.isopenall = $("#isopenall1").val();
	    	  params.opentime = $("#opentimeE").val();
		        if(params.shopid==""){
		        	$.myAlert("机构编码不能为空！");
		    	return ;
		      }	
		        if(params.shopname==""){
		        	$.myAlert("机构名不能为空！");
		    	return ;
		      }	

	        
	        $.myAjax({
	    		url: $.getService("ui","insertShop"),			
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
		    	$("#shopid").val("");
		    	$("#shopname").val("");
		    	$("#shoptype").val("");
		    	$("#taxno").val("");
		    	$("#manager").val("");
		    	$("#telephone").val("");
		    	$("#opentime").val("");
       }
	    });
	  });
	  
	});

function dataEdit(shopid) {
	params.shopid=shopid;   
   
	
	$.myAjax({
		url: $.getService("ui","getShopById"),			
		contentType:"application/json;charset=UTF-8",
	    data: JSON.stringify(params),
	    success: function (data) { 
		$("#shopid2").val(data.data.shopid);
		$("#shopname2").val(data.data.shopname);
		$("#shopstatus2").val(data.data.shopstatus);
		$("#shoptype2").val(data.data.shoptype);
		$("#taxno2").val(data.data.taxno);
		$("#manager2").val(data.data.manager);
		$("#telephone2").val(data.data.telephone);
		$("#iseinvoice2").val(data.data.isEinvoice);
		$("#isopenall2").val(data.data.isopenall);
		$("#opentime2").val(data.data.opentime);
		$('select').trigger('changed.selected.amui');
	    }
 });

    $('#my-edit').modal({
      relatedTarget: this,
      onConfirm: function(e) {
    	  params.shopid = $("#shopid2").val();
    	  params.shopname = $("#shopname2").val();
    	  params.shopstatus = $("#shopstatus2").val();
    	  params.shoptype = $("#shoptype2").val();
    	  params.taxno = $("#taxno2").val();
    	  params.manager = $("#manager2").val();
    	  params.telephone = $("#telephone2").val();
    	  params.iseinvoice = $("#iseinvoice2").val();
    	  params.isopenall = $("#isopenall2").val();
    	  params.opentime = $("#opentime2").val();
    	  if(params.shopid==""){
	        	$.myAlert("机构编码不能为空！");
	    	return ;
	      }	
	        if(params.shopname==""){
	        	$.myAlert("机构名不能为空！");
	    	return ;
	      }	
    	  
	    	$.myAjax({
	    		url: $.getService("ui","updateShop?"+$.cookGetUrlParam(params)),			
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

function dataDelete(shopid){
	params.shopid = shopid;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deleteShop"),
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
