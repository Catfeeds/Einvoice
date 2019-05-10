//系统变量
var myvalue = new myapp();
var curdtable = "login";
var currentdata = [];
var currentindex = 0;
var currentloginid = "";
var hidden_shopids=[];
// 模块初始化
moduleinit();

function moduleinit() {
	
	$.mySelect({
		tragetId:"selectC",
		id:"jpzz",
		url:"/efs-portal/rest/ui/main/getLookupSelect.action?lookupid=13",
		onChange:function(e){
			 
			console.log(e);
		}
	});
	  
	
	loaddata("");
}

function loaddata(msg) {

	if (msg != "") {
		msg = msg + "<br>";
	}

	showLoadingDialog(msg + "正在获取最新数据,请稍候...");
	myvalue
			.ajax({
				url : "/efs-portal/rest/ui/module/getLoginInfo.action?username=null&userid=null",
				type : "post",
				success : function(text) {

					var result = JSON.parse(text);					
			 
					if (result.code=="1")
					{
						
						currentdata = result.data;
						var rolelist = result.rolelist;
						var arealist = result.arealist;
						var shoplist = result.shoplist;
						
						dataHTML = getRowHTML(currentdata, $("#rowsample").html());
						$("#tabledata").html(
								dataHTML.replace(/<tbody>/g, "").replace(
										/<\/tbody>/g, ""));
	
						dataHTML = getRowHTML(rolelist, $("#role_sample").html());
						$("#roledata").html(
								dataHTML.replace(/<tbody>/g, "").replace(
										/<\/tbody>/g, ""));
	
						dataHTML = getRowHTML(arealist, $("#area_sample").html());
						$("#shopdata").html(
								dataHTML.replace(/<tbody>/g, "").replace(
										/<\/tbody>/g, ""));
	
						for (var i = 0; i < arealist.length; i++) {
							var area = arealist[i].area;
							var areashoplist = [];
							for (var j = 0; j < shoplist.length; j++) {
								if (shoplist[j].area == area || area == "所有机构") {
									areashoplist.push(shoplist[j]);
								}
							}
	
							dataHTML = getRowHTML(areashoplist, $("#shop_sample")
									.html());
							$("#areashop_" + area).html(
									dataHTML.replace(/<tbody>/g, "").replace(
											/<\/tbody>/g, ""));
						}
	
						// 需要重新渲染组件...
						myvalue.reloadModules([ 'accordion' ]);
	
						if (currentdata.length > 0) {
	
							if (currentindex >= currentdata.length) {
								currentindex = 0;
							}
	
							// 选择第一行
							selectrow(currentdata[currentindex].serialid,
									currentdata[currentindex].loginid);
						}
	
						if (msg == "") {
							updateLoadingDialog("加载数据完成");
	
							// 这里太快关闭就会影响到遮罩层,所以改成延迟关闭
						} else {
							updateLoadingDialog(msg);
						}
						
						closeLoadingDialog();
					} else if (result.code==-1) {
						alert("您无此模块操作权限,自动退出");												
						window.parent.gotologin();					
					} else {
						updateLoadingDialog(msg+"获取数据失败!<br>失败原因:"+result.msg);
					}
					

				},
				error : function(jqXHR, textStatus, errorThrown) {
					updateLoadingDialog(msg + "获取数据失败:  网络中断或接口异常!");
				}
			});
}

