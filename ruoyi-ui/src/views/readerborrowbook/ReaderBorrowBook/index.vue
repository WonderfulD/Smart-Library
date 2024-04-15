<!--复制藏书管理/图书信息/index.vue-->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="图书编号" prop="bookId">
        <el-input
          v-model="queryParams.bookId"
          placeholder="请输入图书编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="书名" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入书名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input
          v-model="queryParams.author"
          placeholder="请输入作者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="国际标准书号" prop="isbn">
        <el-input
          v-model="queryParams.isbn"
          placeholder="请输入国际标准书号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出版社" prop="publisher">
        <el-input
          v-model="queryParams.publisher"
          placeholder="请输入出版社"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图书分类" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入图书分类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借阅状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择借阅状态" clearable>
          <el-option
            v-for="dict in dict.type.borrow_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BookInfoList" @selection-change="handleSelectionChange">
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
      <el-table-column label="作者" align="center" prop="author" />
      <el-table-column label="国际标准书号" align="center" prop="isbn" />
      <el-table-column label="出版社" align="center" prop="publisher" />
      <el-table-column label="图书分类" align="center" prop="category" />
      <el-table-column label="图书描述" align="center" prop="description" />
      <el-table-column label="版次" align="center" prop="edition" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-reading"
            @click="handleBorrow(scope.row)"
          >借阅</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-discover"
            @click="handleView(scope.row)"
          >查看</el-button>
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
import {
  listBookInfo,
  getBookInfo,
  delBookInfo,
  addBookInfo,
  updateBookInfo, borrowBook
} from "@/api/book/BookInfo";

export default {
  name: "BookInfo",
  dicts: ['borrow_status'],
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
      // 图书副本信息表格数据
      BookInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookId: null,
        title: null,
        author: null,
        isbn: null,
        publisher: null,
        category: null,
        status: null,
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询所有图书馆图书副本信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.Status = 1; //过滤不可用的书籍
      listBookInfo(this.queryParams).then(response => {
        this.BookInfoList = response.rows;
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
      this.ids = selection.map(item => item.bookId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加图书副本信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const bookId = row.bookId || this.ids
      getBookInfo(bookId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改图书副本信息";
      });
    },

    /** 借阅按钮操作 */
    // handleBorrow(row) {
    //   // 事务管理，设置图书状态为借出+新增借阅记录至借阅表必须支持原子性
    //   const readerId = this.$store.state.user.id;
    //   const libraryId = row.libraryId;
    //   const today = new Date();
    //   const borrowInfo = {
    //     bookId: row.bookId,
    //     readerId: readerId,
    //     libraryId: libraryId,
    //     borrowDate: today.toISOString().split('T')[0], // 格式化日期为YYYY-MM-DD
    //   };
    //
    //   // 调用API函数，传入借阅信息
    //   borrowBook(borrowInfo).then(response => {
    //     if (response.code === 200) {
    //       // 借阅成功
    //       this.$message.success('借阅成功，待审核');
    //       this.getList();
    //     } else {
    //       // 后端返回了错误状态，借阅失败
    //       this.$message.error(response.message || '借阅失败');
    //     }
    //   }).catch(error => {
    //     // 请求发送失败或后端抛出异常
    //     console.error('Borrow operation failed:', error);
    //     this.$message.error('借阅失败，请稍后再试');
    //   });
    // },
    handleBorrow(row) {
      this.reset();
      this.form.bookId = row.bookId;
      getBookInfo(row.bookId).then( response => {
        this.form.libraryId = response.data.libraryId;
        this.title = "填写借阅信息";
        this.open = true;
      })
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.$router.push({ path: `/bookdetails/${row.bookId}` });
    },

    /** 提交按钮 */
    submitForm() {
      console.log(this.form);
      borrowBook(this.form).then(response => {
        this.open = false;
        this.getList();
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const bookIds = row.bookId || this.ids;
      this.$modal.confirm('是否确认删除图书副本信息编号为"' + bookIds + '"的数据项？').then(function() {
        return delBookInfo(bookIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('book/BookInfo/export', {
        ...this.queryParams
      }, `BookInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
