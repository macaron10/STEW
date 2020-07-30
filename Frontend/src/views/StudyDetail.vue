<template>
  <div>
    <v-container>
      <v-row>
        <v-col class="mb-6 ">
          <div class="d-flex justify-center">
            <img @click="enterMeetingRoom(group.gpNo)" :src="group.gpImg" alt="image" />
          </div>
          <div>
            <h1>{{ group.gpNm }}</h1>
            <h4>{{ group.gpIntro }}</h4>
            <v-btn
              :to="{ name:'StudyUpdate', params: { id: id }}"
            >
              수정 버튼!(임시)
            </v-btn>
          </div>
        </v-col>
        <v-col class="mb-6">
          <Calendar />
        </v-col>
      </v-row>
    </v-container>
    <v-row justify="space-around">
      <v-switch v-model="icons" class="mx-2" label="Text + icons"></v-switch>
      <v-switch v-model="centered" class="mx-2" label="Centered" :disabled="vertical"></v-switch>
      <v-switch v-model="grow" class="mx-2" label="Grow"></v-switch>
      <v-switch v-model="vertical" class="mx-2" label="Vertical"></v-switch>
      <v-switch v-model="right" class="mx-2" label="Right"></v-switch>
      <v-col cols="12">
        <v-slider v-model="tabs" min="0" max="10" label="Tabs number"></v-slider>
      </v-col>
    </v-row>

    <v-tabs
      v-model="tab"
      background-color="deep-purple accent-4"
      class="elevation-2"
      dark
      :centered="centered"
      :grow="grow"
      :vertical="vertical"
      :right="right"
      :prev-icon="prevIcon ? 'mdi-arrow-left-bold-box-outline' : undefined"
      :next-icon="nextIcon ? 'mdi-arrow-right-bold-box-outline' : undefined"
      :icons-and-text="icons"
    >
      <v-tabs-slider></v-tabs-slider>

      <v-tab v-for="i in tabs" :key="i" :href="`#tab-${i}`">
        Tab {{ i }}
        <v-icon v-if="icons">mdi-phone</v-icon>
      </v-tab>

      <v-tab-item v-for="i in tabs" :key="i" :value="'tab-' + i">
        <v-card flat tile>
          <v-card-text>{{ text }}</v-card-text>
        </v-card>
      </v-tab-item>
    </v-tabs>
  </div>
</template>

<script>
import Calendar from "@/components/temp/Calendar.vue";
import axios from "axios";
// @ is an alias to /src
export default {
  name: "StudyDetail",
  components: {
    Calendar
  },
  data() {
    return {
      group: {},
      // 밑에는 그룹정보(나중에 활용)
      tab: null,
      icons: false,
      centered: false,
      grow: false,
      vertical: false,
      prevIcon: false,
      nextIcon: false,
      right: false,
      tabs: 3
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
      const baseUrl = this.$store.state.baseUrl
      const apiUrl = baseUrl + '/study/' + this.id
      try {
        const config = {
          headers: {
            Authorization: `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOlsic3RyaW5nIiwiUk9MRV9VU0VSIl0sImV4cCI6MTU5NTkyNjk3MywidXNlcklkIjoxLCJpYXQiOjE1OTU5MjUxNzN9.yJA-YJ_1QDVslPVcoT6xD8cad1SxP3iWR0AxT_vxkQiEB1CN-gdimy_mU96CGegEzkTy5JR0GhYQdE0ybwqqhQ `
          }
        }
        const res = await axios.get(apiUrl, config)
        this.group = res.data.object
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
