<template>
  <v-app-bar
    v-if="!$store.state.sg.onMeeting"
    :clipped-left="$vuetify.breakpoint.lgAndUp"
    app
    color="grey darken-1"
    dark
    fixed
  >
    <v-app-bar-nav-icon @click.stop="$store.commit('drawerOnOff')"></v-app-bar-nav-icon>
    <v-col>
      <v-toolbar-title class="text-truncate">
        <v-row>
          <h3 class="pl-10">{{ group.gpNm }}</h3>
          <!-- <div class="mt-2 body-1 white--text"><v-icon class="pl-5 pr-1" color="white">mdi-account</v-icon>{{ group.gpCurNum }}</div> -->
        </v-row>
      </v-toolbar-title>
    </v-col>
    <v-col absolute bottom right>
      <Timer />
    </v-col>
  </v-app-bar>
</template>

<script>
import axios from "axios";
import { mapState, mapActions, mapMutations } from "vuex";
import Timer from "@/components/temp/Timer.vue";

export default {
  name: "RoomNavbar",
  components: {
    Timer
  },
  computed: {
    ...mapState("auth", ["userInfo", "isLogin"])
  },
  methods: {
    ...mapActions("auth", ["signIn", "logout"]),
    async getDetail() {
      const apiUrl = "/study/user/" + this.id;
      try {
        const res = await axios.get(apiUrl);
        this.group = JSON.parse(res.data.object).group;
        this.group = JSON.parse(this.group);
        this.membersData = JSON.parse(res.data.object).joinList;
        console.log(this.group, "그룹데이터!");
      } catch (err) {
        console.error(err);
      }
    }
  },
  data() {
    return {
      group: [],
      membersData: [],
      id: null,
      userId: 0
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.userId = this.$store.state.auth.userInfo.userId;
    this.getDetail();
  },
  beforeDestroy() {
    this.$store.state.sg.onMeeting = true;
  }
};
</script>

<style>
</style>