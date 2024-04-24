<template>
  <div ref="chartContainer" :class="className" :style="{height: height, width: width}"></div>
</template>

<script>
// ECharts核心组件
import * as echarts from 'echarts/core';
import 'echarts/theme/macarons'; // echarts theme
import resize from './mixins/resize';
import { listCategoryCountsByDay } from "@/api/borrow/BookBorrowing";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '400px'
    }
  },
  data() {
    return {
      chart: null,
      processedData: null, // 用于存储处理后的数据
      originalData: null, // 原始数据
    };
  },
  async mounted() {
    await this.fetchData();
    this.initChart();
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  },
  methods: {
    async fetchData() {
      const response = await listCategoryCountsByDay(); // 假设不需要查询参数
      this.originalData = response.data; // 保存原始数据
      this.processData(response.data);
    },

    processData(data) {
      const categories = Object.keys(data).sort(); // 日期排序作为X轴
      let series = []; // 用于存储每个种类的数据序列
      let legendData = []; // 图例数据
      const pieData = []; // 饼图数据，使用最新一天的数据

      const seriesMap = {};
      categories.forEach((date, index) => {
        Object.entries(data[date]).forEach(([category, count]) => {
          if (!seriesMap[category]) {
            seriesMap[category] = Array(categories.length).fill(0);
            legendData.push(category);
          }
          seriesMap[category][index] = count;
          if (index === categories.length - 1) { // 最新一天的数据用于饼图
            pieData.push({ name: category, value: count });
          }
        });
      });

      series = Object.keys(seriesMap).map(category => ({
        name: category,
        type: 'line',
        data: seriesMap[category],
        smooth: true,
      }));

      // 添加饼图的series配置
      series.push({
        name: 'Category Distribution',
        type: 'pie',
        radius: '30%',
        center: ['50%', '25%'], // 调整饼图的位置
        data: pieData
      });

      // 保存处理后的数据，待图表实例化后使用
      this.processedData = { categories, series, legendData };
    },

    initChart() {
      this.$nextTick(() => {
        if (this.$refs.chartContainer) {
          this.chart = echarts.init(this.$refs.chartContainer, 'macarons');
          this.chart.on('updateAxisPointer', this.updatePieSeries); // 添加事件监听
          if (this.processedData) {
            this.updateChart();
          }
        } else {
          console.error("Chart container not available");
        }
      });
    },

    updatePieSeries(event) {
      const xAxisInfo = event.axesInfo[0];
      if (xAxisInfo) {
        const dimension = xAxisInfo.value; // 获取当前高亮的日期索引
        const date = this.processedData.categories[dimension]; // 根据索引获取日期

        // 构建用于饼状图的数据
        const pieData = [];
        // 这里假设this.processedData已经包含了原始的data对象
        Object.entries(this.originalData[date]).forEach(([category, count]) => {
          pieData.push({ name: category, value: count });
        });

        // 更新饼状图的数据
        this.chart.setOption({
          series: [{
            name: 'Category Distribution',
            type: 'pie',
            radius: '30%',
            center: ['50%', '25%'],
            data: pieData
          }]
        });
      }
    },

    updateChart() {
      if (!this.chart) {
        console.error("ECharts instance is not initialized.");
        return;
      }
      const { categories, series, legendData } = this.processedData;
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        legend: {
          data: legendData,
          top: 'top',
        },
        grid: {
          top: '50%', // 将折线图下移,确保不会被饼图遮挡
          bottom: '10%' // 根据需要调整,确保折线图显示完整
        },
        xAxis: [
          {
            type: 'category',
            data: categories,
            axisPointer: {
              type: 'shadow'
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '数量',
            min: 0,
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: series
      };

      this.chart.setOption(option, true); // 使用true来清除之前的配置
    },


  }
};
</script>
