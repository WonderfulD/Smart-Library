<template>
  <div class="dashboard-editor-container">


    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-tooltip class="item" effect="dark" content="读者对图书馆留言建议的词云" placement="top">
          <div class="chart-wrapper">
            <word-cloud />
          </div>
        </el-tooltip>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-tooltip class="item" effect="dark" content="近七天每日读者借阅图书类别分布" placement="top">
          <div class="chart-wrapper">
            <distribution-chart />
          </div>
        </el-tooltip>
      </el-col>
    </el-row>


    <el-row :gutter="8">
      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 12}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:30px;">
        <transaction-table />
      </el-col>
      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 12}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:30px;">
        <views-chart />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import WordCloud from "./components/WordCloud.vue";
import DistributionChart from "./components/DistributionChart.vue";
import TransactionTable from './components/TransactionTable'
import ViewsChart from "./components/ViewsChart.vue";



export default {
  name: 'DashboardAdmin',
  components: {
    DistributionChart,
    WordCloud,
    TransactionTable,
    ViewsChart
  },
  data() {
    return {
    }
  },
  methods: {
  },
  async created() {
  },
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
