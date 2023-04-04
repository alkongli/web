// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import axios from 'axios'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueCookies from 'vue-cookies'
import store from './store'
import VueCoreVideoPlayer from 'vue-core-video-player'


Vue.use(VueCookies)
Vue.use(VueCoreVideoPlayer, {
  lang: 'zh-CN'
})

axios.defaults.withCredentials=true
// axios.defaults.baseURL= "http://localhost:8081"
axios.interceptors.request.use(config=>{
  config.headers.Authorization=window.sessionStorage.getItem('token')
  config.headers.EAuthorization=window.sessionStorage.getItem('ElementToken')
  return config
})
axios.interceptors.response.use(res=>{
  if(res.data.msg==="请先登录"){
    if(router.currentRoute.path!=="/user/login")
      router.push('/user/login')
  }
  if(res.data.msg==="请先登录后台账号"){
    if(router.currentRoute.path!=="/employee/login")
      router.push('/employee/login')
  }
  return res
})

Vue.config.productionTip = false
Vue.prototype.$axios = axios
Vue.use(ElementUI);
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
