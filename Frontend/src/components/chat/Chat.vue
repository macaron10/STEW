<template>
  <div height>
    <ChatMemberList :members="members" />
    <MessageList class="msg-list" :msgs="messages" />
    <v-divider></v-divider>
    <MessageForm class="msg-form" v-on:submitMessage="sendMessage" />
  </div>
</template>

<script>
import { mapMutations, mapState } from "vuex";
import MessageList from "@/components/chat/comp/MessageList.vue";
import MessageForm from "@/components/chat/comp/MessageForm.vue";
import ChatMemberList from "@/components/chat/comp/ChatMemberList.vue";
// import Constant from '@/Constant';

import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import axios from "axios";
import { Cipher } from "crypto";

export default {
  name: "Chat",
  data() {
    return {
      messages: [],
      roomid: 0,
      members: []
    };
  },
  components: {
    MessageList,
    MessageForm,
    ChatMemberList
  },
  methods: {
    initSock() {
      const apiUrl = this.$store.state.comm.baseSocketUrl + "/sock";
      const socket = new SockJS(apiUrl);
      const ws = Stomp.over(socket);
      this.ws = ws;
      this.ws.debug = () => {return };

    },
    getChattingSock(gpNo) {
      const token = {
        accessToken: this.$store.state.auth.userInfo.accessToken
      };
      this.ws.connect(token, frame => {
        this.ws.subscribe("/sub/chat/" + gpNo, msg => {
          // state.groupsReqs.push(JSON.parse(msg.body))
          this.messages.push(JSON.parse(msg.body));
          if (JSON.parse(msg.body).type == "ENTER") {
            const userInf = {
              userNm: JSON.parse(msg.body).userNm,
              userImg: JSON.parse(msg.body).userImg
            };
            this.members.push(userInf);
          } else if (JSON.parse(msg.body).type == "QUIT") {
            const itemToFind = this.members.find(function(item) {
              return item.userNm === JSON.parse(msg.body).userNm;
            });
            const idx = this.members.indexOf(itemToFind);
            if (idx > -1) this.members.splice(idx, 1);
          }
        });
        // this.sendEnterMsg();
      });
    },
    sendMessage(msg) {
      const token = {
        accessToken: this.$store.state.auth.userInfo.accessToken
      };
      const tempMsg = {
        type: "TALK",
        gpNo: this.roomid,
        chatMsg: msg
      };
      const stringMsg = JSON.stringify(tempMsg);
      // this.messages.push(message)
      this.ws.send("/pub/chat", stringMsg, token);
    },
    sendEnterMsg() {
      const token = {
        accessToken: this.$store.state.auth.userInfo.accessToken
      };
      const enterMsg = {
        type: "ENTER",
        gpNo: this.roomid,
        chatMsg: "입장했습니다."
      };
      const stringMsg = JSON.stringify(enterMsg);
      // this.messages.push(message)
      this.ws.send("/pub/chat", stringMsg, token);
    },
    sendQuitMsg() {
      const token = {
        accessToken: this.$store.state.auth.userInfo.accessToken
      };
      const quitMsg = {
        type: "QUIT",
        gpNo: this.roomid,
        chatMsg: "님이 퇴장했습니다."
      };
      const stringMsg = JSON.stringify(quitMsg);
      // this.messages.push(message)
      this.ws.send("/pub/chat", stringMsg, token);
    },
    async getChatRoomInfo() {
      const apiUrl = "/chatroom-info/" + this.$route.params.id;
      try {
        const res = await axios.get(apiUrl);
        const userList = res.data.object.userList;

        Object.values(userList).forEach(e => {
          this.members.push(e);
        });
      } catch (err) {
        console.error(err);
      }
    }
  },
  created() {
    this.roomid = Number(this.$route.params.id);
    this.getChatRoomInfo();
  },
  mounted() {
    this.initSock();
    this.getChattingSock(this.roomid);
  },
  destroyed() {
    this.ws.disconnect();
    this.sendQuitMsg();
  }
};
</script>

<style>
/* .msg-form { */
/* position:absolute;
  right:0;
  bottom:0; */
/* } */
/* .msg-list { */
/* overflow-y: scroll; */
/* } */
</style>