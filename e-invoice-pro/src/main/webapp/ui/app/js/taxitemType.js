var channel = "app";
var data1=[];
var categoryid=0;
var params = {};
params.channel = channel;
//查询类目树
function search(headcatid){
	params.headcatid = headcatid;//父id
	$.myAjax({
		url:$.getService("ui","queryCategory"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			data1= dataJson.data;
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
//查询类目与税目关系
function searchCatetax(cateid){
	params.cateid = cateid;//父id
	$.myAjax({
		url:$.getService("ui","getCatetaxById"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myRowHTML(dataJson.data,"catetax","catetaxlist");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
//税目查询
function searchAll(page){
	params.page=page;
	var taxitemname = $("#requestname").val();
	params.taxitemname = taxitemname;
	$.myAjax({
		url:$.getService("ui","queryTaxitem"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myRowHTML(dataJson.data,"rowmodel","searchRows");
			$.myPage(page,{rows:dataJson.count,jump:function(page){
				searchAll(page);
			}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
$(document).ready(function(){
	 initTree();
//	 var upload=$('#event').AmazeuiUpload({
//		 url : $.getService("ui","uploadCatetax")+"?token="+$.getUrlParam("token"),
//		 dropType: false, //是否允许拖拽 
//		 pasteType: false //是否允许粘贴 
//		 }); 
	 //upload.destory(); //对象销毁
	// upload.setResult(); //置入已上传的对象
	// upload.selectResult(); //获取当前已经完成上传的对象
});

function initTree(){
	$('#firstTree').tree({
	    dataSource: function(options, callback) {
	    	if(options.categoryid == null){
	    		search(0);
	    	}else{
	    		search(options.categoryid);
	    	}
	      setTimeout(function() {
	        callback({
	          data:data1
	        });
	      }, 800);
	    },
	    folderSelect: true,
	    itemIcon: 'am-icon-file'
	  }).on('loaded.tree.amui', function(e) {
	  }).on('selected.tree.amui', function(e, selected) {
		  searchCatetax(selected.target.categoryid);
	  }).on('disclosedVisible.tree.amui', function(e, selected) {
	  }).on('disclosedFolder.tree.amui', function (e, info) {
	  }).on('closed.tree.amui', function(e, info) {
	  });
}

function addtaxitem(e){
	var selectrow;
	if($(e).prop("checked")){
		 selectrow = $(e);
		var selectTreevalue=$('#firstTree').tree('selectedItems');
		if(selectTreevalue == null || selectTreevalue == ''){
			alert("请选择类别树");
			$(e).uCheck('uncheck');
			return;
		}
		//类别id
		params.cateid = selectTreevalue[0].categoryid;
		$.myAjax({
			url:$.getService("ui","getCatetaxById"),
			data:JSON.stringify(params),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data != '' && dataJson.data.length > 0){
					$.myAlert('已经存在对应税目');
					return;
				}else{
					//不存在对应关系
					insertCatetax(selectTreevalue[0],selectrow);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		
	}else{
	}
}

function dataDelete(cateid){
	params.cateid = cateid;
	$.myAjax({
		url:$.getService("ui","deleteCatetaxByid"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myAlert('删除成功');
			searchCatetax(cateid);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
			
 }

function insertCatetax(selectTreevalue,selectrow){
	params.cateid = selectTreevalue.categoryid;
	params.catename = selectTreevalue.categoryname;
	params.taxrate = selectrow[0].value.split(",")[1];
	params.taxitemid =selectrow[0].value.split(",")[0];
	$.myAjax({
		url:$.getService("ui","insertCatetax"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			searchCatetax(selectTreevalue.categoryid);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
			
 }
