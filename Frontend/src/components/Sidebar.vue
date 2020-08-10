<template>
  <v-navigation-drawer
    v-model="$store.state.comm.drawer"
    :clipped="$vuetify.breakpoint.lgAndUp"
    :mini-variant="true"
    :expandOnHover="true"
    app
    color="white"
    class="theme-dark"
  >
    <!-- style="background-image: linear-gradient(180deg, rgba(255, 255, 255, 1), rgba(0, 0, 0, 0.1))" -->
    <v-list dense>
      <template v-for="item in items">
        <v-row v-if="item.heading" :key="item.heading" align="center">
          <v-col cols="6">
            <v-subheader v-if="item.heading">{{ item.heading }}</v-subheader>
          </v-col>
          <v-col cols="6" class="text-center">
            <a href="#!" class="body-2 black--text">EDIT</a>
          </v-col>
        </v-row>
        <v-list-group
          v-else-if="item.children&&(isLogin||item.needLogin)"
          :key="item.text"
          v-model="item.model"
          :prepend-icon="item.model ? item.icon : item['icon-alt']"
          append-icon
        >
          <template v-slot:activator>
            <v-list-item-content>
              <v-list-item-title>{{ item.text }}</v-list-item-title>
            </v-list-item-content>
          </template>
          <v-list-item v-for="(child, i) in item.children" :key="i" link>
            <v-list-item-action v-if="child.icon"></v-list-item-action>
            <v-list-item-content @click="goToStudy(child.value)">
              <v-list-item-title>
                {{ child.text }}
                <v-icon v-if="child.groupManager" color="amber">mdi-crown</v-icon>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>
        <v-list-item
          v-else-if="isLogin||item.needLogin"
          :key="item.text"
          link
          :to="{ name: item.page }"
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>{{ item.text }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import axios from "axios";
export default {
  name: "Sidebar",
  data: () => ({
    items: [
      { icon: "mdi-home", text: "HOME", page: "Home", needLogin: true },
      {
        icon: "mdi-plus",
        text: "Study 만들기",
        page: "StudyCreate",
        needLogin: false
      },
      { icon: "mdi-pen", text: "전체스터디", page: "Main", needLogin: true },
      {
        icon: "mdi-help-circle",
        text: "가이드",
        page: "Guide",
        needLogin: true
      },
      {
        icon: "mdi-chevron-up",
        "icon-alt": "mdi-chevron-down",
        text: "My Study",
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
    this.getStudyList();
  },
  methods: {
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
              value: myStudy.gpNo
            });
          }
          this.items[this.items.length - 1].children = myStudyList;
        })
        .catch(err => console.log(err));
    }
  }
};
</script>

<style>
</style>