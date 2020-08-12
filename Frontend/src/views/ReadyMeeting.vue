<template>
  <div>
    <v-container>
      <v-layout text-center wrap>
        <div style="background:black; width:100%; height:30em;" class="mt-5">
          <div></div><video
            autoplay
            playsinline
            ref="video"
            id="video"
            style="width:100%; height:100%;"
          ></video>
        </div>
      </v-layout>
    </v-container>

    <v-footer color="#ffffff" padless>
      <v-row justify="center" no-gutters>
        <v-btn v-if="options.audio" class="mx-1" fab dark color="#64B5F6" @click="mute">
          <v-icon dark>mdi-volume-high</v-icon>
        </v-btn>
        <v-btn v-else class="mx-1" fab outlined dark color="#FB8C00" @click="unmute">
          <v-icon dark>mdi-volume-off</v-icon>
        </v-btn>

        <v-btn v-if="options.video" class="mx-1" fab dark color="#7CB342" @click="offVideo">
          <v-icon dark>mdi-video</v-icon>
        </v-btn>
        <v-btn v-else class="mx-1" fab outlined dark color="#FF8A65" @click="onVideo">
          <v-icon dark>mdi-video-off</v-icon>
        </v-btn>
      </v-row>

      <v-btn class="ma-2" tile outlined color="success" @click="enterMeetingRoom()">
        <v-icon left>mdi-play</v-icon>JOIN
      </v-btn>
    </v-footer>
  </div>
</template>

<script>
import adapter from "webrtc-adapter";

export default {
  name: "ReadyMeeting",
  data: () => ({
    gpNo: "",
    options: {
      video: false,
      audio: false
    },
    video: {},
    localstream: {}
  }),
  mounted() {
    this.gpNo = this.$route.params.id;
    this.video = this.$refs.video;
  },
  methods: {
    enterMeetingRoom() {
      this.$router.push({
        name: "MeetingRoom",
        params: { id: this.gpNo, options: this.options }
      });
    },
    mute() {
      this.options.audio = false;
    },
    unmute() {
      this.options.audio = true;
    },
    offVideo() {
      this.options.video = false;

      this.video.pause();
      this.video.src = "";
      this.localstream.getTracks().forEach(elem => {
        console.log(elem);
        elem.stop();
      });
    },
    onVideo() {
      this.options.video = true;

      if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        navigator.mediaDevices
          .getUserMedia({ video: true })
          .then(stream => {
            this.video.srcObject = stream;
            this.localstream = stream;

            this.video.play();
          })
          .catch(error => {
            console.log("navigator.getUserMedia error: ", error);
          });
      }
    }
  }
};
</script>

<style>
</style>