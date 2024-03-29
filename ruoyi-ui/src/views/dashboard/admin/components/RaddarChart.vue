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
      chart: null
    };
  },
  mounted() {
    this.initChart();
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el);

      const option = {
        tooltip: {},
        radar: {
          radius: '60%',
          center: ['50%', '50%'],
          indicator: [
            { name: '资源丰富度', max: 100 },
            { name: '环境与设施', max: 100 },
            { name: '服务质量', max: 100 },
            { name: '可获取性', max: 100 },
            { name: '文化活动与学习支持', max: 100 },
            { name: '更新与发展', max: 100 }
          ]
        },
        series: [{
          name: '图书馆评价',
          type: 'radar',
          data: [
            {
              value: [80, 90, 75, 85, 80, 70],
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
