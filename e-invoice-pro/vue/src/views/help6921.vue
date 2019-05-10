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
  margin-top: 80px;
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
  padding: 20px;
}
.info-content-m {
  text-indent: 28px;
}
</style>

<template>

    <div>
        <!-- 头部导航结构 -->
        <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="帮助说明">
            <van-icon style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
        </van-nav-bar>

        <div class="info-content">
            <p>温馨提示：</p>
            <p class="info-content-m">尊敬的顾客，免税商品开票，需在机场办理提货手续后，方可开具电子发票。</p>
            <p>一、开票方式：</p>
            <p>1.扫码开票，详情如下图。</p>

            <van-row gutter="10">
                    <p style="text-align:center;font-size:14px;">免税销售小票</p>
                    <img style="width:100%;" :src="pic1" alt="">
            </van-row>

            <p>2、提取码开票，详情如下图。</p>
            <van-row gutter="10">
                    <p style="text-align:center;font-size:14px;">免税销售小票</p>
                    <img style="width:100%;" :src="pic3" alt="">
            </van-row>

            <p>二、电子券、微信券、纸券、积分、储值卡等等相关优惠支付金额，不能开发票。</p>
            <p>三、部分特殊优惠商品，不能开具发票。</p>
            <p>四、税款担保金以及行邮税部分金额，不能开具发票。</p>
            <p>五、有税小票丢失，不能补回小票，且不能开具发票。</p>
            <p>六、如有疑问，请联系琼海免税店客服0898-62966251我们将竭诚为您解答。</p>
            <p>七、所有权由琼海免税店解释。</p>
        </div>

    </div>

</template>

<script>
import b123 from "../images/b123.jpg";

import b234 from "../images/b234.jpg";

export default {
  data() {
    return {
      pic1: b123,
      pic3: b234,
    };
  },
  methods: {},
  mounted() {},
  created() {}
};
</script>


