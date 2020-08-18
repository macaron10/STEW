<template>
<v-layout fill-height align-center justify-center>
    <v-stepper v-model="index" vertical class="elevation-0">
      <v-stepper-step :complete="index > 1" step="1">
        STEW는?
        <!-- <small>Summarize if needed</small> -->
      </v-stepper-step>

      <v-stepper-content step="1">
        <img :src="aboutImg[0]" /><br>
        <v-btn @click="index++;" text outlined class="mr-3">다음</v-btn>
        <!-- <v-btn icon @click="index++;" class="mr-3"><v-icon>mdi-menu-down</v-icon></v-btn> -->
      </v-stepper-content>

      <v-stepper-step :complete="index > 2" step="2">시작하기 전</v-stepper-step>

      <v-stepper-content step="2">
        <img :src="aboutImg[1]" />
        <img :src="aboutImg[2]" /><br>
        <v-btn @click="index++;" text outlined class="mr-3">다음</v-btn>
        <v-btn text outlined @click="moveSignup">회원가입으로 이동</v-btn>
      </v-stepper-content>

      <v-stepper-step :complete="index > 3" step="3">스터디 참여하기</v-stepper-step>

      <v-stepper-content step="3">
        <img :src="aboutImg[3]" />
        <img :src="aboutImg[4]" /><br>
        <v-btn @click="index++;" text outlined class="mr-3">다음</v-btn>
        <v-btn text outlined @click="moveCreateStd">스터디 생성하기</v-btn>

      </v-stepper-content>

      <v-stepper-step step="4">함께 공부하기</v-stepper-step>
      <v-stepper-content step="4">
        <img :src="aboutImg[5]" />
        <img :src="aboutImg[6]" /><br>
        <v-btn @click="index = 1;" text outlined class="mr-3">처음으로</v-btn>
      </v-stepper-content>
    </v-stepper>
  </v-layout>
</template>

<script>
  export default {
    name: "About",
    data () {
      return {
        index: 1,
        aboutImg: [],
      }
    },

    created() {
      for (let i = 0; i <= 6; i++) {
        this.aboutImg[i] = this.$store.state.comm.baseUrl + "/image/main/about" + i + ".jpg";
      }
    },

    methods: {
      moveSignup() {
        this.$router.push('/user/signup');
      },

      moveCreateStd() {
        this.$router.push('/study/create');
      }
    }
  }
</script>

<style scoped>
  img {
    max-height: 350px !important;
  }
</style>