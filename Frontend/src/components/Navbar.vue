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
            label="userId"
            placeholder="ID"
            solo
          ></v-text-field>
          <v-text-field
            label="userPwd"
            placeholder="PASSWORD"
            solo
          ></v-text-field>
          <v-btn large color="primary" block="true">로그인</v-btn>
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
    <!-- 개인 메뉴 -->
    <div class="text-center">
      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
            <v-btn
              icon
              :to="{ name: 'UserDetail' }"
              v-bind="attrs"
              v-on="on"
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

export default {
    name: 'Navbar',
    methods: {
      drawerOnOff(event) {
        this.$emit('drawer-onoff')
      },
    },
    data () {
      return {
        siginInDialog: false,
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