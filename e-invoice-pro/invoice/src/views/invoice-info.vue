<template>
  <article>
    <van-nav-bar fixed title="发票详情">
      <van-icon @click="$router.go(-1)" name="arrow-left" slot="left" />
      <van-icon @click="toInvoiceList" name="wap-nav" slot="right" />
    </van-nav-bar>
    <van-cell-group>
      <van-cell title="发票类型：">
        <p>电子发票</p>
      </van-cell>
      <van-cell title="购买方名称：">
        <p>{{paramsdata.iqgmfname}}</p>
      </van-cell>
      <template v-if="paramsdata.iqgmftax">
        <van-cell title="纳税人识别号：">
          <p>{{paramsdata.iqgmftax}}</p>
        </van-cell>
        <van-cell title="银行账户：">
          <p>{{paramsdata.iqgmfbank}}</p>
        </van-cell>
        <van-cell title="地址电话：">
          <p>{{paramsdata.iqgmfadd}}</p>
        </van-cell>
      </template>
      <van-cell title="发票金额：">
        <p>{{paramsdata.iqtotje}}</p>
      </van-cell>
      <van-cell title="发票税额：">
        <p>{{paramsdata.iqtotse}}</p>
      </van-cell>
      <van-cell title="发票总额：">
        <p>{{tot}}</p>
      </van-cell>
      <van-cell title="申请时间：">
        <p v-if="paramsdata.rtkprq!= undefined">{{paramsdata.rtkprq | formatDate}}</p>
      </van-cell>
      <van-cell title="邮箱地址：" v-if="useEmail&&paramsdata.iqemail">
        <p>{{paramsdata.iqemail}}</p>
      </van-cell>
      <van-cell title="电话号码：" v-if="usePhone&&paramsdata.iqtel">
        <p>{{paramsdata.iqtel}}</p>
      </van-cell>
    </van-cell-group>

    <van-row style="margin-top:30px;">
      <van-col span="12">
        <van-button @click="showSendPdf=true" style="background-color:#ffffff;color:#000000;" bottom-action>重发至邮箱</van-button>
      </van-col>
      <van-col span="12">
        <van-button @click="download" style="background-color:#0066CC;" type="primary" bottom-action>下载电子发票</van-button>
      </van-col>
    </van-row>

    <van-dialog v-model="showSendPdf" show-cancel-button :before-close="beforeSendPdf">
      <div class="info">请确认下列邮箱，如有变更请修改</div>
      <van-field v-model="email" label="邮箱" placeholder="请输入邮箱" required />
    </van-dialog>
  </article>
</template>
<script>
  import Cookies from 'js-cookie';
  import NP from 'number-precision';
  export default {
    data() {
      return {
        email: "",
        paramsdata: {},
        showSendPdf: false,
        usePhone: 1,
        useEmail: 1,
        tot:0
      };
    },
    created() {
      this.usePhone = parseInt(Cookies.get('usePhone'));
      this.useEmail = parseInt(Cookies.get('useEmail'));
      this.getInvque();
    },
    // beforeRouteLeave(to,from,next){
    //   if(to.name=='confirm') this.$router.push('/') 
    //   next()
    // },
    methods: {
      download() {
        window.open(this.paramsdata.iqpdf);
      },
      /**获取发票详情 */
      getInvque() {
        this.$http.getInvque({
          iqseqno: this.$route.query.iqseqno
        }).then(res => {
          if (res.code == 0) {
            if (res.data) {
              if (res.data.length > 0) {
                this.paramsdata = res.data[0];
                this.tot = NP.plus(this.paramsdata.iqtotje,this.paramsdata.iqtotse);
                
                this.email = this.paramsdata.iqemail;
                return
              }
            }
          }
          this.$toast.fail('查询详情失败')
          this.$router.go(-1)
        });
      },
      /**重发发票确认 */
      beforeSendPdf(action, done) {
        if (action === 'confirm') {
          if (this.email == '') {
            this.$dialog.alert({
              message: "邮箱必须填写",
              confirmButtonText: "知道了"
            });
          }
          var regEmail =
            /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,3}$/;
          if (this.email != '' && !regEmail.test(this.email)) {
            this.$dialog.alert({
              message: "邮箱格式不正确" + this.email,
              confirmButtonText: "知道了"
            });
            return;
          }

          this.sendPdf(done);
        } else {
          done();
        }
      },
      /**重发发票结果 */
      sendPdf(done) {
        this.$http.sendPdf({
          iqseqno: this.$route.query.iqseqno,
          email: this.email
        }).then(res => {
          done();
          if (res.code != 0) {
            this.$dialog.alert({
              message: res.message,
              confirmButtonText: "错误"
            });
          } else {
            this.$dialog.alert({
              message: "邮件发送中，请稍后到邮箱查收",
              confirmButtonText: "成功"
            });
          }
        });
      },
      /**跳转发票列表页面 */
      toInvoiceList() {
        this.$router.push('invoice-list');
      }
    }
  };

</script>
<style scoped>
  .info {
    font-size: 12px;
    margin-top: 6px;
    margin-left: 12px;
    color: #999;
  }

  .van-cell p {
    text-align: left;
    margin: 0;
    padding-left: 20px;
  }

</style>
