<template>
<v-container fluid>
    <v-row>
      <v-col
        v-for="member in members[0]"
        :key="member.userId"
        class="d-flex child-flex"
        cols="6"
      >
        <v-card
          class="mx-auto"
        >
          <v-list-item>
            <!-- 가져올 수 있는 것  목표시간, 유저이름, 유저소개, 유저이미지, (유저 이메일,유저아이디)(필요x) -->
            <v-list-item-content>
              <v-row>
                <v-col cols=2>                 
                  <v-avatar
                    size="36px"
                  >
                      <!-- v-if="member.user.userImg" -->
                    <img
                      v-if="member.user.userImg"
                      alt="Avatar"
                      :src="$store.state.baseUrl + '/user' + member.user.userImg"
                    >
                    <!-- @click="toDetail(member.userId)" 유저디테일 이동기능-->   <!-- :to="'/user/' + user넘버" -->
                    <v-icon
                      v-else
                      :color="primary"
                      size="36px"
                    >mdi-account-circle</v-icon>
                  </v-avatar>
                </v-col>
                <v-col cols="7">
                  <v-list-item-title class="headline">{{ member.user.userNm }}</v-list-item-title>
                  <v-list-item-subtitle>{{ member.user.userEmail }}</v-list-item-subtitle>
                </v-col>
                <v-col cols="2">
                  <v-btn
                    v-if="gpMgrId===userId&&member.user.userId!==userId"
                    color="red lighten-2 white--text"
                    @click="kick(member.gpJoinNo)"
                  >퇴출
                  </v-btn>
                </v-col>
              </v-row>
              <v-card-text class="py-0">
                일일 목표 공부 시간 : {{ member.user.userGoalHr }} 시간
              </v-card-text>        
              <v-card-text>
                {{ member.user.userIntro }}
              </v-card-text>
            </v-list-item-content>
          </v-list-item>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from "axios";

  export default {
    name: 'MemberList',
    props: {
      members: Array,
      gpMgrId: Number,
    },
    data () {
      return {
        userId:3
      }
    },
    methods : {
      async kick(gpJoinNo) {
        const apiUrl = '/study/user/remove?no='+gpJoinNo
        try {
          const res = axios.post(apiUrl)
        } catch (err) {
          console.error(err)
        }
    },
    mounted () {
      console.log(this.members[0])
    }
    }
  }
</script>