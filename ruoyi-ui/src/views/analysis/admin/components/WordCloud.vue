<template>
  <div ref="wordCloudRef" class="word-cloud-chart" :style="{ height: height, width: width }"></div>
</template>

<script>
// ECharts核心组件
import * as echarts from 'echarts/core';

// 导入Canvas渲染器
import {CanvasRenderer} from 'echarts/renderers';

// 注册Canvas渲染器
echarts.use([CanvasRenderer]);

// 导入词云扩展
import 'echarts-wordcloud';

import {listWordCloudDataByLibraryId} from "@/api/borrowrating/BorrowRating";

export default {
  name: 'WordCloud',
  props: {
    className: {
      type: String,
      default: 'word-cloud-chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '400px'
    },
    autoResize: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      chart: null,
      wordCloudData: [],
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  },
  methods: {
    async initChart() {
      try {
        const response = await listWordCloudDataByLibraryId();
        if (response.code === 200 && response.data) {
          // 直接使用加载的数据
          const wordCloudData = response.data;
          if (!this.chart) {
            this.chart = echarts.init(this.$refs.wordCloudRef);
          }
          this.chart.setOption({
            series: [{
              type: 'wordCloud',
              shape: 'circle',
              left: 'center',
              top: 'center',
              width: '70%',
              height: '80%',
              sizeRange: [12, 60],
              rotationRange: [-45, 45],
              gridSize: 8,
              drawOutOfBound: false,
              textStyle: {
                  fontFamily: 'sans-serif',
                  fontWeight: 'bold',
                  color: function () {
                    // 设置颜色生成函数，以实现颜色随机化
                    return 'rgb(' + [
                      Math.round(Math.random() * 255),
                      Math.round(Math.random() * 255),
                      Math.round(Math.random() * 255)
                    ].join(',') + ')';
                  },
                emphasis: {
                  shadowBlur: 10,
                  shadowColor: '#333'
                }
              },
              data: wordCloudData
            }]
          });
        } else {
          console.error('Failed to load word cloud data:', response.msg);
        }
      } catch (error) {
        console.error('Error loading word cloud data:', error);
      }

      if (this.autoResize && this.chart) {
        window.addEventListener('resize', this.chart.resize);
      }
    }


  }
};
</script>

<style scoped>
.word-cloud-chart {
  height: 100%;
  width: 100%;
}
</style>
