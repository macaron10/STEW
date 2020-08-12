<template>
    <v-container>
        <v-row>
            <v-col cols="4"></v-col>
            <v-col cols="4">
                <main-logo></main-logo>
            </v-col>
            <v-col cols="4"></v-col>
        </v-row>
        <v-row>
            <v-col cols="1" md="3"></v-col>
            <v-col cols="10" md="6">
                <login-form></login-form>
                <v-divider class="my-6"></v-divider>
                <social-form></social-form>
            </v-col>
            <v-col cols="1" md="3"></v-col>
        </v-row>
    </v-container>
</template>

<script>
import LoginForm from "@/components/auth/LoginForm"
import MainLogo from "@/components/auth/MainLogo"
import SocialForm from "@/components/auth/SocialForm"

export default {
    components:{
        LoginForm,
        MainLogo,
        SocialForm
    }
}
</script>

<style>

</style>