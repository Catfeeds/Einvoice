<style scoped>
.margin-top-10 {
  margin-top: 10px;
}
.info{
  font-size: 12px;
  margin-top: 6px;
  margin-left: 12px;
  color: #999;
}
</style>

<template>
    <div style="height:100%;background-color: #e4e4e4;">

        <!-- 头部导航结构 -->
        <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="发票详情">
            <van-icon @click="toHome" style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
            <van-icon @click="toInvoiceList" style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-nav" slot="right" />
        </van-nav-bar>

        <van-cell-group class="margin-top-10">
            <van-cell title="发票类型：">
                <p style="text-align:left;margin:0;padding-left:20px;">电子发票</p>
            </van-cell>
            <van-cell title="购买方名称：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.iqgmfname}}</p>
            </van-cell>
            <van-cell title="发票金额：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.iqtotje}}</p>
            </van-cell>
            <van-cell title="申请时间：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.rtkprq}}</p>
            </van-cell>
            <van-cell title="邮箱地址：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.iqemail}}</p>
            </van-cell>
            <van-cell title="电话号码：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.iqtel}}</p>
            </van-cell>
        </van-cell-group>

        <van-row style="margin-top:30px;">
            <van-col span="12">
                <van-button @click="btnSendPdf" style="background-color:#ffffff;color:#000000;" bottom-action>重发至邮箱</van-button>
            </van-col>
            <van-col span="12">
                <!-- <van-button  style="background-color:#CC3327;" type="primary" bottom-action>下载电子发票</van-button> -->
                <van-button  @click="download" style="background-color:#CC3327;" type="primary" bottom-action>下载电子发票</van-button>
            </van-col>
        </van-row>

        <van-dialog
          v-model="showSendPdf"
          show-cancel-button
          :before-close="beforeSendPdf"
        >
          <div class="info">请确认下列邮箱，如有变更请修改</div>
          <van-field
            v-model="email"
            label="邮箱"
            placeholder="请输入邮箱"
            required
          />
        </van-dialog>

    </div>
</template>

<script>
import Util from "@/libs/util";
export default {
  data() {
    return {
      iqseqno: "",
      email:"",
      paramsdata:{},
      showSendPdf:false
    };
  },
  methods: {
    download(){
      window.open(this.paramsdata.iqpdf);
    },
    getInvque() {
        let querydata ={};
        querydata.iqseqno=this.iqseqno;
        this.$http({
        url: "/e-invoice-pro/rest/api/getInvque", //请求地址
        method: "post", //请求类型
        data: querydata
        //请求参数对象
      }).then(res => {
        //  then中可直接只用this
        this.paramsdata=res.data[0];
        this.email = this.paramsdata.iqemail;
        this.paramsdata.rtkprq = Util.formatDate(this.paramsdata.rtkprq);
      });
    },
    btnSendPdf(){
      this.showSendPdf = true;
    },
    beforeSendPdf(action,done){
      if (action === 'confirm') {
        if(this.email ==''){
          this.$dialog.alert({
              message: "邮箱必须填写",
              confirmButtonText: "知道了"
          });
        }
        var regEmail= /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,3}$/;
        if(this.email !='' &&  !regEmail.test(this.email)){
          this.$dialog.alert({
              message: "邮箱格式不正确"+this.email,
              confirmButtonText: "知道了"
          });
          return;
        }

        this.sendPdf(done);
      }else{
        done();
      }
    },
    sendPdf(done){
      let querydata ={};
      querydata.iqseqno=this.iqseqno;
      querydata.email = this.email;
      this.$http({
        url: "/e-invoice-pro/rest/api/sendPdf", //请求地址
        method: "post", //请求类型
        data: querydata
        //请求参数对象
      }).then(res => {
        done();
        //  then中可直接只用this
       if(res.code!=0){
           this.$dialog.alert({
            message: res.message,
            confirmButtonText: "错误"
          });
       }else{
          this.$dialog.alert({
            message: "邮件发送中，请稍后到邮箱查收",
            confirmButtonText: "成功"
          }); 
       }
      });
    },
    toHome() {
      this.$router.push({
        name: "home",
        params: {}
      });
    },
    toInvoiceList() {
      this.$router.push({
        name: "invoice-list",
        params: {}
      });
    }
  },
  computed: {},
  activated() {
    this.iqseqno = this.$route.query.iqseqno;
    console.log("b============:"+this.iqseqno);
    this.getInvque();
  },
  created() {}
};
</script>


