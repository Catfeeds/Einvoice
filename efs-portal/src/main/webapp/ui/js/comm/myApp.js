function getRootPath () {
	   //获取当前网址，如： http://localhost:8080/ems/Pages/Basic/Person.jsp
	   var curWwwPath = window.document.location.href;
	   //获取主机地址之后的目录，如： /ems/Pages/Basic/Person.jsp
	   var pathName = window.document.location.pathname;
	   var pos = curWwwPath.indexOf(pathName);
	   //获取主机地址，如： http://localhost:8080
	   var localhostPath = curWwwPath.substring(0, pos);
	   //获取带"/"的项目名，如：/ems
	   var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	   return(localhostPath + projectName+'/');
	};
	        
var RootPath=getRootPath();			//项目跟路径

//设置为异步执行....
$.ajaxSetup({  
	async : true	
});

var is_load_dialog = false;

var myapp= function(){						
	
	//同步执行获取共享html内容...
	if (!is_load_dialog)
	{
		is_load_dialog = true;
		$.ajax({   
			type:"GET",
			url:RootPath+"ui/DialogDemo.html",
			async:false,
			success:function(data){				
				$("body").append(data);				
			}
		});
	}
	
	this.copy_obj = function (dest, src)
	{
		if (!src) return;
		for (var i in src)
		{
			dest[dest.hasOwnProperty(i) ? (i + '2') : i] = src[i];
		}
	}
	
	this.copy_array = function (dest, src)
	{
		for (var i = 0; i < src.length; i++)
		{
			dest.push(src[i]);
		}
	}
	
	this.persisData = function (param, _exist_data, _new_data, _keys)
	{
		if (!param)
			return;
		param.update = [];
		param.delete = [];
		param.insert = [];
		
		for (var i = 0; i < _exist_data.length; i++)
		{
			var _uni = false;
		_label_2: 
			for (var ii = 0; ii < _new_data.length; ii++)
			{
				for (var ki in _keys)
				{
					var _key = _keys[ki];
					if (_exist_data[i][_key] != _new_data[ii][_key])
					{
						continue _label_2;
					}
				}
				_uni = true;
			}
			
			if (_uni)
			{
				param.update.push(_exist_data[i]);
			}
			else 
			{
				param.delete.push(_exist_data[i]);
			}
		}
		
		var _temp = _exist_data;
		var _exist_data = _new_data;
		_new_data = _temp;
		
		for (var i = 0; i < _exist_data.length; i++)
		{
			var _uni = false;
		_label_2: 
			for (var ii = 0; ii < _new_data.length; ii++)
			{
				for (var ki in _keys)
				{
					var _key = _keys[ki];
					if (_exist_data[i][_key] != _new_data[ii][_key])
					{
						continue _label_2;
					}
				}
				_uni = true;
			}
			
			if (_uni)
			{
			}
			else 
			{
				param.insert.push(_exist_data[i]);
			}
		}
	}
	
	this.push_obj = function (dest, src)
	{
		if (!src) return;
		for (var i in src)
		{
			dest[i] = src[i];
		}
	}
	
	this.addClass = function(obj, cls) 
	{
     if (!this.hasClass(obj, cls)) obj.className += " " + cls;
  }

  this.removeClass = function (obj, cls) 
  {
		if (this.hasClass(obj, cls)) {
		  var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
		  obj.className = obj.className.replace(reg, ' ');
		}
  }
  
	this.hasClass = function (obj, cls) 
	{
		var ksk = obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
		return ksk;
	}
	
	this.copyJson = function (json)
	{
		if (typeof json == 'number' || typeof json == 'string' || typeof json == 'boolean') {
	        return json;
	    } else if (typeof json == 'object') {
	        if (json instanceof Array) {
	            var newArr = [], i, len = json.length;
	            for (i = 0; i < len; i++) {
	                newArr[i] = arguments.callee(json[i]);
	            }
	            return newArr;
	        } else {
	            var newObj = {};
	            for (var name in json) {
	                newObj[name] = arguments.callee(json[name]);
	            }
	            return newObj;
	        }
	    }
	}
		
	this.showData = function(sampleid,showid,datas)
	{
		var dataHTML = this.getRowHTML(datas, $("#" + sampleid).html());
		$("#" + showid).html(dataHTML.replace(/<tbody>/g, "").replace(/<\/tbody>/g,""));
		var _imgs = document.querySelectorAll('#' + showid + ' img[_src]');
		for (var i = 0; i < _imgs.length; i++)
		{
			_imgs[i].src = _imgs[i].attributes.getNamedItem('_src').value;
		}
		document.getElementById(showid).hidden = false;
	};
	
	this.addData = function(_data, _keys, _origin)
	{
		_keys = _keys instanceof Array ? _keys : [_keys];
		
		var _find = 0;
		
		_label1:
		for (var i = 0; i < _origin.length; i++)
		{
			var _item = _origin[i];
			for (var j = 0; j < _keys.length; j++)
			{
				var _k = _keys[j];
				if (_data[_k] != _item[_k])
				{
					continue _label1;
				}		
			}
			_find = 1;
			
			for (var k in _data)
			{
				_origin[i][k] = _data[k];
			}
		}
		
		if (!_find)
		{
			_origin.push(_data);
		}
	}
	
	this.clear_obj = function(obj)
	{
		for (var k in obj)
		{
			delete obj[k];
		}
	}
	
	this.indexdata = function (data, key_names,index_data, lowercase) 
	{	
		for (var i = 0; i < data.length; i++) 
		{
			var item = data[i];
			key_names = typeof(key_names) == 'string' ? [key_names] : key_names;
			var key = '';
			for (var j = 0; j < key_names.length; j++)
			{
				key += (':'+(lowercase ? data[i][key_names[j]].toLowerCase() : data[i][key_names[j]]));
			}
			if (key.length > 0) 
			{
				key = key.substring(1);
			}
			index_data[key] = item;
		}
	}
	
	this.getData = function (form)
	{	
		var _elements = form.elements;
		var _ret = {};
		for (var i = 0;i < _elements.length; i++)
		{
			_ele = _elements[i];
			if (!_ele.name)
				continue;
				
			if (!_ele.value)
				continue;
				
			_ret[_ele.name] = _ele.value;
		}
		return _ret;
	}
	
	this.find_index = function (index_data, key_names)
	{				
		key_names = typeof(key_names) == 'object' ? key_names : [key_names+''];
		
		var key = '';
		for (var j = 0; j < key_names.length; j++)
		{
			key += (':'+key_names[j]);
		}
		if (key.length > 0) 
		{
			key = key.substring(1);
		}
		
		return index_data[key];
	}
	
	this.setData=function(form, C) 
	{
		if (typeof C != "object") C = {};
		for (var A in C) { //原来的C为_
		        var $ = form.querySelectorAll("[name="+A+"]");
		        $ = $ ? $[0] : $;
		        if (!$) continue;
		        var B = C[A];
		        if ("SELECT" == $.nodeName)
		        {
		        	var _select = form.querySelectorAll('select[name=\'' + A + '\']>option[value=\'' + (B ? B : 0) + '\']');
							if (_select[0])
								_select[0].selected = true;
		        	continue;
		        }
		        if (B === null || B === undefined) B = "";
		        $.value=B;
		}
	}
	
	this.getWindowHeight = function () {
		var de = document.documentElement;
		return self.innerHeight||(de && de.clientHeight)||document.body.clientHeight;
	}
	
	this.getWindowWidth = function () {
		var de = document.documentElement;
		return self.innerWidth||(de && de.clientWidth)||document.body.clientWidth;
	}
	
	this.getUrlParameter=function(param,url)
	{
		url = url ? url : window.location.search;
		var params=(url.substr(url.indexOf("?")+1)).split("&");
		if (params!=null)
		{
			for(var i=0;i<params.length;i++)
			{
				var strs=params[i].split("=");
				if (strs[0]==param)
				{
					return strs[1];
				}
			}
		}
	};
				
	if (window.location.href.indexOf(RootPath+"ui/login.html")<0&&window.location.href.indexOf(RootPath+"ui/login2.html")<0)
	{	
		this.token=this.getUrlParameter("token",window.location.search);							
	}
	
	//统一进行encode编码再发送，mysuccess为http调用成功处理，myerror为http调用失败处理
	this.ajax=function(parjson){	
		
		token="&token="+this.token;
		
		//HTTP调用成功的返回
		this.mysuccess=function(text)
		{		
			
			var data={};							
			try
			{
				data = JSON.parse(text);					
				//所有服务必须有code返回,否则被标记为未知错误...
				var code=data.code;			
			} catch(e)
			{				
				data="{'code':'0','msg':'未知错误','note':"+text+"}";												
				parjson.success(data);
				return;
			}					
						
			parjson.success(text);
		};
		
		this.UrlDecode=function(zipStr){  
		    var uzipStr="";  
		    for(var i=0;i<zipStr.length;i++){  
		        var chr = zipStr.charAt(i);  
		        if(chr == "+"){  
		            uzipStr+=" ";  
		        }else if(chr=="%"){  
		            var asc = zipStr.substring(i+1,i+3);  
		            if(parseInt("0x"+asc)>0x7f){  
		                uzipStr+=decodeURI("%"+asc.toString()+zipStr.substring(i+3,i+9).toString());  
		                i+=8;  
		            }else{  
		                uzipStr+=AsciiToString(parseInt("0x"+asc));  
		                i+=2;  
		            }  
		        }else{  
		            uzipStr+= chr;  
		        }  
		    }  
		  
		    return uzipStr;  
		}
		
		//HTTP调用失败的返回
		this.myerror=function(jqXHR, textStatus, errorThrown)
		{			
			//alert(jqXHR.responseText);
			parjson.error(jqXHR, textStatus, errorThrown);
		};
		
		//默认为30秒
		this.timeout=30000;
		
		//如果参数中带有超时时间，则用参数的...
		if (("timeout" in parjson))
		{
			timeout=parjson.timeout;
		}		
		
		this.data={};
		if (("data" in parjson))
		{
			this.data.data=parjson.data;
		}							
				
		//还可以增加其他的ajax请求参数...
		$.ajax({
			url: encodeURI(parjson.url+token),
			data: this.data,
			type: parjson.type,
			success: this.mysuccess,
			error: this.myerror,
			timeout: this.timeout,
			cache:false,
		})		
	}		
	
	//UI重新渲染
	this.reloadModules=function (m)
	{
	  if(!(m instanceof Array)){console.warn('reloadModules func parameters should be an Array.');return;}
	  var modules = m || [];
	   $.each(modules, function(i, m) {
	    var module = $.AMUI[m];
	    module && module.init && module.init();
	  })
	}
	
	//判断录入项是否有值
	this.checkinput=function (a)
	{
		//判断是否存在
		if ($(a).length==0)
		{			
			return false;
		}
		
		if ($(a).val().trim()=="")
		{
			return false;
		}
		
		return true;
	}
	
	
	
	this.showMsgDialog = function (msg,btn) {				
		
		if (btn==null) btn=false;
		$("#my-modal-msg #msgcontent").html(msg);		
		$("#my-modal-msg").modal("open");
		
		if (btn)
		{ 			
			$("#my-modal-msg-from").css("height","282px");
			$("#my-modal-msg").removeClass("am-modal-no-btn");
		} else {
			$("#my-modal-msg-from").css("height","230px");
			$("#my-modal-msg").addClass("am-modal-no-btn");
		}

	}
	
	this.showLoadingDialog = function (msg) {
				
		if (msg == null || msg == "")
			msg = "正在处理，请稍候...";

		$("#my-modal-loading #msgcontent").html(msg);
		$("#my-modal-loading #loadingicon").show();
		
		//amazeui 2.4 版本 am-modal-loading 类 只能通过js关闭,这会影响到update里面的内容后也无法点击遮罩关闭....		
		$("#my-modal-loading").modal("open");

	}

	this.closeLoadingDialog = function(delay) {
		setTimeout(function() {
			$("#my-modal-loading").modal("close");
		}, delay ? delay : 300);
	}

	this.updateLoadingDialog = function (msg) {
		
		$("#my-modal-loading #msgcontent").html(msg);
		$("#my-modal-loading #loadingicon").hide();				
		
		if ($("#my-modal-loading").is(":hidden")) {
			// 如果结果返回的时候，遮罩层已经被点击关闭了则会进入这里.....
			showMsgDialog(msg,true);	
		}
	}

	this.getRowHTML = function (datajson,subHTML)
	{
		var subList="";											
		var HTMLArray = subHTML.split('||');						
		
		for (var i = 0; i <= datajson.length-1; i++)
		{														
				datajson[i].serialid=i+1;												
							
				jsonflag=false;
				
				for (var j = 0; j <= HTMLArray.length-1; j++)
				{
					
					//只要html里面不直接用||作为开头，那么用基/偶数就就可以判断是否是变量了....
					if (jsonflag==true)
					{
						var data="";
						if (datajson[i][HTMLArray[j]] != undefined)
						{
							data=datajson[i][HTMLArray[j]];
						}
						
						subList += data; 				
					} else {
						subList += HTMLArray[j];
					}	
					
					//取反
					jsonflag=!jsonflag;
				}						
		}
		
		return subList;
	}
	
}

