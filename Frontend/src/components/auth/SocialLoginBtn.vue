<template>
    <v-btn
    fab
    depressed
    class="ma-4"
    @click="SocialSignIn"
    >
      <v-progress-circular
        v-show="loading"
        :indeterminate="true"
        style="position: absolute; z-index: 1000"
        color="light-blue"
      ></v-progress-circular>
      <img
        style="position:absolute; border-radius: 50%"
        :src="logoUrl"
        width="100%"
        />
    </v-btn>
</template>

<script>
import axios from "axios";
import * as Constants from "../../constants";

export default {
  created(){
    const provider = this.$props.provider;

    if(provider == 'Kakao') this.logoUrl = this.$store.state.comm.baseUrl + Constants.KAKAO_LOGO_URL;
    else if(provider == 'Naver') this.logoUrl = this.$store.state.comm.baseUrl + Constants.NAVER_LOGO_URL;
    else if(provider == 'Google') this.logoUrl = this.$store.state.comm.baseUrl + Constants.GOOGLE_LOGO_URL;
    else if(provider == 'Facebook') this.logoUrl = this.$store.state.comm.baseUrl + Constants.FACEBOOK_LOGO_URL;
  },
  data(){
    return {
      loading: false,
      signInWindow: null,
      logoUrl: '',
    }
  },
  props:{
    provider: String,
  },
  methods:{
    SocialSignIn(){
      
      this.loading = !this.loading;

      if(this.signInWindow == null){
        this.signInWindow = window.open(
          Constants.getCustomAuthUrl(this.provider.toLowerCase()),
          '_blank',
          "width=450, height=600",
        )
        this.signInWindow.onunload = this.childWindowUnloadEventHandler;
      }
    },
    childWindowUnloadEventHandler(){
      this.loading = false;
      this.signInWindow = null;
    }
  }
}
</script>

<style scoped>
</style>