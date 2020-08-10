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
          v-else-if="item.children"
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
            <v-list-item-action v-if="child.icon">
              <v-icon>{{ child.icon }}</v-icon>
            </v-list-item-action>
            <v-list-item-content @click="goToStudy(child.value)">
              <v-list-item-title>{{ child.text }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>
        <v-list-item v-else :key="item.text" link :to="{ name: item.page }">
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
      { icon: "mdi-home", text: "HOME", page: "Home" },
      { icon: "mdi-plus", text: "Study 만들기", page: "StudyCreate" },
      { icon: "mdi-message", text: "Contact Us", page: "Contact" },
      { icon: "mdi-help-circle", text: "가이드", page: "Guide" },
      {
        icon: "mdi-chevron-up",
        "icon-alt": "mdi-chevron-down",
        text: "My Study",
        model: false,
        children: []
      }
    ]
  }),
  mounted() {
    this.getStudyList();
  },
  methods: {
    goToStudy(no){
      this.$router.push({ name: 'StudyDetail', params: { id: no }})
      this.$router.go()
    },
    getStudyList() {
      const myStudyList = [];
      axios
        .get("/study/user/my")
        .then(res => {
          for (const myStudy of res.data.object) {
            myStudyList.push({
              text: myStudy.gpNm,
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