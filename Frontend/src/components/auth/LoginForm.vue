<template>
    <v-container>
        <link rel='stylesheet prefetch' href='https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons'>
        <v-layout justify-center>
            <v-form>
                <v-text-field dense outlined
                color="#64b4f6"
                prepend-inner-icon="person"
                label="이메일"
                type="text"
                ></v-text-field>

                <v-text-field dense outlined
                color="#64b4f6"
                label="패스워드"
                prepend-inner-icon="lock"
                append-icon="mdi-eye-off"
                @click:append="showPassword = !showPassword"
                v-bind:type="showPassword ? 'text' : 'password'"
                ></v-text-field>

                <v-btn block dark depressed
                color="#64b4f6"
                >
                로그인
                </v-btn>
            </v-form>
        </v-layout>
    </v-container>
</template>

<script>
export default {
    data(){
        return{
            showPassword: false,
            userEmail: String,
            userPw: String
        }
    }
}
</script>

<style>
</style>