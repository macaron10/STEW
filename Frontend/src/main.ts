import Vue from "vue";
import App from "./App.vue";
import router from "@/router";
import store from "@/store";
import vuetify from "./plugins/vuetify";
import "roboto-fontface/css/roboto/roboto-fontface.css";
import "@mdi/font/css/materialdesignicons.css";
import axios from "axios";
import jwt from "jsonwebtoken";
import { getBaseUrl } from '@/constants'
import { promises } from 'dns';

Vue.config.productionTip = false;
// Vue.prototype.$http = axios

axios.defaults.baseURL = getBaseUrl('api')

axios.interceptors.request.use(config => {
  const token = store.getters['auth/getUserInfo'].accessToken;
  if (token != "") {
    const tokenInfo: any = jwt.decode(store.getters['auth/getUserInfo'].accessToken.replace("Bearer ", ""));

    if (tokenInfo.exp && tokenInfo.exp * 1000 - Date.now() < 60000) { // accessToken의 만료시간이 1분(이하) 후 일 때
      return store.dispatch('auth/tokenRefresh')
      .then(() => {
        config.headers.Authorization = store.getters['auth/getUserInfo'].accessToken;

        return config;
      })
      .catch(()=> {
        throw new axios.Cancel('Login Expired');
      })
    } else {
      config.headers.Authorization = token;
    }
  }
  return config;
})

axios.interceptors.response.use(
  function (res) {
    // console.log(res);
    return res;
  },

  function (err) {
    const originReq = err.config;
    // console.log("err response");
    // console.log(err);
    
    if (err.response && err.response.status === 401 && !originReq._retry) {
      originReq._retry = true;
      
      const refreshInfo: any = jwt.decode(store.getters['auth/getUserInfo'].refreshToken.replace("Bearer ", ""));
      
      if (refreshInfo.exp && Date.now() - refreshInfo.exp * 1000 < 0) {
        // console.log("token refresh start");
        
        return store.dispatch('auth/tokenRefresh').then(() => {
          // console.log("token refresh fin");
          
          originReq.headers.Authorization = store.getters['auth/getUserInfo'].accessToken;
          return axios.request(originReq);
          // return Promise.resolve(originReq);
        });
      } else {
        // console.log("can't refresh. have to logout")
        store.commit("auth/logoutSuccess");
        alert("다시 로그인해주세요");
        router.push('/').catch(()=>({}));
        return Promise.reject(err);
      }
    }
  }
)

const v = new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");

(window as any).app = v;
