<template>
  <v-card flat>
    <v-snackbar v-model="snackbar" absolute top right color="success">
      <span>스터디그룹 생성 완료!</span>
      <v-icon dark>mdi-checkbox-marked-circle</v-icon>
    </v-snackbar>
    <v-form ref="form" @submit.prevent="submit">
      <v-container fluid>
        <v-row>
          <v-col offset="1" cols="10">
            <v-text-field
              v-model="form.gpNm"
              :rules="rules.groupName"
              color="blue"
              label="그룹 이름"
              required
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col offset="1" cols="10" sm="3">
            <div class="d-flex justify-center">
              <img ref="imgpreview" :src="imgSrc" alt="그룹 이미지" height="150px" width="225px" />
            </div>
          </v-col>

          <v-col cols="10" offset="1" sm="6">
            <v-file-input
              label="사진 넣기"
              v-model="form.gpImg"
              filled
              class="mt-10"
              prepend-icon="mdi-camera"
              @change="changeImg"
              :rules="[
                () => form.gpImg.size <= 1000000 || '1MB 이하의 파일만 등록 가능합니다.',
                () => form.gpImg.length > 0 || correctExt || '지원하지 않는 확장자입니다.'
              ]"
            ></v-file-input>
          </v-col>
          <v-col sm="1"></v-col>
        </v-row>
        <v-row>
          <v-col cols="10" offset="1">
            <v-textarea v-model="form.gpIntro" rows="2" color="blue">
              <template v-slot:label>
                <div>
                  스터디 소개
                  <small>(선택사항)</small>
                </div>
              </template>
            </v-textarea>
          </v-col>
          <v-col sm="1"></v-col>
        </v-row>
        <v-row>
          <v-col cols="10" sm="5" offset="1">
            <v-select
              v-model="form.gpCatNo"
              :items="categories"
              :rules="rules.types"
              color="blue"
              label="스터디 타입"
              required
            ></v-select>
          </v-col>
          <v-col offset="1" sm="5">
            <v-checkbox v-model="form.gpPublic" color="blue">
              <template @click.stop="form.gpPublic=!form.gpPublic" v-slot:label>
                <div>
                  <span v-if="form.gpPublic">공개 스터디</span>
                  <span v-else>비공개 스터디</span>
                </div>
              </template>
            </v-checkbox>
          </v-col>
          <v-col sm="1"></v-col>
        </v-row>
        <v-row>
          <v-col cols="1"></v-col>

          <v-col cols="10">
            <v-combobox
              v-model="tags"
              :items="tagItems"
              :search-input.sync="search"
              hide-selected
              hint="스터디 그룹을 잘 나타낼 수 있는 태그를 최대 5개 적어주세요"
              label="태그"
              multiple
              persistent-hint
              small-chips
              @keyup="tagKey"
            >
              <template v-slot:no-data>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-title>
                      태그를 추가하시려면
                      <kbd>ENTER</kbd>키를 눌러주세요!
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </template>
            </v-combobox>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="4" offset="4" class="d-flex justify-center">
            <v-card-actions>
              <v-btn width="80px" :disabled="!formIsValid" color="blue" type="submit">생성</v-btn>
              <v-btn width="80px" color="amber lighten-1" @click="resetForm">초기화</v-btn>
              <!-- 생성버튼!! -->
            </v-card-actions>
          </v-col>
          <v-spacer></v-spacer>
        </v-row>
      </v-container>
    </v-form>
  </v-card>
</template>

<script>
// @ is an alias to /src
import axios from "axios";

