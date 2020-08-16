<template>
    <v-container fill-height>
        <v-row align="center" justify="center">
            <v-col cols="10">
                <main-logo></main-logo>
                <login-form></login-form>
                <v-divider class="my-6"></v-divider>
                <social-form></social-form>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import LoginForm from "@/components/auth/LoginForm"
import MainLogo from "@/components/auth/MainLogo"
import SocialForm from "@/components/auth/SocialForm"
import { mapGetters } from "vuex"

export default {
    created(){
        if(this.loginStatus) this.$router.back();
    },
    components:{
        LoginForm,
        MainLogo,
        SocialForm
    },
    computed: {
        ...mapGetters('auth', [
            'loginStatus',
        ]),
    },
    watch:{
        loginStatus: function(){
            this.$router.back()
        }
    }
}
</script>

<style>

</style>