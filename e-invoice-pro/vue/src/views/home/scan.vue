<style scoped>
img { pointer-events: none; }
.title {
  color: #ca1100;
  padding: 0 20px;
  font-size: 14px;
}
.scan {
  text-align: center;
  margin-top: 80px;
}
.scan-icon {
  font-size: 8rem;
  color: #999;
}
.scan-p {
  font-size: 14px;
  color: #CC3327;
}
.scan-img{
  width: 33%;

}
.info {
  padding: 0 10px;
}
.info-title {
  text-align: center;
  font-size: 14px;
  position: relative;
  display: block;
  color: #6f6666;
}
.info-title:before,
.info-title:after {
  content: "";
  position: absolute;
  top: 52%;
  background: #6f6666;
  width: 36%;
  height: 1px;
}
.info-title:before {
  left: 0;
}
.info-title:after {
  right: 0;
}
.info-content {
  color: #6f6666;
  font-size: 14px;
}
.info-content-m {
  text-indent: 28px;
}
</style>

<template>
  <div style="padding:10px 5px;">
    <div style="height:370px;top:0px;">

    <!-- 扫码结构 -->
    <div class="scan">
      <!--<van-icon @click="scanAction" class="scan-icon" name="qr" />-->
      <van-row>
        <van-col offset="14" span="8" class="scan-p">点这里</van-col>
      </van-row>
      <van-row>
        <div style="width:100%;" @click="scanAction">
        <van-col offset="6" span="12">
          <img  style="width:100%;" :src="pic3" onclick="return false" >
        </van-col>
        </div>
      </van-row>
      <p class="scan-p">点击扫描二维码</p>
    </div>
  </div>

    <div class="info">
      <p class="info-title" @click="showHelp">开票说明 <van-icon name="question" /></p>
      <div class="info-content" v-show="helpDisplay">
        <p>尊敬的顾客：</p>
        <p class="info-content-m">免税商品开票，需在机场办理提货手续后，方可开具电子发票。</p>
        <p class="info-content-m">通过扫描销售小票上的二维码，即可开具发票。</p>

        <van-row gutter="10">
          <van-col span="12">
            <p style="text-align:center;font-size:12px;">免税商品扫码开票</p>
            <img style="width:100%;" :src="pic1" alt="">
          </van-col>
          <van-col span="12">
            <p style="text-align:center;font-size:12px;">有税商品扫码开票</p>
            <img style="width:100%;" :src="pic2" alt="">
          </van-col>
        </van-row>
      </div>
    </div>

  </div>
</template>

<script>
import u120 from "../../images/u120.jpg";
import u124 from "../../images/u124.jpg";
import scan from "../../images/scan.jpg";
import wx from 'weixin-js-sdk';


export default {
  data() {
    return {
      pic1: u120,
      pic2: u124,
      pic3: scan,
      wxOk:true,
      helpDisplay:true
    };
  },
  methods: {
    //   扫描
    scanAction() {
      console.log("click scanAction");
      if(this.wxOk){
        console.log('扫码购物小票二维码');
        let _this = this;
        wx.scanQRCode({
          needResult: 1,
          desc: '扫码购物小票二维码',
          success: function (data) {
            console.log('扫码结果：'+data);
            _this.getQrCode(data.resultStr);
          }
        });
      }else{
        alert("需要在微信使用该功能");
      }
      return false;
    },
    maskImg(){console.log("click maskImg");return false;},
    showHelp(){
      this.helpDisplay=!this.helpDisplay;
    },
    getQrCode(data){
      if(data == "error decoding QR Code"){
        this.$dialog.alert({
            message: "不能识别二维码或二维码不正确",
            confirmButtonText: "知道了"
          });
      }else{
        if(data.indexOf("http")>=0){
          var startIdx = data.indexOf("qr=");
          
          if(startIdx>0){
            startIdx=startIdx+3;
            data = data.substr(startIdx);
          }else{
            this.$dialog.alert({
              message: "不能识别二维码或二维码不正确",
              confirmButtonText: "知道了"
            });
          }
        }
        var querydata ={ticketQC:data,sheettype:6};
        this.getbillInfo(querydata);
      }
    },

    getbillInfo(querydata){
      this.$http({
        url: "/e-invoice-pro/rest/api/getInvoiceBillInfoWithDetail", //请求地址
        method: "post", //请求类型
        data: querydata
        //请求参数对象
      }).then(res => {
        //  then中可直接只用this
        console.log(res);
        if (res.code == 0) {
          if (res.data.flag == 1) {
            let tempdata = {};
            tempdata.iqseqno = res.data.iqseqno;
            this.$router.push({
              name: "invoice-info",
              query: tempdata
            });
          } else {
            this.$router.push({
              name: "scan-invoice",
              params: res.data
            });
          }
        }else if (res.code == -2) {
            this.$dialog.alert({
                message: "提取码错误",
                confirmButtonText: "知道了"
            });
        }else if (res.code == -6) {
            this.$dialog.alert({
                message: "请在提货后开具发票",
                confirmButtonText: "知道了"
            });
        }else if (res.code == -9) {
          this.$dialog.alert({
              message: res.message,
              confirmButtonText: "知道了"
          });
        } else {
            this.$dialog.alert({
                message: "不能识别二维码或二维码不正确",
                confirmButtonText: "知道了"
            });
        }
      });
    },

    isWeix() {
      const ua = window.navigator.userAgent.toLowerCase();
      if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
      } else {
        return false;
      }
    },
    initWeixinJS(dataJson){
      var _this = this;
      wx.config({  
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
        appId: dataJson.data.appId, // 必填，公众号的唯一标识  
        timestamp: dataJson.data.timestamp, // 必填，生成签名的时间戳  
        nonceStr: dataJson.data.noncestr, // 必填，生成签名的随机串  
        signature: dataJson.data.signature,// 必填，签名，见附录1  
        jsApiList: ['checkJsApi',
                    'scanQRCode'// 微信扫一扫接口
                  ],
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

    }
    
  },
  mounted() {


  },
  created() {
    //加载微信config
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
};
</script>


