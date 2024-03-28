<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
// ECharts核心组件
import * as echarts from 'echarts/core';

import { PieChart } from 'echarts/charts';

// 导入提示框、标题、直角坐标系、图例组件
import {
  TooltipComponent,
  TitleComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';

// 导入Canvas渲染器，注意ECharts 5.x后可以按需引入渲染器
import { CanvasRenderer } from 'echarts/renderers';

// 导入主题
import 'echarts/theme/macarons';

// 通过use注册必须的组件
echarts.use(
  [TitleComponent, TooltipComponent, GridComponent, PieChart, CanvasRenderer, LegendComponent]
);


require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import {getCategoryDistributionByDeptId} from "@/api/remote-search";

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
      default: '300px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.fetchDataAndInitChart();
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    fetchDataAndInitChart() {
      getCategoryDistributionByDeptId().then(response => {
        if (response.code === 200) {
          this.initChart(response.data);
        } else {
          console.error('Failed to fetch chart data:', response.msg);
        }
      }).catch(error => {
        console.error('Error fetching chart data:', error);
      });
    },

    initChart(data) {
      const categories = Object.keys(data);
      const chartData = categories.map(category => ({
        value: data[category],
        name: category
      }));

      // 这里确保echarts实例化的元素已经准备好
      this.chart = echarts.init(this.$el, 'macarons');

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: categories
        },
        series: [
          {
            name: '借阅藏书类别',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: chartData,
            animationEasing: 'cubicInOut',
            animationDuration: 2000
          }
        ]
      });
    }
  }
}
</script>
