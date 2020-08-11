<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="10" offset="1">
          <div class="d-flex justify-center">
            <img
              v-if="group.gpImg"
              :src="$store.state.comm.baseUrl + '/image/group' + group.gpImg"
              alt="그룹 이미지"
              width="100%"  
            />
            <img
              v-else
              :src="$store.state.comm.baseUrl + '/image/group/default.png'"
              alt="그룹이미지"
              width="100%"
            />
          </div>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="10" sm="5" offset="1">
          <h2 class="mb-3">
            {{ group.gpNm }}
            <v-btn
              icon
              :to="{ name:'StudyUpdate', params: { id: id }}"
              v-if="group.gpMgrId===userId"
            >
              <v-icon>mdi-cogs</v-icon>
            </v-btn>
          </h2>
          <h4>
            <span v-if="group.gpPublic">
              <v-icon>mdi-lock-open-outline</v-icon>공개
            </span>
            <span v-else>
              <v-icon color="blue accent-1">mdi-lock-outline</v-icon>비공개
            </span>
            그룹 · 멤버 {{group.gpCurNum}}명
          </h4>
        </v-col>
        <v-col>
          <v-btn
            @click="readyEnterMeeting(group.gpNo)"
            color="green accent-1"
            class="white--text font-weight-bold mx-3"
          >스터디 캠 입장</v-btn>
          <v-btn
            color="red accent-1"
            class="white--text font-weight-bold"
            @click="quitGroup(group.gpNo)"
          >탈퇴</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col class="mb-6">
          <v-card>
            <v-tabs
              v-model="tab"
              background-color="blue lighten-1"
              :grow="true"
              dark
              :icons-and-text="true"
            >
              <v-tab v-for="item in items" :key="item.no">
                {{ item.tab }}
                <v-icon>{{ item.icon }}</v-icon>
              </v-tab>
            </v-tabs>

            <v-tabs-items v-model="tab">
              <v-tab-item v-for="item in items" :key="item.tab">
                <v-card flat>
                  <Calendar v-if="item.no==='1'" />
                  <MemberList
                    v-else-if="item.no==='2'"
                    :members="membersData"
                    :gpMgrId="group.gpMgrId"
                    :gpNo="group.gpNo"
                  />
                  <TimeRank v-else />
                </v-card>
              </v-tab-item>
            </v-tabs-items>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Calendar from "@/components/temp/Calendar.vue";
import MemberList from "@/components/temp/tabs/MemberList.vue";
import TimeRank from "@/components/temp/tabs/TimeRank.vue";
import axios from "axios";
// @ is an alias to /src
export default {
  name: "StudyDetail",
  components: {
    Calendar,
    MemberList,
    TimeRank
  },
  data() {
    return {
      group: [],
      membersData: [],
      // 밑에는 그룹정보(나중에 활용)
      tab: null,
      items: [
        { tab: "스터디 일정", icon: "mdi-calendar", no: "1" },
        { tab: "스터디 그룹 멤버", icon: "mdi-account-group", no: "2" },
        { tab: "이번달 공부 랭킹", icon: "mdi-timer", no: "3" }
      ],
      id: null,
      userId: 0
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.userId = this.$store.state.auth.userInfo.userId;
    this.getDetail();
  },
  methods: {
    readyEnterMeeting(gpNo) {
      this.$router.push({ name: "ReadyMeeting", params: { id: gpNo } });
    },
    async getDetail() {
      const apiUrl = "/study/user/" + this.id;
      try {
        const res = await axios.get(apiUrl);
        this.group = JSON.parse(res.data.object).group;
        this.group = JSON.parse(this.group);
        this.membersData = JSON.parse(res.data.object).joinList;
      } catch (err) {
        this.$router.push("/main/");
        console.error(err);
      }
    },
    async quitGroup(gpNo) {
      const apiUrl = "/study/user/exit?no=" + gpNo;
      try {
        const res = await axios.post(apiUrl);
        this.$router.push("/main/");
      } catch (err) {
        console.error(err);
      }
    }
  }
};
</script>

<style scoped>
</style>
