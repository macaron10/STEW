<template>
  <v-card flat>
    <v-snackbar v-model="snackbar" absolute top right color="success">
      <span>스터디그룹 수정 완료!</span>
      <v-icon dark>mdi-checkbox-marked-circle</v-icon>
    </v-snackbar>
    <v-form ref="form" @submit.prevent="submit">
      <v-container fluid>
        <v-row>
          <v-col offset="1" cols="10">
            <v-text-field
              v-model="form.gpNm"
              :rules="rules.groupName"
              color="blue darken-2"
              label="그룹 이름"
              required
            ></v-text-field>
          </v-col>
        </v-row>
        <v-row>
          <v-col offset="1" cols="10" sm="3">
            <div class="d-flex justify-center">
              <img
                v-if="imgSrc"
                ref="imgpreview"
                :src="imgSrc"
                alt="그룹 이미지"
                height="150px"
                width="225px"
              />
            </div>
          </v-col>

          <v-col cols="10" offset="1" sm="6">
            <v-file-input
              label="사진 넣기"
              v-model="form.gpImg"
              class="mt-10"
              prepend-icon="mdi-camera"
              @change="changeImg"
              :rules="[
                () => form.gpImg.size <= 3000000 || '3MB 이하의 파일만 등록 가능합니다.',
                () => form.gpImg.length > 0 || form.updateGpImg || '지원하지 않는 확장자입니다 기존 사진이 적용됩니다..'
              ]"
            ></v-file-input>
          </v-col>
          <v-col sm="1"></v-col>
        </v-row>
        <v-row>
          <v-col cols="10" offset="1">
            <v-textarea v-model="form.gpIntro" rows="2" color="primary">
              <template v-slot:label>
                <div>
                  소개
                  <small>(선택사항)</small>
                </div>
              </template>
            </v-textarea>
          </v-col>
          <v-col cols="1"></v-col>
        </v-row>
        <v-row>
          <v-col cols="10" sm="5" offset="1">
            <v-select
              v-model="form.gpCatNo"
              :items="gpCategroies"
              item-value="gpCatNo"
              item-text="gpCatNm"
              :rules="rules.types"
              color="pink"
              label="스터디 타입"
              required
            ></v-select>
          </v-col>
          <v-col offset="1" sm="5">
            <v-checkbox v-model="form.gpPublic" color="green">
              <template v-slot:label>
                <div @click.stop="form.gpPublic=!form.gpPublic">
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
              @keypress="tagKey"
            >
              <template v-slot:no-item>
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
              <v-btn width="80px" :disabled="!formIsValid" color="primary" type="submit">수정완료</v-btn>
              <v-btn width="80px" color="amber" @click="goToBefore">취소</v-btn>
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
  name: "StudyUpdate",
  components: {},
  data() {
    const groupData = Object.freeze({
      // gpCatNm: "", // 카테고리이름
      gpCatNo: 0, // 타입 아이디 ㅇ
      gpImg: null, // 스터디 썸네일 ㅇ
      gpIntro: "", //스터디 소개ㅇ
      gpNm: "", //스터디 이름ㅇ
      gpPublic: true, //스터디 공개 ㅇ
      gpTag: "", //스터디 태그 (임시)
      updateGpImg: false // 사진 변경 여부 - 수정시에 gpImg 값이 변경이 없으면 "" 로 그대로 날아가기 때문에 기존의 사진이 변경시에 지워질 수 있다
      // 그러므로 사진 변경 여부를 확인하여 변경 없을 시 기존의 사진을 그대로 두고, 변경 시에는 null 로 넣으면 null, 다른 사진을 넣으면 다른 사진으로
      // 변경되게끔 구현(아직 미구현)
    });
    const formData = new FormData();
    return {
      tags: [],
      form: Object.assign({}, this.groupData),
      rules: {
        types: [val => val >= 1 || "타입을 지정해 주세요."],
        groupName: [val => (val || "").length > 0 || "이름을 입력해 주세요."]
      },
      categories: [],
      categoryObj: {},
      gpCategroies: [],
      conditions: false,
      content: `로렌입섬`,
      snackbar: false,
      terms: false,
      groupData,
      formData,
      imgSrc: "",
      // 해쉬태그 데이터
      tagItems: [],
      model: [],
      search: null,
      watch: {
        model(val) {
          if (val.length > 5) {
            this.$nextTick(() => this.model.pop());
          }
        }
      }
    };
  },
  beforeMount() {
    this.form.gpImg = { size: 0, length: 1 };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.getDetail();
    this.getCategories();
  },
  computed: {
    formIsValid() {
      return this.form.gpNm && this.form.gpCatNo;
    }
  },
  methods: {
    tagKey(e){
      if(e.key == ' ' || e.key == ','){
        const tag = this.search.replace(',','').replace(' ','');
        if(tag.length>0 && !this.tags.includes(tag))
          this.tags.push(tag);
        this.search = "";
      }
    },
    changeImg(e){
      this.form.updateGpImg = false;
      // console.log(e);
      // const file = e.target.files[0]; // Get first index in files
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
          this.form.updateGpImg = true;
          this.$refs.imgpreview.src = e ? URL.createObjectURL(e) : "";
      this.$refs.imgpreview.src = e ? URL.createObjectURL(e) : this.gpImgDefault;
        }
      }
    },
    async getDetail() {
      const apiUrl = "/study/user/" + this.id;
      const baseUrl = this.$store.state.comm.baseUrl;
      try {
        const res = await axios.get(apiUrl);
        this.groupData = JSON.parse(res.data.object).group;
        this.groupData = JSON.parse(this.groupData);
        this.form = this.groupData;
        this.imgSrc = this.groupData.gpImg != null? baseUrl + '/image/group' + this.groupData.gpImg : this.gpImgDefault;
        this.form.updateGpImg = false
        if(this.groupData.gpTag != null)
          this.tags = this.groupData.gpTag;
        this.form.updateGpImg = false;
      } catch (err) {
        console.error(err);
      }
    },
    resetForm() {
      this.form = Object.assign({}, this.form);
      this.$refs.form.reset();
    },
    goToBefore() {
      this.$router.go(-1);
    },
    makeFormData () {
    this.formData.append('gpNm', this.form.gpNm)
    this.formData.append('gpCatNo', Number(this.form.gpCatNo))
    if(this.form.updateGpImg)
      this.formData.append('gpImg', this.form.gpImg)
    this.formData.append('gpIntro', this.form.gpIntro)
    this.formData.append('gpPublic', Boolean(this.form.gpPublic))
    if(this.form.gpTag.length > 0)
      this.formData.append('gpTag', this.form.gpTag)
    this.formData.append('gpNo', this.id)
    this.formData.append('updateGpImg',this.form.updateGpImg)
    },
    async updateGroup() {
      try {
        const config = {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        };
        const apiUrl = "/study/user/" + this.id;
        for (const pair of this.formData.entries()) {
          console.log(pair[0] + ", " + pair[1]);
        }
        const res = await axios.put(apiUrl, this.formData, config);
        this.$router.push({ name: "StudyDetail", params: { id: this.id } });
      } catch (err) {
        console.error(err);
      }
    },
    inputGpCatNo() {
      this.form.gpCatNo = this.categoryObj[this.form.gpCatNo];
    },
    submit () {
      this.form.gpTag = this.tags;
      this.snackbar = true
      // this.inputGpCatNo()
      this.makeFormData();
      this.updateGroup();
      this.resetForm();
    },
    async getCategories() {
      try {
        const apiUrl = "/study/cate";
        const res = await axios.get(apiUrl);
        this.gpCategroies = res.data.object;
        for (const i in res.data.object) {
          this.categories.push(res.data.object[i].gpCatNm);
          this.categoryObj[res.data.object[i].gpCatNm] =
            res.data.object[i].gpCatNo;
        }
      } catch (err) {
        console.err(err);
      }
    }
  }
};
</script>
