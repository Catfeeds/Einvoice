var user;
var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
var print_iqsen;
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
});

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
	data.sheettype = 1;
	if(data.shopid=='' ||data.syjid=='' ||data.billno=='' ){
		return;
	}
	str = shopid+"-"+syjid+"-"+billno;
	data.ticketQC = str;
	
		$.myAjax({
			url:$.getService("api","getInvoiceBillInfoWithDetail"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				if(dataJson.data.flag == 0){
					var weilist = $("#weilist input[type=checkbox]");
					$.each( weilist, function(i, n){
						var m = $(n);
						if(dataJson.data.sheetid == m.val()){
							$.myAlert("已经查询过该小票");
							issearch = false;
							return false;
						}
					});
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
	}

function geren(){
	isdanwei=0;
	$("#geren").addClass("am-active");
	$("#danwei").removeClass("am-active");
	$("#invoiceHeadEntText").val('个人');
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
	
	if(isNaN(dqfpdm) || isNaN(dqfphm)){
		$.myAlert("请先查询空白票");
		return ;
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
			var oo = v.split('-');
			var item = {};
			item.shopid = oo[0];
			item.syjid = oo[1];
			item.billno = oo[2];
			item.sheetid=item.shopid+"-"+item.syjid+"-"+item.billno;
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
	requestBill.sheettype = 1;
	requestBill.iqpayee=$("#iqpayee").val();
	requestBill.iqchecker=$("#iqchecker").val();
	requestBill.iqmemo=$("#remark").val();
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
			//alert(dataJson.data[0].iqmsg);
			if(previewData[0].gmfNsrsbh != null && previewData[0].gmfNsrsbh != ''){
				updateCustomerInfo();
			}
			if(dataJson.data[0].iqmsg!=''&&dataJson.data[0].iqmsg!=null){
					ww =  sk.Operate(dataJson.data[0].processMsg);
				var json;
				ret = sk.Operate(dataJson.data[0].iqmsg);
				
				var xotree = new XML.ObjTree(); 
				 json = xotree.parseXML(ret);  					//  alert(JSON.stringify(json.business.body.returndata));
				if(json.business.body.returncode!=0){
					alert(json.business.body.returnmsg);
				}
				/*ret ="<?xml version=\"1.0\" encoding=\"gbk\"?>"+
				"<business id=\"10008\" comment=\"发票开具\">"+
				"<body yylxdm=\"1\">"+
				"<returncode>0</returncode>"+
				"<returnmsg>返回信息</returnmsg>"+
				"<returndata>"+
				"<fpdm>发票代码</fpdm>"+
				"<fphm>发票号码</fphm>"+
				"<kprq>开票日期</kprq>"+
				"<skm>税控码</skm>"+
				"<jym>校验码</jym>"+
				"<ewm>二维码</ewm>"+
				"</returndata>"+
				"</body>"+
				"</business>";*/
				dataJson.data[0].processMsg=ret;
				
				retpapiao(dataJson.data[0]);
				
			}else{
				$.myAlert("生成发票报文有问题！");
			}
			previewData = {};
			var iqseqno = dataJson.data[0].iqseqno;
			print_iqsen=iqseqno;
			$.myAlert("开票成功,请放入发票纸打印");
			$('#btn_submit').attr("disabled","disabled");
			$("#btn_print").removeAttr("disabled");
			searchKB();
			$("#row1").html("");
			$("#row2").html("");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function retpapiao(que){
	 $.myAjax({
			url:$.getService("api","retpapiao"),
			data:JSON.stringify(que),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}

			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
}

function searchKB(){
	var iqfplxdm = $('#iqfplxdm').val();
	var data ={};
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
/*				ret = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
						"<business id=\"10004\" comment=\"查询当前未开票号\">"+
						"<body yylxdm=\"1\">"+
						"<returncode>0</returncode>"+
						"<returnmsg>返回信息</returnmsg>"+
						"<returndata>"+
						"<dqfpdm>1110000478</dqfpdm>"+
						"<dqfphm>82460687</dqfphm>"+
						"</returndata>"+
						"</body>"+
						"</business>";*/
				
				 var json;
	 
				if(dataJson.data.dqfpdm!=''){
				ww=	sk.Operate(dataJson.data.dqfphm);
				 
			  ret = sk.Operate(dataJson.data.dqfpdm);
		 
				var xotree = new XML.ObjTree(); 
					 json = xotree.parseXML(ret);  					//  alert(JSON.stringify(json.business.body.returndata));

				}
				$.myRowHTML([json.business.body.returndata],"fpdmmo","fpdm");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
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
			sk.Operate(dataJson.message);
			var ret =sk.Operate(dataJson.data.invque.iqmsg);
			 var xotree = new XML.ObjTree();
/*			 ret = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
					"<business id=\"20004\" comment=\"发票打印\">"+
					"<body yylxdm=\"1\">"+
					"<returncode>0</returncode>"+
					"<returnmsg>返回信息</returnmsg>"+
					"</body>"+
					"</business>";*/
			 
			var json = xotree.parseXML(ret); 
			if(json.business.body.returncode==0){
				if(dataJson.data.isqingdan != '' && dataJson.data.isqingdan == 1){
					$.myConfirm({title:"提醒",msg:"请放入清单纸",onConfirm:fp_printforQd});
				}else{
					$('#my-yulan').modal('close');
					window.location.href=window.location.href;
				}
			}else{
				$.myAlert(json.business.body.returnmsg);
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
			sk.Operate(dataJson.message);
			var ret =sk.Operate(dataJson.data.invque.iqmsg);
/*			 var xotree = new XML.ObjTree();
			 ret = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
					"<business id=\"20004\" comment=\"发票打印\">"+
					"<body yylxdm=\"1\">"+
					"<returncode>0</returncode>"+
					"<returnmsg>返回信息</returnmsg>"+
					"</body>"+
					"</business>";
			 
			var json = xotree.parseXML(ret); 
			if(json.business.body.returncode==0){
				$.myAlert("打印成功");
			}else{
				$.myAlert(json.business.body.returnmsg);
			}*/
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
}

function zuofei(){
	var data={};
	var iqfplxdm = $('#iqfplxdm').val();
	var yfpdm = $("#kbfpdm").text();//空白发票代码
	var yfphm = $("#kbfphm").text();//空白发票号码
	data.zflx=0;
	data.iqfplxdm=iqfplxdm;//发票类型（如普票）
	data.yfpdm = yfpdm;
	data.yfphm = yfphm;
	$.myAjax({
		url:$.getService("api","fp_zuofei"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
					sk.Operate(dataJson.data.rtinvoiceBean.fphm);
			var ret =sk.Operate(dataJson.data.rtinvoiceBean.fpdm);
			 var xotree = new XML.ObjTree();
			/* ret= "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
					"<business id=\"10009\" comment=\"发票作废\">"+
					"<body yylxdm=\"1\">"+
					"<returncode>0</returncode>"+
					"<returnmsg>返回信息</returnmsg>"+
					"<returndata>"+
					"<fpdm>037001751507</fpdm>"+
					"<fphm>00100001</fphm>"+
					"<zfrq>20180514130240</zfrq>"+
					"</returndata>"+
					"</body>"+
					"</business>";*/
			 
			var json = xotree.parseXML(ret); 
			if(json.business.body.returncode=='0'){

				$.myAlert("作废成功");
			}else{
				$.myAlert(json.business.body.returnmsg);
			}
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function loadXml(str) {
	  if (str == null) {
	   return null;
	  }
	  var doc = str;
	  try{
	   doc = createXMLDOM();
	   doc.async = false;
	   doc.loadXML(str);
	  }catch(e){
	   doc = $.parseXML(str);
	  }
	  return doc;
	}

/**
 * 将XML的Document对象转换为JSON字符串
 * @param xmlDoc xml的Document对象
 * @return string
 */
function convertToJSON(xmlDoc) {
    //准备JSON字符串和缓存（提升性能）
    var jsonStr = "";
    var buffer = new Array();
 
    buffer.push("{");
    //获取xml文档的所有子节点
    var nodeList = xmlDoc.childNodes;
 
    generate(nodeList);
 
    /**
     * 中间函数，用于递归解析xml文档对象，并附加到json字符串中
     * @param node_list xml文档的的nodeList
     */
    function generate(node_list) {
 
        for (var i = 0; i < node_list.length; i++) {
            var curr_node = node_list[i];
            //忽略子节点中的换行和空格
            if (curr_node.nodeType == 3) {
                continue;
            }
            //如果子节点还包括子节点，则继续进行遍历
            if (curr_node.childNodes.length > 1) {
                buffer.push("\"" + curr_node.nodeName + "\": {");
                generate(curr_node.childNodes);
            } else {
                var firstChild = curr_node.childNodes[0];
 
                if (firstChild != null) {
                    //nodeValue不为null
                    buffer.push("\"" + curr_node.nodeName + "\":\"" + firstChild.nodeValue + "\"");
                } else {
                    //nodeValue为null
                    buffer.push("\"" + curr_node.nodeName + "\":\"\"");
                }
 
            }
            if (i < (node_list.length - 2)) {
                buffer.push(",");
            } else {
                break;
            }
        }
        //添加末尾的"}"
        buffer.push("}");
    }
 
    jsonStr = buffer.join("");
    return jsonStr;
}