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
      <el-form-item label="借出日期" prop="borrowDate">
        <el-date-picker clearable
                        v-model="queryParams.borrowDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择借出日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="应还日期" prop="dueDate">
        <el-date-picker clearable
                        v-model="queryParams.dueDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择应还日期">
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
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="BookBorrowingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="借阅号" align="center" prop="borrowId"/>
      <el-table-column label="图书编号" align="center" prop="bookId"/>
      <el-table-column label="书名" align="center" prop="title"/>
      <el-table-column label="图书封面" align="center">
        <template slot-scope="scope">
          <el-popover placement="top-start" title="" trigger="hover">
            <img :src="scope.row.coverUrl" alt="" style="width: 150px;height: 150px">
            <img slot="reference" :src="scope.row.coverUrl" style="width: 50px;height: 50px">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="借出日期" align="center" prop="borrowDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.borrowDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应还日期" align="center" prop="dueDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dueDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="借阅状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.latest_borrow_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="逾期罚款" align="center" prop="fine"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-reading"
            @click="handleReturn(scope.row)"
          >归还
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-time"
            @click="handleExtension(scope.row)"
          >申请延长还书日期
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-round"
            @click="handleChat(scope.row)"
          >联系图书馆
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

    <!-- 满意度调查对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" label-width="300px" label-position="top">
        <!-- 第一题 -->
        <el-form-item label="您是从什么渠道了解到智慧图书馆的？">
          <el-radio-group v-model="form.discoveryChannel">
            <el-radio :label="0">社交平台</el-radio>
            <el-radio :label="1">亲朋好友</el-radio>
            <el-radio :label="2">线下宣传</el-radio>
            <el-radio :label="3">其他</el-radio>
          </el-radio-group>
        </el-form-item>


        <!-- 第二题 -->
        <el-form-item label="您选择智慧图书馆的理由有哪些？">
          <el-checkbox-group v-model="form.selectionReasons">
            <el-checkbox :label="0">线上借阅，无需到馆</el-checkbox>
            <el-checkbox :label="1">资源丰富，满足需要</el-checkbox>
            <el-checkbox :label="2">选择多样，节约时间</el-checkbox>
            <el-checkbox :label="3">高效检索, 便捷服务</el-checkbox>
            <el-checkbox :label="4">实体体验, 数字管理</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <!-- 第三题 -->
        <el-form-item label="您对本次借阅是否满意？">
          <el-radio-group v-model="form.borrowingSatisfaction">
            <el-radio :label="5">非常满意</el-radio>
            <el-radio :label="4">满意</el-radio>
            <el-radio :label="3">一般</el-radio>
            <el-radio :label="2">不满意</el-radio>
            <el-radio :label="1">非常不满意</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 第四题 -->
        <el-form-item label="您对图书质量是否满意？">
          <el-radio-group v-model="form.bookQualitySatisfaction">
            <el-radio :label="5">非常满意</el-radio>
            <el-radio :label="4">满意</el-radio>
            <el-radio :label="3">一般</el-radio>
            <el-radio :label="2">不满意</el-radio>
            <el-radio :label="1">非常不满意</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 第五题 -->
        <el-form-item label="您对本次提供图书的图书馆是否满意？">
          <el-radio-group v-model="form.librarySatisfaction">
            <el-radio :label="5">非常满意</el-radio>
            <el-radio :label="4">满意</el-radio>
            <el-radio :label="3">一般</el-radio>
            <el-radio :label="2">不满意</el-radio>
            <el-radio :label="1">非常不满意</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 第六题 -->
        <el-form-item label="您是否愿意向其他人推荐智慧图书馆？">
          <el-radio-group v-model="form.recommendationWillingness">
            <el-radio :label="2">非常愿意</el-radio>
            <el-radio :label="1">有合适的机会愿意推荐</el-radio>
            <el-radio :label="0">不愿意</el-radio>
          </el-radio-group>
        </el-form-item>


        <!-- 第七题 -->
        <el-form-item label="您还有哪些建议和期望？">
          <el-input type="textarea" v-model="form.suggestions" placeholder="请输入您的宝贵意见"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 联系信息对话框 -->
    <el-dialog
      title="联系图书馆"
      :visible.sync="contactInfoDialogVisible"
      width="400px"
      class="custom-dialog"
    >
      <div>{{ currentContactInfo }}</div>
      <span slot="footer" class="dialog-footer">
      <el-button @click="contactInfoDialogVisible = false">关闭</el-button>
    </span>
    </el-dialog>

    <el-dialog
      title="选择您的归还方式"
      :visible.sync="returnMethodDialogVisible"
      width="500px"
    >
      <el-form :model="returnMethodForm">
        <el-form-item label="归还方式">
          <el-radio-group v-model="returnMethodForm.returnMethod">
            <el-radio :label="0">到馆</el-radio>
            <el-radio :label="1">邮寄</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="returnMethodForm.returnMethod === 1" label="快递单号">
          <el-input v-model="returnMethodForm.trackingNumber"></el-input>
        </el-form-item>
        <el-form-item v-if="returnMethodForm.returnMethod === 0" label="预约到馆时间">
          <el-date-picker
            clearable
            v-model="returnMethodForm.returnDate"
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
      <span slot="footer" class="dialog-footer">
    <el-button @click="submitReturnMethod">提交</el-button>
    <el-button @click="returnMethodDialogVisible = false">取消</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import {
  getBookBorrowing
} from "@/api/borrow/BookBorrowing";
import {
  borrowExtension,
  getReturnListWithStatusByReaderId, readerReturnBook
} from "@/api/book/BookInfo";
import {addBorrowRating} from "@/api/borrowrating/BorrowRating";
import {getDept} from "@/api/system/dept";

