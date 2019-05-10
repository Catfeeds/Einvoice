var channel = "app";
var pagesize=10;
var params = {};
params.channel = channel;
$(document).ready(function(){
	search(0);
    $("#btnSearch").click(function(event){
		search(0);
	});
    $.mySelect({
		tragetId:"selectA",
		id:"taxmode",
		url:$.getService("ui","getLookupSelect?lookupid=3&"+$.cookGetUrlParam(params)),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectC",
		id:"itfeqpttype",
		url:$.getService("ui","getLookupSelect?lookupid=8&"+$.cookGetUrlParam(params)),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectB",
		id:"taxmode2",
		url:$.getService("ui","getLookupSelect?lookupid=3&"+$.cookGetUrlParam(params)),
		onChange:function(e){
		}
	});
  $.mySelect({
		tragetId:"selectD",
		id:"itfeqpttype2",
		url:$.getService("ui","getLookupSelect?lookupid=3&"+$.cookGetUrlParam(params)),
		onChange:function(e){
		}
	});
});


function search(page){
	var taxno=$("#TaxNo").val();
	var taxname=$("#TaxName").val();
	params.taxno=taxno;
	params.taxname=taxname;
	params.page=page;
	params.pagesize = pagesize;
	
    	$.myAjax({
 		   url: $.getService("ui","queryTaxinfo?"+$.cookGetUrlParam(params)),			
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
				$.myAlert("没有该企业纳税号信息");
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

	    	  params.taxno = $("#taxno").val();
	    	  params.taxname = $("#taxname").val();
	    	  params.taxadd = $("#taxadd").val();
	    	  params.taxbank = $("#taxbank").val();
	    	  params.taxperson = $("#taxperson").val();
	    	  params.taxmode = $("#taxmode").val();
	    	  params.itfkpd = $("#itfkpd").val();
	    	  params.itfeqpttype = $("#itfeqpttype").val();
	    	  params.itfskpbh = $("#itfskpbh").val();
	    	  params.itfskpkl = $("#itfskpkl").val();
	    	  params.itfkeypwd = $("#itfkeypwd").val();
	    	  params.itfbbh = $("#itfbbh").val();
	    	  params.itfserviceurl = $("#itfserviceurl").val();
	    	  params.itfjrdm = $("#itfjrdm").val();
	    	       
		    if(params.taxno==""){
		    	$.myAlert("纳税人识别号不能为空！");
		    	return false;
		    }
		    if(params.itfjrdm==""){
		    	$.myAlert("接入代码不能为空！");
		    	return false;
		    }
	        
	        $.myAjax({
	 		   url: $.getService("ui","insertTaxinfo?"+$.cookGetUrlParam(params)),			
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
		    	$("#taxno").val("");
		    	$("#taxname").val("");
		    	$("#taxadd").val("");
		    	$("#taxbank").val("");
		    	$("#taxperson").val("");
		    	$("#taxmode").val("");
		    	$("#itfkpd").val("");
		    	$("#itfeqpttype").val("");
		    	$("#itfskpbh").val("");
		    	$("#itfskpkl").val("");
		    	$("#itfkeypwd").val("");
		    	$("#itfbbh").val("");
		    	$("#itfserviceurl").val("");
		    	$("#itfjrdm").val("");
         }
	    });
	  });
	 
	});

function dataEdit(taxno) {	
	params.taxno=taxno;
	
	$.myAjax({
		url: $.getService("ui","getTaxinfoByNo?"+$.cookGetUrlParam(params)),			
		contentType:"application/json;charset=UTF-8",
	    data:JSON.stringify(params),
	    success: function (data) { 

		$("#taxno2").val(data.data.taxno);
		$("#taxname2").val(data.data.taxname);
		$("#taxadd2").val(data.data.taxadd);
		$("#taxbank2").val(data.data.taxbank);
		$("#taxperson2").val(data.data.taxperson);
		$("#taxmode2").val(data.data.taxmode);
		$("#itfkpd2").val(data.data.itfkpd);
		$("#itfeqpttype2").val(data.data.itfeqpttype);
		$("#itfskpbh2").val(data.data.itfskpbh);
		$("#itfskpkl2").val(data.data.itfskpkl);
		$("#itfkeypwd2").val(data.data.itfkeypwd);
		$("#itfbbh2").val(data.data.itfbbh);
		$("#itfserviceurl2").val(data.data.itfserviceUrl);
		$("#itfjrdm2").val(data.data.itfjrdm);
		$('select').trigger('changed.selected.amui');
	    },
 });

    $('#my-edit').modal({
      relatedTarget: this,
      onConfirm: function(e) {

    	  params.taxno = $("#taxno2").val();
    	  params.taxname = $("#taxname2").val();
    	  params.taxadd = $("#taxadd2").val();
    	  params.taxbank = $("#taxbank2").val();
    	  params.taxperson = $("#taxperson2").val(); 
    	  params.taxmode =  $("#taxmode2").val();
    	  params.itfkpd = $("#itfkpd2").val();
    	  params.itfeqpttype = $("#itfeqpttype2").val();
    	  params.itfskpbh = $("#itfskpbh2").val();
    	  params.itfskpkl = $("#itfskpkl2").val();
    	  params.itfkeypwd = $("#itfkeypwd2").val();
    	  params.itfbbh = $("#itfbbh2").val();
    	  params.itfserviceurl = $("#itfserviceurl2").val();
    	  params.itfjrdm = $("#itfjrdm2").val();
    	  
    	  if(params.taxno==""){
		    	$.myAlert("纳税人识别号不能为空！");
		    	return false;
		    }
    	  if(params.itfjrdm==""){
		    	$.myAlert("接入代码不能为空！");
		    	return false;
		    }
	    	$.myAjax({
	    		url: $.getService("ui","updateTaxinfo?"+$.cookGetUrlParam(params)),			
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

function dataDelete(taxno){
	params.taxno = taxno;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","deleteTaxinfo"),
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

