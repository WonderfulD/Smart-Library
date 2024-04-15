import request from '@/utils/request'

// 查询图书副本信息列表
export function listBookInfo(query) {
  return request({
    url: '/book/BookInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图书副本信息详细
export function getBookInfo(bookId) {
  return request({
    url: '/book/BookInfo/' + bookId,
    method: 'get'
  })
}

// 新增图书副本信息
export function addBookInfo(data) {
  return request({
    url: '/book/BookInfo',
    method: 'post',
    data: data
  })
}

// 修改图书副本信息
export function updateBookInfo(data) {
  return request({
    url: '/book/BookInfo',
    method: 'put',
    data: data
  })
}

// 删除图书副本信息
export function delBookInfo(bookId) {
  return request({
    url: '/book/BookInfo/' + bookId,
    method: 'delete'
  })
}

//根据当前登录管理员所在图书馆id（部门id）查询图书副本信息列表
export function listBookInfoByLibraryId(query) {
  return request({
    url: '/book/BookInfo/listByLibrary',
    method: 'get',
    params: query
  })
}

// 根据提供的借阅信息借阅图书
export function borrowBook(data) {
  return request({
    url: '/book/BookInfo/borrow',
    method: 'post',
    data: data // 传递完整的借阅信息
  });
}

//根据借阅人ID查询图书借阅信息
export function listBookBorrowingListByReaderId(query) {
  return request({
    url: '/book/BookInfo/listByReader',
    method: 'get',
    params: query
  })
}

// 图书馆管理员根据提供的借阅信息确认归还图书
export function adminReturnBook(data) {
  return request({
    url: '/book/BookInfo/return',
    method: 'post',
    data: data
  });
}

// 读者归还图书
export function readerReturnBook(data) {
  return request({
    url: '/book/BookInfo/readerReturn',
    method: 'post',
    data: data
  });
}

// 根据提供的借阅信息完成借阅延期
export function borrowExtension(data) {
  return request({
    url: '/book/BookInfo/extension',
    method: 'post',
    data: data
  });
}

//根据借阅人ID查询应还图书列表
export function getReturnListWithStatusByReaderId(query) {
  return request({
    url: '/book/BookInfo/returnListWithStatusByReader',
    method: 'get',
    params: query
  })
}



