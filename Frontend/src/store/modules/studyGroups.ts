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
    async getGroups({ state }: any) {
      // const baseUrl = this.$store.state.baseUrl
      const apiUrl = '/study/'
      try {
        const res = await axios.get(apiUrl)
        state.groups = res.data.object
        const listLength = state.groups.length
        // const listSize = this.listSize
        // const page = Math.floor((listLength - 1) / listSize) + 1
        // this.listSize = page
      } catch (err) {
        console.error(err)
        // } finally {
        //   this.sortBy(this.sortedBy)
        //   this.showPagination = true
      }
    },
    async getNextGroups({ state }: any, pageNumber: number){
      const apiUrl = '/study/?page='+ pageNumber
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
  }
}