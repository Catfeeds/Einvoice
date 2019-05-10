var myvalue= new myapp();


function regite_company(m)
{					
	value={};		
	value.companyname = $('#companyname').val();
	value.register_name = $('#register_name').val();
	value.register_phone = $('#register_phone').val();
	value.register_email = $('#register_email').val();
	value.login_name = $('#login_name').val();
	value.login_password = $('#login_password').val();
	
	myvalue.ajax(
			{			
				url: "/efs-portal/rest/ui/module/registerCompanySelf.action?",
				type: "post",		
				data: JSON.stringify(value),
				success: function (text) {  
					var data=JSON.parse(text);					
					if (data.code=="1") {		
						alert("新增数据成功!");	
					} else {
						alert("新增数据失败!<br>失败原因:"+data.msg);
					}
				},        
				error: function (jqXHR, textStatus, errorThrown) {
					alert("失败:  网络中断或接口异常!");
				}					
			}
	);	
}
