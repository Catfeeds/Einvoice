webpackJsonp([3],{101:function(e,t,n){var i=n(102);"string"==typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);n(20)("74e938a3",i,!1,{})},102:function(e,t,n){t=e.exports=n(19)(!1),t.push([e.i,"\n.margin-top-10[data-v-80ce5a6c] {\n  margin-top: 5px;\n}\n",""])},103:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticStyle:{height:"100%","background-color":"#e4e4e4"}},[n("van-nav-bar",{staticStyle:{"background-color":"#CC3327",color:"#ffffff"},attrs:{title:"发票信息"}},[n("van-icon",{staticStyle:{color:"#ffffff","font-size":"20px","font-weight":"bold"},attrs:{slot:"left",name:"wap-home"},slot:"left"})],1),e._v(" "),n("van-cell-group",[n("van-cell",{attrs:{title:"发票类型："}},[n("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px",color:"#CC3300"}},[e._v("电子发票")])])],1),e._v(" "),n("van-cell-group",{staticClass:"margin-top-10"},[n("van-cell",{attrs:{title:"销售方名称："}},[n("p",{staticClass:"van-ellipsis",staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[e._v(e._s(e.paramsdata.taxname))])])],1),e._v(" "),n("van-cell-group",{directives:[{name:"show",rawName:"v-show",value:!e.isMs,expression:"!isMs"}],staticClass:"margin-top-10"},[n("van-cell",[n("van-radio-group",{staticStyle:{"text-align":"left","padding-left":"20px"},on:{change:e.fptdChange},model:{value:e.fptd,callback:function(t){e.fptd=t},expression:"fptd"}},[n("van-radio",{staticStyle:{display:"inline","margin-right":"10px"},attrs:{name:"gr"}},[e._v("个人")]),e._v(" "),n("van-radio",{staticStyle:{display:"inline","margin-right":"10px"},attrs:{name:"dw"}},[e._v("单位")])],1)],1)],1),e._v(" "),n("van-cell-group",{staticClass:"margin-top-10"},[n("van-field",{attrs:{center:"",required:"",label:"发票抬头",placeholder:"请填写发票抬头",readonly:e.isMs},model:{value:e.gmfMc,callback:function(t){e.gmfMc=t},expression:"gmfMc"}},[n("van-icon",{directives:[{name:"show",rawName:"v-show",value:e.wxOk,expression:"wxOk"}],staticStyle:{display:"block","font-size":"18px",color:"#CA3415"},attrs:{slot:"button",name:"add"},on:{click:e.chooseInvoiceTitle},slot:"button"})],1)],1),e._v(" "),n("van-cell-group",{staticClass:"margin-top-10"},[n("van-field",{directives:[{name:"show",rawName:"v-show",value:e.isDw,expression:"isDw"}],attrs:{label:"纳税人识别号",placeholder:"请填写纳税人识别号"},model:{value:e.gmfNsrsbh,callback:function(t){e.gmfNsrsbh=t},expression:"gmfNsrsbh"}})],1),e._v(" "),n("van-cell-group",{staticClass:"margin-top-10"},[n("van-field",{attrs:{label:"收票人邮箱",placeholder:"请填写收票人邮箱",type:"email"},model:{value:e.recvEmail,callback:function(t){e.recvEmail=t},expression:"recvEmail"}}),e._v(" "),n("van-field",{attrs:{label:"收票人电话",placeholder:"请填写收票人电话"},model:{value:e.recvPhone,callback:function(t){e.recvPhone=t},expression:"recvPhone"}}),e._v(" "),n("van-cell",[n("p",{staticStyle:{"text-align":"left",margin:"0",color:"#CC3300"}},[e._v("邮箱或电话至少填写一项")])])],1),e._v(" "),n("van-button",{staticStyle:{"background-color":"#CC3327","margin-top":"10px"},attrs:{type:"primary","bottom-action":""},on:{click:e.nextAction}},[e._v("下一步")])],1)},o=[];i._withStripped=!0;var a={render:i,staticRenderFns:o};t.default=a},65:function(e,t,n){"use strict";function i(e){l||n(101)}Object.defineProperty(t,"__esModule",{value:!0});var o=n(80),a=n.n(o);for(var r in o)"default"!==r&&function(e){n.d(t,e,function(){return o[e]})}(r);var s=n(103),c=n.n(s),l=!1,d=n(21),u=i,p=d(a.a,c.a,!1,u,"data-v-80ce5a6c",null);p.options.__file="src\\views\\invoice-apply.vue",t.default=p.exports},72:function(e,t){!function(t,n){e.exports=function(e,t){function n(t,n,i){e.WeixinJSBridge?WeixinJSBridge.invoke(t,o(n),function(e){s(t,e,i)}):d(t,i)}function i(t,n,i){e.WeixinJSBridge?WeixinJSBridge.on(t,function(e){i&&i.trigger&&i.trigger(e),s(t,e,n)}):i?d(t,i):d(t,n)}function o(e){return e=e||{},e.appId=A.appId,e.verifyAppId=A.appId,e.verifySignType="sha1",e.verifyTimestamp=A.timestamp+"",e.verifyNonceStr=A.nonceStr,e.verifySignature=A.signature,e}function a(e){return{timeStamp:e.timestamp+"",nonceStr:e.nonceStr,package:e.package,paySign:e.paySign,signType:e.signType||"SHA1"}}function r(e){return e.postalCode=e.addressPostalCode,delete e.addressPostalCode,e.provinceName=e.proviceFirstStageName,delete e.proviceFirstStageName,e.cityName=e.addressCitySecondStageName,delete e.addressCitySecondStageName,e.countryName=e.addressCountiesThirdStageName,delete e.addressCountiesThirdStageName,e.detailInfo=e.addressDetailInfo,delete e.addressDetailInfo,e}function s(e,t,n){"openEnterpriseChat"==e&&(t.errCode=t.err_code),delete t.err_code,delete t.err_desc,delete t.err_detail;var i=t.errMsg;i||(i=t.err_msg,delete t.err_msg,i=c(e,i),t.errMsg=i),(n=n||{})._complete&&(n._complete(t),delete n._complete),i=t.errMsg||"",A.debug&&!n.isInnerInvoke&&alert(JSON.stringify(t));var o=i.indexOf(":");switch(i.substring(o+1)){case"ok":n.success&&n.success(t);break;case"cancel":n.cancel&&n.cancel(t);break;default:n.fail&&n.fail(t)}n.complete&&n.complete(t)}function c(e,t){var n=e,i=v[n];i&&(n=i);var o="ok";if(t){var a=t.indexOf(":");"confirm"==(o=t.substring(a+1))&&(o="ok"),"failed"==o&&(o="fail"),-1!=o.indexOf("failed_")&&(o=o.substring(7)),-1!=o.indexOf("fail_")&&(o=o.substring(5)),"access denied"!=(o=(o=o.replace(/_/g," ")).toLowerCase())&&"no permission to execute"!=o||(o="permission denied"),"config"==n&&"function not exist"==o&&(o="ok"),""==o&&(o="fail")}return t=n+":"+o}function l(e){if(e){for(var t=0,n=e.length;t<n;++t){var i=e[t],o=h[i];o&&(e[t]=o)}return e}}function d(e,t){if(!(!A.debug||t&&t.isInnerInvoke)){var n=v[e];n&&(e=n),t&&t._complete&&delete t._complete,console.log('"'+e+'",',t||"")}}function u(e){if(!(I||x||A.debug||P<"6.0.2"||C.systemType<0)){var t=new Image;C.appId=A.appId,C.initTime=b.initEndTime-b.initStartTime,C.preVerifyTime=b.preVerifyEndTime-b.preVerifyStartTime,B.getNetworkType({isInnerInvoke:!0,success:function(e){C.networkType=e.networkType;var n="https://open.weixin.qq.com/sdk/report?v="+C.version+"&o="+C.isPreVerifyOk+"&s="+C.systemType+"&c="+C.clientVersion+"&a="+C.appId+"&n="+C.networkType+"&i="+C.initTime+"&p="+C.preVerifyTime+"&u="+C.url;t.src=n}})}}function p(){return(new Date).getTime()}function f(t){k&&(e.WeixinJSBridge?"preInject"===_.__wxjsjs__isPreInject?_.addEventListener&&_.addEventListener("WeixinJSBridgeReady",t,!1):t():_.addEventListener&&_.addEventListener("WeixinJSBridgeReady",t,!1))}function m(){B.invoke||(B.invoke=function(t,n,i){e.WeixinJSBridge&&WeixinJSBridge.invoke(t,o(n),i)},B.on=function(t,n){e.WeixinJSBridge&&WeixinJSBridge.on(t,n)})}function g(e){if("string"==typeof e&&e.length>0){var t=e.split("?")[0],n=e.split("?")[1];return t+=".html",void 0!==n?t+"?"+n:t}}if(!e.jWeixin){var h={config:"preVerifyJSAPI",onMenuShareTimeline:"menu:share:timeline",onMenuShareAppMessage:"menu:share:appmessage",onMenuShareQQ:"menu:share:qq",onMenuShareWeibo:"menu:share:weiboApp",onMenuShareQZone:"menu:share:QZone",previewImage:"imagePreview",getLocation:"geoLocation",openProductSpecificView:"openProductViewWithPid",addCard:"batchAddCard",openCard:"batchViewCard",chooseWXPay:"getBrandWCPayRequest",openEnterpriseRedPacket:"getRecevieBizHongBaoRequest",startSearchBeacons:"startMonitoringBeacons",stopSearchBeacons:"stopMonitoringBeacons",onSearchBeacons:"onBeaconsInRange",consumeAndShareCard:"consumedShareCard",openAddress:"editAddress"},v=function(){var e={};for(var t in h)e[h[t]]=t;return e}(),_=e.document,y=_.title,w=navigator.userAgent.toLowerCase(),S=navigator.platform.toLowerCase(),I=!(!S.match("mac")&&!S.match("win")),x=-1!=w.indexOf("wxdebugger"),k=-1!=w.indexOf("micromessenger"),M=-1!=w.indexOf("android"),T=-1!=w.indexOf("iphone")||-1!=w.indexOf("ipad"),P=function(){var e=w.match(/micromessenger\/(\d+\.\d+\.\d+)/)||w.match(/micromessenger\/(\d+\.\d+)/);return e?e[1]:""}(),b={initStartTime:p(),initEndTime:0,preVerifyStartTime:0,preVerifyEndTime:0},C={version:1,appId:"",initTime:0,preVerifyTime:0,networkType:"",isPreVerifyOk:1,systemType:T?1:M?2:-1,clientVersion:P,url:encodeURIComponent(location.href)},A={},E={_completes:[]},N={state:0,data:{}};f(function(){b.initEndTime=p()});var V=!1,O=[],B={config:function(e){A=e,d("config",e);var t=!1!==A.check;f(function(){if(t)n(h.config,{verifyJsApiList:l(A.jsApiList)},function(){E._complete=function(e){b.preVerifyEndTime=p(),N.state=1,N.data=e},E.success=function(e){C.isPreVerifyOk=0},E.fail=function(e){E._fail?E._fail(e):N.state=-1};var e=E._completes;return e.push(function(){u()}),E.complete=function(t){for(var n=0,i=e.length;n<i;++n)e[n]();E._completes=[]},E}()),b.preVerifyStartTime=p();else{N.state=1;for(var e=E._completes,i=0,o=e.length;i<o;++i)e[i]();E._completes=[]}}),m()},ready:function(e){0!=N.state?e():(E._completes.push(e),!k&&A.debug&&e())},error:function(e){P<"6.0.2"||(-1==N.state?e(N.data):E._fail=e)},checkJsApi:function(e){var t=function(e){var t=e.checkResult;for(var n in t){var i=v[n];i&&(t[i]=t[n],delete t[n])}return e};n("checkJsApi",{jsApiList:l(e.jsApiList)},(e._complete=function(e){if(M){var n=e.checkResult;n&&(e.checkResult=JSON.parse(n))}e=t(e)},e))},onMenuShareTimeline:function(e){i(h.onMenuShareTimeline,{complete:function(){n("shareTimeline",{title:e.title||y,desc:e.title||y,img_url:e.imgUrl||"",link:e.link||location.href,type:e.type||"link",data_url:e.dataUrl||""},e)}},e)},onMenuShareAppMessage:function(e){i(h.onMenuShareAppMessage,{complete:function(){n("sendAppMessage",{title:e.title||y,desc:e.desc||"",link:e.link||location.href,img_url:e.imgUrl||"",type:e.type||"link",data_url:e.dataUrl||""},e)}},e)},onMenuShareQQ:function(e){i(h.onMenuShareQQ,{complete:function(){n("shareQQ",{title:e.title||y,desc:e.desc||"",img_url:e.imgUrl||"",link:e.link||location.href},e)}},e)},onMenuShareWeibo:function(e){i(h.onMenuShareWeibo,{complete:function(){n("shareWeiboApp",{title:e.title||y,desc:e.desc||"",img_url:e.imgUrl||"",link:e.link||location.href},e)}},e)},onMenuShareQZone:function(e){i(h.onMenuShareQZone,{complete:function(){n("shareQZone",{title:e.title||y,desc:e.desc||"",img_url:e.imgUrl||"",link:e.link||location.href},e)}},e)},startRecord:function(e){n("startRecord",{},e)},stopRecord:function(e){n("stopRecord",{},e)},onVoiceRecordEnd:function(e){i("onVoiceRecordEnd",e)},playVoice:function(e){n("playVoice",{localId:e.localId},e)},pauseVoice:function(e){n("pauseVoice",{localId:e.localId},e)},stopVoice:function(e){n("stopVoice",{localId:e.localId},e)},onVoicePlayEnd:function(e){i("onVoicePlayEnd",e)},uploadVoice:function(e){n("uploadVoice",{localId:e.localId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},downloadVoice:function(e){n("downloadVoice",{serverId:e.serverId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},translateVoice:function(e){n("translateVoice",{localId:e.localId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},chooseImage:function(e){n("chooseImage",{scene:"1|2",count:e.count||9,sizeType:e.sizeType||["original","compressed"],sourceType:e.sourceType||["album","camera"]},(e._complete=function(e){if(M){var t=e.localIds;t&&(e.localIds=JSON.parse(t))}},e))},getLocation:function(e){},previewImage:function(e){n(h.previewImage,{current:e.current,urls:e.urls},e)},uploadImage:function(e){n("uploadImage",{localId:e.localId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},downloadImage:function(e){n("downloadImage",{serverId:e.serverId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},getLocalImgData:function(e){!1===V?(V=!0,n("getLocalImgData",{localId:e.localId},(e._complete=function(e){if(V=!1,O.length>0){var t=O.shift();wx.getLocalImgData(t)}},e))):O.push(e)},getNetworkType:function(e){var t=function(e){var t=e.errMsg;e.errMsg="getNetworkType:ok";var n=e.subtype;if(delete e.subtype,n)e.networkType=n;else{var i=t.indexOf(":"),o=t.substring(i+1);switch(o){case"wifi":case"edge":case"wwan":e.networkType=o;break;default:e.errMsg="getNetworkType:fail"}}return e};n("getNetworkType",{},(e._complete=function(e){e=t(e)},e))},openLocation:function(e){n("openLocation",{latitude:e.latitude,longitude:e.longitude,name:e.name||"",address:e.address||"",scale:e.scale||28,infoUrl:e.infoUrl||""},e)},getLocation:function(e){e=e||{},n(h.getLocation,{type:e.type||"wgs84"},(e._complete=function(e){delete e.type},e))},hideOptionMenu:function(e){n("hideOptionMenu",{},e)},showOptionMenu:function(e){n("showOptionMenu",{},e)},closeWindow:function(e){n("closeWindow",{},e=e||{})},hideMenuItems:function(e){n("hideMenuItems",{menuList:e.menuList},e)},showMenuItems:function(e){n("showMenuItems",{menuList:e.menuList},e)},hideAllNonBaseMenuItem:function(e){n("hideAllNonBaseMenuItem",{},e)},showAllNonBaseMenuItem:function(e){n("showAllNonBaseMenuItem",{},e)},scanQRCode:function(e){n("scanQRCode",{needResult:(e=e||{}).needResult||0,scanType:e.scanType||["qrCode","barCode"]},(e._complete=function(e){if(T){var t=e.resultStr;if(t){var n=JSON.parse(t);e.resultStr=n&&n.scan_code&&n.scan_code.scan_result}}},e))},openAddress:function(e){n(h.openAddress,{},(e._complete=function(e){e=r(e)},e))},openProductSpecificView:function(e){n(h.openProductSpecificView,{pid:e.productId,view_type:e.viewType||0,ext_info:e.extInfo},e)},addCard:function(e){for(var t=e.cardList,i=[],o=0,a=t.length;o<a;++o){var r=t[o],s={card_id:r.cardId,card_ext:r.cardExt};i.push(s)}n(h.addCard,{card_list:i},(e._complete=function(e){var t=e.card_list;if(t){for(var n=0,i=(t=JSON.parse(t)).length;n<i;++n){var o=t[n];o.cardId=o.card_id,o.cardExt=o.card_ext,o.isSuccess=!!o.is_succ,delete o.card_id,delete o.card_ext,delete o.is_succ}e.cardList=t,delete e.card_list}},e))},chooseCard:function(e){n("chooseCard",{app_id:A.appId,location_id:e.shopId||"",sign_type:e.signType||"SHA1",card_id:e.cardId||"",card_type:e.cardType||"",card_sign:e.cardSign,time_stamp:e.timestamp+"",nonce_str:e.nonceStr},(e._complete=function(e){e.cardList=e.choose_card_info,delete e.choose_card_info},e))},openCard:function(e){for(var t=e.cardList,i=[],o=0,a=t.length;o<a;++o){var r=t[o],s={card_id:r.cardId,code:r.code};i.push(s)}n(h.openCard,{card_list:i},e)},consumeAndShareCard:function(e){n(h.consumeAndShareCard,{consumedCardId:e.cardId,consumedCode:e.code},e)},chooseWXPay:function(e){n(h.chooseWXPay,a(e),e)},openEnterpriseRedPacket:function(e){n(h.openEnterpriseRedPacket,a(e),e)},startSearchBeacons:function(e){n(h.startSearchBeacons,{ticket:e.ticket},e)},stopSearchBeacons:function(e){n(h.stopSearchBeacons,{},e)},onSearchBeacons:function(e){i(h.onSearchBeacons,e)},openEnterpriseChat:function(e){n("openEnterpriseChat",{useridlist:e.userIds,chatname:e.groupName},e)},launchMiniProgram:function(e){n("launchMiniProgram",{targetAppId:e.targetAppId,path:g(e.path),envVersion:e.envVersion},e)},miniProgram:{navigateBack:function(e){e=e||{},f(function(){n("invokeMiniProgramAPI",{name:"navigateBack",arg:{delta:e.delta||1}},e)})},navigateTo:function(e){f(function(){n("invokeMiniProgramAPI",{name:"navigateTo",arg:{url:e.url}},e)})},redirectTo:function(e){f(function(){n("invokeMiniProgramAPI",{name:"redirectTo",arg:{url:e.url}},e)})},switchTab:function(e){f(function(){n("invokeMiniProgramAPI",{name:"switchTab",arg:{url:e.url}},e)})},reLaunch:function(e){f(function(){n("invokeMiniProgramAPI",{name:"reLaunch",arg:{url:e.url}},e)})},postMessage:function(e){f(function(){n("invokeMiniProgramAPI",{name:"postMessage",arg:e.data||{}},e)})},getEnv:function(t){f(function(){t({miniprogram:"miniprogram"===e.__wxjs_environment})})}}},L=1,W={};return _.addEventListener("error",function(e){if(!M){var t=e.target,n=t.tagName,i=t.src;if(("IMG"==n||"VIDEO"==n||"AUDIO"==n||"SOURCE"==n)&&-1!=i.indexOf("wxlocalresource://")){e.preventDefault(),e.stopPropagation();var o=t["wx-id"];if(o||(o=L++,t["wx-id"]=o),W[o])return;W[o]=!0,wx.ready(function(){wx.getLocalImgData({localId:i,success:function(e){t.src=e.localData}})})}}},!0),_.addEventListener("load",function(e){if(!M){var t=e.target,n=t.tagName;if(t.src,"IMG"==n||"VIDEO"==n||"AUDIO"==n||"SOURCE"==n){var i=t["wx-id"];i&&(W[i]=!1)}}},!0),t&&(e.wx=e.jWeixin=B),B}}(t)}(window)},80:function(e,t,n){"use strict";function i(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var o=n(3),a=(i(o),n(2)),r=i(a),s=n(72),c=i(s);t.default={data:function(){return{gmfMc:"",gmfNsrsbh:"",recvEmail:"",recvPhone:"",invType:1,paramsdata:"",fptd:"gr",isDw:!1,isMs:!0,wxOk:!1}},methods:{fptdChange:function(e){"gr"==e?(this.isDw=!1,this.gmfNsrsbh=""):this.isDw=!0},nextAction:function(){var e=this,t=/^1[3|4|5|7|8][0-9]{9}$/,n=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,3}$/;if(""==this.gmfMc||null==this.gmfMc)return void this.$dialog.alert({message:"必须录入发票抬头",confirmButtonText:"知道了"});if(""==this.recvEmail&&""==this.recvPhone)return void this.$dialog.alert({message:"邮箱或电话至少填写一项",confirmButtonText:"知道了"});if(""!=this.recvEmail&&!n.test(this.recvEmail))return void this.$dialog.alert({message:"邮箱格式不正确"+this.recvEmail,confirmButtonText:"知道了"});if(""!=this.recvPhone&&!t.test(this.recvPhone))return void this.$dialog.alert({message:"电话号码格式不正确"+this.recvPhone,confirmButtonText:"知道了"});var i={},o=[],a={};a.sheetid=this.paramsdata.sheetid,a.je=this.paramsdata.totalamount,o.push(a),i.requestInvoicePreviewItem=o,i.gmfMc=this.gmfMc,i.gmfNsrsbh=this.gmfNsrsbh,i.recvPhone=this.recvPhone,i.recvEmail=this.recvEmail,i.invType=this.invType,i.sheettype=this.paramsdata.sheettype,r.default.set("recvEmail",this.recvEmail),r.default.set("recvPhone",this.recvPhone),this.$http({url:"/e-invoice-pro/rest/api/getInvoicePreview",method:"post",data:i}).then(function(t){if(0==t.code){t.data[0].shopname=e.paramsdata.shopname,e.$router.push({name:"confirm",params:t.data[0]})}else e.$dialog.alert({message:t.message,confirmButtonText:"知道了"})})},isWeix:function(){return"micromessenger"==window.navigator.userAgent.toLowerCase().match(/MicroMessenger/i)},initWeixinJS:function(e){var t=this;c.default.config({debug:!1,appId:e.data.appId,timestamp:e.data.timestamp,nonceStr:e.data.noncestr,signature:e.data.signature,jsApiList:["checkJsApi"],beta:!0}),c.default.ready(function(){console.log("微信授权完成"),t.wxOk=!0}),c.default.error(function(e){console.log("微信授权失败，扫描功能受限："+e.errMsg),t.wxOk=!1})},chooseInvoiceTitle:function(){var e=this;c.default.invoke("chooseInvoiceTitle",{scene:"1"},function(t){var n=JSON.parse(t.choose_invoice_title_info);e.isDw?(e.gmfMc=n.title,e.gmfNsrsbh=n.taxNumber):e.gmfMc=n.title})}},computed:{},created:function(){var e=this;if(this.$route.params.sheetid){this.paramsdata=this.$route.params,this.gmfMc=this.paramsdata.gmfname,console.log("this.gmfMc:"+this.gmfMc),void 0!=this.gmfMc&&null!=this.gmfMc&&""!=this.gmfMc||(this.isMs=!1,this.isWeix()?(console.log("当前微信客户端"),this.$http({url:"/e-invoice-pro/rest/wx/getWeixTicket?weburl="+window.location.href,method:"post",data:{}}).then(function(t){if(0!=t.code)return void console.log("微信授权失败，扫描功能受限："+t.message);e.initWeixinJS(t)})):console.log("当前非微信客户端"));var t=r.default.set("recvEmail"),n=r.default.set("recvPhone");void 0!=t&&(this.recvEmail=r.default.set("recvEmail")),void 0!=n&&(this.recvPhone=r.default.set("recvPhone"))}}}}});