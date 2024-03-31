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
      const libraryCounts = {}; // 存储每个图书馆的借阅量
      borrowingData.forEach(item => {
        const libraryName = item.deptName; // 获取图书馆名称
        if (!libraryCounts[libraryName]) {
          libraryCounts[libraryName] = 0; // 如果这个图书馆名称还没有记录，则初始化
        }
        libraryCounts[libraryName]++; // 累加借阅量
      });

      // 将统计结果转换为ECharts的数据格式
      const libraryNames = Object.keys(libraryCounts);
      const borrowCounts = Object.values(libraryCounts);

      return {
        categories: libraryNames,
        data: borrowCounts
      };
    },



    initChart() {
      const {categories, data} = this.processBorrowingData(this.borrowingData);
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        xAxis: [{
          type: 'category',
          data: categories, // 这里是图书馆的名称数组
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
          name: '您在当前图书馆的借阅量',
          type: 'bar',
          barWidth: '60%',
          data: data, // 这里是对应图书馆借阅量的数组
          animationDuration
        }]
      })
    }
  }
}
</script>
