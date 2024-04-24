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
            icon="el-icon-reading"
            @click="handleBorrow(scope.row)"
          >借阅
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

    <!-- 借阅信息填写对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form :model="form">
        <el-form-item label="取阅方式">
          <el-radio-group v-model="form.borrowMethod">
            <el-radio :label="0">到馆</el-radio>
            <el-radio :label="1">邮寄</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.borrowMethod === 1" label="寄送地址">
          <el-input v-model="form.comments"></el-input>
        </el-form-item>
        <el-form-item v-if="form.borrowMethod === 0" label="预约到馆时间">
          <el-date-picker
            clearable
            v-model="form.borrowDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            :picker-options="{
      disabledDate(time) {
        return time.getTime() < Date.now();
      }
    }"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getRecommendBooksListByReaderId} from "@/api/rate/BookRatings";
import {borrowBook, getBookInfo} from "@/api/book/BookInfo";

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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 表单参数
      form: {},
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

    /** 借阅按钮操作 */
    handleBorrow(row) {
      this.reset();
      this.form.bookId = row.bookId;
      getBookInfo(row.bookId).then(response => {
        this.form.libraryId = response.data.libraryId;
        this.title = "填写借阅信息";
        this.open = true;
      })
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.$router.push({path: `/bookdetails/${row.bookId}`});
    },

    // 表单重置
    reset() {
      this.form = {
        bookId: null,
        libraryId: null,
        readerId: this.$store.state.user.id,
        borrowDate: new Date().toISOString().split('T')[0],
        title: null,
        author: null,
        isbn: null,
        publisher: null,
        publishDate: null,
        category: null,
        description: null,
        language: null,
        pages: null,
        coverUrl: null,
        edition: null,
        status: null,
        borrowMethod: null,
        comments: null
      };
      this.resetForm("form");
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 提交按钮 */
    submitForm() {
      console.log(this.form);
      borrowBook(this.form).then(response => {
        this.open = false;
        this.getList();
      })
    },
  }
}
</script>
