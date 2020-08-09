import Vue from "vue";
import Vuex, { Store } from "vuex";
import router from "../router";
import axios from "axios";
import querystring from 'querystring';
import createPersistedState from "vuex-persistedstate";
import jwt from "jsonwebtoken";

import sg from "@/store/modules/studyGroups";
import notice from "@/store/modules/notification";
import auth from "@/store/modules/authentication";
import basic from "@/store/modules/basic";

Vue.use(Vuex);

export default new Vuex.Store({

  modules: {
    basic: basic,
    auth: auth,
    sg: sg,
    notice: notice,
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

console.log(store.state.auth.userInfo);
