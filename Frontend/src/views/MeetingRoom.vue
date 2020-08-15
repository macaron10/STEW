<template>
  <div class="meeting-room">
    <RoomNavbar />
    <v-row>
      <v-col class="py-0" sm="7" md="9" xs="12">
        <div class="videos-container">
          <!-- <h1>비디오의 상태 {{options.video}}</h1> -->
          <!-- footer -->

          <v-row class="footer d-none d-md-block" style="z-index:1">
            <div width="100%" class="text-center">
              <v-btn v-if="options.audio" class="mx-1" fab dark color="#FB8C00" @click="mute">
                <v-icon dark>mdi-microphone</v-icon>
              </v-btn>
              <v-btn v-else class="mx-1" fab outlined dark color="#FB8C00" @click="unmute">
                <v-icon dark>mdi-microphone-off</v-icon>
              </v-btn>
              <v-btn v-if="options.video" class="mx-1" fab dark color="#FF8A65" @click="offVideo">
                <v-icon dark>mdi-video</v-icon>
              </v-btn>
              <v-btn v-else class="mx-1" fab outlined dark color="#FF8A65" @click="onVideo">
                <v-icon dark>mdi-video-off</v-icon>
              </v-btn>
              <v-btn
                v-if="showChatRoom"
                class="mx-1"
                fab
                dark
                color="#29B6F6"
                @click="showChatRoom = !showChatRoom"
              >
                <v-icon dark>mdi-message-text-outline</v-icon>
              </v-btn>
              <v-btn
                v-else
                class="mx-1"
                fab
                outlined
                dark
                color="#29B6F6"
                @click="showChatRoom = !showChatRoom"
              >
                <v-icon dark>mdi-message-text-outline</v-icon>
              </v-btn>
              <v-btn
                class="mx-1"
                fab
                dark
                color="red"
                @click="outRoom"
                :to="{ name: 'StudyDetail' }"
              >
                <v-icon dark>mdi-account-arrow-right-outline</v-icon>
              </v-btn>
            </div>
          </v-row>
          <v-speed-dial
            v-model="fab"
            direction="top"
            bottom
            left
            :open-on-hover="true"
            transition="slide-y-reverse-transition"
            absolute
            class="d-md-none"
            id="fab"
          >
            <template v-slot:activator>
              <v-btn v-model="fab" color="#424242" dark fab>
                <v-icon v-if="fab">mdi-close</v-icon>
                <v-icon v-else>mdi-settings</v-icon>
              </v-btn>
            </template>
            <v-btn v-if="options.audio" class="mx-1" small fab dark color="#FFB300" @click="mute">
                <v-icon dark>mdi-microphone</v-icon>
              </v-btn>
              <v-btn v-else class="mx-1" fab outlined small dark color="#FFB300" @click="unmute">
                <v-icon dark>mdi-microphone-off</v-icon>
              </v-btn>
              <v-btn v-if="options.video" class="mx-1" small fab dark color="#43A047" @click="offVideo">
                <v-icon dark>mdi-video</v-icon>
              </v-btn>
              <v-btn v-else class="mx-1" fab outlined small dark color="#43A047" @click="onVideo">
                <v-icon dark>mdi-video-off</v-icon>
              </v-btn>
              <v-btn
                v-if="showChatRoom"
                class="mx-1"
                fab
                dark small
                color="#29B6F6"
                @click="showChatRoom = !showChatRoom"
              >
                <v-icon dark>mdi-message-text-outline</v-icon>
              </v-btn>
              <v-btn
                v-else
                class="mx-1"
                fab
                outlined
                dark small
                color="#29B6F6"
                @click="showChatRoom = !showChatRoom"
              >
                <v-icon dark>mdi-message-text-outline</v-icon>
              </v-btn>
              <v-btn
                class="mx-1"
                fab
                dark small
                color="red"
                @click="outRoom"
                :to="{ name: 'StudyDetail' }"
              >
                <v-icon dark>mdi-account-arrow-right-outline</v-icon>
              </v-btn>
          </v-speed-dial>
        </div>
      </v-col>
      <v-col class="py-0 pl-0 chatroom" sm="5" md="3" xs="12" height="92vh" v-show="showChatRoom">
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
      videos: {},
      showChatRoom: false,
      fab: false,
      pos: {
        pos1: 0,
        pos2: 0,
        pos3: 0,
        pos4: 0
      },
      elem: {}
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
    this.dragElement();
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
      // this.connection.onstream = function(event) {
      //   const video = event.mediaElement;
      //   video.id = event.streamid;
      //   // document.body.insertBefore(video, document.body.firstChild);

      //   event.stream.mute("video");
      //   if (this.$route.params.options.audio == false) {
      //     this.mute();
      //   }
      //   if (this.$route.params.options.video == false) {
      //     this.offVideo();
      //   }
      // };
    },
    joinRoom() {
      this.connection = new RTCMultiConnection();
      this.connection.session = {
        audio: true,
        video: true,
        data: true
      };

      // this.connection.socketURL = "https://i3b103.p.ssafy.io/socket/"; //배포옹
      this.connection.socketURL =
        "https://rtcmulticonnection.herokuapp.com:443/"; // 개발용

      this.connection.mediaConstraints = {
        audio: true,
        video: {}
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
    dragElement() {
      this.elem = document.getElementById("fab");
      this.elem.onmousedown = this.dragMouseDown;
    },
    dragMouseDown(e) {
      e = e || window.event;
      e.preventDefault();
      // get the mouse cursor position at startup:
      this.pos.pos3 = e.clientX;
      this.pos.pos4 = e.clientY;
      document.onmouseup = this.closeDragElement;
      // call a function whenever the cursor moves:
      document.onmousemove = this.elementDrag;
    },
    elementDrag(e) {
      e = e || window.event;
      e.preventDefault();
      this.pos.pos1 = this.pos.pos3 - e.clientX;
      this.pos.pos2 = this.pos.pos4 - e.clientY;
      this.pos.pos3 = e.clientX;
      this.pos.pos4 = e.clientY;
      this.elem.style.top = this.elem.offsetTop - this.pos.pos2 + "px";
      this.elem.style.left = this.elem.offsetLeft - this.pos.pos1 + "px";
    },
    closeDragElement() {
      document.onmouseup = null;
      document.onmousemove = null;
    }
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
  background-color: #fff;
  height: 92vh;
  position: absolute;
  top: 0;
  right: 0;
}

video::-webkit-media-controls {
  display: none;
}
.videos-container {
  display: grid;
  height: 92vh;
  width: 100%;

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