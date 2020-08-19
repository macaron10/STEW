import axios from "axios";
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';

export default {
  namespaced: true,
  state: {
    groupsReqs: [],
    myGroupsReqs: [],
    notis: []
  },
  mutations: {
    setReqs({ state, rootState }: any, groupsReqs: any) {
      state.groupsReqs = groupsReqs
    },
    delReq(state: any, reqNo: any) {
      state.groupsReqs.forEach((elem :any , index: any) => {
        if (elem.gpReqNo == reqNo) {
          state.groupsReqs.splice(index, 1);
        }
      });
    },
    delNoti(state: any, notiNo: any) {
      state.notis.forEach((elem :any , index: any) => {
        if (elem.notiNo == notiNo) {
          state.notis.splice(index, 1);
        }
      });
    },
  },
  actions: {
    async getReqs({ state, rootState }: any, event: any) {
      if (rootState.auth.isLogin===true){
        const apiUrl = '/study/user/reqlist'
        try {
          const res = await axios.get(apiUrl)
          state.groupsReqs = res.data.object
        } catch (err) {
          console.error(err)
        }
      }
    },
    async getMyReqs({ state, rootState }: any, event: any) {
      if (rootState.auth.isLogin===true) {
        const apiUrl = '/study/user/ureqlist'
        try {
          const res = await axios.get(apiUrl)
          state.myGroupsReqs = res.data.object
        } catch (err) {
          console.error(err)
        }
      }
    },
    async getNotis({ state, rootState }: any, event: any) {
    if (rootState.auth.isLogin===true){
      const apiUrl = '/noti/list'
      try {
        const res = await axios.get(apiUrl)
        state.notis = res.data.object
      } catch (err) {
        console.error(err)
      }
    }
    },
    getReqsSock({ state, rootState }: any, event: any) {
      const apiUrl = rootState.comm.baseSocketUrl + '/sock'
      const socket = new SockJS(apiUrl)
      const ws = Stomp.over(socket)
      ws.debug = () => {return };

      const token = {
        'accessToken': rootState.auth.userInfo.accessToken
      }
      ws.connect(token,
        (frame: any) => {
          ws.subscribe("/sub/req/" + rootState.auth.userInfo.userId, (msg: { body: string; }) => {
            state.groupsReqs.push(JSON.parse(msg.body))
          })
          ws.subscribe("/sub/noti/" + rootState.auth.userInfo.userId, (msg: { body: string; }) => {
            state.notis.push(JSON.parse(msg.body));
          })
        })
    }
  },
}