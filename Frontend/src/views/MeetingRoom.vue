<template>
  <div class="meeting-room">
    <RoomNavbar />
    <v-row>
      <v-col class="py-0" cols="9">
        <div class="videos-container"></div>
        <!-- <h1>비디오의 상태 {{options.video}}</h1> -->
        <!-- footer -->
        <v-row class="footer" justify="center" no-gutters>
          <v-btn v-if="options.audio" class="mx-1"  fab dark color="#FB8C00" @click="mute">
            <v-icon dark>mdi-volume-high</v-icon>
          </v-btn>
          <v-btn v-else class="mx-1" fab outlined dark color="#FB8C00" @click="unmute">
            <v-icon dark>mdi-volume-off</v-icon>
          </v-btn>
          <v-btn v-if="options.video" class="mx-1" fab dark color="#FF8A65" @click="offVideo">
            <v-icon dark>mdi-video</v-icon>
          </v-btn>
          <v-btn v-else class="mx-1" fab outlined dark color="#FF8A65" @click="onVideo">
            <v-icon dark>mdi-video-off</v-icon>
          </v-btn>
          <v-btn class="mx-1" fab dark color="red" @click="outRoom" :to="{ name: 'StudyDetail' }">
            <v-icon dark>mdi-account-arrow-right-outline</v-icon>
          </v-btn>
        </v-row>
      </v-col>
      <v-col class="py-0 pl-0 chatroom" cols="3" height="92vh">
        <Chat :roomid="roomid" />
      </v-col>
    </v-row>
  </div>
</template>
<script src="https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js"></script>
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>
<script>
import StudyDetailVue from "./StudyDetail.vue";
import RoomNavbar from "@/components/room/RoomNavbar.vue";

import Chatting from "@/components/room/Chatting.vue";
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
    Chat
  },
  created() {
    this.joinRoom();
  },
  mounted() {
    this.$store.state.sg.onMeeting = false;

    this.options = this.$route.params.options;
    console.log(this.options);
    this.check();

    this.initoptions();
  },
  methods: {
    check() {
      alert(
        "현재 설정으로 미팅룸에 접속합니다. 접속 후 오디오와 비디오 기능을 재설정할 수 있습니다."
      );
    },
    initoptions() {
      this.connection.videosContainer = document.querySelector(
        ".videos-container"
      );
    },
    joinRoom() {
      this.connection = new RTCMultiConnection();
      this.connection.session = {
        audio: true,
        video: true,
        data: true
      };

      //this.connection.socketURL = "https://i3b103.p.ssafy.io/socket/"; //배포옹
       this.connection.socketURL = "https://rtcmulticonnection.herokuapp.com:443/"; // 개발용

      this.connection.mediaConstraints = {
        audio: true,
        video: {}
      };
      this.connection.sdpConstraints.mandatory = {
        OfferToReceiveAudio: true,
        OfferToReceiveVideo: true
      };
      this.connection.openOrJoin(`stew${this.$route.params.id}ssafy3`)
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
      console.log(localStream);
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
    handleOnsream(e) {
      var video = document.createElement("video");
      try {
        video.setAttributeNode(document.createAttribute("autoplay"));
        video.setAttributeNode(document.createAttribute("playsinline"));
      } catch (e) {
        video.setAttribute("autoplay", true);
        video.setAttribute("playsinline", true);
      }
      video.srcObject = e.stream;
    }
    // getvideos() {
    //   document.getElementsByName('video')
    // }
  },
  destroyed() {
    this.$store.state.sg.onMeeting = true;
    this.outRoom();
  }
};
</script>

<style>
.meeting-room {
  background-color: #5f5f5f;
}

.chatroom {
  height: 92vh;
  background-color: #fff;
}

video::-webkit-media-controls {
  display: none;
}
.videos-container {
  display: grid;

  /* grid-template-rows: ; */
  /* grid-template-columns: repeat(2, calc((100vw - 400px)/2.5) ); */
  grid-template-columns: repeat(3, 23.7vw);
}

.videos-container video {
  display: block;
  width: 23.7vw;
  /* width: calc((100vw - 400px)/2.5); */
  border: 1px solid;
}

.footer {
  position: fixed;
  bottom: 25px;
  left: 33vw;
}
</style>