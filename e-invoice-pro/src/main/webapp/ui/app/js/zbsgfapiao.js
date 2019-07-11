var channel = "app";
var isdanwei = 0;//0个人，1单位
var previewData = {};
var params = {};
params.channel = channel;
$(document).ready(function () {
    geren();
    add1();
    getShop();
    //验证
    $('#div_info').validator();
    //提交
    $("#btn_submit").click(function () {
        commit();
    });

    $("#iqfplxdm").on('change', function () {
        if (this.value == "026") {

        }
    });
});

function instax() {
    var selectrow = $("#taxtable input[name='taxitems']:checked");
    if (selectrow == null || selectrow == '') {
        return;
    }
    if (selectrow != null && selectrow.length > 1) {
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
        taxiteinput.append("<option value='" + taxite[i] + "'>" + taxite[i] + "</option>");
    }
    $('#modal').modal('close');
}

function cal(e, type) {
    var tr = $(e).parents("tr");
    var goodsBarcode = tr.children().eq(1).find('input').val();
    var goodsName = tr.children().eq(2).find('input').val();
    var taxrate = Number(tr.find("input[name='taxrate']").val());//税率
    var numInput = Number(tr.find("input[name='num']").val());//数量
    var priceInput = Number(tr.find("input[name='price']").val());//单价
    var amtInput = tr.find("input[name='netamt']");//金额
    var taxamtInput = tr.find("input[name='taxamt']");//税额
    var netamt=0,amt = 0, taxamt = 0;
    if (goodsBarcode == '' || goodsBarcode == 'NaN') {
        amtInput.val('');
        taxamtInput.val('');
        $.myAlert("请输入商品条码");
        return;
    }

    if (goodsName == '' || goodsName == 'NaN') {
        amtInput.val('');
        taxamtInput.val('');
        $.myAlert("请输入商品名称");
        return;
    }

    if (taxrate<0||taxrate>0.13) {
        amtInput.val('');
        taxamtInput.val('');
        $.myAlert("税率必须介于0到0.13之间");
        return;
    }

    if (numInput<=0) {
        $.myAlert("数量不能小于等于0");
        tr.find("input[name='num']").val('1');
        calsum();
        return;
    }else if (priceInput<0.1||priceInput>9999999) {
        $.myAlert("单价不能小于1角,不能大于9,999,999");
        tr.find("input[name='price']").val('0');
        amtInput.val(0);
        taxamtInput.val(0);
        calsum();
        return;
    }else{
        var peices = decimal(priceInput, 6);
        var numbers = decimal(numInput, 4);

        amt = decimal(peices * numbers, 2);//金额
        netamt=decimal(amt/(1+taxrate),2);//无税金额
        taxamt = decimal(amt-netamt, 2);//税额
        if(amt>9999999999.99||taxamt>99999999.99){
            $.myAlert("单价乘以数量算出的金额过大导致异常，请修改单价或数量！");
            tr.find("input[name='num']").val('1');
            tr.find("input[name='price']").val('0');
            amtInput.val(0);
            taxamtInput.val(0);
            calsum();
            return;
        }
            amtInput.val(amt);
            taxamtInput.val(taxamt);
            calsum();

    }
}

function calsum() {
    var sumtaxamt = 0, sumnetamt = 0;
    $("#table1").find("tr").each(function (i, n) {
        if (i == 0) {
            return true;
        }
        if ($(this).attr("id") == 'tfoot') return true;

        var tdArr = $(this).children();
        sumnetamt += Number(tdArr.eq(7).find('input').val());
        sumtaxamt += Number(tdArr.eq(8).find('input').val());
    });

    $("#sumnetamt").text(decimal(sumnetamt, 2));
    $("#sumtaxamt").text(decimal(sumtaxamt, 2));
}

function decimal(num, v) {
    var vv = Math.pow(10, v);
    return Math.round(num * vv) / vv;
}

