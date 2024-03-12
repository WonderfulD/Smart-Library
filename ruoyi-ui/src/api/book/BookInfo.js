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