function input_number(_input) 
{
	var reg=/[A-Za-z]+/g;
	_input.value=_input.value.replace(reg,'');
}



function input_pwd(_input) 
{
	_input._value=_input._value ? _input._value : '';
	if (_input.value.length > _input._value.length) 
	{
		_input._value += _input.value.substr(-1);	
	} 
	else 
	{
		_input._value = _input._value.substring(0,_input.value.length);	
	}
	var reg=/./g;
	_input.value=_input.value.replace(reg,'*');
}

window.addEventListener('load',ksk_onload);

var comm_app;

function ksk_onload()
{
	comm_app = new myapp();
	
	ksk_replace_html_history_back();
	
	append_ajax_iframe();
	
	ksk_table();
	
	ksk_resize();
	
	ksk_tab();
	
	ksk_scroll();
	
	ksk_load_others();
	
	remove_write_html_scripts();
	
	var ss = document.querySelectorAll('input[cflag1]');
	for (var i = 0; i < ss.length; i++)
	{
		var item = ss[i];
		item.addEventListener('input', ksk_check_input(item));
	}
}

function ksk_replace_html_history_back()
{
	var _temp = document.querySelectorAll('.ksk_replace_html_history_back');
	if (!_temp[0])
		return;
		
	_temp = _temp[0];
	
	_temp.addEventListener('click',function() {
		ksk_replace_html_func_history_url = get_ksk_replace_html_func_history_url();
		if (ksk_replace_html_func_history_url.length > 1)
		{
			ksk_replace_html(ksk_replace_html_func_history_url[ksk_replace_html_func_history_url.length - 2], true);
		} 
		else 
		{
			window.location.reload();
		}
	});
}

