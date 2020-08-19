import router from "@/router";
import axios from 'axios';
import jwt from "jsonwebtoken";

interface UserInfo {
  userId: number,
  userNm: string,
  userEmail: string,
  userImg: string,
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
        userNm: "",
        userEmail: "",
        userImg: "",
        accessToken: "",
        refreshToken: "",
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
        state.userInfo.userId = 0;
        state.userInfo.accessToken = "";
        state.userInfo.refreshToken = "";
      },
  
      refreshSuccess(state: any, payload: any) {
        state.userInfo.accessToken = payload;
      },

      getUserInfoByToken(state: { userInfo: any; }, payload: any) {
       state.userInfo.userId = payload.userId; 
       state.userInfo.userNm = payload.userNm;
       state.userInfo.userEmail = payload.userEmail;
       state.userInfo.userImg = payload.userImg
      }
    },
  
    actions: {
      // 로그인
      async signIn({ commit, dispatch }: any, userObj: any) {
        await axios.post('/user/signin', userObj)
          .then(res => {
            const userInfo = {
              'accessToken': res.headers.accesstoken,
              'refreshToken': res.headers.refreshtoken
            }
            //임시(userId 불러오기용)
            commit("loginSuccess", userInfo);
            dispatch("tokenInformation");
            // dispatch("notice/getReqsSock", null, { root: true });
            // dispatch("notice/getReqs", null, { root: true });
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
            router.push("/").catch(()=>({}));
          })
      },
  
      // 토큰 갱신
      tokenRefresh({ state, commit }: any) {
        return new Promise(resolve => {
          const config = {
            headers: {
              "refreshToken": state.userInfo.refreshToken
            }
          }
          const origin = state.userInfo.accessToken;
  
          axios.get('/user/refresh', config)
            .then(res => {
              // console.log("토큰 재발급 응답");
              commit("refreshSuccess", res.headers.accesstoken);
              //console.log(origin);
              //console.log(state.userInfo.accessToken);
              if (origin !== state.userInfo.accessToken) {
                resolve();
              }
            })
        })
      },
  
      // accessToken 정보 확인
      tokenInformation({ state, commit }: any) {
        const token = state.userInfo.accessToken.replace("Bearer ", "");
        const decode: any = jwt.decode(token);
        if (decode) {
          const userInfo = {
            'userId': decode.userId,
            'userEmail': decode.sub,
            'userNm': decode.userNm,
            'userImg': decode.userImg
          };
          commit("getUserInfoByToken", userInfo);
        }
      },
    },
  }