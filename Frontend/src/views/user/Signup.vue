<template>
    <div id="Signup">
      <!-- <ValidationObserver ref="observer" v-slot="{ validate, reset }"> -->
        <v-contaier>
            <h1> 회원가입 </h1>
            <form class="signup-form">
              <!-- <ValidationProvider v-slot="{ errors }" name="Name" rules="required|max:10"> -->

                <v-text-field
                    v-model="userId"
                    label="아이디"
                    required
                    :error-messages="nameErrors"
                ></v-text-field>
                <!-- 아이디 확인 버튼 -->

                <v-text-field
                    v-model="userPwd"
                    label="비밀번호"
                    type="password"
                    required
                ></v-text-field>

                <v-text-field
                    v-model="userNm"
                    label="이름"
                    required
                ></v-text-field>

                <v-text-field
                    v-model="email"
                    :error-messages="emailErrors"
                    label="이메일"
                    required
                    @input="$v.email.$touch()"
                    @blur="$v.email.$touch()"
                ></v-text-field>

                <v-text-field
                    v-model="userPhone"
                    label="연락처"
                    type="tel"
                ></v-text-field>

                <!-- <v-divider></v-divider> -->

                <v-radio-group v-model="userGender" row>
                    <v-radio class="user-gender" label="여자" value="female" />
                    <v-radio class="user-gender" label="남자" value="male" />
                </v-radio-group>

                <v-file-input 
                    v-model="userImg"
                    accept="image/*" 
                    label="프로필 이미지"
                    prepend-icon="mdi-camera"
                ></v-file-input>

                <v-text-field
                    v-model="userIntro"
                    label="자기소개"
                    :counter="100"
                ></v-text-field>

                <v-text-field
                    v-model="userGoalHr"
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
  import { extend } from 'vee-validate';
  import { required, email } from 'vee-validate/dist/rules';

  extend('required', {
    ...required,
    message: '{_filed_} 를 입력해주세요'
  })

  export default {
    components: {
      // ValidationProvider,
      // ValidationObserver,
    },
    data() {
      return {
        userId: '',
        userPw: '',
        userNm: '',
        userEmail: '',
        userPhone: '',
        userGender: '',
        userIntro: '',
        userImg: '',
        userGoalHr: '',
      }
    },

    methods: {
      signupHandler() {
        axios.post('http://localhost:8080/study/user/signup', {
          userId: this.userId,
          userPw: this.userPw,
          userNm: this.userNm,
          userEmail: this.userEmail,
          userPhone: this.userPhone,
          userGender: this.userGender,
          userIntro: this.userIntro,
          userImg: this.userImg,
          userGoalHr: this.userGoalHr,
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