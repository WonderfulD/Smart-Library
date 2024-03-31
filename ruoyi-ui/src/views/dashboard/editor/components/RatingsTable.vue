<template>
  <el-table :data="list" style="width: 100%; padding-top: 15px;">
    <el-table-column label="书名" min-width="200" align="center">
      <template slot-scope="scope">
        {{ scope.row.title }}
      </template>
    </el-table-column>
    <el-table-column label="图书封面" width="195" align="center">
        <template slot-scope="scope">
          <el-popover placement="top-start" title="" trigger="hover">
            <img :src="scope.row.coverUrl" alt="" style="width: 150px;height: 150px">
            <img slot="reference" :src="scope.row.coverUrl" style="width: 50px;height: 50px">
          </el-popover>
        </template>
    </el-table-column>
    <el-table-column label="评分" width="195" align="center">
      <template slot-scope="scope">
        {{ scope.row.rating }}
      </template>
    </el-table-column>
    <!-- 新增借阅状态列 -->
    <el-table-column label="评分日期" width="195" align="center">
      <template slot-scope="scope">
        <span>{{ parseTime(scope.row.ratingTime, '{y}-{m}-{d}') }}</span>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import {listBookRatings} from "@/api/rate/BookRatings";
import {parseTime} from "@/utils/ruoyi";

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
        readerId: this.$store.state.user.id,
        borrowDate: null,
        dueDate: null,
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    parseTime,
    fetchData() {
      listBookRatings(this.queryParams).then(response => {
        this.list = response.rows.slice(0, 9) // 限制显示行数为8，根据需要调整
      })
    },
  }
}
</script>