function addrow() {
	// 设置标题
	$("#editform #edit-title").html("新增账号");

	// 设置编辑模态框默认显示的内容......
	$('#editform #loginid').val("");
	//$('#editform #password').val("");
	$('#editform #username').val("");
	$('#editform #idcard').val("");
	$('#editform #isstop').val(0);
	$('#editform #contacttel').val("");
	$('#editform #note').val("");
	$('#editform #kpd').val("");
	$('#editform #jpzz').val("");

	// 只有先删除了这个属性之后,后面才回再次验证是否为空等信息.......
	$("#editform :input").removeData("validity");
	$("#editform :input").removeClass(
			"am-active am-field-valid am-field-warning am-field-error");
	$("#editform .am-form-group").removeClass("am-form-error am-form-success");
	$(".am-form-group .successinputicon").css("display", "none");
	$(".am-form-group .errorinputicon").css("display", "none");

	// 设置能编辑项
	$('#editform #loginid').attr("disabled", false);

	// 设置保存事件...
	// 解绑事件再绑定事件
	$('#edit-save-btn').unbind("click");
	$('#edit-save-btn').bind(
		"click",
		function() {
			if ($('#my-form').data('amui.validator').isFormValid()) {
				var loginid = $.trim($("#editform #loginid").val());
				//var password = $.trim($("#editform #password").val());
				var username = $.trim($("#editform #username")
						.val());
				var idcard = $.trim($("#editform #idcard").val());
				var kpd = $.trim($("#editform #kpd").val());
				var jpzz = $.trim($("#editform #jpzz").val());
				
				var isstop = $.trim($("#editform #isstop").val());
				var contacttel = $.trim($("#editform #contacttel")
						.val());
				var note = $.trim($("#editform #note").val());
	
				// 检查不能为空的项.....
				if (loginid == "") {
					return true;
				}
				
				var value = {};
				value.loginid = loginid;
				value.password = hex_sha1("abcd1234");
				value.username = username;
				value.idcard = idcard;
				value.kpd = kpd;
				value.jpzz = jpzz;
				value.isstop = 0;
				value.logintype = 0;
				value.contacttel = contacttel;
				value.note = note;
	
				var valuelist = [];
				valuelist.push(value);
	
				$("#editform").modal("close");
				showLoadingDialog();
	
				myvalue
						.ajax({
							url : "/efs-portal/rest/ui/module/insertStandardTable.action?tablename="
									+ curdtable,
							type : "post",
							data : JSON.stringify(valuelist),
							success : function(text) {
								var data = JSON.parse(text);
								if (data.code == "1") {
									// 重新载入数据....
									loaddata("新增数据成功!");
								} else {
									updateLoadingDialog("新增数据失败!<br>失败原因:"
											+ data.msg);
								}
							},
							error : function(jqXHR, textStatus,
									errorThrown) {
								updateLoadingDialog("失败: 网络中断或接口异常!");
							}
						});
			} else {
				$(".am-form-success .successinputicon").css(
						"display", "inline");
				$(".am-form-success .errorinputicon").css(
						"display", "none");
				$(".am-form-error .errorinputicon").css("display",
						"inline");
				$(".am-form-error .successinputicon").css(
						"display", "none");
				alert("表单输入错误!");
			}
		}
	);

	// 设置不能编辑项
	$('#editform').modal({
		relatedTarget : this,
		closeViaDimmer : true,
		onCancel : function() {
			// 取消更新

		}
	});

}

function deleterow(loginid) {
	var username = "";
	for (var i = 0; i < currentdata.length; i++) {
		if (currentdata[i].loginid == loginid) {
			username = currentdata[i].username;
		}
	}

	// 绑定事件
	$('#my-modal-confirm .module-btn').unbind("click");
	$('#my-modal-confirm .module-btn')
			.bind(
					"click",
					function() {
						var value = {};

						value.loginid = loginid;

						var valuelist = [];
						valuelist.push(value);

						$("#my-modal-confirm").modal("close");
						showLoadingDialog();
						myvalue
								.ajax({
									url : "/efs-portal/rest/ui/module/deleteStandardTable.action?tablename="
											+ curdtable,
									type : "post",
									data : JSON.stringify(valuelist),
									success : function(text) {
										var data = JSON.parse(text);
										if (data.code == "1") {
											// 重新载入数据....
											loaddata("删除数据成功!");
										} else {
											updateLoadingDialog("删除数据失败!<br>失败原因:"
													+ data.msg);
										}
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										updateLoadingDialog("失败: 网络中断或接口异常!");
									}
								});
					});

	$("#my-modal-confirm #msgcontent").html(
			"确认删除[" + username + "]持有的账号[" + loginid + "]的信息?");

	$("#my-modal-confirm").modal({});
}

