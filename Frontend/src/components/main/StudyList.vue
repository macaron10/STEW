<template>
  <v-container fluid>
    <v-row>
      <v-col
        v-for="group in groups"
        :key="group.gpNo"
        class="d-flex child-flex"
        cols="3"
      >
        <v-card
          class="mx-auto"
          :to="'/study/' + group.gpNo"
        >
          <v-img
            src="https://cdn.vuetifyjs.com/images/cards/mountain.jpg"
            height="150"
          >
            <v-row align="end" class="my-3 lightbox white--text pa-2 fill-height">
              <v-col>
                <div class="body-1">인원수 1/5</div>
              </v-col>
            </v-row>
          </v-img>
          <v-list-item>
            <!-- 제목, 간단한설명, 태그, 썸네일, 인원수 -->
            <v-list-item-content>
              <v-list-item-title class="headline">{{ group.gpNm }}</v-list-item-title>
              <v-list-item-subtitle>추가할거면 하고(기능)</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-card-text>
            {{ group.gpIntro }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'StudyList',
  data () {
    return {
      groups: []
    }
  },
  methods: {
    async getGroups () {
      const baseUrl = this.$store.state.baseUrl
      const apiUrl = baseUrl + '/study/all'
      try {
        const res = await axios.get(apiUrl)
        // 개별요소 수정할때
        // for (const i of res.data) {
        //   i.created_at = String(i.created_at).substring(0, 10)
        //   i.half_rate = i.vote_average / 2
        //   if (i.popularity !== 0) {
        //     i.poster_path = this.imageURL + i.poster_path
        //     i.backdrop_path = this.imageURL + i.backdrop_path
        //   }
        // }
        this.groups = res.data.object
        const listLength = this.groups.length
        // const listSize = this.pageSize
        // const page = Math.floor((listLength - 1) / listSize) + 1
        // this.movieSize = page
        console.log(this.groups)
      } catch (err) {
        console.error(err)
      // } finally {
      //   this.sortBy(this.sortedBy)
      //   this.showPagination = true
      }
    },
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
    moveToDetail () {
      return console.log('ㅎㅇㅎㅇ')
    }
  },
  mounted () {
    this.getGroups()
  }

}
</script>

<style>

</style>