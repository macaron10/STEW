<template>
  <v-container fluid>
    <v-row>
      <v-col v-for="member in members" :key="member.userId" class="d-flex child-flex" cols="12">
        <v-card class="mx-auto">
          <v-list-item>
            <!-- 가져올 수 있는 것  목표시간, 유저이름, 유저소개, 유저이미지, (유저 이메일,유저아이디)(필요x) -->
            <v-list-item-content>
              <v-row>
                <v-col cols="4" offset="4" sm="2" offset-sm="0" class="d-flex flex-column pr-0 pt-0 py-sm-0" >
                  <img
                    v-if="member.user.userImg"
                    alt="Avatar"
                    :src="$store.state.comm.baseUrl + '/image/user' + member.user.userImg"
                    style="width: 100%"
                    class="my-auto rounded-pill"
                  />
                  <!-- @click="toDetail(member.userId)" 유저디테일 이동기능-->
                  <!-- :to="'/user/' + user넘버" -->
                  <!-- 아래 아이콘대신 기본이미지를 넣는것이 좋을듯!! -->
                  <img
                    v-else
                    :src="$store.state.comm.baseUrl + '/image/user/default.png'"
                    alt="default image"
                  />
                </v-col>
                <v-col cols="12" sm="10" class="py-0 text-sm-align d-sm-flex">
                  <div class="my-auto text-center text-sm-left">
                    <h2 class="d-inline">{{ member.user.userNm }}  </h2>
                    <v-icon v-if="gpMgrId===member.user.userId" color="amber">mdi-crown</v-icon>
                    <v-tooltip top>
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                          v-bind="attrs"
                          v-on="on"
                          icon
                          small
                          v-if="gpMgrId===userInfo.userId&&member.user.userId!==userInfo.userId"
                          color="purple lighten-2"
                          @click="mand(gpNo, member.user.userId)"
                        >
                          <v-icon>mdi-account-convert</v-icon>
                        </v-btn>
                      </template>
                      <span>그룹장 위임</span>
                    </v-tooltip>
                    <v-tooltip top>
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                          icon
                          small
                          v-bind="attrs"
                          v-on="on"
                          v-if="gpMgrId===userInfo.userId&&member.user.userId!==userInfo.userId"
                          color="red lighten-2"
                          @click="kick(member.gpJoinNo)"
                          class
                        >
                          <v-icon>mdi-account-off-outline</v-icon>
                        </v-btn>
                      </template>
                      <span>퇴장시키기</span>
                    </v-tooltip>
                    <h4>{{ member.user.userEmail }}</h4>
                    <h5>목표 : 하루 {{ member.user.userGoalHr }}시간</h5>
                  </div>
                </v-col>
                <v-col cols="12">
                  <h4>{{ member.user.userIntro }}</h4>
                  
                </v-col>
              </v-row>
            </v-list-item-content>
          </v-list-item>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";

export default {
  name: "MemberList",
  data() {
    return {
      members: null,
      gpNo : 0,
      userId: 0,
      gpMgrId: 0
    };
  },
  created() {
    this.gpNo = this.$route.params.id;
    this.userId = this.$store.state.auth.userInfo.userId;
    this.getDetail()
  },
  computed: {
    ...mapState("auth", ["userInfo"])
  },
  methods: {
    async getDetail() {
      const apiUrl = "/study/user/" + this.gpNo;
      try {
        const res = await axios.get(apiUrl);
        this.members = JSON.parse(res.data.object).joinList[0]
        this.gpMgrId = JSON.parse(JSON.parse(res.data.object).group[0]).gpMgrId
      } catch (err) {
        this.$router.push("/main/");
        console.error(err);
      }
    },
    async kick(gpJoinNo) {
      const apiUrl = "/study/user/remove?no=" + gpJoinNo;
      try {
        const res = await axios.post(apiUrl)
        this.$emit('event')
      } catch (err) {
        console.error(err);
      }
    },
    async mand(gpNo, userId) {
      const apiUrl = "/study/user/mgr?no=" + gpNo + "&userId=" + userId;
      try {
        const res = await axios.post(apiUrl)
        this.$emit('event')
      } catch (err) {
        console.error(err);
      }
    },
  }
};
</script>
<style scoped>
button {
  height: 29px;
}
</style>
