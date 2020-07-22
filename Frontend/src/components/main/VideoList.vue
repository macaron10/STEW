<template>
  <v-row>
    <v-col cols="12">
      <v-card>
        <v-container fluid>
          <v-row>
            <v-col
              v-for="n in 16"
              :key="n"
              class="d-flex child-flex"
              cols="3"
            >
              <v-card flat tile class="d-flex">
                <v-img
                  :src="`https://picsum.photos/500/300?image=${n * 5 + 10}`"
                  :lazy-src="`https://picsum.photos/10/6?image=${n * 5 + 10}`"
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
                  <h4>동영상과 동영상 설명이 들어갈 자리</h4>
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
export default {
  name: 'VideoList'
}
</script>

<style>

</style>