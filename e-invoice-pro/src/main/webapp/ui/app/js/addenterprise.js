var channel = "app";
var pagesize=10;
var params = {};

$(document).ready(function(){
	search(0);
    hiddens();
    AddNew();
    $("#btnSearch").click(function(event){
		search(0);
	});
    $.mySelect({
		tragetId:"selectB",
		id:"shopstatus2",
		url:$.getService("ui","Zbgetshopstatus"),
		onChange:function(e){
		}
	});
    $.mySelect({
		tragetId:"selectshoptype",
		id:"shoptype2",
		url:$.getService("ui","Zbgetselectshoptype"),
		onChange:function(e){
		}
	});
    $.mySelect({
		tragetId:"selectshoptypeB",
		id:"shoptypeB",
		url:$.getService("ui","Zbgetselectshoptype"),
		onChange:function(e){
		}
	});
    $.mySelect({
		tragetId:"selectD",
		id:"taxno2",
		url:$.getService("ui","getTaxnoSelect?"),
		onChange:function(e){
			//console.log(e);
		}
	});

    $.mySelect({
		tragetId:"selectA",
		id:"shopstatus",
		url:$.getService("ui","Zbgetshopstatus"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectC",
		id:"taxno",
		url:$.getService("ui","getTaxnoSelect?"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectE",
		id:"iseinvoice1",
		url:$.getService("ui","Zbgetiseinvoice"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectF",
		id:"iseinvoice2",
		url:$.getService("ui","Zbgetiseinvoice"),
		onChange:function(e){
			//console.log(e);
		}
	});
  $.mySelect({
		tragetId:"selectG",
		id:"iseinvoice",
		url:$.getService("ui","Zbgetiseinvoice"),
		onChange:function(e){
			//console.log(e);
		}
	});
});


function search(page){
	params.channel = channel;
	params.shopid=$("#shopId").val();
	params.shopname=$("#ShopName").val();
	params.iseinvoice=$("#iseinvoice").val();
	params.page=page;
	params.pagesize = pagesize;

		$.myAjax({
			url: $.getService("ui","ZbqueryShop"),
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
					$.myAlert("没有该企业机构信息");
					return;
				}
		},
		error: function (jqXHR, textStatus, errorThrown) {
			$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
		}
	  });

}

function AddNew() {
	  $('#btnNew').on('click', function() {
	      $('#my-add').css("display","block");
          $('#bgcolor').css("display","block");
	});
}

function TiJiao() {
    params.shopid = $("#shopid").val();
    params.shopname = $("#shopname").val();
    params.shopstatus = $("#shopstatus").val();
    params.shoptype = $("#shoptype2").val();
    params.taxno = $("#taxno").val();
    params.manager = $("#manager").val();
    params.telephone = $("#telephone").val();
    params.iseinvoice = $("#iseinvoice1").val();

        if(params.shopid==""){
            $.myAlert("机构编码不能为空！");
            return ;
        }
        if(params.shopname==""){
            $.myAlert("机构名不能为空！");
            return ;
        }

        if(params.shopstatus==""){
            $.myAlert("请选择机构状态！");
            return ;
        }

        if(params.shoptype=="" ){
            $.myAlert("请选择机构类型！");
            return ;
        }

        if(params.taxno==""){
            $.myAlert("请选择税号！");
            return ;
        }

        if(params.iseinvoice==""){
            $.myAlert("请选择是否开通电票！");
            return ;
        }


        $.myAjax({
            url: $.getService("ui","ZbinsertShop"),
            contentType:"application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (data) {

                if(data.code == "0"){
                    $.myAlert('新增成功');
                    $('#my-add').css("display","none");
                    $('#bgcolor').css("display","none");
                    clears();
                    search(0);
                }else{
                    $.myAlert(data.msg,'信息');
                    $("#shopid").val('');
                }
            },
        });
}

function QuXiao() {
    $('#my-add').css("display","none");
    $('#bgcolor').css("display","none");
    clears();
}

function clears() {
    $("#shopid").val('');
    $("#shopname").val('');
}

