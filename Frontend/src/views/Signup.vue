<template>
    <div id="Signup">
        <v-contaier>
            <h1> 회원가입 </h1>
            <form class="signup-form">

                <v-text-field
                    v-model="userId"
                    label="아이디"
                    required
                    :error-messages="nameErrors"
                    @input="$v.userId.$touch()"
                    @blur="$v.userId.$touch()"
                ></v-text-field>
                <!-- 아이디 확인 버튼 -->

                <v-text-field
                    v-model="userPwd"
                    label="비밀번호"
                    type="password"
                    required
                    @input="$v.userPwd.$touch()"
                    @blur="$v.userPwd.$touch()"
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

                <v-btn class="mr-4" @click="submit">submit</v-btn>
            </form>
        </v-contaier>
    </div>
</template>

<script>
  import { validationMixin } from 'vuelidate'
  import { required, maxLength, email } from 'vuelidate/lib/validators'

  export default {
    mixins: [validationMixin],

    validations: {
      name: { required, maxLength: maxLength(10) },
      email: { required, email },
      select: { required },
      checkbox: {
        checked (val) {
          return val
        },
      },
    },

    data: () => ({
      name: '',
      email: '',
      select: null,
      items: [
        'Item 1',
        'Item 2',
        'Item 3',
        'Item 4',
      ],
      checkbox: false,
    }),

    computed: {
      nameErrors () {
        const errors = []
        if (!this.$v.name.$dirty) return errors
        !this.$v.name.maxLength && errors.push('Name must be at most 10 characters long')
        !this.$v.name.required && errors.push('Name is required.')
        return errors
      },
      emailErrors () {
        const errors = []
        if (!this.$v.email.$dirty) return errors
        !this.$v.email.email && errors.push('Must be valid e-mail')
        !this.$v.email.required && errors.push('E-mail is required')
        return errors
      },
    },

    methods: {
      submit () {
        this.$v.$touch()
      },
    //   clear () {
    //     this.$v.$reset()
    //     this.name = ''
    //     this.email = ''
    //     this.select = null
    //     this.checkbox = false
    //   },
    },
  }
</script>

<style scoped>
    #Signup {
        margin: 50px;
    }
</style>