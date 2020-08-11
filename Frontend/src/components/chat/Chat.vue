<template>
  <div class="inner-wrap" fluid fill-height inner-wrap>
    <MessageList :msgs="messages" />
    <MessageForm v-on:submitMessage="sendMessage" />
  </div>
</template>

<script>
import { mapMutations, mapState } from 'vuex';
import MessageList from '@/components/chat/comp/MessageList.vue';
import MessageForm from '@/components/chat/comp/MessageForm.vue';
// import Constant from '@/Constant';

import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';

export default {
  name: 'Chat',
  data() {
    return {
      messages: [],
    };
  },
  components: {
    MessageList,
    MessageForm,
  },
  methods: {

      initSock () {
        const apiUrl = this.$store.state.comm.baseUrl + '/sock'
        const socket = new SockJS(apiUrl)
        const ws = Stomp.over(socket)
        this.ws = ws
        console.log(this.ws, '이거다')
      },
      getChattingSock(gpNo) {
        const token = {
          'accessToken': this.$store.state.auth.userInfo.accessToken
        }
        this.ws.connect(token,
          frame => {
            console.log('소켓 연결 성공');

            this.ws.subscribe("/sub/chat/" + gpNo, msg =>{
              // state.groupsReqs.push(JSON.parse(msg.body))
              this.messages.push(JSON.parse(msg.body))
              console.log(JSON.parse(msg.body), '구독msg')
            })
        })
      },
    sendMessage(msg) {
      const token = {
          'accessToken': this.$store.state.auth.userInfo.accessToken,
      }
      const tempMsg = {
        type: 'TALK',
        gpNo: 26,
        chatMsg: msg
      }
      const stringMsg = JSON.stringify(tempMsg)
      // this.messages.push(message)
      this.ws.send("/pub/chat", stringMsg, token)
      }
    },
    mounted() {
      this.initSock()
      console.log(this.roomid,'얘는 대체 어디감?')
      this.getChattingSock(26)
    }
};
</script>

<style>
/* .msg-form {
  bottom: -28px;
  position: absolute;
  left: 0;
  right: 0;
}
.msg-list {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 60px;
  overflow-x: scroll;
} */
</style>