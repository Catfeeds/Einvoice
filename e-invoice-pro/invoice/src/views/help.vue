<template>
  <article>
    <van-nav-bar fixed title="温馨提示">
      <van-icon name="arrow-left" slot="left" @click="$router.go(-1)" />
    </van-nav-bar>
    <div class="info-content">
      <img :src="url+helpImg">
      <!-- <p>尊敬的顾客：</p>
      <p class="info-content-m">免税商品开票，需在机场办理提货手续后，方可开具电子发票。</p>
      <p>一、开票方式：扫描二维码开票或提取码开票。</p>
      <p>1、扫描二维码开票，详情如下：</p>
      <van-row gutter="10">
        <van-col span="12">
          <p class="tac fs12">免税商品扫码开票</p>
          <img :src="pic1">
        </van-col>
        <van-col span="12">
          <p class="tac fs12">有税商品扫码开票</p>
          <img :src="pic2">
        </van-col>
      </van-row>
      <p>2、提取码开票</p>
      <van-row gutter="10">
        <van-col span="12">
          <p class="tac fs12">免税商品扫码开票</p>
          <img :src="pic1">
        </van-col>
        <van-col span="12">
          <p class="tac fs12">有税商品扫码开票</p>
          <img :src="pic2">
        </van-col>
      </van-row>
      <p>二、电子券、微信券、纸券、积分、储值卡等相关优惠支付金额，不能开发票。</p>
      <p>三、税款担保金以及行邮税金额，不能开具发票。</p>
      <p>四、有税购物小票，无法补开，遗失不能开具发票。</p>
      <p>五、如有疑问，请致电400-699-6956。</p>
      <p>六、最终解释权归三亚国际免税城所有。</p> -->
    </div>
  </article>
</template>
<script>
  import u120 from "../images/u120.jpg";
  import u124 from "../images/u124.jpg";
  import Cookies from 'js-cookie';
  export default {
    data() {
      return {
        pic1: u120,
        pic2: u124,
        helpImg: '',
        url: ''
      };
    },
    created() {
      this.url = window.location.href.split('wx.html')[0];
      this.helpImg = Cookies.get('helpImg')
    }
  };

</script>
<style scoped>
  .info-content {
    box-sizing: border-box;
    height: 100%;
    padding: 20px;
    color: #6f6666;
    background-color: #fff;
  }

  .info-content-m {
    text-indent: 28px;
  }

  img {
    width: 100%;
  }

</style>
