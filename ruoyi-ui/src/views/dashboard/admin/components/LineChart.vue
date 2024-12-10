<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
// ECharts核心组件
import * as echarts from 'echarts/core';

// 导入柱状图图表，图表后缀都可以在`echarts/charts`下找到

import { LineChart } from 'echarts/charts';


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
  [TitleComponent, TooltipComponent, GridComponent, LineChart, CanvasRenderer, LegendComponent]
);




require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
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
    initChart() {
      console.log("进入initChart");
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({ expectedData, actualData } = {}) {
      // 生成日期数组
      const today = new Date();
      const dateList = [];
      for (let i = 6; i >= 0; i--) {
        const d = new Date(today);
        d.setDate(d.getDate() - i);
        // 格式化日期为YYYY-MM-DD
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        dateList.push(`${year}-${month}-${day}`);
      }

      this.chart.setOption({
        xAxis: {
          data: dateList,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 35,
          right: 35,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['expected', 'actual']
        },
        series: [{
          name: '预期', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: expectedData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: '累计',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: actualData,
          animationDuration: 2000,
          animationEasing: 'quadraticOut'
        }]
      })
    }
  }
}
</script>
