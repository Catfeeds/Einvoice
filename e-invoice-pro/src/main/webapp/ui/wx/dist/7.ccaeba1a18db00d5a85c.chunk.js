webpackJsonp([7],{273:function(t,e,a){"use strict";function n(t){l||a(296)}Object.defineProperty(e,"__esModule",{value:!0});var o=a(286),i=a.n(o);for(var r in o)"default"!==r&&function(t){a.d(e,t,function(){return o[t]})}(r);var s=a(298),c=a.n(s),l=!1,u=a(118),f=n,d=u(i.a,c.a,!1,f,"data-v-5a4bed13",null);d.options.__file="src\\views\\home.vue",e.default=d.exports},286:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a(42),o=function(t){return t&&t.__esModule?t:{default:t}}(n);e.default={data:function(){return{}},methods:{toInvoiceList:function(){this.$router.push({name:"invoice-list",params:{}})},changePage:function(t,e){1==t?this.$router.push({name:"scan",params:{}}):this.$router.push({name:"extract",params:{}})}},computed:{},mounted:function(){this.$router.push({name:"extract",params:{}})},activated:function(){},created:function(){var t=this,e=o.default.getUrlKey("qr");if(console.log("qr:"+e),"null"!=e&&""!=e&&null!=e){var a={ticketQC:e,sheettype:6};this.$http({url:"/e-invoice-pro/rest/api/getInvoiceBillInfoWithDetail",method:"post",data:a}).then(function(e){if(console.log(e),0==e.code)if(1==e.data.flag){var a={};a.iqseqno=e.data.iqseqno,t.$router.push({name:"invoice-info",query:a})}else console.log("home.vue.created"),console.log(e.data),t.$router.push({name:"scan-invoice",params:e.data});else-2==e.code?t.$dialog.alert({message:"提取码错误",confirmButtonText:"知道了"}):-6==e.code?t.$dialog.alert({message:"请在提货后开具发票",confirmButtonText:"知道了"}):-9==e.code?t.$dialog.alert({message:e.message,confirmButtonText:"知道了"}):t.$dialog.alert({message:"不能识别二维码或二维码不正确",confirmButtonText:"知道了"})})}}}},296:function(t,e,a){var n=a(297);"string"==typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);a(117)("3ad21bc2",n,!1,{})},297:function(t,e,a){e=t.exports=a(116)(!1),e.push([t.i,"\n.van-tab--active[data-v-5a4bed13]{\n  color:#0066CC;\n}\n.van-tabs__nav-bar[data-v-5a4bed13]{\n    z-index: 1;\n    left: 0;\n    bottom: 15px;\n    height: 2px;\n    position: absolute;\n    background-color: #0066CC;\n}\n\n",""])},298:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("van-nav-bar",{staticStyle:{"background-color":"#0066CC",color:"#ffffff"},attrs:{title:"微信开票申请"}},[a("van-icon",{staticStyle:{color:"#ffffff","font-size":"20px","font-weight":"bold"},attrs:{slot:"left",name:"wap-home"},slot:"left"}),t._v(" "),a("van-icon",{staticStyle:{color:"#ffffff","font-size":"20px","font-weight":"bold"},attrs:{slot:"right",name:"wap-nav"},on:{click:t.toInvoiceList},slot:"right"})],1),t._v(" "),a("van-tabs",{staticClass:"tab-bar",on:{click:t.changePage}},[a("van-tab",{attrs:{title:"提取码开票"}}),t._v(" "),a("van-tab",{attrs:{title:"扫码开票"}})],1),t._v(" "),a("keep-alive",[a("router-view")],1)],1)},o=[];n._withStripped=!0;var i={render:n,staticRenderFns:o};e.default=i}});