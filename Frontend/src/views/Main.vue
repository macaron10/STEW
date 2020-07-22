<template>
  <div id="Mainpage"> 
    <MyPage />
    <VideoList />
  </div>

</template>

<script>
// @ is an alias to /src
import MyPage from "@/components/main/MyPage.vue";
import VideoList from "@/components/main/VideoList.vue";

export default {
  name: "Main",
  components: {
    MyPage,
    VideoList,
  },
};
</script>
