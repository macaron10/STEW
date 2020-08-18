<template>
  <v-app-bar
    v-if="$store.state.sg.onMeeting"
    :clipped-left="$vuetify.breakpoint.lgAndUp"
    app
    color="#FFFFFF"
    fixed
  >
    <v-app-bar-nav-icon @click.stop="$store.commit('drawerOnOff')" color="blue lighten-2"></v-app-bar-nav-icon>
    <v-toolbar-title style="width: 300px" class="ml-0 pl-10">
      <v-btn icon dark large :to="{ name: 'Main' }">
        <v-img
          :src="$store.state.comm.baseUrl + '/image/main/stew.png'"
          alt="Vuetify"
          :aspect-ratio="200"
          :min-height="30"
          :min-width="100"
        ></v-img>
      </v-btn>
      <span class="hidden-sm-and-down blue--text text--lighten-3"></span>
    </v-toolbar-title>
    <!-- <v-text-field
      solo-inverted
      hide-details
      color="blue lighten-2"
      outlined
      flat single-line
      prepend-inner-icon="mdi-magnify"
      label="Search"
      :append-icon-cb="() => {}"
      class="hidden-sm-and-down"
      v-model="wordForSearching"
      @keypress.enter="fetchGroups"
    ></v-text-field>-->
    <v-text-field
      placeholder="이름, #태그를 검색해보세요!"
      single-line
      append-icon="mdi-magnify"
      color="blue lighten-2"
      hide-details
      v-model="wordForSearching"
      @keypress.enter="searchGroup"
    ></v-text-field>
    <v-spacer></v-spacer>
    <v-btn icon v-if="!isLogin" :to="{ name: 'Login' }" color="blue lighten-2">
      <v-icon>mdi-account</v-icon>
    </v-btn>

    <v-btn icon v-if="!isLogin" :to="{ name: 'SignUp' }" color="blue lighten-2">
      <v-icon>mdi-account-plus</v-icon>
    </v-btn>

    <!-- 알림 메뉴 -->
    <v-menu transition="slide-y-transition" bottom v-if="isLogin" offset-y min-width="300">
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon color="blue lighten-2" v-bind="attrs" v-on="on">
          <v-badge :content="messages" :value="messages" color="red" overlap>
            <v-icon>mdi-bell</v-icon>
          </v-badge>
        </v-btn>
      </template>
      <v-list>
        <v-list-item-group v-model="model2">
          <v-list-item v-for="(groupsReq, j) in groupsReqs" :key="j">
            <v-list-item-icon>
              <v-avatar size="36px">
                <img alt="Avatar" :src='$store.state.comm.baseUrl + "/image/user" + groupsReq.user.userImg' />
              </v-avatar>
            </v-list-item-icon>
            <!-- 내의 신청 -->
            <v-list-item-content v-if="userInfo.userId===groupsReq.user.userId">
              '{{ groupsReq.gp.gpNm }}'에 가입을 신청했습니다.(대기중)
              <br />
              <div>전송 메세지 : "{{ groupsReq.gpReqMsg }}"</div>
            </v-list-item-content>
            <!-- 다른 유저에게서 온 신청 -->
            <v-list-item-content v-else>
              '{{ groupsReq.user.userNm }}' 님이 '{{ groupsReq.gp.gpNm }}'에 가입을 신청했습니다.
              <!-- <v-list-item-tistle v-text="groupsReqs.gp.gpNm"></v-list-item-title> -->
              <br />
              <div v-if="groupsReq.gpReqMsg">"{{ groupsReq.gpReqMsg }}"</div>
            </v-list-item-content>
            <v-btn class="mx-2" dark color="green lighten-2" @click="reqOk(groupsReq.gpReqNo)">수락</v-btn>
            <v-btn color="red lighten-2" dark  @click="reqReject(groupsReq.gpReqNo)">거절</v-btn>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-menu>

    <!-- 개인 메뉴 -->
    <div class="text-center">
      <v-menu v-if="isLogin" offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon v-bind="attrs" v-on="on" color="blue lighten-2">
            <v-icon>mdi-account-circle</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item-group v-model="model">
            <v-list-item v-for="(item, i) in items" :key="i" @click="goToPage(item.text)">
              <v-list-item-icon>
                <v-icon v-text="item.icon"></v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title v-text="item.text"></v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-menu>
    </div>
  </v-app-bar>
</template>

<script>
import axios from "axios";
import { mapState, mapActions, mapMutations } from "vuex";
import router from "../router";

export default {
  name: "Navbar",
  computed: {
    ...mapState("auth", ["userInfo", "isLogin"]),
    ...mapState("notice", ["groupsReqs"]),
    messages: function() {
      return this.groupsReqs.length;
    }
  },
  methods: {
    ...mapActions("notice", ["getReqsSock", "getReqs"]),
    ...mapActions("auth", ["signIn", "logout"]),
    ...mapActions("sg", ["fetchGroups", "getGroups"]),
    async reqOk(gpReqNo) {
      const apiUrl = "/study/user/accept?no=" + gpReqNo;
      try {
        const res = await axios.post(apiUrl);
      } catch (err) {
        console.error(err);
      }
    },
    async reqReject(gpReqNo) {
      const apiUrl = "/study/user/reject?no=" + gpReqNo;
      try {
        const res = await axios.post(apiUrl);
      } catch (err) {
        console.error(err);
      }
    },
    goToPage(nextPage) {
      switch (nextPage) {
        case "내 일정":
          this.$router.push({ name: "MySchedule" });
          break;
        case "프로필 수정":
          this.$router.push({ name: "UserDetail" });
          break;
        case "공부 기록":
          this.$router.push({ name: "UserTimer" });
          break;
        case "로그아웃":
          this.logout();
      }
    },
    searchGroup() {
      this.$store.commit("sg/setKeyWord", this.wordForSearching);
      this.$store.dispatch("sg/fetchGroups");
      this.$router
        .push({
          name: "StudySearch"
        })
        .catch(d => {
          d;
        });
    }
  },
  data() {
    return {
      wordForSearching: "",
      items: [
        {
          icon: "mdi-account",
          text: "프로필 수정"
        },
        {
          icon: "mdi-timer",
          text: "공부 기록"
        },
        {
          icon: "mdi-calendar",
          text: "내 일정"
        },
        {
          icon: "mdi-logout",
          text: "로그아웃"
        }
      ],
      model: 1,
      model2: 2,
      // noticeItems: [
      //   {
      //     icon: 'mdi-user',
      //     text: 'ㅇㅇㅇ님이 ㅇㅇㅇ그룹에 가입하고 싶어합니다. ',
      //   },
      //   {
      //     icon: 'mdi-star',
      //     text: 'ㅇㅇㅇ님이 ㅇㅇㅇ그룹에 가입하고 싶어합니다.',
      //   },
      //   {
      //     icon: 'mdi-send',
      //     text: 'ㅇㅇㅇ님이 ㅇㅇㅇ그룹에 가입하고 싶어합니다.',
      //   },
      //   {
      //     icon: 'mdi-email-open',
      //     text: '기타알림',
      //   },
      // ],
      show: false
    };
  },
  mounted () {
    this.getReqs()
    this.getReqsSock()
  }

};
</script>
