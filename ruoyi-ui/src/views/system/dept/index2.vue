<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="图书馆编号" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入图书馆编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图书馆名称" prop="deptName">
        <el-input
          v-model="queryParams.deptName"
          placeholder="请输入图书馆名称"
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
          v-hasPermi="['system:dept:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deptList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图书馆编号" align="center" prop="deptId" />
      <el-table-column label="图书馆名称" align="center" prop="deptName" />
      <el-table-column label="负责人" align="center" prop="leader" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="图书馆地址" align="center" prop="address" />
      <el-table-column label="开馆时间" align="center" prop="openHour" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.deptName !== currentDeptName"
            size="mini"
            type="text"
            icon="el-icon-phone"
            @click="handleContact(scope.row)"
          >联系他们</el-button>
          <el-button
            v-else
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
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

    <!-- 添加或修改图书馆信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" label-position="left">
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="form.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="开馆时间" prop="openHour">
          <el-input v-model="form.openHour" placeholder="请输入图书馆联系信息" />
        </el-form-item>
        <el-form-item label="联系信息" prop="contactInfo">
          <el-input v-model="form.contactInfo" placeholder="请输入图书馆联系信息" />
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
  </div>
</template>

<script>
import {listLibrary, getDept, delDept, addDept, updateDept } from "@/api/system/dept";
import {getUser} from "@/api/system/user";

export default {
  name: "Dept",
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
      // 图书馆信息表格数据
      deptList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //联系信息弹出层
      contactInfoDialogVisible: false,
      //联系信息
      currentContactInfo: '',
      currentDeptName: null, //当前登录用户所在图书馆名称
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId: null,
        deptName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDeptName();
  },
  methods: {
    /** 查询图书馆信息列表 */
    getList() {
      this.loading = true;
      listLibrary(this.queryParams).then(response => {
        // 后端的数据直接放在response.data中
        this.deptList = response.data;
        // 由于后端没有提供total字段，使用deptList的长度作为总数
        this.total = response.data.length;
        this.loading = false;
      });
    },

    /** 查询当前登录用户所在图书馆名称 **/
    getDeptName() {
      getUser(this.$store.state.user.id).then( response => {
        this.currentDeptName = response.data.dept.deptName;
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
        deptId: null,
        parentId: null,
        ancestors: null,
        deptName: null,
        orderNum: null,
        leader: null,
        phone: null,
        email: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        address: null,
        contactInfo: null,
        openHour: null
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
      this.ids = selection.map(item => item.deptId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加图书馆信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const deptId = row.deptId || this.ids
      getDept(deptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改图书馆信息";
      });
    },

    /** 联系他们按钮操作 */
    handleContact(row) {
      this.currentContactInfo = row.contactInfo; // 从点击行获取联系信息
      this.contactInfoDialogVisible = true; // 显示对话框
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId != null) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(response => {
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
      const deptIds = row.deptId || this.ids;
      this.$modal.confirm('是否确认删除图书馆信息编号为"' + deptIds + '"的数据项？').then(function() {
        return delDept(deptIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dept/export', {
        ...this.queryParams
      }, `libraryList_${new Date().getTime()}.xlsx`)
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


