import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
import axios from "axios";
import createPersistedState from 'vuex-persistedstate';

import { isNull } from 'util';
import { userInfo } from 'os';

Vue.use(Vuex);

interface UserInfo {
  refreshToken: string,
  userId: number,
}

export default new Vuex.Store({
  state: {
    baseUrl: 'http://localhost:8399/api',
    drawer: false,
    userInfo: {
      refreshToken: "",
      userId: 0
    },
    isLogin: false,
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
      state.userInfo.refreshToken = payload;
    },

    logoutSuccess(state) {
      state.isLogin = false;
      state.userInfo.refreshToken = "";
    }
  },  

  actions: {
    // 로그인 (refreshToken만 저장, accessToken은 쿠키에서 확인)
    signIn({ commit }, userObj ) {
      axios.post('http://localhost:8399/api/user/signin', userObj)
        .then(res => {
          // console.log(res);
          console.log("로그인 됐습니당");
          const userInfo = {
            'refreshToken': res.headers.authorization
          }
          commit("loginSuccess", userInfo);
        })
        .catch(err => {
          alert("이메일과 비밀번호를 확인하세요");
          console.log(err)
        })
    },

    // 로그아웃
    logout({commit}) {
      // 쿠키에서 엑세스 토큰 지우고, 내꺼에서 리프레쉬 토큰 지우기

      // let token = isNull(this.state.userInfo)? "" : this.state.userInfo.authorization;
      
      // let token = this.state.userInfo.authorization;
      // let config = {
      //   headers: {
          // "authorization": token.authorization
        // }
      // }
      // axios.post('')
      //   .then()
      // commit('logout')
      console.log("로그아웃합니당");
      commit("logoutSuccess");
      router.push({name: "Home"}) // @click="$store.dispatch('logout')"" ($store.commit('logout')해도 되지만 우리는 logout커밋 후 이동을 해야되서)
    },

    // accessToken 확인
    validateAccessToken() {
      console.log("해야돼요...");
      // 쿠키에서 가져온 토큰을 복호화 한 후 exptime 비교 (ms)
    }
  },

  modules: {},

  plugins: [
    createPersistedState(),
  ]
});
