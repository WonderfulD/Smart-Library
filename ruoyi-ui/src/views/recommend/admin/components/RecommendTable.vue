<template>
  <div>
    <el-table :data="list" style="width: 100%; padding-top: 15px;">
      <el-table-column label="藏书编号" min-width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.bookId }}
        </template>
      </el-table-column>
      <el-table-column label="书名" min-width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.title }}
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
      <el-table-column label="图书分类" min-width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.category }}
        </template>
      </el-table-column>
      <el-table-column label="图书描述" min-width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleBorrow(scope.row)"
          >去借阅
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-discover"
            @click="handleView(scope.row)"
          >查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {getRecommendBooksListByReaderId} from "@/api/rate/BookRatings";

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
      getRecommendBooksListByReaderId().then(response => {
        this.list = response.data.slice(0, 10) // 限制显示行数为10
      })
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.$router.push({path: `/bookdetails/${row.bookId}`});
    },
    /** 去借阅按钮操作 */
    handleBorrow(row) {
      this.$router.push({path: `/readerborrowbook/ReaderBorrowBook`});
    }
  }
}
</script>
