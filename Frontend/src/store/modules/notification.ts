import axios from "axios";
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';

export default {
    namespaced: true,
    state: {
      groupsReqs: [],
      myGroupsReqs: []
    },
    mutations: {
      setReqs({state, rootState}: any, groupsReqs: any) {
        state.groupsReqs = groupsReqs
      }
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
      async getMyReqs({state, rootState}: any, event: any){
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
      getReqsSock({ state, rootState }: any, event: any) {
        const apiUrl = rootState.comm.baseSocketUrl + '/sock'
        const socket = new SockJS(apiUrl)
        const ws = Stomp.over(socket)
        ws.debug = () => {return }
  
        const token = {
          'accessToken': rootState.auth.userInfo.accessToken
        }
        ws.connect(token,
          (frame: any) => {
  
            ws.subscribe("/sub/mgr-req/" + rootState.auth.userInfo.userId, (msg: { body: string; }) =>{
              state.groupsReqs.push(JSON.parse(msg.body))
            })

            ws.subscribe("/sub/user-req/" + rootState.auth.userInfo.userId, (msg: any) =>{
              state.groupsReqs.push(JSON.parse(JSON.parse(msg.body).req))
            })
          })
      }
    },
  }