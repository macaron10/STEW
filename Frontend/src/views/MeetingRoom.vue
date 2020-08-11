<template>
  <div>
    <v-row class="mx-10 my-5">
      <v-col cols="9">
        <Timer />
        <div class="mx-10 my-10 videos-container" ></div>
      </v-col>
      <v-col cols="3">
        <Chat :roomid="roomid"/>
      </v-col>
          
    </v-row>
    <!-- footer -->
    <v-footer color="#ffffff" padless>
      <v-row justify="center" no-gutters>
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
    </v-footer>
  </div>
</template>
<script src="https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js"></script>
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>
<script>
import StudyDetailVue from "./StudyDetail.vue";
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
      localStream: {}
    };
  },
  components: {
    Timer,
    Chat
  }, 
  created() {
    this.joinRoom();
  },
  mounted() {
    this.check();
    this.initoptions();
    this.getRoodId();
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
          width: 400,
          height: 300
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
    getRoodId() {
      this.roomid = this.$route.params.id
      console.log(this.roomid, '설정!')
    }
  },
  destroyed() {
    this.outRoom();
  }
};
</script>

<style>
video::-webkit-media-controls {
  display: none;
}
</style>