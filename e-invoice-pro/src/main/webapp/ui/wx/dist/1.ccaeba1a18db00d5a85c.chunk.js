webpackJsonp([1],{274:function(t,e,n){"use strict";function a(t){l||n(299)}Object.defineProperty(e,"__esModule",{value:!0});var i=n(287),o=n.n(i);for(var s in i)"default"!==s&&function(t){n.d(e,t,function(){return i[t]})}(s);var r=n(301),c=n.n(r),l=!1,d=n(118),p=a,f=d(o.a,c.a,!1,p,"data-v-43a96438",null);f.options.__file="src\\views\\home\\extract.vue",e.default=f.exports},283:function(t,e,n){t.exports=n.p+"51dab0dc114386b1d60b0bfe9d832ba9.jpg"},284:function(t,e,n){t.exports=n.p+"119078694c4584c1ed3ea61572371987.jpg"},287:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var i=n(283),o=a(i),s=n(284),r=a(s);e.default={data:function(){return{pic1:o.default,pic2:r.default,sheetid:"",errorMsg1:"",errorMsg2:"",helpDisplay:!0}},methods:{changePage:function(t,e){console.log("index:",t),console.log("title:",e)},showHelp:function(){this.helpDisplay=!this.helpDisplay},getInvoiceBillInfoWithDetail:function(){var t=this;if(""==this.sheetid)return void(this.errorMsg1="必须录入提取码和金额");this.errorMsg1="",this.errorMsg2="";var e={ticketQC:this.sheetid,sheettype:6};this.$http({url:"/e-invoice-pro/rest/api/getInvoiceBillInfoWithDetail",method:"post",data:e}).then(function(e){if(console.log(e),0==e.code)if(1==e.data.flag){var n={};n.iqseqno=e.data.iqseqno,t.$router.push({name:"invoice-info",query:n})}else t.$router.push({name:"scan-invoice",params:e.data});else-6==e.code?(t.errorMsg1="校验失败",t.errorMsg2="请在提货后开具发票"):-9==e.code?t.$dialog.alert({message:e.message,confirmButtonText:"知道了"}):(t.errorMsg1="校验失败",t.errorMsg2="请输入正确的提取码和小票金额")})}},mounted:function(){},created:function(){}}},299:function(t,e,n){var a=n(300);"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);n(117)("5f7de4d0",a,!1,{})},300:function(t,e,n){e=t.exports=n(116)(!1),e.push([t.i,'\n.title[data-v-43a96438] {\n  color: #ca1100;\n  padding: 0 20px;\n  font-size: 14px;\n}\n.font12[data-v-43a96438]{\n  font-size: 12px;\n}\n.font14[data-v-43a96438]{\n  font-size: 14px;\n}\n.red[data-v-43a96438]{\n  color: #f44;\n}\n.text-center[data-v-43a96438]{\n  text-align:center;\n}\n.margin-top-10[data-v-43a96438]{\n  margin-top: 10px;\n}\n.info[data-v-43a96438] {\n  padding: 0 10px;\n}\n.info-title[data-v-43a96438] {\n  text-align: center;\n  font-size: 14px;\n  position: relative;\n  display: block;\n  color: #6f6666;\n}\n.info-title[data-v-43a96438]:before,\n.info-title[data-v-43a96438]:after {\n  content: "";\n  position: absolute;\n  top: 52%;\n  background: #6f6666;\n  width: 36%;\n  height: 1px;\n}\n.info-title[data-v-43a96438]:before {\n  left: 0;\n}\n.info-title[data-v-43a96438]:after {\n  right: 0;\n}\n.info-content[data-v-43a96438] {\n  color: #6f6666;\n  font-size: 14px;\n}\n.info-content-m[data-v-43a96438] {\n  text-indent: 28px;\n}\n',""])},301:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticStyle:{padding:"10px 5px"}},[n("div",{staticStyle:{height:"370px"}},[n("van-cell-group",{staticStyle:{"margin-top":"20px"}},[n("van-field",{attrs:{label:"发票提取码",placeholder:"请填写发票提取码",icon:"clear",required:""},on:{"click-icon":function(e){t.sheetid=""}},model:{value:t.sheetid,callback:function(e){t.sheetid=e},expression:"sheetid"}})],1),t._v(" "),n("van-row",[n("van-col",{staticClass:"font14 red margin-top-10",attrs:{offset:"1",span:"23"}},[t._v("示例：1001-0346-3473431-18.6")])],1),t._v(" "),n("div",{staticStyle:{padding:"0 20px","margin-top":"40px"}},[n("van-button",{staticStyle:{"background-color":"#0066CC"},attrs:{type:"primary","bottom-action":""},on:{click:t.getInvoiceBillInfoWithDetail}},[t._v("校验")]),t._v(" "),n("div",[n("p",{staticClass:"text-center red"},[t._v(t._s(t.errorMsg1))]),t._v(" "),n("p",{staticClass:"text-center red"},[t._v(t._s(t.errorMsg2))])])],1)],1),t._v(" "),n("div",{staticClass:"info"},[n("p",{staticClass:"info-title",on:{click:t.showHelp}},[t._v("开票说明 "),n("van-icon",{attrs:{name:"question"}})],1),t._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:t.helpDisplay,expression:"helpDisplay"}],staticClass:"info-content"},[n("p",[t._v("尊敬的顾客：")]),t._v(" "),n("p",{staticClass:"info-content-m"},[t._v("免税商品开票，需在机场办理提货手续后，方可开具电子发票。")]),t._v(" "),n("p",{staticClass:"info-content-m"},[t._v("通过扫描销售小票上的二维码，即可开具发票。")]),t._v(" "),n("van-row",{attrs:{gutter:"10"}},[n("van-col",{attrs:{span:"12"}},[n("p",{staticStyle:{"text-align":"center","font-size":"12px"}},[t._v("免税商品扫码开票")]),t._v(" "),n("img",{staticStyle:{width:"100%"},attrs:{src:t.pic1,alt:""}})]),t._v(" "),n("van-col",{attrs:{span:"12"}},[n("p",{staticStyle:{"text-align":"center","font-size":"12px"}},[t._v("有税商品扫码开票")]),t._v(" "),n("img",{staticStyle:{width:"100%"},attrs:{src:t.pic2,alt:""}})])],1)],1)])])},i=[];a._withStripped=!0;var o={render:a,staticRenderFns:i};e.default=o}});