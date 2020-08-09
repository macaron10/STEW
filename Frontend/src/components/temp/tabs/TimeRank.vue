<template>
  <div>
    <h1>hii</h1>
  </div>
</template>

<script>
// @ is an alias to /src
import axios from "axios"
export default {
  name: "TimeRank",
  data () {
    return {
      gpNo: null
    }
  },
  mounted() {
    this.gpNo = this.$route.params.id
    this.getStudyTotal()
  },
  components: {
  },
  methods: {
    getStudyTotal(){
      axios
        .get(`/timer/${this.gpNo}/${new Date().getFullYear()}`)
        .then(res => console.log(res.data))
        .catch(err => console.log(err))
    }
  },
};
</script>
