<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
// ECharts核心组件
import * as echarts from 'echarts/core';

// 导入柱状图图表，图表后缀都可以在`echarts/charts`下找到
import { BarChart } from 'echarts/charts';

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
  [TitleComponent, TooltipComponent, GridComponent, BarChart, CanvasRenderer, LegendComponent]
);



require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import {listBookBorrowingByDept, listBookBorrowingByUser} from "@/api/borrow/BookBorrowing";


const animationDuration = 2000

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
      default: '320px'
    },

  },
  data() {
    return {
      chart: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 999999,
        borrowId: null,
        bookId: null,
        readerId: null,
        borrowDate: null,
        dueDate: null,
      },
      borrowingData: [],
    }
  },
  created() {
    this.fetchBorrowingData();
  },

  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    fetchBorrowingData() {
      listBookBorrowingByUser(this.queryParams).then(response => {
        this.borrowingData = response.rows;
        this.initChart()
      }).catch(error => {
          console.error("Failed to fetch borrowing data:", error);
        });
    },

    processBorrowingData(borrowingData) {
      const counts = {}; // 用于存储每天的借阅量
      const today = new Date();
      for (let i = 0; i < 7; i++) {
        const date = new Date(today);
        date.setDate(today.getDate() - i);
        const dateString = date.toISOString().split('T')[0]; // 获取日期字符串
        counts[dateString] = 0; // 初始化借阅量
      }
      borrowingData.forEach(borrow => {
        if (counts.hasOwnProperty(borrow.borrowDate)) {
          counts[borrow.borrowDate]++;
        }
      });

      // 将counts转换为ECharts需要的数据格式
      const categories = [];
      const data = [];
      Object.keys(counts).forEach(date => {
        categories.unshift(date); // 日期
        data.unshift(counts[date]); // 借阅量
      });
      return {categories, data};
    },

    initChart() {
      const {categories, data} = this.processBorrowingData(this.borrowingData);
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        xAxis: [{
          type: 'category',
          data: categories,
          axisTick: {
            alignWithLabel: true
          }
        }],
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },

        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '当日借阅量',
          type: 'bar',
          barWidth: '60%',
          data: data,
          animationDuration
        }]
      })
    }
  }
}
</script>
