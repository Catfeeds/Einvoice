<style scoped>
.margin-top-10 {
  margin-top: 5px;
}
</style>

<template>
    <div style="height:100%;background-color: #e4e4e4;">

        <!-- 头部导航结构 -->
        <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="发票信息">
            <van-icon style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
        </van-nav-bar>

        <van-cell-group>
            <van-cell title="发票类型：">
                <p style="text-align:left;margin:0;padding-left:20px;color:#CC3300;">电子发票</p>
            </van-cell>
        </van-cell-group>

        <van-cell-group class="margin-top-10">
            <van-cell title="销售方名称：">
                <p class="van-ellipsis" style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.taxname}}</p>
            </van-cell>
        </van-cell-group>
        <van-cell-group class="margin-top-10" v-show="!isMs">
            <van-cell>
                <van-radio-group v-model="fptd" style="text-align:left;padding-left:20px;" @change="fptdChange">
                    <van-radio style="display: inline;margin-right:10px;" name="gr">个人</van-radio>
                    <van-radio style="display: inline;margin-right:10px;" name="dw">单位</van-radio>
                </van-radio-group>
            </van-cell>
        </van-cell-group>
        <van-cell-group class="margin-top-10">
            <van-field v-model="gmfMc" center required label="发票抬头" placeholder="请填写发票抬头" :readonly="isMs">
                <van-icon @click="chooseInvoiceTitle" v-show="wxOk" slot="button" name="add" style="display: block;font-size: 18px;color: #CA3415;" />
            </van-field>
        </van-cell-group>

        <van-cell-group class="margin-top-10">
            <van-field v-model="gmfNsrsbh" label="纳税人识别号" placeholder="请填写纳税人识别号" v-show="isDw"></van-field>
        </van-cell-group>

        <van-cell-group class="margin-top-10">
            <van-field v-model="recvEmail" label="收票人邮箱" placeholder="请填写收票人邮箱" type="email"></van-field>
            <van-field v-model="recvPhone" label="收票人电话" placeholder="请填写收票人电话"></van-field>
            <van-cell>
                <p style="text-align:left;margin:0;color:#CC3300;">邮箱或电话至少填写一项</p>
            </van-cell>
        </van-cell-group>
        <van-button @click="nextAction" style="background-color:#CC3327;margin-top:10px;" type="primary" bottom-action>下一步</van-button>

    </div>
</template>

<script>
import Util from "@/libs/util";
import Cookies from 'js-cookie';
import wx from 'weixin-js-sdk';

