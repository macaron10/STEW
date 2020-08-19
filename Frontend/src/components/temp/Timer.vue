<template>
  <div>
    <div class="btn-container float-right text-no-wrap" style="width: 230px">
      <v-row>
        <v-icon>mdi-timer</v-icon>
        <span class="py-auto my-auto mx-auto time">{{ time }}</span>
        <v-btn class="mx-2 my-3" text icon color="blue lighten-2" v-if="!running" @click="start">
          <v-icon>mdi-play</v-icon>
        </v-btn>
        <v-btn class="mx-2 my-3" text icon color="grey lighten-2" v-if="running" @click="stop">
          <v-icon>mdi-pause</v-icon>
        </v-btn>
        <v-btn class="my-3" text icon color="red lighten-2" v-if="timeBegan" @click="end">
          <v-icon>mdi-stop</v-icon>
        </v-btn>
      </v-row>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { stringify } from "querystring";
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
      running: false,
      groupNo: 0
    };
  },
  computed: {
    secondTime() {
      const hours = Number(this.time.slice(0, 2));
      const minutes = Number(this.time.slice(3, 5));
      const seconds = Number(this.time.slice(6, 8));
      return hours * 3600 + minutes * 60 + seconds;
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
      const config = {
        gpNo: this.groupNo,
        tmAcmlTime: this.secondTime
      };
      console.log(config.gpNo)
      axios
        .post("/timer", stringify(config))
        .then((res) => (console.log(res)))
        .catch(err => console.log(err));
      this.reset();
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
  },
  mounted() {
    this.groupNo = this.$route.params.id
  },
  destroyed() {
    this.end();
  }
};
</script>
