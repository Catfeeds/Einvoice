var user;
var checkshopid;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
var print_iqsen;
var searchdata;
$(document).ready(function(){
	geren();
	getuser();
	searchKB();
	//验证
	$('#div_info').validator();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
	//打印
	$("#btn_print").click(function(){
		fp_print();
	});
	
	//选择开电票类型时，隐藏空白票
	
	$("#iqfplxdm").on('change', function() {
		do_something();
	});

});

function do_something(){
	var iqfplxdm = $('#iqfplxdm').val();
	if(iqfplxdm=="026"){
		  $("#kongbai").hide();
		  $("#kongbai1").hide();
		  $("#fpdm").hide();
	}else{
		  $("#kongbai").show();
		  $("#kongbai1").show();
		  $("#fpdm").show();
	}
}

function getuser(){
	$.myAjax({
		url:$.getService("ui","getcuruser"),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			user = dataJson.data;
			checkshopid = user.shopid;
			$("#requestShopid").val(user.shopid);
			$("#requestShopname").val(user.shopname);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function search(){
	var str;
	var issearch = true;
	var data ={};
	var shopid = $("#requestShopid").val();
	var syjid = $("#requestSyjid").val();
	var billno = $("#requestbillno").val();
	data.channel = channel;
	data.sheettype = 6;
	if(data.shopid=='' ||data.syjid=='' ||data.billno=='' ){
		return;
	}
	
	if(syjid!=''){//用小票提取码提取数据
	str = syjid ;//shopid+"-"+syjid+"-"+billno;
	data.ticketQC = str;
	data.shopid = checkshopid;
	}else
	if(billno!=''){//用省份证号提取数据,带发票票种
		str = billno ;//shopid+"-"+syjid+"-"+billno;
		data.gmfno = str;
		data.fplxdm = $('#iqfplxdm').val();
		data.shopid = checkshopid;
	}else{
		$.myAlert("请输入提取码或者身份证号！");
		return false;
	}
		if(syjid!=''&&syjid!=null){
		$.myAjax({
			url:$.getService("api","getInvoiceBillInfoWithDetail"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				searchdata = dataJson;

				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				
				if(dataJson.data.flag == 0){
					var iqfplxdm = $('#iqfplxdm').val();
					if(dataJson.data.invoicelx!=''&&dataJson.data.invoicelx!=null){
						if(iqfplxdm!=dataJson.data.invoicelx){
							$.myAlert("查询的小票不是选择发票的票种或者该小票未办理提货手续，不能开具电子发票！");
							return false;
						}
					}
					
					if(dataJson.data.invoiceamount==0){
						$.myAlert("查询的小票为不可开票状态！");
						return false;
					}
					
					var weilist = $("#weilist input[type=checkbox]");
					$.each( weilist, function(i, n){
						var m = $(n);
						if(dataJson.data.sheetid == m.val()){
							$.myAlert("已经查询过该小票");
							issearch = false;
							return false;
						}
					});
				
			
					if($("#invoiceHeadEntText").val()!=''&&$("#invRecvPhone").val()!=''){
						if($("#invoiceHeadEntText").val()!=dataJson.data.gmfname){
							$.myAlert("此小票对应发票抬头与页面名称不一样！此小票发票抬头为："+dataJson.data.gmfname+"!");
							return false;
						}
						if($("#invRecvPhone").val()!=dataJson.data.gmfadd){
							$.myAlert("此小票对应手机号与页面手机号不一样！此小票手机号为："+dataJson.data.gmfadd+"!");
							return false;
						}
					}else{
						if(dataJson.data.gmfname!=null&&dataJson.data.gmfname!=''){
							 
							$("#invoiceHeadEntText").val(dataJson.data.gmfname);
							$('#invRecvPhone').val(dataJson.data.gmfadd);
							$("#invoiceHeadEntText").attr("disabled","disabled");
							$("#invoiceHeadDzdh").attr("disabled","disabled");
							$("#invoiceHeadYhzh").attr("disabled","disabled");
							$("#geren").attr("disabled","disabled");
							$("#danwei").attr("disabled","disabled"); 
							//$("#invRecvPhone").attr("disabled","disabled");
							//$("#iqfplxdm").prop("disabled", true);  
							
							$('#iqfplxdm').attr("disabled","disabled");
							$("#iqfplxdm").trigger("chosen:updated");  
						}
					}
					if(issearch){
						$.myRowHTML([dataJson.data],"rowmo","row1",1);
					}
					
					 
				}else{
					var yilist = $("#yilist input[type=checkbox]");
					$.each( yilist, function(i, n){
						var m = $(n);
						if(dataJson.data.sheetid == m.val()){
							$.myAlert("已经查询过该小票");
							issearch = false;
							return false;
						}
					});
					if(issearch){
						$.myRowHTML([dataJson.data],"rowmo","row2",1);
					}
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("没有查询到该小票! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
		}else{
			
			$.myAjax({
				url:$.getService("api","getInvoiceBillInfoByGmfNo"),
				data:JSON.stringify(data),
				contentType:"text/html;charset=UTF-8",
				success: function (dataJson) {
					searchdata = dataJson;
					
					if(dataJson.code!=0){
						$.myAlert(dataJson.message);
						return;
					}
					
					if(dataJson.data.length==0){
						$.myAlert("没有查询到该身份证下的小票信息!");
						return false;
					}
					
					var fptt = true;
					for (var i=0;i<dataJson.data.length;i++)
					{
						var xidata =dataJson.data[i]; 
						
						if(xidata.flag == 0){
							var iqfplxdm = $('#iqfplxdm').val();
							if(dataJson.data.invoicelx!=''&&dataJson.data.invoicelx!=null){
								if(iqfplxdm!=xidata.invoicelx){
									$.myAlert("查询的小票不是选择发票的票种！");
									return false;
								}
							}
							
						
							
							var weilist = $("#weilist input[type=checkbox]");
							$.each( weilist, function(i, n){
								var m = $(n);
								if(xidata.sheetid == m.val()){
									$.myAlert("已经查询过该小票");
									issearch = false;
									return false;
								}
							});
						
							if(fptt){
								if($("#invoiceHeadEntText").val()!=''&&$("#invRecvPhone").val()!=''){
									if($("#invoiceHeadEntText").val()!=xidata.gmfname){
										$.myAlert("此小票对应发票抬头与页面名称不一样！此小票发票抬头为："+xidata.gmfname+"!");
										return false;
									}
									if($("#invRecvPhone").val()!=xidata.gmfadd){
										$.myAlert("此小票对应手机号与页面手机号不一样！此小票手机号为："+xidata.gmfadd+"!");
										return false;
									}
									
								}else{
									if(xidata.gmfname!=null&&xidata.gmfname!=''){
										 
										$("#invoiceHeadEntText").val(xidata.gmfname);
										$('#invRecvPhone').val(xidata.gmfadd);
										$("#invoiceHeadEntText").attr("disabled","disabled");
										$("#invoiceHeadDzdh").attr("disabled","disabled");
										$("#invoiceHeadYhzh").attr("disabled","disabled");
										$("#geren").attr("disabled","disabled");
										$("#danwei").attr("disabled","disabled"); 
										//$("#invRecvPhone").attr("disabled","disabled");
										//$("#iqfplxdm").prop("disabled", true);  
										
										$('#iqfplxdm').attr("disabled","disabled");
										$("#iqfplxdm").trigger("chosen:updated");  
									}
								}
								fptt = false;
							}
							
							if(issearch){
								if(xidata.invoiceamount!=0){
									$.myRowHTML([xidata],"rowmo","row1",1);
								}
							}
							
							 
						}else{
							var yilist = $("#yilist input[type=checkbox]");
							$.each( yilist, function(i, n){
								var m = $(n);
								if(xidata.sheetid == m.val()){
									$.myAlert("已经查询过该小票");
									issearch = false;
									return false;
								}
							});
							
							if(issearch){
								if(xidata.invoiceamount!=0){
									$.myRowHTML([xidata],"rowmo","row2",1);
								}
							}
						}
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
					$.myAlert("没有查询到该小票! status:"+textStatus+" msg:"+jqXHR.responseText);
				}
			});
		}
	}



function geren(){
	isdanwei=0;
	$("#geren").addClass("am-active");
	$("#danwei").removeClass("am-active");
	$("#invoiceHeadEntText").val('');
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadEntTaxno").attr("disabled","disabled");
//	$("#invoiceHeadEntText").attr("disabled","disabled");
}
function danwei(){
	isdanwei=1;
	$("#danwei").addClass("am-active");
	$("#geren").removeClass("am-active");
	$("#invoiceHeadEntText").val('');
	$("#invoiceHeadEntTaxno").removeAttr("disabled"); 
	$("#invoiceHeadEntText").removeAttr("disabled");
}

function add(){
    var tr=$("#table1 tr:last");
    tr.append($("#addnew").innerHtml);
 }

function kaipiao(){
	
	var invoiceHeadEntText = $("#invoiceHeadEntText").val();//公司名称，抬头
	var invoiceHeadEntTaxno = $("#invoiceHeadEntTaxno").val();//纳税人识别号
	var invoiceHeadDzdh = $("#invoiceHeadDzdh").val();//地址电话
	var invoiceHeadYhzh = $("#invoiceHeadYhzh").val();//银行账户
	var dqfpdm = $("#kbfpdm").text();//空白发票代码
	var dqfphm = $("#kbfphm").text();//空白发票号码
	var iqfplxdm = $('#iqfplxdm').val();
	var invRecvMail = $('#invRecvMail').val();
	var invRecvPhone = $('#invRecvPhone').val();

	if(iqfplxdm=='026'){
		if(invRecvMail=='' && invRecvPhone==''){
			$.myAlert("请填写邮箱或手机号");
			return ;
		}
	}else{
		if(isNaN(dqfpdm) || isNaN(dqfphm)){
			$.myAlert("请先查询空白票");
			return ;
		}
	}
	
	
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
	requestBill.source = 1;
	requestBill.channel=channel;
	requestBill.requestInvoicePreviewItem = [];
	//找到所有选中的小票
	var boxes = $("#weilist input[type=checkbox]");
	$.each( boxes, function(i, n){
		
		var m = $(n);
		if(m.is(":checked")) {
			var v = m.val();
			var item = {};
/*			var oo = v.split('-');
			
			item.shopid = oo[0];
			item.syjid = oo[1];
			item.billno = oo[2];*/
			item.sheetid=v;//item.shopid+"-"+item.syjid+"-"+item.billno;
			requestBill.requestInvoicePreviewItem.push(item);
		}
	});
	if(requestBill.requestInvoicePreviewItem.length==0){
		$.myAlert("至少选中一张小票");
		return ;
	}
	$('#my-yulan').modal();
	requestBill.gmfNsrsbh = invoiceHeadEntTaxno;
	requestBill.gmfMc = invoiceHeadEntText;
	requestBill.gmfDzdh = invoiceHeadDzdh;
	requestBill.gmfYhzh = invoiceHeadYhzh;
	requestBill.dqfpdm = dqfpdm;
	requestBill.dqfphm = dqfphm;
	requestBill.iqfplxdm = iqfplxdm;
	requestBill.invType = "1";
	requestBill.sheettype = 6;
	requestBill.iqpayee=$("#iqpayee").val();
	requestBill.iqchecker=$("#iqchecker").val();
	requestBill.iqmemo=$("#remark").val();
	requestBill.recvPhone = invRecvPhone;
	requestBill.recvEmail = invRecvMail;
	askPreView(requestBill);
}

function askPreView(requestBill){
	print_iqsen="";
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
			$("#dqfpdm").text(requestBill.dqfpdm);
			$("#dqfphm").text(requestBill.dqfphm);
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
			if(previewData[0].gmfNsrsbh != null && previewData[0].gmfNsrsbh != ''){
				updateCustomerInfo();
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			print_iqsen=iqseqno;
			var iqfplxdm = $('#iqfplxdm').val();
			if(iqfplxdm!='026'){
			$.myAlert("开票成功,请放入发票纸打印");
			$('#btn_submit').attr("disabled","disabled");
			$("#btn_print").removeAttr("disabled"); 
			 $("#kongbai").show();
			  $("#kongbai1").show();
			  $("#fpdm").show();
			searchKB();
			}else{
	
				$.myConfirm({title:"成功",msg:"稍后电子发票将发送到您的短信和邮箱，请注意查收",btn1:"继续开票",btn2:"查看开票",onCancel:function(){window.location.reload();},onConfirm:function(){$.gotoUrl('./invoiceView.html?iqseqno='+iqseqno);}});
				  $("#kongbai").hide();
				  $("#kongbai1").hide();
				  $("#fpdm").hide();
			}
			$("#row1").html("");
			$("#row2").html("");
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function searchKB(){
	var iqfplxdm = $('#iqfplxdm').val();
	var data ={};
	if(iqfplxdm=='026'){
		  $("#kongbai").hide();
		  $("#kongbai1").hide();
		  $("#fpdm").hide();
	}
	//004:专票 007:普票 
	data.iqfplxdm=iqfplxdm;
	 $.myAjax({
			url:$.getService("api","getBlankInvoice"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myRowHTML([dataJson.data],"fpdmmo","fpdm");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				
				$.myAlert("空白票查询错误! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
}

function fp_print(){
	var data ={};
	data.iqseqno=print_iqsen;
	$.myAjax({
		url:$.getService("api","fp_print"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.data.isqingdan != '' && dataJson.data.isqingdan == 1){
				$.myConfirm({title:"提醒",msg:"请放入清单纸",onConfirm:fp_printforQd});
			}else{
				$('#my-yulan').modal('close');
				window.location.href=window.location.href;
			}
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("发票打印错误! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function fp_printforQd(){
	var data ={};
	data.iqseqno=print_iqsen;
	$.myAjax({
		url:$.getService("api","fp_printforQd"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$('#my-yulan').modal('close');
			window.location.href=window.location.href;
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("清单打印错误! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function clearlist(){
	$("#row1").html("");
	$("#row2").html("");
	$("#iqfplxdm").removeAttr("disabled"); 
	//$("#iqfplxdm").prop("enabled", true);  
	$("#iqfplxdm").trigger("chosen:updated");  
	$("#invRecvPhone").val('');
	$("#invoiceHeadEntText").val('');
	$("#geren").removeAttr("disabled");
	$("#danwei").removeAttr("disabled");
	$("#invoiceHeadEntText").removeAttr("disabled");
	$("#invoiceHeadDzdh").removeAttr("disabled");
	$("#invoiceHeadYhzh").removeAttr("disabled");
	$("#invRecvPhone").removeAttr("disabled");
	
}

function zuofei(){
	$.myConfirm({msg:"确定要作废吗？",onConfirm:qdzuofei,onCancel:function(){}});
}
function qdzuofei(){
	var data={};
	var iqfplxdm = $('#iqfplxdm').val();
	data.zflx=0;
	data.iqfplxdm=iqfplxdm;//发票类型（如普票）
	$.myAjax({
		url:$.getService("api","fp_zuofei"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myAlert("作废成功");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

//查看已开票明细
var showinvoiceid;
function showModal(invoiceid){
	showinvoiceid=invoiceid;
	$('#taxmodal').modal({width:'800',height:'500'});
	showDetail(0);
}

function showDetail(page){
	var data={};
	data.page=page;
	data.pagesize=8;
	data.invoiceid=showinvoiceid;
	var syjid = $("#requestSyjid").val();
	var billno = $("#requestbillno").val();
	if(syjid!=''){//用小票提取码提取数据
		$.myRowHTML(searchdata.data.invoiceSaleDetail,"rowmo2","searchRows");
		//str = syjid ;//shopid+"-"+syjid+"-"+billno;
		//data.ticketQC = str;
		}else
		if(billno!=''){//用省份证号提取数据,带发票票种
			for (var i=0;i<searchdata.data.length;i++)
			{
				var xidata =searchdata.data[i];
				$.myRowHTML(xidata.invoiceSaleDetail,"rowmo2","searchRows");
			}
			
			//str = billno ;//shopid+"-"+syjid+"-"+billno;
			//data.gmfno = str;
			//data.fplxdm = $('#iqfplxdm').val();
		} 
	
	
/*	$.myAjax({
		url:$.getService("ui","getInvoiceDetail"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myPage(page,{rows:dataJson.count,pagesize:8,targetId:"page1",jump:function(page){
				showDetail(page);
			}});
			$.myRowHTML(searchdata.data.invoiceSaleDetail,"rowmo2","searchRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});*/
}

