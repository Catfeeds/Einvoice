<style scoped>
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
  font-size: 12rem;
}
.scan-p {
  font-size: 14px;
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
      <div style="height:280px;">
        <van-cell-group style="margin-top:20px;">
            <van-field v-model="sheetid" label="发票提取码" placeholder="请填写发票提取码" required />
            <van-field  v-model="invoiceamount" type="number" label="小票金额" placeholder="请填写小票金额" required />
        </van-cell-group>
        <div style="padding:0 20px;margin-top:40px;">
            <van-button style="background-color:#CC3327;" type="primary" bottom-action @click="getInvoiceBillInfoWithDetail">校验</van-button>
            <div>
                <p style="text-align:center;color:#CC3327;">{{errorMsg1}}</p>
                <p style="text-align:center;color:#CC3327;">{{errorMsg2}}</p>
            </div>
        </div>
      </div>

        <div class="info">
            <p class="info-title" @click="showHelp">开票说明 <van-icon name="question" /></p>
            <div class="info-content" v-show="helpDisplay">
                <p>尊敬的顾客：</p>
                <p class="info-content-m">免税商品开票，需在机场办理提货手续后，方可开具电子发票。</p>
                <p class="info-content-m">可通过扫描销售小票上的二维码，即可开具发票。</p>

                <van-row gutter="10">
                    <van-col span="12">
                        <p style="text-align:center;font-size:12px;">免税销售小票</p>
                        <img style="width:100%;" :src="pic1" alt="">
                    </van-col>
                    <van-col span="12">
                        <p style="text-align:center;font-size:12px;">有税销售小票</p>
                        <img style="width:100%;" :src="pic2" alt="">
                    </van-col>
                </van-row>
            </div>
        </div>

    </div>
</template>

<script>
import h234 from "../../images/h234.jpg";
import h456 from "../../images/h456.jpg";
export default {
  data() {
    return {
      pic1: h234,
      pic2: h456,
      sheetid: "",
      invoiceamount: "",
      errorMsg1: "",
      errorMsg2: "",
      helpDisplay:true
    };
  },
  methods: {
    //   界面切换事件
    changePage(index, title) {
      console.log("index:", index);
      console.log("title:", title);
    },
    showHelp(){
      this.helpDisplay=!this.helpDisplay;
    },
    getInvoiceBillInfoWithDetail() {
      if(this.sheetid=='' || this.invoiceamount==''){
        this.errorMsg1="必须录入提取码和金额";
        return;
      }else{
        this.errorMsg1="";
        this.errorMsg2="";
      }
      let querydata = {
        ticketQC: this.sheetid + "-" + this.invoiceamount,
        sheettype: 6
      };
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
        }else if (res.code == -6) {
          (this.errorMsg1 = "校验失败"),
            (this.errorMsg2 = "请在提货后开具发票");
        }else if (res.code == -9) {
          this.$dialog.alert({
              message: res.message,
              confirmButtonText: "知道了"
          });
        }else{
          (this.errorMsg1 = "校验失败"),
            (this.errorMsg2 = "请输入正确的提取码和小票金额");
        }
      });
    }
  },
  mounted() {},
  created() {}
};
</script>


