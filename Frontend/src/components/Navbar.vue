<template>
  <v-app-bar
    :clipped-left="$vuetify.breakpoint.lgAndUp"
    app
    color="#FFFFFF"
    dark
  >
    <v-app-bar-nav-icon @click.stop="$store.commit('drawerOnOff')" color="blue lighten-2"></v-app-bar-nav-icon>
    <v-toolbar-title
      style="width: 300px"
      class="ml-0 pl-10"
    >
      <v-btn
      icon
      large
      :to="{ name: 'Main' }"
      
    >
        <v-img
          :src="$store.state.baseUrl + '/study/thumb/2020/00/00/stew.png'"
          alt="Vuetify"
          :aspect-ratio="200"
          :min-height="30"
          :min-width="100"
        ></v-img>
    </v-btn>
      <span class="hidden-sm-and-down blue--text text--lighten-3"></span>
    </v-toolbar-title>
    <v-text-field
      solo-inverted
      hide-details
      color="blue lighten-2"
      outlined
      prepend-inner-icon="mdi-magnify"
      label="Search"
      class="hidden-sm-and-down"
      v-model="wordForSearching"
      @keypress.enter="searchingWord(wordForSearching)"
    ></v-text-field>
    <v-spacer></v-spacer>
    <v-btn 
      icon
      v-if="!isLogin"
      @click.stop= "siginInDialog = true"
      color="blue lighten-2"
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
            large color="primary" block=true 
            @click="signIn({'userEmail': user.userEmail, 'userPw':user.userPw}), siginInDialog = false"
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

    <v-btn icon v-if="!isLogin" :to="{ name: 'Signup' }">
      <v-icon>mdi-account-plus</v-icon>
    </v-btn>
    <v-btn icon v-if="isLogin" @click="logout">
      로그아웃
    </v-btn>
    <!-- 개인 메뉴 -->
    <div class="text-center">
      <v-menu v-if="isLogin" offset-y>
        <template v-slot:activator="{ on, attrs }">
            <v-btn
              icon
              :to="{ name: 'UserDetail' }"
              v-bind="attrs"
              v-on="on"
              color="blue lighten-2"
            ><v-icon>mdi-account-circle</v-icon>
            </v-btn>
        </template>
          <v-list>
            <v-list-item-group v-model="model">
              <v-list-item
                v-for="(item, i) in items"
                :key="i"
              >
                <v-list-item-icon>
                  <v-icon v-text="item.icon"></v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title v-text="item.text"></v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
      </v-menu>
    </div>
  </v-app-bar>
</template>

<script>
import axios from 'axios';
import { mapState, mapActions, mapMutations} from 'vuex';

export default {
    name: 'Navbar',
    computed: {
      ...mapState([ 
        "userInfo",
        "isLogin"
        ])
    },
    methods: {
      ...mapActions([
        "signIn",
        "logout"
        ]),
    },
    data () {
      return {
        wordForSearching: "",
        siginInDialog: false,
        user: {
          userEmail: "",
          userPw: "",
        },
        items: [
          {
            icon: 'mdi-inbox',
            text: 'Inbox',
          },
          {
            icon: 'mdi-star',
            text: 'Star',
          },
          {
            icon: 'mdi-send',
            text: 'Send',
          },
          {
            icon: 'mdi-email-open',
            text: 'Drafts',
          },
        ],
        model: 1,
      }
    }
}
</script>

<style>

</style>