var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
var params = {};
params.channel = channel;
$(document).ready(function(){
	geren();
	add();
	//验证
	$('#div_info').validator();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
	
	$("#iqfplxdm").on('change', function() {
	    if(this.value=="026"){
	    	
	    }
	  });
});

var sea;
var selectTr;
function search(e){
	sea=$(e).parent().prev();
	selectTr=$(e).parents("tr");
	$('#modal').modal({width:'800',height:'500'});
	searchtax(0);
}
function searchtax(page){
	params.page=page;
	params.pagesize=8;
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
			$.myPage(page,{rows:dataJson.count,pagesize:8,jump:function(page){
				searchtax(page);
			}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function instax(){
	var selectrow = $("#taxtable input[name='taxitems']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条税目");
		 return;
	}
	var selectrowData = selectrow.val().split("-");
	var taxitemid = selectrowData[0];
	
	var taxiteminput = selectTr.find("input[name='taxitemid']");
	taxiteminput.val(taxitemid);
	
	var taxite = selectrowData[1].split(",");
	var taxiteinput = selectTr.find("select[name='taxrate']");
	taxiteinput.children("option").remove();
	for (var i = 0; i < taxite.length; i++) {
		taxiteinput.append("<option value='"+taxite[i]+"'>"+taxite[i]+"</option>");
	}
	$('#modal').modal('close');
}

function cal(e,type){
	var tr=$(e).parents("tr");
	var taxitemid = tr.children().eq(1).find('input').val();
	var taxrate = Number(tr.find("select[name='taxrate']").val());
	var amtInput = tr.find("input[name='amt']");
	var netamtInput = tr.find("input[name='netamt']");
	var taxamtInput = tr.find("input[name='taxamt']");
	var amt=0,netamt=0,taxamt = 0;
	if(taxitemid=='' || taxrate=='NaN'){
		amtInput.val('');
		netamtInput.val('');
		taxamtInput.val('');
		$.myAlert("请先选税目");
		return;
	}
	
	if(type==1){
		amt = decimal(Number(amtInput.val()),2);
		netamt = decimal(amt/(1+taxrate),2);
		taxamt = decimal(amt - netamt,2);
	}else{
		netamt = decimal(Number(netamtInput.val()),2);
		taxamt = decimal(netamt*taxrate,2);
		amt = decimal(Number(netamt+taxamt),2);
	}
	
	amtInput.val(amt);
	netamtInput.val(netamt);
	taxamtInput.val(taxamt);
	calsum();
}

function calsum(){
	var sumamt=0,sumtaxamt=0,sumnetamt=0;
	$("#table1").find("tr").each(function(i, n){
		if(i==0){return true;}
		if($(this).attr("id")=='tfoot') return true;
		
		var tdArr = $(this).children();
		sumamt += Number(tdArr.eq(6).find('input').val());
		sumnetamt += Number(tdArr.eq(7).find('input').val());
		sumtaxamt += Number(tdArr.eq(8).find('input').val());
   });
	
	$("#sumamt").text(decimal(sumamt,2));
	$("#sumnetamt").text(decimal(sumnetamt,2));
	$("#sumtaxamt").text(decimal(sumtaxamt,2));
}

function decimal(num,v){
	var vv = Math.pow(10,v);
	return Math.round(num*vv)/vv;
}

function geren(){
	isdanwei=0;
	$("#geren").addClass("am-active");
	$("#danwei").removeClass("am-active");
	$("#invoiceHeadEntText").val('个人');
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadDzdh").val('');
	$("#invoiceHeadYhzh").val('');
	$("#invoiceHeadEntTaxno").attr("disabled","disabled");
	$("#invoiceHeadDzdh").attr("disabled","disabled");
	$("#invoiceHeadYhzh").attr("disabled","disabled");
}
function danwei(){
	isdanwei=1;
	$("#danwei").addClass("am-active");
	$("#geren").removeClass("am-active");
	$("#invoiceHeadEntText").val('');
	$("#invoiceHeadEntTaxno").removeAttr("disabled"); 
	$("#invoiceHeadEntText").removeAttr("disabled");
	$("#invoiceHeadDzdh").removeAttr("disabled");
	$("#invoiceHeadYhzh").removeAttr("disabled");
}
function add(){
    var tr=$("#table1 tr:last");
    var add=$("#addnew").clone();
    add.insertBefore(tr);
    calsum();
 }
function del(m){
	m.parentNode.parentNode.remove();
	calsum();
}
function kaipiao(){
	params.sheettype = 0;
	params.requestBillItem=[];
	var ischeck = true;
	
	$("#table1").find("tr").each(function(i, n){
		if(i==0){return true;}
		if($(this).attr("id")=='tfoot') return true;
        var tdArr = $(this).children();
        var item = {};
        item.taxitemid = tdArr.eq(1).find('input').val();//税目
        item.goodsname = tdArr.eq(2).find('input').val();//名称
        item.unit = tdArr.eq(3).find('input').val();//单位
        item.qty = tdArr.eq(4).find('input').val();//数量
        item.taxrate = tdArr.eq(5).find('select').val();//税率
        item.amt = tdArr.eq(6).find('input').val();//含税金额
        item.taxpre = tdArr.eq(9).find('select').val();//是否享受优惠
        item.taxprecon = tdArr.eq(10).find('input').val();//优惠类型
        item.zerotax = tdArr.eq(11).find('select').val();//零税率标识
        if(item.taxitemid == null || item.taxitemid == ''){
        	$.myAlert("请选择税目");
        	ischeck=false;
        	return false;
        }
        if(item.goodsname == null || item.goodsname == ''){
        	$.myAlert("请输入名称");
        	ischeck=false;
        	return false;
        }
        if(item.qty == null || item.qty == ''){
        	$.myAlert("请输入数量");
        	ischeck=false;
        	return false;
        }
        if(item.amt == null || item.amt == ''){
        	$.myAlert("请输入金额");
        	ischeck=false;
        	return false;
        }
        if(item.taxrate == null || item.taxrate == ''){
        	$.myAlert("请输入税率");
        	ischeck=false;
        	return false;
        }
        
        params.requestBillItem.push(item);
    });
	if(!ischeck){
		return;
	}
	var invoiceHeadEntText = $("#invoiceHeadEntText").val();//公司名称，抬头
	var invoiceHeadEntTaxno = $("#invoiceHeadEntTaxno").val();//纳税人识别号
	var invoiceHeadDzdh = $("#invoiceHeadDzdh").val();//地址电话
	var invoiceHeadYhzh = $("#invoiceHeadYhzh").val();//银行账户
	var iqfplxdm = $('#iqfplxdm').val();
	var invRecvMail = $("#invRecvMail").val();//发票接收人邮箱
	
	if(invoiceHeadEntText == '' || invoiceHeadEntText == null){
		$.myAlert("请输入单位名称");
		return ;
	}
	//个人
	if(isdanwei == 0){
	}else{
		if(invoiceHeadEntTaxno == null || invoiceHeadEntTaxno == ''){
			$.myAlert("请输入纳税人识别号");
			return ;
		}
	}
	if(!$('#div_info').validator("isFormValid")) return;
	
	var requestBill ={};
	//1有小票发票，0手工录入发票
	requestBill.source = 0;
	requestBill.channel=channel;
	requestBill.gmfNsrsbh = invoiceHeadEntTaxno;
	requestBill.gmfMc = invoiceHeadEntText;
	requestBill.gmfDzdh = invoiceHeadDzdh;
	requestBill.gmfYhzh = invoiceHeadYhzh;
	requestBill.iqfplxdm = iqfplxdm;
	//开票类型 0蓝字、1红字、2作废
	requestBill.invType = 0;
	requestBill.sheettype = 0;
	requestBill.recvEmail=invRecvMail;
	requestBill.requestInvoicePreviewItem = [];
	$.myAjax({
		url:$.getService("api","getInvoiceBillInfo"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			params.action="update";
			params.sheetid=dataJson.data.sheetid;
			timepre(requestBill);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
	$("#btn_submit").removeAttr("disabled");
	$('#my-yulan').modal({width:"760"});
}
function timepre(requestBill){
	var itemss={};
	itemss.sheetid=params.sheetid;
	itemss.je=0;
	requestBill.requestInvoicePreviewItem.push(itemss);
	askPreView(requestBill);
}

function askPreView(requestBill){
	$("#preview").html("");
	var postData =JSON.stringify(requestBill);
	$.myAjax({
		url:$.getService("api","getInvoicePreview"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			//把预览的数据存下来，正式提交发票时要用到
			previewData = dataJson.data;
			$.myRowHTML(dataJson.data,"rowPreview","preview",1);
			//写入明细信息
			for ( var i = 0; i < dataJson.data.length; i++) {
				$.myRowHTML(dataJson.data[i].invoicePreviewItem,"rowPreviewDetail","previewDetail"+i,2);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

//提交开票申请
function commit(){
	var postData =JSON.stringify(previewData);
	$.myAjax({
		url:$.getService("api","askInvoice"),
		contentType:"application/json;charset=UTF-8",
		data:postData,
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			$('#btn_submit').attr("disabled","disabled");
			params.action="";
			params.sheetid="";
			
			$.myConfirm({msg:"开票成功",title:"开票成功",btn2:"查看",
				onCancel:function(){window.location.reload();},
				onConfirm:function(){
					var url="invoiceView.html?iqseqno="+iqseqno+"&token="+$.getUrlParam("token");
					window.open(url);
					window.location.reload();
			}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function wkpiaomo(){
	$('#wkpiao').modal({width:'800',height:'500'});
	$("#startdate").datepicker();
	$("#enddate").datepicker();
	dangqianbtn();
	wkpiao(0);
}
function wkpiao(page){
	var params={};
	params.page=page;
	params.pagesize=8;
	
	params.startdate=$("#startdate").val();
	params.enddate=$("#enddate").val();
	$.myAjax({
		url:$.getService("api","getBillsalereport"),
		contentType:"application/json;charset=UTF-8",
		data:JSON.stringify(params),
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			for (var i=0;i<dataJson.data.length;i++){
				dataJson.data[i].sdate=TimeObjectUtil.longMsTimeConvertToDate(dataJson.data[i].sdate);
			}
			$.myRowHTML(dataJson.data,"Billsalereport","searchBillsalereport");
			$.myPage(page,{rows:dataJson.count,pagesize:8,targetId:"page1",jump:function(page){
				wkpiao(page);
			}});
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function wkpiaoins(){
	var selectrows = $("#wkptable input[type='checkbox']:checked");
	if(selectrows == null || selectrows == ''){
		return;
	}
	
	for(var i=0;i<selectrows.length;i++){
		var add=$("#addnew").clone();
		$("#table1").append(add);
		var tdArr=$("#table1 tr:last").children();
	 	tdArr.eq(1).find('input').val(selectrows[i].value.split(",")[0]);//税目
        tdArr.eq(2).find('input').val(selectrows[i].value.split(",")[1]);//名称
        tdArr.eq(4).find('input').val(selectrows[i].value.split(",")[2]);//数量
        tdArr.eq(6).find('input').val(selectrows[i].value.split(",")[3]);//含税金额
        tdArr.eq(7).find('input').val(selectrows[i].value.split(",")[4]);//税率
	}
	
	$('#wkpiao').modal('close');
}

function dangqianbtn(){
	$("#startdate").datepicker('setValue', new Date());
	$("#enddate").datepicker('setValue', new Date());
}

