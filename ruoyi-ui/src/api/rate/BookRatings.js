import request from '@/utils/request'

// 查询藏书评分列表
export function listBookRatings(query) {
  return request({
    url: '/rate/BookRatings/list',
    method: 'get',
    params: query
  })
}

// 查询藏书评分详细
export function getBookRatings(ratingId) {
  return request({
    url: '/rate/BookRatings/' + ratingId,
    method: 'get'
  })
}


// 新增藏书评分
export function addBookRatings(data) {
  return request({
    url: '/rate/BookRatings',
    method: 'post',
    data: data
  })
}

// 修改藏书评分
export function updateBookRatings(data) {
  return request({
    url: '/rate/BookRatings',
    method: 'put',
    data: data
  })
}

// 删除藏书评分
export function delBookRatings(ratingId) {
  return request({
    url: '/rate/BookRatings/' + ratingId,
    method: 'delete'
  })
}


// 获取藏书总评分
export function getBookAverageRating(query) {
  return request({
    url: '/rate/BookRatings/averageRating',
    method: 'get',
    params: query
  })
}
