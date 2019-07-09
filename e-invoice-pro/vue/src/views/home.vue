<style scoped>
.van-tab--active{
  color:#CC3327 !important;
}
.van-tabs__nav-bar{
    z-index: 1;
    left: 0;
    bottom: 15px;
    height: 2px;
    position: absolute;
    background-color: #CC3327 !important;
}

</style>

<template>
  <div>
    <!-- 头部导航结构 -->
    <van-nav-bar style="background-color:#CC3327;color:#ffffff;" title="微信开票申请">
      <van-icon style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-home" slot="left" />
      <van-icon @click="toInvoiceList" style="color:#ffffff;font-size:20px;font-weight:bold;" name="wap-nav" slot="right" />
    </van-nav-bar>

    <!-- 导航结构 -->
    <van-tabs @click="changePage" class="tab-bar">
      <van-tab title="扫码开票"></van-tab>
      <van-tab title="提取码开票"></van-tab>
    </van-tabs>

    <!-- 子界面路由导航 -->
    <router-view></router-view>

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
    },

    //   界面切换事件
    changePage(index, title) {
      this.$http({
                url: "/e-invoice-pro/rest/wx/getEntIdForZM", //请求地址
                method: "post"								 //请求类型
               }).then(res => {
               					let myEntId = res.data;
      if (index == 0) {
      	if (myEntId=='ZM002')
	    {   
	    	this.$router.push({name:"scan6922",params:{}});
	    } 
	    else if (myEntId=='ZM003')
	    {
	    	this.$router.push({name:"scan6921",params:{}});
	    }
	    else
	    {
        	this.$router.push({name: "scan",params: {}});
        }
      } 
      else {
      	if (myEntId=='ZM002')
      	{
        	this.$router.push({name: "extract6922", params: {}});
        }
        else if (myEntId=='ZM003')
	    {
	    	this.$router.push({name:"extract6921",params:{}});
	    }
      	else
      	{
        	this.$router.push({name: "extract", params: {}});
        }
      }
    });
   }
  },
  computed: {},
  mounted() {},
  activated() {
  	//根据企业切换界面
  	let qr = Util.getUrlKey("qr");
  	if (qr=='null'||qr==''||qr==null){
  		this.$http({
                url: "/e-invoice-pro/rest/wx/getEntIdForZM", //请求地址
                method: "post"								 //请求类型
               }).then(res => {
               					let myEntId = res.data;

  								if (myEntId == 'ZM002')
    						  	{   
    								this.$router.push({name:"scan6922",params:{}});
    							} 
    							else if (myEntId == 'ZM003')
    							{
    								this.$router.push({name:"scan6921",params:{}});
    							}
    							else
    							{
    								this.$router.push({name:"scan",params:{}});
    							}
    						  });
    }
  },
  created() {
    	//读取可能存在的qr参数
        let qr = Util.getUrlKey("qr");
        console.log("qr:"+qr);
        if(qr!='null' && qr!='' && qr!=null){
            let querydata = {
                ticketQC: qr,
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
                        console.log("home.vue.created");
                        console.log(res.data);
                        this.$router.push({
                        name: "scan-invoice",
                        params: res.data
                        });
                    }
                }else if (res.code == -2) {
                    this.$dialog.alert({
                        message: "不能识别二维码或二维码不正确",
                        confirmButtonText: "知道了"
                    });
                }else if (res.code == -6) {
                    this.$dialog.alert({
                        message: "未扫描到二维码或者二维码不正确，请重试",
                        confirmButtonText: "知道了"
                    });
                }else if (res.code == -9) {
                    this.$dialog.alert({
                        message: res.message,
                        confirmButtonText: "知道了"
                    });
                } else {
                    this.$dialog.alert({
                        message: "不能识别二维码或二维码不正确",
                        confirmButtonText: "知道了"
                    });
                }
            });
        } 
  }
};
</script>


