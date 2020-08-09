import Vue from "vue";
import Vuex from "vuex";
import router from "../router";
import axios from "axios";
import querystring from 'querystring';
import createPersistedState from "vuex-persistedstate";
import jwt from "jsonwebtoken";

// 소켓
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';

import { isNull } from 'util';
import { userInfo } from 'os';
import { config } from 'process';

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
    keyword: '', // 검색어
    searchedGroups: [],
  },

  mutations: {
    setGroups(state: any, groups: any) {
      state.groups = groups
    },
    setKeyWord(state: any, keyWord: any) {
      state.keyWord = keyWord
    }
  },

  actions: {
    // 그룹들 불러오기
    async getGroups({ state }: any, event: any) {
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
      } catch (err) {
        console.error(err)
        // } finally {
        //   this.sortBy(this.sortedBy)
        //   this.showPagination = true
      }
    },
  fetchGroups({ commit, state }: any, event: any) {
    commit('setKeyWord', event.target.value)
    const apiUrl = '/study/search'
    const config = {
      params: {
        "gpNm": state.keyWord,
        // "gpCatNo": state.keyWord
      }
    }
    axios.get(apiUrl, config)
    .then(res => {
      commit('setGroups', res.data.object)
      })
      .catch(err => console.error(err))
  },
  }
}

const notifications = {
  namespaced: true,
  state: {
    groupsReqs: []
  },
  mutations: {
    setReqs(state: any, groupsReqs: any) {
      state.groupsReqs = groupsReqs
    }
  },
  actions: {
    async getReqs({ state }: any, event: any) {
      const apiUrl = '/study/user/reqlist'
      try {
        const res = await axios.get(apiUrl)
        state.groupsReqs = res.data.object
        console.log(res)
      } catch (err) {
        console.error(err)
      }
    },
    getReqsSock({ state, rootState }: any, event: any) {
      const apiUrl = rootState.baseUrl + '/sock'
      const socket = new SockJS(apiUrl)
      const ws = Stomp.over(socket)

      const token = {
        'accessToken': rootState.userInfo.accessToken
      }
      ws.connect(token,
        frame => {
          console.log('소켓 연결 성공');

          ws.subscribe("/sub/mgr-req/" + rootState.userInfo.userId, msg =>{
            state.groupsReqs.push(JSON.parse(msg.body))
            console.log(JSON.parse(msg.body))
          })

          ws.subscribe("/sub/user-req/" + rootState.userInfo.userId, msg =>{
            state.groupsReqs.push(JSON.parse(msg.body))
            console.log(JSON.parse(msg.body))
          })
        })
    }
  },
}

export default new Vuex.Store({
  state: {
    baseUrl: "http://localhost:8399/api",//개발용
    // baseUrl: "https://i3b103.p.ssafy.io", //배포용
    drawer: false,
    isLogin: false,
    userInfo: {
      accessToken: "",
      refreshToken: "",
    //임시 (유저id 불러오기용(유저정보))
      userId: ""
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

    //임시(userId 불러오기용)
    changeUserId(state, payload) {
      state.userInfo.userId = payload
    }
  },

  actions: {
    // 로그인
    signIn({ commit, dispatch }, userObj) {
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
          dispatch("notice/getReqsSock")
          dispatch("notice/getReqs")
          //임시(userId 불러오기용)
          dispatch("getUserInfoImsi")

        })
        .catch(err => {
          alert("이메일과 비밀번호를 확인하세요");
          console.log(err)
        })
    },

    // 로그아웃
    logout({ commit }) {
      axios.get('/user/logout')
        .then(res => {
          console.log(res);
          console.log("로그아웃합니당");
          commit("logoutSuccess");
          router.push({ name: "Home" })
        })
    },

    // 토큰 갱신
    tokenRefresh({ commit }) {
      return new Promise(resolve => {
        const config = {
          headers: {
            "refreshToken": this.state.userInfo.refreshToken
          }
        }
        const origin = this.state.userInfo.accessToken;

        axios.get('/user/refresh', config)
          .then(res => {
            console.log("토큰 재발급 요청 응답");
            commit("refreshSuccess", res.headers.accesstoken);
            console.log("origin : " + origin);
            console.log("new : " + this.state.userInfo.accessToken);
            if (origin !== this.state.userInfo.accessToken) {
              resolve();
            }
          })
      })
    },

    // accessToken 정보 확인
    tokenInformation() {
      const token = this.state.userInfo.accessToken.replace("Bearer", "");
      const decode = jwt.decode(token);
      console.log(decode);
    },

    //임시(userId불러오기용)
    getUserInfoImsi({ commit }) {
      axios.get('/user/')
      .then(({ data }) => {
          commit('changeUserId', data.object.userId)
          console.log(data.object.userId)
      })
      
  },
  },


  modules: {
    sg: studyGroups,
    notice: notifications
  },

  plugins: [
    createPersistedState(),
  ]
});
