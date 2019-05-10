<template>
  <article>
    <!-- 头部导航结构 -->
    <van-nav-bar fixed title="发票信息">
      <van-icon name="arrow-left" slot="left" @click="$router.go(-1)" />
    </van-nav-bar>

    <van-cell-group>
      <van-cell title="发票类型：">
        <p class="texta" style="color:#CC3300;">电子发票</p>
      </van-cell>
    </van-cell-group>

    <van-cell-group class="margin-top-10">
      <van-cell title="销售方名称：">
        <p class="van-ellipsis texta">{{paramsdata.taxname}}</p>
      </van-cell>
    </van-cell-group>

    <van-cell-group class="margin-top-10">
      <van-cell title="小票号：">
        <p class="van-ellipsis texta">{{paramsdata.sheetid}}</p>
      </van-cell>
      <van-cell title="金额：">
        <p class="van-ellipsis texta">￥{{paramsdata.invoiceamount}}</p>
      </van-cell>
      <van-cell>
        <p style="text-align:left;margin:0;color:#CC3300;">{{msg}}</p>
      </van-cell>
    </van-cell-group>

    <van-panel title="标题" class="margin-top-10">
      <div slot="header" class="tac" @click="showCould=!showCould">
        可开票商品明细
        <van-icon :class="{show:showCould}" name="upgrade"></van-icon>
      </div>
      <template v-if="showCould">
        <van-row gutter="10" style="font-size:12px;padding:10px 0;">
          <van-col span="16" style="text-align:left;padding-left:20px;">商品名称</van-col>
          <van-col span="3" class="tal">数量</van-col>
          <van-col span="5" class="tal">金额</van-col>
        </van-row>
        <van-cell v-for="(invoiceSale,index) in invoiceSaleDetailY" :key="index">
          <div>
            <van-row gutter="10" class="fl12">
              <van-col span="16">{{invoiceSale.goodsname}}</van-col>
              <van-col span="3" class="tac">{{invoiceSale.qty}}</van-col>
              <van-col span="5" class="tar">{{invoiceSale.amt}}</van-col>
            </van-row>
          </div>
        </van-cell>
      </template>
    </van-panel>
    <van-panel title="标题" class="margin-top-10" v-if="invoiceSaleDetailN.length>0">
      <div slot="header" class="tac" @click="showNo=!showNo">不可开票商品明细<van-icon :class="{show:showNo}" name="upgrade"></van-icon></div>
      <template v-if="showNo">
        <van-row gutter="10" style="font-size:12px;padding:10px 0;">
          <van-col span="16" style="text-align:left;padding-left:20px;">商品名称</van-col>
          <van-col span="3" class="tal">数量</van-col>
          <van-col span="5" class="tal">金额</van-col>
        </van-row>
        <van-cell v-for="(invoiceSale,index) in invoiceSaleDetailN" :key="index">
          <div>
            <van-row gutter="10" style="font-size:12px">
              <van-col span="16">{{invoiceSale.goodsname}}</van-col>
              <van-col span="3" class="tac">{{invoiceSale.qty}}</van-col>
              <van-col span="5" class="tar">{{invoiceSale.amt}}</van-col>
            </van-row>
          </div>
        </van-cell>
      </template>
      <!-- <van-cell title="折扣：">
                <p class="texta">￥-30</p>
            </van-cell> -->

    </van-panel>

    <van-cell-group class="margin-top-10">
      <van-cell @click="toHelp" style="color:#CC3300;font-weight:blod;" title=" " value="帮助　">
        <van-icon slot="right-icon" name="question" class="van-cell__right-icon" />
      </van-cell>
    </van-cell-group>

    <van-button @click="nextAction" :disabled="invoiceNo" style="background-color:#0066CC;margin-top:10px;" type="primary" bottom-action>下一步</van-button>

  </article>
</template>

<script>
  import Util from "@/libs/util";
  export default {
    data() {
      return {
        paramsdata: {},
        shopname: "",
        invoiceSaleDetailY: [],
        invoiceSaleDetailN: [],
        msg: "",
        showCould: false,
        showNo: false,
        invoiceNo: false //是否不可开票 默认false可开票
      };
    },
    methods: {
      toHelp() {
        this.$router.push('help');
      },
      nextAction() {
        this.$router.push("invoice-apply");
      }
    },
    created() {
      const scanInvoice = JSON.parse(window.localStorage.getItem('scanInvoice'))
      if (!scanInvoice) {
        this.$dialog.alert({
          message: "请输入发票提取码查询",
          confirmButtonText: "知道了"
        });
        this.$router.go(-1);
      } else {
        if (scanInvoice.flag == 0) {
          this.paramsdata = scanInvoice;
          if (this.paramsdata.gmfname != undefined && this.paramsdata.gmfname != '') {
            this.msg = "本笔交易为免税商品";
          }
          this.shopname = this.paramsdata.shopname;
          this.invoiceSaleDetailY = this.paramsdata.invoiceSaleDetail.filter(p => p.isinvoice == 'Y');
          this.invoiceSaleDetailN = this.paramsdata.invoiceSaleDetail.filter(p => p.isinvoice == 'N');
          if (this.paramsdata.invoiceamount <= 0) {
            this.msg = "本笔交易不可开票";
            this.invoiceNo = true;
          }
        }
      }
    }
  };

</script>
<style scoped>
  .margin-top-10 {
    margin-top: 5px;
  }

  .tac {
    font-size: 14px;
    line-height: 30px;
  }
  .tac .van-icon{
    vertical-align: middle;
    margin-left: 10px;
  }
  .van-icon.show{
    transform: rotateZ(180deg)
  }
</style>
