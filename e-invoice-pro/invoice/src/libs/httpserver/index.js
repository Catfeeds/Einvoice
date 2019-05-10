import axios from "axios";
// import qs from "qs";
// import Cookies from 'js-cookie';
const Axios = axios.create({
  baseURL: "/e-invoice-pro/rest/",
  timeout: 30000,
  // responseType: "json",
  withCredentials: true, // 是否允许带cookie这些
  headers: {
    "Content-Type": "application/json; charset=UTF-8"
  },
  // 请求URL中统一添加公共get参数数据
  params: {
    // jsessionid: Cookies.get("jsessionid"),
    // userid: Cookies.get("userid")
  }
});

//请求拦截器设置
// Axios.interceptors.request.use(
//     config => {
//         console.log('Axios.interceptors.request.use-config:', config);
//         return config;
//     },
//     error => {
//         console.log('Axios.interceptors.request.use-error:', error);
//         return Promise.reject(error);
//     }
// );

//响应拦截器-主要提前处理返回结果错误
Axios.interceptors.response.use(
  res => {
    // console.log('Axios.interceptors.response.use-res:', res);
    // 判断API接口状态约定, 错误处理配置
    // if (res.data && res.data.returncode && res.data.returncode != 0) {
    //     Modal.error({
    //         title: '错误提示',
    //         content: res.data.data
    //     });
    //     return Promise.reject(res.data);
    // }
    return res.data;
  },
  error => {
    console.log('Axios.interceptors.response.use-error:', error);
    return error;
  }
);

function get(url, config) {
  return Axios.get(url, config);
}

function post(url, params) {
  return Axios.post(url, params);
}

function put(url, params) {
  return Axios.put(url, params);
}

function del(url, config) {
  return Axios.delete(url, config);
}
export default {
  /**开发拟获取微信权限 */
  getProductionPermission(data) {
    return get('wx/wxopen', {
      params: data
    })
  },
  getWxConfig(data) {
    return get('wx/getWeixConfig', {
      params: data
    })
  },
  /**校验获取发票详情 */
  getInvoiceBillInfoWithDetail(data){
    return post('api/getInvoiceBillInfoWithDetail',data)
  },
  /**提交发票抬头联系方式等信息 */
  getInvoicePreview(data){
    return post('api/getInvoicePreview',data)
  },
  askInvoice(data){
    return post('api/askInvoice',data)
  },
  getInvque(data){
    return post('api/getInvque',data)
  },
  sendPdf(data){
    return post('api/sendPdf',data)
  },
  getWeixTicket(data){
    return post('wx/getWeixTicket?weburl='+ window.location.href,data)
  },
  
}
// const fetch = (options) => {
//   const {
//     method = 'get', data, url,
//   } = options
//   switch (method.toLowerCase()) {
//     case 'get':
//       return Axios.get(`${url}${options.data ? `?${qs.stringify(options.data)}` : ''}`)
//     case 'delete':
//       return Axios.delete(url, {
//         data
//       })
//     case 'head':
//       return Axios.head(url, data)
//     case 'post':
//       return Axios.post(url, data)
//     case 'put':
//       return Axios.put(url, data)
//     case 'patch':
//       return Axios.patch(url, data)
//     default:
//       return Axios(options)
//   }
// }

// export default function request(options) {
//   return fetch(options).catch((error) => {
//     console.log("error:", error);
//   });
// }
