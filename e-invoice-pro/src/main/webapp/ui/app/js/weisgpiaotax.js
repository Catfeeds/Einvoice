var channel = "app";
var isdanwei = 0;// 0个人，1单位
var previewData = {};
var params = {};
var user;
var wkplist;// 查询出的未开票商品集合
var yxsplist = []; // 已选择的开票商品；
params.channel = channel;
$(document).ready(function() {

	geren();
	getuser();
	// 验证
	$('#div_info').validator();
	// 提交
	$("#btn_submit").click(function() {
		commit();
	});

	$.mySelect({
		tragetId : "shopname",
		id : "shopid",
		url : $.getService("ui", "getShopList"),
		onChange : function(e) {
			// console.log(e);
		}
	});

	$.mySelect({
		tragetId : "taxshopname",
		id : "shopidtax",
		url : $.getService("ui", "getShopList"),
		onChange : function(e) {
			// console.log(e);
		}
	});
});

function getuser() {
	$.myAjax({
		url : $.getService("ui", "getcuruser"),
		contentType : "text/html;charset=UTF-8",
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			user = dataJson.data;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
}
var sea;
var selectTr;
function search(e) {
	sea = $(e).parent().prev();
	selectTr = $(e).parents("tr");
	$('#modal').modal({
		width : '800',
		height: '500'
	});
	searchtax(0);
}
function searchtax(page) {
	params.page = page;
	params.pagesize = 8;
	var taxitemname = $("#requestname").val();
	params.taxitemname = taxitemname;
	$.myAjax({
		url : $.getService("ui", "queryTaxitem"),
		data : JSON.stringify(params),
		contentType : "text/html;charset=UTF-8",
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			$.myRowHTML(dataJson.data, "rowmodel", "searchRows");
			$.myPage(page, {
				rows : dataJson.count,
				pagesize : 8,
				jump : function(page) {
					searchtax(page);
				}
			});
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
}
function instax() {
	var selectrow = $("#taxtable input[type='checkbox']:checked");
	if (selectrow == null || selectrow == '') {
		return;
	}
	if (selectrow != null && selectrow.length > 1) {
		$.myAlert("只能选择一条税目");
		return;
	}
	var taxitemid = selectrow[0].value.split(",")[0];
	var taxite = selectrow[0].value.split(",")[1];
	sea.val(taxitemid);
	var taxiteinput = selectTr.find("input[name='taxrate']");
	taxiteinput.val(taxite);
	$('#modal').modal('close');
}

function geren() {
	isdanwei = 0;
	$("#geren").addClass("am-active");
	$("#danwei").removeClass("am-active");
	$("#invoiceHeadEntText").val('个人');
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadDzdh").val('');// 地址电话
	$("#invoiceHeadYhzh").val('');// 银行账户
	$("#invRecvMail").val('');// 发票接收人邮箱
	$("#invoiceHeadEntTaxno").attr("disabled", "disabled");
}
function danwei() {
	isdanwei = 1;
	$("#danwei").addClass("am-active");
	$("#geren").removeClass("am-active");
	$("#invoiceHeadEntText").val('');
	$("#invoiceHeadDzdh").val('');// 地址电话
	$("#invoiceHeadYhzh").val('');// 银行账户
	$("#invRecvMail").val('');// 发票接收人邮箱
	$("#invoiceHeadEntTaxno").removeAttr("disabled");
	$("#invoiceHeadEntText").removeAttr("disabled");
}
function add() {
	var tr = $("#table1 tr:last");

	var add = $("#addnew").clone();
	$("#table1").append(add);
}
function del(m) {
	m.parentNode.parentNode.remove();
}
function kaipiao() {
	params.sheettype = 0;
	params.requestBillItem = [];
	var ischeck = true;

	$("#table1")
			.find("tr")
			.each(
			function(i, n) {
				if (i == 0) {
					return true;
				}
				var tdArr = $(this).children();
				var item = {};
				item.taxitemid = tdArr.find("input[name='taxitemid']").val();// 税目
				item.goodsname = tdArr.find("select[name='goodsname']").val();// 名称
				item.unit = tdArr.find("input[name='unit']").val();// 单位
				item.qty = tdArr.find("input[name='qty']").val();// 数量
				item.price = tdArr.find("input[name='price']").val();// 单价
				item.amt = tdArr.find("input[name='amt']").val();// 含税金额
				item.taxrate = tdArr.find("input[name='taxrate']").val();// 税率
				item.goodsid = tdArr.find("input[name='goodsid']").val();
				item.tradeDate = tdArr.find("input[name='tradeDate']").val();

				if (item.taxitemid == null || item.taxitemid == '') {
					$.myAlert("请选择税目");
					ischeck = false;
					return false;
				}
				if (item.goodsname == null || item.goodsname == '') {
					$.myAlert("请输入名称");
					ischeck = false;
					return false;
				}
				if (item.qty == null || item.qty == '') {
					$.myAlert("请输入数量");
					ischeck = false;
					return false;
				}
				if (item.amt == null || item.amt == '') {
					$.myAlert("请输入金额");
					ischeck = false;
					return false;
				}
				if (parseFloat(item.amt) > parseFloat(tdArr.find(
						"input[name='unusedamt']").val())) {
					$.myAlert("输入金额超出可开票金额");
					ischeck = false;
					return false;
				}
				if (parseFloat(item.qty) > parseFloat(tdArr.find(
						"input[name='unuseqty']").val())) {
					$.myAlert("输入数量超出可开票数量");
					ischeck = false;
					return false;
				}
				if (item.taxrate == null || item.taxrate == '') {
					$.myAlert("请输入税率");
					ischeck = false;
					return false;
				}
				var syamt = item.amt;
				for (var x = 0; x < yxsplist.length; x++) {
					if (yxsplist[x].amt - yxsplist[x].useamt > 0) {
						if (item.goodsid == yxsplist[x].goodsid) {
							if (syamt > 0) {
								var itemclone = cloneObj(item);
								if (syamt > (yxsplist[x].amt - yxsplist[x].useamt)) {
									syamt = syamt - (yxsplist[x].amt - yxsplist[x].useamt);
									itemclone.amt = yxsplist[x].amt - yxsplist[x].useamt;
									itemclone.qty = (yxsplist[x].amt - yxsplist[x].useamt) / item.price;
									itemclone.price = item.price;
									var trad = yxsplist[x].trademonth.substr(0, 4) + "-" + yxsplist[x].trademonth.substr(4, 2) + "-01";
									itemclone.tradeDate = trad;
									params.requestBillItem.push(itemclone);
								} else {
									itemclone.amt = syamt;
									syamt = 0;
									itemclone.qty = itemclone.amt / item.price;
									itemclone.price = item.price;
									var trad = yxsplist[x].trademonth.substr(0, 4) + "-" + yxsplist[x].trademonth.substr(4, 2) + "-01";
									itemclone.tradeDate = trad;
									params.requestBillItem.push(itemclone);
								}
							}
						}
					}

				}
			});
	
	if(params.requestBillItem.length==0){
		return;
	}

	if (!ischeck) {
		return;
	}
	/*if (!jsprice) {
		$.myAlert("商品单价不在可开票范围内");
		return;
	}*/
	var invoiceHeadEntText = $("#invoiceHeadEntText").val();// 公司名称，抬头
	var invoiceHeadEntTaxno = $("#invoiceHeadEntTaxno").val();// 纳税人识别号
	var invoiceHeadDzdh = $("#invoiceHeadDzdh").val();// 地址电话
	var invoiceHeadYhzh = $("#invoiceHeadYhzh").val();// 银行账户
	var iqfplxdm = $('#iqfplxdm').val();
	var invRecvMail = $("#invRecvMail").val();// 发票接收人邮箱

	if (invoiceHeadEntText == '' || invoiceHeadEntText == null) {
		$.myAlert("请输入单位名称");
		return;
	}
	// 个人
	/*
	 * if(isdanwei == 0){ }else{ if(invoiceHeadEntTaxno == null ||
	 * invoiceHeadEntTaxno == ''){ $.myAlert("请输入纳税人识别号"); return ; } }
	 */
	if (iqfplxdm == '026' && invRecvMail == '') {
		$.myAlert("请输入邮箱");
		return;
	}
	if (!$('#div_info').validator("isFormValid"))
		return;

	var requestBill = {};
	// 1有小票发票，0手工录入发票
	requestBill.source = 0;
	requestBill.channel = channel;
	$('#my-yulan').modal();
	requestBill.gmfNsrsbh = invoiceHeadEntTaxno;
	requestBill.gmfMc = invoiceHeadEntText;
	requestBill.gmfDzdh = invoiceHeadDzdh;
	requestBill.gmfYhzh = invoiceHeadYhzh;
	requestBill.iqfplxdm = iqfplxdm;
	// 开票类型 0蓝字、1红字、2作废
	requestBill.invType = 0;
	requestBill.sheettype = 0;
	requestBill.recvEmail = invRecvMail;
	requestBill.requestInvoicePreviewItem = [];
	$.myAjax({
		url : $.getService("api", "getInvoiceBillInfo"),
		data : JSON.stringify(params),
		contentType : "text/html;charset=UTF-8",
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			params.action = "update";
			params.sheetid = dataJson.data.sheetid;
			timepre(requestBill);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("status:" + textStatus + " msg:" + jqXHR.responseText);
		}
	});
	// $('#btn_print').attr("disabled","disabled");
	$("#btn_submit").removeAttr("disabled");
}
function timepre(requestBill) {
	var itemss = {};
	itemss.sheetid = params.sheetid;
	itemss.je = 0;
	requestBill.requestInvoicePreviewItem.push(itemss);
	askPreView(requestBill);
}

function askPreView(requestBill) {
	$("#preview").html("");
	var postData = JSON.stringify(requestBill);
	$.myAjax({
		url : $.getService("api", "getInvoicePreview"),
		contentType : "application/json;charset=UTF-8",
		data : postData,
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			// 把预览的数据存下来，正式提交发票时要用到
			previewData = dataJson.data;
			$.myRowHTML(dataJson.data, "rowPreview", "preview", 1);
			// 写入明细信息
			for (var i = 0; i < dataJson.data.length; i++) {
				$.myRowHTML(dataJson.data[i].invoicePreviewItem,
						"rowPreviewDetail", "previewDetail" + i, 2);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
}

// 提交开票申请
function commit() {
	var postData = JSON.stringify(previewData);
	$.myAjax({
		url : $.getService("api", "askInvoice"),
		contentType : "application/json;charset=UTF-8",
		data : postData,
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			if (previewData[0].gmfNsrsbh != null
					&& previewData[0].gmfNsrsbh != '') {
				updateCustomerInfo();
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			$.myAlert("开票成功");
			$('#btn_submit').attr("disabled", "disabled");
			// $("#btn_print").removeAttr("disabled");
			params.action = "";
			params.sheetid = "";
			$('#my-yulan').modal('close');
			window.location.href = window.location.href;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
}

function wkpiaomo() {
	$('#wkpiao').modal({
		width : '900',
		height : '880'
	});
	loaddate();
	$("#shopid").val(user.shopid);
	$('select').trigger('changed.selected.amui');
	$("#shopid").selected('disable');
	wkpiao(0);
}

function wkpiaotaxmo() {
	$('#wkpiaotax').modal({
		width : '900',
		height : '880'
	});
	loaddate();
	$("#shopidtax").val(user.shopid);
	$('select').trigger('changed.selected.amui');
	$("#shopidtax").selected('disable');
	wkpiaotax(0);
}

function wkpiao(page) {
	var params = {};
	params.page = page;
	params.pagesize = 20;
	params.shopid = $("#shopid").val();
	params.sdate = $("#startdate").val();
	params.edate = $("#enddate").val();
	params.goodsname = $("#goodsname").val();

	if (params.edate != '' && params.edate != null) {
		params.edate = params.edate + " 23:59:59";
	}
	$.myAjax({
		url : "/e-invoice-pro/rest/api/querySheetStat",
		contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(params),
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			wkplist = dataJson.data;
			for (var i = 0; i < dataJson.data.length; i++) {
				dataJson.data[i].unuseqty = parseFloat(
						dataJson.data[i].qty - dataJson.data[i].useqty)
						.toFixed(2);
				dataJson.data[i].unusedamt = parseFloat(
						dataJson.data[i].amt - dataJson.data[i].useamt)
						.toFixed(2);
			}
			$
					.myRowHTML(dataJson.data, "Billsalereport",
							"searchBillsalereport");
			$.myPage(page, {
				rows : dataJson.count,
				pagesize : 16,
				targetId : "page1",
				jump : function(page) {
					wkpiao(page);
				}
			});
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
}

function wkpiaotax(page) {
	var params = {};
	params.page = page;
	params.pagesize = 20;
	params.shopid = $("#shopidtax").val();
	params.sdate = $("#taxstartdate").val();
	params.edate = $("#taxenddate").val();
	params.goodsname = $("#taxgoodsname").val();

	if (params.edate != '' && params.edate != null) {
		params.edate = params.edate + " 23:59:59";
	}
	$.myAjax({
		url : "/e-invoice-pro/rest/api/querySheetTaxitem",
		contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(params),
		success : function(dataJson) {
			if (dataJson.code != 0) {
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.count ==0 ){
				$.myAlert("没有数据");
				return;
			}
			wkplist = dataJson.data;
			for (var i = 0; i < dataJson.data.length; i++) {
				dataJson.data[i].unuseqty = parseFloat(
						dataJson.data[i].qty - dataJson.data[i].useqty)
						.toFixed(2);
				dataJson.data[i].unusedamt = parseFloat(
						dataJson.data[i].amt - dataJson.data[i].useamt)
						.toFixed(2);
			}
			$.myRowHTML(dataJson.data, "Billsalereporttax",
					"searchBillsalereporttax");
			$.myPage(page, {
				rows : dataJson.count,
				pagesize : 16,
				targetId : "page2",
				jump : function(page) {
					wkpiaotax(page);
				}
			});
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
}

function wkpiaoins() {
	var selectrows = $("#wkptable input[type='checkbox']:checked");
	if (selectrows == null || selectrows == '') {
		return;
	}

	for (var i = 0; i < selectrows.length; i++) {
		var flag = true;
		$("#table1").find("tr").each(function(j, n) {
			if (j == 0) {
				return true;
			}
			var tdArr = $(this).children();
			var goodsid;
			goodsid = tdArr.find("input[name='goodsid']").val();
			if (goodsid == (selectrows[i].value.split(",")[1])) {
				flag = false;
				return false;
			}
		});

		if (flag) {
			var unuseramt = 0;
			var unuserqty = 0;

			for (var x = 0; x < wkplist.length; x++) {
				if (wkplist[x].goodsid == selectrows[i].value.split(",")[1]) {
					unuserqty += parseFloat(wkplist[x].qty - wkplist[x].useqty);
					unuseramt += parseFloat(wkplist[x].amt - wkplist[x].useamt);
					yxsplist.push(wkplist[x]);
					break;
				}
			}

			addtr(selectrows[i],unuseramt,unuserqty);
		}
	}

	$('#wkpiao').modal('close');
}

function wkpiaoinstax() {
	var selectrows = $("#wkptabletax input[type='checkbox']:checked");
	if (selectrows == null || selectrows == '') {
		return;
	}

	for (var i = 0; i < selectrows.length; i++) {
		var flag = true;
		if (flag) {

			var unuseramt = 0;
			var unuserqty = 0;

			for (var x = 0; x < wkplist.length; x++) {
				if (wkplist[x].taxitemid == selectrows[i].value.split(",")[0]) {
					unuserqty += parseFloat(wkplist[x].qty - wkplist[x].useqty);
					unuseramt += parseFloat(wkplist[x].amt - wkplist[x].useamt);
					yxsplist.push(wkplist[x]);
					break;
				}
			}

			addtr(selectrows[i],unuseramt*0.9,unuserqty);
		}
	}

	$('#wkpiaotax').modal('close');
}

function addtr(selectrow,unuseramt,unuserqty){
	
	//查询税目对应的下拉
	var taxitemid = selectrow.value.split(",")[0];
	var taxrate = selectrow.value.split(",")[3];
	
	var data = {};
	data.taxitemid =taxitemid;
	data.taxrate = taxrate;
	var postData = JSON.stringify(data);
	$.myAjax({
		url : $.getService("api", "queryBillTaxGoodsName"),
		data : postData,
		contentType : "application/json;charset=UTF-8",
		success : function(dataJson) {
			if(dataJson.count==0){
				$.myAlert("该税目及税率下没有配置商品名称");
				return;
			}
			
			var add = $("#addnew").clone();
			$("#table1").append(add);
			var tdArr = $("#table1 tr:last").children();
			tdArr.find('input[name="taxitemid"]').val(taxitemid);// 税目
			//tdArr.find('input[name="goodsname"]').val(selectrow.value.split(",")[2]);// 名称
			tdArr.find('input[name="taxrate"]').val(taxrate);// 税率
			tdArr.find('input[name="qty"]').val(parseFloat(unuserqty).toFixed(2));// 数量
			tdArr.find('input[name="amt"]').val(parseFloat(unuseramt).toFixed(2));// 金额
			var price = parseFloat(parseFloat(unuseramt)/parseFloat(unuserqty)).toFixed(4);
			tdArr.find('input[name="price"]').val(price);// 单价
			
			tdArr.find("input[name='goodsid']").val(selectrow.value.split(",")[1]);
			tdArr.find("input[name='unusedamt']").val(parseFloat(unuseramt).toFixed(2));
			tdArr.find("input[name='unuseqty']").val(parseFloat(unuserqty).toFixed(2));
			tdArr.find("input[name='tradeDate']").val(selectrow.value.split(",")[9]);
			tdArr.find("input[name='hidprice']").val(parseFloat(selectrow.value.split(",")[10]).toFixed(2));
			
			var sel = tdArr.find('select[name="goodsname"]');
			var option = sel.find("option");
			for (var i = 0; i < option.length; i++) {
				sel.find("option:last").remove();
			}
			for (var i = 0; i < dataJson.data.length; i++) {
				console.log("<option value='"+dataJson.data[i].goodsname+"'>"+dataJson.data[i].goodsname+"</option>")
				sel.append("<option value='"+dataJson.data[i].goodsname+"'>"+dataJson.data[i].goodsname+"</option>");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:" + textStatus + " msg:"
					+ jqXHR.responseText);
		}
	});
	
}

var jsprice = true;
function jisuan(e, column) {
	var shopid = $("#shopid").val();
	var subshopid = shopid.substring(0, 1);

	var selectTr = $(e).parents("tr");
	var hidprice = parseFloat(selectTr.find("input[name='hidprice']").val());
	var unusedamt = parseFloat(selectTr.find("input[name='unusedamt']").val());
	var unuseqty = parseFloat(selectTr.find("input[name='unuseqty']").val());
	var qty = parseFloat(selectTr.find("input[name='qty']").val());
	var amt = parseFloat(selectTr.find("input[name='amt']").val());
	if (isNaN(qty)) {
		$.myAlert("输入的数量不是数字");
		return;
	}
	if (isNaN(amt)) {
		$.myAlert("输入的金额不是数字");
		return;
	}
	if(amt> unusedamt){
		$.myAlert("您开票的金额高于限额,最大开票金额为" + unusedamt + "!");
		amt = unusedamt;
		selectTr.find("input[name='amt']").val(amt);
	}
	if(qty> unuseqty){
		$.myAlert("您开票的数量高于限额,最大开票数量为" + unuseqty + "!");
		qty = unuseqty;
		selectTr.find("input[name='qty']").val(qty);
	}
	
	var newprice = (parseFloat(amt) / parseFloat(qty)).toFixed(4);
	if (user.entid == "SDYZ" && subshopid == "0") {

	} else {
		if (newprice > parseFloat(hidprice)) {
			$.myAlert("您开票的单价高于商品的参考售价,商品售价为" + hidprice + "!");
			jsprice = false;
		} else if (newprice < parseFloat(hidprice) * 0.95) {
			$.myAlert("您开票的单价低于商品参考售价的95折,商品售价为" + hidprice + "!");
			jsprice = false;
		} else {
			jsprice = true;
		}
	}

	selectTr.find("input[name='price']").val(newprice);
}

function clearkehu() {
	$("#invoiceHeadEntTaxno").val('');
	$("#invoiceHeadDzdh").val('');// 地址电话
	$("#invoiceHeadYhzh").val('');// 银行账户
	$("#invRecvMail").val('');// 发票接收人邮箱
}
// 获取用户初始化信息电话邮箱抬头等
function getCustomerInfo() {
	var data = {};
	data.taxname = $("#invoiceHeadEntText").val();
	if (!data.taxname) {
		clearkehu();
	}
	if (data.taxname != '') {
		$("#invoiceHeadEntText")
				.autocomplete(
						{
							source : function(request, response) {
								$
										.myAjax({
											url : $.getService("api",
													"getWebCustomerInfo"),
											data : JSON.stringify(data),
											contentType : "application/json;charset=UTF-8",
											success : function(dataJson) {
												if (dataJson.code == 0
														&& dataJson.data) {
													var myArray = new Array();
													for (var i = 0; i < dataJson.data.length; i++) {
														myArray[i] = dataJson.data[i].taxname;
													}
													response($
															.map(
																	dataJson.data,
																	function(
																			item) {
																		var name = item.name;
																		var id = item.id;
																		return {
																			label : item.taxname,// 下拉框显示值
																			value : item.taxname,// 选中后，填充到下拉框的值
																			taxno : item.taxno,
																			taxaddr : item.taxaddr,
																			taxbank : item.taxbank,
																			email : item.email
																		}
																	}));
												}
											},
											error : function(jqXHR, textStatus,
													errorThrown) {
												$.myAlert("访问后台失败! status:"
														+ textStatus + " msg:"
														+ jqXHR.responseText);
											}
										});

							},
							select : function changesel(event, ui) {
								changeCustomerInfo(ui.item);
							}
						});

	}

}

function changeCustomerInfo(data) {
	// $("#invoiceHeadEntText").val(data.taxname);
	$("#invoiceHeadEntTaxno").val(data.taxno);
	$("#invoiceHeadDzdh").val(data.taxaddr);
	$("#invoiceHeadYhzh").val(data.taxbank);
	$("#invRecvMail").val(data.email);
}

function updateCustomerInfo() {
	var data = {};
	data.taxname = $("#invoiceHeadEntText").val();
	data.taxno = $("#invoiceHeadEntTaxno").val();
	data.taxaddr = $("#invoiceHeadDzdh").val();
	data.taxbank = $("#invoiceHeadYhzh").val();
	data.email = $("#invRecvMail").val();

	var postData = JSON.stringify(data);
	$.myAjax({
		url : $.getService("api", "updateCustomerInfo"),
		data : postData,
		contentType : "application/json;charset=UTF-8",
		success : function(dataJson) {
		},
		error : function(jqXHR, textStatus, errorThrown) {
		}
	});
}

function loaddate() {
	var now = new Date;
	var now1 = new Date;
	var now2 = new Date;
	var enddate = TimeObjectUtil.longMsTimeConvertToDate(now1.setDate(now1
			.getDate() - 10));
	var mindate = TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now
			.getDate() - 92));
	var defultdate = TimeObjectUtil.longMsTimeConvertToDate(now2.setDate(now2
			.getDate() - 91));
	$("#enddate").datepicker({// 添加日期选择功能
		showButtonPanel : true,// 是否显示按钮面板
		dateFormat : 'yy-mm-dd',// 日期格式
		clearText : "清除",// 清除日期的按钮名称
		closeText : "关闭",// 关闭选择框的按钮名称
		yearSuffix : '年', // 年的后缀
		showMonthAfterYear : true,// 是否把月放在年的后面
		defaultDate : enddate,// 默认日期
		minDate : mindate,// 最小日期
		maxDate : enddate,// 最大日期
		onSelect : function(selectedDate) {// 选择日期后执行的操作
		}
	});

	$("#taxenddate").datepicker({// 添加日期选择功能
		showButtonPanel : true,// 是否显示按钮面板
		dateFormat : 'yy-mm-dd',// 日期格式
		clearText : "清除",// 清除日期的按钮名称
		closeText : "关闭",// 关闭选择框的按钮名称
		yearSuffix : '年', // 年的后缀
		showMonthAfterYear : true,// 是否把月放在年的后面
		defaultDate : enddate,// 默认日期
		minDate : mindate,// 最小日期
		maxDate : enddate,// 最大日期
		onSelect : function(selectedDate) {// 选择日期后执行的操作
		}
	});

	$("#startdate").datepicker({// 添加日期选择功能
		showButtonPanel : true,// 是否显示按钮面板
		dateFormat : 'yy-mm-dd',// 日期格式
		clearText : "清除",// 清除日期的按钮名称
		closeText : "关闭",// 关闭选择框的按钮名称
		yearSuffix : '年', // 年的后缀
		showMonthAfterYear : true,// 是否把月放在年的后面
		defaultDate : enddate,// 默认日期
		minDate : mindate,// 最小日期
		maxDate : enddate,// 最大日期
		onSelect : function(selectedDate) {// 选择日期后执行的操作
		}
	});
	$("#taxstartdate").datepicker({// 添加日期选择功能
		showButtonPanel : true,// 是否显示按钮面板
		dateFormat : 'yy-mm-dd',// 日期格式
		clearText : "清除",// 清除日期的按钮名称
		closeText : "关闭",// 关闭选择框的按钮名称
		yearSuffix : '年', // 年的后缀
		showMonthAfterYear : true,// 是否把月放在年的后面
		defaultDate : enddate,// 默认日期
		minDate : mindate,// 最小日期
		maxDate : enddate,// 最大日期
		onSelect : function(selectedDate) {// 选择日期后执行的操作
		}
	});
	$("#startdate").datepicker("setDate", defultdate);
	$("#enddate").datepicker("setDate", enddate);

	$("#taxstartdate").datepicker("setDate", defultdate);
	$("#taxenddate").datepicker("setDate", enddate);
}

var cloneObj = function(obj) {
	var newObj = {};
	if (obj instanceof Array) {
		newObj = [];
	}
	for ( var key in obj) {
		var val = obj[key];
		// newObj[key] = typeof val === 'object' ? arguments.callee(val) : val;
		// //arguments.callee 在哪一个函数中运行，它就代表哪个函数, 一般用在匿名函数中。
		newObj[key] = typeof val === 'object' ? cloneObj(val) : val;
	}
	return newObj;
};