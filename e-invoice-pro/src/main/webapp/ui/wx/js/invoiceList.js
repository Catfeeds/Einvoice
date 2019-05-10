var pagesize=5;

$(document).ready(function(){
	//读取分页信息
	getCount();
});

var getCount = function(){
	var params = {};
	params.channel = "wx";
	
	$.myAjax({
		url:$.getService("api","getInvqueCount?"+$.cookGetUrlParam(params)),
		data:JSON.stringify(params),
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.data.count==0){
				$.myAlert("没有您的申请发票记录");
				return;
			}
			
			//根据屏幕高度设定pagesize
			
			$.myPage(page,{rows:dataJson.data.count,pagesize:pagesize,jump:function(page){
				search(page);
			}});
			search(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
};

var search = function(page){
	var params = {};
	params.channel = "wx";
	params.page=page;
	params.pagesize = pagesize;
	
	$.myAjax({
		url:$.getService("api","getInvque?"+$.cookGetUrlParam(params)),
		data:JSON.stringify(params),
		contentType:"application/json;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.each( dataJson.data, function(i, n){
				n.status = n.iqstatus>=40?"已开票":"开票中";
				n.iqdate = TimeObjectUtil.formatterDate(new Date(n.iqdate));
				dataJson.data[i].panel = n.iqstatus>=40?"am-panel-success":"am-panel-warning";
			});
			$("#rowList").html("");
			$.myRowHTML(dataJson.data,"tmpRowList","rowList",0);
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
};
