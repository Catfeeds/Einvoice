## nodejs环境搭建，npm包管理工具安装

##安装微信sdk
npm install weixin-js-sdk --save

## 工程初始化
npm install

## 开发运行
npm run dev

## 发布打包
### 打包完成后根目录dist目录中为全部资源文件
npm run build

## UI库
使用Vant参考文档：https://www.youzanyun.com/zanui/vant#/zh-CN/intro

## JS库
使用vue2.0参考文档：https://cn.vuejs.org/v2/guide/syntax.html#%E5%8E%9F%E5%A7%8B-HTML

## VUE路由库
https://router.vuejs.org/zh-cn/api/router-link.html

## 文件结构
```
├── build  脚手架项目构建配置
└── src
    ├── images  图片文件
    ├── libs  工具方法
    ├── router  路由配置
    ├── store  状态管理
    ├── styles  样式文件
    ├── template  模板文件
    ├── vendors  公共库文件
    └── views 界面文件(每个新增界面在views下新建一个独立文件夹隔离)
```

## 路由添加与配置
在src/router/index.js中配置全局路由钩子事件，处理下权限问题（可选）
在src/router/router.js中配置界面相关路由地址

## 网络请求配置与使用
开发模式，在build/webpack.dev.config.js中配置跨域代理转发

在src/libs/httpserver/index.js中配置全局返回错误代码处理等相关请求配置
vue实例对象中已经添加$http方法，可在vue文件中直接使用
this.$http({
    url:"" //请求地址
    method: "post", //请求类型
    data: {} //请求参数对象
}).then(res => {
    <!-- then中可直接只用this -->
    if(res){
        console.log("returnData:", res); //res返回结果
    }
});

## 打包工程服务器nginx部署配置
### 打包完成后将dist中文件拷贝到服务器xxxxx目录
```
server
{
    listen       80;
    server_name  www.xxx.com;
    index index.html index.htm index.php;
    root  /opt/nginx/html/xxxxx;

    ## 跨域转发配置，对应开发模式build/webpack.dev.config.js中配置跨域代理
    location /api {
        proxy_pass http://s.pmlabs.cn/;
    }

location /fapiao/* {
        proxy_pass http://172.17.8.44:8081/e-invoice-pro/rest;
    }
    
}
```
