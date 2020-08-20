<template>
  <div class="meeting-room">
    <RoomNavbar />
    <v-row>
      <v-col class="py-0">
        <div class="videos-container">
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
                class="mx-1 chatRoomBtn"
                fab
                dark
                color="#29B6F6"
                @click="showChatRoom = !showChatRoom"
              >
                <v-icon dark>mdi-message-text-outline</v-icon>
              </v-btn>
              <v-btn
                v-else
                class="mx-1 chatRoomBtn"
                fab
                outlined
                dark
                color="#29B6F6"
                @click="showChatRoom = !showChatRoom"
              >
                <v-icon dark>mdi-message-text-outline</v-icon>
              </v-btn>
              <v-btn class="mx-1" fab dark color="grey darken-1" @click.stop="openDialog">
                <v-icon dark>mdi-camera-metering-partial</v-icon>
              </v-btn>
              <!-- <v-btn color="primary" dark @click.stop="openDialog">Setting</v-btn> -->
              <v-btn class="mx-1" fab dark color="red" @click="checkout">
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
            <v-btn
              v-if="options.video"
              class="mx-1"
              small
              fab
              dark
              color="#43A047"
              @click="offVideo"
            >
              <v-icon dark>mdi-video</v-icon>
            </v-btn>
            <v-btn v-else class="mx-1" fab outlined small dark color="#43A047" @click="onVideo">
              <v-icon dark>mdi-video-off</v-icon>
            </v-btn>
            <v-btn class="mx-1" fab small dark color="grey darken-1" @click.stop="openDialog">
              <v-icon dark>mdi-camera-metering-partial</v-icon>
            </v-btn>
            <v-btn
              v-if="showChatRoom"
              class="mx-1"
              fab
              dark
              small
              color="#29B6F6"
              @click="chatBtn()"
            >
              <v-icon dark>mdi-message-text-outline</v-icon>
            </v-btn>
            <v-btn v-else class="mx-1" fab outlined dark small color="#29B6F6" @click="chatBtn()">
              <v-icon dark>mdi-message-text-outline</v-icon>
            </v-btn>
            <v-btn class="mx-1" fab dark small color="red" @click="checkout">
              <v-icon dark>mdi-account-arrow-right-outline</v-icon>
            </v-btn>
          </v-speed-dial>
        </div>
      </v-col>
      <v-col
        id="chatRoom"
        class="py-0 pl-0 chatroom"
        sm="5"
        md="3"
        xs="12"
        height="92vh"
        v-show="showChatRoom"
      >
        <Chat :roomid="roomid" />
      </v-col>
    </v-row>

    <v-row justify="center">
      <v-dialog v-model="dialog" max-width="400">
        <v-card>
          <v-card-title class="headline">설정</v-card-title>

          <v-card-text>
            <v-select
              v-model="audioInputTmp"
              :items="audioInputSelect"
              item-value="value"
              item-text="text"
              color="pink"
            ></v-select>
            <v-select
              v-model="videoInputTmp"
              :items="videoInputSelect"
              item-value="value"
              item-text="text"
              color="pink"
            ></v-select>
            <v-checkbox v-model="reverseCamTmp" label="카메라 좌우반전"></v-checkbox>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="saveSetting()">확인</v-btn>
            <v-btn color="red darken-1" text @click="dialog=false">취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>
<script src="https://cdn.jsdelivr.net/npm/rtcmulticonnection@latest/dist/RTCMultiConnection.min.js"></script>
<script src="https://rtcmulticonnection.herokuapp.com/socket.io/socket.io.js"></script>
<script>
import StudyDetailVue from "./StudyDetail.vue";
import RoomNavbar from "@/components/room/RoomNavbar.vue";

import Chat from "@/components/chat/Chat.vue";
import { log } from "util";

