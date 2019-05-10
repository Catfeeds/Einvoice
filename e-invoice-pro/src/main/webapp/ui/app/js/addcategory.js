var params = {};
var pagesize=5;
var channel = "app";

$(document).ready(function(){
	//search(0);
   $("#btnSearch").click(function(event){
		search(0);
	});
});


function search(page){
	params.categoryid=$("#categoryId").val();
	params.categoryname=$("#categoryName").val();
	params.deptlevelid=$("#deptlevelId").val();
	params.page=page;
	params.pagesize = pagesize;
	if((params.categoryid||params.categoryname||params.deptlevelid)==""){
        $.myAlert("至少输入一个查询条件才能查询！");
        return ;
    }
    if(params.deptlevelid !=='1'&& params.deptlevelid !==''&& params.deptlevelid !=='2'&&  params.deptlevelid !=='3'&&  params.deptlevelid !=='4'&& params.deptlevelid !=='5'){
        $.myAlert("品类级别必须为1或2或3或4或5！");
        return ;
    }
    
	   $.myAjax({
		   url: $.getService("ui","getCategory?"+$.cookGetUrlParam(params)),
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

$(function() {
    $('#btnNew').on('click', function() {

        $('#my-add').modal({
            relatedTarget: this,
            onConfirm: function(e) {
                params.categoryid = $("#categoryid").val();
                params.categoryname = $("#categoryname").val();
                params.headcatid = $("#headcatid").val();
                params.deptlevelid = $("#deptlevelid").val();
                var categoryid = $("#categoryid").val();
                var categoryname = $("#categoryname").val();
                var headcatid = $("#headcatid").val();
                var deptlevelid = $("#deptlevelid").val();
                if(categoryid ==""){
                    $.myAlert("商品品类ID不能为空！");
                    return ;
                }
                if(categoryname==""){
                    $.myAlert("商品品类名称不能为空！");
                    return ;
                }
                if(categoryname.length > 40){
                    $.myAlert("商品品类名称不能超过40个字符！");
                    return ;
                }
                if(deptlevelid==""){
                    $.myAlert("品类级别不能为空！");
                    return ;
                }
                if(deptlevelid!=='1'&& deptlevelid!=='2'&& deptlevelid!=='3'&& deptlevelid!=='4'&& deptlevelid!=='5'){
                    $.myAlert("品类级别必须为1或2或3或4或5！");
                    return ;
                }
                if(headcatid ==""){
                    $.myAlert("上级品类ID不能为空！");
                    return ;
                }
                dataAdd();
            },
            onCancel: function(e) {
                $("#categoryid").val("");
                $("#categoryname").val("");
                $("#headcatid").val("");
                $("#deptlevelid").val("");
                $("#headcatid").attr("disabled",false);
            }
        });
    });
});

function dataAdd(){
    $.myAjax({
        url: $.getService("ui","insertCategory?"+$.cookGetUrlParam(params)),
        contentType:"application/json;charset=UTF-8",
        data: JSON.stringify(params),
        success: function (data) {

            if(data.code == "0"){
                $.myAlert("新增成功！");
                var categoryid=params.categoryid;
                addsuccess(categoryid);
                $("#categoryid").val("");
                $("#categoryname").val("");
                $("#headcatid").val("");
                $("#deptlevelid").val("");
                $("#headcatid").attr("disabled",false);
                $("#categoryId").val("");
                $("#categoryName").val("");
                $("#deptlevelId").val("");
            }else if(data.code == "-1"){
                $.myAlert("此商品品类ID已经添加过，如需更改请在编辑模块进行编辑！");
            }else if(data.code == "-2"){
                $.myAlert("此商品的品类级别对应的上级品类ID不存在！");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
        }
    });

}

function dataEdit(categoryid) {
	params.categoryid=categoryid;
	$.myAjax({
 	    url: $.getService("ui","getCategoryById?"+$.cookGetUrlParam(params)),
 	    contentType:"application/json;charset=UTF-8",
  	    data:JSON.stringify(params),
  	    success: function (data) { 

  		$("#categoryid2").val(data.data.categoryid);
  		$("#categoryname2").val(data.data.categoryname);
  		$("#headcatid2").val(data.data.headcatid);
  		$("#deptlevelid2").val(data.data.deptlevelid);
  		$("#categorystatus2").val(data.data.categorystatus);

  		if(data.data.deptlevelid==1){
            $("#headcatid2").attr("disabled",true);
        }else{
            $("#headcatid2").attr("disabled",false);
        }

  	    },
     });

	    $('#my-edit').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  params.categoryid = $("#categoryid2").val();
	    	  params.categoryname = $("#categoryname2").val();
	    	  params.headcatid = $("#headcatid2").val();
	    	  params.deptlevelid = $("#deptlevelid2").val();
	    	  params.categorystatus = $("#categorystatus2").val();

              var categoryname2 = $("#categoryname2").val();
              var deptlevelid2 = $("#deptlevelid2").val();
              var headcatid2 = $("#headcatid2").val();

              if(categoryname2==""){
                  $.myAlert("商品品类名称不能为空！");
                  return ;
              }
              if(categoryname2.length > 40){
                  $.myAlert("商品品类名称不能超过40个字符！");
                  return ;
              }

              if(deptlevelid2!=='1'&& deptlevelid2!=='2'&& deptlevelid2!=='3'&& deptlevelid2!=='4'&& deptlevelid2!=='5'){
                  $.myAlert("品类级别必须为1或2或3或4或5！");
                  return ;
              }

		        $.myAjax({
		     	    url: $.getService("ui","updateCategory?"+$.cookGetUrlParam(params)),
		     	    contentType:"application/json;charset=UTF-8",
		     	    data: JSON.stringify(params),
		     	    success: function (data) { 
		     		   if(data.code == "0"){
		     			  $.myAlert("修改成功!");
		     			  var categoryid=params.categoryid;
                           addsuccess(categoryid);
                           $("#categoryId").val("");
                           $("#categoryName").val("");
                           $("#deptlevelId").val("");
		     			}else if(data.code == "-2"){
                           $.myAlert("此商品的品类级别对应的上级品类ID不存在！");
                       }else{
		     				$.myAlert(data.msg,'信息');
		     			}
		     	    },
		        });
	    	  
	      },
	      onCancel: function(e) {

              var deptlevelid2 = $("#deptlevelid2").val();
              if(deptlevelid2=='1'){
                  $("#headcatid2").val(0);
                  $("#headcatid2").attr("disabled",true);
              }else{
                  $("#headcatid2").attr("disabled",false);
              }
	      }
	    });
	    
  }

function myFunction1() {
    var deptlevelid = $("#deptlevelid").val();
    var headcatid = $("#headcatid").val();
    if(deptlevelid=='1'){
        $("#headcatid").val(0);
        $("#headcatid").attr("disabled",true);
    }else if(deptlevelid=='1'&& headcatid=='0'){
        $("#headcatid").attr("disabled",true);
    }else if(deptlevelid!='1'&&headcatid=='0'){
        $("#headcatid").val("");
        $("#headcatid").attr("disabled",false);
    }else if(deptlevelid!='1'&&headcatid!='0'){
        $("#headcatid").attr("disabled",false);
    }
}

function myFunction2() {
    var deptlevelid2 = $("#deptlevelid2").val();
    var headcatid2 = $("#headcatid2").val();
        if(deptlevelid2=='1'){
            $("#headcatid2").val(0);
            $("#headcatid2").attr("disabled",true);
        }else if(deptlevelid2=='1'&& headcatid2=='0'){
            $("#headcatid2").attr("disabled",true);
        }else if(deptlevelid2!='1'&&headcatid2=='0'){
            $("#headcatid2").val("");
            $("#headcatid2").attr("disabled",false);
        }else if(deptlevelid2!='1'&&headcatid2!='0'){
            $("#headcatid2").attr("disabled",false);
        }
}

function addsuccess(categoryid) {
    params.taxitemid =categoryid;
    params.page=0;
    params.pagesize = pagesize;

    $.myAjax({
        url: $.getService("ui","getCategory?"+$.cookGetUrlParam(params)),
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