import Vue from 'vue';
import Cookies from 'js-cookie';
import router from './router/index';
import store from './store';
import App from './app.vue';
import request from "./libs/httpserver";
import NP from 'number-precision';

// 有赞UI组件库
import Vant from 'vant';
// import 'vant/lib/vant-css/index.css';
// import 'vant/lib/vant-css/icon-local.css';
import './style/style.css'
Vue.use(Vant);

// vue原型链拓展方法
Vue.prototype.$http = request;
Vue.prototype.$cookies = Cookies;

// 路由加载前钩子函数，一般添加全局权限验证操作
router.beforeEach((to, from, next) => {
  next();
});
router.afterEach((to) => {
  window.scrollTo(0, 0);
});
import Util from "@/libs/util";
// 全局注册过滤器
Vue.filter('formatDate', Util.formatDate);


new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App),
  created() {
    if (process.env.NODE_ENV !== 'production')
      this.$http.getProductionPermission({
        entid: "A00001002",
        openid: "A00001002",
        sheettype: 2,
        qr:'4add0d8879844fb5b5d07c07b294c27d00000002-33400'
      }).then(res => {
        Cookies.set('jsessionid', res.data.jsessionid);
      })
  }
});
