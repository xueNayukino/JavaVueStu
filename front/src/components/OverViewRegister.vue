<template>
  <div class="register-chart">
    <canvas ref="chart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from "chart.js";

Chart.register(...registerables);

export default {
  name: "OverViewRegister", 
  props: {
    reportedStudent: {
      type: Number,
      required: true,
      default: 0, // 已报道人数默认值
    },
    totalStudent: {
      type: Number,
      required: true,
      default: 0, // 总人数默认值
    },
  },
  data() {
    return {
      chartInstance: null, // 图表实例
    };
  },
  mounted() {
    this.initChart(); // 初始化图表
  },
  watch: {
    // 监听属性变化
    reportedStudent() {
      this.updateChart();
    },
    totalStudent() {
      this.updateChart(); 
    }
  },
  methods: {
    // 初始化图表
    initChart() {
      // 如果已存在图表实例,先销毁
      if (this.chartInstance) {
        this.chartInstance.destroy();
      }

      const ctx = this.$refs.chart.getContext("2d");
  
      // 计算未报道人数
      const unreportedStudent = this.totalStudent - this.reportedStudent;
      const data = [this.reportedStudent, unreportedStudent];
  
      // 创建环形图
      this.chartInstance = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: ["已报道", "未报道"], // 图例标签
          datasets: [
            {
              label: "报道情况",
              data: data, // 数据
              backgroundColor: ["#4CAF50", "#FFC107"], // 颜色配置
              hoverOffset: 10,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false, // 保持图表适配
          plugins: {
            legend: {
              position: "top", // 图例位置
            },
            tooltip: {
              enabled: true, // 启用提示
            },
          },
        },
      });
    },
    // 更新图表数据
    updateChart() {
      if (this.chartInstance) {
        const unreportedStudent = this.totalStudent - this.reportedStudent;
        this.chartInstance.data.datasets[0].data = [
          this.reportedStudent,
          unreportedStudent,
        ];
        this.chartInstance.update(); // 刷新图表
      }
    },
  },
  beforeUnmount() {
    // 销毁图表实例，防止内存泄漏
    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }
  },
};
</script>

<style scoped>
.register-chart {
  text-align: center;
  margin: 20px 0; /* 添加一些外边距 */
}

canvas {
  max-width: 100%;
  height: 300px; /* 固定图表高度 */
}
</style>