<style scoped>
.margin-top-10 {
  margin-top: 10px;
}
</style>

<template>
    <div style="height:100%;background-color: #e4e4e4;">

        <!-- 头部导航结构 -->
        <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="发票信息">
            <van-icon style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
        </van-nav-bar>

        <van-cell-group class="margin-top-10">
            <van-cell title="发票类型：">
                <p style="text-align:left;margin:0;padding-left:20px;">电子发票</p>
            </van-cell>
            <van-cell title="销售方名称：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.xsfMc}}</p>
            </van-cell>
            <van-cell title="购买方名称：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.gmfMc}}</p>
            </van-cell>
            <van-cell title="发票金额：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.amount}}</p>
            </van-cell>
            <van-cell title="申请时间：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{nowTime}}</p>
            </van-cell>
            <van-cell title="邮箱地址：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.recvEmail}}</p>
            </van-cell>
            <van-cell title="电话号码：">
                <p style="text-align:left;margin:0;padding-left:20px;">{{paramsdata.recvPhone}}</p>
            </van-cell>
        </van-cell-group>

        <van-button @click="askInvoice"  style="background-color:#CC3327;margin-top:30px;" type="primary" bottom-action :disabled="loading">确认提交</van-button>
        
    </div>
</template>

<script>
import Util from "@/libs/util";
export default {
  data() {
    return {
      paramsdata: "",
      nowTime: "",
      loading:false
    };
  },
  methods: {
    toHome() {
      this.$router.push({
        name: "home",
        params: {}
      });
    },
    askInvoice() {
        this.$toast.loading({
            mask: true,
            message: '正在提交...'
        });
      this.loading=true;
      let querydata = [];
      querydata.push(this.paramsdata);
      this.$http({
        url: "/e-invoice-pro/rest/api/askInvoiceZM", //请求地址
        method: "post", //请求类型
        timeout:30000,
        data: querydata
        //请求参数对象
      }).then(res => {
          this.loading=false;
          this.$toast.clear();
        //  then中可直接只用this
        console.log(res);
        if(res.code==0){
            this.$dialog.alert({message:"开票成功"});
            let tempdata = {};
            tempdata.iqseqno = res.data[0].iqseqno;
            this.$router.push({ name: "invoice-info", query: tempdata });
        }else if(res.code==-9){
          var _this=this;
          this.$dialog.alert({
            message: res.message,
            confirmButtonText: "知道了",
            beforeClose:_this.toHome()
          });
        }else{
          this.$dialog.alert({
            message: res.message,
            confirmButtonText: "错误"
          });
        }
       
      });
    }
  },
  computed: {},
  mounted() {},
  activated() {
    console.log(this.$route.params);
    if(!this.$route.params.gmfMc) return;

    this.paramsdata = this.$route.params;
    
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if(month<10){
        month='0'+month;
    }
    var day = date.getDate();
    if(day<10){
        day='0'+day;
    }
    var hours = date.getHours();
     if(hours<10){
        hours='0'+hours;
    }
    var minutes = date.getMinutes();
    if(minutes<10){
        minutes='0'+minutes;
    }
    var seconds = date.getSeconds();
    if(seconds<10){
        seconds='0'+seconds;
    }
    this.nowTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  },
  created() {}
};
</script>


