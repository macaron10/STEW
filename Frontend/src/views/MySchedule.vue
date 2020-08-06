<template>
  <div>
    <v-container>
      <Calendar />
    </v-container>
  </div>
</template>

<script>
import Calendar from "@/components/temp/Calendar.vue";
export default {
  name: "MySchedule.vue",
  components: {
    Calendar
  }
};
</script>
