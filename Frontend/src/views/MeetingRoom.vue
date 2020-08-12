<template>
  <div>
    <RoomNavbar />
    <v-row class="d-flex align-center">
      <v-col cols="9">
        <div class=" videos-container" ></div>
        <h1>비디오의 상태 {{options.video}}</h1>
        <!-- footer -->
        <v-row class="footer" justify="center" no-gutters>
          <v-btn class="mx-1" fab dark color="indigo" @click="unmute">
            <v-icon dark>mdi-volume-high</v-icon>
          </v-btn>
          <v-btn class="mx-1" fab dark color="blue lighten-1" @click="mute">
            <v-icon dark>mdi-volume-off</v-icon>
          </v-btn>
          <v-btn class="mx-1" fab dark color="teal lighten-1" @click="onVideo">
            <v-icon dark>mdi-video</v-icon>
          </v-btn>
          <v-btn class="mx-1" fab dark color="teal accent-1" @click="offVideo">
            <v-icon dark>mdi-video-off</v-icon>
          </v-btn>
          <v-btn class="mx-1" fab dark color="red" @click="outRoom" :to="{ name: 'StudyDetail' }">
            <v-icon dark>mdi-account-arrow-right-outline</v-icon>
          </v-btn>
        </v-row>
      </v-col>
      <v-col cols="3">
        <Timer />
        <Chat :roomid="roomid"/>
      </v-col>
    </v-row>
  </div>
</template>
<script src="https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js"></script>
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>
<script>
import StudyDetailVue from "./StudyDetail.vue";
import RoomNavbar from "@/components/room/RoomNavbar.vue";
import Timer from "@/components/temp/Timer.vue";
// import Chatting from "@/components/room/Chatting.vue";
import Chat from "@/components/chat/Chat.vue";
import { log } from "util";

export default {
  name: "MeetingRoom",
  data() {
    return {
      roomid: "",
      connection: null,
      options: {
        video: false,
        audio: false
      },
      localStream: {},
      videos: {}
    };
  },
  components: {
    RoomNavbar,
    Timer,
    Chat,
  }, 
  created() {
    this.joinRoom();
  },
  mounted() {
    this.$store.state.comm.onMeeting = false;
    this.check();
    this.initoptions();
  },
  methods: {
    check() {
      alert(
        "비디오와 오디오가 켜집니다. 접속 후 오디오와 비디오 기능을 비활성화 시킬 수 있습니다."
      );
    },
    initoptions() {
      this.connection.videosContainer = document.querySelector(".videos-container");
      this.options = this.$route.params.options;

      let localStream = {};
      localStream = this.connection;
      localStream = localStream;
      console.log(this.connection.attachStreams);
      console.log(this.connection.attachStreams[0]);
      if (this.options.audio == false) {
        // localStream.attachStreams[0].mute("video");
      }
      if (this.options.video == false) {
        // localStream.attachStreams[0].mute("audio");
      }
    },
    joinRoom() {
      this.connection = new RTCMultiConnection();
      this.connection.session = {
        audio: true,
        video: true,
        data: true
      };

      // this.connection.socketURL = "https://i3b103.p.ssafy.io/soket/"; //배포옹
      this.connection.socketURL =
        "https://rtcmulticonnection.herokuapp.com:443/"; // 개발용

      this.connection.mediaConstraints = {
        audio: true,
        video: {
        }
      };
      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true
      };
      this.connection.openOrJoin(this.$route.params.id);
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
      console.log("mute");
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
    },
    // getvideos() {
    //   document.getElementsByName('video')
    // }
  },
  destroyed() {
    this.$store.state.comm.onMeeting = true;
    this.outRoom();
  }
};
</script>

<style>
video::-webkit-media-controls {
  display: none;
}
.videos-container {
  display: grid;
  /* grid-template-rows: repeat(3, 100px); */
  /* grid-template-columns: repeat(2, calc((100vw - 400px)/2.5) ); */
  grid-template-columns: repeat(3, 23.5vw);
}

.videos-container video{
  width: 23.5vw;

  /* width: calc((100vw - 400px)/2.5); */
  border: solid;
}

.footer {
  position: fixed;
  bottom: 10px;
  left: 25vw;
}
</style>