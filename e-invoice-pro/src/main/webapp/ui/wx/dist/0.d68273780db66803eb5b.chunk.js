webpackJsonp([0],{119:function(e,n,t){var i=t(120);"string"==typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);t(20)("e5f8be94",i,!1,{})},120:function(e,n,t){n=e.exports=t(19)(!1),n.push([e.i,'\nimg[data-v-c8422074] { pointer-events: none;\n}\n.title[data-v-c8422074] {\r\n  color: #ca1100;\r\n  padding: 0 20px;\r\n  font-size: 14px;\n}\n.scan[data-v-c8422074] {\r\n  text-align: center;\r\n  margin-top: 20px;\n}\n.scan-icon[data-v-c8422074] {\r\n  font-size: 8rem;\r\n  color: #999;\n}\n.scan-p[data-v-c8422074] {\r\n  font-size: 14px;\r\n  color: #CC3327;\n}\n.scan-img[data-v-c8422074]{\r\n  width: 33%;\n}\n.info[data-v-c8422074] {\r\n  padding: 0 10px;\n}\n.info-title[data-v-c8422074] {\r\n  text-align: center;\r\n  font-size: 14px;\r\n  position: relative;\r\n  display: block;\r\n  color: #6f6666;\n}\n.info-title[data-v-c8422074]:before,\r\n.info-title[data-v-c8422074]:after {\r\n  content: "";\r\n  position: absolute;\r\n  top: 52%;\r\n  background: #6f6666;\r\n  width: 36%;\r\n  height: 1px;\n}\n.info-title[data-v-c8422074]:before {\r\n  left: 0;\n}\n.info-title[data-v-c8422074]:after {\r\n  right: 0;\n}\n.info-content[data-v-c8422074] {\r\n  color: #6f6666;\r\n  font-size: 14px;\n}\n.info-content-m[data-v-c8422074] {\r\n  text-indent: 28px;\n}\r\n',""])},121:function(e,n,t){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var i=function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("div",{staticStyle:{padding:"10px 5px"}},[t("div",{staticStyle:{height:"280px",top:"0px"}},[t("div",{staticClass:"scan"},[t("van-row",[t("van-col",{staticClass:"scan-p",staticStyle:{color:"#CC3327"},attrs:{offset:"14",span:"8"}},[e._v("点这里")])],1),e._v(" "),t("van-row",[t("div",{staticStyle:{width:"100%"},on:{click:e.scanAction}},[t("van-col",{attrs:{offset:"6",span:"12"}},[t("img",{staticStyle:{width:"100%"},attrs:{src:e.pic3,onclick:"return false"}})])],1)]),e._v(" "),t("p",{staticClass:"scan-p",staticStyle:{color:"#CC3327"}},[e._v("点击扫描二维码")])],1)]),e._v(" "),t("div",{staticClass:"info"},[t("p",{staticClass:"info-title",on:{click:e.showHelp}},[e._v("开票说明 "),t("van-icon",{attrs:{name:"question"}})],1),e._v(" "),t("div",{directives:[{name:"show",rawName:"v-show",value:e.helpDisplay,expression:"helpDisplay"}],staticClass:"info-content"},[t("p",[e._v("尊敬的顾客：")]),e._v(" "),t("p",{staticClass:"info-content-m"},[e._v("免税商品开票，需在机场办理提货手续后，方可开具电子发票。")]),e._v(" "),t("p",{staticClass:"info-content-m"},[e._v("可通过扫描销售小票上的二维码，即可开具发票。")]),e._v(" "),t("van-row",{attrs:{gutter:"10"}},[t("van-col",{attrs:{span:"12"}},[t("p",{staticStyle:{"text-align":"center","font-size":"12px"}},[e._v("免税销售小票")]),e._v(" "),t("img",{staticStyle:{width:"100%"},attrs:{src:e.pic1,alt:""}})]),e._v(" "),t("van-col",{attrs:{span:"12"}},[t("p",{staticStyle:{"text-align":"center","font-size":"12px"}},[e._v("有税销售小票")]),e._v(" "),t("img",{staticStyle:{width:"100%"},attrs:{src:e.pic2,alt:""}})])],1)],1)])])},o=[];i._withStripped=!0;var a={render:i,staticRenderFns:o};n.default=a},65:function(e,n,t){"use strict";function i(e){d||t(119)}Object.defineProperty(n,"__esModule",{value:!0});var o=t(93),a=t.n(o);for(var r in o)"default"!==r&&function(e){t.d(n,e,function(){return o[e]})}(r);var c=t(121),s=t.n(c),d=!1,l=t(21),u=i,p=l(a.a,s.a,!1,u,"data-v-c8422074",null);p.options.__file="src/views/home/scan6922.vue",n.default=p.exports},76:function(e,n){!function(n,t){e.exports=function(e,n){function t(n,t,i){e.WeixinJSBridge?WeixinJSBridge.invoke(n,o(t),function(e){c(n,e,i)}):l(n,i)}function i(n,t,i){e.WeixinJSBridge?WeixinJSBridge.on(n,function(e){i&&i.trigger&&i.trigger(e),c(n,e,t)}):i?l(n,i):l(n,t)}function o(e){return e=e||{},e.appId=A.appId,e.verifyAppId=A.appId,e.verifySignType="sha1",e.verifyTimestamp=A.timestamp+"",e.verifyNonceStr=A.nonceStr,e.verifySignature=A.signature,e}function a(e){return{timeStamp:e.timestamp+"",nonceStr:e.nonceStr,package:e.package,paySign:e.paySign,signType:e.signType||"SHA1"}}function r(e){return e.postalCode=e.addressPostalCode,delete e.addressPostalCode,e.provinceName=e.proviceFirstStageName,delete e.proviceFirstStageName,e.cityName=e.addressCitySecondStageName,delete e.addressCitySecondStageName,e.countryName=e.addressCountiesThirdStageName,delete e.addressCountiesThirdStageName,e.detailInfo=e.addressDetailInfo,delete e.addressDetailInfo,e}function c(e,n,t){"openEnterpriseChat"==e&&(n.errCode=n.err_code),delete n.err_code,delete n.err_desc,delete n.err_detail;var i=n.errMsg;i||(i=n.err_msg,delete n.err_msg,i=s(e,i),n.errMsg=i),(t=t||{})._complete&&(t._complete(n),delete t._complete),i=n.errMsg||"",A.debug&&!t.isInnerInvoke&&alert(JSON.stringify(n));var o=i.indexOf(":");switch(i.substring(o+1)){case"ok":t.success&&t.success(n);break;case"cancel":t.cancel&&t.cancel(n);break;default:t.fail&&t.fail(n)}t.complete&&t.complete(n)}function s(e,n){var t=e,i=h[t];i&&(t=i);var o="ok";if(n){var a=n.indexOf(":");"confirm"==(o=n.substring(a+1))&&(o="ok"),"failed"==o&&(o="fail"),-1!=o.indexOf("failed_")&&(o=o.substring(7)),-1!=o.indexOf("fail_")&&(o=o.substring(5)),"access denied"!=(o=(o=o.replace(/_/g," ")).toLowerCase())&&"no permission to execute"!=o||(o="permission denied"),"config"==t&&"function not exist"==o&&(o="ok"),""==o&&(o="fail")}return n=t+":"+o}function d(e){if(e){for(var n=0,t=e.length;n<t;++n){var i=e[n],o=v[i];o&&(e[n]=o)}return e}}function l(e,n){if(!(!A.debug||n&&n.isInnerInvoke)){var t=h[e];t&&(e=t),n&&n._complete&&delete n._complete,console.log('"'+e+'",',n||"")}}function u(e){if(!(I||x||A.debug||M<"6.0.2"||b.systemType<0)){var n=new Image;b.appId=A.appId,b.initTime=P.initEndTime-P.initStartTime,b.preVerifyTime=P.preVerifyEndTime-P.preVerifyStartTime,E.getNetworkType({isInnerInvoke:!0,success:function(e){b.networkType=e.networkType;var t="https://open.weixin.qq.com/sdk/report?v="+b.version+"&o="+b.isPreVerifyOk+"&s="+b.systemType+"&c="+b.clientVersion+"&a="+b.appId+"&n="+b.networkType+"&i="+b.initTime+"&p="+b.preVerifyTime+"&u="+b.url;n.src=t}})}}function p(){return(new Date).getTime()}function f(n){k&&(e.WeixinJSBridge?"preInject"===_.__wxjsjs__isPreInject?_.addEventListener&&_.addEventListener("WeixinJSBridgeReady",n,!1):n():_.addEventListener&&_.addEventListener("WeixinJSBridgeReady",n,!1))}function g(){E.invoke||(E.invoke=function(n,t,i){e.WeixinJSBridge&&WeixinJSBridge.invoke(n,o(t),i)},E.on=function(n,t){e.WeixinJSBridge&&WeixinJSBridge.on(n,t)})}function m(e){if("string"==typeof e&&e.length>0){var n=e.split("?")[0],t=e.split("?")[1];return n+=".html",void 0!==t?n+"?"+t:n}}if(!e.jWeixin){var v={config:"preVerifyJSAPI",onMenuShareTimeline:"menu:share:timeline",onMenuShareAppMessage:"menu:share:appmessage",onMenuShareQQ:"menu:share:qq",onMenuShareWeibo:"menu:share:weiboApp",onMenuShareQZone:"menu:share:QZone",previewImage:"imagePreview",getLocation:"geoLocation",openProductSpecificView:"openProductViewWithPid",addCard:"batchAddCard",openCard:"batchViewCard",chooseWXPay:"getBrandWCPayRequest",openEnterpriseRedPacket:"getRecevieBizHongBaoRequest",startSearchBeacons:"startMonitoringBeacons",stopSearchBeacons:"stopMonitoringBeacons",onSearchBeacons:"onBeaconsInRange",consumeAndShareCard:"consumedShareCard",openAddress:"editAddress"},h=function(){var e={};for(var n in v)e[v[n]]=n;return e}(),_=e.document,S=_.title,y=navigator.userAgent.toLowerCase(),w=navigator.platform.toLowerCase(),I=!(!w.match("mac")&&!w.match("win")),x=-1!=y.indexOf("wxdebugger"),k=-1!=y.indexOf("micromessenger"),T=-1!=y.indexOf("android"),C=-1!=y.indexOf("iphone")||-1!=y.indexOf("ipad"),M=function(){var e=y.match(/micromessenger\/(\d+\.\d+\.\d+)/)||y.match(/micromessenger\/(\d+\.\d+)/);return e?e[1]:""}(),P={initStartTime:p(),initEndTime:0,preVerifyStartTime:0,preVerifyEndTime:0},b={version:1,appId:"",initTime:0,preVerifyTime:0,networkType:"",isPreVerifyOk:1,systemType:C?1:T?2:-1,clientVersion:M,url:encodeURIComponent(location.href)},A={},V={_completes:[]},B={state:0,data:{}};f(function(){P.initEndTime=p()});var O=!1,L=[],E={config:function(e){A=e,l("config",e);var n=!1!==A.check;f(function(){if(n)t(v.config,{verifyJsApiList:d(A.jsApiList)},function(){V._complete=function(e){P.preVerifyEndTime=p(),B.state=1,B.data=e},V.success=function(e){b.isPreVerifyOk=0},V.fail=function(e){V._fail?V._fail(e):B.state=-1};var e=V._completes;return e.push(function(){u()}),V.complete=function(n){for(var t=0,i=e.length;t<i;++t)e[t]();V._completes=[]},V}()),P.preVerifyStartTime=p();else{B.state=1;for(var e=V._completes,i=0,o=e.length;i<o;++i)e[i]();V._completes=[]}}),g()},ready:function(e){0!=B.state?e():(V._completes.push(e),!k&&A.debug&&e())},error:function(e){M<"6.0.2"||(-1==B.state?e(B.data):V._fail=e)},checkJsApi:function(e){var n=function(e){var n=e.checkResult;for(var t in n){var i=h[t];i&&(n[i]=n[t],delete n[t])}return e};t("checkJsApi",{jsApiList:d(e.jsApiList)},(e._complete=function(e){if(T){var t=e.checkResult;t&&(e.checkResult=JSON.parse(t))}e=n(e)},e))},onMenuShareTimeline:function(e){i(v.onMenuShareTimeline,{complete:function(){t("shareTimeline",{title:e.title||S,desc:e.title||S,img_url:e.imgUrl||"",link:e.link||location.href,type:e.type||"link",data_url:e.dataUrl||""},e)}},e)},onMenuShareAppMessage:function(e){i(v.onMenuShareAppMessage,{complete:function(){t("sendAppMessage",{title:e.title||S,desc:e.desc||"",link:e.link||location.href,img_url:e.imgUrl||"",type:e.type||"link",data_url:e.dataUrl||""},e)}},e)},onMenuShareQQ:function(e){i(v.onMenuShareQQ,{complete:function(){t("shareQQ",{title:e.title||S,desc:e.desc||"",img_url:e.imgUrl||"",link:e.link||location.href},e)}},e)},onMenuShareWeibo:function(e){i(v.onMenuShareWeibo,{complete:function(){t("shareWeiboApp",{title:e.title||S,desc:e.desc||"",img_url:e.imgUrl||"",link:e.link||location.href},e)}},e)},onMenuShareQZone:function(e){i(v.onMenuShareQZone,{complete:function(){t("shareQZone",{title:e.title||S,desc:e.desc||"",img_url:e.imgUrl||"",link:e.link||location.href},e)}},e)},startRecord:function(e){t("startRecord",{},e)},stopRecord:function(e){t("stopRecord",{},e)},onVoiceRecordEnd:function(e){i("onVoiceRecordEnd",e)},playVoice:function(e){t("playVoice",{localId:e.localId},e)},pauseVoice:function(e){t("pauseVoice",{localId:e.localId},e)},stopVoice:function(e){t("stopVoice",{localId:e.localId},e)},onVoicePlayEnd:function(e){i("onVoicePlayEnd",e)},uploadVoice:function(e){t("uploadVoice",{localId:e.localId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},downloadVoice:function(e){t("downloadVoice",{serverId:e.serverId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},translateVoice:function(e){t("translateVoice",{localId:e.localId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},chooseImage:function(e){t("chooseImage",{scene:"1|2",count:e.count||9,sizeType:e.sizeType||["original","compressed"],sourceType:e.sourceType||["album","camera"]},(e._complete=function(e){if(T){var n=e.localIds;n&&(e.localIds=JSON.parse(n))}},e))},getLocation:function(e){},previewImage:function(e){t(v.previewImage,{current:e.current,urls:e.urls},e)},uploadImage:function(e){t("uploadImage",{localId:e.localId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},downloadImage:function(e){t("downloadImage",{serverId:e.serverId,isShowProgressTips:0==e.isShowProgressTips?0:1},e)},getLocalImgData:function(e){!1===O?(O=!0,t("getLocalImgData",{localId:e.localId},(e._complete=function(e){if(O=!1,L.length>0){var n=L.shift();E.getLocalImgData(n)}},e))):L.push(e)},getNetworkType:function(e){var n=function(e){var n=e.errMsg;e.errMsg="getNetworkType:ok";var t=e.subtype;if(delete e.subtype,t)e.networkType=t;else{var i=n.indexOf(":"),o=n.substring(i+1);switch(o){case"wifi":case"edge":case"wwan":e.networkType=o;break;default:e.errMsg="getNetworkType:fail"}}return e};t("getNetworkType",{},(e._complete=function(e){e=n(e)},e))},openLocation:function(e){t("openLocation",{latitude:e.latitude,longitude:e.longitude,name:e.name||"",address:e.address||"",scale:e.scale||28,infoUrl:e.infoUrl||""},e)},getLocation:function(e){e=e||{},t(v.getLocation,{type:e.type||"wgs84"},(e._complete=function(e){delete e.type},e))},hideOptionMenu:function(e){t("hideOptionMenu",{},e)},showOptionMenu:function(e){t("showOptionMenu",{},e)},closeWindow:function(e){t("closeWindow",{},e=e||{})},hideMenuItems:function(e){t("hideMenuItems",{menuList:e.menuList},e)},showMenuItems:function(e){t("showMenuItems",{menuList:e.menuList},e)},hideAllNonBaseMenuItem:function(e){t("hideAllNonBaseMenuItem",{},e)},showAllNonBaseMenuItem:function(e){t("showAllNonBaseMenuItem",{},e)},scanQRCode:function(e){t("scanQRCode",{needResult:(e=e||{}).needResult||0,scanType:e.scanType||["qrCode","barCode"]},(e._complete=function(e){if(C){var n=e.resultStr;if(n){var t=JSON.parse(n);e.resultStr=t&&t.scan_code&&t.scan_code.scan_result}}},e))},openAddress:function(e){t(v.openAddress,{},(e._complete=function(e){e=r(e)},e))},openProductSpecificView:function(e){t(v.openProductSpecificView,{pid:e.productId,view_type:e.viewType||0,ext_info:e.extInfo},e)},addCard:function(e){for(var n=e.cardList,i=[],o=0,a=n.length;o<a;++o){var r=n[o],c={card_id:r.cardId,card_ext:r.cardExt};i.push(c)}t(v.addCard,{card_list:i},(e._complete=function(e){var n=e.card_list;if(n){for(var t=0,i=(n=JSON.parse(n)).length;t<i;++t){var o=n[t];o.cardId=o.card_id,o.cardExt=o.card_ext,o.isSuccess=!!o.is_succ,delete o.card_id,delete o.card_ext,delete o.is_succ}e.cardList=n,delete e.card_list}},e))},chooseCard:function(e){t("chooseCard",{app_id:A.appId,location_id:e.shopId||"",sign_type:e.signType||"SHA1",card_id:e.cardId||"",card_type:e.cardType||"",card_sign:e.cardSign,time_stamp:e.timestamp+"",nonce_str:e.nonceStr},(e._complete=function(e){e.cardList=e.choose_card_info,delete e.choose_card_info},e))},openCard:function(e){for(var n=e.cardList,i=[],o=0,a=n.length;o<a;++o){var r=n[o],c={card_id:r.cardId,code:r.code};i.push(c)}t(v.openCard,{card_list:i},e)},consumeAndShareCard:function(e){t(v.consumeAndShareCard,{consumedCardId:e.cardId,consumedCode:e.code},e)},chooseWXPay:function(e){t(v.chooseWXPay,a(e),e)},openEnterpriseRedPacket:function(e){t(v.openEnterpriseRedPacket,a(e),e)},startSearchBeacons:function(e){t(v.startSearchBeacons,{ticket:e.ticket},e)},stopSearchBeacons:function(e){t(v.stopSearchBeacons,{},e)},onSearchBeacons:function(e){i(v.onSearchBeacons,e)},openEnterpriseChat:function(e){t("openEnterpriseChat",{useridlist:e.userIds,chatname:e.groupName},e)},launchMiniProgram:function(e){t("launchMiniProgram",{targetAppId:e.targetAppId,path:m(e.path),envVersion:e.envVersion},e)},miniProgram:{navigateBack:function(e){e=e||{},f(function(){t("invokeMiniProgramAPI",{name:"navigateBack",arg:{delta:e.delta||1}},e)})},navigateTo:function(e){f(function(){t("invokeMiniProgramAPI",{name:"navigateTo",arg:{url:e.url}},e)})},redirectTo:function(e){f(function(){t("invokeMiniProgramAPI",{name:"redirectTo",arg:{url:e.url}},e)})},switchTab:function(e){f(function(){t("invokeMiniProgramAPI",{name:"switchTab",arg:{url:e.url}},e)})},reLaunch:function(e){f(function(){t("invokeMiniProgramAPI",{name:"reLaunch",arg:{url:e.url}},e)})},postMessage:function(e){f(function(){t("invokeMiniProgramAPI",{name:"postMessage",arg:e.data||{}},e)})},getEnv:function(n){f(function(){n({miniprogram:"miniprogram"===e.__wxjs_environment})})}}},R=1,W={};return _.addEventListener("error",function(e){if(!T){var n=e.target,t=n.tagName,i=n.src;if(("IMG"==t||"VIDEO"==t||"AUDIO"==t||"SOURCE"==t)&&-1!=i.indexOf("wxlocalresource://")){e.preventDefault(),e.stopPropagation();var o=n["wx-id"];if(o||(o=R++,n["wx-id"]=o),W[o])return;W[o]=!0,E.ready(function(){E.getLocalImgData({localId:i,success:function(e){n.src=e.localData}})})}}},!0),_.addEventListener("load",function(e){if(!T){var n=e.target,t=n.tagName;if(n.src,"IMG"==t||"VIDEO"==t||"AUDIO"==t||"SOURCE"==t){var i=n["wx-id"];i&&(W[i]=!1)}}},!0),n&&(e.wx=e.jWeixin=E),E}}(n)}(window)},77:function(e,n,t){e.exports=t.p+"0b0e913218694e4e44fd30278343d41b.jpg"},84:function(e,n,t){e.exports=t.p+"47df86f9b53decdefcf54b9e5d16d84a.jpg"},85:function(e,n,t){e.exports=t.p+"8a4064c592a00ce4b5bafe59a67872a2.jpg"},93:function(e,n,t){"use strict";function i(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(n,"__esModule",{value:!0});var o=t(84),a=i(o),r=t(85),c=i(r),s=t(77),d=i(s),l=t(76),u=i(l);n.default={data:function(){return{pic1:a.default,pic2:c.default,pic3:d.default,wxOk:!0,helpDisplay:!0}},methods:{scanAction:function(){if(console.log("click scanAction"),this.wxOk){console.log("扫码购物小票二维码");var e=this;u.default.scanQRCode({needResult:1,desc:"扫码购物小票二维码",success:function(n){console.log("扫码结果："+n),e.getQrCode(n.resultStr)}})}else alert("需要在微信使用该功能");return!1},maskImg:function(){return console.log("click maskImg"),!1},showHelp:function(){this.helpDisplay=!this.helpDisplay},getQrCode:function(e){if("error decoding QR Code"==e)this.$dialog.alert({message:"未扫到二维码或者二维码不正确，请重试",confirmButtonText:"知道了"});else{if(e.indexOf("http")>=0){var n=e.indexOf("qr=");n>0?(n+=3,e=e.substr(n)):this.$dialog.alert({message:"未扫到二维码或者二维码不正确，请重试",confirmButtonText:"知道了"})}var t={ticketQC:e,sheettype:6};this.getbillInfo(t)}},getbillInfo:function(e){var n=this;this.$http({url:"/e-invoice-pro/rest/api/getInvoiceBillInfoWithDetail",method:"post",data:e}).then(function(e){if(console.log(e),0==e.code)if(1==e.data.flag){var t={};t.iqseqno=e.data.iqseqno,n.$router.push({name:"invoice-info",query:t})}else n.$router.push({name:"scan-invoice",params:e.data});else-2==e.code?n.$dialog.alert({message:"提取码错误",confirmButtonText:"知道了"}):-6==e.code?n.$dialog.alert({message:"未扫描到二维码或者二维码不正确，请重试",confirmButtonText:"知道了"}):-9==e.code?n.$dialog.alert({message:e.message,confirmButtonText:"知道了"}):n.$dialog.alert({message:"未扫描到二维码或者二维码不正确，请重试",confirmButtonText:"知道了"})})},isWeix:function(){return"micromessenger"==window.navigator.userAgent.toLowerCase().match(/MicroMessenger/i)},initWeixinJS:function(e){var n=this;u.default.config({debug:!1,appId:e.data.appId,timestamp:e.data.timestamp,nonceStr:e.data.noncestr,signature:e.data.signature,jsApiList:["checkJsApi","scanQRCode"],beta:!0}),u.default.ready(function(){console.log("微信授权完成"),n.wxOk=!0}),u.default.error(function(e){console.log("微信授权失败，扫描功能受限："+e.errMsg),n.wxOk=!1})}},mounted:function(){},created:function(){var e=this;this.isWeix()?(console.log("当前微信客户端"),this.$http({url:"/e-invoice-pro/rest/wx/getWeixTicket?weburl="+window.location.href,method:"post",data:{}}).then(function(n){if(0!=n.code)return void console.log("微信授权失败，扫描功能受限："+n.message);e.initWeixinJS(n)})):console.log("当前非微信客户端")}}}});