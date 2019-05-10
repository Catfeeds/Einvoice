var channel = "app";
var isdanwei=0;//0个人，1单位
var previewData = {};
var params = {};
var wkplist;//查询出的未开票商品集合
var yxsplist = []; //已选择的开票商品；
var user;
params.channel = channel;
$(document).ready(function(){
	getuser();
	//验证
	$('#div_info').validator();
	//提交
	$("#btn_submit").click(function(){
		commit();
	});
	
	$.mySelect({
		tragetId:"shopname",
		id:"shopid",
		url:$.getService("ui","getShopList"),
		onChange:function(e){
			//console.log(e);
		}
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
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("没有获取用户信息! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
}
var sea;
var selectTr;

function add(){
    var tr=$("#table1 tr:last");
    
    var add=$("#addnew").clone();
    $("#table1").append(add);
 }
function del(m){
	m.parentNode.parentNode.remove(); 
}
function kaipiao(){
	params.sheettype = 0;
	params.requestBillItem=[];
	var ischeck = true;
	
	$("#table1").find("tr").each(function(i, n){
		 if(i==0){return true;}
        var tdArr = $(this).children();
        var item = {};
        item.taxitemid = tdArr.eq(1).find('input').val();//税目
        item.goodsname = tdArr.eq(2).find('input').val();//名称
        item.unit = tdArr.eq(3).find('input').val();//单位
        item.qty = tdArr.eq(4).find('input').val();//数量
        item.price = tdArr.eq(5).find('input').val();//单价
        item.amt = tdArr.eq(6).find('input').val();//含税金额
        item.taxrate = tdArr.eq(7).find('input').val();//税率
        item.goodsid = tdArr.find("input[name='goodsid']").val();
        item.tradeDate=tdArr.find("input[name='tradeDate']").val();
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
        if(parseFloat(item.amt) > parseFloat(tdArr.find("input[name='unusedamt']").val())){
        	$.myAlert("输入金额超出可开票金额");
        	ischeck=false;
        	return false;
        }
        if(parseFloat(item.qty) > parseFloat(tdArr.find("input[name='unuseqty']").val())){
        	$.myAlert("输入数量超出可开票数量");
        	ischeck=false;
        	return false;
        }
        if(item.taxrate == null || item.taxrate == ''){
        	$.myAlert("请输入税率");
        	ischeck=false;
        	return false;
        }
/*        if(item.tradeDate != ''){
        	var trad=item.tradeDate.substr(0,4)+"-"+item.tradeDate.substr(4,2)+"-01"
        	item.tradeDate=trad;
        }*/
        var syamt =item.amt;
       	for (var x=0;x<yxsplist.length;x++){
       		
       		if( item.goodsid == yxsplist[x].goodsid){
       			if(syamt>0){
       				var itemclone=cloneObj(item);
	       			if(syamt>(yxsplist[x].amt -yxsplist[x].useamt)){
	       				syamt = syamt -(yxsplist[x].amt -yxsplist[x].useamt);
	       				itemclone.amt = yxsplist[x].amt -yxsplist[x].useamt;
	       				itemclone.qty = (yxsplist[x].amt -yxsplist[x].useamt)/item.price;
	       				itemclone.price = item.price;
	       		        	var trad=yxsplist[x].trademonth.substr(0,4)+"-"+yxsplist[x].trademonth.substr(4,2)+"-01"
	       		        	itemclone.tradeDate=trad;
	       		        params.requestBillItem.push(itemclone);
	       		        
	       			}else{
	       				itemclone.amt =syamt;
	       				syamt =0;
	       				itemclone.qty = itemclone.amt/item.price;
	       				itemclone.price = item.price;
	       		        	var trad=yxsplist[x].trademonth.substr(0,4)+"-"+yxsplist[x].trademonth.substr(4,2)+"-01"
	       		        	itemclone.tradeDate=trad;
	       		        params.requestBillItem.push(itemclone); 	
	       			}
       			}
       		}
    	
		}
       // params.requestBillItem.push(item);
    });
	
	if(!ischeck){
		return;
	}
	if(!jsprice){
		$.myAlert("商品单价不在可开票范围内");
		return;
	}
	$.myAjax({
		url:$.getService("api","getInvoiceBillInfo"),
		data:JSON.stringify(params),
		contentType:"text/html;charset=UTF-8",
		success: function (dataJson) {
			if(dataJson.code!=0){
				$.myAlert(dataJson.message);
				return;
			}
			params.isLock=1;
			params.sheetid=dataJson.data.sheetid;
			
			 //创建Form
            var submitfrm = document.createElement("form");
            var token = "&token="+$.getUrlParam("token");
            submitfrm.action = "/e-invoice-pro/rest/api/download?sheetid="+params.sheetid+token;
            submitfrm.method = "post";
            document.body.appendChild(submitfrm);
            submitfrm.submit();
            setTimeout(function () {
                submitfrm.parentNode.removeChild(submitfrm);
                params.sheetid="";
                window.location.href=window.location.href;
            }, 2000);
           
           
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	});
	$("#btn_submit").removeAttr("disabled");
}

function wkpiaomo(){
	$('#wkpiao').modal({width:'900',height:'880'});
	loaddate();
	$("#shopid").val(user.shopid);
	$('select').trigger('changed.selected.amui');
	$("#shopid").selected('disable');
	wkpiao(0);
}
function wkpiao(page){
	var params={};
	params.page=page;
	params.pagesize=20;
	params.shopid=$("#shopid").val();
	params.sdate=$("#startdate").val();
	params.edate=$("#enddate").val();
	params.goodsname=$("#goodsname").val();
	$.myAjax({
		url: "/e-invoice-pro/rest/api/querySheetStat",
		contentType:"application/json;charset=UTF-8",
		data:JSON.stringify(params),
		success: function (dataJson) {
			if(dataJson.code != 0){
				$.myAlert(dataJson.message);
				return;
			}
			wkplist = dataJson.data;
			for (var i=0;i<dataJson.data.length;i++){
				dataJson.data[i].unuseqty=parseFloat(dataJson.data[i].qty-dataJson.data[i].useqty).toFixed(2);
				dataJson.data[i].unusedamt=parseFloat(dataJson.data[i].amt-dataJson.data[i].useamt).toFixed(2);
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
		var flag = true;
		$("#table1").find("tr").each(function(j, n){
			 if(j==0){return true;}
	        var tdArr = $(this).children();
	        var goodsid;
	        goodsid = tdArr.find("input[name='goodsid']").val();
	        if(goodsid == (selectrows[i].value.split(",")[1])){
	        	flag = false;
	        	return false;
	        }
	    });
				
		if(flag){
			 
			var unuseramt=0;
			var unuserqty=0;
			
			for (var x=0;x<wkplist.length;x++){
				 if(wkplist[x].goodsid == selectrows[i].value.split(",")[1]){
					 unuserqty += parseFloat(wkplist[x].qty -wkplist[x].useqty);
					 unuseramt += parseFloat(wkplist[x].amt -wkplist[x].useamt);
					 yxsplist.push(wkplist[x]);
				 }
			}
		
		
			var add=$("#addnew").clone();
			$("#table1").append(add);
			var tdArr=$("#table1 tr:last").children();
			
		 	tdArr.eq(1).find('input').val(selectrows[i].value.split(",")[0]);//税目
	        tdArr.eq(2).find('input').val(selectrows[i].value.split(",")[2]);//名称
	        tdArr.eq(4).find('input').val(parseFloat(unuserqty).toFixed(2));//数量
	        tdArr.eq(5).find('input').val(parseFloat(selectrows[i].value.split(",")[10]).toFixed(2));//单价
	        tdArr.eq(6).find('input').val(parseFloat(unuseramt).toFixed(2));//剩余金额
	        tdArr.eq(7).find('input').val(selectrows[i].value.split(",")[3]);//税率
	        tdArr.find("input[name='goodsid']").val(selectrows[i].value.split(",")[1]);
	        tdArr.find("input[name='unusedamt']").val(parseFloat(unuseramt).toFixed(2));
	        tdArr.find("input[name='unuseqty']").val(parseFloat(unuserqty).toFixed(2));
	        tdArr.find("input[name='tradeDate']").val(selectrows[i].value.split(",")[9]);
	        tdArr.find("input[name='hidprice']").val(parseFloat(selectrows[i].value.split(",")[10]).toFixed(2));
		}
	}
	
	$('#wkpiao').modal('close');
}

var jsprice=true;
function jisuan(e){
	var selectTr=$(e).parents("tr");
	var hidprice = selectTr.find("input[name='hidprice']").val();
	var qty = selectTr.find("input[name='qty']").val();
	var amt = selectTr.find("input[name='amt']").val();
	if(isNaN(qty)){
		$.myAlert("输入的数量不是数字");
		return;
	}
	if(isNaN(amt)){
		$.myAlert("输入的金额不是数字");
		return;
	}
	var newprice=(parseFloat(amt)/parseFloat(qty)).toFixed(2);
	if(newprice > parseFloat(hidprice)){
		$.myAlert("您开票的单价高于商品的售价");
		jsprice=false;
	}else if(newprice <parseFloat(hidprice)*0.95){
		$.myAlert("您开票的单价低于商品售价的95折");
		jsprice=false;
	}else{
		jsprice=true;
	}
	
	selectTr.find("input[name='price']").val(newprice);
}

function loaddate(){ 
	var now = new Date;
	var now1=new Date;
	var now2=new Date;
	var enddate=TimeObjectUtil.longMsTimeConvertToDate(now1.setDate(now1.getDate() - 10));
	var mindate=TimeObjectUtil.longMsTimeConvertToDate(now.setDate(now.getDate() - 92));
	var defultdate=TimeObjectUtil.longMsTimeConvertToDate(now2.setDate(now2.getDate() - 61));
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
    
    $("#startdate").datepicker( "setDate", defultdate );
	$("#enddate").datepicker( "setDate", enddate);
} 

var cloneObj = function (obj) {  
    var newObj = {};  
    if (obj instanceof Array) {  
        newObj = [];  
    }  
    for (var key in obj) {  
        var val = obj[key];  
        //newObj[key] = typeof val === 'object' ? arguments.callee(val) : val; //arguments.callee 在哪一个函数中运行，它就代表哪个函数, 一般用在匿名函数中。  
        newObj[key] = typeof val === 'object' ? cloneObj(val): val;  
    }  
    return newObj;  
}; 