# Jsjds2024

#### 介绍
计算机设计大赛2024

#### 软件架构
##### 后端技术
- SpringBoot
- Spring Security
- JWT
- MyBatis
- Druid
- Fastjson

##### 前端技术
- Vue
- Vuex
- Element-ui
- Axios
- Sass
- Quill


#### 安装教程

1. 进入项目目录
`cd ruoyi-ui`
2. 安装依赖
`npm install`
3. 启动服务
`npm run dev`

#### 使用说明

1. 解决 `Node.js` 加密库兼容性问题 
- 设置 `Node.js` 加密策略，在运行构建脚本之前在终端中设置：
```bash
set NODE_OPTIONS=--openssl-legacy-provider
```
2. 解决文件上传时 `URL` 没有自动加端口问题(部署到服务器时)
- 在 `application.yml` 中修改文件路径 `profile` 为 
```java
/home/ruoyi/uploadPath
```
- 在 `CommonController.java` 的通用上传请求（多个）接口中修改 `fileName` 为
```java
"/prod-api" + FileUploadUtils.upload(filePath, file);
```

#### 目前已知的问题
- 读者借阅图书失败时事务并未显示原子性，`Books`表`status`字段显示`1`(`借出`)，但`BookBorrowing`表并未新增记录
