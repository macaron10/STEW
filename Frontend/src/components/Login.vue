<template>
  <!-- <v-row justify="center">
    <v-btn
      color="primary"
      dark
      @click.stop="siginInDialog = true"
    >
      Login Button
    </v-btn>

  </v-row> -->
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
</template>

<script>
  export default {
    name: "SignIn",
    props: {
      showdialog: Boolean
    },
    data () {
      return {
        siginInDialog: false,
      }
    },
    created() {
      this.siginInDialog = this.showdialog;
    }
  }
</script>