function updaterow(loginid) {
	for (var i = 0; i < currentdata.length; i++) {
		if (currentdata[i].loginid == loginid) {
			// 设置标题
			$("#editform #edit-title").html("您正在修改账号[" + loginid + "]的信息");

			// 设置编辑模态框默认显示的内容......
			$('#editform #loginid').val(currentdata[i].loginid);
			//$('#editform #password').val(currentdata[i].password);
			$('#editform #username').val(currentdata[i].username);
			$('#editform #idcard').val(currentdata[i].idcard);
			$('#editform #kpd').val(currentdata[i].kpd);
			$('#editform #jpzz').val(currentdata[i].jpzz);
			$('#editform #isstop').val(currentdata[i].isstop);
			$('#editform #contacttel').val(currentdata[i].contacttel);
			$('#editform #note').val(currentdata[i].note);

			// 设置不能编辑项
			$('#editform #loginid').attr("disabled", true);

		}
	}

	// 只有先删除了这个属性之后,后面才回再次验证是否为空等信息.......
	$("#editform :input").removeData("validity");
	$("#editform :input").removeClass(
			"am-active am-field-valid am-field-warning am-field-error");
	$("#editform .am-form-group").removeClass("am-form-error am-form-success");
	$(".am-form-group .successinputicon").css("display", "none");
	$(".am-form-group .errorinputicon").css("display", "none");

	// 解绑事件再绑定事件
	$('#edit-save-btn').unbind("click");
	$('#edit-save-btn')
			.bind(
					"click",
					function() {
						if ($('#my-form').data('amui.validator').isFormValid()) {
							//var password = $.trim($("#editform #password").val());
							var username = $.trim($("#editform #username")
									.val());
							var idcard = $.trim($("#editform #idcard").val());
							var kpd = $.trim($("#editform #kpd").val());
							var jpzz = $.trim($("#editform #jpzz").val());
							var isstop = $.trim($("#editform #isstop").val());
							var contacttel = $.trim($("#editform #contacttel")
									.val());
							var note = $.trim($("#editform #note").val());

							var value = {};
							value.old_loginid = loginid;
							//value.password = password;
							value.username = username;
							value.idcard = idcard;
							value.kpd = kpd;
							value.jpzz = jpzz;
							value.isstop = 0;
							value.contacttel = contacttel;
							value.note = note;

							var valuelist = [];
							valuelist.push(value);

							$("#editform").modal("close");
							showLoadingDialog();

							myvalue
									.ajax({
										url : "/efs-portal/rest/ui/module/updateStandardTable.action?tablename="
												+ curdtable,
										type : "post",
										data : JSON.stringify(valuelist),
										success : function(text) {
											var data = JSON.parse(text);
											if (data.code == "1") {
												// 重新载入数据....
												loaddata("修改数据成功!");
											} else {
												updateLoadingDialog("修改数据失败!<br>失败原因:"
														+ data.msg);
											}
										},
										error : function(jqXHR, textStatus,
												errorThrown) {
											updateLoadingDialog("失败: 网络中断或接口异常!");
										}
									});
						} else {
							$(".am-form-success .successinputicon").css(
									"display", "inline");
							$(".am-form-success .errorinputicon").css(
									"display", "none");
							$(".am-form-error .errorinputicon").css("display",
									"inline");
							$(".am-form-error .successinputicon").css(
									"display", "none");
							alert("表单输入错误!");
						}
					});

	$('#editform').modal({
		relatedTarget : this,
		closeViaDimmer : true,
		onCancel : function() {
			// 取消更新

		}
	});
}


$('.am-modal').on('opened.modal.amui', function() {
	$(".am-dimmer,.am-active").show();
});

	
function showLoadingDialog(msg) {
	if (msg == null || msg == "")
		msg = "正在处理，请稍候...";

	$("#my-modal-loading #msgcontent").html(msg);
	$("#my-modal-loading #loadingicon").show();
	$("#my-modal-loading").modal("open");

}

function closeLoadingDialog() {
	setTimeout(function() {
		$("#my-modal-loading").modal("close");
	}, 300);
}

