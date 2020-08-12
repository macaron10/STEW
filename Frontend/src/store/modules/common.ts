export default {
    state: {
        baseUrl: "http://localhost:8399/api",//개발용
        // baseUrl: "https://i3b103.p.ssafy.io/image", //배포용
        drawer: false,
        onMeeting: true,
      },

      getters: {
        getBaseUrl: (state: { baseUrl: string }) => state.baseUrl,
        getDrawer: (state: { drawer: boolean }) => state.drawer,
      },

      mutations: {
        drawerOnOff(state: any) {
          if (state.drawer) {
            state.drawer = false
          } else {
            state.drawer = true
          }
        },
      },
}