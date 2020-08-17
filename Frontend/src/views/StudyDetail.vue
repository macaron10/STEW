<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="10" offset="1" sm="8" offset-sm="2" md="6" offset-md="0">
          <div class="d-flex justify-center ma-6" style="position: relative">
            <v-img
              v-if="group.gpImg"
              :src="$store.state.comm.baseUrl + '/image/group' + group.gpImg"
              alt="그룹 이미지"
              width="90%"
              color="white"
              gradient="to bottom, rgba(255,255,255,0), rgba(0,0,0,.1)"
            />
            <v-img
              v-else
              :src="$store.state.comm.baseUrl + '/image/group/default.png'"
              alt="그룹이미지"
              width="90%"
              color="white"
              gradient="to bottom, rgba(255,255,255,0), rgba(0,0,0,.1)"
            />
            <v-btn
              absolute
              dark
              fab
              bottom
              right
              color="amber darken-1"
              class="mb-10"
              @click="readyEnterMeeting(group.gpNo)"
            >
              <v-icon>mdi-video</v-icon>
            </v-btn>
          </div>
          <h2 class="mb-3 d-inline">
            {{ group.gpNm }}
            <v-btn
              icon
              class="ml-5"
              color="grey darken-2"
              :to="{ name:'StudyUpdate', params: { id: id }}"
              v-if="group.gpMgrId===userId"
            >
              <v-icon>mdi-settings</v-icon>
            </v-btn>
          </h2>
          <h4>
            <span v-if="group.gpPublic">
              <v-icon>mdi-lock-open-outline</v-icon>공개
            </span>
            <span v-else>
              <v-icon>mdi-lock-outline</v-icon>비공개
            </span>
            그룹 · 멤버 {{group.gpCurNum}}명
          </h4>
        </v-col>
        <v-col cols="10" offset="1" md="6" offset-md="0">
          <v-card>
            <v-tabs
              v-model="tab"
              :grow="true"
              :height="55"
              color="blue"
              :slider-size="3"
              :elevation="0"
            >
              <v-tab v-for="item in items" :key="item.no">
                <v-icon>{{ item.icon }}</v-icon>
                <pre></pre>
                <span>{{ item.tab }}</span>
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
                    :key="componentKey"
                    @event="forceRerender()"
                  />
                  <TimeRank v-else />
                </v-card>
              </v-tab-item>
            </v-tabs-items>
          </v-card>
        </v-col>
      </v-row>

      <v-row>
        <v-col class="text-right">
          <span class="text-decoration-underline">스터디를 탈퇴하시겠습니까?</span>
          <v-tooltip top>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                icon
                v-bind="attrs"
                v-on="on"
                dark
                color="red accent-1"
                @click="quitGroup(group.gpNo)"
              >
                <v-icon>mdi-account-arrow-right-outline</v-icon>
              </v-btn>
            </template>
            <span>그룹 탈퇴하기</span>
          </v-tooltip>
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
      componentKey: 0,
      group: [],
      membersData: [],
      // 밑에는 그룹정보(나중에 활용)
      tab: null,
      items: [
        { tab: "스터디 일정", icon: "mdi-calendar", no: "1" },
        { tab: "스터디 멤버", icon: "mdi-account-group", no: "2" },
        { tab: "이번달 랭킹", icon: "mdi-timer", no: "3" }
      ],
      id: null,
      userId: 0
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.userId = this.$store.state.auth.userInfo.userId;
    this.getDetail();
    this.$vuetify.goTo(0, {
        duration: 100,
        offset: 0
      })
  },
  methods: {
    forceRerender() {
      this.componentKey += 1;
    },
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
      const answer = confirm("정말 탈퇴하시겠습니까?");
      if (answer) {
        try {
          const res = await axios.post(apiUrl);
          if (res.data.msg === "success") {
            this.$router.push("/main/");
          } else if (res.data.msg === "매니저 탈퇴 불가") {
            alert("매니저는 탈퇴가 불가능합니다.");
          }
        } catch (err) {
          console.error(err);
        }
      }
    }
  }
};
</script>

<style scoped>
.v-item-group {
  min-height: 688px;
}
.v-slide-group__prev {
  display: none !important;
}
</style>
