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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['book:BookInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['book:BookInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['book:BookInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['book:BookInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BookInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图书编号" align="center" prop="bookId" />
      <el-table-column label="书名" align="center" prop="title" />
      <el-table-column label="作者" align="center" prop="author" />
      <el-table-column label="国际标准书号" align="center" prop="isbn" />
      <el-table-column label="出版社" align="center" prop="publisher" />
      <el-table-column label="图书分类" align="center" prop="category" />
      <el-table-column label="图书描述" align="center" prop="description" />
      <el-table-column label="版次" align="center" prop="edition" />
      <el-table-column label="借阅状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.borrow_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['book:BookInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['book:BookInfo:remove']"
          >删除</el-button>
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

    <!-- 添加或修改图书副本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="国际标准书号" prop="isbn">
          <el-input v-model="form.isbn" placeholder="请输入国际标准书号" />
        </el-form-item>
        <el-form-item label="出版社" prop="publisher">
          <el-input v-model="form.publisher" placeholder="请输入出版社" />
        </el-form-item>
        <el-form-item label="出版日期" prop="publishDate">
          <el-date-picker clearable
            v-model="form.publishDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出版日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="图书分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入图书分类" />
        </el-form-item>
        <el-form-item label="图书描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="图书语言" prop="language">
          <el-input v-model="form.language" placeholder="请输入图书语言" />
        </el-form-item>
        <el-form-item label="页数" prop="pages">
          <el-input v-model="form.pages" placeholder="请输入页数" />
        </el-form-item>
        <el-form-item label="封面图片URL" prop="coverUrl">
          <el-input v-model="form.coverUrl" placeholder="请输入封面图片URL" />
        </el-form-item>
        <el-form-item label="版次" prop="edition">
          <el-input v-model="form.edition" placeholder="请输入版次" />
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
  updateBookInfo,
  listBookInfoByLibraryId
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
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        libraryId: [
          { required: true, message: "图书馆ID不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "书名不能为空", trigger: "blur" }
        ],
        author: [
          { required: true, message: "作者不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 根据登陆管理员所在图书馆ID查询图书副本信息列表 */
    getList() {
      this.loading = true;
      // listBookInfo(this.queryParams).then(response => {
      //   this.BookInfoList = response.rows;
      //   this.total = response.total;
      //   this.loading = false;
      // });
      listBookInfoByLibraryId(this.queryParams).then(response => {
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
        status: null
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
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bookId != null) {
            updateBookInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBookInfo(this.form).then(response => {
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
