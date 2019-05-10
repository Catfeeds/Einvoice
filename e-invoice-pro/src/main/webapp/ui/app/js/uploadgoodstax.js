var params = {};
var pagesize=10;
var channel = "app";
params.channel = channel;
$(document).ready(function(){
	
//	$('#fileupload').fileupload({
//        url: $.getService("ui","uploadGoodstax?token="+$.getUrlParam("token")), 
//        done: function (e, data) {
//        	alert(1);
//            $.each(data.result.files, function (index, file) {
//                $('<p/>').text(file.name).appendTo('#files');
//            });
//        }
//    });
	
	$("#fileuploader").uploadFile({
        url:$.getService("ui","uploadGoodstax?token="+$.getUrlParam("token")),
        fileName:"file",   //文件的名称，此处是变量名称，不是文件的原名称只是后台接收的参数
        dragDrop:false,  //可以取消
        abortStr:"取消",
        sequential:true,  //按顺序上传
        sequentialCount:1,  //按顺序上传
        autoSubmit :"false",  //取消自动上传
        extErrorStr:"上传文件格式不对",
        maxFileCount:100,       //上传文件数量
        sizeErrorStr:"上传文件不能大于1M", 
        showFileCounter:false,
        onSuccess:function(files,data,xhr,pd)  //上传成功事件，data为后台返回数据
        {
            //将返回的上传文件id动态加入的表单中，用于提交表单时返回给后台。
            var datas=jQuery.parseJSON(data);
              if(datas.code=="0"){
                  $("#erMsg").text("错误或重复商品编码");
                  $.myRowHTML(datas.errlist,"rowSample","rowTraget");
                  $.myAlert("数据上传失败！");
              }else if(datas.code=="1"){
                  $("#erMsg").text("上传成功数据");
                  $.myRowHTML(datas.sucMsg,"rowSample","rowTraget");
                  $.myAlert("数据上传成功！");
            }else{
                  $.myAlert("上传失败！");
              }
        	pd.statusbar.hide();
        },
        statusBarWidth:600,
        dragdropWidth:600
    });
});
function downloaderrdata(){
	 //创建Form
    var submitfrm = document.getElementById("myerrlsit");
    var token = "&token="+$.getUrlParam("token");
    submitfrm.action = "/e-invoice-pro/rest/ui/downloaderrdata?"+token;
    submitfrm.method = "post";
    submitfrm.submit();
    setTimeout(function () {
    }, 1000);
}