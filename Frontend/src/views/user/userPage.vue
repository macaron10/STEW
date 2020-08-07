<template>
  <div>
      <v-row justify="center" id="userpage" class="mx-10 my-10">
          <v-col cols="7" class="flex-column">
            <v-row class="d-flex align-center mr-2">
                <img :src="$store.state.baseUrl+`/user`+userInfo.userImg" id="userImg" class="mr-10 ml-5 my-5" />
                <v-col class="d-flex flex-column justify-center">
                    <v-row class="mb-5">
                        <h1 style="color:black">{{userInfo.userEmail}}</h1>
                    </v-row>
                    <v-row>
                         <v-text-field
                            v-model="userInfo.userNm" label="이름"
                            class="br-2"
                            single-line solo
                            id="userNm"
                        ></v-text-field>
                    </v-row>
                </v-col>
            </v-row>
            <v-row class="d-flex flex-column mx-auto mt-5">
                <label for="goalHr" class="mr-5 my-3" >하루 목표 공부 시간</label>
                <v-text-field
                    id="goalHr" v-model="userInfo.userGoalHr"
                    class="flex-grow-0 my-1"
                    type="number" suffix="시간"
                    min="0" max="24" solo
                ></v-text-field>
            </v-row>
            <v-row>
                <v-textarea
                    v-model="userInfo.userIntro"
                    label="자기소개 및 목표"
                    class="ma-3" solo counter=100 
                ></v-textarea>
            </v-row>

            <v-row class="d-flex justify-center" >
                <v-btn depressed class="mb-2 mx-5" @click.stop="updatePwdDialog=true">비밀번호 변경</v-btn>
                <v-dialog v-model="updatePwdDialog" persistent max-width="300">
                    <v-card>
                        <v-card-title class="headline">비밀번호 변경</v-card-title>
                        <v-card-text>
                            <v-text-field
                                v-model="updatePwd.origin" label="현재 비밀번호"
                                type="password" counter="15" required
                                :rules="[
                                () => !!updatePwd.origin|| '비밀번호를 입력해주세요.',
                                ]"
                            />

                            <v-text-field
                                v-model="updatePwd.new" label="새로운 비밀번호"
                                type="password" counter="15" required
                                :rules="[
                                () => !!updatePwd.new || '새로운 비밀번호를 입력해주세요.',
                                () => updatePwd.new.length >= 6 || '6자 이상 입력해주세요.',
                                ]"
                            />

                            <v-text-field
                                v-model="updatePwd.newChk" label="새로운 비밀번호 확인"
                                type="password" counter="15" required
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
                <v-btn depressed color="error" class="mb-2 mx-5" @click="deleteUserDialog=true">회원 탈퇴</v-btn>
                <v-dialog v-model="deleteUserDialog" persistent max-width="300">
                    <v-card>
                        <v-card-title class="headline">회원 탈퇴</v-card-title>
                        <v-card-text>
                            <v-text-field
                                v-model="updatePwd.origin" label="비밀번호"
                                type="password" counter="15" required
                                :rules="[
                                () => !!updatePwd.origin|| '비밀번호를 입력해주세요.',
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
import axios from 'axios';
import querystring from 'querystring';

export default {
    name: "userPage",
    
    components: {
    },
    
    data() {
        return {
            userInfo: {},
            newUserInfo: {
                userEmail: "",
                userPw: "",
                userNm: "",
                userImg: "",
                userIntro: "",
                userGoalHr: ""
            },
            updatePwdDialog: false,
            deleteUserDialog: false,
            updatePwd: {
                origin: "",
                new: "",
                newChk: "",
            },

        }
    },
    
    mounted() {
        this.getUserInfo();
    },
    
    methods: {
        getUserInfo() {
            axios.get('/user/')
            .then(({ data }) => {
                console.log(data);
                this.userInfo = data.object;
                this.newUserInfo.userPw = "";
                this.newUserInfo.userNm = this.userInfo.userNm;
                this.newUserInfo.userImg = this.userInfo.userImg;
                this.newUserInfo.userIntro = this.userInfo.userIntro;
                this.newUserInfo.userGoalHr = this.userInfo.userGoalHr;
            })
            
        },

        checkUserPwd(pwd, type) {
            axios.post('/user/checkpw', querystring.stringify({userPw : pwd}))
            .then(({ data }) => {
                console.log(data);
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
        },

        updateUserInfo() {
            axios.put('/user', {
            })

        },

        updateUserPwd() {
            axios.put('/user', querystring.stringify({userNewPw : this.updatePwd.new}))
            .then(({ data }) => {
                console.log(data);
                if (data.msg === "success") {
                    alert("비밀번호 변경 성공");
                    this.updatePwdDialog=false;
                }
            })
        },

        deleteUser() {
            axios.delete('/user/'+this.userInfo.userId)
            .then(({ data }) => {
                console.log(data);
                if (data.msg === "success") {
                    alert("탈퇴가 완료되었습니다.")
                    this.$store.commit("logoutSuccess");
                    this.$router.push({ name: "Home" });
                }
            })
        },
    }
}
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