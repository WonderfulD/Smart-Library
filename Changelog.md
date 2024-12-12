# 更新日志

自`2024年12月12日（星期四）17:00:00 (UTC+8)`起，所有显著的更改都记录在此文件中

## [未发布]

### 新增
- 对更多的功能添加缓存

### 变更
- 删除`Books`表中`status`字段
- 删除`Books`表中`library_id`字段
- 借阅ID考虑使用UUID替代项目实现的方法

### 修复
- 修复`Books`表某条记录不全时，可能导致的显示问题。

### 其他
- 优化数据库查询性能。
- `Books`表中`status`字段是否可删除待验证

## [0.1.0] - 2024-12-12

### 新增
- 初始发布版本，包含以下功能：
    - 省略，参考`README.md`文件
- 根据图书馆ID查询总藏书量接口
- 删除指定图书馆和图书ID列表的订购信息接口
- 前端function`getTotalBooksCountByLibraryId`，根据当前登录管理员所在图书馆id（部门id）查询总藏书量

### 变更
- 删除`Books`表中`status`字段
- 删除`Books`表中`library_id`字段
- 删除了`预估未来七天的借阅量`方法`estimateFutureBorrowCounts`
- **删除了`Books`实体类中关于`libraryId`的字段和方法**
- 删除了`BooksMapper`XML文件中关于`libraryId`的字段和方法中的语句
- 删除了`BooksMapper`XML文件中`selectBooksListByLibrary`方法
- 删除了`BooksMapper`中`根据图书馆ID查询图书副本信息列表`方法`selectBooksListByLibrary`
- 删除了`IBooksService`及其实现类`BooksServiceImpl`中`根据图书馆ID查询图书副本信息列表`方法`selectBooksListByLibrary`
- 变更了`IBooksService`及其实现类`BooksServiceImpl`中`selectAvailableBooksList`方法为`selectBooksListByIds`
- 修改了前端`插入图书信息表单`中的表单校验规则，除图书封面url字段外其余字段均强制填写
- 修改了前端`修改图书信息表单`，现在不显示`数量`表单项
- 修改了图书馆管理员的控制台页面中`藏书量`panel的数据获取方式
  - 通过`getTotalBooksCountByLibraryId`方法获取

### 修复
- 新增图书副本信息接口
  -  现在新增图书时可以联动增加订购表记录
- 删除图书副本信息接口
  -  现在删除图书时可以联动删除订购表记录

### 其他
- 已部署上线
