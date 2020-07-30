<template>
    <div id="Signup">
        <h1> 회원가입 </h1>

        <v-row justify="center">
          <v-col cols="12" sm="10" md="8" lg="6">
            <v-form ref="form">
                <v-text-field
                    v-model="user.email"
                    :rules="[
                      () => !!user.email || '이메일을 입력해주세요.',
                      () => /.+@.+\..+/.test(user.email) || '이메일 형식이 올바르지 않습니다.',
                    ]"
                    label="이메일 *"
                    :readonly="idCheck"
                    :clearable=false
                    required
                />

                  <!-- :disabled="!disableCheck" -->
                <v-btn 
                  @click="idCheckHandler" 
                >
                  이메일 확인
                </v-btn>

                <v-text-field
                    v-model="user.pwd"
                    :rules="[
                      () => !!user.pwd || '비밀번호를 입력해주세요.',
                      () => user.pwd.length >= 6 || '6자 이상 입력해주세요.',
                      addressCheck
                    ]"
                    label="비밀번호 *"
                    type="password"
                    counter="15"
                    required
                />

                <v-text-field
                  v-model="user.pwdCheck"
                  label="비밀번호 확인 *"
                  :rules="[
                    () => !!user.pwdCheck || '비밀번호를 재입력해주세요.',
                    () => user.pwd === user.pwdCheck || '비밀번호와 일치하지 않습니다.'
                  ]"
                  type="password"
                  counter="15"
                  required
                />

                <v-text-field
                    v-model="user.name"
                    :rules="[() => !!user.name || '이름을 입력해주세요.']"
                    label="이름 *"
                    required
                />

                <v-subheader>선택사항</v-subheader>

                <v-file-input 
                    v-model="user.img"
                    accept="image/*" 
                    label="프로필 이미지"
                    prepend-icon="mdi-camera"
                ></v-file-input>

                <v-textarea
                    v-model="user.intro"
                    label="자기소개"
                    auto-grow rows="1" row-height="15"
                    counter
                    maxlength="100"
                ></v-textarea>

                <v-text-field
                    v-model="user.goalHr"
                    label="하루 목표 시간"
                    type="number"
                    background-color="lightgray"
                ></v-text-field>

                <v-btn class="mr-4" @click="formCheckHandler">회원가입</v-btn>
            </v-form>
          </v-col>
        </v-row>
    </div>
</template>

<script>
  import axios from 'axios';

  export default {
    components: {},
    data() {
      return {
        user: {
          email: '',
          pwd: '',
          pwdCheck: '',
          name: '',
          img: '',
          intro: '',
          goalHr: ''
        },
        idCheck: false,
      }
    },
    computed: {
      disableCheck() {
        return this.$refs.form.$children[0].validate();
      }
    },
    methods: {
      idCheckHandler() {
        axios.get('http://localhost:8399/api/user/check', {
          params: {
            email: this.user.email
          }
        })
        .then(({ data }) => {
          console.log(data);
          if (data.msg === "success" && data.object) {
            this.idCheck = true;
          }
        })
      },

      formCheckHandler() {
        // console.log(this.$refs.form.validate());
        // console.log(this.$refs.form.$children[0].validate());

        if (this.$refs.form.validate()) {
          if (this.idCheck) {
            this.signupHandler();
          } else {
            alert("이메일 확인을 눌러주세요!!")
          }
        } else {
          alert("입력폼을 다시 확인해주세요!!");
        }
      },

      signupHandler() {
        // console.log(this.user);
        axios.post('http://localhost:8399/api/user/signup', {
          userEmail: this.user.email,
          userPw: this.user.pwd,
          userNm: this.user.name,
          userImg: this.user.img,
          userIntro: this.user.intro,
          userGoalHr: this.user.goalHr,
        })
        .then(({ data }) => {
          let msg = '다시 시도해주세요';
          console.log("로그인결과임다");
          console.log(data);

          if (data.msg === 'success') {
            msg = '회원가입 성공';
            this.moveMain();
          }
          alert(msg);
        })
      },

      moveMain() {
        this.$router.push('/main');
      },
    },
  }
</script>

<style scoped>
</style>