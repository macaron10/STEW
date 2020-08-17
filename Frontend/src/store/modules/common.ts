import { getBaseUrl } from '@/constants'

export default {
    state: {
        baseUrl: getBaseUrl('img'),
        baseSocketUrl: getBaseUrl('api'),
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