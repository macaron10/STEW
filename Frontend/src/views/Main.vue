<template>
  <div id="Mainpage">
    <MyPage 
      v-intersect="onIntersect"
    />
    <div class="mt-5" v-if="$store.state.auth.isLogin">
      <v-row class="d-none d-sm-flex">
        <v-col md="6" xs="12">
          <TodayTimer />
        </v-col>
        <v-col md="5" sm="10" xs="12" class="mt-3 ml-3">
          <v-card class="mx-2" height="180px" color="#42A5F5" dark>
            <v-card-title class="font-weight-bold">나의 다짐</v-card-title>
            <v-row justify="center" align="center" class="m-5">
              <v-card-text class="h3 headline text-center ">{{userIntro}}</v-card-text>
            </v-row>
          </v-card>
        </v-col>
      </v-row>
    </div>
    <br />
    <div class="p-3 ma-5">
      <h2 class="mb-3">이달의 스터디 랭킹</h2>
      <div class="pl-3">
        <div v-if="rankGpList.length == 0">아직 이달의 랭킹이 존재하지 않습니다!</div>
        <v-card flat tile class="d-flex">
          <v-card flat tile v-for="(gp, index) in rankGpList" :key="index + gp" class="align-start">
            <v-container>
              <v-row justify="space-between">
                <v-col cols="auto" v-if="index < 3">
                  <v-img
                    :src="$store.state.comm.baseUrl + `/image/main/${index+1}-medal.png`"
                    height="40px"
                    width="40px"
                  ></v-img>
                </v-col>
                <v-col cols="auto" v-else></v-col>
                <v-col cols="auto" class="text-center pl-0">
                  <v-row class="flex-column ma-0 pa-0" justify="center">
                    <v-col class="pa-0 ma-0">
                      <span class="blue--text">{{index+1}}등</span>&nbsp;
                      <a color="black" >{{gp.gpNm}}</a>
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
    <br />
    <div class="p-3 mx-5">
      <h2>스터디 목록</h2>
      <v-icon small color="#666666" class="ml-3">mdi-lock</v-icon>
      <span class="text-caption">비공개 스터디</span>
    </div>
    <StudyList 
      :key="componentKey"
      @event="forceRerender()" />
    <!-- 상단으로 버튼 -->
      <v-btn
      color="red lighten-1"
      v-if="!isIntersecting"
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
    >
      <v-icon>mdi-chevron-up</v-icon>
    </v-btn>
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
    componentKey: 0,
    rankGpList: [],
    userIntro : "",
    // baseUrl: "http://localhost:8399/api",
    baseUrl: "https://i3b103.p.ssafy.io",
    // baseUrl: this.$store.state.comm.baseUrl,
    bottom: false,
    //상단 바로가기 버튼
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
    forceRerender() {
      this.componentKey += 1;
    },
    getUserInfo() {
      axios.get("/user/").then(({ data }) => {
        this.userIntro = data.object.userIntro;
        if(this.userIntro ==""||this.userIntro==null)
        this.userIntro="나의 각오를 적어보세요!";
      });
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
      const bottomOfPage = visible + scrollY >= pageHeight
      return bottomOfPage || pageHeight < visible
    },
    // 상단 바로가기 버튼
      onIntersect (entries, observer) {
        this.isIntersecting = entries[0].isIntersecting
        console.log('인터섹트중')
      },
  },
  watch: {
    // 무한스크롤
    bottom(bottom) {
      if (bottom) {
        console.log('그룹 불러오기!')
      }
    }
  },
  created() {
    //무한스크롤
    window.addEventListener('scroll', () => {
      this.bottom = this.bottomVisible()
    })
    console.log('그룹불러오기!')
  }
};
</script>