function updateLoadingDialog(msg) {
	$("#my-modal-loading #msgcontent").html(msg);
	$("#my-modal-loading #loadingicon").hide();
	if ($("#my-modal-loading").is(":hidden")) {
		// 如果结果返回的时候，遮罩层已经被点击关闭了则会进入这里.....
	}
}

function showMsgDialog(msg) {
	$("#my-modal-msg #msgcontent").html(msg);
	if ($("#my-modal-msg").is(":hidden")) {
		$("#my-modal-msg").modal("open");
	}
}

function getRowHTML(datajson, subHTML) {
	var subList = "";
	var HTMLArray = subHTML.split('||');

	for (var i = 0; i <= datajson.length - 1; i++) {
		datajson[i].serialid = i + 1;

		var jsonflag = false;

		for (var j = 0; j <= HTMLArray.length - 1; j++) {

			// 只要html里面不直接用||作为开头，那么用基/偶数就就可以判断是否是变量了....
			if (jsonflag == true) {
				var temp = datajson[i][HTMLArray[j]];
				subList += temp==undefined?"":temp;
			} else {
				subList += HTMLArray[j];
			}

			// 取反
			jsonflag = !jsonflag;
		}
	}

	return subList;
}

function selectrow(serialid, loginid) {

	currentindex = serialid - 1; // 这里要减一,数组从0开始,serialid从1开始编码

	$(".rowtr").css("color", "#000000");
	$("#rowtr_" + serialid).css("color", "#0033CC");

	// 当前选择的账号
	currentloginid = loginid;

	// 先清空勾选.......
	$(".rolecheckbox").attr("checked", false);
	$(".areacheckbox").attr("checked", false);
	$(".shopcheckbox").attr("checked", false);

	// 获取帐号拥有的权限....
	myvalue.ajax({
		url : "/efs-portal/rest/ui/module/getLoginPower.action?loginid="
				+ currentloginid,
		type : "post",
		success : function(text) {
			var result = JSON.parse(text);

			var rolelist = result.rolelist;
			var shoplist = result.shoplist;
			hidden_shopids=shoplist;
			for (var i = 0; i < rolelist.length; i++) {
				var roleid = rolelist[i].roleid;

				$("#role_" + roleid).attr("checked", true);
			}

			for (var i = 0; i < shoplist.length; i++) {
				var shopid = shoplist[i].shopid;

				$("#shop_" + shopid).attr("checked", true);
				shopcheckboxclick($("#shop_" + shopid));
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			updateLoadingDialog("获取数据失败:  网络中断或接口异常!");
		}
	});

	$(".rowcheckbox").uCheck('uncheck');
	try {
		$("#rowcheckbox_" + serialid).uCheck('check'); // 插件BUG,用这个方法会导致下面的js都无法继续执行,所以要加个异常捕捉.....
	} catch (e) {
	}

}

function savepower() {
	var loginid = currentloginid;
	var username = "";
	for (var i = 0; i < currentdata.length; i++) {
		if (currentdata[i].loginid == loginid) {
			username = currentdata[i].username;
		}
	}

	$("#my-modal-confirm #msgcontent").html(
			"确认保存[" + username + "]持有的账号[" + loginid + "]的权限信息?");

	// 绑定事件
	$('#my-modal-confirm .module-btn').unbind("click");
	$('#my-modal-confirm .module-btn')
			.bind(
					"click",
					function() {

						var value = {};
						var rolelist = [];
						var shoplist = [];

						// 获取到勾选的角色
						$("#roledata .role input:checkbox").each(function() {

							if ($(this).attr("checked")) {
								role = {};
								role.roleid = $(this).data("roleid");
								rolelist.push(role);
							}
						});

						// 获取到勾选的机构
						$("#shopdata .shopcheckbox")
								.each(
										function() {

											if ($(this).attr("checked")) {
												var have = false;

												// 判断是否已经存在，已经存在则不存入，避免主键冲突
												for (var i = 0; i < shoplist.length; i++) {
													if (shoplist[i].shopid == $(
															this)
															.data("shopid")) {
														have = true;
														break;
													}
												}

												if (have) {
													return;
												}

												shop = {};
												shop.shopid = $(this).data(
														"shopid");
												shoplist.push(shop);
											}
										});

						// 增加角色权限
						value.rolelist = rolelist;
//						value.shoplist = shoplist;
						value.shoplist = hidden_shopids;
						$("#my-modal-confirm").modal("close");
						showLoadingDialog();
						myvalue
								.ajax({
									url : "/efs-portal/rest/ui/module/setLoginPower.action?loginid="
											+ loginid,
									type : "post",
									data : JSON.stringify(value),
									success : function(text) {
										var data = JSON.parse(text);
										if (data.code == "1") {
											// 重新载入数据....
											loaddata("保存用户权限成功!");
										} else {
											updateLoadingDialog("保存用户权限失败!<br>失败原因:"
													+ data.msg);
										}
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										updateLoadingDialog("失败: 网络中断或接口异常!");
									}
								});
					});

	$("#my-modal-confirm").modal({});

}

// /////////////////////////////////////////////////机构选择器//////////////////////////////////////////////
// 选择区域.....
function allareashop(obj) {
	var area = $(obj).data("area");

	// 勾选/取消归属于这个区域的所有机构,及其他区域的同名机构
	if (obj.checked) {
		// 如果勾选的是[所有机构],则所有区域和机构都勾上...
		if (area == "所有机构") {
			$("#shopdata .areacheckbox").attr("checked", true);
			$("#shopdata .shopcheckbox").attr("checked", true);
			hidden_shopids = [];
			$("#shopdata .shopcheckbox").each(function() {
				var shopid = $(this).data("shopid");
				hidden_shopids.push({'shopid':shopid});
			});
			return;
		}

		hidden_shopids = [];
		$("#areashop_" + area).find("input:checkbox").each(function() {
			$(this).attr("checked", true);
			var shopid = $(this).data("shopid");
			hidden_shopids.push({'shopid':shopid});
		});
	} else {
		hidden_shopids=[];
		// 如果勾选的是[所有机构],则所有区域和机构都勾上...
		if (area == "所有机构") {
			$("#shopdata .areacheckbox").attr("checked", false);
			$("#shopdata .shopcheckbox").attr("checked", false);
			return;
		}

		$("#areashop_" + area).find("input:checkbox").each(function() {
			$(this).attr("checked", false);

			var shopid = $(this).data("shopid");
			// 处理编码相同其他区域的项
			$("#shopdata input:checkbox").each(function() {
				if ($(this).data("shopid") == shopid) {
					$(this).attr("checked", false);
				}
			});
		});
	}

	// 判断如果所有门店的已经勾选,则[所有机构]也为勾选状态....
	var allcheck = true;
	$("#shopdata .shopcheckbox").each(function() {
		if (!$(this).attr("checked")) {
			allcheck = false;
			return;
		}
	});
	$("#area_所有机构").attr("checked", allcheck);
}

// 勾选机构
function shopcheckboxclick(obj) {
	var area = $(obj).data("area");
	var shopid = $(obj).data("shopid");
	// 勾选[所有区域/指定区域]机构编码相同的项
	if ($(obj).attr("checked")) {
		$("#shopdata input:checkbox").each(function() {
			if ($(this).data("shopid") == shopid) {
				$(this).attr("checked", true);
			}
		});
	}
	var isin=0;
	if ($(obj).attr("checked")) {
		for(var i=0;i<hidden_shopids.length;i++){
			if(shopid==hidden_shopids[i].shopid){
				isin=1;
				break;
			}
		}
		if(isin==0){
			hidden_shopids.push({'shopid':shopid});
		}
	}else{
		for(var i=0;i<hidden_shopids.length;i++){
			if(shopid==hidden_shopids[i].shopid){
				hidden_shopids.splice(i, 1); 
				break;
			}
		}
	}
	
	// 如果机构所属区域已经全部勾选,则将区域设置为勾选状态
	var allshopcheck = true;
	$("#areashop_" + area + " input:checkbox").each(function() {
		if (!$(this).attr("checked")) {
			allshopcheck = false;
			return;
		}
	});
	$("#area_" + area).attr("checked", allshopcheck);

	// 判断如果所有门店的已经勾选,则[所有机构]也为勾选状态....
	var allcheck = true;
	$("#shopdata .shopcheckbox").each(function() {
		if (!$(this).attr("checked")) {
			allcheck = false;
			return;
		}
	});
	$("#area_所有机构").attr("checked", allcheck);
}
// /////////////////////////////////////////////////机构选择器//////////////////////////////////////////////

function seachByshopIds(reqshop){
//	String reqshop=$("#reqshop").val();

	myvalue.ajax({
		url : "/efs-portal/rest/ui/module/getShopInfoByReqest.action?reqshop="+reqshop,
		type : "post",
		success : function(text) {

			var result = JSON.parse(text);					
			
			if (result.code=="1")
			{
				var arealist = result.arealist;
				var shoplist = result.shoplist;

				for (var i = 0; i < arealist.length; i++) {
					var area = arealist[i].area;
					var areashoplist = [];
					for (var j = 0; j < shoplist.length; j++) {
						if (shoplist[j].area == area || area == "所有机构") {
							areashoplist.push(shoplist[j]);
						}
					}

					dataHTML = getRowHTML(areashoplist, $("#shop_sample")
							.html());
					$("#areashop_" + area).html(
							dataHTML.replace(/<tbody>/g, "").replace(
									/<\/tbody>/g, ""));
				}

				// 需要重新渲染组件...
				myvalue.reloadModules([ 'accordion' ]);
				
					selectrow(currentindex+1,
							currentdata[currentindex].loginid);
				

			} else if (result.code==-1) {
				alert("您无此模块操作权限,自动退出");												
				window.parent.gotologin();					
			} else {
				updateLoadingDialog(msg+"获取数据失败!<br>失败原因:"+result.msg);
			}
			

		},
		error : function(jqXHR, textStatus, errorThrown) {
			updateLoadingDialog(msg + "获取数据失败:  网络中断或接口异常!");
		}
	});
	
}
function search_name(){
	$("#request_userid").val('');
	$("#request_name").val('');
	$('#search_prompt').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  showLoadingDialog();
	    		myvalue
	    				.ajax({
	    					url : "/efs-portal/rest/ui/module/getLoginInfo.action?username="+$("#request_name").val()+"&userid="+$("#request_userid").val(),
	    					type : "post",
	    					success : function(text) {

	    						var result = JSON.parse(text);					
	    						
	    						if (result.code=="1")
	    						{
	    							currentdata = result.data;
	    							var rolelist = result.rolelist;
	    							var arealist = result.arealist;
	    							var shoplist = result.shoplist;
	    		
	    							dataHTML = getRowHTML(currentdata, $("#rowsample").html());
	    							$("#tabledata").html(
	    									dataHTML.replace(/<tbody>/g, "").replace(
	    											/<\/tbody>/g, ""));
	    		
	    							dataHTML = getRowHTML(rolelist, $("#role_sample").html());
	    							$("#roledata").html(
	    									dataHTML.replace(/<tbody>/g, "").replace(
	    											/<\/tbody>/g, ""));
	    		
	    							dataHTML = getRowHTML(arealist, $("#area_sample").html());
	    							$("#shopdata").html(
	    									dataHTML.replace(/<tbody>/g, "").replace(
	    											/<\/tbody>/g, ""));
	    		
	    							for (var i = 0; i < arealist.length; i++) {
	    								var area = arealist[i].area;
	    								var areashoplist = [];
	    								for (var j = 0; j < shoplist.length; j++) {
	    									if (shoplist[j].area == area || area == "所有机构") {
	    										areashoplist.push(shoplist[j]);
	    									}
	    								}
	    		
	    								dataHTML = getRowHTML(areashoplist, $("#shop_sample")
	    										.html());
	    								$("#areashop_" + area).html(
	    										dataHTML.replace(/<tbody>/g, "").replace(
	    												/<\/tbody>/g, ""));
	    							}
	    		
	    							// 需要重新渲染组件...
	    							myvalue.reloadModules([ 'accordion' ]);
	    		
	    							if (currentdata.length > 0) {
	    		
	    								if (currentindex >= currentdata.length) {
	    									currentindex = 0;
	    								}
	    		
	    								// 选择第一行
	    								selectrow(currentdata[currentindex].serialid,
	    										currentdata[currentindex].loginid);
	    							}
	    							updateLoadingDialog("加载数据完成");
	    							
	    							// 这里太快关闭就会影响到遮罩层,所以改成延迟关闭
	    							closeLoadingDialog();
	    						} else if (result.code==-1) {
	    							alert("您无此模块操作权限,自动退出");												
	    							window.parent.gotologin();					
	    						} else {
	    							updateLoadingDialog(msg+"获取数据失败!<br>失败原因:"+result.msg);
	    						}
	    						

	    					},
	    					error : function(jqXHR, textStatus, errorThrown) {
	    						updateLoadingDialog(msg + "获取数据失败:  网络中断或接口异常!");
	    					}
	    				});
	      },
	      onCancel: function(e) {
	       
	      }});
	
	
	
}

