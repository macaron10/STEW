<template>
  <!-- sm="10" md="8" lg="6" -->
    <v-layout justify-center>
      <v-form ref="form" id="form">
        <h1>반가워요!</h1>
        <v-text-field dense outlined
          :color="baseColor"
          prepend-inner-icon="mdi-account"
          v-model="user.email"
          :append-icon="idCheck ? 'mdi-checkbox-marked-circle' : 'mdi-checkbox-blank-circle-outline'"
          @click:append="idCheckHandler"
          :rules="[
            () => !!user.email || '이메일을 입력해주세요.',
            () => /.+@.+\..+/.test(user.email) || '이메일 형식이 올바르지 않습니다.', 
            () => /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/.test(user.email) || '이메일 형식이 올바르지 않습니다.', 
          ]"
          label="이메일 *"
          :clearable=false
          required
        />
        <!-- <v-btn icon depressed
          tabindex="-1"
          @click="idCheckHandler"
        >
          <v-icon
          :color="idCheckColor"
          >{{ idCheck ? 'mdi-checkbox-marked-circle' : 'mdi-checkbox-blank-circle-outline' }}</v-icon>
        </v-btn> -->
        <v-text-field dense outlined
          prepend-inner-icon="mdi-lock"
          :color="baseColor"
          v-model="user.pwd"
          :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
          @click:append="showPassword = !showPassword"
          v-bind:type="showPassword ? 'text' : 'password'"
          :rules="[
            () => !!user.pwd || '비밀번호를 입력해주세요.',
            () => user.pwd.length >= 6 || '6자 이상 입력해주세요.',
          ]"
          label="비밀번호 *"
          counter="15"
          required
        />
        <v-text-field dense outlined
          prepend-inner-icon="mdi-lock"
          :color="baseColor"
          v-model="user.pwdCheck"
          label="비밀번호 확인 *"
          :rules="[
            () => !!user.pwdCheck || '비밀번호를 재입력해주세요.',
            () => user.pwd === user.pwdCheck || '비밀번호와 일치하지 않습니다.'
          ]"
          counter="15"
          type="password"
          required
        />
        <v-text-field dense outlined
          :color="baseColor"
          v-model="user.name"
          :rules="[() => !!user.name || '이름을 입력해주세요.']"
          label="이름 *"
          required
        />
        <v-subheader>선택사항</v-subheader>

        <v-file-input dense outlined
          :color="baseColor"
          v-model="user.img"
          accept="image/*" 
          label="프로필 이미지"
          prepend-icon="mdi-camera" show-size
          :rules="[
              () => !user.img || user.img.size <= 1000000 || '1MB 이하의 파일만 등록 가능합니다.',
              () => !user.img || correctExt || '지원하지 않는 확장자입니다.'
          ]"
          @change="confirmExt"
        ></v-file-input>
        <v-textarea dense outlined
          :color="baseColor"
          v-model="user.intro"
          label="나의 목표"
          auto-grow rows="1" row-height="15"
          counter
          maxlength="100"
        ></v-textarea>
        <v-text-field  dense outlined
          :color="baseColor"
          v-model="user.goalHr"
          label="하루 목표 시간"
          type="number"
          min=0
          max=24
          background-color="lightgray"
          :rules="[
            () => (!user.goalHr || (0 <= user.goalHr && user.goalHr <= 24)) || '0 ~ 24'
          ]"
        ></v-text-field>
        <v-btn block dark 
          :color="baseColor"
          @click.once="formCheckHandler"
          :key="btnKey"
        >
          <v-icon class="mr-4"
          >mdi-account-plus</v-icon>
        회원가입</v-btn>
        <v-divider class="my-6"></v-divider>
        <social-form></social-form>
      </v-form>
    </v-layout>
</template>

<script>
  import axios from 'axios';
  import SocialForm from '@/components/auth/SocialForm'
  import { mapGetters, mapActions } from 'vuex'

  export default {
    beforeRouteEnter(to, from, next){
      next(vm => {
        vm.$data.prevPage = from.next;
      })
    },
    components: {
      SocialForm
    },
    data() {
      const formData = new FormData();

      return {
        prevPage: '',
        user: {
          email: '',
          pwd: '',
          pwdCheck: '',
          name: '',
          img: undefined,
          intro: '',
          goalHr: ''
        },
        baseColor: '#64b4f6',
        idCheck: false,
        idCheckColor: "",
        showPassword: false,
        correctExt: false,
        formData,
        btnKey: 1,
      }
    },
    computed: {
      isEnabledEmail() {
        return this.$refs.form.$children[0].validate();
      },
      emailChanged() {
        return this.user.email;
      },
      ...mapGetters('auth', [
          'loginStatus',
      ])
    },
    watch: {
      emailChanged: function(){
        this.idCheck = false;
        this.idCheckColor = "";
      }
    },
    methods: {
      ...mapActions('auth', [
        'signIn'
      ]),
      confirmExt() {
        this.correctExt = false;
        if (this.user.img) {
          const ext = this.user.img.name.substring(this.user.img.name.lastIndexOf(".")+1, this.user.img.name.length).toLowerCase();
          const imgExts = "xbm,tif,pjp,pjpeg,svgz,jpg,jpeg,ico,tiff,gif,svg,bmp,png,gfif,webp";
          const eachExts = imgExts.split(",");
  
          for (let i = 0; i < eachExts.length; i++) {
            if (ext == eachExts[i]) this.correctExt = true;
          }
        }
      },

      idCheckHandler() {
        if(!this.isEnabledEmail) return;
        axios.get('/user/check', {
          params: {
            userEmail: this.user.email
          }
        })
        .then(({ data }) => {
          if (data.msg === "success" && data.object) {
            this.idCheck = true;
            this.idCheckColor = "#64b4f6"
          }else {
            this.idCheckColor = "red"
            alert('이미 사용중인 이메일입니다')
          }
        })
      },

      formCheckHandler() {
        if (this.$refs.form.validate()) {
          if (this.idCheck) {
            this.makeFormData();
            this.signupHandler();
          } else {
            this.btnKey++;
            alert("이메일 확인을 눌러주세요!!")
          }
        } else {
          this.btnKey++;
          alert("입력폼을 다시 확인해주세요!!");
        }
      },

      makeFormData() {
        this.formData = new FormData();

        this.formData.append('userEmail', this.user.email);
        this.formData.append('userPw', this.user.pwd);
        this.formData.append('userNm', this.user.name);
        if (this.user.img instanceof File) {
          this.formData.append('userImg', this.user.img);
        }
        this.formData.append('userIntro', this.user.intro);
        this.formData.append('userGoalHr', Number(this.user.goalHr));
      },
      signInHandler(){
        this.signIn({'userEmail': this.user.email, 'userPw':this.user.pwd})
      },
      signupHandler() {
        const config = {
          headers: {
            'Content-Type' : 'multipart/form-data',
          }
        }

        axios.post('/user/signup', this.formData, config)
        .then(({ data }) => {
          let msg = '다시 시도해주세요';

          if (data.msg === 'success') {
            msg = '회원가입 성공';
            this.signInHandler();
            this.moveMain();
          }
          alert(msg);
        })
      },

      moveMain() {
        this.$router.push({ name: 'Main' });
      },
    },
  }
</script>

<style scoped>
</style>