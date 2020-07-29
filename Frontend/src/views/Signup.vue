<template>
    <div id="Signup">
      <!-- <ValidationObserver ref="observer" v-slot="{ validate, reset }"> -->
        <v-contaier>
            <h1> 회원가입 </h1>
            <form class="signup-form">
              <!-- <ValidationProvider v-slot="{ errors }" name="Name" rules="required|max:10"> -->

                <v-text-field
                    v-model="user.email" name="email"
                    :error-messages="emailErrors"
                    label="이메일"
                    required
                    @input="$v.email.$touch()"
                    @blur="$v.email.$touch()"
                />

                <v-btn>
                  이메일 확인
                </v-btn>

                <v-text-field
                    v-model="user.pwd" name="pwd"
                    label="비밀번호"
                    type="password"
                    required
                />

                <v-text-field
                  v-model="user.pwdCheck" name="pwdCheck"
                  label="비밀번호 확인"
                  type="password"
                  required
                />

                <!-- {{ computedCheckPwd }} -->

                <v-text-field
                    v-model="user.name" name="name"
                    label="이름"
                    required
                />

                <v-text-field
                    v-model="user.phone" name="phone"
                    label="연락처"
                    type="tel"
                />

                <!-- <v-divider></v-divider> -->

                <v-radio-group v-model="user.gender" row>
                    <v-radio class="user-gender" label="여자" value="F" />
                    <v-radio class="user-gender" label="남자" value="M" />
                </v-radio-group>

                <v-file-input 
                    v-model="user.img"
                    accept="image/*" 
                    label="프로필 이미지"
                    prepend-icon="mdi-camera"
                ></v-file-input>

                <v-text-field
                    v-model="user.intro"
                    label="자기소개"
                    :counter="100"
                ></v-text-field>

                <v-text-field
                    v-model="user.goalHr"
                    label="하루 목표 시간"
                    type="number"
                ></v-text-field>

                <v-btn class="mr-4" @click="signupHandler">회원가입</v-btn>
              <!-- </ValidationProvider> -->

            </form>
        </v-contaier>
      <!-- </ValidationObserver> -->
    </div>
</template>

<script>
  import axios from 'axios';
  import VeeValidate, { Validator } from 'vee-validate';
  import KoreanValidate from 'vee-validate/dist/locale/ko';

  // Validator.localize('ko', KoreanValidate);
  // Vue.use(VeeValidate, { locale: KoreanValidate })
  
  // extend('required', {
  //   ...required,
  //   message: '{_filed_} 를 입력해주세요'
  // })

  export default {
    components: {
      // ValidationProvider,
      // ValidationObserver,
    },
    data() {
      return {
        user: {
          email: '',
          pwd: '',
          pwdCheck: '',
          name: '',
          phone: '',
          gender: '',
          img: '',
          intro: '',
          goalHr: ''
        },
        idCheck: false,
        pwdCheck: false,
      }
    },
    computed: {
      // computedCheckPwd() {
      //   if (this.user.pwd === this.user.pwdCheck) {
      //     this.pwdCheck = true;
      //     return "비밀번호가 확인되었습니다.";
      //   } else {
      //     return "비밀번호를 확인해주세요.";
      //   }
      // }
    },
    methods: {
      formCheck() {
        if (this.idCheck && this.pwdCheck) {
          this.signupHandler();
        }
      },

      signupHandler() {
        console.log(this.user);
        axios.post('http://localhost:8399/api/user/signup', {
          userEmail: this.user.email,
          userPw: this.user.pwd,
          userNm: this.user.name,
          userPhone: this.user.phone,
          userGender: this.user.gender,
          userImg: this.user.img,
          userIntro: this.user.intro,
          userGoalHr: this.user.goalHr,
          roles: "USER"
        })
        .then(({ data }) => {
          let msg = '다시 시도해주세요';
          if (data === 'success') {
            msg = '회원가입 성공';
          }
          alert(msg);
          this.moveMain();
        })
      },
      moveMain() {
        this.$router.push('/main');
      },
      // submit () {
      //   this.$refs.observer.validate()
      // },
    },
  }
</script>

<style scoped>
    #Signup {
        margin: 50px 150px;
    }
</style>