var pagesize=10;
var params = {};

$(document).ready(function(){
	
   $("#btnSearch").click(function(event){
		search(0);
	});
   $.mySelect({
		tragetId:"selectA",
		id:"taxno",
		url:$.getService("ui","getTaxnoSelect?"),
		onChange:function(e){
			//console.log(e);
		}
	});
   
});


function search(page){
	
	params.startDate=$("#startDate").val().replace(/\-/g, "")+'000000';
	params.endDate=$("#endDate").val().replace(/\-/g, "")+'000000';
	params.taxno=$("#taxno").val();
	params.page=page;
	params.pagesize = pagesize;

	   $.myAjax({
		   url: $.getService("ui","gettaxdata?"),
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
					$.myAlert("没有该信息");
					return;
				}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	  });	

}
