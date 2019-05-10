<template>
  <div>
    <!-- 头部导航结构 -->
    <van-nav-bar fixed title="发票信息">
      <van-icon name="arrow-left" slot="left" @click="$router.go(-1)" />
    </van-nav-bar>

    <van-cell-group>
      <van-cell title="发票类型：">
        <p style="color:#CC3300;">电子发票</p>
      </van-cell>
    </van-cell-group>

    <van-cell-group class="margin-top-10">
      <van-cell title="销售方名称：">
        <p class="van-ellipsis">{{paramsdata.taxname}}</p>
      </van-cell>
    </van-cell-group>
    <van-cell-group class="margin-top-10" v-show="!isMs">
      <van-cell>
        <van-radio-group v-model="isDw" class="pl20">
          <van-radio style="display: inline;margin-right:10px;" :name="false">个人</van-radio>
          <van-radio style="display: inline;" :name="true">单位</van-radio>
        </van-radio-group>
      </van-cell>
    </van-cell-group>
    <van-cell-group class="margin-top-10">
      <van-field v-model="gmfMc" center required label="发票抬头" placeholder="请填写发票抬头" :readonly="isMs" v-if="!isDw">
        <van-icon @click="chooseInvoiceTitle" slot="button" name="add" style="display: block;font-size: 18px;color: #CA3415;"
        />
      </van-field>
      <template v-else>
        <van-field v-model="gmfDwMc" required label="单位名称" placeholder="请填写单位名称">
          <van-icon @click="chooseInvoiceTitle" slot="button" name="add" style="display: block;font-size: 18px;color: #CA3415;"
        />
        </van-field>
        <van-field v-model="gmfNsrsbh" required maxLength="20" label="纳税人识别号" placeholder="请填写纳税人识别号"></van-field>
        <van-field v-model="gmfYhzh" label="银行账户" placeholder="选填开户银行及账号"></van-field>
        <van-field v-model="gmfDzdh" label="地址电话" placeholder="选填地址及联系方式"></van-field>
      </template>
    </van-cell-group>
    <van-cell-group class="margin-top-10">
      <van-field v-model="recvEmail" required label="收票人邮箱" placeholder="请填写收票人邮箱" v-if="useEmail" type="email"></van-field>
      <van-field v-model="recvPhone" label="收票人电话" placeholder="请填写收票人电话" v-if="usePhone"></van-field>
      <van-cell>
        <!--<p style="text-align:left;margin:0;color:#CC3300;">{{getTips}}</p>-->
      </van-cell>
    </van-cell-group>
    <van-button @click="nextAction" style="background-color:#0066CC" type="primary" bottom-action>下一步</van-button>

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
        gmfDwMc: "",
        recvEmail: "",
        recvPhone: "",
        usePhone: 1,
        useEmail: 1,
        /**单位三项数据 */
        gmfNsrsbh: "",
        gmfDzdh: '',
        gmfYhzh: '',
        /**单位三项数据 */
        invType: 1,
        paramsdata: {},
        isDw: false, //是否为单位开票 默认false为个人
        isMs: false, //是否已填写过发票抬头
        wxOk: false //微信授权是否成功
      };
    },
    created() {
      this.usePhone = parseInt(Cookies.get('usePhone'));
      this.useEmail = parseInt(Cookies.get('useEmail'));
      this.initData();
    },
    methods: {
      /**校验 */
      verification() {
        var regPhone = /^1[3|4|5|7|8|9][0-9]{9}$/;
        var regEmail =
          /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,3}$/;
        if (this.gmfMc != null) this.gmfMc = this.gmfMc.trim();
        if (this.gmfDwMc != null) this.gmfDwMc = this.gmfDwMc.trim();
        this.recvPhone = this.recvPhone.trim();
        this.recvEmail = this.recvEmail.trim();
        this.gmfNsrsbh = this.gmfNsrsbh.trim();
        this.gmfDzdh = this.gmfDzdh.trim();
        this.gmfYhzh = this.gmfYhzh.trim();
        if (this.isDw) {
          if (this.gmfDwMc == '' || this.gmfDwMc == null) {
            this.$dialog.alert({
              message: '必须录入单位名称',
              confirmButtonText: "知道了"
            });
            return false;
          }
          const len = this.gmfNsrsbh.length;
          if (this.gmfNsrsbh == '' || len != 15 && len != 18 && len != 20) {
            this.$toast('必须录入15/18/20位纳税人识别号');
            return false;
          }
        } else {
          if (this.gmfMc == '' || this.gmfMc == null) {
            this.$toast('必须录入发票抬头');
            return false;
          }
        }
        if (this.useEmail == 1 && this.usePhone == 1) {
          if (this.recvEmail == "" && this.recvPhone == "") {
            this.$toast('邮箱或电话至少填写一项');
            return false;
          }
        } else if (this.useEmail == 1 && this.usePhone == 0) {
          if (!regEmail.test(this.recvEmail)) {
            this.$toast('邮箱格式错误')
            return false;
          }
        } else if (this.useEmail == 0 && this.usePhone == 1) {
          if (!regPhone.test(this.recvPhone)) {
            this.$toast('电话格式错误')
            return false;
          }
        }
        return true;
      },
      /**校验并下一步 */
      nextAction() {
        if (!this.verification()) return;
        let requestInvoicePreviewItem = [{
          sheetid: this.paramsdata.sheetid,
          je: this.paramsdata.totalamount
        }]
        const querydata = {
          requestInvoicePreviewItem,
          gmfMc: this.isDw ? this.gmfDwMc : this.gmfMc,
          recvPhone: this.recvPhone,
          recvEmail: this.recvEmail,
          gmfNsrsbh: this.gmfNsrsbh,
          gmfDzdh: this.gmfDzdh,
          gmfYhzh: this.gmfYhzh,
          invType: this.invType,
          sheettype: this.paramsdata.sheettype
        }
        /**个人发票 则祛除单位三项数据 */
        if (!this.isDw) {
          delete querydata.gmfNsrsbh;
          delete querydata.gmfDzdh;
          delete querydata.gmfYhzh;
        }
        //记住已填写
        Cookies.set("recvEmail", this.recvEmail);
        Cookies.set("recvPhone", this.recvPhone);
        Cookies.set("gmfDzdh", this.gmfDzdh);
        Cookies.set("gmfYhzh", this.gmfYhzh);
        Cookies.set("gmfDwMc", this.gmfDwMc);
        Cookies.set("gmfNsrsbh", this.gmfNsrsbh);
        Cookies.set("gmfMc", this.gmfMc);
        this.$http.getInvoicePreview(querydata).then(res => {
          if (res.code == 0) {
            const query = Object.assign({
              shopname: this.paramsdata.shopname
            }, res.data[0])
            window.localStorage.setItem('invoiceApplay', JSON.stringify(query))
            this.$router.push(confirm);
          } else {
            this.$dialog.alert({
              message: res.message,
              confirmButtonText: "知道了"
            });
          }
        });
      },
      isWeix() {
        const ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == "micromessenger") {
          return true;
        } else {
          return false;
        }
      },
      initWeixinJS(dataJson) {
        var _this = this;
        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
          appId: dataJson.data.appId, // 必填，公众号的唯一标识  
          timestamp: dataJson.data.timestamp, // 必填，生成签名的时间戳  
          nonceStr: dataJson.data.noncestr, // 必填，生成签名的随机串  
          signature: dataJson.data.signature, // 必填，签名，见附录1  
          jsApiList: ['checkJsApi'],
          beta: true //调用未公开方法
        });
        wx.ready(function () {
          _this.wxOk = true;
        });
        wx.error(function (res) {
          _this.wxOk = false; //授权失败禁止显示按钮
        });
      },
      chooseInvoiceTitle() {
        if(!this.isWeix()){
          this.$toast('仅在微信端打开才能读取发票抬头信息');
          return;
        }

        if(!this.wxOk){
          this.$toast('微信授权失败，请手工录入');
          return;
        }

        var _this = this;
        wx.invoke("chooseInvoiceTitle", {
          "scene": "1"
        }, function (msg) {
          console.log(msg)
          var res = JSON.parse(msg.choose_invoice_title_info);
          if (_this.isDw) {
            _this.gmfDwMc = res.title;
            _this.gmfNsrsbh = res.taxNumber;
          } else {
            _this.gmfMc = res.title;
          }
        });
      },
      initData() {
        const scanInvoice = JSON.parse(window.localStorage.getItem('scanInvoice'))
        if (scanInvoice.sheetid) {
          this.paramsdata = scanInvoice;
          this.getCookiesData();
            //尝试开启微信
            if (this.isWeix()) {
              console.log("当前微信客户端");
              this.$http.getWeixTicket().then(res => {
                if (res.code != 0) {
                  console.log("微信授权失败，扫描功能受限：" + res.message);
                  return;
                }
                this.initWeixinJS(res);
              });
            } else {
              console.log("当前非微信客户端");
            }
        }
      },
      getCookiesData() {
        const CookiesArr = ['gmfNsrsbh', 'gmfMc', 'gmfDwMc', 'recvEmail', 'recvPhone', 'gmfDzdh', 'gmfYhzh']
        CookiesArr.forEach(p => {
          const cook = Cookies.get(p);
          if (cook != undefined) this[p] = cook;
        })
      }
    },
    computed: {
      getTips() {
        if (this.useEmail == 1 && this.usePhone == 1) return '邮箱或电话至少填写一项'
        else if (this.useEmail == 1 && this.usePhone == 0) return '需填写接收邮箱'
        else if (this.useEmail == 0 && this.usePhone == 1) return '需填写联系电话'
      }
    }
  };

</script>

<style scoped>
  .van-cell p {
    text-align: left;
    margin: 0;
    padding-left: 20px;
  }

</style>