export default {
  name: "MeetingRoom",
  data() {
    return {
      roomid: "",
      connection: null,
      options: {
        video: true,
        audio: true
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
      elem: {},
      audioInputSelect: [],
      audioOutputSelect: [],
      videoInputSelect: [],
      audioInput: "",
      audioOutput: "",
      videoInput: "",
      audioInputTmp: "",
      audioOutputTmp: "",
      videoInputTmp: "",
      reverseCam: false,
      reverseCamTmp: true,
      dialog: false
    };
  },
  components: {
    RoomNavbar,
    Chat
  },
  created() {
    if (!this.$route.params.options) {
      alert("페이지를 새로고침 할 수 없습니다ㅠㅡㅠ");
      this.$router.push({ name: "ReadyMeeting" });

      return;
    }

    window.addEventListener("resize", this.onResize);
  },
  mounted() {
    this.joinRoom();
    if (window.innerWidth > 960) {
      this.showChatRoom = true;
      document.getElementsByClassName("chatRoomBtn").forEach(elem => {
        elem.style.display = "none";
      });
    }
    this.$store.state.sg.onMeeting = false;

    this.options = this.$route.params.options;

    const filter = "win16|win32|win64|mac";
    if (navigator.platform) {
      if (0 > filter.indexOf(navigator.platform.toLowerCase())) {
        this.check();
        window.addEventListener("resize", function() {
          if (window.matchMedia("(orientation: portrait)").matches) {
            this.check();
          }
        });
      }
    }

    this.initoptions();
    this.dragElement();
  },
  // beforeDestroy() {
  //   // window.removeEventListener("resize", this.onResize);
  //   // this.connection.close();
  // },
  methods: {
    openDialog() {
      this.getDevices();
      this.dialog = true;
    },
    onResize() {
      if (window.innerWidth > 960) {
        this.showChatRoom = true;
        document.getElementsByClassName("chatRoomBtn").forEach(elem => {
          elem.style.display = "none";
        });
      }
    },
    saveSetting() {
      this.dialog = false;

      // this.connection.attachStreams.forEach(function(stream) {
      //   stream.getTracks().forEach(function(track) {
      //     track.stop();
      //   });
      // });

      this.audioInput = this.audioInputTmp;
      this.videoInput = this.videoInputTmp;
      this.reverseCam = this.reverseCamTmp;

      const constraints = {
        audio: { deviceId: this.audioInput },
        video: { deviceId: this.videoInput }
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
      .then(this.getStream)
      .then(this.reverseCamera);
      // this.reverseCamera();
    },
    getDevices() {
      let audioInput = [];
      let audioOutput = [];
      let videoInput = [];
      navigator.mediaDevices
        .enumerateDevices()
        .then(function(devices) {
          devices.forEach(function(device) {
            let option = {};
            option.value = device.deviceId;
            if (device.kind == "audioinput") {
              option.text =
                device.label || `microphone ${audioInput.length + 1}`;
              audioInput.push(option);
            } else if (device.kind === "audiooutput") {
              option.text = device.label || `speaker ${audioOutput.length + 1}`;
              audioOutput.push(option);
            } else if (device.kind === "videoinput") {
              option.text = device.label || `camera ${videoInput.length + 1}`;
              videoInput.push(option);
            }
          });
        })
        .then(() => {
          this.audioInputSelect = audioInput;
          this.audioOutputSelect = audioOutput;
          this.videoInputSelect = videoInput;

          if (this.audioInput == "") this.audioInput = this.audioInputSelect[0];
          if (this.videoInput == "") this.videoInput = this.videoInputSelect[0];

          this.audioInputTmp = this.audioInput;
          this.videoInputTmp = this.videoInput;
          this.reverseCamTmp = this.reverseCam;
        });
    },
    getStream(stream) {
      this.replaceTrack(stream.getAudioTracks()[0], "audio");
      this.replaceTrack(stream.getVideoTracks()[0], "video");

      // let connection = this.connection;
      // connection.captureUserMedia(function() {
      //   connection.renegotiate(); // share with all existing users
      // });

      let localStream = this.connection.attachStreams[0];

      // localStream.removeTrack(localStream.getVideoTracks()[0]);
      // localStream.removeTrack(localStream.getAudioTracks()[0]);

      // localStream.addTrack(stream.getVideoTracks()[0]);
      // localStream.addTrack(stream.getAudioTracks()[0]);

      window.stream = stream;
      document.getElementById(localStream.streamid).srcObject = stream;
    },
    attachSinkId(element, sinkId) {
      if (typeof element.sinkId !== "undefined") {
        element
          .setSinkId(sinkId)
          .then(() => {
            console.log(`Success, audio output device attached: ${sinkId}`);
          })
          .catch(error => {
            console.log(error);
          });
      } else {
        console.warn("Browser does not support output device selection.");
      }
    },
    reverseCamera() {
      console.log(this.connection.attachStreams[0]);
      const msg = {
        id: this.connection.attachStreams[0].streamid,
        state: this.reverseCam
      };

      this.connection.send(msg);
      this.reverseCam
        ? document.getElementById(msg.id).classList.add("mirror-video")
        : document.getElementById(msg.id).classList.remove("mirror-video");
    },
    replaceTrack(track, type) {
      if (this.connection.getAllParticipants().length == 0) return;
      this.connection.getAllParticipants().forEach(pid => {
        const { peer } = this.connection.peers[pid];
        peer.getSenders().forEach(sender => {
          console.log(sender);
          if (sender.track.kind === type) sender.replaceTrack(track);
        });
      });
    },
    chatBtn() {
      this.showChatRoom = !this.showChatRoom;
    },
    check() {
      alert("모바일에서는 가로모드를 이용해 주세요!");
    },
    initoptions() {
      this.connection.videosContainer = document.querySelector(
        ".videos-container"
      );
      let options = { video: true, audio: true };
      if (!!this.$route.params.options) options = this.$route.params.options;
      const connection = this.connection;

      let audioinput, videoInput;
      connection.onstream = function(e) {
        e.mediaElement.id = e.streamid; // ---------- set ID
        document.querySelector(".videos-container").appendChild(e.mediaElement);

        if (e.type == "local") {
          if (!options.video) e.stream.mute("video");
          if (!options.audio) {
            e.stream.mute("audio");
            e.stream.muted = true;
          }
        }
      };

      connection.onmessage = function(event) {
        const data = event.data;

        // document.getElementById(data.id).classList.toggle("mirror-video");
        if (data.state)
          document.getElementById(data.id).classList.add("mirror-video");
        else document.getElementById(data.id).classList.remove("mirror-video");
      };
      this.connection.enableLogs = false;
    },
    joinRoom() {
      this.connection = new RTCMultiConnection();
      this.connection.session = {
        audio: true,
        video: true,
        data: true
      };

      //this.connection.socketURL = "http://i3b103.p.ssafy.io/socket/"; //배포옹
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

      this.connection.openOrJoin(`stew${this.$route.params.id}ssafy3`);
    },
    checkout() {
      const answer = confirm("회의를 종료하시겠습니까?");
      if (answer) {
        this.$router.push({ name: "StudyDetail" });
      }
    },
    outRoom() {
      this.connection.getAllParticipants().forEach(participantId => {
        this.connection.disconnectWith(participantId);
      });

      // this.connection.attachStreams.forEach(function(localStream) {
      //   localStream.stop();
      // });

      this.connection.attachStreams.forEach(function(stream) {
        stream.stop();
        // stream.getTracks().forEach(function(track) {
        //   stream.removeTrack(track);

        //   if (track.stop) {
        //     track.stop();
        //   }
        // });
      });

      this.connection.closeSocket();
    },
    mute() {
      let localStream = this.connection.attachStreams[0];
      localStream.mute("audio");
      localStream.muted = true;
      this.options.audio = false;
    },
    unmute() {
      let localStream = this.connection.attachStreams[0];
      localStream.unmute("audio");
      // this.connection.streamEvents.selectFirst(
      //   "local"
      // ).mediaElement.muted = true;
      this.connection.streamEvents.selectFirst(
        "local"
      ).mediaElement.muted = true;
      this.options.audio = true;
    },
    offVideo() {
      // let localStream = this.connection.attachStreams[0];
      // localStream.mute("video");

      this.connection.streamEvents
        .selectFirst("local")
        .stream.getTracks()[1].enabled = false;

      this.options.video = false;
    },
    onVideo() {
      // this.connection.session.video = true;

      this.connection.streamEvents.selectFirst("local").isAudioMuted = false;
      let localStream = this.connection.attachStreams[0];
      localStream.unmute("video");

      this.options.video = true;
    },

    dragElement() {
      // this.elem = document.getElementById("fab");
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
.mirror-video {
  transform: rotateY(180deg);
  -webkit-transform: rotateY(180deg); /* Safari and Chrome */
  -moz-transform: rotateY(180deg); /* Firefox */
}
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
  /* grid-template-columns: repeat(3, 30vw); */
  grid-auto-rows: max-content;

  overflow-y: auto;
  overflow-x: hidden;
}

.videos-container video {
  display: inline-block;
  width: 23.7vw;
  /* width: 30vw; */
  /* width: calc((100vw - 400px) / 2.5); */
  border: 1px solid;
}

@media (max-width: 960px) {
  .videos-container video {
    width: 49vw;
  }
  .videos-container {
    grid-template-columns: repeat(2, 49vw);
  }
}
/* @media (max-width: 600px) {
  .videos-container video {
    width: 95vw;
  }
  .videos-container {
    grid-template-columns: repeat(1, 95vw);
  }
} */
.footer {
  position: fixed;
  bottom: 25px;
  left: 33vw;
}
</style>