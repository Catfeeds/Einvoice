<template>
  <div class="invoiceList">
    <van-nav-bar fixed title="开票历史">
      <van-icon @click="toHome" name="wap-home" slot="left" />
      <van-icon @click="refreshList" name="upgrade" slot="right" />
    </van-nav-bar>
    <van-list v-model="loading" :finished="finished" @load="onLoad">
      <van-panel v-for="(item,index) in list" :key="index" class="margin-top-10">
        <div slot="header" class="listItemHeader">￥:{{item.iqtotje}}</div>
        <van-cell @click="goInfo(item.iqseqno)">
          <div style="padding:0  5px;">
            <van-row class="van-row" gutter="10">
              <van-col span="8">发票代码：</van-col>
              <van-col span="16">{{item.rtfpdm}}</van-col>
            </van-row>
            <van-row class="van-row" gutter="10">
              <van-col span="8">发票号码：</van-col>
              <van-col span="16">{{item.rtfphm}}</van-col>
            </van-row>
            <van-row class="van-row" gutter="10">
              <van-col span="8">开票日期：</van-col>
              <van-col span="16">{{item.rtkprq|formatDate}}</van-col>
            </van-row>
            <van-row class="van-row" gutter="10">
              <van-col span="8">发票抬头：</van-col>
              <van-col span="16">{{item.iqgmfname}}</van-col>
            </van-row>
          </div>
        </van-cell>
      </van-panel>
    </van-list>
    <div v-show="finished" class="listLastest">没有更多了</div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        list: [],
        loading: true,
        finished: false,
        isLoading: false,
        page: 1,
        pagesize: 5,
        showMore: false
      };
    },
    created() {
      this.onLoad();
    },
    methods: {
      toHome() {
        this.$router.push('/');
      },
      refreshList() {
        this.list = [];
        this.page = 1;
        this.onLoad();
      },
      onLoad() {
        this.$http.getInvque({
          channel: "wx",
          page: this.page,
          pagesize: this.pagesize
        }).then(res => {
          this.loading = false;
          if(res.code==0){
            if(res.data.length == 0) this.finished = true;
            else{
              this.page++;
              this.list.push(...res.data)
            }
          }else{
            this.finished = true;
            this.$toast.fail(res.message)
          }
        });
      },
      goInfo(iqseqno) {
        this.$router.push({
          name: 'invoice-info',
          query: {
            iqseqno
          }
        })
      }
    }
  };

</script>

<style scoped>
  .invoiceList {
    padding: 46px 10px 10px;
  }

  .listItemHeader {
    color: #CC3323;
  }

  .listLastest {
    margin-top: 10px;
    text-align: center;
    font-size: 12px;
  }

  .van-row {
    font-size: 14px;
    line-height: 26px;
  }

</style>
