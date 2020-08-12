<template>
  <div class="pie-chart" :style="pieStyles"></div>
</template>

<script>
export default {
  props: ["pieData"],
  computed: {
    pieStyles() {
      let acum = 0;
      const styles = this.pieData.map(
        segment => `${segment.color} 0 ${(acum += segment.value)}%`
      );

      return {
        background: `conic-gradient( ${styles.join(",")} )`
      };
    }
  }
};
</script>

<style scoped>
.pie-chart {
  border-radius: 50%;
}
</style>