export default {
  name: "BookBorrowing",
  dicts: ['latest_borrow_status'],
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
      //联系信息弹出层
      contactInfoDialogVisible: false,
      //联系信息
      currentContactInfo: '',
      returnMethodDialogVisible: false, // 控制归还方式对话框的显示
      returnMethodForm: {
        borrowId: null,
        bookId: null,
        returnMethod: null, // 归还方式
        trackingNumber: '', // 快递单号
        returnDate: this.getNowFormatDate(), // 预约到馆时间，默认为今天
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        borrowId: null,
        bookId: null,
        readerId: null,
        borrowDate: null,
        dueDate: null,
        pendingStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        bookId: [
          {required: true, message: "书籍ID不能为空", trigger: "blur"}
        ],
        readerId: [
          {required: true, message: "读者ID不能为空", trigger: "blur"}
        ],
        libraryId: [
          {required: true, message: "图书馆ID不能为空", trigger: "blur"}
        ],
        borrowDate: [
          {required: true, message: "借出日期不能为空", trigger: "blur"}
        ],
        dueDate: [
          {required: true, message: "应还日期不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 根据当前登录管理员所在图书馆（部门）id查询图书借阅信息列表 */
    getList() {
      this.loading = true;
      getReturnListWithStatusByReaderId(this.queryParams).then(response => {
        this.BookBorrowingList = response.rows;
        this.total = response.total;
        this.loading = false;
      })
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
        pendingStatus: null,
        discoveryChannel: null,
        selectionReasons: [],
        borrowingSatisfaction: null,
        bookQualitySatisfaction: null,
        librarySatisfaction: null,
        recommendationWillingness: null,
        suggestions: null
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

    /** 打开用户评分表单 */
    handleForm(row) {
      this.reset();
      const borrowId = row.borrowId || this.ids
      getBookBorrowing(borrowId).then(response => {
        this.form = response.data;
        if (!this.form.selectionReasons || !Array.isArray(this.form.selectionReasons)) {
          this.$set(this.form, 'selectionReasons', []);
        }
        this.open = true;
        this.title = "借阅满意度调查";
      });
    },

    /** 归还按钮操作 */
    handleReturn(row) {
      // 初始化默认表单状态
      this.returnMethodForm = {
        borrowId: row.borrowId,
        bookId: row.bookId,
        returnMethod: null,
        trackingNumber: '',
        returnDate: this.getNowFormatDate(),
      };
      getBookBorrowing(row.borrowId).then(response => {
        // 使用Vue.set确保所有属性都是响应式的
        Object.keys(response.data).forEach(key => {
          if (key === 'comments') {
            // 特别处理 comments 字段，提取日期
            const extractedDate = this.parseDateFromString(response.data[key]);
            this.$set(this.returnMethodForm, 'returnDate', extractedDate);
          } else {
            this.$set(this.returnMethodForm, key, response.data[key]);
          }
        });
        this.returnMethodDialogVisible = true;
      }).catch(error => {
        console.error('Error fetching borrowing details:', error);
        this.$message.error('无法加载借阅信息，请稍后再试');
      });
    },

    /** 解析日期字符串 */
    parseDateFromString(str) {
      const regex = /\d{4}-\d{2}-\d{2}/; // 匹配形如 yyyy-mm-dd 的日期格式
      const matches = str.match(regex);
      return matches ? matches[0] : null;
    },


    submitReturnMethod() {
      if (this.returnMethodForm.returnDate === null) this.returnMethodForm.returnDate = this.getNowFormatDate();
      getBookBorrowing(this.returnMethodForm.borrowId).then( response => {
        var flag = true;
        if (response.data.returnMethod !== null) flag = false;
        readerReturnBook(this.returnMethodForm).then( response => {
          if(response.code === 200) {
            this.$message.success(response.msg);
            if (flag) {
              this.handleForm({
                borrowId: this.returnMethodForm.borrowId,
              });
            }
            this.getList();
          }else {
            this.$message.success(response.msg);
          }
        })
      })
      this.returnMethodDialogVisible = false;
    },

    /** 申请延长归还日期按钮操作 */
    handleExtension(row) {
      if (row.status === 3) {
        this.$message.error('您已逾期，无法延长期限，请尽快归还书籍！');
      } else {
        const borrowInfo = {
          borrowId: row.borrowId,
          bookId: row.bookId,
          dueDate: row.dueDate,
        };
        console.log(borrowInfo);
        // 调用API函数，传入借阅信息
        borrowExtension(borrowInfo).then(response => {
          if (response.code === 200) {
            // 延期成功
            this.$message.success('您已成功申请延期归还！');
            this.getList();
          } else {
            // 后端返回了错误状态，延期失败
            this.$message.error('申请延期失败，请稍后再试');
          }
        }).catch(error => {

        });
      }
    },

    /** 联系图书馆按钮操作 */
    handleChat(row) {
      getDept(row.libraryId).then( response => {
        this.currentContactInfo = response.data.contactInfo;
      })
      this.contactInfoDialogVisible = true; // 显示对话框
    },

    //获取当前日期函数
    getNowFormatDate() {
      let date = new Date(),
        year = date.getFullYear(), //获取完整的年份(4位)
        month = date.getMonth() + 1, //获取当前月份(0-11,0代表1月)
        strDate = date.getDate() // 获取当前日(1-31)
      if (month < 10) month = `0${month}` // 如果月份是个位数，在前面补0
      if (strDate < 10) strDate = `0${strDate}` // 如果日是个位数，在前面补0

      return `${year}-${month}-${strDate}`
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const today = new Date();
          // 转换selectionReasons数组为逗号分隔的字符串
          const submissionData = {
            ...this.form,
            selectionReasons: this.form.selectionReasons.join(','),
            ratingDate: today.toISOString().split('T')[0], // 格式化日期为YYYY-MM-DD
          };
          console.log(submissionData);
          if (this.form.borrowId != null) {
            addBorrowRating(submissionData).then(response => {
              this.$modal.msgSuccess("感谢您的参与");
              this.open = false;
              this.getList();
            });
          } else {
            this.$modal.msgSuccess("无法获取到借阅号，请稍后再试");
          }
        }
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

<style>
.custom-dialog .el-dialog {
  border-radius: 12px !important; /* 强制应用圆角到整个对话框 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1) !important; /* 添加阴影效果 */
  overflow: hidden; /* 隐藏溢出的内容，确保内部元素不超出圆角边界 */
}

.custom-dialog .el-dialog__header,
.custom-dialog .el-dialog__body,
.custom-dialog .el-dialog__footer {
  border-radius: 0 !important; /* 移除内部元素的圆角 */
  padding-left: 20px;
  padding-right: 20px;
}

.custom-dialog .el-dialog__header {
  background-color: #f5f5f5 !important; /* 应用头部背景色 */
  color: #333 !important; /* 应用头部文字颜色 */
}

.custom-dialog .el-dialog__body {
  padding: 20px !important; /* 应用主体内容的内边距 */
  background-color: white !important; /* 应用主体背景色 */
  color: #666 !important; /* 应用文字颜色 */
}
</style>
