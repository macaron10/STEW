import axios from "axios";

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
    },
  }

  export default notifications;