<template>
  <div>
    <!-- 头部导航结构 -->
    <van-nav-bar fixed title="微信开票申请">
      <van-icon name="wap-home" slot="left" />
      <van-icon @click="toInvoiceList" name="wap-nav" slot="right" />
    </van-nav-bar>
    <!-- 导航结构 -->
    <van-tabs @click="changePage" class="tab-bar">
      <van-tab title="提取码开票"></van-tab>
      <van-tab title="扫码开票"></van-tab>
    </van-tabs>
    <!-- 子界面路由导航 -->
    <keep-alive>
      <router-view class="homeChildView" :multiIpt="multiIpt" :sheettype="sheettype"></router-view>
    </keep-alive>
  </div>
</template>

<script>
  import Util from "@/libs/util";
  import Cookies from 'js-cookie';
  export default {
    data() {
      return {
        qr: '',
        multiIpt: true,
        sheettype: 1
      };
    },
    created() {
      this.$toast.loading({
        duration: 0,
        mask: true,
        forbidClick: true
      });
      this.getWxConfig()
    },
    methods: {
      toInvoiceList() {
        this.$router.push('invoice-list');
      },
      //   界面切换事件
      changePage(index, title) {
        if (index == 1) {
          this.$router.push('scan');
        } else {
          this.$router.push('extract');
        }
      },
      initWxdata() {
        this.$http.getInvoiceBillInfoWithDetail({
          ticketQC: this.qr,
          sheettype: this.sheettype
        }).then(res => {
          if (res.code == 0) {
            if (res.data.flag == 0) {
              window.localStorage.setItem('scanInvoice', JSON.stringify(res.data))
              this.$router.push('scan-invoice');
            } else if (res.data.flag == 1 && res.data.iqstatus == 30) this.$toast('正在开票，请稍后')
            else this.$router.push({
              name: "invoice-info",
              query: {
                iqseqno: res.data.iqseqno
              }
            });
          } else if (res.code == -2) this.$toast.fail('提取码错误');
          else if (res.code == -6) this.$toast.fail('开票数据尚未处理完成，请稍后再试');
          else if (res.code == -9) this.$toast.fail(res.message)
          else this.$toast.fail('不能识别二维码或二维码不正确');
        });
      },
      /** 页面打开时请求服务器的可能存在的qr参数*/
      getWxConfig() {
        this.$http.getWxConfig({
          token: '',
          changeshopid: ''
        }).then(res => {
          if (res.code == 0 && res.data) {
            Cookies.set('helpImg', res.data.weixinHelp);
            this.sheettype = res.data.sheettype
            if (this.sheettype == '') {
              this.$toast('无法获取到业务类型');
              return; //此处应返回前一页面
            } else if (this.sheettype != 1) this.multiIpt = false;
            Cookies.set('sheettype', this.sheettype);
            this.$toast.clear();
            if (res.data.useQR == 1) { //是否启用扫码
              // this.$router.push('scan');
              // this.changePage(1)
              // return 
            }
            if (res.data.usePhone == 1) Cookies.set('usePhone', 1)
            else Cookies.set('usePhone', 0)
            if (res.data.useEmail == 1) Cookies.set('useEmail', 1)
            else Cookies.set('useEmail', 0)
            if (res.data.qr != 'null' && res.data.qr != '' && res.data.qr != null) {
              if (res.data.qr.length > 1) {
                const qrArr = res.data.qr.split("-");
                this.qr = res.data.qr;
                this.initWxdata();
              }
            }
          } else this.$toast('请从菜单进入');
        }).catch(err => {
          console.log(err)
        })
      }
    }
  };

</script>


<style scoped>
  .van-tab--active {
    color: #0066CC;
  }

  .tab-bar {
    position: fixed;
    top: 46px;
    left: 0;
    width: 100%;
    z-index: 100;
  }

  .homeChildView {
    padding: 10px 5px;
    background-color: #fff;
    margin-top: 44px;
    box-sizing: border-box;
    min-height: calc(100% - 46px);
  }

</style>
