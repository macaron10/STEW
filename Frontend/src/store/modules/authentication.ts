import router from "@/router";
import axios from 'axios';
import jwt from "jsonwebtoken";

interface UserInfo {
  userId: number,
  accessToken: string,
  refreshToken: string,
}

// 유저 인증 정보 모듈화
export default {
    namespaced: true,
    
    state: {
      isLogin: false,
      userInfo: {
        userId: 0,
        accessToken: "",
        refreshToken: ""
      },
    },

    getters: {
      loginStatus: (state: { isLogin: any; }) => state.isLogin,
      getUserInfo: (state: { userInfo: UserInfo; }) => state.userInfo
    },
  
    mutations: {
      loginSuccess(state: any, payload: any) {
        state.isLogin = true;
        state.userInfo = payload;
      },
  
      logoutSuccess(state: any) {
        state.isLogin = false;
        state.userInfo.accessToken = "";
        state.userInfo.refreshToken = "";
      },
  
      refreshSuccess(state: any, payload: any) {
        state.userInfo.accessToken = payload;
      },

      //임시(userId 불러오기용)
      changeUserId(state: { userInfo: { userId: number; }; }, payload: any) {
        state.userInfo.userId = payload
      }
    },
  
    actions: {
      // 로그인
      signIn({ commit, dispatch }: any, userObj: any) {
        axios.post('/user/signin', userObj)
          .then(res => {
            // console.log(res);
            const userInfo = {
              'accessToken': res.headers.accesstoken,
              'refreshToken': res.headers.refreshtoken
            }
            commit("loginSuccess", userInfo);
            dispatch("notice/getReqsSock",null, { root: true });
            dispatch("notice/getReqs",null, { root: true });
            //임시(userId 불러오기용)
            dispatch("getUserInfoImsi");
          })
          .catch(err => {
            alert("이메일과 비밀번호를 확인하세요");
            // console.log(err)
          })
      },
  
      // 로그아웃
      logout({ commit }: any) {
        axios.get('/user/logout')
          .then(res => {
            // console.log(res);
            commit("logoutSuccess");
            router.push({ name: "Home" })
          })
      },
  
      // 토큰 갱신
      tokenRefresh({ state, commit }: any) {
        return new Promise(resolve => {
          const config = {
            headers: {
              "refreshToken": state.auth.userInfo.refreshToken
            }
          }
          const origin = state.auth.userInfo.accessToken;
  
          axios.get('/user/refresh', config)
            .then(res => {
              console.log("토큰 재발급 요청 응답");
              commit("refreshSuccess", res.headers.accesstoken);
              console.log("origin : " + origin);
              console.log("new : " + state.auth.userInfo.accessToken);
              if (origin !== state.auth.userInfo.accessToken) {
                resolve();
              }
            })
        })
      },
  
      // accessToken 정보 확인
      tokenInformation({ state }: any) {
        const token = state.auth.userInfo.accessToken.replace("Bearer", "");
        const decode = jwt.decode(token);
        console.log(decode);
      },

      //임시(userId불러오기용)
      getUserInfoImsi({ commit }: any) {
        axios.get('/user/')
        .then(({ data }) => {
            commit('changeUserId', data.object.userId)
            console.log(data.object.userId)
        })
      },
    },
  }