function dataEdit(shopid) {
	params.shopid=shopid;


	$.myAjax({
		url: $.getService("ui","ZbgetShopById"),
		contentType:"application/json;charset=UTF-8",
	    data: JSON.stringify(params),
	    success: function (data) {
		$("#shopid2").val(data.data.shopid);
		$("#shopname2").val(data.data.shopname);
		$("#shopstatus2").val(data.data.shopstatus);
		$("#shoptypeB").val(data.data.shoptype);
		$("#taxno2").val(data.data.taxno);
		$("#manager2").val(data.data.manager);
		$("#telephone2").val(data.data.telephone);
		$("#iseinvoice2").val(data.data.isEinvoice);
		$('select').trigger('changed.selected.amui');
	    }
 });

    $('#my-edit').modal({
      relatedTarget: this,
      onConfirm: function(e) {
    	  params.shopid = $("#shopid2").val();
    	  params.shopname = $("#shopname2").val();
    	  params.shopstatus = $("#shopstatus2").val();
    	  params.shoptype = $("#shoptypeB").val();
    	  params.taxno = $("#taxno2").val();
    	  params.manager = $("#manager2").val();
    	  params.telephone = $("#telephone2").val();
    	  params.iseinvoice = $("#iseinvoice2").val();
    	  if(params.shopid==""){
	        	$.myAlert("机构编码不能为空！");
	    	return ;
	      }
	        if(params.shopname==""){
	        	$.myAlert("机构名不能为空！");
	    	return ;
	      }

	    	$.myAjax({
	    		url: $.getService("ui","ZbupdateShop?"+$.cookGetUrlParam(params)),
	    		contentType:"application/json;charset=UTF-8",
	     	    data: JSON.stringify(params),
	     	    success: function (data) {
	     		   if(data.code == "0"){
	     			  $.myAlert('修改成功');
					  search(0);
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

function dataDelete(shopid,taxno){
	params.shopid = shopid;
	params.taxno=taxno;
	$.myConfirm({msg:"确认要删除？",title:"提醒",onConfirm:function(){
		$.myAjax({
			url:$.getService("ui","ZbdeleteShop"),
			data:JSON.stringify(params),
			contentType:"text/html;charset=UTF-8",
			success: function (dataJson) {
				if(dataJson.code!=0){
					$.myAlert(dataJson.message);
					return;
				}
				$.myAlert("删除成功");
				search(0);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
			}
		});
	}});
	}

function dataOption(shopid) {
    params.shopid=shopid;
    $("#my-option").css("display","block");
    $("#bgcolor").css("display","block");

    $.myAjax({
        url: $.getService("ui","getAllByshopid"),
        contentType:"application/json;charset=UTF-8",
        data: JSON.stringify(params),
        success: function (data) {
        	if(data.code=="1"){
                $("#connectid").val('');
                $("#connecturl").val('');
                $("#connectusername").val('');
                $("#connectpassword").val('');
                $("#connectdriver").val('');
                $("#connectbdcode").val('');
                $("#clientid").val('');
                $("#clientname").val('');
                $("#clientpassword").val('');
                $("#clienturl").val('');
                $("#mac").val('');
                $("#ip").val('');
			}else if(data.code=="2"){
                for(var i=0;i<data.data.length;i++){
                    $("#connectid").val(data.data[i].connectid);
                    $("#connecturl").val(data.data[i].connecturl);
                    $("#connectusername").val(data.data[i].connectusername);
                    $("#connectpassword").val(data.data[i].connectpassword);
                    $("#connectdriver").val(data.data[i].connectdriver);
                    $("#connectbdcode").val(data.data[i].connectbdcode);
                    $("#clientid").val(data.data[i].clientid);
                    $("#clientname").val(data.data[i].clientname);
                    $("#clientpassword").val(data.data[i].clientpassword);
                    $("#clienturl").val(data.data[i].clienturl);
                    $("#mac").val(data.data[i].mac);
                    $("#ip").val(data.data[i].ip);

                }
            }
			else{
        		data.msg;
			}
        }
    });
}

function clickTiJiao() {
        params.connectid = $("#connectid").val();
        params.url = $("#connecturl").val();
        params.username = $("#connectusername").val();
        params.password = $("#connectpassword").val();
        params.driver = $("#connectdriver").val();
        params.dbcharcode = $("#connectbdcode").val();
        params.clientid = $("#clientid").val();
        params.clientname = $("#clientname").val();
        params.clientpassword = $("#clientpassword").val();
        params.clienturl = $("#clienturl").val();
        params.mac = $("#mac").val();
        params.ip = $("#ip").val();
        if(params.connectid==""){
            $.myAlert("数据库连接编码不能为空！");
            return ;
        }
        if(params.url==""){
            $.myAlert("数据库链接地址不能为空！");
            return ;
        }
        if(params.username==""){
            $.myAlert("数据库用户名不能为空！");
            return ;
        }
        if(params.password==""){
            $.myAlert("数据库密码不能为空！");
            return ;
        }
        if(params.driver==""){
            $.myAlert("数据库驱动名不能为空！");
            return ;
        }
        if(params.dbcharcode==""){
            $.myAlert("数据库编码不能为空！");
            return ;
        }
        if(params.clientid==""){
            $.myAlert("小票提取服务编码不能为空！");
            return ;
        }
        if(params.clientname==""){
            $.myAlert("小票提取服务用户名不能为空！");
            return ;
        }
        if(params.clientpassword==""){
            $.myAlert("小票提取服务密码不能为空！");
            return ;
        }
        if(params.clienturl==""){
            $.myAlert("小票提取服务数据库连接地址不能为空！");
            return ;
        }

        $.myAjax({
            url: $.getService("ui","ZbupdateOption"),
            contentType:"application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (data) {
                if(data.code == "0"){
                    $.myAlert('修改成功');
                    $("#my-option").css("display","none");
                    $("#bgcolor").css("display","none");
                    shoptype="";
                    search(0);
                    
                }else{
                    $.myAlert(data.msg,'信息');
                }
            },
        });
}

function clear2() {
    $("#my-option").css("display","none");
    $("#bgcolor").css("display","none");
}

function GetClientinfoByshopid() {
    params.clientid=$("#clientid").val();
    if(params.clientid==""||params.clientid==null){
        $("#clientname").val('');
        $("#clientpassword").val('');
        $("#clienturl").val('');
        $("#mac").val('');
        $("#ip").val('');
    }else{

        $.myAjax({
            url: $.getService("ui", "getClientinfoByshopid"),
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (data) {
                if (data.code = '1') {
                    var html="";
                    for (var i = 0; i < data.data.length; i++) {
                        html+="<option>"+data.data[i].clientid+"</option>";
                    }
                    $("#clientidcodelist").html(html);
                } else {
                    data.msg;
                }
            }
        });

        $.myAjax({
            url: $.getService("ui", "getClientAllinfoByshopid"),
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (data) {
                if (data.code == '1') {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#clientname").val(data.data[i].clientname);
                        $("#clientpassword").val(data.data[i].password);
                        $("#clienturl").val(data.data[i].url);
                        $("#mac").val(data.data[i].mac);
                        $("#ip").val(data.data[i].ip);
                    }
                } else if(data.code == '2'){
                    $("#clientname").val('');
                    $("#clientpassword").val('');
                    $("#clienturl").val('');
                    $("#mac").val('');
                    $("#ip").val('');
                }
                else {
                    data.msg;
                }
            }
        });
    }

}


function GetCconectinfoByshopid() {

    params.connectid=$("#connectid").val();
    if(params.connectid==""||params.connectid==null){
        $("#connecturl").val('');
        $("#connectusername").val('');
        $("#connectpassword").val('');
        $("#connectbdcode").val('');
        $("#connectdriver").val('');
    }else{

        $.myAjax({
            url: $.getService("ui", "getCconectinfoByshopid"),
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (data) {
                if (data.code = '1') {
                    var html = "";
                    for (var i = 0; i < data.data.length; i++) {
                        html += "<option>" + data.data[i].connectid + "</option>";
                    }
                    $("#connectidlist").html(html);
                } else {
                    data.msg;
                }
            }
        });
        $.myAjax({
            url: $.getService("ui", "getCconectAllinfoByshopid"),
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(params),
            success: function (data) {
                if (data.code == '1') {
                    for (var i = 0; i < data.data.length; i++) {
                        $("#connecturl").val(data.data[i].url);
                        $("#connectusername").val(data.data[i].username);
                        $("#connectpassword").val(data.data[i].password);
                        $("#connectbdcode").val(data.data[i].dbcharcode);
                        $("#connectdriver").val(data.data[i].driver);
                    }
                }else if(data.code == '2'){
                    $("#connecturl").val('');
                    $("#connectusername").val('');
                    $("#connectpassword").val('');
                    $("#connectbdcode").val('');
                    $("#connectdriver").val('');
                } else {
                    data.msg;
                }
            }
        });

    }

}

function hiddens() {
    $("#bgcolor").click(function () {
        $("#my-option").css("display","none");
        $("#my-add").css("display","none");
        $("#bgcolor").css("display","none");
    });
}