function remove_write_html_scripts()
{
	var _temp = document.querySelectorAll('script[ksk_write_html]');
	for (var i = 0; i < _temp.length; i++)
	{
		_temp[i].parentNode.removeChild(_temp[i]);
	}
}

function append_ajax_iframe()
{
	if (document.querySelectorAll('iframe[name=\'attach\']')[0])
		return;
	
	var _iframe = document.createElement('iframe');
	_iframe.name='attach';
	_iframe.style.display = 'none';
	document.body.appendChild(_iframe);
}

function ksk_table()
{
	var table_bodys = document.querySelectorAll("div[ksk_table_head]");
	if (!table_bodys[0])
	{
		return;
	}
	
	for (var i = 0; i < table_bodys.length; i++)
	{
		_table_body = table_bodys[i];
		append_table_head(_table_body);
		_table_body.addEventListener("scroll", get_ksk_table_scroll(_table_body));
	}
}

function append_table_head(_table_body)
{
	var temp = document.createElement("th");
	temp.innerHTML="<div style='width:10px;'></div>";
	document.querySelectorAll('#' + _table_body.attributes.getNamedItem('ksk_table_head').value + '>table>thead>tr')[0]
		.appendChild(temp);
}

function get_ksk_table_scroll(_table_body)
{
	return function() 
	{
		document.getElementById(_table_body.attributes.getNamedItem('ksk_table_head').value).scrollLeft = _table_body.scrollLeft;
	}
}

