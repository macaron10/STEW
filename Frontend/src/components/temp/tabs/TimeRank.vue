<template>
  <div>
    <v-container>
      <div v-if="studyTotal!=='00:00:00'">
        <h2 class="text-center">{{nowMonth}}월 스터디 누적 공부량</h2>
        <h2
          class="text-center"
        >{{studyTotal.slice(0,2)}}시간 {{studyTotal.slice(3,5)}}분 {{studyTotal.slice(6,8)}}초</h2>
        <PieChart class="mx-auto" :pie-data="pieData" />

        <v-simple-table>
          <template v-slot:default>
            <thead>
              <tr>
                <th>순위</th>
                <th class="text-left">이름</th>
                <th class="text-left">{{nowMonth}}월 누적 공부시간</th>
                <th class="text-left d-sm-table-cell d-none">일평균 공부시간/목표시간</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, idx) in rankData" :key="item.user.userid">
                <td v-if="idx < 6">{{idx+1}}</td>
                <td v-if="idx < 6" class="d-flex">
                  <img
                    :src="$store.state.comm.baseUrl+'/image/user'+item.user.userImg"
                    class="d-sm-flex rounded-circle my-auto d-none"
                    style="width:35px; height:35px"
                    alt="profile"
                  />
                  <p class="d-flex my-auto ml-2">{{ item.user.userNm }}</p>
                </td>
                <td v-if="idx < 6">{{item.tmAcmlTime.slice(0,2)}}시간 {{item.tmAcmlTime.slice(3,5)}}분 {{item.tmAcmlTime.slice(6,8)}}초</td>
                <td v-if="idx < 6" class="d-sm-table-cell d-none">
                  {{parseInt(parseInt(item.tmAcmlTimeLong/(new Date().getDate())) / 3600)}}시간
                  {{parseInt((parseInt(item.tmAcmlTimeLong/(new Date().getDate()))%3600)/60)}}분 / {{item.user.userGoalHr}}시간
                </td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </div>
      <div v-else>
        <h3 class="text-center">아직 이번달 공부기록이 없네요 :(</h3>
        <h3 class="text-center">캠스터디에 입장하여 공부해 보세요!</h3>
      </div>
    </v-container>
  </div>
</template>

<script>
// @ is an alias to /src
import axios from "axios";
import PieChart from "./pieChart";
export default {
  name: "TimeRank",
  data() {
    return {
      gpNo: null,
      studyTotal: "00:00:00",
      rankData: null,
      pieData: [],
      colors: ["#0277BD", "#00BFA5", "#B2EBF2", "#FFE082", "#FB8C00", "#E53935"]
    };
  },
  mounted() {
    this.gpNo = this.$route.params.id;
    this.getStudyTotal();
    this.getStduyRank();
  },
  computed: {
    nowMonth() {
      return new Date().getMonth() + 1;
    }
  },
  components: { PieChart },
  methods: {
    getStudyTotal() {
      axios
        .get(`/timer/${this.gpNo}/${new Date().getFullYear()}`)
        .then(res => {
          if (res.data.object === []) {
            return;
          } else {
            const year = new Date().getFullYear();
            const mon = new Date().getMonth() + 1;
            const month = String(mon).length === 2 ? mon : `0${mon}`;
            for (const i of res.data.object) {
              if (i.tmAcmlDate === `${year}-${month}`) {
                this.studyTotal = i.tmAcmlTime;
                return;
              }
            }
          }
        })
        .catch(err => console.log(err));
    },
    getStduyRank() {
      axios
        .get(
          `/timer/user/${
            this.gpNo
          }/${new Date().getFullYear()}/${new Date().getMonth() + 1}/`
        )
        .then(res => {
          this.rankData = res.data.object;
          const totalTime =
            Number(this.studyTotal.slice(0, 2)) * 3600 +
            Number(this.studyTotal.slice(3, 5)) * 60 +
            Number(this.studyTotal.slice(6, 8));
          for (const idx in this.rankData) {
            const time = this.rankData[idx].tmAcmlTime;
            const studyTime =
              Number(time.slice(0, 2)) * 3600 +
              Number(time.slice(3, 5)) * 60 +
              Number(time.slice(6, 8));
            if (this.pieData.length == 6) {
              break;
            } else {
              this.pieData.push({
                color: this.colors[Number(idx)],
                value: totalTime ? (studyTime / totalTime) * 100 : 0
              });
            }
          }
        })
        .catch(err => console.log(err));
    }
  }
};
</script>
<style scoped>
.pie-chart {
  width: 180px;
  height: 180px;
}
</style>
