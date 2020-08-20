<template>
  <v-container>
    <v-row>
      <v-col cols="12" v-if="groups.length == 0" class="my-10">
        <div style="color:#424242;" class="text-center">
          <h1>카테고리에 등록된 스터디가 없습니다 :(</h1>
          <h3>새로운 스터디를 만들어 STEW와 함께 공부해요!</h3>
        </div>
      </v-col>
      <v-col
        v-for="group in groups"
        :key="group.gpNo"
        class="d-flex child-flex"
        cols="12"
        sm="6"
        md="4"
        lg="3"
      >
        <v-hover>
          <template v-slot:default="{ hover }">
            <!-- :to="'/study/' + group.gpNo" -->
            <v-card class="ma-2" height="250" @click="toDetail(group)" :elevation="1">
              <v-img
                :src="group.gpImg != null?($store.state.comm.baseUrl + '/image/group' + group.gpImg):gpImgDefault"
                height="170"
                color="white"
                gradient="to bottom, rgba(255,255,255,0), rgba(0,0,0,.15)"
              >
                <v-row>
                  <v-col>
                    <div class="body-1 white--text pl-2" align="end">
                      <v-chip
                        small
                        class="mr-2 mb-3"
                        color="blue darken-4"
                        text-color="white"
                        v-if="!group.gpPublic"
                      >
                        <v-avatar left>
                          <v-icon small>mdi-lock</v-icon>
                        </v-avatar>비공개
                      </v-chip>
                      <v-row v-else align="center" class="ma-3 lightbox black--text pa-2"></v-row>
                    </div>
                  </v-col>
                </v-row>
                <v-row align="center" class="mt-5 lightbox black--text pa-2"></v-row>
                <v-row align="end" class="mt-5 lightbox black--text pa-2">
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
                    <p style="min-width: 200px;" class="multiwrap mx-auto">{{group.gpIntro}}</p>
                    <div class="text-justify p-10 text-center mx-auto" style="width:80%">
                      <span v-for="tag in group.gpTag" samll :key="tag">#{{tag}}&nbsp;&nbsp;</span>
                    </div>
                  </div>
                  <!-- <v-btn
                    align="end"
                    color="#039BE5"
                  >입장하기</v-btn>-->
                </v-overlay>
              </v-fade-transition>
            </v-card>
          </template>
        </v-hover>
      </v-col>
    </v-row>
    <v-dialog v-model="dialog" width="500">
      <v-card>
        <v-card-title class="headline grey lighten-4"><b>{{ selectedGroup.gpNm }}에 가입하시겠습니까?</b></v-card-title>
        <v-textarea v-model="message" color="teal" class="mx-5" v-if="!selectedGroup.gpPublic">
          <template v-slot:label>
            <div class="px-5">
              가입신청 메세지를 작성해보세요!
            </div>
          </template>
        </v-textarea>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="signUpGroup(selectedGroup)">신청하기</v-btn>
          <v-btn text @click="dialog = false">닫기</v-btn>
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
    async getMyGroups() {
      const apiUrl = "/study/user/my";
      try {
        const res = await axios.get(apiUrl);
        this.myGroups = res.data.object;
      } catch (err) {
        console.error(err);
      }
    },
    async toDetail(group) {
      if (this.$store.state.auth.isLogin === false) {
        alert("로그인이 필요합니다.");
        this.$router.push({ name: "Login" });
        return;
      }
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
        const reqCkUrl = "/study/user/reqck/" + group.gpNo;
        const reqRes = await axios.get(reqCkUrl);
        const reqCk = reqRes.data.object;

        if (reqCk) alert("아직 가입 승인 대기 중인 스터디입니다");
        else this.dialog = true;
      }
    },
    async signUpGroup(group) {
      if (group.gpCurNum === group.gpMaxNum) {
        alert("정원이 가득 찼습니다.");
        this.message = "";
        this.dialog = false;
        return;
      }
      const apiUrl = "/study/user/req?gpNo=" + group.gpNo;
      const msg = {
        reqMsg: this.message
      };
      try {
        const res = await axios.post(
          apiUrl,
          querystring.stringify({ reqMsg: this.message })
        );
        this.dialog = false;
        this.snackbar = true;
        this.message = "";
        if (group.gpPublic) {
          alert("공개그룹입니다. 자동가입됩니다.");
          this.$router.push({
            name: "StudyDetail",
            params: { id: group.gpNo }
          });
        } else {
          alert("비공개 그룹입니다. 그룹장의 승인을 기다려 주세요");
        }
        // this.$router.go()
      } catch (err) {
        console.error(err);
      }
    }
  },
  computed: {
    // groups () { return this.$store.state.sg.groups }
    ...mapState("sg", ["groups"])
  },
  created() {
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