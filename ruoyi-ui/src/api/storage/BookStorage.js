import request from '@/utils/request'

// 查询图书库存列表
export function listBookStorage(query) {
  return request({
    url: '/storage/BookStorage/list',
    method: 'get',
    params: query
  })
}

// 查询图书库存详细
export function getBookStorage(storageId) {
  return request({
    url: '/storage/BookStorage/' + storageId,
    method: 'get'
  })
}

// 新增图书库存
export function addBookStorage(data) {
  return request({
    url: '/storage/BookStorage',
    method: 'post',
    data: data
  })
}

// 修改图书库存
export function updateBookStorage(data) {
  return request({
    url: '/storage/BookStorage',
    method: 'put',
    data: data
  })
}

// 删除图书库存
export function delBookStorage(storageId) {
  return request({
    url: '/storage/BookStorage/' + storageId,
    method: 'delete'
  })
}
