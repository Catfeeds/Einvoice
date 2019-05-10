
$(document).ready(function(){
	
});

function clearkehu(){
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadDzdh").val('');//地址电话
	$("#invoiceHeadYhzh").val('');//银行账户
	$("#invRecvMail").val('');//发票接收人邮箱
}
//获取用户初始化信息电话邮箱抬头等
function getCustomerInfo(){
	var data={};
	data.bz=1;
	data.taxname=$("#invoiceHeadEntText").val();
	if(!data.taxname){
		clearkehu();
	}
	if(data.taxname != ''){
		$( "#invoiceHeadEntText" ).autocomplete({
			 source: function( request, response ) {
				 $.myAjax({
						url:$.getService("api","getWebCustomerInfo"),
						data:JSON.stringify(data),
						contentType:"application/json;charset=UTF-8",
						success: function (dataJson) {
							if(dataJson.code==0 && dataJson.data){
								var myArray=new Array();
								for(var i=0;i<dataJson.data.length;i++){
									myArray[i]=dataJson.data[i].taxname;
								}
								 response($.map(dataJson.data,function(item){  
			                            var name = item.name;  
			                            var id = item.id;  
			                            return {  
			                                label:item.taxname,//下拉框显示值  
			                                value:item.taxname,//选中后，填充到下拉框的值  
			                                taxno:item.taxno,
			                                taxaddr:item.taxaddr,
			                                taxbank:item.taxbank,
			                                email:item.email
			                            }  
			                        }));  
							}
						},
						error: function (jqXHR, textStatus, errorThrown) {
							$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
						}
					});
				 
			 },
			 select:function changesel(event, ui ){
				 changeCustomerInfo(ui.item);
			 }
		    });
		
	}
	
}

function changeCustomerInfo(data){
//			$("#invoiceHeadEntText").val(data.taxname);
			$("#invoiceHeadEntTaxno").val(data.taxno);
			$("#invoiceHeadDzdh").val(data.taxaddr);
			$("#invoiceHeadYhzh").val(data.taxbank);
			$("#invRecvMail").val(data.email);
		}


function updateCustomerInfo(){
	var data={};
	data.taxname = $("#invoiceHeadEntText").val();
	data.taxno = $("#invoiceHeadEntTaxno").val();
	data.taxaddr = $("#invoiceHeadDzdh").val();
	data.taxbank = $("#invoiceHeadYhzh").val();
	data.email = $("#invRecvMail").val();
	data.bz=1;
	var postData =JSON.stringify(data);
	$.myAjax({
		url:$.getService("api","updateCustomerInfo"),
		data:postData,
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
		},
		error: function (jqXHR, textStatus, errorThrown) {
		}
	});
}

