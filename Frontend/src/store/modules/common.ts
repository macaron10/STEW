export default {
    state: {
        baseUrl: "http://localhost:8399/api",//개발용
        // baseUrl: "https://i3b103.p.ssafy.io", //배포용
        baseSocketUrl: "http://localhost:8399/api", //개발용
        // baseSocketUrl: "https://i3b103.p.ssafy.io/api" //배포용
        drawer: false,
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