function geren() {
    isdanwei = 0;
    $("#geren").addClass("am-active");
    $("#danwei").removeClass("am-active");
    $("#invoiceHeadEntText").val('个人');
    $("#invoiceHeadEntTaxno").val('');
    $("#invoiceHeadDzdh").val('');
    $("#invoiceHeadYhzh").val('');
    $("#invoiceHeadEntTaxno").attr("disabled", "disabled");
    $("#invoiceHeadDzdh").attr("disabled", "disabled");
    $("#invoiceHeadYhzh").attr("disabled", "disabled");
}

function danwei() {
    isdanwei = 1;
    $("#danwei").addClass("am-active");
    $("#geren").removeClass("am-active");
    $("#invoiceHeadEntText").val('');
    $("#invoiceHeadEntTaxno").removeAttr("disabled");
    $("#invoiceHeadEntText").removeAttr("disabled");
    $("#invoiceHeadDzdh").removeAttr("disabled");
    $("#invoiceHeadYhzh").removeAttr("disabled");
}

function add1() {
    var tr = $("#table1 tr:last");//合计行
    var add = $("#addnew").clone();//复制新行
    add.insertBefore(tr);
    calsum();
}

function add(e) {
    var a = $(e).parents("tr");//当前行
    var add = $("#addnew").clone();//新增一行
    add.insertAfter(a);
    a.next().find("input[name='goodsBarcode']").val('');
    a.next().find("input[name='goodsname']").val('');
    a.next().find("input[name='unit']").val('');
    a.next().find("input[name='num']").val('1');
    a.next().find("input[name='taxrate']").val('');
    a.next().find("input[name='price']").val('');
    a.next().find("input[name='netamt']").val('');
    a.next().find("input[name='taxamt']").val('');
    calsum();
}

function del(m) {
    m.parentNode.parentNode.remove();
    calsum();
}

