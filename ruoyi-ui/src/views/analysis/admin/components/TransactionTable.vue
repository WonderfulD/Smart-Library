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
    <el-table-column label="读者评分" min-width="180" align="center">
      <template slot-scope="scope">
        <el-tag :type="ratingsTag(scope.row.averageRating)" :disable-transitions="false">
          {{ scope.row.averageRating}}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import {getBookAverageRatingList} from "@/api/rate/BookRatings";

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
      getBookAverageRatingList().then(response => {
        this.list = response.data.slice(0, 9) // 限制显示行数为8，根据需要调整
      })
    },
    ratingsTag(status) {
      switch (status) {
        case 5:
          return 'success';
        case 4:
          return 'Brand Color';
        case 3:
          return 'Info';
        case 2:
          return 'danger';
        case 1:
          return 'warning';
        case 0:
          return 'danger';
      }
    },
  }
}
</script>
