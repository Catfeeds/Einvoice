

// appRouter数组内每个对象代表一个界面路由
export const appRouter = [
    {
        path: '/',
        name: 'home',
        redirect: 'scan',
        title: '首页',
        component: () => import('@/views/home.vue'),
        children: [
            { path: 'scan', title: '扫描开票', name: 'scan', component: () => import('@/views/home/scan.vue') },
            { path: 'extract', title: '提取码开票', name: 'extract', component: () => import('@/views/home/extract.vue') },
            { path: 'scan6921', title: '扫描开票', name: 'scan6921', component: () => import('@/views/home/scan6921.vue') },
            { path: 'extract6921', title: '提取码开票', name: 'extract6921', component: () => import('@/views/home/extract6921.vue') },
            { path: 'scan6922', title: '扫描开票', name: 'scan6922', component: () => import('@/views/home/scan6922.vue') },
            { path: 'extract6922', title: '提取码开票', name: 'extract6922', component: () => import('@/views/home/extract6922.vue') }
        ]
    },
    {
        path: '/scan-invoice',
        name: 'scan-invoice',
        title: '扫描开发票',
        component: () => import('@/views/scan-invoice.vue'),
    },
    {
        path: '/help',
        name: 'help',
        title: '帮助',
        component: () => import('@/views/help.vue'),
    },
    {
        path: '/help6921',
        name: 'help6921',
        title: '帮助',
        component: () => import('@/views/help6921.vue'),
    },
    {
        path: '/help6922',
        name: 'help6922',
        title: '帮助',
        component: () => import('@/views/help6922.vue'),
    },
    {
        path: '/invoice-apply',
        name: 'invoice-apply',
        title: '开票申请',
        component: () => import('@/views/invoice-apply.vue'),
    },
    {
        path: '/confirm',
        name: 'confirm',
        title: '确认信息',
        component: () => import('@/views/confirm.vue'),
    },
    {
        path: '/download',
        name: 'download',
        title: '发票下载',
        component: () => import('@/views/download.vue'),
    },
    {
        path: '/invoice-list',
        name: 'invoice-list',
        title: '历史发票查询',
        component: () => import('@/views/invoice-list.vue'),
    },
    {
        path: '/invoice-info',
        name: 'invoice-info',
        title: '发票详情',
        component: () => import('@/views/invoice-info.vue'),
    }
];

export const routers = [
    ...appRouter,
];