function ksk_load_others()
{
	$('.am-modal').on('opened.modal.amui', function() {
		$(".am-dimmer,.am-active").show();
	});
}

function ksk_scroll()
{
	var scrolls = document.querySelectorAll("div[ksk-scroll]");
	if (!scrolls[0])
	{
		return;
	}
	
	for (var i = 0; i < scrolls.length; i++)
	{
		_scroll = scrolls[i];
		var _fun = scroll_call(_scroll);
		_scroll._fun = _fun;
		_scroll.addEventListener("scroll", _fun);
	}
}

function scroll_call(sc) 
{
	return function()
	{
		if (sc.scrollTop + sc.clientHeight == sc.scrollHeight)
		{
			var _hiddens = sc.querySelectorAll("div[ksk-scroll=\"" 
				+ sc.attributes.getNamedItem('ksk-scroll').value 
				+ "\"] div[hidden][ksk-scroll-hidden]");
				
			var _loading = sc.querySelectorAll("div[ksk-scroll=\"" 
						+ sc.attributes.getNamedItem('ksk-scroll').value 
						+ "\"] div[ksk-scroll-loading]")
			if (!_hiddens[0])
			{
				sc.removeEventListener("scroll", sc._fun);
				return;
			}			
			_loading[0].hidden = false;
			setTimeout(function()
			{
				_hiddens[0].hidden = false;
				_loading[0].hidden = true;
				if (!_hiddens[1]) 
				{
					sc.removeEventListener("scroll", sc._fun);
					
					if (_loading[0])
					{
						_loading[0].hidden = true;
					}
					return;
				}
			},1000);
		}
	}
}

