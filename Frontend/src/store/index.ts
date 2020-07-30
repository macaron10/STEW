import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
import axios from "axios";
import createPersistedState from 'vuex-persistedstate';
import jwt from 'jsonwebtoken';

import { isNull } from 'util';
import { userInfo } from 'os';

Vue.use(Vuex);

interface UserInfo {
  accessToken: string,
  refreshToken: string,
}

export default new Vuex.Store({
  state: {
    drawer: false,
    isLogin: false,
    userInfo: {
      accessToken: "",
      refreshToken: ""
    },
  },

  mutations: {
    drawerOnOff(state) {
      if (state.drawer) {
        state.drawer = false
      } else {
        state.drawer = true
      }
    },

    loginSuccess(state, payload) {
      state.isLogin = true;
      state.userInfo = payload;
    },

    logoutSuccess(state) {
      state.isLogin = false;
      state.userInfo.accessToken = "";
      state.userInfo.refreshToken = "";
    }
  },  

  actions: {
    // 로그인
    signIn({ commit }, userObj ) {
      axios.post('/user/signin', userObj)
        .then(res => {
          console.log(res);
          console.log("로그인 됐습니당");
          const userInfo = {
            'accessToken': res.headers.accesstoken,
            'refreshToken': res.headers.refreshtoken
          }
          commit("loginSuccess", userInfo);
          axios.defaults.headers.common['Authorization'] = this.state.userInfo.accessToken;
        })
        .catch(err => {
          alert("이메일과 비밀번호를 확인하세요");
          console.log(err)
        })
    },

    // 로그아웃
    logout({commit}) {
      axios.get('/user/logout')
      .then(res => {
        console.log(res);
        console.log("로그아웃합니당");
        commit("logoutSuccess");
        router.push({name: "Home"}) 
      })
    },

    // accessToken 확인
    validateAccessToken() {
      // let decode = jwt.decode(this.state.userInfo.accessToken);

      console.log("해야돼요...");
      console.log(jwt.decode(this.state.userInfo.accessToken));
      // 쿠키에서 가져온 토큰을 복호화 한 후 exptime 비교 (ms)
    }
  },

  modules: {},

  plugins: [
    createPersistedState(),
  ]
});
