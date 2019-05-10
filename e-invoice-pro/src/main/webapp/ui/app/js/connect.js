var user;
var channel = "app";
$(document).ready(function(){
	search(0);
});


function search(page){
	var data ={};
	data.page=page;
	data.requestmo=$("#requestmo").val();
		$.myAjax({
			url:$.getService("api","c_connect"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myPage(page,{rows:dataJson.count,jump:function(page){
					search(page);
				}});
				$.myRowHTML(dataJson.data,"rowmo","row1");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert(" status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}

var dataConnect;
function dataEdit(entid,connectid) {
	var params={};
	params.entid=entid;
	params.connectid=connectid;
	 $.myAjax({
   		url: $.getService("api","c_connect"),	
   		contentType:"application/json;charset=UTF-8",
		data: JSON.stringify(params),
		success: function (data) {
			dataConnect=data.data[0];
			$("#url").val(data.data[0].url);
			$("#username").val(data.data[0].username);
			$("#password").val(data.data[0].password);
			$("#driver").val(data.data[0].driver);
			$("#dbcharcode").val(data.data[0].dbcharcode);
			doEdit();
		    }
	 });
	   
	}

function doEdit() {
	$('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  dataConnect.url = $("#url").val();
	    	  dataConnect.username = $("#username").val();
	    	  dataConnect.password = $("#password").val();
	    	  dataConnect.driver = $("#driver").val();
	    	  dataConnect.dbcharcode = $("#dbcharcode").val();
			   if(dataConnect.url==""){
				   $.myAlert('链接串不能为空');
			    	return false;
			    }
			   if(dataConnect.username==""){
				   $.myAlert('用户名不能为空');
			    	return false;
			    }
			   if(dataConnect.password==""){
				   $.myAlert('密码不能为空');
			    	return false;
			    }
		        $.myAjax({
		       		url: $.getService("api","updateConnect"),			
		       		contentType:"application/json;charset=UTF-8",
		     	    data: JSON.stringify(dataConnect),
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

function datatest(sellurl,connectid){
	var pram={};
	pram.connectid=connectid;
	pram.sellurl=sellurl;
	 $.myAjax({
    		url: $.getService("api","test_connect"),			
    		contentType:"application/json;charset=UTF-8",
	  	    data: JSON.stringify(pram),
	  	    success: function (data) { 
  		    if(data.code == "0"){
//  		    	if(data.data.active==true){
//  		    		$.myAlert(data.data.lastMsg,'信息');
//  		    	}
  		    	$.myAlert(data.data.lastMsg,'信息');
  		    	 search(0);
  			}else{
 				$.myAlert(data.msg,'信息');
 			}
  	    },
     }); 
	 
}
