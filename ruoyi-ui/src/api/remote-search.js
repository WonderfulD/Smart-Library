import request from '@/utils/request'

export function searchUser(name) {
  return request({
    url: '/vue-element-admin/search/user',
    method: 'get',
    params: { name }
  })
}

export function transactionList(query) {
  return request({
    url: '/borrow/BookBorrowing/listWithStatus',
    method: 'get',
    params: query
  })
}


export function getCategoryDistributionByDeptId(query) {
  return request({
    url: '/book/BookInfo/categoryDistribution',
    method: 'get',
    params: query
  })
}

export function getCategoryDistributionByUserId(query) {
  return request({
    url: '/book/BookInfo/borrowedBooksCategoryDistribution',
    method: 'get',
    params: query
  })
}


export function getRecentBooksCounts(query) {
  return request({
    url: '/book/BookInfo/listRecentBooks',
    method: 'get',
    params: query
  })
}


export function getRecentBorrowsCounts(query) {
  return request({
    url: '/book/BookInfo/listRecentBorrows',
    method: 'get',
    params: query
  })
}


export function getRecentMembersCounts(query) {
  return request({
    url: '/borrow/BookBorrowing/listRecentMembers',
    method: 'get',
    params: query
  })
}

export function getTotalMembers(query) {
  return request({
    url: '/borrow/BookBorrowing/getTotalMembers',
    method: 'get',
    params: query
  })
}


