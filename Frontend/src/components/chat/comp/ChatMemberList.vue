<template>
<div>
    <v-list-item height="6.5vh" >
      <v-list-item-content class="py-0" >
        <v-list-item-title class="d-block" width="20px">참여중인 멤버</v-list-item-title>
        <div class="pt-1 pr-2"><v-icon>mdi-account</v-icon>{{memsNum}}</div>
      </v-list-item-content>
      
    </v-list-item>
<v-divider></v-divider>
  <v-list class="py-0 chat-member-list" height="21vh" color="#f5f5f5">
    <v-list-item-group>
      <v-list-item
        v-for="(member, j) in members"
        :key="j"
        height="48"
      >
        <v-list-item-icon class="my-2 mx-0">
            <v-avatar
              size="36px"
            >
              <img
                alt="Avatar"
                :src="$store.state.comm.baseUrl + '/image/user' + member.userImg"
              >
            </v-avatar>
        </v-list-item-icon>
        <v-list-item-content class="py-0 pl-5 d-block">
          {{ member.userNm }}
        <v-list-item-subtitle height="10">
        "{{ member.userEmail }}"
        </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list-item-group>
  </v-list>
</div>
</template>

<script>
// @ is an alias to /src
export default {
  name: "ChatMemberList",
  props: {
    members: Array
  },
  computed : {
    memsNum: function() {
      return this.members.length
    }
  }
};
</script>

<style scoped>
.chat-member-list {
  overflow-y: auto;
}
</style>
