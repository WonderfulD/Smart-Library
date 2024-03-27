<template>
  <el-card class="box-card-component" style="margin-left:8px;">
    <div slot="header" class="box-card-header">
      <img :src="libraryBackgroundImage" alt="Library Background">
    </div>
    <div style="position:relative;">
      <!-- 图书馆的logo -->
      <pan-thumb :image="libraryLogoImage" class="panThumb" />
      <mallki class-name="mallki-text" text="本馆借阅情况汇总" />
      <div style="padding-top:35px;" class="progress-item">
        <span>如期归还</span>
        <el-progress :percentage="returnOnTimePercent" />
      </div>
      <div class="progress-item">
        <span>借阅正常</span>
        <el-progress :percentage="borrowingNormalPercent" />
      </div>
      <div class="progress-item">
        <span>逾期归还</span>
        <el-progress :percentage="returnLatePercent" />
      </div>
      <div class="progress-item">
        <span>逾期未还</span>
        <el-progress :percentage="notReturnedPercent" />
      </div>
    </div>
  </el-card>
</template>

<script>
import { transactionList } from '@/api/remote-search'
import Mallki from "@/components/TextHoverEffect/Mallki.vue";
import PanThumb from "@/components/PanThumb/index.vue";
import {getInfo} from "@/api/login";

export default {
  components: {PanThumb, Mallki},
  data() {
    return {
      returnOnTimePercent: 0,
      borrowingNormalPercent: 0,
      returnLatePercent: 0,
      notReturnedPercent: 0,
      libraryBackgroundImage: '', // 存储背景图片路径
      libraryLogoImage: '', // 存储logo图片路径
    }
  },
  created() {
    this.fetchData();
    this.fetchLibraryIdAndSetBackgroundImage();
    this.fetchLibraryIdAndSetLogo();
  },
  methods: {
    fetchData() {
      transactionList(this.queryParams).then(response => {
        const total = response.total;
        const statusCounts = response.rows.reduce((acc, { status }) => {
          acc[status] = (acc[status] || 0) + 1;
          return acc;
        }, {});

        // 根据状态计算百分比
        this.returnOnTimePercent = Number(((statusCounts[0] || 0) / total * 100).toFixed(2));
        this.borrowingNormalPercent = Number(((statusCounts[1] || 0) / total * 100).toFixed(2));
        this.returnLatePercent = Number(((statusCounts[2] || 0) / total * 100).toFixed(2));
        this.notReturnedPercent = Number(((statusCounts[3] || 0) / total * 100).toFixed(2));
      })
    },
    fetchLibraryIdAndSetBackgroundImage() {
      getInfo().then(response => {
        // 假设response中包含user对象，user对象中包含deptId
        const libraryId = response.user.deptId;
        // 根据libraryId动态构造背景图片的路径
        this.libraryBackgroundImage = require(`@/assets/library_background/${libraryId}.jpg`);
      }).catch(error => {
        console.error('Failed to fetch library information:', error);
        // 在出错或找不到图片时，可以设置一个默认的背景图片
        this.libraryBackgroundImage = require('@/assets/library_background/default.jpg');
      });
    },
    fetchLibraryIdAndSetLogo() {
      getInfo().then(response => {
        const libraryId = response.user.deptId;
        // 动态构造logo图片的路径，适用于本地静态资源
        this.libraryLogoImage = require(`@/assets/library_logo/${libraryId}.png`);
      }).catch(error => {
        console.error('Failed to fetch library information:', error);
        // 设置一个默认logo图片
        this.libraryLogoImage = require('@/assets/library_logo/default.png');
      });
    },
  }
}
</script>

<style lang="scss" >
.box-card-component{
  .el-card__header {
    padding: 0px!important;
  }
}
</style>
<style lang="scss" scoped>
.box-card-component {
  .box-card-header {
    position: relative;
    height: 220px;
    img {
      width: 100%;
      height: 100%;
      transition: all 0.2s linear;
      &:hover {
        transform: scale(1.1, 1.1);
        filter: contrast(130%);
      }
    }
  }
  .mallki-text {
    position: absolute;
    top: 0px;
    right: 0px;
    font-size: 20px;
    font-weight: bold;
  }
  .panThumb {
    z-index: 100;
    height: 70px!important;
    width: 70px!important;
    position: absolute!important;
    top: -45px;
    left: 0px;
    border: 5px solid #ffffff;
    background-color: #fff;
    margin: auto;
    box-shadow: none!important;
    ::v-deep .pan-info {
      box-shadow: none!important;
    }
  }
  .progress-item {
    margin-bottom: 10px;
    font-size: 14px;
  }
  @media only screen and (max-width: 1510px){
    .mallki-text{
      display: none;
    }
  }
}
</style>
