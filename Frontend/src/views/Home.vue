<template>
<div>
  <v-carousel>
    <v-carousel-item
      v-for="(item,i) in items"
      :key="i"
      :src="item.src"
      reverse-transition="fade-transition"
      transition="fade-transition"
    >
    </v-carousel-item>
  </v-carousel>

  <v-img
    dark
    :src= "$store.state.baseUrl + '/study/thumb/2020/00/00/home2.jpg'"
  >
    <v-row class="my-10"></v-row>
    <v-row class="my-10"></v-row>
    <v-row class="my-10"></v-row>
    <v-row class="my-10"></v-row>
    <v-row
      align="center"
      justify="center"
    >
      <v-col class="text-center" cols="12">
        <h1 class="display-1 font-weight-thin mb-4">친구들과 함께 공부해요</h1>
        <h2 class="subheading">STudy EveryWhere!</h2>
      </v-col>
    </v-row>
  </v-img>
  <v-img  :src= "$store.state.baseUrl + '/study/thumb/2020/00/00/home3.jpg'"></v-img>
</div>
</template>

<script>
// @ is an alias to /src

export default {
  name: "Home",
  components: {
  },
  data () {
    return {
      items: [
        {
          src: this.$store.state.baseUrl + '/study/thumb/2020/00/00/studyeverywhere.jpg',
        },
        {
          src: 'https://cdn.vuetifyjs.com/images/carousel/sky.jpg',
        },
        {
          src: 'https://cdn.vuetifyjs.com/images/carousel/bird.jpg',
        },
        {
          src: 'https://cdn.vuetifyjs.com/images/carousel/planet.jpg',
        },
      ],
    }
  },
};
</script>