function kaipiao() {
	
	params.sheettype = 0;
	params.requestBillItem=[];
	var ischeck = true;
	
    
    $("#table1").find("tr").each(function (i, n) {
        if (i == 0) {
            return true;
        }
        if ($(this).attr("id") == 'tfoot') return true;
        var tdArr = $(this).children();
        var item = {};
        item.goodsBarcode = tdArr.eq(1).find('input').val();//商品条码
        item.goodsname = tdArr.eq(2).find('input').val();//名称
        item.unit = tdArr.eq(3).find('input').val();//单位
        item.num = tdArr.eq(4).find('input').val();//数量
        item.price = tdArr.eq(5).find('input').val();//单价
        item.taxrate = tdArr.eq(6).find('input').val();//税率
        item.amt = tdArr.eq(7).find('input').val();//金额
        item.taxamt = tdArr.eq(8).find('input').val();//税额
        item.shopid = $("#shopid").val();

        if (item.goodsBarcode == null || item.goodsBarcode == '') {
            $.myAlert("请输入商品条码");
            ischeck = false;
            return false;
        }
        if (item.goodsname == null || item.goodsname == '') {
            $.myAlert("请输入名称");
            ischeck = false;
            return false;
        }
        if (item.num <=0) {
            $.myAlert("数量不能小于等于0");
            item.num =0;
            ischeck = false;
            return false;
        }


        if (item.price <0.1 || item.price >9999999) {
            $.myAlert("单价不能小于1角，不能大于9999999");
            item.price =9999999;
            ischeck = false;
            return false;
        }

        if (item.taxrate == null || item.taxrate == '') {
            $.myAlert("请输入税率");
            ischeck = false;
            return false;
        }

        if (item.taxrate<0 || item.taxrate >0.13) {
            $.myAlert("税率介于0到0.13之间");
            ischeck = false;
            return false;
        }

        if(item.shopid ==null||item.shopid ==''){
            $.myAlert("请选择门店");
            ischeck = false;
            return false;
        }
        params.shopid = $("#shopid").val();
        params.requestBillItem.push(item);
    });
    if (!ischeck) {
        return;
    }
    var invoiceHeadEntText = $("#invoiceHeadEntText").val();//公司名称，抬头
    var invoiceHeadEntTaxno = $("#invoiceHeadEntTaxno").val();//纳税人识别号
    var invoiceHeadDzdh = $("#invoiceHeadDzdh").val();//地址电话
    var invoiceHeadYhzh = $("#invoiceHeadYhzh").val();//银行账户
    var iqfplxdm = $('#iqfplxdm').val();
    var invRecvMail = $("#invRecvMail").val();//发票接收人邮箱
    var memo = $("#memo").val();//发票接收人邮箱

    if (invoiceHeadEntText == '' || invoiceHeadEntText == null) {
        $.myAlert("请输入单位名称");
        return;
    }
    //个人
    if (isdanwei == 0) {
    } else {
        if (invoiceHeadEntTaxno == null || invoiceHeadEntTaxno == '') {
            $.myAlert("请输入纳税人识别号");
            return;
        }
    }
    if (!$('#div_info').validator("isFormValid")) return;

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
	requestBill.memo=memo;
	requestBill.requestInvoicePreviewItem = [];
	$.myAjax({
		url:$.getService("zbapi","ZBgetInvoiceBillInfo"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
            $('#my-yulan').modal();
            params.action="update";
			params.sheetid=dataJson.data.sheetid;
			timepre(requestBill);
            $("#invRecvMail").val("");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
//
	$("#btn_submit").removeAttr("disabled");
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
			}else{
                //把预览的数据存下来，正式提交发票时要用到
                previewData = dataJson.data;
                $.myRowHTML(dataJson.data,"rowPreview","preview",1);
                //写入明细信息
                for ( var i = 0; i < dataJson.data.length; i++) {
                    $.myRowHTML(dataJson.data[i].invoicePreviewItem,"rowPreviewDetail","previewDetail"+i,2);
                }
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
//			$("#btn_print").removeAttr("disabled");
			params.action="";
			params.sheetid="";
			
			//跳到发票查看页面
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

function wkpiaomo() {
    $('#wkpiao').modal({width: '800', height: '500'});
    $("#startdate").datepicker();
    $("#enddate").datepicker();
    dangqianbtn();
    wkpiao(0);
}

function wkpiao(page) {
    var params = {};
    params.page = page;
    params.pagesize = 8;

    params.startdate = $("#startdate").val();
    params.enddate = $("#enddate").val();
    $.myAjax({
        url: $.getService("api", "getBillsalereport"),
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(params),
        success: function (dataJson) {
            if (dataJson.code != 0) {
                $.myAlert(dataJson.message);
                return;
            }
            for (var i = 0; i < dataJson.data.length; i++) {
                dataJson.data[i].sdate = TimeObjectUtil.longMsTimeConvertToDate(dataJson.data[i].sdate);
            }
            $.myRowHTML(dataJson.data, "Billsalereport", "searchBillsalereport");
            $.myPage(page, {
                rows: dataJson.count, pagesize: 8, targetId: "page1", jump: function (page) {
                    wkpiao(page);
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:" + textStatus + " msg:" + jqXHR.responseText);
        }
    });
}

function wkpiaoins() {
    var selectrows = $("#wkptable input[type='checkbox']:checked");
    if (selectrows == null || selectrows == '') {
        return;
    }

    for (var i = 0; i < selectrows.length; i++) {
        var add = $("#addnew").clone();
        $("#table1").append(add);
        var tdArr = $("#table1 tr:last").children();
        tdArr.eq(1).find('input').val(selectrows[i].value.split(",")[0]);//税目
        tdArr.eq(2).find('input').val(selectrows[i].value.split(",")[1]);//名称
        tdArr.eq(4).find('input').val(selectrows[i].value.split(",")[2]);//数量
        tdArr.eq(6).find('input').val(selectrows[i].value.split(",")[3]);//含税金额
        tdArr.eq(7).find('input').val(selectrows[i].value.split(",")[4]);//税率
    }

    $('#wkpiao').modal('close');
}

function dangqianbtn() {
    $("#startdate").datepicker('setValue', new Date());
    $("#enddate").datepicker('setValue', new Date());
}

function getGoodsBarcodes(e) {
    var tr = $(e).parents("tr");
    var params = {};
    params.goodsBarcode = tr.children().eq(1).find('input').val();

    $.myAjax({
        type: 'POST',
        dataType: "json",
        url: $.getService("ui", "GetGoodsBarcodes"),
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(params),
        success: function (dataJson) {
            var html = "";
            if (dataJson.code == '1') {
                for (var i = 0; i < dataJson.data.length; i++) {
                    html += "<option>" + dataJson.data[i].goodsBarcode + "</option>";
                }
                $("[name='goodsBarcodes']").html(html);
            } else {
                $.myAlert("访问后台失败!");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:" + textStatus + " msg:" + jqXHR.responseText);
        }
    });


    if (params.goodsBarcode == '' || params.goodsBarcode == null) {
        tr.find("input[name='goodsname']").val('');
        tr.find("input[name='unit']").val('');
        tr.find("input[name='taxrate']").val('');
        tr.find("input[name='price']").val('');
        tr.find("input[name='netamt']").val('');
        tr.find("input[name='taxamt']").val('');
    } else {
        $.myAjax({
            type: 'POST',
            url: $.getService("ui", "GetGoodsInfoByBarcode"),
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (dataJson) {
                if (dataJson.code == '1') {
                    tr.find("input[name='goodsname']").val(dataJson.data.goodsName);
                    tr.find("input[name='unit']").val(dataJson.data.unit);
                    tr.find("input[name='taxrate']").val(dataJson.data.taxrate);
                }else if(dataJson.code == '2'){
                    tr.find("input[name='goodsname']").val('');
                    tr.find("input[name='unit']").val('');
                    tr.find("input[name='taxrate']").val('');
                    tr.find("input[name='price']").val('');
                    tr.find("input[name='netamt']").val('');
                    tr.find("input[name='taxamt']").val('');
                } else {
                    $.myAlert("访问后台失败!");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $.myAlert("访问后台失败! status:" + textStatus + " msg:" + jqXHR.responseText);
            }
        });
    }
}

function getGoodsNames(e) {
    var tr = $(e).parents("tr");
    var params = {};
    params.goodsName = tr.children().eq(2).find('input').val();

    $.myAjax({
        type: 'POST',
        url: $.getService("ui", "GetGoodsNames"),
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(params),
        success: function (dataJson) {
            var html = "";
            if (dataJson.code == '1') {
                for (var i = 0; i < dataJson.data.length; i++) {
                    html += "<option>" + dataJson.data[i].goodsName + "</option>";
                }
                $("[name='goodsnames']").html(html);
            } else {
                $.myAlert("访问后台失败!");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:" + textStatus + " msg:" + jqXHR.responseText);
        }
    });


    if (params.goodsName == '' || params.goodsName == null) {
        tr.find("input[name='goodsBarcode']").val('');
        tr.find("input[name='unit']").val('');
        tr.find("input[name='taxrate']").val('');
        tr.find("input[name='price']").val('');
        tr.find("input[name='netamt']").val('');
        tr.find("input[name='taxamt']").val('');
    } else {
        $.myAjax({
            type: 'POST',
            url: $.getService("ui", "GetGoodsInfoByName"),
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (dataJson) {
                if (dataJson.code == '1') {
                    tr.find("input[name='goodsBarcode']").val(dataJson.data.goodsBarcode);
                    tr.find("input[name='unit']").val(dataJson.data.unit);
                    tr.find("input[name='taxrate']").val(dataJson.data.taxrate);
                }else if(dataJson.code == '2'){
                    tr.find("input[name='goodsBarcode']").val('');
                    tr.find("input[name='unit']").val('');
                    tr.find("input[name='taxrate']").val('');
                    tr.find("input[name='price']").val('');
                    tr.find("input[name='netamt']").val('');
                    tr.find("input[name='taxamt']").val('');
                }else {
                    $.myAlert("访问后台失败!");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $.myAlert("访问后台失败! status:" + textStatus + " msg:" + jqXHR.responseText);
            }
        });
    }
}


function getShop() {
    $.myAjax({
        type: 'POST',
        url: "/efs-portal/rest/ui/main/getMyShopList.action?",
        success: function (text) {
            var getData = JSON.parse(text);
            var data = getData.data;
            var html = "<option value=''>请选择门店</option>";
            for (var i = 0; i < data.length; i++) {
                html += "<option value='" + data[i].shopid + "'>" + data[i].shopname + "</option>";
            }
            $("#shopid").html(html);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:" + textStatus + " msg:" + jqXHR.responseText);
        }
    });
    
}


