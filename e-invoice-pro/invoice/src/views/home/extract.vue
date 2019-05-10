<template>
  <div>
    <div style="height:370px;">
      <section class="extract_ipt_box van-hairline--top-bottom">
        <label>发票提取码</label>
        <section>
          <div v-for="(item,key) in getIptArray" :key="key">
            <input type="text" :placeholder="item.placeholder" ref="ipts">
          </div>
          <!--  :maxlength="item.maxLength"  @keyup="inputCode(key,$event)" -->
        </section>
      </section>
      <van-row>
        <van-col offset="1" span="23" class="fs14 red margin-top-10">示例：1001-0346-3473431-18.6</van-col>
      </van-row>
      <div style="padding:0 20px;margin-top:40px;">
        <van-button style="background-color:#0066CC;" type="primary" bottom-action @click="getInvoiceBillInfoWithDetail">校验</van-button>
        <div>
          <p class="tac red">{{errorMsg1}}</p>
          <p class="tac red">{{errorMsg2}}</p>
        </div>
      </div>
    </div>

    <!-- <div class="plr10">
      <p class="info-title" @click="showHelp">开票说明
        <van-icon name="question" />
      </p>
      <div class="info-content" v-show="helpDisplay">
        <p>尊敬的顾客：</p>
        <p class="info-content-m">免税商品开票，需在机场办理提货手续后，方可开具电子发票。</p>
        <p class="info-content-m">通过扫描销售小票上的二维码，即可开具发票。</p>

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
      </div>
    </div> -->
  </div>
</template>

<script>
  import u120 from "../../images/u120.jpg";
  import u124 from "../../images/u124.jpg";
  export default {
    props: {
      multiIpt: {//是否为4个输入框 false则为2个
        type: Boolean,
        default: true
      },
      sheettype:{
        default:1
      }
    },
    data() {
      return {
        pic1: u120,
        pic2: u124,
        // sheettype: 1,
        sheetid: "",
        errorMsg1: "",
        errorMsg2: "",
        helpDisplay: true
      }
    },
    methods: {
      //   界面切换事件
      changePage(index, title) {
        console.log("index:", index);
        console.log("title:", title);
      },
      showHelp() {
        this.helpDisplay = !this.helpDisplay;
      },
      getInvoiceBillInfoWithDetail() {
        if (this.$refs.ipts.some(p => p.value.length === 0)) {
          this.errorMsg1 = "必须录入完整提取码和金额";
          return;
        } else {
          this.errorMsg1 = "";
          this.errorMsg2 = "";
        }
        this.sheetid = "";
        this.$refs.ipts.forEach((q, i) => {
          this.sheetid += q.value;
          if (i < this.$refs.ipts.length - 1) this.sheetid += '-';
        })
        this.$http.getInvoiceBillInfoWithDetail({
          ticketQC: this.sheetid,
          sheettype: this.sheettype
        }).then(res => {
          if (res.code == 0) {
            if (res.data.flag == 0) {
              window.localStorage.setItem('scanInvoice', JSON.stringify(res.data))
              this.$router.push('scan-invoice');
            } else if (res.data.flag == 1 && res.data.iqstatus == 30) this.$toast('正在开票，请稍后')
            else this.$router.push({
              name: "invoice-info",
              query: {
                iqseqno: res.data.iqseqno
              }
            });
            // if (res.data.flag == 1) {
            //   if (res.data.iqstatus == 30) this.$toast('正在开票，请稍后')
            //   else this.$router.push({
            //     name: "invoice-info",
            //     query: {
            //       iqseqno: res.data.iqseqno
            //     }
            //   });
            // } else {
            //   window.localStorage.setItem('scanInvoice', JSON.stringify(res.data));
            //   this.$router.push('scan-invoice');
            // }
          } else if (res.code == -6) {
            this.errorMsg1 = "校验失败"
            this.errorMsg2 = "请在提货后开具发票"
          } else if (res.code == -9) {
            this.$dialog.alert({
              message: res.message,
              confirmButtonText: "知道了"
            });
          } else {
            this.errorMsg1 = "校验失败"
            this.errorMsg2 = res.message
          }
        });
      },
      inputCode(index, e) {
        //暂不需要自动跳下一个输入框
        // const p = e.target;
        // if ((index <= 1 && p.value.length == 4) || (index == 2 && p.value.length == 3))
        //   this.$refs.ipts[index + 1].focus();
        this.sheetid = "";
        this.$refs.ipts.forEach((q, i) => {
          this.sheetid += q.value;
          if (i < this.$refs.ipts.length - 1) this.sheetid += '-';
        })
        if (this.sheetid === '---') this.sheetid = '';
      }
    },
    computed: {
      getIptArray() {
        if (this.multiIpt) return [{
            maxLength: 4,
            placeholder: '门店号'
          },
          {
            maxLength: 4,
            placeholder: '收银号'
          },
          {
            maxLength: 3,
            placeholder: '流水号'
          },
          {
            maxLength: -1,
            placeholder: '金额'
          }
        ]
        else return [{
            maxLength: 14,
            placeholder: '单号'
          },
          {
            maxLength: -1,
            placeholder: '金额'
          }
        ]
      }
    }
  };

</script>
<style lang="less" scoped>
  .extract_ipt_box {
    width: 100%;
    margin-top: 20px;
    box-sizing: border-box;
    padding: 10px 15px;
    padding-right: 0;
    display: flex;
    font-size: 14px;
    line-height: 24px;
    &::before {
      content: '*';
      position: absolute;
      left: 7px;
      font-size: 14px;
      color: #f44;
    }
    label {
      width: 90px;
    }
    section {
      flex: 1;
      display: flex;
      div{
        width: 45px;
        padding-right: 9px;
        position: relative;
        &:last-of-type {
          padding: 0;
          &::after{
            display: none;
          }
        }
        &::after{
          content: '';
          display: block;
          width: 5px;
          height: 1px;
          position: absolute;
          right: 2px;
          top: 50%;
          background-color: #333;
        }
      }
      input {
        width: 100%;
        padding: 0;
        border: none;
        background: transparent;
        &[placeholder='单号'] {
          width: 142px
        }
      }
    }
  }

</style>

<style scoped>
  .red {
    color: #f44;
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

  img {
    width: 100%
  }

</style>
