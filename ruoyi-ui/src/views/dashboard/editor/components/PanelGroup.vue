<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-education">
          <svg-icon icon-class="education" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            总借阅书籍
          </div>
          <count-to :start-val="0" :end-val=booksCount :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-list">
          <svg-icon icon-class="list" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            待还书籍
          </div>
          <count-to :start-val="0" :end-val=ReturnCount :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-peoples">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            会员天数
          </div>
          <count-to :start-val="0" :end-val=memberDay :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-star">
          <svg-icon icon-class="star" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            评价过的书籍
          </div>
          <count-to :start-val="0" :end-val="ratedBooksCount" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import {getReturnListWithStatusByReaderId} from "@/api/book/BookInfo";
import {listBookBorrowingByUser} from "@/api/borrow/BookBorrowing";
import {getUser} from "@/api/system/user";
import {listBookRatings} from "@/api/rate/BookRatings";

export default {
  components: {
    CountTo
  },
  created() {
    this.getCounts();
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    getCounts() {
      listBookBorrowingByUser().then(response => {
        this.booksCount = response.total;
      })
      getReturnListWithStatusByReaderId().then(response => {
        this.ReturnCount = response.total;
      })
      getUser(this.$store.state.user.id).then(response => {
        const creationDate = new Date(response.data.createTime);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        creationDate.setHours(0, 0, 0, 0);
        const differenceInMilliseconds = today - creationDate;
        const differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);
        this.memberDay = Math.floor(differenceInDays);
        if (this.memberDay < 1) {
          this.memberDay = 0;
        }
      });
      listBookRatings({
        readerId : this.$store.state.user.id
      }).then(response => {
        this.ratedBooksCount = response.total;
      })
    }
  },
  data() {
    return {
      booksCount: 0,
      ReturnCount: 0,
      memberDay: 0,
      ratedBooksCount: 0,
    }
  },
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-education {
        background: #40c9c6;
      }

      .icon-list {
        background: #36a3f7;
      }

      .icon-peoples {
        background: #f4516c;
      }

      .icon-star {
        background: #ffD700
      }
    }

    .icon-education {
      color: #40c9c6;
    }

    .icon-list {
      color: #36a3f7;
    }

    .icon-peoples {
      color: #f4516c;
    }

    .icon-star {
      color: #ffD700
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
