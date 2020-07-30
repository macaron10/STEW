import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    baseUrl: 'http://localhost:8399/api',
    drawer: false
  },
  mutations: {
    drawerOnOff(state) {
      if (state.drawer) {
        state.drawer = false
      } else {
        state.drawer = true
      }
    }
  },
  actions: {},
  modules: {}
});
