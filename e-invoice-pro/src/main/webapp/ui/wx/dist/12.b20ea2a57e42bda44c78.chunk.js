webpackJsonp([12],{103:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a(3),i=function(t){return t&&t.__esModule?t:{default:t}}(n);e.default={data:function(){return{iqseqno:"",email:"",paramsdata:{},showSendPdf:!1}},methods:{download:function(){window.open(this.paramsdata.iqpdf)},getInvque:function(){var t=this,e={};e.iqseqno=this.iqseqno,this.$http({url:"/e-invoice-pro/rest/api/getInvque",method:"post",data:e}).then(function(e){t.paramsdata=e.data[0],t.email=t.paramsdata.iqemail,t.paramsdata.rtkprq=i.default.formatDate(t.paramsdata.rtkprq)})},btnSendPdf:function(){this.showSendPdf=!0},beforeSendPdf:function(t,e){if("confirm"===t){""==this.email&&this.$dialog.alert({message:"邮箱必须填写",confirmButtonText:"知道了"});var a=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,3}$/;if(""!=this.email&&!a.test(this.email))return void this.$dialog.alert({message:"邮箱格式不正确"+this.email,confirmButtonText:"知道了"});this.sendPdf(e)}else e()},sendPdf:function(t){var e=this,a={};a.iqseqno=this.iqseqno,a.email=this.email,this.$http({url:"/e-invoice-pro/rest/api/sendPdf",method:"post",data:a}).then(function(a){t(),0!=a.code?e.$dialog.alert({message:a.message,confirmButtonText:"错误"}):e.$dialog.alert({message:"邮件发送中，请稍后到邮箱查收",confirmButtonText:"成功"})})},toHome:function(){this.$router.push({name:"home",params:{}})},toInvoiceList:function(){this.$router.push({name:"invoice-list",params:{}})}},computed:{},activated:function(){this.iqseqno=this.$route.query.iqseqno,console.log("b============:"+this.iqseqno),this.getInvque()},created:function(){}}},149:function(t,e,a){var n=a(150);"string"==typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);a(20)("08b2dd2b",n,!1,{})},150:function(t,e,a){e=t.exports=a(19)(!1),e.push([t.i,"\n.margin-top-10[data-v-47bae530] {\n  margin-top: 10px;\n}\n.info[data-v-47bae530]{\n  font-size: 12px;\n  margin-top: 6px;\n  margin-left: 12px;\n  color: #999;\n}\n",""])},151:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticStyle:{height:"100%","background-color":"#e4e4e4"}},[a("van-nav-bar",{staticStyle:{"background-color":"#CC3327",color:"#ffffff"},attrs:{title:"发票详情"}},[a("van-icon",{staticStyle:{color:"#ffffff","font-size":"20px","font-weight":"bold"},attrs:{slot:"left",name:"wap-home"},on:{click:t.toHome},slot:"left"}),t._v(" "),a("van-icon",{staticStyle:{color:"#ffffff","font-size":"20px","font-weight":"bold"},attrs:{slot:"right",name:"wap-nav"},on:{click:t.toInvoiceList},slot:"right"})],1),t._v(" "),a("van-cell-group",{staticClass:"margin-top-10"},[a("van-cell",{attrs:{title:"发票类型："}},[a("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[t._v("电子发票")])]),t._v(" "),a("van-cell",{attrs:{title:"购买方名称："}},[a("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[t._v(t._s(t.paramsdata.iqgmfname))])]),t._v(" "),a("van-cell",{attrs:{title:"发票金额："}},[a("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[t._v(t._s(t.paramsdata.iqtotje))])]),t._v(" "),a("van-cell",{attrs:{title:"申请时间："}},[a("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[t._v(t._s(t.paramsdata.rtkprq))])]),t._v(" "),a("van-cell",{attrs:{title:"邮箱地址："}},[a("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[t._v(t._s(t.paramsdata.iqemail))])]),t._v(" "),a("van-cell",{attrs:{title:"电话号码："}},[a("p",{staticStyle:{"text-align":"left",margin:"0","padding-left":"20px"}},[t._v(t._s(t.paramsdata.iqtel))])])],1),t._v(" "),a("van-row",{staticStyle:{"margin-top":"30px"}},[a("van-col",{attrs:{span:"12"}},[a("van-button",{staticStyle:{"background-color":"#ffffff",color:"#000000"},attrs:{"bottom-action":""},on:{click:t.btnSendPdf}},[t._v("重发至邮箱")])],1),t._v(" "),a("van-col",{attrs:{span:"12"}},[a("van-button",{staticStyle:{"background-color":"#CC3327"},attrs:{type:"primary","bottom-action":""},on:{click:t.download}},[t._v("下载电子发票")])],1)],1),t._v(" "),a("van-dialog",{attrs:{"show-cancel-button":"","before-close":t.beforeSendPdf},model:{value:t.showSendPdf,callback:function(e){t.showSendPdf=e},expression:"showSendPdf"}},[a("div",{staticClass:"info"},[t._v("请确认下列邮箱，如有变更请修改")]),t._v(" "),a("van-field",{attrs:{label:"邮箱",placeholder:"请输入邮箱",required:""},model:{value:t.email,callback:function(e){t.email=e},expression:"email"}})],1)],1)},i=[];n._withStripped=!0;var o={render:n,staticRenderFns:i};e.default=o},75:function(t,e,a){"use strict";function n(t){c||a(149)}Object.defineProperty(e,"__esModule",{value:!0});var i=a(103),o=a.n(i);for(var s in i)"default"!==s&&function(t){a.d(e,t,function(){return i[t]})}(s);var l=a(151),r=a.n(l),c=!1,f=a(21),d=n,p=f(o.a,r.a,!1,d,"data-v-47bae530",null);p.options.__file="src/views/invoice-info.vue",e.default=p.exports}});