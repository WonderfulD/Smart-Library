import request from '@/utils/request'

// 查询图书馆信息列表
export function listLibraryInfo(query) {
  return request({
    url: '/library/LibraryInfo/list',
    method: 'get',
    params: query
  })
}

// 查询图书馆信息详细
export function getLibraryInfo(libraryId) {
  return request({
    url: '/library/LibraryInfo/' + libraryId,
    method: 'get'
  })
}

// 新增图书馆信息
export function addLibraryInfo(data) {
  return request({
    url: '/library/LibraryInfo',
    method: 'post',
    data: data
  })
}

// 修改图书馆信息
export function updateLibraryInfo(data) {
  return request({
    url: '/library/LibraryInfo',
    method: 'put',
    data: data
  })
}

// 删除图书馆信息
export function delLibraryInfo(libraryId) {
  return request({
    url: '/library/LibraryInfo/' + libraryId,
    method: 'delete'
  })
}
