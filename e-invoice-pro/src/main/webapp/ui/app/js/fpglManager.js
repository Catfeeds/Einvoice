var user;
var channel = "app";
$(document).ready(function(){
	//search(0);
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
			url:$.getService("ui","getInvoiceHead"),
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
	var statusmsg = selectrow[0].value.split(",")[4];
	var invoicetype = selectrow[0].value.split(",")[5];
	data.iqseqno=iqseqno;
	data.yfpdm=yfpdm;
	data.yfphm=yfphm;

    if(statusmsg=='已红冲'||statusmsg=='90'){
        $.myAlert("此发票已红冲，不能再红冲！");
        return;
    }
    if(statusmsg=='已作废'||statusmsg=='99'){
        $.myAlert("此发票已作废，不能再红冲！");
        return;
    }
    if(invoicetype=='红冲票'||invoicetype=='1'){
        $.myAlert("此发票已红冲，不能再红冲！");
        return;
    }

	$.myAjax({
		url:$.getService("api","hongchongInvoice"),
		data:JSON.stringify(data),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			$.myAlert("红冲成功");
			search(0);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
function zuofei(){
	$.myConfirm({title:"",msg:"确定要作废吗?",onConfirm:function(){zuofei1();}});
}
function zuofei1(){
	var data={};
	var selectrow = $("#fptable input[type='checkbox']:checked");
	if(selectrow == null || selectrow == ''|| selectrow == '0'){
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
	var statusmsg = selectrow[0].value.split(",")[4];
	var invoicetype = selectrow[0].value.split(",")[5];
    if (!(lxdm == '专票' || lxdm == '004'|| lxdm =='普票' || lxdm =='007')){
        $.myAlert("此发票不能作废！");
        return;
    }
    if(statusmsg=="已红冲"||statusmsg=="90"||statusmsg=="已作废"||statusmsg=="已红冲"=="99"){
        $.myAlert("此发票不能作废！");
        return;
    }
    if(invoicetype=="红冲票"||invoicetype=="1"){
        $.myAlert("此发票已红冲，不能作废！");
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
			$.myAlert("作废成功");
			search(0);
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
			var datavalue =JSON.stringify(dataJson.data.invque.data);
			//创建Form
	/*	    var submitfrm = document.form1;
		    submitfrm.action = dataJson.data.invque.data.taxprintinfo.fpprintUrl;
		    document.form1.templetName.value="invoice.jrxml";
		    document.form1.printerName.value="HP LaserJet M3027 mfp PCL6";
		    document.form1.data.value=JSON.stringify(datavalue);
		    //submitfrm.method = "post";
		   // document.body.appendChild(submitfrm);
		    submitfrm.submit();*/
			if(dataJson.data.invque.data.taxprintinfo.fpprintUrl!=''&&dataJson.data.invque.data.taxprintinfo.fpprintUrl!=null){
			$.ajax({
                url:dataJson.data.invque.data.taxprintinfo.fpprintUrl,
                type:"post",
                data:"templetName="+dataJson.data.invque.data.taxprintinfo.fptempletName+"&printerName="+dataJson.data.invque.data.taxprintinfo.fpprinterName+"&data="+encodeURIComponent(datavalue),
                success:function(data){
                     alert(data.msg);
                },
                error:function(e){
                     alert(JSON.stringify(e));
                }
            });
			}else{
				$.myAlert("打印成功");
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
			$.myAlert("打印成功");
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