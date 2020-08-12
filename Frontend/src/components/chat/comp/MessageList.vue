<template id="MsgList">
<!-- v-auto-bottom="msgs" -->
  <v-list >
    <v-card
    class="mx-auto"
    height="68vh"
    >
     <v-virtual-scroll
    :items="msgs"
    :item-height="70"
    height="68vh"
  >
    <template  slot-scope="props">
      <!-- 남이보낸 메세지 -->
      <v-list-item v-if="props.item.userId!==$store.state.auth.userInfo.userId">
            <v-list-item-icon>
                <v-avatar
                  size="36px"
                >
                  <img
                    alt="Avatar"
                    src="https://avatars0.githubusercontent.com/u/9064066?v=4&s=460"
                  >
                </v-avatar>
            </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ props.item.userNm }}</v-list-item-title>
              {{ props.item.chatMsg }}
            <v-list-item-subtitle>
              {{ props.item.regTime }}
            </v-list-item-subtitle>
          </v-list-item-content>
      </v-list-item>
      <!-- 내가 보낸 메세지 -->
      <v-list-item v-if="props.item.userId===$store.state.auth.userInfo.userId">
        <v-list-item-content class="text-right">
          <v-list-item-title >{{ props.item.userNm }}</v-list-item-title>
            <div>{{ props.item.chatMsg }}</div>
          <v-list-item-subtitle>
            {{ props.item.regTime }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
        </template>
      </v-virtual-scroll>
    </v-card>
  </v-list>
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

</style>