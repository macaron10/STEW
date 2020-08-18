<template>
  <div class="ready-meeting-room">
    <!-- <h1 class="mt-5 text-center">화면과 마이크를 테스트해주세요</h1> -->
    <v-container>
      <v-layout text-center wrap>
        <div style="background:black; width: 80%; height:25em;" class="mt-5 mx-auto">
          <div></div>
          <video autoplay playsinline ref="video" id="video" style="width:100%; height:100%;"></video>
        </div>
      </v-layout>
    </v-container>

    <v-footer color="#ffffff" padless>
      <v-row class="d-flex justify-center" no-gutters>
        <v-btn v-if="options.audio" class="mr-2" fab dark color="#FB8C00" @click="mute">
          <v-icon dark>mdi-volume-high</v-icon>
        </v-btn>
        <v-btn v-else class="mr-2" fab outlined dark color="#FB8C00" @click="unmute">
          <v-icon dark>mdi-volume-off</v-icon>
        </v-btn>
        <v-btn v-if="options.video" class="mx-1" fab dark color="#FF8A65" @click="offVideo">
          <v-icon dark>mdi-video</v-icon>
        </v-btn>
        <v-btn v-else class="mx-1" fab outlined dark color="#FF8A65" @click="onVideo">
          <v-icon dark>mdi-video-off</v-icon>
        </v-btn>
      </v-row>
      <v-btn
        absolute
        right
        class="float-right ma-2"
        tile
        outlined
        color="success"
        @click="enterMeetingRoom()"
      >
        <v-icon left>mdi-play</v-icon>입장하기
      </v-btn>
    </v-footer>
  </div>
</template>

<script>
import adapter from "webrtc-adapter";

export default {
  name: "ReadyMeeting",
  components: {},
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
  destroyed() {
    if (this.localstream.getTracks===undefined) {
      return
    }
    this.localstream.getTracks().forEach(elem => {
      elem.stop();
    });
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