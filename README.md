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

#### 优化
- 新页面
  - 分析页面
    - 展示每个种类最高评分的书（书名和评分）、每个种类最高阅读量的书（书名+阅读量）、每个种类（种类名+平均评分+某种类总阅读量） 
  - 回复读者页面
    - 展示读者联系信息
    - 回复读者信息
- 读者借阅优化
  - 在不同图书馆都拥有相同书籍时，将相同ISBN号的书折叠，只显示一条，后台处理读者具体接的是哪个图书馆的书
- 读者归还优化：
  - 将未还书籍优先展示~~似乎已经实现~~
- `馆际联盟`新功能：
  - 读者的“联系图书馆”按钮将会把联系信息放在该图书馆管理员的回复读者页面
  - 图书馆管理员的回复信息将会展示在读者`馆际联盟`页面中
- `控制台`优化(__紧急__)
  - `panelGroup`组件中`评分`应展示为其他待定
  - `雷达图`数据目前为模拟，需要从后端获取
