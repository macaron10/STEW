<template>
  <div>
    <span>이번달 우리 스터디의 공부량은 {{studyTotal}}입니다</span>
  </div>
</template>

<script>
// @ is an alias to /src
import axios from "axios";
export default {
  name: "TimeRank",
  data() {
    return {
      gpNo: null,
      studyTotal: "00:00:00"
    };
  },
  mounted() {
    this.gpNo = this.$route.params.id;
    this.getStudyTotal();
    this.getStduyRank()
  },
  components: {},
  methods: {
    getStudyTotal() {
      axios
        .get(`/timer/${this.gpNo}/${new Date().getFullYear()}`)
        .then(res => {
          if (res.data.object === []) {
            return;
          } else {
            const year = new Date().getFullYear();
            const mon = new Date().getMonth() + 1
            const month = String(mon).length === 2 ? mon : `0${mon}`
            for (const i of res.data.object) {
              if (i.tmAcmlDate === `${year}-${month}`) {
                this.studyTotal = i.tmAcmlTime
                return
              }
            }
          }
        })
        .catch(err => console.log(err));
    },
    getStduyRank() {
      axios
        .get(`/timer/user/${this.gpNo}/${new Date().getFullYear()}/${new Date().getMonth()+1}/`)
        .then(res => {
          console.log(res)
        })
        .catch(err => console.log(err))
    }
  },
};
</script>
