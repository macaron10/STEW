import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
import axios from "axios";
import { isNull } from 'util';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    userInfo: null,
    isLogin: false,
  },

  mutations: {
    loginSuccess(state, payload) {
      state.isLogin = true;
      state.userInfo = payload;
    },

    logoutSuccess(state) {
      state.isLogin = false;
      state.userInfo = null;
    }
  },  

  actions: {
    // access-tocken 유효한지 검사하는 부분 만들어야됨

    // 로그인
    signIn({ commit }, userObj ) {
      axios.post('http://localhost:8399/api/user/signin', userObj)
        .then(res => {
          // console.log(res);
          console.log("로그인함;");
          const userInfo = {
            'authorization': res.headers.authorization
          }
          commit("loginSuccess", userInfo);
        })
        .catch(err => {
          alert("이메일과 비밀번호를 확인하세요");
          console.log(err)
        })
    },
    logout({commit}) {
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
      console.log("로그아웃됐니?");
      commit("logoutSuccess");
      router.push({name: "Home"}) // @click="$store.dispatch('logout')"" ($store.commit('logout')해도 되지만 우리는 logout커밋 후 이동을 해야되서)
    }
  },

  modules: {}
});
