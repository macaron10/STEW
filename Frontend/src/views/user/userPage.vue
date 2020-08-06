<template>
  <div>
      <img />
      <h1>{{userInfo}}</h1>
      
  </div>
</template>

<script>
import axios from 'axios';

export default {
    name: "userPage",
    
    components: {
    },
    
    data() {
        return {
            userInfo: {},
        }
    },
    
    mounted() {
        this.getUserInfo();
    },
    
    methods: {
        getUserInfo() {
            axios.get('/user')
            .then(({ data }) => {
                console.log(data);
                this.userInfo = data.object;
            })
            
        }
    }

}
</script>

<style>

</style>