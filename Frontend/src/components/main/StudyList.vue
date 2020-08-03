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
  </v-container>
</template>

<script>
import axios from 'axios'
import { mapState, mapActions } from 'vuex'

export default {
  name: 'StudyList',
  data () {
    return {
    }
  },
  methods: {
    ...mapActions(['getGroups']),
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
    getSearchedGroup() {
      return {}
    }
  },
  computed: {
    // groups () { return this.$store.state.sg.groups }
    ...mapState('sg', ['groups'])
  },
  mounted () {
    this.getGroups()
    console.log(this.groups, '이거')
  }

}
</script>

<style>

</style>