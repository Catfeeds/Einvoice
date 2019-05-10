var curData;
var curPage;

$(document).ready(function(){
	
	//初始化下拉数据
	$.mySelect({
		tragetId:"selectA",
		id:"inputFlag",
		//url:$.getService("portal","main/getShopList.action?")+"companyid=2",
		dataArray:[{name:"无：无：无",value:"000"},{name:"无：无：时间",value:"001"},{name:"无：门店：无",value:"010"},
				   {name:"无：门店：时间",value:"011"},{name:"税号：无：无",value:"100"},{name:"税号：无：时间",value:"101"},
				   {name:"税号：门店：无",value:"110"},{name:"税号：门店：时间",value:"111"}],
/*		onChange:function(e){
			console.log(e);
			$.myAlert(e.text() + " : "+e.val());
		}*/
	});
	
	//点击查询事件
	$("#btnSave").click(function(event){
		if(!$('#editForm').validator("isFormValid")) return;
		var params = $.myCookValue({name:"data-query"});

		if(params.entid==null||params.entid==undefined||params.entid==""){
			alert("企业ID不能为空！");
			return false;
		}
		else
		if( $("#inputFlag").val()==null|| $("#inputFlag").val()==undefined){
			alert("没有选择规则");
			return false;
		}else{
			if($("#inputFlag").val().substring(0,1)=="1"){
				if(params.taxnonum==null||params.taxnonum==undefined||params.taxnonum==""){
					alert("请填写税号数量！");
					return false;
				}
				
			}
			
			if($("#inputFlag").val().substring(1,2)=="1"){
				if(params.shopnum==null||params.shopnum==undefined||params.shopnum==""){
					alert("请填写门店数量！");
					return false;
				}
				
			}
			
			if($("#inputFlag").val().substring(2)=="1"){
				if(params.enddate==null||params.enddate==undefined||params.enddate==""){
					alert("请填写有效截止时间！");
					return false;
				}
				
			}
			
		}
		params.rule =$("#inputFlag").val();
		 
		$.myAjax({
			url:$.getService("portal","module/insertEnterpriseRegister.action"),
			
			data:{register:JSON.stringify(params)},
			
			success: function (dataJson) {
		 
				
				if(dataJson.code=="1"){
					$.myAlert("保存成功！");
					search();
				}else{
					$.myAlert(dataJson.msg);
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		
	});
	
	$("#entid").blur(function(event){
		
		 search();
	});
	
	

});

function search(){
	$("#taxnonum").attr("value","");
	$("#shopnum").attr("value","");
	$("#enddate").attr("value","");
	//$('#inputFlag').val('000');
	var  ss = document.getElementById('inputFlag');
	ss[0].selected = true;//选中
	$('#inputFlag').trigger('changed.selected.amui');	
	
	$.myAjax({
		url:$.getService("portal","module/findEnterprise.action"),
		data:{entid:$("#entid").val()},
		success: function (dataJson) {
			 
			if(dataJson.code=="1"){
				 var $selected = $('#inputFlag');
				 $selected.val(dataJson.data.rule);
				 $('#inputFlag').trigger('changed.selected.amui');	
				$("#taxnonum").attr("value",dataJson.data.taxnonum);
				$("#shopnum").attr("value",dataJson.data.shopnum);
				$("#enddate").attr("value",dataJson.data.enddate);
			}else{
				//$.myAlert(dataJson.msg);
			}
				
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
	


}

/*<div class="am-u-sm-6 am-u-lg-8">
<select id="inputFlag"  style="width:200px;">
<option value="000">无：无：无</option>
<option value="001">无：无：时间</option>
<option value="010">无：门店：无</option>
<option value="011">无：门店：时间</option>
<option value="100">税号：无：无</option>
<option value="101">税号：无：时间</option>
<option value="110">税号：门店：无</option>
<option value="111">税号：门店：时间</option>
</select>
</div>
*/

function dataDelete(index){
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myPrompt("删除需要输入授权码","",function(e){
			$.myAlert("授权码："+e.data+" 校验失败");
		});
	}});
}