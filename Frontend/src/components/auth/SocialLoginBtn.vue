<template>
  <div>
    <v-btn 
    @click="SocialSignIn"
    :block="true"
    >
      <v-progress-circular
        v-show="loading"
        :indeterminate="true"
        style="position: absolute"
        color="light-blue"
      ></v-progress-circular>
      <img
        style="position:absolute; left: 5%"
        :src="logoUrl"
        width="20"
        height="20"
        />
      {{btnText}}
    </v-btn>
  </div>
</template>

<script>
import axios from "axios";
import * as Constants from "../../constants";

export default {
  created(){
    const provider = this.$props.provider;

    if(provider == 'Kakao') this.logoUrl = Constants.KAKAO_LOGO_URL;
    else if(provider == 'Naver') this.logoUrl = Constants.NAVER_LOGO_URL;
    else if(provider == 'Google') this.logoUrl = Constants.GOOGLE_LOGO_URL;
    else if(provider == 'Facebook') this.logoUrl = Constants.FACEBOOK_LOGO_URL;

    this.btnText = 'Sign in with ' + provider;
  },
  data(){
    return {
      loading: false,
      signInWindow: null,
      logoUrl: '',
      btnText: ''
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
      this.loading = !this.loading;
      this.signInWindow = null;
    }
  }
}
</script>

<style scoped>
</style>