<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="图书编号" prop="bookId">
        <el-input
          v-model="queryParams.bookId"
          placeholder="请输入图书编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评分" prop="rating">
        <el-input
          v-model="queryParams.rating"
          placeholder="请输入评分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['rate:BookRatings:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BookRatingsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图书编号" align="center" prop="bookId" />
      <el-table-column  label="图书封面" align="center">
        <template slot-scope="scope">
          <el-popover placement="top-start" title="" trigger="hover">
            <img :src="scope.row.coverUrl" alt="" style="width: 150px;height: 150px">
            <img slot="reference" :src="scope.row.coverUrl" style="width: 50px;height: 50px">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="书名" align="center" prop="title" />
      <el-table-column label="藏书博物馆" align="center" prop="deptName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleRating(scope.row)"
          >评分</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改已阅图书评分对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="160px" label-position="top">
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="请为该图书打分">
          <el-radio-group v-model="form.rating">
            <el-radio :label="5">非常满意</el-radio>
            <el-radio :label="4">满意</el-radio>
            <el-radio :label="3">一般</el-radio>
            <el-radio :label="2">不满意</el-radio>
            <el-radio :label="1">非常不满意</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="您对该图书评价如何？">
          <el-input type="textarea" v-model="form.comment" placeholder="请留下您的宝贵意见"></el-input>
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
import {
  delBookRatings,
  addBookRatings,
  updateBookRatings,
  listBookRatings
} from "@/api/rate/BookRatings";
import {parseTime} from "@/utils/ruoyi";
import {listByUserDistinctBooks} from "@/api/borrow/BookBorrowing";

export default {
  name: "BookRatings",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 已阅图书评分表格数据
      BookRatingsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookId: null,
        rating: null,
        ratingTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bookId: [
          { required: true, message: "图书编号不能为空", trigger: "blur" }
        ],
        readerId: [
          { required: true, message: "读者号不能为空", trigger: "blur" }
        ],
        rating: [
          { required: true, message: "评分不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询已阅图书评分列表 */
    getList() {
      this.loading = true;
      listByUserDistinctBooks(this.queryParams).then(response => {
        this.BookRatingsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        ratingId: null,
        bookId: null,
        readerId: null,
        rating: null,
        comment: null,
        ratingTime: null,
        title: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.ratingId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    /** 评分按钮操作 */
    handleRating(row) {
      this.reset();
      const bookId = row.bookId;
      listBookRatings({
        readerId: this.$store.state.user.id,
        bookId: bookId
      }).then(response => {
        if (response.total === 0) {
          this.form.title = row.title;
          this.form.bookId = bookId;
          this.form.readerId = row.readerId;
          this.form.ratingTime = Date;
        }else {
          this.form = response.rows[0];
        }
        this.open = true;
        this.title = "图书评分";
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.ratingId != null) {
            updateBookRatings(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            console.log(this.form);
            addBookRatings(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ratingIds = row.ratingId || this.ids;
      this.$modal.confirm('是否确认删除已阅图书评分编号为"' + ratingIds + '"的数据项？').then(function() {
        return delBookRatings(ratingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('rate/BookRatings/export', {
        ...this.queryParams
      }, `BookRatings_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
