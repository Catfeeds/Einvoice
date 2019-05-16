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
                <p class="van-ellipsis" style="text-align:left;margin:0;padding-left:20px;" >{{taxname}}</p>
            </van-cell>
        </van-cell-group>

        <van-cell-group class="margin-top-10">
            <van-cell title="小票号：">
                <p class="van-ellipsis" style="text-align:left;margin:0;padding-left:20px;">{{sheetid}}</p>
            </van-cell>
            <van-cell title="金额：">
                <p class="van-ellipsis" style="text-align:left;margin:0;padding-left:20px;">￥{{invoiceamount}}</p>
            </van-cell>
            <van-cell>
                <p style="text-align:left;margin:0;color:#CC3300;">{{msg}}</p>
            </van-cell>
        </van-cell-group>

        <van-panel  title="标题"  class="margin-top-10">
            <div slot="header"  style="text-align:center;" >开票商品明细</div>
             <van-row gutter="10" style="font-size:12px;margin-top:10px;">
                <van-col span="16" style="text-align:left;padding-left:20px;">商品名称</van-col>
                <van-col span="3" style="text-align:left;">数量</van-col>
                <van-col span="5" style="text-align:left;">金额</van-col>
            </van-row>
            <van-cell v-for="(invoiceSale,index) in invoiceSaleDetail" :key="index">
                <div style="padding:0 0 10px 0;">
                    <van-row gutter="10" style="font-size:12px;margin-top:10px;">
                        <van-col span="16" >{{invoiceSale.goodsname}}</van-col>
                        <van-col span="3" style="text-align:center;">{{invoiceSale.qty}}</van-col>
                        <van-col span="5" style="text-align:right;">{{invoiceSale.amt}}</van-col>
                    </van-row>
                </div>
            </van-cell>

            <!-- <van-cell title="折扣：">
                <p style="text-align:left;margin:0;padding-left:20px;">￥-30</p>
            </van-cell> -->

        </van-panel>

        <van-cell-group class="margin-top-10">
            <van-cell @click="toHelp" style="color:#CC3300;font-weight:blod;" title=" " value="帮助　" >
              <van-icon slot="right-icon" name="question" class="van-cell__right-icon" />
            </van-cell>
        </van-cell-group>

        <van-button @click="nextAction" style="background-color:#CC3327;margin-top:10px;" type="primary" bottom-action>下一步</van-button>

    </div>
</template>

<script>
import Util from "@/libs/util";
export default {
  data() {
    return {
      paramsdata: {},
      taxname: "",
      shopname:"",
      sheetid: "",
      invoiceamount: "",
      invoiceSaleDetail: [],
      msg:""
    };
  },
  methods: {
    toHelp() {
    	this.$http({
                url: "/e-invoice-pro/rest/wx/getEntIdForZM", //请求地址
                method: "post"								 //请求类型
               }).then(res => {
               					let myEntId = res.data;
  								if (myEntId == 'ZM002')
    						  	{   
    								this.$router.push({name:"help6922",params:{}});
    							} 
    							else if (myEntId == 'ZM003')
    							{
    								this.$router.push({name:"help6921",params:{}});
    							}
    							else
    							{
    								this.$router.push({name:"help",params:{}});
    							}
    						  });
    },
    nextAction() {
      this.$router.push({
        name: "invoice-apply",
        params: this.paramsdata
      });
    }
  },
  computed: {},
  created() {
    console.log("scan-invoice.vue.created");
    console.log(this.$route.params);
    console.log("this.$route.params.flag:"+this.$route.params.flag);
    if(this.$route.params.flag==0){
        this.paramsdata = this.$route.params;
        console.log("this.paramsdata.gmfname:"+this.paramsdata.gmfname);
        if(this.paramsdata.gmfname!=undefined && this.paramsdata.gmfname !=''){
            this.msg="本笔交易为免税商品";
        }
        this.taxname = this.paramsdata.taxname;
        this.shopname = this.paramsdata.shopname;
        this.sheetid = this.paramsdata.sheetid;
        this.invoiceamount = this.paramsdata.invoiceamount;
        this.invoiceSaleDetail = this.paramsdata.invoiceSaleDetail;
    }
  }
};
</script>


