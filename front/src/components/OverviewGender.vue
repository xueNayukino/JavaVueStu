<template>
  <div class="gender-chart">
    <canvas ref="chart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from "chart.js";

Chart.register(...registerables);

export default {
  name: "OverviewGender",
  props: {
    boyCount: {
      type: Number,
      required: true,
    },
    girlCount: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      chartInstance: null,
    };
  },
  mounted() {
    this.initChart();
  },
  watch: {
    boyCount: "updateChart",
    girlCount: "updateChart",
  },
  methods: {
    initChart() {
      const canvas = this.$refs.chart;

      // 检查 canvas 是否存在
      if (!canvas) {
        console.error("性别表Canvas 元素未找到，无法初始化图表");
        return;
      }

      const ctx = canvas.getContext("2d");

      if (this.chart) {
        this.chart.destroy();
      }

      this.chart = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: ["男生", "女生"],
          datasets: [
            {
              data: [this.boyCount, this.girlCount],
              backgroundColor: ["#36A2EB", "#FF6384"],
              borderWidth: 0,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: "bottom",
            },
            tooltip: {
              callbacks: {
                label: function (context) {
                  const label = context.label || "";
                  const value = context.raw || 0;
                  const total = context.dataset.data.reduce((a, b) => a + b, 0);
                  const percentage = Math.round((value / total) * 100);
                  return `${label}: ${value}人 (${percentage}%)`;
                },
              },
            },
          },
        },
      });
    },
  },
  updateChart() {
    if (this.chartInstance) {
      // 更新数据
      this.chartInstance.data.datasets[0].data = [this.boyCount, this.girlCount];
      this.chartInstance.update(); // 刷新图表
    }
  },
};
</script>

<style scoped>
.gender-chart {
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

canvas {
  max-width: 100%;
  max-height: 100%;
}
</style>