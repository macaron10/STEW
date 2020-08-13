<template>
<div>
    <v-list-item height="6.5vh" >
      <v-list-item-content class="py-0" >
        <v-list-item-title>참여중인 멤버</v-list-item-title>
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
  data () {
    return {
      members: [
        {
          "userId": 1,
          "userNm": "유저1",
          "userEmail": "user1@a.a",
          "userIntro": "",
          "userImg": "\\2020\\08\\12\\f3df2cac3da241adb34145c5ba1314bc145301.png",
          "type": "stew",
          "userGoalHr": 0
        },
        {
          "userId": 2,
          "userNm": "유저2",
          "userEmail": "user2@a.a",
          "userIntro": "",
          "userImg": "\\2020\\08\\12\\f3df2cac3da241adb34145c5ba1314bc145301.png",
          "type": "stew",
          "userGoalHr": 0
        },
        {
          "userId": 1,
          "userNm": "유저1",
          "userEmail": "user1@a.a",
          "userIntro": "",
          "userImg": "\\2020\\08\\12\\f3df2cac3da241adb34145c5ba1314bc145301.png",
          "type": "stew",
          "userGoalHr": 0
        },
        {
          "userId": 1,
          "userNm": "유저1",
          "userEmail": "user1@a.a",
          "userIntro": "",
          "userImg": "\\2020\\08\\12\\f3df2cac3da241adb34145c5ba1314bc145301.png",
          "type": "stew",
          "userGoalHr": 0
        },
      ],
    }
  }

};
</script>

<style scoped>
.chat-member-list {
  overflow-y: auto;
}
</style>
