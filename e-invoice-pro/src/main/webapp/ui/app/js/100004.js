var params = {};
var pagesize=10;
var channel = "app";

$(document).ready(function(){
	// search(0);
	$("#btnSearch").click(function(event){
		search(0);
	});
});


function search(page){
	params.taxitemid = $("#taxitemId").val();
	params.taxitemname = $("#taxitemName").val();
	params.taxrate = $("#taxRate").val();
	params.page=page;
	params.pagesize = pagesize;

	if(params.taxitemid==""&&params.taxitemname==""&&params.taxrate==""){
        $.myAlert("至少输入一个查询条件才能查询！");
        return ;
	}

		$.myAjax({
			url: $.getService("ui","queryTaxitem?"+$.cookGetUrlParam(params)),			
			contentType:"application/json;charset=UTF-8",
			data:JSON.stringify(params),
		    success: function (data) { 

			   if(data.code == "0"){
				    //首次查询page
				$.myPage(page,{rows:data.count,pagesize:pagesize,jump:function(page){
					search(page);
				}});
					$.myRowHTML(data.data,"rowSample","rowTraget");
				}
                if(data.count==0){
                    $.myAlert("没有该税目信息！");
                    return;
                }
		},error: function (jqXHR, textStatus, errorThrown) {
                $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
            }
	  });	
}

$(function() {
	  $('#btnNew').on('click', function() {

	    $('#my-add').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.taxversion = $("#taxversion").val();
	    	  params.taxitemid = $("#taxitemid").val();
	    	  params.taxitemname = $("#taxitemname").val();
	    	  params.taxrate = $("#taxrate").val();
	    	  params.taxitemmemo = $("#taxitemmemo").val();
	    	  params.taxitemkey = $("#taxitemkey").val();
	    	  params.taxitemmode = $("#taxitemmode").val();
    	    
		    if(params.taxitemid==""){
		    	$.myAlert("税目代码不能为空！");
		    	return false;
		    }
		    if(params.taxitemid.length!=19){
                $.myAlert("税目代码位数不对！");
                return false;
			}
		    if(params.taxversion==""){4
		    	$.myAlert("版本号不能为空！");
		    	return false;
		    }
		    if(params.taxitemname==""){
		    	$.myAlert("税目名称不能为空！");
		    	return false;
		    }
		    if(params.taxrate==""){
		    	$.myAlert("税率不能为空！");
		    	return false;
		    }
		    if(params.taxrate<0||params.taxrate>=1){
                $.myAlert("税率必须大于等于0小于1！");
                return false;
			}
			if(isNaN(params.taxrate)){
                $.myAlert("税率必须数字并且大于等于0小于1！");
                return false;
			}
              if(params.taxitemmode==""){
                  $.myAlert("请选择是否汇总！");
                  return false;
              }

	        $.myAjax({
	    		url: $.getService("ui","insertTaxitem?"+$.cookGetUrlParam(params)),			
	    		contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) { 

	     		   if(data.code == "0"){
	     			  $.myAlert('新增成功！');
                      var taxitemid=params.taxitemid;
                       addsuccess(taxitemid);
				     $("#taxitemid").val("");
				     $("#taxitemname").val("");
				     $("#taxrate").val("");
				     $("#taxitemmemo").val("");
				     $("#taxitemkey").val("");
				     $("#taxitemId").val("");
                     $("#taxitemName").val("");
                     $("#taxRate").val("");
	     			}else if(data.code == "-2"){
                       $.myAlert("此税目代码已经添加过，如需更改请在编辑模块进行编辑！");
				   }
	     			else{
	    				$.myAlert(data.msg,'信息');
	    			}
	     	    },error: function (jqXHR, textStatus, errorThrown) {
                    $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
                }
	        });        
	      },
	      onCancel: function(e) {
	            $("#taxitemid").val("");
		    	$("#taxitemname").val("");
		    	$("#taxrate").val("");
		    	$("#taxitemmemo").val("");
		    	$("#taxitemkey").val("");
     }
	    });
	  });

    $.mySelect({
        tragetId:"selectA",
        id:"taxitemmode",
        url:$.getService("ui","getLookupSelect?lookupid=7&"+$.cookGetUrlParam(params)),
        onChange:function(e){
            //console.log(e);
        }
    });
	 
	});


