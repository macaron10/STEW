import Vue from "vue";
import App from "./App.vue";
import router from "@/router";
import store from "@/store";
import vuetify from "./plugins/vuetify";
import "roboto-fontface/css/roboto/roboto-fontface.css";
import "@mdi/font/css/materialdesignicons.css";
import axios from "axios";
import jwt from "jsonwebtoken";

Vue.config.productionTip = false;
// Vue.prototype.$http = axios

// axios.defaults.baseURL = "http://localhost:8399/api" // 개발용
axios.defaults.baseURL = "https://i3b103.p.ssafy.io/api" // 배포용

axios.interceptors.request.use(config => {
  const token = store.getters['auth/getUserInfo'].accessToken;
  
  if (token != "") {
    const tokenInfo: any = jwt.decode(store.getters['auth/getUserInfo'].accessToken.replace("Bearer ", ""));
    config.headers.Authorization = token;
  }
  return config;
})

axios.interceptors.response.use(
  function (res) {
    console.log("res 응답");
    console.log(res);
    return res;
  },

  function (err) {
    const originalRequest = err.config;
    console.log("err 응답");
    console.log(err);
    
    if (err.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      const refreshInfo: any = jwt.decode(store.getters['auth/getUserInfo'].refreshToken.replace("Bearer ", ""));
      
      if (Date.now() - refreshInfo.exp * 1000 < 0) {
        console.log("토큰 재발급 고고");
        
        store.dispatch('auth/tokenRefresh').then(() => {
          console.log("토큰 재발급 완료");
          
          originalRequest.headers.Authorization = store.getters['auth/getUserInfo'].accessToken;
          console.log(originalRequest);
          return Promise.resolve(originalRequest);
        });
      } else {
        console.log("로그아웃 해야됨");
        
        store.commit("auth/logoutSuccess");
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
