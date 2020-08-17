import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

import studyGroups from "@/store/modules/studyGroups";
import notification from "@/store/modules/notification";
import authentication from "@/store/modules/authentication";
import common from "@/store/modules/common";

Vue.use(Vuex);

export default new Vuex.Store({
  
  modules: {
    comm: common,
    auth: authentication,
    sg: studyGroups,
    notice: notification,
  },

  plugins: [
    createPersistedState({
      paths: [
        'comm',
        'auth',
      ]
    }),
  ]
});