export default {
  name: "StudyCreate",
  components: {},
  data() {
    const groupData = Object.freeze({
      gpNm: "", //스터디 이름ㅇ
      gpCatNo: 0, // 타입 아이디 ㅇ
      gpEndTm: 0, // 선호 종료 시간 ㅇ
      gpImg: { size: 0, length: 0 },
      gpIntro: "", //스터디 소개ㅇ
      gpPublic: true, //스터디 공개 ㅇ
      gpStTm: 0, // 선호 시작 시간 ㅇ
      gpTag: "" //스터디 태그 (임시)
    });
    const formData = new FormData();
    return {
      tags: [],
      form: Object.assign({}, groupData),
      rules: {
        types: [val => val.length > 0 || "타입을 지정해 주세요."],
        groupName: [val => (21 > val.length && val.length > 0) || "그룹이름은 1~20자 이내로 가능합니다."]
      },
      correctExt: true,
      categories: [],
      categoryObj: {},
      conditions: false,
      content: `로렌입섬`,
      snackbar: false,
      terms: false,
      groupData,
      formData,
      gpImgDefault: this.$store.state.comm.baseUrl + "/image/group/default.png",
      imgSrc: this.$store.state.comm.baseUrl + "/image/group/default.png",
      // 해쉬태그 데이터
      tagItems: [],
      model: [],
      search: null
    };
  },
  watch: {
    model(val) {
      if (val.length > 5) {
        this.$nextTick(() => this.model.pop());
      }
    },
    tags(tags) {
      if (tags.length > 5) {
        alert("태그는 5개까지 허용됩니다.");
        this.tags.pop();
      }
    },
  },
  computed: {
    formIsValid() {
      return 0 < this.form.gpNm.length && this.form.gpNm.length < 21 && this.form.gpCatNo && this.correctExt;
    }
  },

  methods: {
    tagKey(e) {
      if (e.key == " " || e.key == ",") {
        const tag = this.search.replace(",", "").replace(" ", "");
        if (tag.length > 0 && !this.tags.includes(tag)) this.tags.push(tag);
        this.search = "";
      }
    },
    changeImg(e) {
      // const file = e.target.files[0]; // Get first index in files

      if (e === undefined || e.size === 0) {
        this.$refs.imgpreview.src =
          this.$store.state.comm.baseUrl + "/image/group/default.png";

        this.form.gpImg = { size: 0, name: "default.png" };
        return;
      }

      const ext = this.form.gpImg.name
        .substring(
          this.form.gpImg.name.lastIndexOf(".") + 1,
          this.form.gpImg.name.length
        )
        .toLowerCase();
      const imgExts =
        "xbm,tif,pjp,pjpeg,svgz,jpg,jpeg,ico,tiff,gif,svg,bmp,png,gfif,webp";
      const eachExts = imgExts.split(",");

      for (let i = 0; i < eachExts.length; i++) {
        if (ext == eachExts[i]) {
          this.correctExt = true;
          this.$refs.imgpreview.src = e ? URL.createObjectURL(e) : "";
          break;
        } else {
          this.correctExt = false;
        }
      }
      this.$refs.imgpreview.src = e
        ? URL.createObjectURL(e)
        : this.gpImgDefault;
    },
    resetForm() {
      this.form = Object.assign({}, this.groupData);
      this.tags = [];
      this.$refs.imgpreview.src =
        this.$store.state.comm.baseUrl + "/image/group/default.png";
    },
    inputGpCatNo() {
      this.form.gpCatNo = this.categoryObj[this.form.gpCatNo];
    },
    makeFormData() {
      this.formData.append("gpNm", this.form.gpNm); // 순서는 상관 없음
      this.formData.append("gpCatNo", Number(this.form.gpCatNo));
      this.formData.append("gpEndTm", Number(this.form.gpEndTm));
      if (this.form.gpImg.size !== 0) {
        this.formData.append("gpImg", this.form.gpImg);
      }
      this.formData.append("gpIntro", this.form.gpIntro);
      this.formData.append("gpPublic", Boolean(this.form.gpPublic));
      this.formData.append("gpStTm", Number(this.form.gpStTm));
      if (this.form.gpTag !== "")
        this.formData.append("gpTag", this.form.gpTag);
    },
    async createGroup() {
      try {
        const config = {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        };
        const apiUrl = "/study/user/";
        const res = await axios.post(apiUrl, this.formData, config);
        this.$router.push({
          name: "StudyDetail",
          params: { id: res.data.object.gpNo }
        });
      } catch (err) {
        console.error(err);
      }
    },
    submit() {
      this.form.gpTag = this.tags.length===0 ? "" : this.tags
      this.snackbar = true;
      this.inputGpCatNo();
      this.makeFormData();
      this.createGroup();
      // this.resetForm();
    },
    async getCategories() {
      try {
        const apiUrl = "/study/cate";
        const res = await axios.get(apiUrl);
        for (const i in res.data.object) {
          this.categories.push(res.data.object[i].gpCatNm);
          this.categoryObj[res.data.object[i].gpCatNm] =
            res.data.object[i].gpCatNo;
        }
      } catch (err) {
        console.err(err);
      }
    }
  },
  mounted() {
    this.getCategories();

    this.$vuetify.goTo(0, {
      duration: 100,
      offset: 0
    })
  }
};
</script>