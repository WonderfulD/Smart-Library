import request from '@/utils/request'

// 查询借阅评分列表
export function listBorrowRating(query) {
  return request({
    url: '/borrowrating/BorrowRating/list',
    method: 'get',
    params: query
  })
}

// 根据图书馆Id查询读者对此图书馆评分
export function listRatingsByLibraryId(query) {
  return request({
    url: '/borrowrating/BorrowRating/getRatings',
    method: 'get',
    params: query
  })
}

// 根据图书馆Id查询读者对此图书馆评分列表
export function listRatingsListByLibraryId(query) {
  return request({
    url: '/borrowrating/BorrowRating/listRecentRatings',
    method: 'get',
    params: query
  })
}

// 根据图书馆Id查询词云数据
export function listWordCloudDataByLibraryId(query) {
  return request({
    url: '/borrowrating/BorrowRating/getWordCloudByLibraryId',
    method: 'get',
    params: query
  })
}

// 根据图书馆Id查询雷达图数据
export function getRadarChartDataByLibraryId() {
  return request({
    url: '/borrowrating/BorrowRating/getRadarByLibraryId',
    method: 'get',
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
