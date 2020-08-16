<template>
    <v-progress-circular
        :indeterminate="true"
        style="top: 50%; left: 50%"
        color="light-blue"
      ></v-progress-circular>
</template>

<script>
import {ACCESS_TOKEN_STRING, REFRESH_TOKEN_STRING, TOKEN_PREFIX} from '../../constants';

export default {
    created(){
        const accessToken = TOKEN_PREFIX + this.getUrlParameter(ACCESS_TOKEN_STRING);
        const refreshToken = TOKEN_PREFIX + this.getUrlParameter(REFRESH_TOKEN_STRING);

        // fail handling
        // const error = this.getUrlParameter('error');
        // if(!error){

        // }
        
        window.opener.app.$root.$store.commit('auth/loginSuccess', {accessToken, refreshToken})
        window.close();

    },
    methods:{
        getUrlParameter(key) {
            const regex = new RegExp('[\\?&]' + key + '=([^&#]*)');
            return regex.exec(window.location.hash)[1];
        },
    },
}
</script>

<style scoped>
</style>