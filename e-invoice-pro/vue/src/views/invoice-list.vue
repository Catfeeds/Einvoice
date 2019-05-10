<style scoped>
.margin-top-10 {
  margin-top: 10px;
}
.van-row {
  font-size: 14px;
  line-height: 26px;
}
</style>

<template>
  <div style="height:100%;background-color: #e4e4e4;">

    <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="开票历史">
      <van-icon @click="toHome" style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
    </van-nav-bar>

    <div style="padding:0 10px 10px 10px;background-color: #e4e4e4;">
        <van-list v-model="loading" :finished="finished" @load="onLoad">
            <van-panel v-for="item in list" :key="item" class="margin-top-10">
              <div slot="header" style="color:#CC3323;">￥:{{item.iqtotje}} {{item.iqtypename}}</div>
               <router-link :to="{name:'invoice-info',query:{iqseqno:item.iqseqno}}">
              <van-cell>
                <div style="padding:0  5px;">
                  <van-row class="van-row" gutter="10">
                    <van-col span="8">发票代码：</van-col>
                    <van-col span="16" style="text-align:left;">{{item.rtfpdm}}</van-col>
                  </van-row>
                  <van-row class="van-row" gutter="10">
                    <van-col span="8">发票号码：</van-col>
                    <van-col span="16" style="text-align:left;">{{item.rtfphm}}</van-col>
                  </van-row>
                  <van-row class="van-row" gutter="10">
                    <van-col span="8">开票日期：</van-col>
                    <van-col span="16" style="text-align:left;">{{item.rtkprq}}</van-col>
                  </van-row>
                  <van-row class="van-row" gutter="10">
                    <van-col span="8">发票抬头：</van-col>
                    <van-col span="16" style="text-align:left;">{{item.iqgmfname}}</van-col>
                  </van-row>
                </div>
              </van-cell>
              </router-link>
            </van-panel>
          
        </van-list>
    </div>

  </div>
</template>

<script>
import Util from "@/libs/util";
export default {
  data() {
    return {
      list: [],
      loading: true,
      finished: false,
      isLoading: false,
      page:1,
      pagesize:5
    };
  },
  methods: {
    toHome() {
      this.$router.push({
        name: "home",
        params: {}
      });
    },
    onLoad() {
      let quertdata = {};
      quertdata.channel = "wx";
      quertdata.page = this.page;
      quertdata.pagesize = this.pagesize;
      this.$http({
        url: "/e-invoice-pro/rest/api/getInvque", //请求地址
        method: "post", //请求类型
        data: quertdata
        //请求参数对象
      }).then(res => {
        this.loading = false;
        if(res.code==0){
          this.page++;
          if(res.data.length==0){
            this.finished = true;
          }else{
            for (let index = 0; index < res.data.length; index++) {
              res.data[index].rtkprq = Util.formatDate(res.data[index].rtkprq);
              if(res.data[index].iqtype==1){
                res.data[index].iqtypename='[红字]';
              }else{
                res.data[index].iqtypename='';
              }
              this.list.push(res.data[index]);
            }
          }
        }else{
          this.finished = true;
        }
      });
    },
    onRefresh() {
      
    },
    formatDate(str){
      return str.substr(0,4)+"-"+str.substr(4,2)+"-"+str.substr(6,2)+" "+str.substr(8,2)+":"+str.substr(10,2)+":"+str.substr(12,2);
    }
  },
  computed: {},
  mounted() {
    this.onLoad();
  },
  created() {}
};
</script>


