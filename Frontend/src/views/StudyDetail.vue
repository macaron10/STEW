<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="mb-6">
          <div class="d-flex justify-center">
            <img :src="$store.state.baseUrl + '/group' + group.gpImg" alt="그룹 이미지" height="400px" />
          </div>
          <div class="text-center">
            <h1 class="my-3">{{ group.gpNm }}</h1>
            <h4 class="mb-2">{{ group.gpIntro }}</h4>
            <v-btn
              @click="readyEnterMeeting(group.gpNo)"
              color="green accent-1"
              class="white--text font-weight-bold mx-3"
            >스터디 캠 입장</v-btn>
            <v-btn
              :to="{ name:'StudyUpdate', params: { id: id }}"
              color="blue accent-1"
              class="white--text font-weight-bold mx-3"
              v-if="group.gpMgrId===userId"
            >수정</v-btn>
            <v-btn
              color="red accent-1"
              class="white--text font-weight-bold"
              @click="quitGroup(group.gpNo)"
            >탈퇴</v-btn>
          </div>
        </v-col>
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
    TimeRank,
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
      userId: 1
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.getDetail();
  },
  methods: {
    readyEnterMeeting(gpNo) {      
      this.$router.push({name:'ReadyMeeting', params:{id: gpNo}})
    },
    async getDetail() {
      const apiUrl = "/study/user/" + this.id;
      try {
        const res = await axios.get(apiUrl);
        this.group = JSON.parse(res.data.object).group;
        this.group = JSON.parse(this.group);
        this.membersData = JSON.parse(res.data.object).joinList;
        console.log(this.group);
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
