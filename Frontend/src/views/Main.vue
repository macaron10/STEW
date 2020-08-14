<template>
  <div id="Mainpage">
    <MyPage />
    <div class="p-3 ma-5">
      <div>
        <h2 class="mb-3 text-center text-sm-left">{{new Date().getMonth()+1}}월의 스터디 랭킹</h2>
        <div v-if="rankGpList.length == 0">아직 이달의 랭킹이 존재하지 않습니다!</div>
        <v-card flat tile class="d-flex justify-center justify-sm-start">
          <v-card flat tile v-for="(gp, index) in rankGpList" :key="index + gp" class="align-start">
            <v-container v-bind:class="{ 'd-none d-sm-flex' : index > 2}">
              <v-row justify="space-between">
                <v-col
                  cols="4"
                  offset="4"
                  sm="4"
                  offset-sm="0"
                  class="pa-0 pa-sm-3"
                  v-if="index < 3"
                >
                  <v-img
                    :src="baseUrl + `image/main/${index+1}-medal.png`"
                    height="40px"
                    width="40px"
                  ></v-img>
                </v-col>
                <v-col cols="auto" class="pa-auto" v-else></v-col>
                <v-col cols="12" sm="auto" class="text-center pl-0">
                  <v-row class="flex-column ma-0 pa-0" justify="center">
                    <v-col class="pa-0 ma-0">
                      <span class="blue--text">{{index+1}}등</span>&nbsp;
                      <a color="black">{{gp.gpNm}}</a>
                    </v-col>
                    <v-col class="pa-0 ma-0">
                      <span class="subtitle-2">
                        평균
                        {{gp.tmAcmlAvg | toTimeFormat}}
                      </span>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-card>
      </div>
    </div>
    <div class="p-3 ma-5" v-if="$store.state.auth.isLogin">
      <v-row>
        <v-col cols="12">
          <TodayTimer />
        </v-col>
        <v-col>
          <v-card class="mx-auto" height="180px" color="#42A5F5" dark>
            <v-card-title class="font-weight-bold">나의 다짐</v-card-title>
            <v-row justify="center" align="center" class="m-5">
              <v-card-text class="h3 headline text-center">{{userIntro}}</v-card-text>
            </v-row>
          </v-card>
        </v-col>
      </v-row>
    </div>
    <br />

    <br />
    <div class="p-3 mx-5">
      <h2>스터디 목록</h2>
      <v-icon small color="#666666" class="ml-3">mdi-lock</v-icon>
      <span class="text-caption">비공개 스터디</span>
    </div>
    <StudyList />
  </div>
</template>

<script>
// @ is an alias to /src
import axios from "axios";

import MyPage from "@/components/main/MyPage.vue";
import StudyList from "@/components/main/StudyList.vue";
import TodayTimer from "@/components/timer/UserTodayTime.vue";

export default {
  name: "Main",
  components: {
    MyPage,
    StudyList,
    TodayTimer
  },
  data: () => ({
    rankGpList: [],
    userIntro: "",
    // baseUrl: "http://localhost:8399/api/"
    baseUrl: "https://i3b103.p.ssafy.io/"
    // baseUrl: this.$store.state.comm.baseUrl
  }),
  filters: {
    toTimeFormat: sec => {
      let hours = Math.floor(sec / 3600);
      let minutes = Math.floor((sec - hours * 3600) / 60);
      let seconds = sec - hours * 3600 - minutes * 60;

      if (hours < 10) {
        hours = "0" + hours;
      }
      if (minutes < 10) {
        minutes = "0" + minutes;
      }
      if (seconds < 10) {
        seconds = "0" + seconds;
      }
      return hours + ":" + minutes + ":" + seconds;
    }
  },
  mounted() {
    this.getRankGpList();
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      if (this.$store.state.auth.isLogin)
        axios.get("/user/").then(({ data }) => {
          this.userIntro = data.object.userIntro;
        });
    },
    getRankGpList() {
      axios
        .get("/study/rank")
        .then(res => {
          this.rankGpList = res.data.object;
        })
        .catch(err => console.log(err));
    }
  }
};
</script>
