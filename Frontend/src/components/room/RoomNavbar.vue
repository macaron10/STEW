<template>
  <v-app-bar
    v-if="!$store.state.comm.onMeeting"
    :clipped-left="$vuetify.breakpoint.lgAndUp"
    app
    color="grey"
    dark
    fixed
  >
    <v-app-bar-nav-icon @click.stop="$store.commit('drawerOnOff')"></v-app-bar-nav-icon>
    <v-toolbar-title
      style="width: 300px"
      class=""
    >
      <v-btn
      icon
      large
      :to="{ name: 'Main' }"
      
    >
      <v-icon>mdi-home</v-icon>
    </v-btn>
    </v-toolbar-title>
  </v-app-bar>
</template>

<script>
import axios from 'axios';
import { mapState, mapActions, mapMutations} from 'vuex';

export default {
    name: 'RoomNavbar',
    computed: {
      ...mapState('auth', [ 
        "userInfo",
        "isLogin"
      ]),
    },
    methods: {
      ...mapActions('auth', [
        "signIn",
        "logout"
        ]),
    },
    data () {
      return {
        wordForSearching: "",
        signinInDialog: false,
        user: {
          userEmail: "",
          userPw: "",
        },
        model: 1,
        show: false,
      }
    },
  beforeDestroy() {
    this.$store.state.comm.onMeeting = true;
  }
}
</script>

<style>

</style>