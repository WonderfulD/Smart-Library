import request from '@/utils/request'

// 查询图书借阅信息列表
export function listBookBorrowing(query) {
  return request({
    url: '/borrow/BookBorrowing/list',
    method: 'get',
    params: query
  })
}

// 查询图书借阅信息详细
export function getBookBorrowing(borrowId) {
  return request({
    url: '/borrow/BookBorrowing/' + borrowId,
    method: 'get'
  })
}

// 新增图书借阅信息
export function addBookBorrowing(data) {
  return request({
    url: '/borrow/BookBorrowing',
    method: 'post',
    data: data
  })
}

// 修改图书借阅信息
export function updateBookBorrowing(data) {
  return request({
    url: '/borrow/BookBorrowing',
    method: 'put',
    data: data
  })
}

// 删除图书借阅信息
export function delBookBorrowing(borrowId) {
  return request({
    url: '/borrow/BookBorrowing/' + borrowId,
    method: 'delete'
  })
}

//根据当前登录管理员所在图书馆（部门）id查询图书借阅信息列表
export function listBookBorrowingByDept(query) {
  return request({
    url: '/borrow/BookBorrowing/listByDept',
    method: 'get',
    params: query
  })
}