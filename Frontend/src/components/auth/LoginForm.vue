<template>
    <v-layout justify-center>
        <v-form>
            <v-text-field dense outlined
            color="#64b4f6"
            prepend-inner-icon="mdi-account"
            label="이메일"
            type="text"
            v-model="userEmail"
            ></v-text-field>

            <v-text-field dense outlined
            color="#64b4f6"
            label="패스워드"
            prepend-inner-icon="mdi-lock"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append="showPassword = !showPassword"
            v-bind:type="showPassword ? 'text' : 'password'"
            v-model="userPw"
            ></v-text-field>

            <v-btn block dark depressed
            color="#64b4f6"
            @click="signInHandler"
            >
            로그인
            </v-btn>
            <v-btn
            color="gray"
            text small
            >
                아이디/비밀번호 찾기
            </v-btn>

            <v-btn
            :to="{ name: 'Signup' }"
            color="light gray"
            text small
            >
                회원가입
            </v-btn>
        </v-form>
    </v-layout>
</template>

<script>
import { mapActions } from 'vuex';

export default {
    data(){
        return{
            showPassword: false,
            userEmail: "",
            userPw: ""
        }
    },
    methods:{
        ...mapActions('auth', [
            "signIn"
            ]),
        async signInHandler() {
            await this.signIn({'userEmail': this.userEmail, 'userPw':this.userPw});
            this.userEmail = this.userPw = "";
            this.$router.go();
        }
    }
}
</script>

<style>
</style>