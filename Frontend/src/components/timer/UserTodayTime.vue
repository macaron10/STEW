<template>
  <div>
    <v-row class="d-flex flex-row mx-1">
      <v-col sm="6" xs="12">
        <v-card class="mx-1" height="180px">
          <v-card-title class="font-weight-bold">오늘의 공부시간</v-card-title>
          <v-row justify="center" align="center" class="m-5">
            <v-card-text
              class="text--primary headline text-center"
            >{{todayStudyTime.tmAcmlTime}} / {{userInfo.userGoalHr}}시간</v-card-text>
          </v-row>
        </v-card>
      </v-col>
      <v-col  sm="6" xs="12">
        <v-card class="mx-1" height="180px">
          <v-card-title class="font-weight-bold">오늘의 목표 달성률</v-card-title>
          <v-row justify="center" align="center" class="m-5">
            <v-progress-circular
              :indeterminate="indeterminate"
              :rotate="270"
              :size="100"
              :value="todayStudyTime.tmAcmlTimeLong | toPercent(goalSecond)"
              :width="15"
              color="light-blue"
            >{{todayStudyTime.tmAcmlTimeLong | toPercent(goalSecond)}} %</v-progress-circular>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      userInfo: {},
      todayStudyTime: {
        tmAcmlDate: "",
        tmAcmlTime: "00:00:00",
        tmAcmlTimeLong: 0
      },
      goalSecond: 0
    };
  },
  filters: {
    toPercent: (val, second) => {
      if (second == 0) return 0;
      const percent = Math.round((val / second) * 100, 0);

      return percent > 100 ? 100 : percent;
    }
  },
  mounted(){
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      axios.get("/user/").then(({ data }) => {
        this.userInfo = data.object;
        this.goalSecond = this.userInfo.userGoalHr * 60 * 60;
      });
    },
    initTodayStudy() {
      axios.get("/timer/today").then(({ data }) => {
        this.todayStudyTime = data.object;
        console.log(this.todayStudyTime);
      });
    }
  }
};
</script>
