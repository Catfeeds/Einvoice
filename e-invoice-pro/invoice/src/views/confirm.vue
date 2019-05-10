<template>
  <article>
    <!-- 头部导航结构 -->
    <van-nav-bar fixed title="发票信息">
      <van-icon name="arrow-left" slot="left" @click="$router.go(-1)" />
    </van-nav-bar>
    <van-cell-group class="margin-top-10">
      <van-cell title="发票类型：">
        <p>电子发票</p>
      </van-cell>
      <van-cell title="销售方名称：">
        <p>{{paramsdata.xsfMc}}</p>
      </van-cell>
      <van-cell title="购买方名称：">
        <p>{{paramsdata.gmfMc}}</p>
      </van-cell>
      <template v-if="paramsdata.gmfNsrsbh">
        <van-cell title="纳税人识别号：">
          <p>{{paramsdata.gmfNsrsbh}}</p>
        </van-cell>
        <van-cell title="银行账户：">
          <p>{{paramsdata.gmfYhzh}}</p>
        </van-cell>
        <van-cell title="地址电话：">
          <p>{{paramsdata.gmfDzdh}}</p>
        </van-cell>
      </template>
      <van-cell title="发票金额：">
        <p>{{paramsdata.amount}}</p>
      </van-cell>
      <van-cell title="申请时间：">
        <p>{{nowTime}}</p>
      </van-cell>
      <van-cell title="邮箱地址：" v-if="useEmail&&paramsdata.recvEmail">
        <p>{{paramsdata.recvEmail}}</p>
      </van-cell>
      <van-cell title="电话号码：" v-if="usePhone&&paramsdata.recvPhone">
        <p>{{paramsdata.recvPhone}}</p>
      </van-cell>
    </van-cell-group>
    <van-button @click="askInvoice" style="background-color:#0066CC;margin-top:30px;" type="primary" bottom-action :disabled="loading">确认提交</van-button>
  </article>
</template>

<script>
  import Util from "@/libs/util";
  import Cookies from 'js-cookie';
  export default {
    data() {
      return {
        paramsdata: {},
        nowTime: "",
        loading: false,
        usePhone: 1,
        useEmail: 1,
      };
    },
    created() {
      this.usePhone = parseInt(Cookies.get('usePhone'));
      this.useEmail = parseInt(Cookies.get('useEmail'));
      this.initData()
    },
    methods: {
      /**确认提交当前发票信息到服务器 */
      askInvoice() {
        this.$toast.loading({
          mask: true,
          message: '正在提交...'
        });
        this.loading = true;
        let querydata = [this.paramsdata];
        this.$http.askInvoice(querydata).then(res => {
          this.loading = false;
          this.$toast.clear();
          if (res.code == 0) {
            // this.$toast.success('开票成功')
            // this.removeCookiesData();
            this.$router.push({
              name: "invoice-info",
              query: {
                iqseqno: res.data[0].iqseqno
              }
            });
          } else {
            this.$toast(res.message)
            // this.$dialog.alert({
            //   message: res.message,
            //   confirmButtonText: "错误"
            // });
          }

        });
      },
      /**提交成功后清除缓存数据 */
      removeCookiesData() {
        const CookiesArr = ['gmfNsrsbh', 'gmfMc', 'gmfDwMc', 'recvEmail', 'recvPhone', 'gmfDzdh', 'gmfYhzh']
        CookiesArr.forEach(p => {
          const cook = Cookies.remove(p);
        });
        window.localStorage.removeItem('scanInvoice');
        window.localStorage.removeItem('invoiceApplay');
      },
      /**初始化待提交发票信息 获取当前时间 */
      initData() {
        const applyData = JSON.parse(window.localStorage.getItem('invoiceApplay'))
        // console.log(applyData)
        // if (!applyData.gmfMc) return;
        this.paramsdata = applyData;
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        if (month < 10) {
          month = '0' + month;
        }
        var day = date.getDate();
        if (day < 10) {
          day = '0' + day;
        }
        var hours = date.getHours();
        if (hours < 10) {
          hours = '0' + hours;
        }
        var minutes = date.getMinutes();
        if (minutes < 10) {
          minutes = '0' + minutes;
        }
        var seconds = date.getSeconds();
        if (seconds < 10) {
          seconds = '0' + seconds;
        }
        this.nowTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