export default {
  data() {
    return {
      gmfMc: "",
      gmfNsrsbh: "",
      recvEmail: "",
      recvPhone: "",
      invType: 1,
      paramsdata: "",
      fptd:"gr",
      isDw:false,
      isMs:true,
      wxOk:false
    };
  },
  methods: {
    fptdChange(n){
        if(n=='gr'){
            this.isDw = false;
            this.gmfNsrsbh="";
        }else{
            this.isDw = true;
        }
    },
    nextAction() {
         var regPhone=/^1[3|4|5|7|8][0-9]{9}$/;
         var regEmail= /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,3}$/;
      if (this.gmfMc == '' || this.gmfMc == null) {
        // alert("必须录入发票抬头");
        this.$dialog.alert({
            message: "必须录入发票抬头",
            confirmButtonText: "知道了"
        });
        return;
      }

      if(this.recvEmail == "" && this.recvPhone == ""){
           this.$dialog.alert({
            message: "邮箱或电话至少填写一项",
            confirmButtonText: "知道了"
        });
        return;
      }else

      if(this.recvEmail !='' &&  !regEmail.test(this.recvEmail)){
        // alert("邮箱格式不正确");
        this.$dialog.alert({
            message: "邮箱格式不正确"+this.recvEmail,
            confirmButtonText: "知道了"
        });
        return;
      }
      if(this.recvPhone != '' && !regPhone.test(this.recvPhone)){
          this.$dialog.alert({
            message: "电话号码格式不正确"+this.recvPhone,
            confirmButtonText: "知道了"
        });
        return;
      }



      let querydata = {};
      let requestInvoicePreviewItem = [];
      let item = {};
      item.sheetid = this.paramsdata.sheetid;
      item.je = this.paramsdata.totalamount;
      requestInvoicePreviewItem.push(item);
      querydata.requestInvoicePreviewItem = requestInvoicePreviewItem;
      querydata.gmfMc = this.gmfMc;
      querydata.gmfNsrsbh = this.gmfNsrsbh;
      querydata.recvPhone = this.recvPhone;
      querydata.recvEmail = this.recvEmail;
      querydata.invType = this.invType;
      querydata.sheettype = this.paramsdata.sheettype;

      //记住邮箱
      Cookies.set("recvEmail",this.recvEmail);
      Cookies.set("recvPhone",this.recvPhone);

      this.$http({
        url: "/e-invoice-pro/rest/api/getInvoicePreview", //请求地址
        method: "post", //请求类型
        data: querydata
        //请求参数对象
      }).then(res => {
        //  then中可直接只用this 
        if(res.code==0){
            var params = res.data[0];
            params.shopname = this.paramsdata.shopname;
            this.$router.push({
                name: "confirm",
                params: res.data[0]
            });
        }else{
            this.$dialog.alert({
                message: res.message,
                confirmButtonText: "知道了"
            });
        }
      });
    },isWeix() {
      const ua = window.navigator.userAgent.toLowerCase();
      if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
      } else {
        return false;
      }
    },initWeixinJS(dataJson){
        var _this=this;
      wx.config({  
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
        appId: dataJson.data.appId, // 必填，公众号的唯一标识  
        timestamp: dataJson.data.timestamp, // 必填，生成签名的时间戳  
        nonceStr: dataJson.data.noncestr, // 必填，生成签名的随机串  
        signature: dataJson.data.signature,// 必填，签名，见附录1  
        jsApiList: ['checkJsApi'],
        beta:true //调用未公开方法
      });

      wx.ready(function(){
        console.log("微信授权完成");
        _this.wxOk=true;
      });
      wx.error(function(res){
        console.log("微信授权失败，扫描功能受限："+res.errMsg);
        _this.wxOk=false;
      });
    },chooseInvoiceTitle(){
         var _this=this;
        wx.invoke("chooseInvoiceTitle", {
            "scene" : "1"
        }, function(msg) {
            var res = JSON.parse(msg.choose_invoice_title_info);
            if(_this.isDw){
                _this.gmfMc = res.title;
                _this.gmfNsrsbh = res.taxNumber;
            }else{
               _this.gmfMc = res.title;
            }            
        });
    }
  },
  computed: {},
  created() {
    if(this.$route.params.sheetid){
        this.paramsdata = this.$route.params;
        this.gmfMc = this.paramsdata.gmfname;
        console.log("this.gmfMc:"+this.gmfMc);
        if(this.gmfMc==undefined || this.gmfMc==null || this.gmfMc==''){
            this.isMs = false;
            //尝试开启微信
            if(this.isWeix()){
                console.log("当前微信客户端");
                this.$http({
                    url: "/e-invoice-pro/rest/wx/getWeixTicket?weburl="+window.location.href, //请求地址
                    method: "post", //请求类型
                    data: {}
                    //请求参数对象
                }).then(res => {
                    if(res.code!=0){
                        console.log("微信授权失败，扫描功能受限："+res.message);
                        return;
                    }
                    this.initWeixinJS(res);
                });
            }else{
                console.log("当前非微信客户端");
            }
        }

        var email = Cookies.set("recvEmail");
        var phone = Cookies.set("recvPhone");
        if(email != undefined){
            this.recvEmail = Cookies.set("recvEmail");
        }
        if(phone != undefined){
            this.recvPhone = Cookies.set("recvPhone");
        }
        
    }
  }
};
</script>


