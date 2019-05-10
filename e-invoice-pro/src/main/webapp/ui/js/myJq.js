(function($){
	//获取URL参数值
	$.getUrlParam = function(name){
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return decodeURIComponent(r[2]); return null;
	};
	
	//将js对象转为get方式的参数串
	$.cookGetUrlParam = function(param, key){
		var paramStr="";
		if(param instanceof String||param instanceof Number||param instanceof Boolean){
			paramStr+="&"+key+"="+encodeURIComponent(param);
		}else{
			$.each(param,function(i){
				var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
				if(k!=null){
					paramStr+='&'+$.cookGetUrlParam(this, k);
				}
			});
		}
		return paramStr.substr(1);
	};
	
	$.startWith = function(c,s){
		if(s==null||s==""||c.length==0||s.length>c.length)
			   return false;
			  if(c.substr(0,s.length)==s)
			     return true;
			  else
			     return false;
			  return true;
	};
	
	$.endWith = function(c,s){
		if(s==null||s==""||c.length==0||s.length>c.length)
		     return false;
		  if(c.substring(c.length-s.length)==s)
		     return true;
		  else
		     return false;
		  return true;
	};
	
	$.gotoUrl = function(url){
		var token = "token="+$.getUrlParam("token");
		if(url.indexOf("?")<0){
			url+="?";
		}
		if($.endWith(url,"?")){
			url = url+token;
		}else{
			url = url+"&"+token;
		}
		window.location.href = url;
	};
	
	//将页面指定的input[name]对象值获取为json对象
	$.myCookValue = function(parjson){
		var name = "data-input";
		var pageSize = 10;
		var page = 0;
		if (("name" in parjson)){
			name = parjson.name;
		}
		if (("pageSize" in parjson)){
			pageSize = parjson.pageSize;
		}
		if (("page" in parjson)){
			page = parjson.page<=0?0:parjson.page;
		}
		var res  = {};
		res.pageSize = pageSize;
		res.page = page;
		var objs = $("input["+name+"],select["+name+"]");
		$.each( objs, function(i, n){
			var m = $(n);
			var value=m.attr(name);
			res[value] = m.val();
		});
		return res;
	};
	
	//分页
	$.myPage = function(page, parjson){
		//当指定页大于0时直接退出，防止多次生成page控件
		if(page>0) return;
		
		if (!parjson.targetId){
			parjson.targetId = "page";
		}
		if (!parjson.rows){
			parjson.rows = 0;
		}
		if (!parjson.pagesize){
			parjson.pagesize =10;
		}
		if (!parjson.pages){
			//如果没有指定页数则根据总行数rows和pagesize计算
			parjson.pages = Math.ceil(parjson.rows/parjson.pagesize);
			if(parjson.pages==0) parjson.pages=1;
		}
		if (!parjson.curr){
			parjson.curr = 1;
		}
		
		if (parjson.groups===undefined){
			parjson.groups =3;
		}
		if (parjson.first===undefined){
			parjson.first ="首页";
		}
		if (parjson.last===undefined){
			parjson.last ="尾页";
		}
		if (parjson.jump===undefined){
			parjson.jump = function(page){
				console.log('跳转到第' + page + "页");
			};
		}
		$("#"+parjson.targetId).unbind();
		$("#"+parjson.targetId).page({
            pages: parjson.pages, //页数
            curr: parjson.curr, //当前页 
            type: 'default', //主题
            groups: parjson.groups, //连续显示分页数
            prev: '<', //若不显示，设置false即可
            next: '>', //若不显示，设置false即可        
            first: parjson.first,
            last: parjson.last, //false则不显示
            render: function(context, $el, index) { //渲染[context：对this的引用，$el：当前元素，index：当前索引]
                if (index == 'last') { //虽然上面设置了last的文字为尾页，但是经过render处理，结果变为最后一页
                    $el.find('a').html('最后一页');
                    return $el;
                }
                return false; //没有返回值则按默认处理
            },
            /*
             * 触发分页后的回调，如果首次加载时后端已处理好分页数据则需要在after中判断终止或在jump中判断first是否为假
             */
            jump: function(context, first) {
            	if(!first){
            		parjson.jump(context.option.curr);
            	}
            }
        });
	};
	
	/*
	 * datajson 数组数据
	 * fromId row模板Id
	 * toId 写入Id
	 * type 0=向前追加 1=向后追加 2=替换 others=填入
	 * */
	$.myRowHTML = function(datajson,fromId,toId,type){
		var tr = $("#"+fromId+":first").html();
		var HTMLArray = tr.split('||');
		var jsonflag= false;
		var subList="";
		for (var i = 0; i <= datajson.length-1; i++){
			datajson[i].r=i+1;
			datajson[i].index=i;
			jsonflag=false;
			for (var j = 0; j <= HTMLArray.length-1; j++){
				if (jsonflag){
					var temp = datajson[i][HTMLArray[j]];
					subList += temp==undefined?"":temp;
				} else {
					subList += HTMLArray[j];
				}
				jsonflag=!jsonflag;
			}
		}
		
		if(type==0){
			$("#"+toId).prepend(subList);
		}else if(type==1){
			$("#"+toId).append(subList);
		}else if(type==2){
			$("#"+toId).replaceWith(subList);
		}else{
			$("#"+toId).html(subList);
		}
		
	};
	
	//ajax方式请求，自动带上token,url进行转码
	$.myAjax = function(parjson){
		//默认为60秒
		if (!("timeout" in parjson)){
			parjson.timeout = 60000;
		}
		
		if (!("showModal" in parjson)){
			parjson.showModal = true;
		}
		
		//post提交的数据
		if (!("data" in parjson)){
			parjson.data = {};
		}
		
		if (!("type" in parjson)){
			parjson.type="post";
		}
		if (!("progress" in parjson)){
			parjson.progress=true;
		}
		if (!("contentType" in parjson)){
			parjson.contentType="application/x-www-form-urlencoded;charset=UTF-8";
		}
		
		//显示进度条
		if(parjson.progress){
			$.AMUI.progress.start();
		}
		if(parjson.showModal){
			$.myModal(true,null,"请稍后...");
		}
		
		var mysuccess=function(text){
			if(parjson.progress){
				$.AMUI.progress.done();
			}
			if(parjson.showModal){
				$.myModal(false);
			}
			var data={};
			try{
				data = JSON.parse(text);
				parjson.success(data);
			} catch(e){
				parjson.success(text);
			}
		};
		
		var myerror=function(jqXHR, textStatus, errorThrown){
			if(parjson.progress){
				$.AMUI.progress.done();
				$.myModal(false);
			}
			parjson.error(jqXHR, textStatus, errorThrown);
		};
		
		var token = "token="+$.getUrlParam("token");
		var shopid = "&changeshopid="+$.getUrlParam("changeshopid");
		if(parjson.url.indexOf("?")<0){
			parjson.url +="?";
		}
		if($.endWith(parjson.url,"?")){
			parjson.url = parjson.url+token+shopid;
		}else{
			parjson.url = parjson.url+"&"+token+shopid;
		}
		
		$.ajax({
			url: parjson.url,
			data: parjson.data,
			type: parjson.type,
			dataType:parjson.dataType,
			jsonpCallback:parjson.jsonpCallback,
			contentType:parjson.contentType,
			success: mysuccess,
			error: myerror,
			timeout: parjson.timeout,
			cache:false,
		});
	};
	
	$.myModal = function(isShow,msg,title){
		if($("#my-modal").length>0){
			$("#my-modal").remove();
		}
		
		if($("#my-modal").length==0){
			var obj = $('<div class="am-modal am-modal-no-btn" tabindex="-1" id="my-modal">'+
					'<div class="am-modal-dialog">'+
					'<div class="am-modal-hd" id="my-modal-title">信息'+
					'<a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a></div>'+
					'<div class="am-modal-bd" id="my-modal-msg"><span class="am-icon-spinner am-icon-spin"></span></div></div></div>');
			obj.appendTo(document.body);
		}
		if(isShow){
			if (title){
				$("#my-modal-title").text(title);
			}
			if (msg){
				$("#my-modal-msg").html(msg);
			}
			$("#my-modal").show();
			$("#my-modal").addClass("am-modal-active");
		}else{
			$("#my-modal").hide();
			$("#my-modal").removeClass("am-modal-active");
		}
	};
	
	//显示Alert
	$.myAlert = function(msg,title){
		//检查alert标签是否存在
		if($("#my-alert").length==0){
			var obj = $('<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">'+
								'<div class="am-modal-dialog">'+
								'<div class="am-modal-hd" id="my-alert-title">信息</div>'+
								'<div class="am-modal-bd" id="my-alert-msg"></div>'+
								'<div class="am-modal-footer">'+
								'<span class="am-modal-btn" data-am-modal-cancel>确定</span></div></div></div>');
			obj.appendTo(document.body);
		}
		if (title){
			$("#my-alert-title").text(title);
		}
		if (msg){
			$("#my-alert-msg").html(msg);
		}
		$("#my-alert").modal({
			relatedTarget: this
		});
	};
	
	$.myConfirm = function(pjson){
		if (!pjson.title){
			pjson.title = "";
		}
		if (!pjson.msg){
			pjson.msg = "";
		}
		if (!pjson.btn1){
			pjson.btn1 = "取消";
		}
		if (!pjson.btn2){
			pjson.btn2 = "确认";
		}
		
		if($("#my-confirm").length>0){
			$("#my-confirm").remove();
		}
		
		if($("#my-confirm").length==0){
			var obj = $('<div class="am-modal am-modal-confirm" tabindex="-1" id="my-confirm">'+
								'<div class="am-modal-dialog">'+
								'<div class="am-modal-hd" id="my-confirm-title">'+pjson.title+'</div>'+
								'<div class="am-modal-bd" id="my-confirm-msg">'+pjson.msg+'</div>'+
								'<div class="am-modal-footer">'+
								'<span class="am-modal-btn" data-am-modal-cancel>'+pjson.btn1+'</span><span class="am-modal-btn" data-am-modal-confirm>'+pjson.btn2+'</span></div></div></div>');
			obj.appendTo(document.body);
		}
		
		$("#my-confirm").modal({
			closeViaDimmer:0,
			relatedTarget: this,
			onConfirm:pjson.onConfirm,
			onCancel:pjson.onCancel
		});
	};
	
	$.myPrompt = function(p){
		if($("#my-prompt").length>0){
			$("#my-prompt").remove();
		}
		
		if($("#my-prompt").length==0){
			var obj = $('<div class="am-modal am-modal-confirm" tabindex="-1" id="my-prompt">'+
								'<div class="am-modal-dialog">'+
								'<div class="am-modal-hd" id="my-prompt-title">信息</div>'+
								'<div class="am-modal-bd" id="my-prompt-msg"></div>'+
								'<div class="am-modal-bd"><input type="text" class="am-modal-prompt-input" id="my-prompt-input"></div>'+
								'<div class="am-modal-footer">'+
								'<span class="am-modal-btn" data-am-modal-cancel>取消</span><span class="am-modal-btn" data-am-modal-confirm>提交</span></div></div></div>');
			obj.appendTo(document.body);
		}
		if (p.title){
			$("#my-prompt-title").text(p.title);
		}
		if (p.msg){
			$("#my-prompt-msg").html(p.msg);
		}
		if (p.value){
			$("#my-prompt-input").val(p.value);
		}
		$("#my-prompt").modal({
			relatedTarget: this,
			onConfirm:p.onConfirm,
			onCancel:p.onCancel
		});
	};
	
	/*
	 * 编辑性质弹出框
	 * pjson.title 标题
	 * pjson.form 预置的form对象
	 * pjosn.formDataName 数据名称
	 * pjson.formData 数据
	 * pjson.valid(newData) 数据校验方法，参数为新数据，返回true则通过
	 * pjson.onConfirm 提交方法
	 * pjson.onCancel
	 * */
	$.myEditModal = function(pjson){
		if(!pjson.formData){
			return;
		}
		if(!pjson.title){
			pjson.title = "编辑";
		}
		
		//销毁
		if($("#my-editmodal").length>0){
			$("#my-editmodal").remove();
		}
		
		var obj = $('<div class="am-modal am-modal-confirm" tabindex="-1" id="my-editmodal">'+
				'<div class="am-modal-dialog">'+
				'<div class="am-modal-bd" id="my-editmodal-title"></div>'+
				'<div class="am-modal-bd" id="my-editmodal-form"></div>'+
				'<div class="am-modal-footer">'+
				'<span class="am-modal-btn" data-am-modal-cancel>取消</span><span class="am-modal-btn" data-am-modal-confirm>提交</span></div></div></div>');
		obj.appendTo(document.body);
		
		//将data数据自动绑定到form
		var objs = $("input["+pjson.formDataName+"],select["+pjson.formDataName+"]");
		$.each( objs, function(i, n){
			var e = $(n);
			if(pjson.formData==null){
				e.attr("value","");
			}else{
				e.attr("value",pjson.formData[e.attr(pjson.formDataName)]);
			}
		});
		
		$("#my-editmodal-title").text(pjson.title);
		$("#my-editmodal-form").html(pjson.form.html());
		$("#my-editmodal-form").validator();
		$("#my-editmodal").modal({
			relatedTarget: this,
			width:"900",
			onConfirm:function(){
				//验证不通过不能提交
				this.options.closeOnConfirm = $('#my-editmodal-form').validator("isFormValid");
				
				var newData = {};
				var objs = $("input["+pjson.formDataName+"],select["+pjson.formDataName+"]");
				$.each( objs, function(i, n){
					n = $(n);
					var value=n.attr(pjson.formDataName);
					newData[value] = n.attr("value");
				});
				
				//如果有校验方法，返回false则跳出
				if("valid" in pjson){
					this.options.closeOnConfirm = pjson.valid(newData);
				}
				//调用保存方法
				if(this.options.closeOnConfirm) pjson.onConfirm(newData);
			},
			onCancel:pjson.onCancel
		});
	};
	
	/*
	 * 下拉选择
	 * tragetId 写入地址
	 * id 控件的唯一id
	 * multiple 默认false 是否多选
	 * dataArray 下拉数据数组 [{name:显示名称,value:名称,xxx:其它自定义属性}]
	 * url 数据获取地址，如果有值则追加到dataArray后面
	 * onChange(e) 值改变事件
	 * btnWidth 按钮宽度，默认为 200px
	 * btnSize 按钮尺寸，可选值为 xl|sm|lg|xl
	 * btnStyle: 'default': 按钮样式，可选值为 primary|secondary|success|warning|danger
	 * maxHeight: null: 列表最大高度
	 * dropUp: 0: 是否为上拉，默认为 0 (false)
	 * placeholder: 占位符，默认读取 <select> 的 placeholder 属性，如果没有则为 点击选择...
	 */
	$.mySelect = function(pjson){
		//是否多选
		var multiple = pjson.multiple?"multiple":"";
		
		var obj = $('<select data-am-selected="{searchBox: 1}" id="'+pjson.id+'" '+multiple+'>'+
							'</select>');
		$("#"+pjson.tragetId).html(obj);
		
		if(!'btnWidth' in pjson){
			pjson.btnWidth = 200;
		}
		if(!'btnSize' in pjson){
			pjson.btnSize = null;
		}
		if(!'btnStyle' in pjson){
			pjson.btnStyle = 'default';
		}
		if(pjson.maxHeight===undefined){
			pjson.maxHeight = 150;
		}
		if(!'dropUp' in pjson){
			pjson.dropUp = 0;
		}
		if(!'placeholder' in pjson){
			pjson.placeholder = '点击选择...';
		}
		if(!pjson.dataArray){
			pjson.dataArray = [];
		}
		
		if('onChange' in pjson){
			$("#"+pjson.id).on('change', function() {
			  pjson.onChange($(this).find('option').eq(this.selectedIndex));
			});
		}
		
		if("url" in pjson){
			obj.append("<option ></option>");
			$.myAjax({
				url:pjson.url,
				data:null,
				success: function (dataJson) {
					var array = dataJson.data;
					for(var i =0; i<array.length;i++){
						var o = array[i];
						pjson.dataArray.push(o);
					}
					for(var i =0; i<pjson.dataArray.length;i++){
						var d = pjson.dataArray[i];
						var o = $("<option ></option>");
						o.text(d.name);
						$.each( d, function(i, n){
							o.attr(i,n);
						});
						obj.append(o);
					}
					$("#"+pjson.id).selected(pjson);
				},
				error: function (jqXHR, textStatus, errorThrown) {
					$.myAlert("访问后台失败! status:"+textStatus+" msg:"+jqXHR.responseText);
				}
			});
		}else{
			for(var i =0; i<pjson.dataArray.length;i++){
				var d = pjson.dataArray[i];
				var o = $("<option ></option>");
				o.text(d.name);
				$.each( d, function(i, n){
					o.attr(i,n);
				});
				obj.append(o);
			}
			$("#"+pjson.id).selected(pjson);
		}
	};
})(jQuery);