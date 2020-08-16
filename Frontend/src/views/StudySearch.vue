<template>
  <v-container>
    <div v-if="gpList == 0" class="text-center my-10">
      <h1>검색 결과가 없습니다!</h1>
      <h3>새로운 스터디를 등록해보세요 :)</h3>
    </div>
    <div class="mx-auto">
      <div v-if="gpList != 0" class="my-10 text-center">
        <h2>검색결과</h2>
      </div>
      <v-row v-for="group in gpList" :key="group.gpNo">
        <v-card height="150" width="80%" @click="enterStudy(group)" class="mx-auto my-2">
          <div class="d-flex flex-no-wrap">
            <div width="200" class="d-flex align-stretch align-center">
              <v-img
                class="ma-2"
                :src="group.gpImg != null?($store.state.comm.baseUrl + '/image/group' + group.gpImg):gpImgDefault"
                max-width="200"
                max-height="140"
                contain
              />
            </div>
            <div align="left" class="text-left align-center ml-3">
              <v-card-title v-text="group.gpNm"></v-card-title>
              <v-card-subtitle class="text-truncate py-1" v-text="group.gpIntro"></v-card-subtitle>
              <v-card-text class="my-0 py-1">
                <p class="body-2" v-if="!group.gpPublic">
                  <v-icon small color="#616161">mdi-lock</v-icon>
                  <span color="#616161">비공개 스터디</span>
                </p>
                <v-else>
                  <span color="light-blue darken-2">공개 스터디</span>
                </v-else>
              </v-card-text>
              <div class="text-truncate">
                <v-chip
                  class="ma-2"
                  small
                  color="#F5F5F5"
                  v-for="tag in group.gpTag"
                  :key="tag"
                ># {{tag}} &nbsp;&nbsp;</v-chip>
              </div>
            </div>
          </div>
        </v-card>
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
    </div>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import querystring from "querystring";

export default {
  name: "StudySearch",
  data() {
    return {
      selectedGroup: { gpNo: 0, gpNm: "", gpIntro: "" },
      dialog: false,
      message: "",
      gpImgDefault: this.$store.state.comm.baseUrl + "/image/group/default.png"
    };
  },
  computed: {
    keyword() {
      return this.$store.state.sg.keyword;
    },
    gpList() {
      return this.$store.state.sg.searchedGroups;
    }
  },
  methods: {
    async enterStudy(group) {
      if (!this.$store.state.comm.isLogin) {
        alert("로그인이 필요합니다!");
        this.$router.push({name:'Login'}) 
        return;
      }

      try {
        const joinCkUrl = "/study/user/joinck/" + group.gpNo;
        const joinRes = await axios.get(joinCkUrl);
        const joinCk = joinRes.data.object;

        if (joinCk) {
          this.$router.push("/study/" + group.gpNo);
        } else {
          const reqCkUrl = "/study/user/reqck/" + group.gpNo;
          const reqRes = await axios.get(reqCkUrl);
          const reqCk = reqRes.data.object;

          if (reqCk) alert("아직 가입 승인 대기중인 그룹입니다");
          else {
            this.dialog = true;
            this.selectedGroup = group;
          }
        }
      } catch (err) {
        console.error(err);
      }
    },
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
        this.message = "";
      } catch (err) {
        console.error(err);
      }
    }
  }
};
</script>
