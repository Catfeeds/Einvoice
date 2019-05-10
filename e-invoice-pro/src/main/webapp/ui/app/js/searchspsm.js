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
	  		url: $.getService("ui","queryGoodstax"),			
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


