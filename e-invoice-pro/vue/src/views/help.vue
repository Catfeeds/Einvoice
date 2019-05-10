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
        <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="温馨提示">
            <van-icon style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
        </van-nav-bar>

        <div class="info-content">
            <p>尊敬的顾客：</p>
            <p class="info-content-m">免税商品开票，需在机场办理提货手续后，方可开具电子发票。</p>
            <p>一、开票方式：扫描二维码开票或提取码开票。</p>

            <p>1、扫描二维码开票，详情如下：</p>
            <van-row gutter="10">
                <van-col span="12">
                    <p style="text-align:center;font-size:12px;">免税商品扫码开票</p>
                    <img style="width:100%;" :src="pic1" alt="">
                </van-col>
                <van-col span="12">
                    <p style="text-align:center;font-size:12px;">有税商品扫码开票</p>
                    <img style="width:100%;" :src="pic2" alt="">
                </van-col>
            </van-row>

            <p>2、提取码开票</p>
            <van-row gutter="10">
                <van-col span="12">
                    <p style="text-align:center;font-size:12px;">免税商品扫码开票</p>
                    <img style="width:100%;" :src="pic3" alt="">
                </van-col>
                <van-col span="12">
                    <p style="text-align:center;font-size:12px;">有税商品扫码开票</p>
                    <img style="width:100%;" :src="pic4" alt="">
                </van-col>
            </van-row>

            <p>二、电子券、微信券、纸券、积分、储值卡等相关优惠支付金额，不能开发票。</p>
            <p>三、税款担保金以及行邮税金额，不能开具发票。</p>
            <p>四、有税购物小票，无法补开，遗失不能开具发票。</p>
            <p>五、如有疑问，请致电400-699-6956。</p>
            <p>六、最终解释权归三亚国际免税城所有。</p>
        </div>

    </div>

</template>

<script>
import u120 from "../images/u120.jpg";
import u124 from "../images/u124.jpg";

import u136 from "../images/u136.jpg";
import u134 from "../images/u134.jpg";

export default {
  data() {
    return {
      pic1: u120,
      pic2: u124,
      pic3: u136,
      pic4: u134
    };
  },
  methods: {},
  mounted() {},
  created() {}
};
</script>