var __scrollBarWidth = null;
function getScrollBarWidth() {
	if (__scrollBarWidth) return __scrollBarWidth;
	
	var scrollBarHelper = document.createElement("div");
	// if MSIE
	// 如此设置的话，scroll bar的最大宽度不能大于100px（通常不会）。
	scrollBarHelper.style.cssText = "overflow:scroll;width:100px;height:100px;"; 
	// else OTHER Browsers:
	// scrollBarHelper.style.cssText = "overflow:scroll;";
	document.body.appendChild(scrollBarHelper);
	if (scrollBarHelper) {
	   __scrollBarWidth = {
	       horizontal: scrollBarHelper.offsetHeight - scrollBarHelper.clientHeight,
	       vertical: scrollBarHelper.offsetWidth - scrollBarHelper.clientWidth
	   };
	}
	document.body.removeChild(scrollBarHelper);
	return __scrollBarWidth;
}

function ksk_resize()
{
	ksk_window_width();
	ksk_window_height();
	
	ksk_resize_height();
	
	ksk_new_resize_width();
	ksk_new_resize_height();
}

function ksk_window_width()
{
	var _ary = document.querySelectorAll(".ksk-window-width");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		_item.style.width = comm_app.getWindowWidth() + 'px';
	}
	
	var _ary = document.querySelectorAll(".ksk-window-scroll-width");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		_item.style.width = (comm_app.getWindowWidth() - getScrollBarWidth().vertical)+ 'px';
	}
	
}

function ksk_window_height()
{
	var _ary = document.querySelectorAll(".ksk-window-height");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		_item.style.height = comm_app.getWindowHeight() + 'px';
	}
	
	var _ary = document.querySelectorAll(".ksk-window-scroll-height");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		_item.style.height = (comm_app.getWindowHeight() - getScrollBarHeight().vertical)+ 'px';
	}
	
}

