import Vue from 'vue';
import router from 'vue-router';

Vue.use(router);

// 路由配置
export default new router({
  // mode: 'history',
  routes: [{
      path: '/',
      name: 'home',
      redirect: 'extract',
      title: '首页',
      component: () =>
        import ('@/views/home.vue'),
      children: [{
          path: 'extract',
          title: '提取码开票',
          name: 'extract',
          component: () =>
            import ('@/views/home/extract.vue')
        },
        {
          path: 'scan',
          title: '扫描开票',
          name: 'scan',
          component: () =>
            import ('@/views/home/scan.vue')
        }
      ]
    },
    {
      path: '/scan-invoice',
      name: 'scan-invoice',
      title: '扫描开发票',
      component: () =>
        import ('@/views/scan-invoice.vue'),
    },
    {
      path: '/help',
      name: 'help',
      title: '帮助',
      component: () =>
        import ('@/views/help.vue'),
    },
    {
      path: '/invoice-apply',
      name: 'invoice-apply',
      title: '开票申请',
      component: () =>
        import ('@/views/invoice-apply.vue'),
    },
    {
      path: '/confirm',
      name: 'confirm',
      title: '确认信息',
      component: () =>
        import ('@/views/confirm.vue'),
    },
    {
      path: '/download',
      name: 'download',
      title: '发票下载',
      component: () =>
        import ('@/views/download.vue'),
    },
    {
      path: '/invoice-list',
      name: 'invoice-list',
      title: '历史发票查询',
      component: () =>
        import ('@/views/invoice-list.vue'),
    },
    {
      path: '/invoice-info',
      name: 'invoice-info',
      title: '发票详情',
      component: () =>
        import ('@/views/invoice-info.vue'),
    }
  ]
});
