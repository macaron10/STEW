import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

import studyGroups from "@/store/modules/studyGroups";
import notification from "@/store/modules/notification";
import authentication from "@/store/modules/authentication";
import basic from "@/store/modules/basic";

Vue.use(Vuex);

export default new Vuex.Store({
  
  modules: {
    basic: basic,
    auth: authentication,
    sg: studyGroups,
    notice: notification,
  },

  plugins: [
    createPersistedState({
      paths: [
        'basic',
        'auth'
      ]
    }),
  ]
});

