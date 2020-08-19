<template>
  <v-container>
    <h2 class="text-center">{{userInfo.userNm}}님의 {{getToday.month}}월 공부기록</h2>
    <v-row justify="center" id="userpage" class="mx-5 my-5">
      <v-col cols="12" class="flex-column">
        <!-- <v-row class="d-flex align-center mr-2">
          <v-col class="d-flex flex-column justify-center">
            <v-row class="mb-5">
              <h1 style="color:black">
                '<strong>{{userInfo.userNm}}</strong>' 님의 공부 기록
              </h1>
            </v-row>
          </v-col>
        </v-row>
        <v-row class="d-flex flex-column mx-auto mt-5">
          <v-col cols="6" md="6" sm="12">
            <v-card class="mx-auto" height="180px">
              <v-card-title class="font-weight-bold">오늘의 공부시간</v-card-title>
              <v-row justify="center" align="center" class="m-5">
              <v-card-text class="text--primary headline text-center">{{todayStudyTime.tmAcmlTime}} / {{userInfo.userGoalHr}}시간</v-card-text>
              </v-row>
            </v-card>
          </v-col>
          <v-col cols="6" md="6" sm="12">
            <v-card class="mx-auto" height="180px">
              <v-card-title class="font-weight-bold">오늘의 목표 달성률</v-card-title>
              <v-row justify="center" align="center" class="m-5">
              <v-progress-circular
                :indeterminate="indeterminate"
                :rotate="270"
                :size="100"
                :value="todayStudyTime.tmAcmlTimeLong | toPercent(goalSecond)"
                :width="15"
                color="light-blue"
              >{{todayStudyTime.tmAcmlTimeLong | toPercent(goalSecond)}} % </v-progress-circular>
                </v-row>
            </v-card>
          </v-col>
        </v-row>-->
        <UserTodayTime />
        <v-col></v-col>
        <v-row>
          <v-col col="12">
            <h3
              class="text-center text-sm-left"
            >{{getToday.year}}년 {{getToday.month}}월의 공부시간 달성률</h3>
            <br />
            <template>
              <v-data-table
                :headers="headers"
                :items="dateTimer"
                hide-default-footer
                fixed-header
                class="elevation-1"
              >
                <template v-slot:item="dt">
                  <tr>
                    <td>{{dt.item.tmAcmlDate}}</td>
                    <td>{{dt.item.tmAcmlTime}}</td>
                    <td>
                      <v-progress-linear :value="dt.item.tmAcmlTimeLong | toPercent(goalSecond)"></v-progress-linear>
                      {{dt.item.tmAcmlTimeLong | toPercent(goalSecond)}} %
                    </td>
                  </tr>
                </template>
              </v-data-table>
            </template>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";
import UserTodayTime from "@/components/timer/UserTodayTime.vue";

export default {
  name: "UserTimer",
  components: {
    UserTodayTime
  },

  data() {
    return {
      headers: [
        { text: "일자", value: "tmAcmlDate" },
        { text: "공부시간", value: "tmAcmlTime" },
        { text: "목표 시간 달성률", sortable: false }
      ],
      userInfo: {},
      dateTimer: [],
      goalSecond: 0
    };
  },
  mounted() {
    this.getUserInfo();
    this.getUserTimer(this.getToday.year, this.getToday.month);
  },
  filters: {
    toPercent: (val, second) => {
      if (second == 0) return 0;
      const percent = Math.round((val / second) * 100, 0);

      return percent > 100 ? 100 : percent;
    }
  },
  computed: {
     getToday() {
      const date = new Date();
      const today = {
        date: String(date.getDate()).padStart(2, "0"),
        month: String(date.getMonth() + 1).padStart(2, "0"),
        year: date.getFullYear()
      };
      return today;
    },
  },
  methods: {
    getUserInfo() {
      axios.get("/user/").then(({ data }) => {
        this.userInfo = data.object;
        this.goalSecond = this.userInfo.userGoalHr * 60 * 60;
      });
    },
    async getUserTimer(year, month) {
      await axios.get("/timer/my/" + year + "/" + month).then(({ data }) => {
        this.dateTimer = data.object;
      });
    }
  }
};
</script>

<style scoped>
#userImg {
  display: block;
  /* width: 100%; */
  max-width: 160px;
  border-radius: 50%;
}

#userNm {
  min-width: 200px;
  max-width: 400px;
}

#goalHr {
  flex-basis: 100px;
}
</style>