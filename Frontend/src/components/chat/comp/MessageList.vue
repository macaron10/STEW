<template id="MsgList">
<!-- v-auto-bottom="msgs" -->
<div>
  <v-divider></v-divider>
  <v-list height="6.5vh" color="white" class="py-0" >
    <v-list-item >
      <v-list-item-content>
        <v-list-item-title>채팅창</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
<v-divider></v-divider>
  <v-list ref="ChatWindow" class='message-list py-0' height="51vh" color="#f5f5f5" >
    <v-list-item-group>
      <div
        v-for="(msg, j) in msgs"
        :key="j"
        >
      <!-- 남이보낸 메세지 -->
        <div
        v-if="msg.userId!==$store.state.auth.userInfo.userId"
        class="message pl-2">
          <img
            class="avatar my-1"
            alt="Avatar"
            :src="$store.state.comm.baseUrl + '/image/user' + msg.userImg"
          >
          <div>
            <div class="msg-bubble pl-3 pr-3">
              <div class="user-name">
                {{ msg.userNm}}
              </div>
              <div class="user-text">
                {{ msg.chatMsg }}
              </div>
            </div>
          </div>
          <div></div>
          <div class="reg-time px-6">
            {{ msg.regTime.substring(5, 7) }}/{{ msg.regTime.substring(8, 10) }} {{ msg.regTime.substring(11, 13) }} : {{ msg.regTime.substring(14, 16) }}
          </div>
        </div>
        <!-- 내가 보낸 메세지 -->
        <div v-if="msg.userId===$store.state.auth.userInfo.userId"
        class="my-message pr-2">
          <div>
            <div class="my-msg-bubble pl-3 pr-3">
              <div class="user-name">
              </div>
              <div class="user-text">
                {{ msg.chatMsg }}
              </div>
            </div>
          </div>
          <div></div>
          <div class="reg-time px-6">
            {{ msg.regTime.substring(5, 7) }}/{{ msg.regTime.substring(8, 10) }} {{ msg.regTime.substring(11, 13) }} : {{ msg.regTime.substring(14, 16) }}
          </div>
        </div>
      </div>
    </v-list-item-group>
  </v-list>
</div>
</template>

<script>
export default {
  name: 'MessageList',
  props: {
    msgs: Array
  },
  updated(){
    const msgList = this.$refs.ChatWindow
    msgList.$el.scrollTop = msgList.$el.scrollHeight;
  },
};
</script>

<style scoped>
.message-list {
  overflow-y: auto;
  overflow-x: hidden;
}

.message {
  display: grid;
  /* grid-template-rows: ; */
  /* grid-template-columns: repeat(2, calc((100vw - 400px)/2.5) ); */
  grid-template-columns: 2.5vw 20vw;
}

.avatar {
  display:inline;
  position: relative;
  left: 0;
  min-width: 36px;
  max-width: 36px;
  min-height: 36px;
  max-height: 36px;
  border-radius: 70%;
}
.msg-bubble {
  display:inline-block;
  /* border: 1px solid; */
  /* border-top-left-radius: 0px; */
  position:relative;
  margin: 5px 0 0 18px;
  /* width: 100%; */
  max-width: 17vw;
  background:#fff;
  border-radius: 10px;
  font-size: medium;
  padding-bottom: 0.3rem;
}
.msg-bubble:after {
 border-top:15px solid #fff;
 border-left: 15px solid transparent;
 border-right: 0px solid transparent;
 border-bottom: 0px solid transparent;
 content:"";
 position:absolute;
 top:10px;
 left:-15px;
}
.user-name {
  font-size: medium;
  margin: 0 0 0 0;
}
.user-text {
  font-size: 15px;
  white-space:pre-line;
}
.my-message{
  text-align:right;
}
.my-msg-bubble{
  display:inline-block;
  position:relative;
  margin: 5px 15px 0 20px;
  background: rgb(160, 235, 253);
  border-radius: 10px;
  font-size: medium;
  text-align: right;
  min-height: 32px;
}
.my-msg-bubble:after {
 border-top:15px solid rgb(160, 235, 253);
 border-left: 0px solid transparent;
 border-right: 15px solid transparent;
 border-bottom: 0px solid transparent;
 content:"";
 position:absolute;
 top:10px;
 right:-15px;
}
.reg-time {
  font-size: 12px;
  color: #909090
}
</style>