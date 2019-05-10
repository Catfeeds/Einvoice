<template>
  <div>

    <!-- 头部导航结构 -->
    <van-nav-bar fixed title="微信开票申请">
      <van-icon name="wap-home" slot="left" />
      <van-icon @click="toInvoiceList" name="wap-nav" slot="right" />
    </van-nav-bar>

    <van-cell-group class="margin-top-10">
      <van-cell title="发票代码：">
        <p style="text-align:left;margin:0;padding-left:20px;">0218819001111</p>
      </van-cell>
      <van-cell title="发票号码：">
        <p style="text-align:left;margin:0;padding-left:20px;">4303417</p>
      </van-cell>
      <van-cell title="开票日期：">
        <p style="text-align:left;margin:0;padding-left:20px;">2018-04-11 13:25:22</p>
      </van-cell>
      <van-cell title="发票金额：">
        <p style="text-align:left;margin:0;padding-left:20px;">430</p>
      </van-cell>
      <van-cell title="发票状态：">
        <p style="text-align:left;margin:0;padding-left:20px;">已开票</p>
      </van-cell>
    </van-cell-group>

    <van-button style="background-color:#0066CC;margin-top:30px;" type="primary" bottom-action>插入微信卡包</van-button>

  </div>
</template>

<script>
  import Util from "@/libs/util";
  export default {
    data() {
      return {};
    },
    methods: {
      toInvoiceList() {
        this.$router.push({
          name: "invoice-list",
          params: {}
        });
      }
    },
    computed: {},
    mounted() {},
    created() {}
  };

</script>
