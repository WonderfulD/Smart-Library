<template>
  <div :class="className" :style="{height: height, width: width}" />
</template>

<script>
import * as echarts from 'echarts/core';
import { RadarChart } from 'echarts/charts';
import {
  TooltipComponent,
  TitleComponent,
  LegendComponent
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

// 导入主题
import 'echarts/theme/macarons';
import {getRadarChartDataByLibraryId} from "@/api/borrowrating/BorrowRating";

echarts.use([TitleComponent, TooltipComponent, LegendComponent, RadarChart, CanvasRenderer]);

export default {
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
      default: '300px'
    }
  },
  data() {
    return {
      chart: null,
      chartData: []
    };
  },
  mounted() {
    this.fetchData();
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    fetchData() {
      getRadarChartDataByLibraryId().then( response => {
        this.chartData = response.data;
        this.initChart();
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el);

      const option = {
        tooltip: {},
        radar: {
          radius: '60%',
          center: ['50%', '50%'],
          indicator: [
            { name: '数字化访问与远程服务', max: 100 },
            { name: '资源多样性与满足度', max: 100 },
            { name: '效率优化与时间管理', max: 100 },
            { name: '用户体验与服务便捷性', max: 100 },
            { name: '技术融合与服务创新', max: 100 },
            { name: '会员推荐意愿', max: 100 }
          ]
        },
        series: [{
          name: '图书馆评价',
          type: 'radar',
          data: [
            {
              value: this.chartData,
              name: '会员满意度评价'
            }
          ],
          animationDuration: 2000,
          animationEasing: 'cubicOut'
        }]
      };

      this.chart.setOption(option);
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize();
      }
    }
  }
};
</script>
