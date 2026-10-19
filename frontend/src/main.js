import Vue from 'vue'
import App from './App.vue'
import router from './router/index.js'
Vue.use(router);
//导入elementUI组件
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//导入全局样式
import './assets/css/global.css';
Vue.use(ElementUI);
//导入axios
import axios from 'axios';
//设置访问后台服务器地址
axios.defaults.baseURL="http://127.0.0.1:8088/";
//将axios挂载到 vue全局对象中,使用this可以直接访问
//axios请求拦截, 当我们使用axios组件向后端发送请求时,会自动执行该段代码,并向请求中携带token
axios.interceptors.request.use(config =>{
  //为请求头对象，添加Token验证的token字段
  config.headers.token = window.sessionStorage.getItem('token');
  return config;
})
Vue.prototype.$http=axios;
Vue.config.productionTip = false



new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
