import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
import axios, {AxiosRequestConfig, AxiosPromise, AxiosResponse} from "axios";
import { userInfo } from 'os';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    userInfo: null,
    allUsers: [],
    isLogin: false,
    isLoginError: false
  },
  mutations: {
    loginSuccess(state) {
      state.isLogin = true;
      state.isLoginError = false;
    },
    loginError(state) {
      state.isLogin = false;
      state.isLoginError = true;
    },
    logout(state) {
      state.isLogin = false;
      state.isLoginError = false;
      state.userInfo = null;
    }
  },  
  actions: {
    // login({ state, commit }, loginObj) {
    //   let selectedUser = null;
    //   state.allUsers.forEach(user => {
    //      if(user.email === loginObj.email) selectedUser = user;
    //   })
    //   selectedU~~~
    // },

    // 로그인
    signIn({ commit }, userObj ) {
      axios.post('http://localhost:8399/api/user/signin', userObj)
        .then(res => {
          console.log(res);
        })
        .catch(err => {
          console.log(err)
        })
    },
    logout({commit}) {
      commit('logout')
      router.push({name: "home"}) // @click="$store.dispatch('logout')"" ($store.commit('logout')해도 되지만 우리는 logout커밋 후 이동을 해야되서)
    }
  },
  modules: {}
});
