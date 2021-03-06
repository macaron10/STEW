<template>
  <div id="Mainpage">
    <v-container>
      <div class="p-3 ma-5">
        <v-row>
          <v-col cols="12" md="4">
            <v-row>
              <h2 class="mb-3 pl-7 text-center text-sm-left">{{new Date().getMonth()+1}}월의 스터디 랭킹</h2>
              <div v-if="rankGpList.length == 0">아직 이달의 랭킹이 존재하지 않습니다!</div>
            </v-row>
            <v-row>
              <v-col cols="12" color="#fcfcfc" class="d-flex justify-center align-center">
                <v-card
                  flat
                  tile
                  v-for="(gp, index) in rankGpList"
                  :key="index + gp"
                  class="align-start"
                  color="#fcfcfc"
                >
                  <v-container>
                    <v-row>
                      <v-row>
                        <v-col cols="12" class="pa-0">
                          <v-img
                            :src="$store.state.comm.baseUrl + `/image/main/${index+1}-medal.png`"
                            height="40px"
                            width="40px"
                            class="mx-auto"
                          ></v-img>
                        </v-col>
                      </v-row>
                      <v-col cols="12" class="text-center">
                        <v-row class="flex-column ma-0 pa-0" justify="center">
                          <v-col class="pa-0 ma-0">
                            <span class="blue--text">{{index+1}}등</span>&nbsp;
                            <div
                              class="blue--text text--darken-2"
                              style="display: block; max-width: 240px; max-height:24px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;"
                            >{{gp.gpNm}}</div>
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
              </v-col>
            </v-row>
          </v-col>
          <v-col cols="12" md="8">
            <Banner v-intersect="onIntersect" ref="Banner" />
          </v-col>
        </v-row>
      </div>
      <div class="p-3 ma-5" v-if="$store.state.auth.isLogin">
        <v-row>
          <v-col cols="12" lg="8" class="pb-0">
            <UserTodayTime />
          </v-col>
          <v-col class="pt-0 pt-lg-3 pl-lg-0" lg="4">
            <v-col cols="12" sm="6" lg="12" class="pl-lg-0">
              <v-card class="mx-auto" height="180px" color="blue lighten-2" dark>
                <v-card-title class="font-weight-bold pb-1">나의 다짐</v-card-title>
                <v-row
                  justify="center"
                  align="center"
                  style="height: 100px; overflow: hidden;"
                >
                  <v-card-text class="text-h5 text-sm-h5 pa-0 multiwrap">{{userIntro}}</v-card-text>
                </v-row>
              </v-card>
            </v-col>
          </v-col>
        </v-row>
      </div>
      <br />
      <v-row class="pl-10 d-flex align-end">
        <v-col cols="12" sm="4" md="2">
          <h2 class="mb-2">스터디 목록</h2>
        </v-col>
        <v-col class="pb-5" cols="4" sm="4" md="4">
          <v-select
            :items="categories"
            item-value="gpCatNo"
            item-text="gpCatNm"
            label="카테고리"
            v-model="gpCatNo"
            hide-details
          ></v-select>
        </v-col>
        <v-col cols="5" sm="3" lg="2" offset="2" offset-sm="0" offset-md="3" offset-lg="4">
          <v-btn
            class="mx-auto mb-3"
            color="amber accent-3 white--text"
            :to="{name: 'StudyCreate'}"
          >
            <v-icon class="mr-2">mdi-plus</v-icon>스터디 생성
          </v-btn>
        </v-col>
      </v-row>
      <StudyList class="pt-0 pb-10" />
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

import Banner from "@/components/main/Banner.vue";
import StudyList from "@/components/main/StudyList.vue";
import UserTodayTime from "@/components/timer/UserTodayTime.vue";

export default {
  name: "Main",
  components: {
    Banner,
    StudyList,
    UserTodayTime
  },
  data: () => ({
    rankGpList: [],
    userIntro: "",
    pageNumber: 1,
    bottom: false,
    //상단 바로가기 버튼 -> 메인화면에 배너가 보일때 사라짐
    isIntersecting: false,
    //카테고리 분류
    categories: [],
    gpCatNo: -1
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
      const scrollY = window.scrollY;
      const visible = document.documentElement.clientHeight;
      const pageHeight = document.documentElement.scrollHeight;
      const bottomOfPage = visible + scrollY + 64 >= pageHeight;
      return bottomOfPage || pageHeight <= visible;
    },
    // 상단 바로가기 버튼
    onIntersect(entries, observer) {
      this.isIntersecting = entries[0].isIntersecting;
    },
    async getCategories() {
      const apiUrl = "/study/cate";
      try {
        const res = await axios.get(apiUrl);
        const allGroups = {
          gpCatNo: -1,
          gpCatNm: "전체"
        };
        this.categories.push(allGroups);
        this.categories.push(...res.data.object);
      } catch (err) {
        console.error(err);
      }
    }
    // async getCateGroups() {

    // }
  },
  watch: {
    // 무한스크롤
    bottom(bottom) {
      if (bottom) {
        setTimeout(
          function() {
            this.getNextGroups([this.pageNumber, this.gpCatNo]),
              this.pageNumber++;
          }.bind(this),
          400
        );
      }
    },
    gpCatNo(gpCatNo) {
      this.getGroups(gpCatNo);
      this.pageNumber = 1;
    }
  },
  created() {
    this.getCategories();
    this.getGroups(this.gpCatNo);
    //무한스크롤
    window.addEventListener("scroll", () => {
      this.bottom = this.bottomVisible();
    });
  }
};
</script>

<style>
#Mainpage {
  background-color: #fcfcfc;
}
.multiwrap {
  /* 한 줄 자르기 */

  display: inline-block;
  width: 85%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  /* 여러 줄 자르기 추가 스타일 */
  white-space: normal;
  line-height: 1.2;
  height: 98px;
  text-align: center;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
