<template>
  <div>
    <v-row justify="center" id="userpage" class="mx-10 my-10">
      <v-col cols="12" sm="10" md="7" class="flex-column">
        <v-row class="d-flex align-center mr-2">
          <v-col class="d-flex flex-column justify-center">
            <img :src="imgSrc" ref="imgpreview" id="userImg" class="mr-10 ml-5 my-5" />
            <v-row class="d-flex flex-nowrap justify-center">
              <v-file-input v-model="userInfo.userImg" accept="image/*" @change="changeImg"></v-file-input>
              <v-btn depressed rounded small class="mt-5 mx-1" @click="resetImg">초기화</v-btn>
            </v-row>
          </v-col>
          <v-col class="d-flex flex-column justify-center">
            <v-row class="mb-5">
              <h1 class="text-sm-h4 text-h5 font-weight-bold" style="color:black">{{userInfo.userEmail}}</h1>
            </v-row>
            <v-row>
              <v-text-field
                v-model="userInfo.userNm"
                label="이름"
                class="br-2"
                single-line
                solo
                id="userNm"
              ></v-text-field>
            </v-row>
          </v-col>
        </v-row>
        <v-row class="d-flex flex-column mx-auto mt-5">
          <label for="goalHr" class="mr-5 my-3">하루 목표 공부 시간</label>
          <v-text-field
            id="goalHr"
            v-model="userInfo.userGoalHr"
            class="flex-grow-0 my-1"
            type="number"
            suffix="시간"
            min="0"
            max="24"
            solo
          ></v-text-field>
        </v-row>
        <v-row>
          <v-textarea v-model="userInfo.userIntro" label="나의 목표" class="ma-3" solo counter="30"></v-textarea>
        </v-row>

        <v-row class="d-flex justify-center">
          <v-btn
            v-if="userInfo.type ? userInfo.type.match('stew') : false"
            depressed
            class="mb-2 mx-5"
            @click.stop="updatePwdDialog=true"
          >비밀번호 변경</v-btn>
          <v-dialog v-model="updatePwdDialog" persistent max-width="300">
            <v-card>
              <v-card-title class="headline">비밀번호 변경</v-card-title>
              <v-card-text>
                <v-text-field
                  v-model="updatePwd.origin"
                  label="현재 비밀번호"
                  type="password"
                  counter="15"
                  required
                  :rules="[
                  () => !!updatePwd.origin || '비밀번호를 입력해주세요.',
                  ]"
                />

                <v-text-field
                  v-model="updatePwd.new"
                  label="새로운 비밀번호"
                  type="password"
                  counter="15"
                  required
                  :rules="[
                  () => !!updatePwd.new || '새로운 비밀번호를 입력해주세요.',
                  () => updatePwd.new.length >= 6 || '6자 이상 입력해주세요.'
                  ]"
                />

                <v-text-field
                  v-model="updatePwd.newChk"
                  label="새로운 비밀번호 확인"
                  type="password"
                  counter="15"
                  required
                  :rules="[
                  () => !!updatePwd.newChk || '새로운 비밀번호를 재입력해주세요.',
                  () => updatePwd.new === updatePwd.newChk || '새 비밀번호와 일치하지 않습니다.'
                  ]"
                />
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="error" text @click="updatePwdDialog = false">취소</v-btn>
                <v-btn color="primary" text @click="checkUserPwd(updatePwd.origin, 'update')">변경</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-btn depressed color="primary" class="mb-2 mx-5" @click="updateUserInfo">프로필 설정 완료</v-btn>
          <!-- <v-btn depressed color="error" class="mb-2 mx-5" @click="deleteUserDialog=true">회원 탈퇴</v-btn> -->
          <v-dialog v-model="deleteUserDialog" persistent max-width="300">
            <v-card>
              <v-card-title class="headline">회원 탈퇴</v-card-title>
              <v-card-text>
                <v-text-field
                  v-model="updatePwd.origin"
                  label="비밀번호"
                  type="password"
                  counter="15"
                  required
                  :rules="[
                  () => !!updatePwd.origin || '비밀번호를 입력해주세요.',
                  ]"
                />
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="error" text @click="deleteUserDialog = false">취소</v-btn>
                <v-btn color="primary" text @click="checkUserPwd(updatePwd.origin, 'del')">탈퇴</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import axios from "axios";
import querystring from "querystring";

