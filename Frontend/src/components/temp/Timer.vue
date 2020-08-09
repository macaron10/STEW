<template>
  <div id="clock">
    <span class="time">{{ time }}</span>
    <div class="btn-container">
      <v-btn v-if="!running" @click="start"><span v-if="timeBegan">재</span>시작</v-btn>
      <v-btn v-if="running" @click="stop">일시정지</v-btn>
      <v-btn v-if="timeBegan" @click="end">종료</v-btn>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { stringify } from 'querystring';
// @ is an alias to /src
export default {
  name: "template",
  components: {},
  data() {
    return {
      time: "00:00:00",
      timeBegan: null,
      timeStopped: null,
      stoppedDuration: 0,
      started: null,
      running: false
    };
  },
  computed: {
    secondTime() {
      const hours = Number(this.time.slice(0, 2))
      console.log(hours)
      const minutes = Number(this.time.slice(3, 5))
      console.log(minutes)
      const seconds = Number(this.time.slice(7, 8))
      console.log(seconds)
      return hours*3600 + minutes*60 + seconds

    }
  
  },
  methods: {
    start() {
      if (this.running) {
        return;
      }
      if (this.timeBegan === null) {
        // reset();
        this.timeBegan = new Date();
      }

      if (this.timeStopped !== null) {
        this.stoppedDuration += new Date() - this.timeStopped;
      }

      this.started = setInterval(this.clockRunning, 10);
      this.running = true;
    },
    stop() {
      this.running = false;
      this.timeStopped = new Date();
      clearInterval(this.started);
    },
    end() {
      const answer = confirm("현재까지의 공부시간이 누적되고 초기화 됩니다. 초기화 하시겠습니까?")
      if (answer) {
        const config = {
          gpNo : Number(this.$route.params.id),
          tmAcmlTime : this.secondTime
        }
        axios
          .post("/timer", stringify(config))
          .then(res => console.log(res))
          .catch(err => console.log(err))
        this.reset()
      }
    },
    clockRunning() {
      const currentTime = new Date(),
        timeElapsed = new Date(
          currentTime - this.timeBegan - this.stoppedDuration
        ),
        hour = timeElapsed.getUTCHours(),
        min = timeElapsed.getUTCMinutes(),
        sec = timeElapsed.getUTCSeconds();

      this.time =
        this.zeroPrefix(hour, 2) +
        ":" +
        this.zeroPrefix(min, 2) +
        ":" +
        this.zeroPrefix(sec, 2);
    },
    zeroPrefix(num, digit) {
      let zero = "";
      for (let i = 0; i < digit; i++) {
        zero += "0";
      }
      return (zero + num).slice(-digit);
    },
    reset() {
      this.running = false;
      clearInterval(this.started);
      this.stoppedDuration = 0;
      this.timeBegan = null;
      this.timeStopped = null;
      this.time = "00:00:00";
    }
  }
};
</script>
