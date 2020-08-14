<template>
  <v-app id="inspire">
    <Navbar @drawer-onoff="turnOnDrawer"/>
    <Sidebar :drawer="drawer" :key='componentKey' />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import store from "./store";

import Navbar from "./components/Navbar.vue";
import Sidebar from "./components/Sidebar.vue";

export default Vue.extend({
  name: "App",
  props: {
    source: String,
  },
  components: {
    Navbar, Sidebar,
  },
  data: () => ({
    dialog: false,
    drawer: false,
    componentKey: 0
  }),
  methods: {
    forceRerender() {
      this.componentKey += 1;
    },
    turnOnDrawer() {
      this.drawer = !this.drawer
    },
  },
});
</script>
