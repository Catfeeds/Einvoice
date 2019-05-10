(function($){
	var main = "http://127.0.0.1:8080/";
	var invoice = "/e-invoice-pro/rest/invoice/";
	var ui = "/e-invoice-pro/rest/ui/";
	var api = "/e-invoice-pro/rest/api/";
	var wx = "/e-invoice-pro/rest/wx/";
	var portal = "/efs-portal/rest/ui/";
	var verifyCode="/efs-portal/";
	$.getService = function(name,fuc){
		if(name=="main") return main+fuc;
		if(name=="invoice") return invoice+fuc;
		if(name=="ui") return ui+fuc;
		if(name=="wx") return wx+fuc;
		if(name=="api") return api+fuc;
		if(name=="portal") return portal+fuc;
		if(name=="verifyCode") return verifyCode+fuc;
		return main;
	};
})(jQuery);