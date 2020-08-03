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
            <v-file-input
              label="사진 넣기"
              v-model="form.gpImg"
              filled
              prepend-icon="mdi-camera"
            ></v-file-input>
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
            <v-checkbox
              v-model="form.updateGpImg"
              color="green"
            >
              <template v-slot:label>
                <div @click.stop="">
                  사진변경 여부
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
      gpTag: "", //스터디 태그 (임시)
      updateGpImg: true // 사진 변경 여부 - 수정시에 gpImg 값이 변경이 없으면 "" 로 그대로 날아가기 때문에 기존의 사진이 변경시에 지워질 수 있다
                        // 그러므로 사진 변경 여부를 확인하여 변경 없을 시 기존의 사진을 그대로 두고, 변경 시에는 null 로 넣으면 null, 다른 사진을 넣으면 다른 사진으로
                        // 변경되게끔 구현(아직 미구현) 
    })
    const formData = new FormData()
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
      formData,
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
      // const baseUrl = this.$store.state.baseUrl
      const apiUrl = '/study/user/' + this.id
      try {
        // const config = {
        //   headers: {
        //     Authorization: `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOlsic3RyaW5nIiwiUk9MRV9VU0VSIl0sImV4cCI6MTU5NjAxNDMzNSwidXNlcklkIjoxLCJpYXQiOjE1OTYwMTI1MzV9.JkRWjfgbMLYwlE8UVpfiNRInO6lRXzTj2dliaqnDKICfVcvMbC87-fZuNRvWSIcKI4CyY3X22wSvXj8WH_fv1w `
        //   }
        // }
        const res = await axios.get(apiUrl)
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
    makeFormData () {
    this.formData.append('gpNm', this.form.gpNm)
    this.formData.append('gpCatNo', Number(this.form.gpCatNo))
    this.formData.append('gpEndTm', Number(this.form.gpEndTm))
    this.formData.append('gpImg', this.form.gpImg)
    this.formData.append('gpIntro', this.form.gpIntro)
    this.formData.append('gpPublic', Boolean(this.form.gpPublic))
    this.formData.append('gpStTm', Number(this.form.gpStTm))
    this.formData.append('gpTag', this.form.gpTag)
    this.formData.append('updateGpImg', this.form.updateGpimg)
    },
    async updateGroup () {
      try {
        const config = {
          headers: {
            // Authorization: `Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGUiOlsic3RyaW5nIiwiUk9MRV9VU0VSIl0sImV4cCI6MTU5NjAxNDMzNSwidXNlcklkIjoxLCJpYXQiOjE1OTYwMTI1MzV9.JkRWjfgbMLYwlE8UVpfiNRInO6lRXzTj2dliaqnDKICfVcvMbC87-fZuNRvWSIcKI4CyY3X22wSvXj8WH_fv1w `,
            'Content-Type': 'multipart/form-data',
          }
        }
        // const baseUrl = this.$store.state.baseUrl
        const apiUrl = '/study/user/' + this.id
        const res = await axios.put(apiUrl, this.formData, config)
        this.$router.push({ name: 'StudyDetail', params: { id: res.data.object.gpNo } })
      } catch (err) {
        console.error(err)
      }
    },
    submit () {
      this.snackbar = true
      this.makeFormData()
      this.updateGroup()
      this.resetForm()
    },
  },
};
</script>