function dataEdit(taxitemid) {
    $.mySelect({
        tragetId:"selectB",
        id:"taxitemmode2",
        url:$.getService("ui","getLookupSelect?lookupid=7&"+$.cookGetUrlParam(params)),
        onChange:function(e){
            //console.log(e);
        }
    });

	params.taxitemid=taxitemid;
	 $.myAjax({
 		url: $.getService("ui","getTaxitemByTaxitemId?"+$.cookGetUrlParam(params)),
 		contentType:"application/json;charset=UTF-8",
	    data: JSON.stringify(params),
	    success: function (data) { 
	    	console.log(data.data);
	    $("#taxversion2").val(data.data.taxversion);
		$("#taxitemid2").val(data.data.taxitemid);
		$("#taxitemname2").val(data.data.taxitemname);
		$("#taxrate2").val(data.data.taxrate);
		$("#taxitemmemo2").val(data.data.taxitemmemo);
		$("#taxitemkey2").val(data.data.taxitemkey);
		$("#taxitemmode2").val(data.data.taxitemmode.toLowerCase());
		$('select').trigger('changed.selected.amui');

	    },
 });

    $('#my-edit').modal({
      relatedTarget: this,
      onConfirm: function(e) {
    	  params.taxversion = $("#taxversion2").val();
    	  params.taxitemid = $("#taxitemid2").val();
    	  params.taxitemname = $("#taxitemname2").val();
    	  params.taxrate = $("#taxrate2").val();
    	  params.taxitemmemo = $("#taxitemmemo2").val();
    	  params.taxitemkey = $("#taxitemkey2").val(); 
    	  params.taxitemmode =$("#taxitemmode2").val();
    	    if(params.taxitemid==""){
		    	$.myAlert("税目代码不能为空！");
		    	return false;
		    }
    	    if(params.taxversion==""){
		    	$.myAlert("版本号不能为空！");
		    	return false;
		    }
		    if(params.taxitemname==""){
		    	$.myAlert("税目名称不能为空！");
		    	return false;
		    }
		    if(params.taxrate==""){
		    	$.myAlert("税率不能为空！");
		    	return false;
		    }
		    if(params.taxrate<0||params.taxrate>=1){
			  $.myAlert("税率必须大于等于0小于1！");
		 	  return false;
		    }
		    if(isNaN(params.taxrate)){
			  $.myAlert("税率必须数字并且大于等于0小于1！");
			  return false;
		    }
	    	 $.myAjax({
	    	 	url: $.getService("ui","updateTaxitem?"+$.cookGetUrlParam(params)),
	    	 	contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) { 
	     		   if(data.code == "0"){
	     			  $.myAlert('修改成功！');
	     			  var taxitemid=params.taxitemid;
	     			  addsuccess(taxitemid);
                       $("#taxitemId").val("");
                       $("#taxitemName").val("");
                       $("#taxRate").val("");
	     			}else{
	    				$.myAlert(data.msg,'信息');
	    			}
	     	    },
	        });
    	  
      },
      onCancel: function(e) {
    	
      }
    });
   
}

function dataDelete(taxitemid){
    params.taxitemid=taxitemid;
    $.myConfirm({msg:"确认要删除"+taxitemid+"？",title:"提醒",onConfirm:function(){
        $.myAjax({
            url:$.getService("ui","deleteTaxitem"),
            data:JSON.stringify(params),
            contentType:"text/html;charset=UTF-8",
            success: function (dataJson) {
                if(dataJson.code=="0"){
                    $.myAlert("删除成功！");
                    addsuccess(taxitemid);
                    search(0);
                }else{
                    $.myAlert("删除失败！");
                    return;
				}
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
            }
        });
    }});
}


function addsuccess(taxitemid) {
    params.taxitemid =taxitemid;
    params.page=0;
    params.pagesize = pagesize;

    $.myAjax({
        url: $.getService("ui","queryTaxitem?"+$.cookGetUrlParam(params)),
        contentType:"application/json;charset=UTF-8",
        data:JSON.stringify(params),
        success: function (data) {

            if(data.code == "0"){
                //首次查询page
                $.myPage(page,{rows:data.count,pagesize:pagesize,jump:function(page){
                    search(page);
                }});
                $.myRowHTML(data.data,"rowSample","rowTraget");
            }
            if(data.count==0){
                $.myAlert("没有该税目信息！");
                return;
            }
        },error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
        }
    });
}