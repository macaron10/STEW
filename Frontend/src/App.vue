<template>
  <v-app id="inspire">
    <Navbar @drawer-onoff="turnOnDrawer"/>
    <Sidebar :drawer="drawer" />
    <v-main>
        <Home />
        <About />
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";

import Navbar from "./components/Navbar.vue";
import Sidebar from "./components/Sidebar.vue";

import Home from "./views/Home.vue"
import About from "./views/About.vue"

export default Vue.extend({
  name: "App",
  props: {
    source: String,
  },
  components: {
    Navbar, Sidebar, Home, About
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
