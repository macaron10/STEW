<template>
<v-layout fill-height align-center justify-center>
    <v-stepper v-model="index" vertical class="elevation-0">
      <v-stepper-step :complete="index > 1" step="1">
        STEW는?
        <!-- <small>Summarize if needed</small> -->
      </v-stepper-step>

      <v-stepper-content step="1">
        <v-card color="grey lighten-1" class="mb-12" width="700px"></v-card>
        <v-btn icon @click="index++;" class="mr-3"><v-icon>mdi-menu-down</v-icon></v-btn>
        <!-- <v-btn color="primary" @click="index = 2">다음</v-btn> -->
        <!-- <v-btn text>Cancel</v-btn> -->
      </v-stepper-content>

      <v-stepper-step :complete="index > 2" step="2">시작하기 전</v-stepper-step>

      <v-stepper-content step="2">
        <!-- <v-card color="grey lighten-1" class="mb-12" height="200px"></v-card> -->
        <img :src="aboutImg1" /><br>
        <v-btn icon @click="index++;" class="mr-3"><v-icon>mdi-menu-down</v-icon></v-btn>
        <v-btn text outlined>회원가입으로 이동</v-btn>
      </v-stepper-content>

      <v-stepper-step :complete="index > 3" step="3">스터디 참여하기</v-stepper-step>

      <v-stepper-content step="3">
        <!-- <v-card color="grey lighten-1" class="mb-12" height="200px"></v-card> -->
        <img :src="aboutImg2" /><br>
        <v-btn icon @click="index++;" class="mr-3"><v-icon>mdi-menu-down</v-icon></v-btn>
        <v-btn text outlined>스터디 생성하기</v-btn>

      </v-stepper-content>

      <v-stepper-step step="4">함께 공부하기</v-stepper-step>
      <v-stepper-content step="4">
        <v-card color="grey lighten-1" class="mb-12" width="700px"></v-card>
        <!-- <v-btn color="primary" @click="index = 1">다음</v-btn> -->
        <v-btn icon @click="index = 1;" class="mr-3"><v-icon>mdi-menu-up</v-icon></v-btn>
        <!-- <v-btn text>Cancel</v-btn> -->
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
        aboutImg1: this.$store.state.comm.baseUrl+"/image/main/About2.jpg",
        aboutImg2: this.$store.state.comm.baseUrl+"/image/main/About3.jpg",
      }
    },
  }
</script>

<style scoped>
/* $stepper-elevation */
</style>