function ksk_new_resize_width()
{
	var _ary = document.querySelectorAll(".ksk_new_width_adaptive");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		var _parent_length = _item.parentNode.clientWidth;
		var _length = _parent_length;
		var _sub_len = '0 0';
		try {
			_sub_len = _item.attributes.getNamedItem('ksk_sub_length').value;
		} catch (e) {
		}
		_sub_len = _sub_len.split(' ');
		_length -= (_sub_len[1]);
		var _ary2 = _item.parentNode.childNodes;
		for (var j = 0; j < _ary2.length; j++)
		{
			var _item2 = _ary2[j];
			if (_item2.nodeName == '#text')
				continue;
			if (_item2 == _item)
				continue;
			_length -= _item2.clientWidth;
		}
		
		_item.style.width = _length + 'px';
	}
}

function ksk_new_resize_height()
{
	var _ary = document.querySelectorAll(".ksk_new_height_adaptive");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		var _parent_length = _item.parentNode.clientHeight;
		var _length = _parent_length;
		var _sub_len = '0 0';
		try {
			_sub_len = _item.attributes.getNamedItem('ksk_sub_length').value;
		} catch (e) {
		}
		_sub_len = _sub_len.split(' ');
		_length -= (_sub_len[0]);
		var _ary2 = _item.parentNode.childNodes;
		for (var j = 0; j < _ary2.length; j++)
		{
			var _item2 = _ary2[j];
			if (_item2.nodeName == '#text')
				continue;
			if (_item2 == _item)
				continue;
			_length -= _item2.clientHeight;
		}
		
		_item.style.height = _length + 'px';
	}
}

function ksk_resize_height()
{
	var _ary = document.querySelectorAll("div[ksk_height_parent]");
	for (var i = 0; i < _ary.length; i++)
	{
		var _item = _ary[i];
		var _ary2 = document.querySelectorAll("div[ksk_height_group='" + _item.attributes.getNamedItem('ksk_height_group').value + "']");
		var _height_parent = _item.attributes.getNamedItem('ksk_height_parent').value;
		var _length = (_height_parent && _height_parent != '') ? document.getElementById(_height_parent).clientHeight :  comm_app.getWindowHeight();
		var _sub_len = 0;
		try {
			_sub_len = parseInt(_item.attributes.getNamedItem('ksk_sub_length').value);
		} catch (e) {
		}
		_length -= _sub_len;
		for (var j = 0; j < _ary2.length; j++)
		{
			var _item2 = _ary2[j];
			if (_item2 == _item)
				continue;
			_length -= _item2.clientHeight;
		}
		
		_item.style.height = _length + 'px';
	}
}

function ksk_tab()
{
	var mytabs = document.querySelectorAll('table[my-data-tab]');
	for (var i = 0; i < mytabs.length; i++)
	{
		var mytab = mytabs[i];
		var heads = mytab.querySelectorAll('tr>td>div');
		for (var j = 0; j < heads.length; j++)
		{
			var head = heads[j];
			head.parentNode.style.width=(100/(heads.length)) + '%';
			head.addEventListener('click', ksk_tab_click(head, mytab));
		}
	}
}

function ksk_tab_click(myhead, mytab)
{
	return function () {
		var heads = mytab.querySelectorAll('tr>td>div');
		var my_index = 0;
		for (var j = 0; j < heads.length; j++)
		{
			var head = heads[j];
			comm_app.removeClass(head,'my-tab-active');
			if (head == myhead)
			{
				my_index = j;
			}
		}
		my_index;
		comm_app.addClass(myhead,'my-tab-active');
		var tabid = mytab.attributes.getNamedItem('my-data-tab').value;
		var tab_body = document.querySelectorAll("." + tabid);
		if (tab_body.length == 0)
		{
			console.warn("not exists tab body array");
			return;
		}
		
		tab_body = tab_body[0];
		var bodys = tab_body.querySelectorAll("div." + tabid + ">div");
		
		for (var i = 0; i < bodys.length; i++)
		{
			var _body = bodys[i];
			_body.hidden = true; 
		}
		
		if (!bodys[my_index])
		{
			console.warn("not exists click tab body");
			return;
		}
		
		bodys[my_index].hidden = false;
		bodys[my_index].style.animation="ksk_tab_move 1s ";
	}
}

