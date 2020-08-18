<template>
  <div>
    <v-row class="d-flex mx-auto">
      <v-col cols="12" sm="6">
        <v-card class="mx-auto" height="180px">
          <v-card-title class="d-block font-weight-bold text-center">오늘의 공부시간</v-card-title>
          <v-card-text
            class="text--primary headline text-center text-subtitle-1 text-sm-h5 mt-7"
          >{{todayStudyTime.tmAcmlTime}} / {{userInfo.userGoalHr}}시간</v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="6">
        <v-card class="mx-auto" height="180px">
          <v-card-title class="d-block font-weight-bold text-center">오늘의 목표 달성률</v-card-title>
          <div class="text-center">
            <v-progress-circular
              :indeterminate="false"
              :rotate="270"
              :size="100"
              :value="todayStudyTime.tmAcmlTimeLong | toPercent(goalSecond)"
              :width="15"
              color="light-blue"
            >{{todayStudyTime.tmAcmlTimeLong | toPercent(goalSecond)}} %</v-progress-circular>
          </div>
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
  mounted() {
    this.getUserInfo();
    this.initTodayStudy()
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
      });
    }
  }
};
</script>
