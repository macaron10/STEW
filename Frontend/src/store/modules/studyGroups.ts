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