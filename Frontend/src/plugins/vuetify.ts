import Vue from "vue";
import Vuetify from "vuetify/lib";
// const Vuetify = require("vuetify/lib");
Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    options: {
      customProperties: true
    }
  }
});
