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
  <v-list class='message-list py-0' height="51vh" color="#f5f5f5">
    <v-list-item-group>
      <div
        v-for="(msg, j) in msgs"
        :key="j"
        >
      <!-- 남이보낸 메세지 -->
        <div
        v-if="msg.userId!==$store.state.auth.userInfo.userId"
        class="pl-2">
          <v-list-item-content>
            <v-list-item-icon>
                <v-avatar
                  size="30px"
                >
                  <img
                    alt="Avatar"
                    :src="$store.state.comm.baseUrl + '/image/user' + msg.userImg"
                  >
                </v-avatar>
            </v-list-item-icon>
            <v-list-item-title>{{ msg.userNm }}</v-list-item-title>
              {{ msg.chatMsg }}
            <v-list-item-subtitle>
              {{ msg.regTime }}
            </v-list-item-subtitle>
          </v-list-item-content>
        </div>
        <!-- 내가 보낸 메세지 -->
        <div v-if="msg.userId===$store.state.auth.userInfo.userId">
          <v-list-item-content class="text-right">
            <v-list-item-title >{{ msg.userNm }}</v-list-item-title>
              <div>{{ msg.chatMsg }}</div>
            <v-list-item-subtitle>
              {{ msg.regTime }}
            </v-list-item-subtitle>
          </v-list-item-content>
    
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
};
</script>

<style scoped>
.message-list {
  overflow-y: auto;
}
</style>