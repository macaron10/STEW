<template>
  <v-app-bar
    :clipped-left="$vuetify.breakpoint.lgAndUp"
    app
    color="blue darken-3"
    dark
  >
    <v-app-bar-nav-icon @click.stop="drawerOnOff"></v-app-bar-nav-icon>
    <v-toolbar-title
      style="width: 300px"
      class="ml-0 pl-4"
    >
      <v-btn
      icon
      large
      :to="{ name: 'Main' }"
    >
      <v-avatar
        size="32px"
        item
      >
        <v-img
          src="https://cdn.vuetifyjs.com/images/logos/logo.svg"
          alt="Vuetify"
        ></v-img></v-avatar>
    </v-btn>
      <span class="hidden-sm-and-down">Cam Study</span>
    </v-toolbar-title>
    <v-text-field
      flat
      solo-inverted
      hide-details
      prepend-inner-icon="mdi-magnify"
      label="Search"
      class="hidden-sm-and-down"
    ></v-text-field>
    <v-spacer></v-spacer>
    <v-btn 
      icon
      @click.stop= "siginInDialog = true"
    >
      <v-icon>mdi-account</v-icon>
    </v-btn>
    <!-- 로그인 모달 창 -->
    <v-dialog
      v-model="siginInDialog"
      max-width="350"
    >
      <v-card>
        <v-card-title class="headline">Login</v-card-title>

        <v-col>
          <v-text-field
            label="userEmail"
            placeholder="Email"
            type="email"
            v-model="user.userEmail"
            solo
          ></v-text-field>
          <v-text-field
            label="userPw"
            placeholder="PASSWORD"
            type="password"
            v-model="user.userPw"
            solo
          ></v-text-field>
          <v-btn 
            large color="primary" block="true" 
            @click="signIn(user.userEmail, user.userPw)"
          >로그인</v-btn>
        </v-col>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="gray"
            text small
            @click="siginInDialog = false"
          >
            아이디/비밀번호 찾기
          </v-btn>

          <v-btn
            :to="{ name: 'Signup' }"
            color="light gray"
            text small
            @click="siginInDialog = false"
          >
            회원가입
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-btn icon :to="{ name: 'Signup' }">
      <v-icon>mdi-account-plus</v-icon>
    </v-btn>
    <v-btn
      icon
      :to="{ name: 'UserDetail' }"
    ><v-icon>mdi-account-circle</v-icon>
    </v-btn>
  </v-app-bar>
</template>

<script>
import axios from 'axios';
import { mapState, mapActions } from 'vuex';

export default {
    name: 'Navbar',
    computed: {
      ...mapState(["userInfo"])
    },
    methods: {
      drawerOnOff(event) {
        this.$emit('drawer-onoff')
      },
      ...mapActions([
        "signIn"
        ]),
      // signIn() {
      //   axios.post('http://localhost:8399/api/user/signin', {
      //     userEmail: this.user.userEmail,
      //     userPw: this.user.userPw
      //   })
      //   .then(res => {
      //     console.log(res);
      //   }) 
      // },
    },
    data () {
      return {
        siginInDialog: false,
        user: {
          userId: "",
          userPwd: "",
        },
      }
    }
}
</script>

<style>

</style>