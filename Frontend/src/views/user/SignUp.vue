<template>
  <v-container fill-height>
    <v-row align="center" justify="center">
      <v-col cols="10">
        <sign-up-form></sign-up-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import SignUpForm from '@/components/auth/SignUpForm'
import { mapGetters } from 'vuex'

export default {
  beforeRouteEnter(to, from, next){   
      next(vm => {
          vm.$data.prevPage = from.name == 'Login' ? 'Main' : from.name;
      })
  },
  data(){
      return{
          prevPage: '',
      }
  },
  computed: {
      ...mapGetters('auth', [
          'loginStatus',
      ]),
  },
  watch:{
      loginStatus: function(){
        console.log(this.prevPage)
        // this.$router.push({ name: this.prevPage }).catch(()=>({}));
        this.$router.push({ name: 'Main' })
      }
  },
  components: {
    SignUpForm,
  },
  mounted() {
      this.$vuetify.goTo(0, {
          duration: 100,
          offset: 0
      })
  },
}
</script>

<style>

</style>