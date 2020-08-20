<template>
  <v-carousel
    height=""
    cycle
    show-arrows-on-hover
    hide-delimiter-background
    hide-delimiters>
    <v-carousel-item
      v-for="(item,i) in items"
      :key="i"
      :src="item.src"
      reverse-transition="fade-transition"
      transition="fade-transition"
    ></v-carousel-item>
  </v-carousel>
  
</template>

<script>
export default {
  name: 'Banner',
  data () {
    return {
      items: [
        {
          src: this.$store.state.comm.baseUrl + '/image/main/studyeverywhere.png',
        },
        {
          src: this.$store.state.comm.baseUrl + '/image/main/corona19.png',
        },
        {
          src: this.$store.state.comm.baseUrl + '/image/main/coupon.png',
        },
        {
          src: this.$store.state.comm.baseUrl + '/image/main/stewbox.png',
        },
      ],
    }
  },
}
</script>

<style>

</style>