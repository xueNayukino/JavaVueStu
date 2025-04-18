<template>
  <div class="enrollment-chart">
    <canvas ref="chart"></canvas>
  </div>
</template>

<script>
import { Chart, registerables } from "chart.js";

Chart.register(...registerables);

export default {
  name: "OverviewEnrollmentYear",
  props: {
    data: {
      type: Array,
      required: true,
      default:()=>[]
    }
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
    data: {
      handler() {
        this.updateChart();
      },
      deep: true
    }
  },
  methods: {
    initChart() {
      const ctx = this.$refs.chart.getContext("2d");

      // 创建图表实例
      this.chartInstance = new Chart(ctx, {
        type: "line",
        data: {
          labels: this.data.map(item => item.year),
          datasets: [
            {
              label: "入学人数",
              data: this.data.map(item => item.count),
              borderColor: "#4CAF50",
              backgroundColor: "rgba(76, 175, 80, 0.1)",
              tension: 0.3,
              fill: true,
              borderWidth: 2,
              pointRadius: 4,
              pointHoverRadius: 6,
              pointBackgroundColor: "#4CAF50",
              pointBorderColor: "#fff",
              pointHoverBackgroundColor: "#fff",
              pointHoverBorderColor: "#4CAF50",
              pointBorderWidth: 2,
              lineTension: 0.4
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: true,
          plugins: {
            legend: {
              display: true,
              position: "top",
            },
            tooltip: {
              mode: "index",
              intersect: false,
              backgroundColor: "rgba(0,0,0,0.7)",
              padding: 10,
              cornerRadius: 4,
              titleColor: "#fff",
              bodyColor: "#fff",
            },
          },
          scales: {
            x: {
              title: {
                display: true,
                text: "入学年份",
                color: "#666"
              },
              grid: {
                display: false
              },
              ticks: {
                color: "#666"
              }
            },
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: "人数",
                color: "#666"
              },
              grid: {
                color: "rgba(0, 0, 0, 0.1)",
                drawBorder: false
              },
              ticks: {
                color: "#666",
                padding: 10
              }
            }
          },
          interaction: {
            intersect: false,
            mode: "index"
          },
          elements: {
            line: {
              tension: 0.4
            }
          }
        }
      });
    },
    updateChart() {
      if (this.chartInstance) {
        this.chartInstance.data.labels = this.data.map(item => item.year);
        this.chartInstance.data.datasets[0].data = this.data.map(item => item.count);
        this.chartInstance.update();
      }
    }
  },
  beforeUnmount() {
    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }
  }
};
</script>

<style scoped>
.enrollment-chart {
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