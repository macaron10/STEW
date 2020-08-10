<template>
  <v-card flat>
    <v-snackbar
      v-model="snackbar"
      absolute
      top
      right
      color="success"
    >
      <span>스터디그룹 생성 완료!</span>
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
              :items="categories"
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
              v-model="tags"
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
        <!-- 생성버튼!! -->
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
  name: "StudyCreate",
  components: {
  },
  data () {
    const groupData = Object.freeze({
      gpNm: "",     //스터디 이름ㅇ
      gpCatNo: 0, // 타입 아이디 ㅇ
      gpEndTm: 0, // 선호 종료 시간 ㅇ
      gpImg: null,
      gpIntro: "",  //스터디 소개ㅇ
      gpPublic: true, //스터디 공개 ㅇ
      gpStTm: 0,    // 선호 시작 시간 ㅇ
      gpTag: "" //스터디 태그 (임시)
    })
    const formData = new FormData()
    return {
      tags: [],
      form: Object.assign({}, groupData),
      rules: {
        time: [
          val => val > 6 || `잠을 자세요!`,
        ],
        types: [val => (val || '').length > 0 || 'This field is required'],
        groupName: [val => (val || '').length > 0 || 'This field is required'],
      },
      categories: [],
      categoryObj: {},
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
  computed: {
    formIsValid () {
      return (
        this.form.gpNm &&
        this.form.gpCatNo
      )
    },
  },

  methods: {
    resetForm () {
      this.form = Object.assign({}, this.form)
      this.$refs.form.reset()
    },
    inputGpCatNo() {
      this.form.gpCatNo = this.categoryObj[this.form.gpCatNo]
    },
    makeFormData () {
      this.formData.append('gpNm', this.form.gpNm) // 순서는 상관 없음
      this.formData.append('gpCatNo', Number(this.form.gpCatNo))
      this.formData.append('gpEndTm', Number(this.form.gpEndTm))
      if (this.form.gpImg != null)
        this.formData.append('gpImg', this.form.gpImg)
      this.formData.append('gpIntro', this.form.gpIntro)
      this.formData.append('gpPublic', Boolean(this.form.gpPublic))
      this.formData.append('gpStTm', Number(this.form.gpStTm))
      this.formData.append('gpTag', this.form.gpTag)
    },
    async createGroup () {
      try {
        const config = {
          headers: {
            'Content-Type': 'multipart/form-data',
          }
        }
        const apiUrl = '/study/user/'
        const res = await axios.post(apiUrl, this.formData, config)
        this.$router.push({ name: 'StudyDetail', params: { id: res.data.object.gpNo } })
      } catch (err) {
        console.error(err)
      }
    },
    submit () {
      console.log(this.tags)
      this.snackbar = true
      this.inputGpCatNo()
      this.makeFormData()
      this.createGroup()
      this.resetForm()
    },
    async getCategories () {
      try {
        const apiUrl = '/study/cate'
        const res = await axios.get(apiUrl)
        for (const i in res.data.object) {
          this.categories.push(res.data.object[i].gpCatNm)
          this.categoryObj[res.data.object[i].gpCatNm] = res.data.object[i].gpCatNo
        }
      } catch (err) {
        console.err(err)
      }
    }
  },
  mounted () {
    this.getCategories()
  }
};
</script>
