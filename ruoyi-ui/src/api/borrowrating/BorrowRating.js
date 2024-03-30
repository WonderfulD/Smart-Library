import request from '@/utils/request'

// 查询借阅评分列表
export function listBorrowRating(query) {
  return request({
    url: '/borrowrating/BorrowRating/list',
    method: 'get',
    params: query
  })
}

// 查询借阅评分详细
export function getBorrowRating(borrowId) {
  return request({
    url: '/borrowrating/BorrowRating/' + borrowId,
    method: 'get'
  })
}

// 新增借阅评分
export function addBorrowRating(data) {
  return request({
    url: '/borrowrating/BorrowRating',
    method: 'post',
    data: data
  })
}

// 修改借阅评分
export function updateBorrowRating(data) {
  return request({
    url: '/borrowrating/BorrowRating',
    method: 'put',
    data: data
  })
}

// 删除借阅评分
export function delBorrowRating(borrowId) {
  return request({
    url: '/borrowrating/BorrowRating/' + borrowId,
    method: 'delete'
  })
}
