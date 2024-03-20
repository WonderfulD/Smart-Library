<template>
  <div class="book-details">
    <!-- 上层布局：图书封面与图书信息，放在一个卡片中 -->
    <el-card class="section-card" shadow="always">
      <el-row gutter="20">
        <!-- 封面图像 -->
        <el-col :span="8">
          <div class="cover-container">
            <img :src="bookInfo.coverUrl" class="cover-image">
          </div>
          <div class="overall-rating">
            <h3>读者评分: <el-rate
              class="overallRatingInStars"
              v-model="overallRating"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}">
            </el-rate></h3>
          </div>
        </el-col>
        <!-- 图书信息 -->
        <el-col :span="16">
          <div class="book-info">
            <h1>{{ bookInfo.title }}</h1>
            <p class="author">作者: {{ bookInfo.author }}</p>
            <p class="publisher">出版社: {{ bookInfo.publisher }}</p>
            <p class="publish-date">出版日期: {{ formatDate(bookInfo.publishDate) }}</p>
            <p v-if="bookInfo.summary" class="summary">简介: {{ bookInfo.summary }}</p>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 分割线 -->
    <hr class="section-divider">

    <!-- 下层布局：读者评分，放在另一个卡片中 -->
    <el-card class="section-card" shadow="always" v-if="!loading && BookRatingsList.length">
      <div class="ratings-header">
        <h2>读者评分</h2>
      </div>
      <div v-for="rating in BookRatingsList" :key="rating.ratingId" class="rating-item">
        <div class="rating-meta">
          <span class="rating-time">{{ formatDate(rating.ratingTime) }}</span>
          <span class="rating-score">{{ rating.rating }} 分</span>
        </div>
        <div class="rating-comment">{{ rating.comment }}</div>
      </div>
    </el-card>
    <el-alert v-else-if="!loading" title="暂无评分数据" type="info"></el-alert>
    <el-spinner v-else></el-spinner>
  </div>
</template>


<script>
import { getBookInfo } from "@/api/book/BookInfo";
import {getBookAverageRating, listBookRatings} from "@/api/rate/BookRatings";

export default {
  name: 'BookDetails',
  data() {
    return {
      bookInfo: null,
      bookId: this.$route.params.bookId,
      loading: false,
      BookRatingsList: [],
      overallRating: 0, // 添加一个用于存储总评分的数据属性
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bookId: this.$route.params.bookId
      },
    }
  },
  created() {
    this.fetchBookInfo();
    this.getRatingsList();
    this.getOverallRatings();
  },
  methods: {
    fetchBookInfo() {
      this.loading = true;
      getBookInfo(this.bookId)
        .then(response => {
          this.bookInfo = response.data;
          this.loading = false;
        })
        .catch(error => {
          console.error(error);
          this.loading = false;
        });
    },
    getRatingsList() {
      this.loading = true;
      listBookRatings(this.queryParams)
        .then(response => {
          this.BookRatingsList = response.rows;
          this.loading = false;
        })
        .catch(error => {
          console.error(error);
          this.loading = false;
        });
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'long', day: 'numeric', timeZone: 'UTC' };
      const date = new Date(dateString);
      return new Intl.DateTimeFormat('zh-CN', options).format(date);
    },
    /** 获取本书总体评分 */
    getOverallRatings() {
      getBookAverageRating(this.queryParams).then(response => {
        this.overallRating = response.data.averageRating;
      })
    }
  }
}
</script>

<style scoped>
.book-details {
  background-color: #FFFFFF;
  padding: 20px;
}

.section-card {
  margin-bottom: 20px; /* 为卡片间添加间距 */
  border-radius: 8px; /* 圆角效果 */
  box-shadow: 0 4px 6px rgba(0,0,0,0.1); /* 始终显示的阴影效果 */
}

.section-divider {
  border: 0;
  height: 1px;
  background-color: #ebebeb; /* 分割线颜色 */
  margin: 20px 0; /* 分割线上下的间距 */
}

.cover-container .cover-image {
  width: auto; /* 自动调整宽度 */
  max-height: 200px; /* 限制最大高度，根据需要调整 */
  max-width: 100%; /* 限制最大宽度，确保图像不超过容器宽度 */
  border-radius: 4px; /* 封面图像的圆角 */
  object-fit: contain; /* 保证图像完整显示，不裁剪 */
  display: block; /* 块级显示 */
  margin: 0 auto; /* 上下边距为0，左右自动，确保居中 */
}

.book-info {
  padding: 15px; /* 调整图书信息内边距，确保内容不会太拥挤 */
}

.book-info h1,
.book-info .author,
.book-info .publisher,
.book-info .publish-date {
  margin-bottom: 10px; /* 为图书信息中的各个元素添加间距 */
}

.description {
  white-space: pre-wrap; /* 保持介绍文本的格式，如空格和换行 */
}

.ratings-header h2 {
  margin-bottom: 10px; /* 为评分区域标题添加间距 */
}

.rating-item {
  border-bottom: 1px solid #eee; /* 为每个评分项添加底部边框 */
  padding: 10px 0; /* 为评分项添加内边距 */
}

.rating-meta {
  display: flex;
  justify-content: space-between; /* 评分时间和评分分数分布于两端 */
  margin-bottom: 10px; /* 与评论内容之间添加间距 */
}

.rating-time,
.rating-score {
  color: #666; /* 设置评分元数据的颜色 */
}

.rating-comment {
  color: #333; /* 设置评论内容的颜色 */
}

.overall-rating {
  margin-top: 30px; /* 在封面和评分信息之间添加一些间距 */
  text-align: center; /* 将文本居中显示 */
}

.overallRatingInStars {
  margin-top: 10px; /* 在封面和评分信息之间添加一些间距 */
  text-align: center; /* 将文本居中显示 */
}
</style>


