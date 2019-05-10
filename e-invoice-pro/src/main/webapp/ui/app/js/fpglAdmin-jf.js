var user;
var channel = "app";
$(document).ready(function(){
	search(0);
	loaddate();
});


function search(page){
	var data ={};
	data.page=page;
	var sheetid = $("#requestsheetid").val();
	var requestfphm = $("#requestfphm").val();
	var invoicetype = $("#invoicetype").val();
	data.startdate=$("#startdate").val();
	data.enddate=$("#enddate").val();
	if(sheetid !='' && sheetid!=null){
		data.sheetid=sheetid;
	}
	if(requestfphm !='' && requestfphm!=null){
		data.fphm=requestfphm;
	}
	if(invoicetype !='' && invoicetype!=null && invoicetype !='9'){
		data.invoicetype=invoicetype;
	}
	if(data.startdate !='' && data.startdate!=null){
		data.startdate=data.startdate.replace(/-/g,"");
	}
	if(data.enddate !='' && data.enddate!=null){
		data.enddate=data.enddate.replace(/-/g,"")+"9999";
	}
		$.myAjax({
			url:$.getService("ui","getInvoiceHeadByAdmin"),
			data:JSON.stringify(data),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				for (var i=0;i<dataJson.data.length;i++){
					if(dataJson.data[i].fprq != ''){
						dataJson.data[i].fprq=dataJson.data[i].fprq.substr(0,4)+"-"+dataJson.data[i].fprq.substr(4,2)+"-"+dataJson.data[i].fprq.substr(6,2);
					}
					if(dataJson.data[i].invoicetype == '0'){
						dataJson.data[i].invoicetype="蓝票";
					}else if(dataJson.data[i].invoicetype == '1'){
						dataJson.data[i].invoicetype="红冲票";
					}
					if(dataJson.data[i].status==90){
						dataJson.data[i].statusmsg='已红冲';
					}else if(dataJson.data[i].status==99){
						dataJson.data[i].statusmsg='已作废';
					}else{
						dataJson.data[i].statusmsg='正常';
					}
				}
				$.myPage(page,{rows:dataJson.count,jump:function(page){
					search(page);
				}});
				$.myRowHTML(dataJson.data,"rowmo","row1");
				$("#pagecount").html("共" +dataJson.count+ "条数据");
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}


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
	$.myAjax({
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
			$.myRowHTML(dataJson.data,"rowmo2","searchRows");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function hongchong(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条数据");
		 return;
	}
	var iqseqno = selectrow[0].value.split(",")[0];
	var yfpdm = selectrow[0].value.split(",")[1];
	var yfphm = selectrow[0].value.split(",")[2];
	data.iqseqno=iqseqno;
	data.yfpdm=yfpdm;
	data.yfphm=yfphm;
	
	$.myAjax({
		url:$.getService("api","hongchongInvoice"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			if(dataJson.data.iqmsg!=''&&dataJson.data.iqmsg!=null){
			
				ww =  sk.Operate(dataJson.data.processMsg);
			ret = sk.Operate(dataJson.data.iqmsg);
		
			/*ret ="<?xml version=\"1.0\" encoding=\"gbk\"?>"+
			"<business id=\"10008\" comment=\"发票开具\">"+
			"<body yylxdm=\"1\">"+
			"<returncode>0</returncode>"+
			"<returnmsg>返回信息</returnmsg>"+
			"<returndata>"+
			"<fpdm>1111111111</fpdm>"+
			"<fphm>11111111</fphm>"+
			"<kprq>开票日期</kprq>"+
			"<skm>税控码</skm>"+
			"<jym>校验码</jym>"+
			"<ewm>二维码</ewm>"+
			"</returndata>"+
			"</body>"+
			"</business>";*/
			dataJson.data.processMsg=ret;
			retpapiao(dataJson.data);
			$.myAlert("红冲成功");
			search(0);
		}else{
			$.myAlert("生成开票报文有问题！");
		}

		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
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

function zuofei(){
	$.myConfirm({title:"",msg:"确定要作废吗?",onConfirm:function(){zuofei1();}});
}
function zuofei1(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条数据");
		 return;
	}
	var iqseqno = selectrow[0].value.split(",")[0];
	var yfpdm = selectrow[0].value.split(",")[1];
	var yfphm = selectrow[0].value.split(",")[2];
	var lxdm = selectrow[0].value.split(",")[3];
	if(lxdm=='电子票' || lxdm=='026'){
		$.myAlert("电子票不能作废");
		 return;
	}
	data.iqseqno=iqseqno;
	data.yfpdm=yfpdm;
	data.yfphm=yfphm;
	data.zflx=1;
	$.myAjax({
		url:$.getService("api","fp_zuofei"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			 
			var ww =sk.Operate(dataJson.data.rtinvoiceBean.fphm);
			 
			var ret =sk.Operate(dataJson.data.rtinvoiceBean.fpdm);
			 
			 var xotree = new XML.ObjTree();
/*			 ret= "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
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
				$.myAjax({
					url:$.getService("api","retzuofei"),
					data:JSON.stringify(dataJson.data.invque),
					contentType:"text/html;charset=UTF-8",
					success: function (dataJson) {
						if(dataJson.code!=0){
							$.myAlert(dataJson.message);
							return;
						}
						$.myAlert("作废成功");
						search(0);
					},
					error: function (jqXHR, textStatus, errorThrown) {
						$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
					}
				});
			}else{
				$.myAlert(json.business.body.returnmsg);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function re_print(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''){
		return;
	}
	if(selectrow != null && selectrow.length >1){
		$.myAlert("只能选择一条数据");
		 return;
	}
	var iqseqno = selectrow[0].value.split(",")[0];
	data.iqseqno=iqseqno;
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
				$.myAlert("打印成功");
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
	var selectrow = $("#fptable input[type='checkbox']:checked");
	var iqseqno = selectrow[0].value.split(",")[0];
	data.iqseqno=iqseqno;
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
				$.myAlert("打印成功");
			}else{
				$.myAlert(json.business.body.returnmsg);
			}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("清单打印错误! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}

function showFapiao(url){
	if(url != null && url != ''){
		var link= $('<a href="'+url+'" target="_blank"></a>');
		link.get(0).click();
	}
}

function loaddate(){ 
	var now = new Date;
	var now1=new Date;
	var now2=new Date;
	var enddate=TimeObjectUtil.longMsTimeConvertToDate(now);
	var mindate=TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now.getDate() - 192));
	$("#enddate").datepicker({//添加日期选择功能  
	    showButtonPanel:true,//是否显示按钮面板  
	    dateFormat: 'yy-mm-dd',//日期格式  
	    clearText:"清除",//清除日期的按钮名称  
	    closeText:"关闭",//关闭选择框的按钮名称  
	    yearSuffix: '年', //年的后缀  
	    showMonthAfterYear:true,//是否把月放在年的后面  
	    defaultDate:enddate,//默认日期  
	    minDate:mindate,//最小日期  
	    maxDate:enddate,//最大日期  
	    onSelect: function(selectedDate) {//选择日期后执行的操作  
	    }  
	    });
    $("#startdate").datepicker({//添加日期选择功能  
	    showButtonPanel:true,//是否显示按钮面板  
	    dateFormat: 'yy-mm-dd',//日期格式  
	    clearText:"清除",//清除日期的按钮名称  
	    closeText:"关闭",//关闭选择框的按钮名称  
	    yearSuffix: '年', //年的后缀  
	    showMonthAfterYear:true,//是否把月放在年的后面  
	    defaultDate:enddate,//默认日期  
	    minDate:mindate,//最小日期  
	    maxDate:enddate,//最大日期  
	    onSelect: function(selectedDate) {//选择日期后执行的操作  
	    }  
	    }); 
    
    $("#startdate").datepicker( "setDate", enddate );
	$("#enddate").datepicker( "setDate", enddate);
} 

function sendPdf(iqseqno,iqemail,lxdm){
	if(lxdm!='电子票') return;
	$.myPrompt(
		{msg:"请确认接收邮箱",title:"发送到邮箱",value:iqemail,onConfirm:function(e){
			var email = e.data;
			if(email==''){alert("邮箱不能空");return false;}
			var data ={};
			data.email = email;
			data.iqseqno = iqseqno;
			$.myAjax({
				url:$.getService("api","sendPdf"),
				data:JSON.stringify(data),
				contentType:"application/json;charset=UTF-8",
				success: function (dataJson) {
					if(dataJson.code!=0){
						$.myAlert(dataJson.message);
					}else{
						$.myAlert("发送中，稍后请在邮箱查收");
					}
				}
			});
		}
	});
}