var ksk_check_input_buf = {};

function ksk_check_input(item)
{
	var tablename = item.attributes.getNamedItem('cflag1').value;
	return function()
	{
		if (ksk_check_input_buf[tablename])
		{
			handle_ksk_check_input(ksk_check_input_buf[tablename],item);
			return;
		}
		var data={tablename:tablename};
		$.ajax({     
			url:'/efs-ui/rest/ui/module/descTable.action',     
			type:'post',     
			data:{data:JSON.stringify(data)},
			async : true, //默认为true 异步     
			error:function(){  
				comm_app.showLoadingDialog('');   
				comm_app.updateLoadingDialog("亲，访问接口失败，请联系管理员!");
			},     
			success:function(data){    
				ksk_check_input_buf[tablename] =  JSON.parse(data);
				handle_ksk_check_input(ksk_check_input_buf[tablename],item);
			}  
		}); 
	}
}

function handle_ksk_check_input(data, item) 
{
	if (data.code==0) 
	{
		comm_app.showLoadingDialog('');
		comm_app.updateLoadingDialog("亲，表结构获取失败!失败原因:" + data.msg);
		return;
	}
	var table = {};
	data = data.data;
	comm_app.indexdata(data, ['field'], table,true);	
	if (!table[item.name])
	{
		console.warn('handle_ksk_check_input:error');
		return;
	}
	var type = table[item.name].type;
	if (type.indexOf('varchar') != -1)
	{
		var len = type.substring(type.indexOf('(')+1,type.lastIndexOf(')'));
		if (item.value.length > len)
		{
			comm_app.showLoadingDialog('');   
			comm_app.updateLoadingDialog("亲，此输入框最大长度为："+len+"，</br>如果过长则自动截断呃!");
			myvalue.closeLoadingDialog(1500);  
			item.value = item.value.substring(0, len);
		}
	} else if (type.indexOf('int') != -1)
	{
		
		var maxint = 2147483647;
		if (parseInt(item.value) > maxint)
		{
			comm_app.showLoadingDialog('');   
			comm_app.updateLoadingDialog("亲，此输入框最大数字为："+maxint+"呃!");
			myvalue.closeLoadingDialog(1500);  
			item.value = maxint;
		}
	} else if (type.indexOf('decimal') != -1)
	{
		
		var decimal = type.substring(type.indexOf('(')+1,type.lastIndexOf(')')).split(',');
		var maxvalue = Number((Math.pow(10, parseInt(decimal[0]) - parseInt(decimal[1])) - 1) + '.' + (Math.pow(10,parseInt(decimal[1])) - 1));
		var minvalue = 0 - maxvalue;
		if (item.value.indexOf('.') != -1 &&
			 	(item.value.substring(item.value.indexOf('.')+1).length > parseInt(decimal[1]))
			 )
		{
			comm_app.showLoadingDialog('');   
			comm_app.updateLoadingDialog("亲，此输入框最多支持"+decimal[1]+"位小数呃!");
			myvalue.closeLoadingDialog(1500);  
			item.value = parseInt(item.value) + '.' 
				+ item.value.substring(item.value.indexOf('.')+1).substring(0,parseInt(decimal[1]));
			return;
		}
		
		if (Number(item.value) > maxvalue)
		{
			comm_app.showLoadingDialog('');   
			comm_app.updateLoadingDialog("亲，此输入框最大只能输入"+maxvalue+"呃!");
			myvalue.closeLoadingDialog(1500);  
			item.value = maxvalue;
			return;
		}
		
		if (Number(item.value) < minvalue)
		{
			comm_app.showLoadingDialog('');   
			comm_app.updateLoadingDialog("亲，此输入框最小只能输入"+minvalue+"呃!");
			myvalue.closeLoadingDialog(1500);  
			item.value = minvalue;
			return;
		}
	}
}