function adminmodifyPassword(loginid) {

	var title;
	
	title = '修改密码';

	// 设置标题
	$("#editform6 #edit-title").html(title);

	
	document.querySelectorAll("#editform6 #myform6")[0].reset();
	

	// 只有先删除了这个属性之后,后面才回再次验证是否为空等信息.......
	$("#editform6 :input").removeData("validity");
	$("#editform6 :input").removeClass(
			"am-active am-field-valid am-field-warning am-field-error");
	$("#editform6 .am-form-group").removeClass("am-form-error am-form-success");
	$("#editform6 .am-form-group .successinputicon").css("display", "none");
	$("#editform6 .am-form-group .errorinputicon").css("display", "none");

	var value = {};
	value.editform="editform6";
	
	value.url="/efs-portal/rest/ui/main/adminmodifyPassword.action";

	// 设置保存事件...
	// 解绑事件再绑定事件
	$('#editform6 #edit-save-btn').unbind("click");
	$('#editform6 #edit-save-btn').bind(
		"click",
		function() {
			if (!$('#'+value.editform+' #myform6').data('amui.validator').isFormValid()) {
				alert('存在未填数据');
			}
			
			var newpwd1=$("#"+value.editform+" #newpwd1").val();	
			var newpwd2=$("#"+value.editform+" #newpwd2").val();
			
			if (newpwd1!=newpwd2) {
				alert('新密码不一致');
				return;
			}
			
			if (newpwd1.length < 8)
			{
				alert('密码长度不能小于8位!');
				return;
			}
			
			
//			var modes = passwordLevel(newpwd1);
//			if ((modes & 6) != 6)
//			{
//				alert('密码必须包括大小写字母');
//				return;
//			}
//			
//			if ((modes & 8) != 8)
//			{
//				alert('密码必须包含特殊字符');
//				return;
//			}
		
			$("#"+value.editform+"").modal("close");
			var form = document.getElementById("myform6");
			
			var data = myvalue.getData(form);
			data.newpwd1 = hex_sha1(newpwd1);
			data.newpwd2 = hex_sha1(newpwd2);
			data.loginid = loginid;
			
			$.ajax({     
				url:value.url,     
				type:'post',     
				data:data,
				async : false, //默认为true 异步     
				error:function(){     
				   myvalue.updateLoadingDialog("亲，访问接口失败，请联系管理员!");
				},     
				success:function(data){     
				    var jsondata = eval("("+data+")");  
	          if(jsondata.code == "1"){  
							alert("密码修改成功!");
	          }else{  
	            alert(jsondata.msg);
	          }  
				}  
			}); 
      
      form.reset(); 
		}
	);
	
	// 设置不能编辑项
	$('#editform6').modal({
		relatedTarget : this,
		closeViaDimmer : true,
		onCancel : function() {
			// 取消更新

		}
	});

}
