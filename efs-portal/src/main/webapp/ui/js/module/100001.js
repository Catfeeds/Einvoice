var curData;
var curPage;

$(document).ready(function(){
	//点击查询事件
	$("#btnSearch").click(function(event){
		search(0);
	});
	
	//新增数据事件
	$("#btnNew").click(function(event){
		$.myEditModal({
			title:"新增",
			form:$("#editForm"),
			formData:null,
			formDataName:"data-edit",
			onConfirm:function(newData){
				newData.action = "add";
				save(newData);
			}
		});
	});
	
	//初始化下拉数据
	$.mySelect({
		tragetId:"selectA",
		id:"inputFlag",
		url:$.getService("portal","main/getShopList.action?")+"companyid=2",
		dataArray:[{name:"1自定义名称",value:" ",xxx:"其它自定义属性"},{name:"2自定义名称2",value:" ",xxx:"其它自定义属性"},{name:"3自定义名称3",value:" ",xxx:"其它自定义属性"}],
		onChange:function(e){
			console.log(e);
			$.myAlert(e.text() + " : "+e.val());
		}
	});
});

function save(data){
	var postData ={data:JSON.stringify(data)};
	$.myAjax({
		url:$.getService("portal","main/getServerDate.action"),
		data:postData,
		success: function (dataJson) {
			data.editdate = dataJson.nowday;
			data.checkdate = dataJson.nowday;
			if(data.action == "add"){
				$.myRowHTML([data],"rowSample","rowTraget",0);
			}
			$.myAlert("保存成功！");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function search(page){
	var params = $.myCookValue({name:"data-query",page:page});
	$.myAjax({
		url:$.getService("portal","main/getServerDate.action?")+ $.cookGetUrlParam(params),
		progress:true,
		success: function (dataJson) {
			var b = [];
			for ( var i = 1; i <= params.pageSize; i++) {
				var a = {};
				a.entId = i+10*((page==0?1:page)-1);
				a.entName = "我是企业："+(i+10*((page==0?1:page)-1));
				a.entTaxNo = Math.ceil(Math.random()+Math.random()*10+Math.random()*100+Math.random()*1000+Math.random()*10000);
				a.editdate = "2016-09-02";
				a.checkdate = "2017-04-13";
				b.push(a);
			}
			
			//首次查询page
			$.myPage(page,{jump:function(page){
				search(page);
			}});
			//刷新表格数据
			$.myRowHTML(b,"rowSample","rowTraget");
			curData = b;
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function dataEdit(index){
	var data = curData[index];
	console.log(data);
	$.myEditModal({
		title:"编辑",
		form:$("#editForm"),
		formData:data,
		formDataName:"data-edit",
		onConfirm:function(newData){
			save(newData);
		}
	});
}

function dataDelete(index){
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myPrompt("删除需要输入授权码","",function(e){
			$.myAlert("授权码："+e.data+" 校验失败");
		});
	}});
}