export default {
  name: "userPage",

  components: {},

  data() {
    return {
      updatePwdDialog: false,
      deleteUserDialog: false,
      userInfo: {},
      imgInfo: {
        originSrc: this.$store.state.comm.baseUrl + "/image/user",
        default: this.$store.state.comm.baseUrl + "/image/user/userDefault.png",
        updateImg: false,
        correctExt: true
      },
      updatePwd: {
        origin: "",
        new: "",
        newChk: ""
      },
      imgSrc: "",
      formData: null
    };
  },

  created() {
    this.getUserInfo();
  },

  methods: {
    getUserInfo() {
      axios.get("/user").then(({ data }) => {
        this.userInfo = data.object;
        this.imgInfo.originSrc = this.imgInfo.originSrc.concat(
          this.userInfo.userImg
        );
        this.imgSrc = this.imgInfo.originSrc;
        this.userInfo.userImg = [];
      });
    },

    changeImg(e){
      // const file = e.target.files[0]; // Get first index in files
      this.imgInfo.correctExt = false;
      if (e && e.size < 3000000) {
        this.confirmExt();
          
        if (this.imgInfo.correctExt) {
          console.log(this.$refs.imgpreview.src);
          console.log(URL.createObjectURL(e));
          this.$refs.imgpreview.src = e ? URL.createObjectURL(e) : this.imgInfo.originSrc;
          this.imgInfo.updateImg = true;
        } else {
          alert("지원하지 않는 확장자입니다.");
          this.userInfo.userImg = [];
        }
      } else {
        if (e && e.size > 3000000)  alert("3MB 이하의 파일만 등록 가능합니다.");
        this.userInfo.userImg = [];
        this.$refs.imgpreview.src = this.imgInfo.originSrc;
      }
    },

    resetImg(){
      this.imgInfo.updateImg = true;
      this.$refs.imgpreview.src = this.imgInfo.default;
      this.userInfo.userImg = [];
    },

    confirmExt() {
      this.imgInfo.correctExt = false;
      const ext = this.userInfo.userImg.name
        .substring(
          this.userInfo.userImg.name.lastIndexOf(".") + 1,
          this.userInfo.userImg.name.length
        )
        .toLowerCase();
      const imgExts = "xbm,tif,pjp,pjpeg,svgz,jpg,jpeg,ico,tiff,gif,svg,bmp,png,gfif,webp";
      const eachExts = imgExts.split(",");

      for (let i = 0; i < eachExts.length; i++) {
        if (ext == eachExts[i]) this.imgInfo.correctExt = true;
      }
    },

    checkUserPwd(pwd, type) {
      axios
        .post("/user/checkpw", querystring.stringify({ userPw: pwd }))
        .then(({ data }) => {
          if (data.msg === "success" && data.object) {
            if (type === "update") {
              this.updateUserPwd();
            } else if (type === "del") {
              this.deleteUser();
            }
          } else if (data.msg === "success" && !data.object) {
            alert("비밀번호가 올바르지 않습니다.");
          } else {
            alert("오류 발생");
          }
        })
        .catch(({ err }) => {
          console.log(err);
        });
    },

    makeFormData() {
      this.formData = new FormData();

      this.formData.append("userNm", this.userInfo.userNm);
      if (this.userInfo.userImg instanceof File) {
        this.formData.append("userImg", this.userInfo.userImg);
      }
      this.formData.append("userIntro", this.userInfo.userIntro);
      this.formData.append("userGoalHr", Number(this.userInfo.userGoalHr));
      this.formData.append("updateImg", this.imgInfo.updateImg);
    },

    updateUserInfo() {
      this.makeFormData();

      const config = {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      };

      axios.put("/user", this.formData, config).then(res => {
        if (res.data.msg === "success") {
          this.$store.commit("auth/refreshSuccess", res.headers.accesstoken);
          this.$store.dispatch("auth/tokenInformation");
          alert("회원 정보 수정 완료");
          // this.$router.go();
        }
      });
    },

    updateUserPwd() {
      axios
        .put("/user", querystring.stringify({ userNewPw: this.updatePwd.new }))
        .then(({ data }) => {
          if (data.msg === "success") {
            alert("비밀번호 변경 성공");
            this.updatePwdDialog = false;
          }
        });
    },

    deleteUser() {
      axios.delete("/user").then(({ data }) => {
        if (data.msg === "success") {
          alert("탈퇴가 완료되었습니다.");
          this.$store.commit("logoutSuccess");
          this.$router.push({ name: "Home" });
        }
      });
    }
  }
};
</script>

<style scoped>
#userImg {
  display: block;
  /* width: 100%; */
  max-width: 160px;
  border-radius: 50%;
}

#userNm {
  min-width: 200px;
  max-width: 400px;
}

#goalHr {
  flex-basis: 100px;
}
</style>