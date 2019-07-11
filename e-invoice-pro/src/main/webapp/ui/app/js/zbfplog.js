var params = {};
var pagesize=5;
var channel = "app";

$(document).ready(function(){
	//search(0);
    loaddate();
   $("#btnSearch").click(function(event){
		search(0);
	});
});


function search(page){
	params.sheetid=$("#sheetid").val();
	params.invoicetype=$("#invoicetype").val();
    params.startdate=$("#startdate").val();
    params.enddate=$("#enddate").val();
	params.page=page;
	params.pagesize = pagesize;

    if(params.startdate !='' && params.startdate!=null){
        params.startdate=params.startdate.replace(/-/g,"");
    }
    if(params.enddate !='' && params.enddate!=null){
        params.enddate=params.enddate.replace(/-/g,"");
    }

	   $.myAjax({
		   url: $.getService("ui","getAllZBfplog?"+$.cookGetUrlParam(params)),
		   data:JSON.stringify(params),
		   contentType:"application/json;charset=UTF-8",
		   success: function (data) {

			   if(data.code == "0"){
				 //首次查询page
					$.myPage(page,{rows:data.count,pagesize:pagesize,jump:function(page){
						search(page);
					}});

					$.myRowHTML(data.data,"rowSample","rowTraget");
				}
			   if(data.count==0){
					$.myAlert("没有相关信息！");
					return;
				}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	  });


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