<template>
  <div style="width: 100%; height: 100%; position: relative;">
    <canvas ref="chart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from "chart.js";

Chart.register(...registerables);

export default {
  name: "OverviewMajor",
  props: {
    data: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      chartInstance: null,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
  watch: {
    data: {
      handler(newData) {
        console.log("专业数据更新:", newData);
        this.$nextTick(() => {
          if (this.chartInstance) {
            this.updateChart();
          } else {
            this.initChart();
          }
        });
      },
      deep: true,
    },
  },
  methods: {
    initChart() {
      const canvas = this.$refs.chart;
      if (!canvas) {
        console.error("专业统计图表Canvas 元素未找到");
        return;
      }

      const ctx = canvas.getContext("2d");
      
      if (this.chartInstance) {
        this.chartInstance.destroy();
      }

      if (!Array.isArray(this.data) || this.data.length === 0) {
        console.warn("专业数据为空或格式不正确");
        return;
      }

      const majors = this.data.map((item) => item.major || "未知专业");
      const counts = this.data.map((item) => item.count || 0);

      const backgroundColors = this.data.map(() => 
        `rgba(${Math.floor(Math.random() * 200)}, ${Math.floor(Math.random() * 200)}, ${Math.floor(Math.random() * 200)}, 0.6)`
      );

      this.chartInstance = new Chart(ctx, {
        type: "bar",
        data: {
          labels: majors,
          datasets: [
            {
              label: "专业人数",
              data: counts,
              backgroundColor: backgroundColors,
              borderColor: backgroundColors.map(color => color.replace("0.6", "1")),
              borderWidth: 1,
              borderRadius: 5,
              barThickness: 40,
              maxBarThickness: 50,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            tooltip: {
              enabled: true,
              backgroundColor: "rgba(0,0,0,0.8)",
              titleColor: "#fff",
              bodyColor: "#fff",
              padding: 12,
              displayColors: false,
              callbacks: {
                label: function(context) {
                  return `人数: ${context.parsed.y}人`;
                }
              }
            },
            title: {
              display: true,
              text: '各专业学生人数统计',
              color: '#333',
              font: {
                size: 16,
                weight: 'bold'
              },
              padding: {
                top: 10,
                bottom: 20
              }
            }
          },
          scales: {
            x: {
              grid: {
                display: false,
              },
              ticks: {
                color: "#666",
                font: {
                  size: 12
                },
                maxRotation: 45,
                minRotation: 45
              }
            },
            y: {
              beginAtZero: true,
              grid: {
                color: "rgba(0, 0, 0, 0.1)",
              },
              ticks: {
                color: "#666",
                font: {
                  size: 12
                },
                callback: function(value) {
                  return value + '人';
                }
              }
            },
          },
          layout: {
            padding: {
              top: 20,
              right: 20,
              bottom: 20,
              left: 20
            }
          }
        },
      });
    },
    updateChart() {
      if (!this.chartInstance || !Array.isArray(this.data)) return;

      const majors = this.data.map((item) => item.major || "未知专业");
      const counts = this.data.map((item) => item.count || 0);

      this.chartInstance.data.labels = majors;
      this.chartInstance.data.datasets[0].data = counts;
      this.chartInstance.update();
    },
  },
  beforeUnmount() {
    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }
  },
};
</script>

<style scoped>
.major-chart {
  text-align: center;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

canvas {
  width: 100% !important;
  height: 100% !important;
}
</style>