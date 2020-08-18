<template>
<v-hover
  v-slot:default="{ hover }"
>
  <v-navigation-drawer
    v-model="$store.state.comm.drawer"
    :clipped="$vuetify.breakpoint.lgAndUp"
    :mini-variant="true"
    :expandOnHover="true"
    app
    color="white"
    dark class="blue lighten-2"
  >
    <!-- style="background-image: linear-gradient(180deg, rgba(255, 255, 255, 1), rgba(0, 0, 0, 0.1))" -->
    <v-list dense class="pt-0">
      <template v-for="item in items">
        <v-list-group
          v-if="item.children&&(isLogin||item.needLogin)"
          v-show="hover"
          :key="item.text"
          v-model="item.model"
          :prepend-icon="item.model ? item.icon : item['icon-alt']"
          append-icon light
          @click="getStudyList(), getMyReqs()"
        >
          <template v-slot:activator>
            <v-list-item-content>
              <v-list-item-title >{{ item.text }}</v-list-item-title>
            </v-list-item-content>
          </template>
          <v-list-item v-for="(child, i) in item.children" :key="i" link>
            <v-list-item-action v-if="child.icon" ></v-list-item-action>
            <v-list-item-content  class="ml-7">
              <v-list-item-title class="d-flex justify-space-between my-auto" style="white-space: normal" v-if="!child.waiting" @click="goToStudy(child.value)">
                {{ child.text }}
                <v-icon v-if="child.groupManager" color="amber" class="ml-2">mdi-crown</v-icon>
              </v-list-item-title>
              <v-list-item-title class="grey--text text--lighten-2 d-flex justify-space-between" v-else>
                <div style="white-space: normal">{{ child.text }}</div>
                <div class="my-auto">승인 대기중
                  <v-progress-circular
                    :value="50"
                    :size="19"
                    indeterminate
                    color="grey lighten-2"
                  ></v-progress-circular>
                </div>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>
        <v-list-item
          v-else-if="isLogin||item.needLogin"
          :key="item.text"
          link
          :to="{ name: item.page }"
          :exact="true"
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>{{ item.text }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    <v-list-item>
    <v-icon v-show="!hover&&isLogin">mdi-chevron-down</v-icon>
    </v-list-item>
    </v-list>
  </v-navigation-drawer>
  </v-hover>
</template>

<script>
import axios from "axios";
import { mapActions } from 'vuex';

export default {
  name: "Sidebar",
  data: () => ({
    items: [
      { icon: "mdi-home", text: "HOME", page: "Main", needLogin: true },
      {
        icon: "mdi-plus",
        text: "스터디 만들기",
        page: "StudyCreate",
        needLogin: true
      },
      {
        icon: "mdi-help-circle",
        text: "가이드",
        page: "Guide",
        needLogin: true
      },
      {
        icon: "mdi-chevron-up",
        "icon-alt": "mdi-chevron-down",
        text: "내 스터디",
        model: false,
        children: [],
        needLogin: false
      }
    ]
  }),
  computed: {
    isLogin() {
      return this.$store.state.auth.isLogin;
    }
  },
  mounted() {
    this.getMyReqs()
    this.getStudyList();
  },
  methods: {
    ...mapActions("notice", ["getMyReqs"]),
    goToStudy(no) {
      this.$router.push({ name: "StudyDetail", params: { id: no } });
      this.$router.go();
    },
    getStudyList() {
      if (!this.isLogin) {
        return;
      }
      const myStudyList = [];
      axios
        .get("/study/user/my")
        .then(res => {
          for (const myStudy of res.data.object) {
            myStudyList.push({
              text: myStudy.gpNm,
              groupManager:
                this.$store.state.auth.userInfo.userId === myStudy.gpMgrId
                  ? true
                  : false,
              value: myStudy.gpNo,
              waiting: false
            });
          }
          for (const myStudyReq of this.$store.state.notice.myGroupsReqs) {
            myStudyList.push({
              text: myStudyReq.gp.gpNm,
              groupManager: false,
              value: myStudyReq.gp.gpNo,
              waiting: true
            })
          }
          this.items[this.items.length - 1].children = myStudyList;
        })
        .catch(err => console.log(err));
    }
  }
};
</script>

<style>
  
.intro {
  display: flex;
  justify-content: center;
  margin: 4rem 0;
}

a {
  color: inherit;
}

.text-center {
  text-align: center;
}
</style>