webpackJsonp([9],{116:function(t,e,n){var i=n(117);"string"==typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);n(20)("ae160b44",i,!1,{})},117:function(t,e,n){e=t.exports=n(19)(!1),e.push([t.i,'\n.title[data-v-09b7f1ad] {\r\n  color: #ca1100;\r\n  padding: 0 20px;\r\n  font-size: 14px;\n}\n.scan[data-v-09b7f1ad] {\r\n  text-align: center;\r\n  margin-top: 80px;\n}\n.scan-icon[data-v-09b7f1ad] {\r\n  font-size: 12rem;\n}\n.scan-p[data-v-09b7f1ad] {\r\n  font-size: 14px;\n}\n.info[data-v-09b7f1ad] {\r\n  padding: 0 10px;\n}\n.info-title[data-v-09b7f1ad] {\r\n  text-align: center;\r\n  font-size: 14px;\r\n  position: relative;\r\n  display: block;\r\n  color: #6f6666;\n}\n.info-title[data-v-09b7f1ad]:before,\r\n.info-title[data-v-09b7f1ad]:after {\r\n  content: "";\r\n  position: absolute;\r\n  top: 52%;\r\n  background: #6f6666;\r\n  width: 36%;\r\n  height: 1px;\n}\n.info-title[data-v-09b7f1ad]:before {\r\n  left: 0;\n}\n.info-title[data-v-09b7f1ad]:after {\r\n  right: 0;\n}\n.info-content[data-v-09b7f1ad] {\r\n  color: #6f6666;\r\n  font-size: 14px;\n}\n.info-content-m[data-v-09b7f1ad] {\r\n  text-indent: 28px;\n}\r\n',""])},118:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticStyle:{padding:"10px 5px"}},[n("div",{staticStyle:{height:"280px"}},[n("van-cell-group",{staticStyle:{"margin-top":"20px"}},[n("van-field",{attrs:{label:"发票提取码",placeholder:"请填写发票提取码",required:""},model:{value:t.sheetid,callback:function(e){t.sheetid=e},expression:"sheetid"}}),t._v(" "),n("van-field",{attrs:{type:"number",label:"小票金额",placeholder:"请填写小票金额",required:""},model:{value:t.invoiceamount,callback:function(e){t.invoiceamount=e},expression:"invoiceamount"}})],1),t._v(" "),n("div",{staticStyle:{padding:"0 20px","margin-top":"40px"}},[n("van-button",{staticStyle:{"background-color":"#CC3327"},attrs:{type:"primary","bottom-action":""},on:{click:t.getInvoiceBillInfoWithDetail}},[t._v("校验")]),t._v(" "),n("div",[n("p",{staticStyle:{"text-align":"center",color:"#CC3327"}},[t._v(t._s(t.errorMsg1))])])],1)],1),t._v(" "),n("div",{staticClass:"info"},[n("p",{staticClass:"info-title",on:{click:t.showHelp}},[t._v("开票说明 "),n("van-icon",{attrs:{name:"question"}})],1),t._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:t.helpDisplay,expression:"helpDisplay"}],staticClass:"info-content"},[n("p",[t._v("尊敬的顾客：")]),t._v(" "),n("p",{staticClass:"info-content-m"},[t._v("免税商品开票，需在机场办理提货手续后，方可开具电子发票。")]),t._v(" "),n("p",{staticClass:"info-content-m"},[t._v("输入销售小票上的发票提取码和金额，进行开具发票。")]),t._v(" "),n("van-row",{attrs:{gutter:"10"}},[n("p",{staticStyle:{"text-align":"center","font-size":"14px","font-weight":"bold"}},[t._v("免税销售小票")]),t._v(" "),n("img",{staticStyle:{width:"100%"},attrs:{src:t.pic1,alt:""}})])],1)])])},a=[];i._withStripped=!0;var o={render:i,staticRenderFns:a};e.default=o},64:function(t,e,n){"use strict";function i(t){c||n(116)}Object.defineProperty(e,"__esModule",{value:!0});var a=n(92),o=n.n(a);for(var r in a)"default"!==r&&function(t){n.d(e,t,function(){return a[t]})}(r);var s=n(118),l=n.n(s),c=!1,d=n(21),f=i,p=d(o.a,l.a,!1,f,"data-v-09b7f1ad",null);p.options.__file="src/views/home/extract6921.vue",e.default=p.exports},83:function(t,e,n){t.exports=n.p+"786b468f491b333ef9dd9d8eb3586faa.jpg"},92:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=n(83),a=function(t){return t&&t.__esModule?t:{default:t}}(i);e.default={data:function(){return{pic1:a.default,sheetid:"",invoiceamount:"",errorMsg1:"",helpDisplay:!0}},methods:{changePage:function(t,e){console.log("index:",t),console.log("title:",e)},showHelp:function(){this.helpDisplay=!this.helpDisplay},getInvoiceBillInfoWithDetail:function(){var t=this;if(""==this.sheetid||""==this.invoiceamount)return void(this.errorMsg1="必须录入提取码和金额");this.errorMsg1="";var e={ticketQC:this.sheetid+"-"+this.invoiceamount,sheettype:6};this.$http({url:"/e-invoice-pro/rest/api/getInvoiceBillInfoWithDetail",method:"post",data:e}).then(function(e){if(console.log(e),0==e.code)if(1==e.data.flag){var n={};n.iqseqno=e.data.iqseqno,t.$router.push({name:"invoice-info",query:n})}else t.$router.push({name:"scan-invoice",params:e.data});else-6==e.code?(t.errorMsg1="校验失败",t.errorMsg2="请输入正确的提取码或小票金额"):-9==e.code?t.$dialog.alert({message:e.message,confirmButtonText:"知道了"}):(t.errorMsg1="校验失败",t.errorMsg2="请输入正确的提取码和小票金额")})}},mounted:function(){},created:function(){}}}});