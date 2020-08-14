<template>
  <v-container>
    <v-timeline align-top :dense="$vuetify.breakpoint.smAndDown">
      <v-timeline-item
        v-for="(item, i) in items"
        :key="i"
        :color="item.color"
        :icon="item.icon"
        fill-dot
      >
        <v-card :color="item.color" dark>
          <v-card-title class="title">{{item.title}}</v-card-title>
          <v-card-text class="white text--primary">
            <p>{{item.content}}</p>
            <v-btn :color="item.color" class="mx-0" outlined>Button</v-btn>
          </v-card-text>
        </v-card>
      </v-timeline-item>
    </v-timeline>
  </v-container>
</template>

<script>
export default {
  name: "Guide",
  data: () => ({
    items: [
      {
        color: "red lighten-2",
        icon: "mdi-star",
        title: "소개",
        content: ""
      },
      {
        color: "purple darken-1",
        icon: "mdi-book-variant",
        title: "사용법",
        content: "1. 캠 준비 2. 로그인(회원가입) 3. 그룹가입 or 그룹생성 4. 캠스터디"
      },
      {
        color: "green lighten-1",
        icon: "mdi-airballoon",
        title: "",
        content: "hello"
      },
      {
        color: "indigo",
        icon: "mdi-buffer",
        title: "문의",
        content: "hello"
      }
    ]
  })
};
</script>