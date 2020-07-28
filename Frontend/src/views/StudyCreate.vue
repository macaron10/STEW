<template>
  <v-card flat>
    <v-snackbar
      v-model="snackbar"
      absolute
      top
      right
      color="success"
    >
      <span>Registration successful!</span>
      <v-icon dark>mdi-checkbox-marked-circle</v-icon>
    </v-snackbar>
    <v-form ref="form" @submit.prevent="submit">
      <v-container fluid>
        <v-row>
          <v-col cols="12" sm="6">
            <v-text-field
              v-model="form.gpNm"
              :rules="rules.name"
              color="blue darken-2"
              label="스터디 그룹 이름"
              required
            ></v-text-field>
          </v-col>
          <v-col cols="12">
            <v-textarea
              v-model="form.bio"
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
              v-model="form.favoriteAnimal"
              :items="animals"
              :rules="rules.animal"
              color="pink"
              label="스터디 타입"
              required
            ></v-select>
          </v-col>
          <v-col cols="12" sm="6">
            <v-slider
              v-model="form.age"
              :rules="rules.age"
              color="orange"
              label="Age"
              hint="Be honest"
              min="1"
              max="100"
              thumb-label
            ></v-slider>
          </v-col>
          <v-col cols="12">
            <v-checkbox
              v-model="form.terms"
              color="green"
            >
              <template v-slot:label>
                <div @click.stop="">
                  Do you accept the
                  <a href="javascript:;" @click.stop="terms = true">terms</a>
                  and
                  <a href="javascript:;" @click.stop="conditions = true">conditions?</a>
                </div>
              </template>
            </v-checkbox>
          </v-col>
        </v-row>
      </v-container>
      <v-card-actions>
        <v-btn text @click="resetForm">Cancel</v-btn>
        <v-spacer></v-spacer>
        <v-btn
          :disabled="!formIsValid"
          text
          color="primary"
          type="submit"
        >Register</v-btn>
      </v-card-actions>
    </v-form>
    <v-dialog v-model="terms" width="70%">
      <v-card>
        <v-card-title class="title">Terms</v-card-title>
        <v-card-text v-for="n in 5" :key="n">
          {{ content }}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            text
            color="purple"
            @click="terms = false"
          >Ok</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="conditions" width="70%">
      <v-card>
        <v-card-title class="title">Conditions</v-card-title>
        <v-card-text v-for="n in 5" :key="n">
          {{ content }}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            text
            color="purple"
            @click="conditions = false"
          >Ok</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
// @ is an alias to /src

export default {
  name: "StudyCreate",
  components: {
  },
  data () {
    const groupData = Object.freeze({
      gpCatNo: 0, // 타입 아이디
      gpEndTm: 0, // 선호 종료 시간
      gpImg: "",  // 스터디 썸네일
      gpIntro: "",  //스터디 소개ㅇ
      gpNm: "",     //스터디 이름ㅇ
      gpPublic: true, //스터디 공개
      gpStTm: 0,    // 선호 시작 시간
      gpTag: "" //스터디 태그
    })
    return {
      form: Object.assign({}, groupData),
      rules: {
        age: [
          val => val < 80 || `I don't believe you!`,
        ],
        animal: [val => (val || '').length > 0 || 'This field is required'],
        name: [val => (val || '').length > 0 || 'This field is required'],
      },
      animals: ['Dog', 'Cat', 'Rabbit', 'Turtle', 'Snake'],
      conditions: false,
      content: `Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero. Sed cursus ante dapibus diam. Sed nisi. Nulla quis sem at nibh elementum imperdiet. Duis sagittis ipsum. Praesent mauris. Fusce nec tellus sed augue semper porta. Mauris massa. Vestibulum lacinia arcu eget nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur sodales ligula in libero. Sed dignissim lacinia nunc.`,
      snackbar: false,
      terms: false,
      groupData,
    }
  },

  computed: {
    formIsValid () {
      return (
        this.form.first &&
        this.form.last &&
        this.form.favoriteAnimal &&
        this.form.terms
      )
    },
  },

  methods: {
    resetForm () {
      this.form = Object.assign({}, this.groupData)
      this.$refs.form.reset()
    },
    submit () {
      this.snackbar = true
      this.resetForm()
    },
  },
};
</script>
