<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <blockquote class="text-warning" style="font-size: 14px">
          2024年（第17届）中国大学生计算机设计大赛
          <br/>
          <el-link
            href="https://jsjds.blcu.edu.cn/"
            type="primary"
            target="_blank"
          >https://jsjds.blcu.edu.cn/
          </el-link
          >
          <br/>
          项目仓库
          <br/>
          <el-link
            href="https://gitee.com/yuzi_work/jsjds2024"
            type="primary"
            target="_blank"
          >https://gitee.com/yuzi_work/jsjds2024
          </el-link
          >
        </blockquote>

        <hr/>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>智慧图书馆框架</h2>
        <p>
          项目简介占位
          <br>
          项目简介占位
          <br>
          项目简介占位
          <br>
          项目简介占位
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="danger">比赛项目</el-tag>
        </p>
        <p>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTarget('https://gitee.com/yuzi_work/jsjds2024')"
          >访问码云
          </el-button
          >
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('https://gitee.com/yuzi_work')"
          >访问主页
          </el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider/>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 码云主页：
              <el-link
                href="https://gitee.com/yuzi_work"
                target="_blank"
              >https://gitee.com/yuzi_work
              </el-link
              >
            </p>
            <p>
              <i class="el-icon-s-comment"></i> 邮箱：<a
              href="https://www.google.com/intl/zh-CN/gmail/about/"
              target="_blank">a550081589@gmail.com</a>
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="v0.4.0 - 2024-03-20">
              <ol>
                <li>新增图书详情页面，读者可以在此页面查看图书封面、图书信息和读者评分</li>
                <li>图书详情页面目前通过'藏书借还：书籍借阅'的'查看'按钮进入</li>
                <li>现在新增读者评分功能，每个读者对某本书的评分和图书总体评分可以在“图书详情”页面查看</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v0.3.0 - 2024-03-18">
              <ol>
                <li>新增藏书借还：书籍借阅模块，读者现在可以正常借阅图书，期限默认30天</li>
                <li>新增‘藏书借还：书籍归还’功能：读者现在可以正常归还图书并可正常计算逾期金额</li>
                <li>优化逻辑：现在借阅管理：图书借阅信息只根据当前登录的图书馆管理员所在图书馆展示借阅信息</li>
                <li>优化逻辑：现在藏书管理：图书信息只根据当前登陆的图书馆管理员所在图书馆展示藏书信息</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v0.2.0 - 2024-03-13">
              <ol>
                <li>新增借阅管理、藏书管理、馆际联盟功能模块</li>
                <li>解决了若干问题：权限问题、动态路由加载、馆际联盟下'图书馆信息'无法加载的问题</li>
                <li>现在用户可以自由导出图书馆藏书信息、图书馆信息、借阅信息</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v0.1.0 - 2024-03-11">
              <ol>
                <li>智慧图书馆部署上线</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>新增模块占位</span>
          </div>
          <div class="body">
            <img
              src="@/assets/images/jsjds.png"
              alt="donate"
              width="100%"
            />
            <span style="display: inline-block; height: 30px; line-height: 30px"
            >中国大学生计算机大赛</span
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "1.0.0"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

