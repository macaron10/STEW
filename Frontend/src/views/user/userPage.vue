<template>
  <div>
      <v-row justify="center" id="userpage" class="mx-10 my-10">
          <v-col cols="7" class="flex-column">
            <v-row class="d-flex align-center mr-2">
                <img src="../user/default_user_image.png" id="userImg" class="mr-10 ml-5 my-5" />
                <v-col class="d-flex flex-column justify-center">
                    <v-row class="mb-5">
                        <h1 style="color:black">{{userInfo.userEmail}}</h1>
                    </v-row>
                    <v-row>
                         <v-text-field
                            v-model="userInfo.userNm" label="이름"
                            class=""
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
                <v-btn depressed class="mb-2 mx-5" @click="updatePwd">비밀번호 변경</v-btn>
                <v-btn depressed color="primary" class="mb-2 mx-5" @click="updateUserInfo">프로필 설정 완료</v-btn>
                <v-btn depressed color="error" class="mb-2 mx-5" @click="deleteUser">회원 탈퇴</v-btn>
            </v-row>
          </v-col>
      </v-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
    name: "userPage",
    
    components: {
    },
    
    data() {
        return {
            userInfo: {},
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
            })
            
        },

        updateUserInfo() {
            axios.put('/user', {
            })

        },

        delete() {
            axios.delete('/user/'+this.userInfo.userId)

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