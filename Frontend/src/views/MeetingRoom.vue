<template>
  <div>
    <h1>Meeting Room</h1>
    <div class="videos-container"></div>
    <v-btn @click="unmute">소리켜기</v-btn>
    <v-btn @click="mute">음소거</v-btn>
    <v-btn @click="onVideo">비디오켜기</v-btn>
    <v-btn @click="offVideo">비디오끄기</v-btn>
    <v-btn @click="outRoom" :to="{ name: 'StudyDetail' }">퇴장</v-btn>
  </div>
</template>
<script src="https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js"></script>
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>
<script>
import StudyDetailVue from "./StudyDetail.vue";
export default {
  name: "MeetingRoom",
  data() {
    return {
      roomid: "",
      connection: null
    };
  },
  mounted() {
    this.joinRoom();
  },
  methods: {
    joinRoom() {
      this.connection = new RTCMultiConnection();
      this.connection.session = {
        audio: true,
        video: true,
        data: true
      };

      // this.connection.socketURL = "http://i3b103.p.ssafy.io:9001/";
      this.connection.socketURL =
        "https://rtcmulticonnection.herokuapp.com:443/";

      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true
      };
      this.connection.openOrJoin("study-id");
      this.connection.videosContainer = document.querySelector(
        ".videos-container"
      );
    },
    outRoom() {
      this.connection.getAllParticipants().forEach(participantId => {
        this.connection.disconnectWith(participantId);
      });

      this.connection.attachStreams.forEach(function(localStream) {
        localStream.stop();
      });

      this.connection.closeSocket();
    },
    mute() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute("audio");
    },
    unmute() {
      let localStream = this.connection.attachStreams[0];
      localStream.unmute("audio");
      this.connection.streamEvents.selectFirst(
        "local"
      ).mediaElement.muted = true;
    },
    offVideo() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute("video");
    },
    onVideo() {
      this.connection.session.video = true;
      let localStream = this.connection.attachStreams[0];
      localStream.unmute("video");
    }
  },
  destroyed() {
    this.outRoom();
  }
};
</script>

<style>
</style>