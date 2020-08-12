<template>
    <v-container>
        <v-layout justify-center>
            <social-login-btn provider="Naver"></social-login-btn>
            <social-login-btn provider="Kakao"></social-login-btn>
            <social-login-btn provider="Facebook"></social-login-btn>
            <social-login-btn provider="Google"></social-login-btn>
        </v-layout>
    </v-container>
</template>

<script>
import SocialLoginBtn from "@/components/auth/SocialLoginBtn"

export default {
    components: {
        SocialLoginBtn
    }
}
</script>

<style>

</style>