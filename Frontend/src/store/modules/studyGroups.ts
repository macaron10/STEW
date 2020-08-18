import axios from "axios";

export default {
  namespaced: true,
  state: {
    // 스터디 그룹
    groups: [],
    keyword: '', // 검색어
    searchedGroups: [],
    onMeeting: true,

  },

  mutations: {
    setSearchGroups(state: any, groups: any) {
      state.searchedGroups = groups
    },
    setGroups(state: any, groups: any) {
      state.groups = groups
    },
    setKeyWord(state: any, keyWord: any) {
      state.keyword = keyWord
    }
  },

  actions: {
    // 그룹들 불러오기
    async getGroups({ state }: any, cateNo: number) {
      const apiUrl = '/study/?cateNo='+ cateNo
      try {
        const res = await axios.get(apiUrl)
        state.groups = res.data.object
        const listLength = state.groups.length
      } catch (err) {
        console.error(err)
      }
    },

    async getNextGroups({ state }: any, payload: any){
      const apiUrl = '/study/?cateNo=' + payload[1] + '&page='+ payload[0]
      try {
        const res = await axios.get(apiUrl)
        state.groups.push(...res.data.object)
        const listLength = state.groups.length
      } catch (err) {
        console.error(err)      
      }
    },  

    fetchGroups({ commit, state }: any) {
      const apiUrl = '/study/search'
      const config = {
        params: {
          "keyword": state.keyword,
          // "gpCatNo": state.keyWord
        }
      }
      axios.get(apiUrl, config)
        .then(res => {
          commit('setSearchGroups', res.data.object)
        })
        .catch(err => console.error(err))
    },

    async joinedGroup({ commit }: any, payload: any) {
      // console.log(payload.gpNo);
      return await axios.get('/study/user/joinck/'+ payload.gpNo)
      .then(res => {
        // console.log(res.data.object);
        return res;
      })
    },
  }
}