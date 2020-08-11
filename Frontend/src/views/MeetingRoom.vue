<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="8">
          <Timer />
          <div class="mx-10 my-10 videos-container" ></div>
        </v-col>
        <v-col cols="4">
          <Chatting :roomid="roomid"/>
        </v-col>
            
      </v-row>
    </v-container>
    <!-- footer -->
    <v-footer color="#ffffff" padless>
      <v-row justify="center" no-gutters>
        <v-btn v-if="options.audio" class="mx-1"  fab dark color="#64B5F6" @click="mute">
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
import Chatting from "@/components/room/Chatting.vue";
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
    Chatting
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
      // if (this.options.audio == false) {
      //   this.mute();
      // }
      // if (this.options.video == false) {
      //   this.offVideo();
      // }
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
      this.options.audio = false;
    },
    unmute() {
      let localStream = this.connection.attachStreams[0];
      localStream.unmute("audio");
      this.connection.streamEvents.selectFirst(
        "local"
      ).mediaElement.muted = true;
      this.options.audio = true;
    },
    offVideo() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute("video");
      this.options.video = false;
    },
    onVideo() {
      this.connection.session.video = true;
      let localStream = this.connection.attachStreams[0];
      localStream.unmute("video");
      this.options.video = true;
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