<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="借阅号" prop="borrowId">
        <el-input
          v-model="queryParams.borrowId"
          placeholder="请输入借阅号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="书籍编号" prop="bookId">
        <el-input
          v-model="queryParams.bookId"
          placeholder="请输入书籍编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="读者号" prop="readerId">
        <el-input
          v-model="queryParams.readerId"
          placeholder="请输入读者号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请日期" prop="borrowDate">
        <el-date-picker clearable
                        v-model="queryParams.borrowDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择申请日期">
        </el-date-picker>
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
          v-hasPermi="['borrow:BookBorrowing:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BookBorrowingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="借阅号" align="center" prop="borrowId"/>
      <el-table-column label="书籍编号" align="center" prop="bookId"/>
      <el-table-column  label="书籍封面" align="center">
        <template slot-scope="scope">
          <el-popover placement="top-start" title="" trigger="hover">
            <img :src="scope.row.coverUrl" alt="" style="width: 150px;height: 150px">
            <img slot="reference" :src="scope.row.coverUrl" style="width: 50px;height: 50px">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="书名" align="center" prop="title"/>
      <el-table-column label="读者号" align="center" prop="readerId"/>
      <el-table-column label="应还日期" align="center" prop="dueDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dueDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还方式" align="center" prop="returnMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.return_method" :value="scope.row.returnMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleCheck(scope.row)"
          >确认
          </el-button>
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

    <!-- 添加或修改图书借阅信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="借阅编号" prop="borrowId">
          <el-input v-model="form.borrowId" placeholder="请输入借阅编号" :disabled="true"/>
        </el-form-item>
        <el-form-item label="书籍编号" prop="bookId">
          <el-input v-model="form.bookId" placeholder="请输入书籍编号" :disabled="true"/>
        </el-form-item>
        <el-form-item label="读者号" prop="readerId">
          <el-input v-model="form.readerId" placeholder="请输入读者号" :disabled="true"/>
        </el-form-item>
        <el-form-item label="归还方式" prop="comments">
          <el-input v-model="form.comments"  placeholder="请输入归还方式" :disabled="true"/>
        </el-form-item>
        <el-form-item v-if="form.returnMethod === 1" label="快递单号">
          <el-input v-model="form.trackingNumber" placeholder="请输入快递单号" :disabled="true"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确认收到图书</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBookBorrowing,
  delBookBorrowing,
  listReturnPendingByDept
} from "@/api/borrow/BookBorrowing";

import {adminReturnBook, updateBookInfo} from "@/api/book/BookInfo";

export default {
  name: "BookBorrowing",
  dicts: ['return_method'],
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
      // 图书借阅信息表格数据
      BookBorrowingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        borrowId: null,
        bookId: null,
        readerId: null,
        borrowDate: null,
        dueDate: null,
        status: null,
        pendingStatus: null
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 根据当前登录管理员所在图书馆（部门）id查询图书归还确认信息列表 */
    getList() {
      this.loading = true;
      listReturnPendingByDept(this.queryParams).then(response => {
        this.BookBorrowingList = response.rows;
        this.total = response.total;
        this.loading = false;
      })
    },


    /** 限制选择当天之前的日子 */
    disabledDate(time) {
      return time.getTime() < Date.now();
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        borrowId: null,
        bookId: null,
        readerId: null,
        libraryId: null,
        borrowDate: null,
        dueDate: null,
        returnDate: null,
        fine: null,
        comments: null,
        status: null,
        pendingStatus: null
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
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.borrowId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加图书借阅信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const borrowId = row.borrowId || this.ids
      getBookBorrowing(borrowId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改图书借阅信息";
      });
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.reset();
      const borrowId = row.borrowId || this.ids
      getBookBorrowing(borrowId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "读者归还方式";
      });
    },

    /** 确认按钮操作 */
    handleCheck(row) {
      this.reset();
      const borrowId = row.borrowId || this.ids
      getBookBorrowing(borrowId).then(response => {
        this.form = response.data;
        this.submitForm();
      });
    },

    /** 提交按钮 */
    submitForm() {
      adminReturnBook(this.form).then( response => {
        this.open = false;
        this.getList();
      });
    },


    /** 删除按钮操作 */
    handleDelete(row) {
      const borrowIds = row.borrowId || this.ids;
      this.$modal.confirm('是否确认删除图书借阅信息编号为"' + borrowIds + '"的数据项？').then(function () {
        return delBookBorrowing(borrowIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('borrow/BookBorrowing/export', {
        ...this.queryParams
      }, `BookBorrowing_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
