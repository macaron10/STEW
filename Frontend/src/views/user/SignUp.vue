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
export default {
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