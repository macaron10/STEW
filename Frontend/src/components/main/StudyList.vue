<template>
  <v-container fluid>
    <v-row>
      <v-col
        v-for="group in groups"
        :key="group.gpNo"
        class="d-flex child-flex"
        cols="6"
        xs="6"
        sm="4"
        md="3"
      >
        <v-hover>
          <template v-slot:default="{ hover }">
            <!-- :to="'/study/' + group.gpNo" -->
            <v-card class="mx-auto" height="250" @click="toDetail(group)">
              <v-img
                :src="group.gpImg != null?($store.state.comm.baseUrl + '/image/group' + group.gpImg):gpImgDefault"
                height="170"
                color="white"
                gradient="to bottom, rgba(255,255,255,0), rgba(0,0,0,.15)"
              >
                <v-row>
                  <v-col>
                    <div class="body-1 white--text pl-2" align="start">
                      <v-icon small color="#212121" v-if="!group.gpPublic">mdi-lock</v-icon>
                      <v-icon small v-if="group.gpPublic"></v-icon>
                    </div>
                  </v-col>
                </v-row>
                <v-row align="center" class="mt-5 lightbox black--text pa-2"></v-row>
                <v-row align="end" class="mt-9 lightbox black--text pa-2">
                  <v-col>
                    <div class="body-1 white--text">
                      <v-icon color="white">mdi-account</v-icon>
                      {{ group.gpCurNum }}/{{ group.gpMaxNum }}
                    </div>
                  </v-col>
                </v-row>
              </v-img>
              <v-list-item>
                <!-- 제목, 간단한설명, 태그, 썸네일, 인원수 -->
                <v-list-item-content>
                  <v-list-item-title>{{ group.gpNm }}</v-list-item-title>
                  <v-list-item-subtitle class="text-truncate">
                    <v-chip
                      class="ma-2"
                      small
                      color="#F5F5F5"
                      v-for="tag in group.gpTag"
                      :key="tag"
                    ># {{tag}} &nbsp;&nbsp;</v-chip>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <!-- <v-card-text>
              </v-card-text>-->
              <v-fade-transition>
                <v-overlay v-if="hover" absolute color="#424242">
                  <div>
                    <p class="multiwrap mx-auto">{{group.gpIntro}}</p>
                    <div class="text-justify p-10 text-center mx-auto" style="width:80%">
                      <span v-for="tag in group.gpTag" samll :key="tag">#{{tag}}&nbsp;&nbsp;</span>
                    </div>
                  </div>
                  <!-- <v-btn
                    align="end"
                    color="#039BE5"
                  >입장하기</v-btn> -->
                </v-overlay>
              </v-fade-transition>
            </v-card>
          </template>
        </v-hover>
      </v-col>
    </v-row>
    <v-dialog v-model="dialog" width="500">
      <v-card>
        <v-card-title
          class="headline blue accent-2 white--text"
        >'{{ selectedGroup.gpNm }}' 에 가입하시겠습니까?</v-card-title>

        <v-card-text class="py-1">{{selectedGroup.gpIntro}}</v-card-text>

        <v-divider></v-divider>
        <v-textarea v-model="message" color="teal" class="mx-5">
          <template v-slot:label>
            <div class="px-5">
              보낼 메세지
              <small>(optional)</small>
            </div>
          </template>
        </v-textarea>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="signUpGroup(selectedGroup.gpNo)">가입신청</v-btn>
          <v-btn color="primary" text @click="dialog = false">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
import querystring from "querystring";

export default {
  name: "StudyList",
  data() {
    return {
      myGroups: {},
      dialog: false,
      selectedGroup: {},
      snackbar: false,
      message: "",
      gpImgDefault: this.$store.state.comm.baseUrl + "/image/group/default.png"
    };
  },
  methods: {
    ...mapActions("sg", ["getGroups"]),
    // 솔팅 함수
    // sortBy (prop) {
    //   if (prop === 'popularity') {
    //     this.sortCriteria = 'Most Popular'
    //   } else if (prop === 'vote_average') {
    //     this.sortCriteria = 'Highest Rated'
    //   } else if (prop === 'release_date') {
    //     this.sortCriteria = 'Release Date'
    //   }
    //   this.sortedBy = prop
    //   this.movies.sort((a, b) => (a[prop] > b[prop] ? -1 : 1))
    // }
    async getMyGroups() {
      const apiUrl = "/study/user/my";
      try {
        const res = await axios.get(apiUrl);
        this.myGroups = res.data.object;
      } catch (err) {
        console.error(err);
      }
    },
    toDetail(group) {
      this.selectedGroup = group;
      let flag = false;
      for (let i = 0; i < this.myGroups.length; i++) {
        if (group.gpNo === this.myGroups[i].gpNo) {
          flag = true;
          // this.$router.push('/study/' + gpNo)
        }
      }
      if (flag === true) {
        this.$router.push("/study/" + group.gpNo);
      } else {
        this.dialog = true;
      }
    },
    // signUpGroup(gpNo) {
    //   const apiUrl = '/study/user/req'
    //   this.gpNoData.gpNo = gpNo
    //   console.log(this.gpNoData)
    //   axios.post(apiUrl, this.gpNoData)
    //   .then((res) => {
    //     console.log(res)
    //     this.dialog = false
    //     this.snackbar = true
    //     })
    // },
    async signUpGroup(gpNo) {
      const apiUrl = "/study/user/req?gpNo=" + gpNo;
      const msg = {
        reqMsg: this.message
      };
      try {
        const res = await axios.post(
          apiUrl,
          querystring.stringify({ reqMsg: this.message })
        );
        console.log(res);
        this.dialog = false;
        this.snackbar = true;
      } catch (err) {
        console.error(err);
      }
    }
  },
  computed: {
    // groups () { return this.$store.state.sg.groups }
    ...mapState("sg", ["groups"])
  },
  mounted() {
    this.getGroups();
    if (this.$store.state.auth.isLogin) {
      this.getMyGroups();
    }
  }
};
</script>

<style>
.multiwrap {
  /* 한 줄 자르기 */
  display: inline-block;
  width: 80%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  /* 여러 줄 자르기 추가 스타일 */
  white-space: normal;
  line-height: 1.2;
  height: 3.6em;
  text-align: center;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>