function _ksk_ajax()
{
	var XMLHttpReq;
	function createXMLHttpRequest() {
	    try {
	        XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");//IE高版本创建XMLHTTP
	    }
	    catch(E) {
	        try {
	            XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");//IE低版本创建XMLHTTP
	        }
	        catch(E) {
	            XMLHttpReq = new XMLHttpRequest();//兼容非IE浏览器，直接创建XMLHTTP对象
	        }
	    }
	
	}
	this.sendAjaxRequest = function (url,func) {
	    createXMLHttpRequest();                                //创建XMLHttpRequest对象
	    XMLHttpReq.open("post", url, false);
	    XMLHttpReq.onreadystatechange = processResponse(func); //指定响应函数
	    XMLHttpReq.send(null);
	}
	//回调函数
	function processResponse(func) {
		return function() {
		    if (XMLHttpReq.readyState == 4) {
		        if (XMLHttpReq.status == 200) {
		            var text = XMLHttpReq.responseText;
		            func(text);
		        }
		    }
		};
	}
}

var ksk_ajax = new _ksk_ajax();

function ksk_write_html(_filename, _entry)
{
	ksk_ajax.sendAjaxRequest(_filename,ksk_write_html_func(_entry));
};

function ksk_replace_html(_filename, isback)
{
	ksk_ajax.sendAjaxRequest(_filename,ksk_replace_html_func);
	
	if (isback)
	{
		pop_ksk_replace_html_func_history_url();
	}
	else 
	{
		push_ksk_replace_html_func_history_url(_filename);
	}
};

function pop_ksk_replace_html_func_history_url()
{
	ksk_replace_html_func_history_url = get_ksk_replace_html_func_history_url();
	ksk_replace_html_func_history_url.pop();
	persist_ksk_replace_html_func_history_url();
}

function push_ksk_replace_html_func_history_url(_filename)
{
	ksk_replace_html_func_history_url = get_ksk_replace_html_func_history_url();
	ksk_replace_html_func_history_url.push(_filename);
	persist_ksk_replace_html_func_history_url();
}

function get_ksk_replace_html_func_history_url()
{
	var url = window.localStorage.ksk_replace_html_func_history_url;
	return url ? JSON.parse(url) : ksk_replace_html_func_history_url;
}

function persist_ksk_replace_html_func_history_url()
{
	window.localStorage.ksk_replace_html_func_history_url = JSON.stringify(ksk_replace_html_func_history_url);
}

function clear_ksk_replace_html_func_history_url()
{
	localStorage.removeItem('ksk_replace_html_func_history_url');
}

function ksk_write_html_func(_entry)
{
	return function(text) 
	{
		if (_entry) 
		{
			for (var _key in _entry)
			{
				var _value = _entry[_key];
				text = text.replace(new RegExp(_key,'g'), _value);
			}
		}
		
		document.write(text);	
	}
}

var ksk_replace_html_func_history_url = [];

function ksk_replace_html_func(text)
{
	window.temp_load = null;
	var new_html = '<html>';
	var _html = document.getElementsByTagName('html')[0].innerHTML;
	var _index;
	if (_html.indexOf('<style') < _html.indexOf('<body'))
	{
		_index = _html.indexOf('<style');
		new_html += _html.substring(0, _index);
		_html = _html.substring(_index);
		_index = _html.indexOf('</style>');
		_html = _html.substring(_index + '</style>'.length);
	}
	
	_index = _html.indexOf('<body');
	new_html += _html.substring(0, _index);
	
	_html = _html.substring(_index);
	_index = _html.indexOf('>');
	new_html += _html.substring(0, _index + 1);
	
	new_html += text;
	
	_index = _html.indexOf('</body>');
	new_html += _html.substring(_index);
	new_html += '</html>';
	document.write(new_html);
	document.close();
	
	ksk_replace_html_load_func();
}

function ksk_replace_html_load_func()
{
	setTimeout(function() {
		if (window.temp_load)
		{
			ksk_onload();
			window.temp_load();
		} else 
		{
			ksk_replace_html_load_func();
		}
	},10);
}

