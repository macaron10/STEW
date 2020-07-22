<template>
  <v-app id="inspire">
    <Navbar @drawer-onoff="turnOnDrawer"/>
    <Sidebar :drawer="drawer" />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";

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
  }),
  methods: {
    turnOnDrawer() {
      this.drawer = !this.drawer
    },
  },
});
</script>
