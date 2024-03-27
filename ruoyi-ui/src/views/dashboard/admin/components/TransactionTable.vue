<template>
  <el-table :data="list" style="width: 100%; padding-top: 15px;">
    <el-table-column label="借阅编号" min-width="200" align="center">
      <template slot-scope="scope">
        {{ scope.row.borrowId }}
      </template>
    </el-table-column>
    <el-table-column label="读者号" width="195" align="center">
      <template slot-scope="scope">
        {{ scope.row.readerId }}
      </template>
    </el-table-column>
    <el-table-column label="借阅日期" width="195" align="center">
      <template slot-scope="scope">
        {{ scope.row.borrowDate }}
      </template>
    </el-table-column>
    <!-- 新增借阅状态列 -->
    <el-table-column label="借阅状态" width="195" align="center">
      <template slot-scope="scope">
        <el-tag :type="borrowStatusTag(scope.row.status)" :disable-transitions="false">
          {{ borrowStatusText(scope.row.status) }}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import { transactionList } from '@/api/remote-search'

export default {
  data() {
    return {
      list: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        borrowId: null,
        bookId: null,
        readerId: null,
        borrowDate: null,
        dueDate: null,
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      transactionList(this.queryParams).then(response => {
        this.list = response.rows.slice(0, 8) // 限制显示行数为8，根据需要调整
      })
    },
    borrowStatusTag(status) {
      switch (status) {
        case 0:
          return 'success'; // 如期归还
        case 1:
          return 'Brand Color'; // 借阅正常
        case 2:
          return 'warning'; // 逾期归还
        case 3:
          return 'danger'; // 逾期未还
        default:
          return 'info'; // 未知状态
      }
    },
    borrowStatusText(status) {
      switch (status) {
        case 0:
          return '如期归还';
        case 1:
          return '借阅正常';
        case 2:
          return '逾期归还';
        case 3:
          return '逾期未还';
        default:
          return '未知状态';
      }
    }
  }
}
</script>
