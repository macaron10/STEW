<template>
  <div id="Mainpage"> 
    <MyPage />
    <StudyList />
  </div>

</template>

<script>
// @ is an alias to /src
import MyPage from "@/components/main/MyPage.vue";
import StudyList from "@/components/main/StudyList.vue";

export default {
  name: "Main",
  components: {
    MyPage,
    StudyList,
  },
};
</script>
