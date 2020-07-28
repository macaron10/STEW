<template>
  <v-card flat>
    <v-snackbar
      v-model="snackbar"
      absolute
      top
      right
      color="success"
    >
      <span>스터디그룹 수정 완료!</span>
      <v-icon dark>mdi-checkbox-marked-circle</v-icon>
    </v-snackbar>
    <v-form ref="form" @submit.prevent="submit">
      <v-container fluid>
        <v-row>
          <v-col cols="12" sm="6">
            <v-text-field
              v-model="form.gpNm"
              :rules="rules.groupName"
              color="blue darken-2"
              label="스터디 그룹 이름"
              required
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6">
            <v-text-field
              v-model="form.gpImg"
              :rules="rules.groupName"
              color="blue darken-2"
              label="스터디 이미지 들어갈 자리"
              required
            ></v-text-field>
          </v-col>
          <v-col cols="12">
            <v-textarea
              v-model="form.gpIntro"
              color="teal"
            >
              <template v-slot:label>
                <div>
                  스터디 소개 <small>(Obtional)</small>
                </div>
              </template>
            </v-textarea>
          </v-col>
          <v-col cols="12" sm="6">
            <v-select
              v-model="form.gpCatNo"
              :items="groupTypes"
              :rules="rules.types"
              color="pink"
              label="스터디 타입"
              required
            ></v-select>
          </v-col>
          <v-col cols="12" sm="6">
          </v-col>
          <v-col cols="12" sm="6">
            <v-slider
              v-model="form.gpStTm"
              :rules="rules.time"
              color="orange"
              label="선호 시작 시간"
              hint="Be honest"
              min="0"
              max="23"
              thumb-label
            ></v-slider>
          </v-col>
          <v-col cols="12" sm="6">
            <v-slider
              v-model="form.gpEndTm"
              :rules="rules.time"
              color="orange"
              label="선호 종료 시간"
              hint="Be honest"
              min="0"
              max="23"
              thumb-label
            ></v-slider>
          </v-col>
          <v-col cols="12">
            <v-checkbox
              v-model="form.gpPublic"
              color="green"
            >
              <template v-slot:label>
                <div @click.stop="">
                  스터디 공개, 비공개 여부
                </div>
              </template>
            </v-checkbox>
          </v-col>
          <v-col cols="12">
            <v-combobox
              v-model="model"
              :items="tagItems"
              :search-input.sync="search"
              hide-selected
              hint="Maximum of 5 tags"
              label="Add some tags"
              multiple
              persistent-hint
              small-chips
            >
              <template v-slot:no-data>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-title>
                      No results matching "<strong>{{ search }}</strong>". Press <kbd>enter</kbd> to create a new one
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </template>
            </v-combobox>
          </v-col>
        </v-row>
      </v-container>
      <v-card-actions>
        <v-btn text @click="resetForm">Cancel</v-btn>
        <v-spacer></v-spacer>
        <!-- 수정버튼!! -->
        <v-btn
          :disabled="!formIsValid"
          text
          color="primary"
          type="submit"
        >Register</v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
// @ is an alias to /src
import axios from 'axios'

export default {
  name: "StudyUpdate",
  components: {
  },
  data () {
    const groupData = Object.freeze({
      gpCatNo: 0, // 타입 아이디 ㅇ
      gpEndTm: 0, // 선호 종료 시간 ㅇ
      gpImg: "",  // 스터디 썸네일 ㅇ
      gpIntro: "",  //스터디 소개ㅇ
      gpNm: "",     //스터디 이름ㅇ
      gpPublic: true, //스터디 공개 ㅇ
      gpStTm: 0,    // 선호 시작 시간 ㅇ
      gpTag: "" //스터디 태그 (임시)
    })
    return {
      form: Object.assign({}, this.groupData),
      rules: {
        time: [
          val => val > 6 || `잠을 자세요!`,
        ],
        types: [val => (val || '').length > 0 || 'This field is required'],
        groupName: [val => (val || '').length > 0 || 'This field is required'],
      },
      groupTypes: ['1', '2', '3', '4', '5'],
      conditions: false,
      content: `로렌입섬`,
      snackbar: false,
      terms: false,
      groupData,
    // 해쉬태그 데이터
    tagItems: ['Gaming', 'Programming', 'Vue', 'Vuetify'],
    model: ['Vuetify'],
      search: null,
    watch: {
      model (val) {
        if (val.length > 5) {
          this.$nextTick(() => this.model.pop())
        }
      },
    },
  }
  },
  mounted () {
    this.id = this.$route.params.id
    this.getDetail()
  },
  computed: {
    formIsValid () {
      return (
        this.form.gpNm &&
        this.form.gpCatNo
      )
    },
  },
  methods: {
    async getDetail () {
      const baseUrl = this.$store.state.baseUrl
      const apiUrl = baseUrl + '/study/' + this.id
      try {
        const config = {
          headers: {
            Authorization: `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOlsic3RyaW5nIiwiUk9MRV9VU0VSIl0sImV4cCI6MTU5NTkyNjk3MywidXNlcklkIjoxLCJpYXQiOjE1OTU5MjUxNzN9.yJA-YJ_1QDVslPVcoT6xD8cad1SxP3iWR0AxT_vxkQiEB1CN-gdimy_mU96CGegEzkTy5JR0GhYQdE0ybwqqhQ `
          }
        }
        const res = await axios.get(apiUrl, config)
        this.groupData = res.data.object
        this.form = this.groupData
      } catch (err) {
        console.error(err)
      }
    },
    resetForm () {
      this.form = Object.assign({}, this.form)
      this.$refs.form.reset()
    },
    async updateGroup () {
      try {
        const config = {
          headers: {
            Authorization: `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOlsic3RyaW5nIiwiUk9MRV9VU0VSIl0sImV4cCI6MTU5NTkxMTY2NSwidXNlcklkIjoxLCJpYXQiOjE1OTU5MDk4NjV9.PuVmFIk-PRCb0DhxcaiL4UMoMT1X4Vuw-mOMmv1INkC3eW6natkq2JG_peh8HxGVzH06JHHftxjb7LMqy1sgAA `
          }
        }
        const baseUrl = this.$store.state.baseUrl
        const apiUrl = baseUrl + '/study/' + this.id
        const res = await axios.post(apiUrl, this.form, config)
        this.$router.push({ name: 'Main' }) // params: { id: res.data.id } })
      } catch (err) {
        console.error(err)
      }
    },
    submit () {
      this.snackbar = true
      this.updateGroup()
      this.resetForm()
    },
  },
};
</script>
