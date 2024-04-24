<template>
  <el-table :data="list" style="width: 100%; padding-top: 15px;">
    <el-table-column label="藏书编号" min-width="180" align="center">
      <template slot-scope="scope">
        {{ scope.row.bookId}}
      </template>
    </el-table-column>
    <el-table-column label="书名" min-width="180" align="center">
      <template slot-scope="scope">
        {{ scope.row.title}}
      </template>
    </el-table-column>
    <el-table-column label="图书封面" min-width="180" align="center">
      <template slot-scope="scope">
        <el-popover placement="top-start" title="" trigger="hover">
          <img :src="scope.row.coverUrl" alt="" style="width: 150px;height: 150px">
          <img slot="reference" :src="scope.row.coverUrl" style="width: 50px;height: 50px">
        </el-popover>
      </template>
    </el-table-column>
    <el-table-column label="读者借阅量" min-width="180" align="center">
      <template slot-scope="scope">
        {{ scope.row.borrowCount}}
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import {getEachBookBorrowsList} from "@/api/rate/BookRatings";

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
      getEachBookBorrowsList().then(response => {
        this.list = response.data.slice(0, 10) // 限制显示行数为10
      })
    },
    borrowStatusTag(status) {
      switch (status) {
        case 0:
          return 'success'; // 如期归还
        case 1:
          return 'Brand Color'; // 借阅正常
        case 2:
          return 'Info'; // 逾期归还
        case 3:
          return 'danger'; // 逾期未还
        case 4:
          return 'warning'; // 待审核
        case 5:
          return 'danger'; // 借阅拒绝
      }
    },
  }
}
</script>
