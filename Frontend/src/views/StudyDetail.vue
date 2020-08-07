<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="mb-6 ">
          <div class="d-flex justify-center">
            <img
            @click="enterMeetingRoom(group.gpNo)"
            :src="$store.state.baseUrl + '/group' + group.gpImg"
            alt="그룹 이미지"
            height=400px
            >
            <!-- :src="$store.state.baseUrl + group.gpImg" 배포용 -->
          </div>
          <div class="text-center">
            <h1 class="my-3">{{ group.gpNm }}</h1>
            <h4 class="mb-2">{{ group.gpIntro }}</h4>
            <v-btn
              :to="{ name:'StudyUpdate', params: { id: id }}"
              color="blue accent-1"
              class="white--text font-weight-bold mx-3"
            >
              수정
            </v-btn>
            <v-btn
              color="red accent-1"
              class="white--text font-weight-bold"
              @click="quitGroup(group.gpNo)"
            >
              탈퇴
            </v-btn>
          </div>
        </v-col>
        <v-col class="mb-6">
          <Calendar />
        </v-col>
      </v-row>
    <v-card>
      <v-tabs
        v-model="tab"
        background-color="blue lighten-1"
        :grow="true"
        dark
        :icons-and-text="true"
      >
        <v-tab
          v-for="item in items"
          :key="item.tab"
        >
          {{ item.tab }}
          <v-icon>{{ item.icon }}</v-icon>
        </v-tab>
      </v-tabs>

      <v-tabs-items v-model="tab">
        <v-tab-item
          v-for="item in items"
          :key="item.tab"
        >
          <v-card flat>
            <MemberList v-if="item.no==='2'" :members="membersData" />
    <!-- 임시페이지 -->
<div v-else>
    <v-progress-linear
      indeterminate
      color="yellow darken-2"
    ></v-progress-linear>
    <br>
    <v-progress-linear
      indeterminate
      color="green"
    ></v-progress-linear>
    <br>
    <v-progress-linear
      indeterminate
      color="teal"
    ></v-progress-linear>
    <br>
    <v-progress-linear
      indeterminate
      color="blue"
    ></v-progress-linear>
    <br>
      <v-progress-linear
      indeterminate
      color="red lighten-3"
    ></v-progress-linear>
    <br>
    <v-progress-linear
      indeterminate
      color="cyan lighten-3"
    ></v-progress-linear>
    <br>
    <v-progress-linear
      indeterminate
      color="teal lighten-3"
    ></v-progress-linear>
    <br>
    <v-progress-linear
      indeterminate
      color="cyan"
    ></v-progress-linear>
  </div>
  <!-- 여기까지 임시페이지 -->
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>
    </v-container>
  </div>
</template>

<script>
import Calendar from "@/components/temp/Calendar.vue";
import MemberList from "@/components/temp/tabs/MemberList.vue"
import axios from "axios";
// @ is an alias to /src
export default {
  name: "StudyDetail",
  components: {
    Calendar,
    MemberList
  },
  data() {
    return {
      group: [],
      membersData: [],
      // 밑에는 그룹정보(나중에 활용)
      tab: null,
      items: [
        { tab: '스터디 소개', icon: 'mdi-book-open-variant', no: '1' },
        { tab: '스터디 그룹 멤버', icon: 'mdi-account-group', no: '2' },
        { tab: '기록', icon: 'mdi-timer', no: '3' },
      ],
    };
  },
  mounted () {
    this.id = this.$route.params.id
    this.getDetail()
  },
  methods: {
    enterMeetingRoom(gpNo) {
      this.$router.push('/meetingroom/'+gpNo)
    },
    async getDetail () {
      // const baseUrl = this.$store.state.baseUrld
      const apiUrl = '/study/user/' + this.id
      try {
        const res = await axios.get(apiUrl)
        this.group = JSON.parse(res.data.object).group
        this.group = JSON.parse(this.group)
        this.membersData = JSON.parse(res.data.object).joinList
      } catch (err) {
        this.$router.push('/main/')
        console.error(err)
      }
    },
    async quitGroup(gpNo) {
      const apiUrl = '/study/user/exit?no=' + gpNo
      try {
        const res = await axios.post(apiUrl)
        this.$router.push('/main/')
      } catch (err) {
        console.error(err)
      }
    }

  }
};
</script>

<style scoped>
img:hover{
  cursor: pointer;
}
</style>
