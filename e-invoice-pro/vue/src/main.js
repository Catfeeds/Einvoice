import Vue from 'vue';
import { router } from './router/index';
import { appRouter } from './router/router';
import store from './store';
import App from './app.vue';

// 有赞UI组件库
import Vant from 'vant';
import 'vant/lib/vant-css/index.css';
import 'vant/lib/vant-css/icon-local.css';
Vue.use(Vant);

// 封装网络请求，在vue中使用$http来启动http请求
import request from "./libs/httpserver";
Vue.prototype.$http = request;

import Cookies from 'js-cookie';
Vue.prototype.$cookies = Cookies;

import Util from "@/libs/util";

new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App),
    data: {
    },
    mounted() {
    },
    created() {
        /*
        this.$http({
            url: "/e-invoice-pro/rest/wx/wxopen", //请求地址
            method: "get", //请求类型
            data: {
                entid: "A00001002",
                openid: "12345678",
                sheettype: "6"
            } //请求参数对象
        }).then(res => { 
            Cookies.set('jsessionid',res.data.jsessionid);
        });
        */
    }
});
