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
      //DB에 보내는 axios
      this.reset()
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
