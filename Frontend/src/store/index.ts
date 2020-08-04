import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
import axios from "axios";
import createPersistedState from "vuex-persistedstate";
import jwt from "jsonwebtoken";

import { isNull } from 'util';
import { userInfo } from 'os';

Vue.use(Vuex);

interface UserInfo {
  accessToken: string,
  refreshToken: string,
}

//모듈화
const studyGroups = {
  namespaced: true,
  state: {
    // 스터디 그룹
    groups: [],
    //미구현
    keyword: '', // 검색어
    searchedGroup: []
  },

  mutations: {
    setGroups (state: any, groups: any) {
      state.groups = groups
    }
  },  

  actions: {
    // 그룹들 불러오기
    async getGroups ({ state }:any, event:any) {
      // const baseUrl = this.$store.state.baseUrl
      const apiUrl = '/study/all'
      try {
        const res = await axios.get(apiUrl)
        // 개별요소 수정할때
        // for (const i of res.data) {
        //   i.created_at = String(i.created_at).substring(0, 10)
        //   i.half_rate = i.vote_average / 2
        //   if (i.popularity !== 0) {
        //     i.poster_path = this.imageURL + i.poster_path
        //     i.backdrop_path = this.imageURL + i.backdrop_path
        //   }
        // }
        state.groups = res.data.object
        const listLength = state.groups.length
        // const listSize = this.pageSize
        // const page = Math.floor((listLength - 1) / listSize) + 1
        // this.movieSize = page
        console.log(state.groups)
      } catch (err) {
        console.error(err)
      // } finally {
      //   this.sortBy(this.sortedBy)
      //   this.showPagination = true
      }
    },
  },
}

export default new Vuex.Store({
  state: {
    baseUrl: "http://localhost:8399/api",
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
    },

    refreshSuccess(state, payload) {
      state.userInfo.accessToken = payload;
    },
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
          // axios.defaults.headers.common['Authorization'] = this.state.userInfo.accessToken;
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

    // 토큰 갱신
    tokenRefresh({commit}) {
      const config = {
        headers: {
          "refreshToken" : this.state.userInfo.refreshToken
        }
      }

      axios.get('/user/refresh', config)
      .then(res => {
        console.log("토큰 재발급 요청 응답");
        console.log(res);
        // commit("refreshSuccess",)
      })
    },

    // accessToken 정보 확인
    tokenInformation() {
      const token = this.state.userInfo.accessToken.replace("Bearer", "");
      const decode = jwt.decode(token);
      console.log(decode);
    },
  },


  modules: {
    sg: studyGroups
  },

  plugins: [
    createPersistedState(),
  ]
});
