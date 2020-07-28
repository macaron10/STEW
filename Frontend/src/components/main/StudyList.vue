<template>
  <v-row>
    <v-col cols="12">
      <v-card>
        <v-container fluid>
          <v-row>
            <v-col
              v-for="group in groups"
              :key="group.gpNo"
              class="d-flex child-flex"
              cols="3"
            >
              <v-card flat tile class="d-flex">
                <v-img
                  :src="`https://picsum.photos/500/300?image=1`"
                  :lazy-src="`https://picsum.photos/10/6?image=1`"
                  aspect-ratio="1"
                  class="grey lighten-2"
                >
                  <template v-slot:placeholder>
                    <v-row
                      class="fill-height ma-0"
                      align="center"
                      justify="center"
                    >
                      <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                    </v-row>
                  </template>
                  <h4 class="white--text" >{{ group.gpNm }}</h4>
                </v-img>
              </v-card>
            </v-col>
          </v-row>
        </v-container>
      </v-card>
    </v-col>
  </v-row>
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
  },
  mounted () {
    this.getGroups()
  }

}
</script>

<style>

</style>