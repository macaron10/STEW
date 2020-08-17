<template>
  <div id="Mainpage" >
    <MyPage v-intersect="onIntersect" ref="MyPage"/>
    <v-container >
      <div class="p-3 ma-5">
        <div>
          <h2 class="mb-3 text-center text-sm-left">{{new Date().getMonth()+1}}월의 스터디 랭킹</h2>
          <div v-if="rankGpList.length == 0">아직 이달의 랭킹이 존재하지 않습니다!</div>
          <v-card flat tile class="d-flex justify-center justify-sm-start">
            <v-card
              flat
              tile
              v-for="(gp, index) in rankGpList"
              :key="index + gp"
              class="align-start"
            >
              <v-container v-bind:class="{ 'd-none d-sm-flex' : index > 2}">
                <v-row>
                  <v-row>
                  <v-col cols="12" class="pa-0">
                  <v-img
                    v-if="index < 3"
                    :src="$store.state.comm.baseUrl + `/image/main/${index+1}-medal.png`"
                    height="40px"
                    width="40px"
                    class="mx-auto"
                  ></v-img>
                  </v-col>
                  </v-row>
                  <v-col cols="12" class="text-center">
                    <v-row class="flex-column ma-0 pa-0" justify="center" >
                      <v-col class="pa-0 ma-0" >
                        <span class="blue--text">{{index+1}}등</span>&nbsp;
                        <div class="blue--text text--darken-2" style="display: block; max-width: 240px; max-height:24px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{gp.gpNm}}</div>
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
          <v-col cols="12" lg="8" class="pb-0">
            <TodayTimer />
          </v-col>
          <v-col class="pt-0 pt-lg-3 pl-lg-0">
            <v-col cols="12" sm="6" lg="12" class="pl-lg-0">
              <v-card class="mx-auto" height="180px" color="#42A5F5" dark>
                <v-card-title class="font-weight-bold">나의 다짐</v-card-title>
                <v-row justify="center" align="center" class="m-5">
                  <v-card-text class="h3 headline text-center">{{userIntro}}</v-card-text>
                </v-row>
              </v-card>
            </v-col>
          </v-col>
        </v-row>
      </div>
      <br />
      <div class="p-3 mx-5">
        <h2>스터디 목록</h2>
        <v-icon small color="#666666" class="ml-3">mdi-lock</v-icon>
        <span class="text-caption">비공개 스터디</span>
      </div>
    </v-container>
    <v-container>
      <StudyList :key="componentKey" @event="forceRerender()" />
    <!-- 상단으로 버튼 -->
      <v-btn
      color="red lighten-1"
      v-show="!isIntersecting"
      dark
      bottom
      right
      fixed
      fab
      class="btTop my-5 mx-5"
      @click="$vuetify.goTo(0, {
        duration: 100,
        offset: 0
      })"
      transition="scale-transition"
    >
      <v-icon>mdi-chevron-up</v-icon>
    </v-btn>
    </v-container>
  </div>
</template>

<script>
// @ is an alias to /src
import axios from "axios";
import { mapActions } from "vuex";

import MyPage from "@/components/main/MyPage.vue";
import StudyList from "@/components/main/StudyList.vue";
import TodayTimer from "@/components/timer/UserTodayTime.vue";

export default {
  name: "Main",
  components: {
    MyPage,
    StudyList,
    TodayTimer,
  },
  data: () => ({
    componentKey: 0,
    rankGpList: [],
    userIntro : "",
    pageNumber: 1,
    bottom: false,
    //상단 바로가기 버튼 -> 메인화면에 배너가 보일때 사라짐
    isIntersecting: false
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
    ...mapActions("sg", ["getGroups", "getNextGroups"]),
    forceRerender() {
      this.componentKey += 1;
    },
    getUserInfo() {
      if (this.$store.state.auth.isLogin) {
        axios.get("/user/").then(({ data }) => {
          this.userIntro = data.object.userIntro;
          if (this.userIntro == "" || this.userIntro == null)
            this.userIntro = "나의 각오를 적어보세요!";
        });
      }
    },
    getRankGpList() {
      axios
        .get("/study/rank")
        .then(res => {
          this.rankGpList = res.data.object;
        })
        .catch(err => console.log(err));
    },
    // 무한스크롤
    bottomVisible() {
      const scrollY = window.scrollY
      const visible = document.documentElement.clientHeight
      const pageHeight = document.documentElement.scrollHeight
      const bottomOfPage = visible + scrollY + 64 >= pageHeight
      return bottomOfPage || pageHeight <= visible
    },
    // 상단 바로가기 버튼
      onIntersect (entries, observer) {
        this.isIntersecting = entries[0].isIntersecting
      },
  },
  watch: {
    // 무한스크롤
    bottom(bottom) {
      if (bottom) {
        setTimeout(
          function () {this.getNextGroups(this.pageNumber), this.pageNumber++}.bind(this), 400
          )
      }
    }
  },
  created() {
    //무한스크롤
    window.addEventListener('scroll', () => {
      this.bottom = this.bottomVisible()
    })
    this.getGroups();
  }
};
</script>

<style scoped>
</style>