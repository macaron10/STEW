<template>
  <v-container fluid>
    <v-row>
      <v-col
        v-for="group in groups"
        :key="group.gpNo"
        class="d-flex child-flex"
        cols="3"
      >
          <!-- :to="'/study/' + group.gpNo" -->
        <v-card
          class="mx-auto"
          @click="toDetail(group)"
        >
          <v-img
            :src="$store.state.baseUrl + '/study/thumb' + group.gpImg"
            height="150"
          >
            <v-row align="end" class="my-3 lightbox white--text pa-2 fill-height">
              <v-col>
                <div class="body-1">인원수 {{ group.gpCurNum }}/{{ group.gpMaxNum }}</div>
              </v-col>
            </v-row>
          </v-img>
          <v-list-item>
            <!-- 제목, 간단한설명, 태그, 썸네일, 인원수 -->
            <v-list-item-content>
              <v-list-item-title class="headline">{{ group.gpNm }}</v-list-item-title>
              <v-list-item-subtitle></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-card-text>
            {{ group.gpIntro }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-dialog
      v-model="dialog"
      width="500"
    >
      <v-card>
        <v-card-title class="headline grey lighten-2">
          {{ selectedGroup.gpNm }} 에 가입하시겠습니까?
        </v-card-title>

        <v-card-text>
         {{selectedGroup.gpIntro}}
        </v-card-text>

        <v-divider></v-divider>
        <v-textarea
          v-model="message"
          color="teal"
        >
          <template v-slot:label>
            <div>
              보낼 메세지 <small>(optional)</small>
            </div>
          </template>
        </v-textarea>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="signUpGroup(selectedGroup.gpNo)"
          >
            가입신청
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            닫기
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios'
import { mapState, mapActions } from 'vuex'
import querystring from 'querystring'

export default {
  name: 'StudyList',
  data () {
    return {
      myGroups: {},
      dialog: false,
      selectedGroup: {},
      snackbar: false,
      message: ""
    }
  },
  methods: {
    ...mapActions('sg', ['getGroups']),
    // 솔팅 함수
    // sortBy (prop) {
    //   if (prop === 'popularity') {
    //     this.sortCriteria = 'Most Popular'
    //   } else if (prop === 'vote_average') {
    //     this.sortCriteria = 'Highest Rated'
    //   } else if (prop === 'release_date') {
    //     this.sortCriteria = 'Release Date'
    //   }
    //   this.sortedBy = prop
    //   this.movies.sort((a, b) => (a[prop] > b[prop] ? -1 : 1))
    // }
    async getMyGroups () {
      const apiUrl = '/study/user/my'
      try {
        const res = await axios.get(apiUrl)
        console.log(res)
        this.myGroups = res.data.object
      } catch (err) {
        console.error(err)
      }
    },
    toDetail(group) {
      this.selectedGroup = group
      let flag = false
      for (let i=0; i<this.myGroups.length; i++){
        if (group.gpNo === this.myGroups[i].gpNo) {
          flag = true
          // this.$router.push('/study/' + gpNo)
        }
      }
      if (flag === true) {
        this.$router.push('/study/' + group.gpNo)
      } else {
        this.dialog = true
      }
    },
    // signUpGroup(gpNo) {
    //   const apiUrl = '/study/user/req'
    //   this.gpNoData.gpNo = gpNo
    //   console.log(this.gpNoData)
    //   axios.post(apiUrl, this.gpNoData)
    //   .then((res) => {
    //     console.log(res)
    //     this.dialog = false
    //     this.snackbar = true
    //     })
    // },
    async signUpGroup(gpNo) {

      const apiUrl = '/study/user/req?gpNo='+gpNo
      const msg = {
        "reqMsg": this.message
      }
      try {
        const res = await axios.post(apiUrl, querystring.stringify({ reqMsg: this.message }))
        console.log(msg)
        console.log(res)
        this.dialog = false
        this.snackbar = true
      } catch (err) {
        console.error(err)
      }
    }
  },
  computed: {
    // groups () { return this.$store.state.sg.groups }
    ...mapState('sg', ['groups'])
  },
  mounted () {
    this.getGroups()
    if (this.$store.state.isLogin) {
      this.getMyGroups()
    }
  }

}
</script>

<style>

</style>