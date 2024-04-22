<template>
  <section class="testimonial-sec">
    <base-container>
      <h2 class="testimonial-sec__title">相关研究</h2>
      <div class="testimonial-wrap">
        <svg class="testimonial-icon" width="64" height="64">
          <use href="../../assets/index/icons.svg#quote"></use>
        </svg>
        <div class="swiper-container">
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="testimonial in testimonials" :key="testimonial.id">
              <article class="testimonial">
                <p class="testimonial__quote">{{ testimonial.quote }}</p>
                <footer class="testimonial__footer">
                  <div>
                    <h5 class="testimonial__name">{{ testimonial.name }}</h5>
                    <div class="testimonial__position">{{ testimonial.position }}</div>
                  </div>
                </footer>
              </article>
            </div>
          </div>
          <!-- Swiper Pagination -->
          <div class="swiper-pagination"></div>
        </div>
      </div>
      <footer class="testimonial-sec__footer">
        <!-- Swiper Navigation -->
        <button :class="{'carousel-nav__button': true, 'testimonial-sec__button-prev': true, 'swiper-button-disabled': isBeginning}" @click="prevSlide">
          <svg class="arrow-icon" width="24" height="24">
            <use href="../../assets/index/icons.svg#chevron-left"></use>
          </svg>
        </button>
        <button :class="{'carousel-nav__button': true, 'testimonial-sec__button-next': true, 'swiper-button-disabled': isEnd}" @click="nextSlide">
          <svg class="arrow-icon" width="24" height="24">
            <use href="../../assets/index/icons.svg#chevron-right"></use>
          </svg>
        </button>
      </footer>
    </base-container>
  </section>
</template>

<script>
import Swiper from 'swiper';
import 'swiper/swiper-bundle.css';

export default {
  data() {
    return {
      testimonials: [
        {
          id: 1,
          quote: "数字化、网络化和智能化是智慧图书馆的信息技术基础，而以人为本、绿色发展、方便读者则是智慧图书馆的灵魂与精髓；其内在特征是以人为本的可持续发展，以满足日益增长的读者的信息需求。智慧图书馆是未来图书馆发展的新模式，将使图书馆真正迈向可持续发展之路。",
          name: "王世伟",
          position: "上海社会科学院信息研究所"
        },
        {
          id: 2,
          quote: "发展至今，图书馆已经不再是简单的存储信息资源的场所，随着图书馆从最初的传统图书馆，进化到自动化图书馆、复合型图书馆阶段，跨越数字化图书馆阶段，目前已经进入智慧图书馆阶段。智慧图书馆是智慧城市建设的衍生概念，近年来成为图书馆研究领域的热点，它再一次革新了图书馆的建设方式、服务理念和服务领域，推动着现代城市的公共文化服务体系的又一次重大革新升级，对满足人们多元化的文化信息服务需求具有重要意义。",
          name: "王海丰",
          position: "桂林电子科技大学图书馆"
        },
        {
          id: 3,
          quote: "近年来，随着用户信息服务需求的不断升级以及新兴技术的高度发展，智慧化转型成为我国图书馆发展的必然趋势，以智慧图书馆为主题的相关研究已经成为国内图书情报领域的研究热点。智慧图书馆建设是《中华人民共和国国民经济和社会发展第十四个五年规划和2035年远景目标纲要》中数字化转型的重要内容。",
          name: "吴丹",
          position: "武汉大学信息管理学院"
        }
      ],
      swiper: null,
      isBeginning: true,
      isEnd: false
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.swiper = new Swiper('.swiper-container', {
        loop: false, // 禁用循环滑动
        pagination: {
          el: '.swiper-pagination',
          clickable: true,
        },
        spaceBetween: 30,
        on: {
          slideChange: this.updateNavigationState
        }
      });
      this.updateNavigationState();
    });
  },
  methods: {
    prevSlide() {
      if (this.swiper) {
        this.swiper.slidePrev();
      }
    },
    nextSlide() {
      if (this.swiper) {
        this.swiper.slideNext();
      }
    },
    updateNavigationState() {
      this.isBeginning = this.swiper.isBeginning;
      this.isEnd = this.swiper.isEnd;
    }
  }
};
</script>

<style scoped>
.testimonial-sec {
  padding-top: 2rem;
  padding-bottom: 2rem;
}

.testimonial-sec__title {
  font-size: 2rem;
  margin-bottom: 3.5rem;
  line-height: 1.5;
  color: var(--blue-900);
}

.testimonial-wrap {
  padding: 2rem 2.5rem 3rem;
  border-radius: 2rem;
  background-color: var(--white);
}

.testimonial-icon {
  color: var(--green-400);
}

.testimonial {
  padding-top: 2rem;
}

.testimonial__quote {
  font-size: 1rem;
  line-height: 200%;
  margin: 0 auto 2rem;
  color: var(--blue-900);
}

.testimonial__name {
  font-size: 1rem;
  margin-bottom: 1rem;
  color: var(--gray-800);
}

.testimonial__position {
  font-size: 0.875rem;
  line-height: normal;
  color: var(--gray-600);
}

.carousel-nav__button {
  /* 其他样式保持不变 */
  width: 48px;
  height: 48px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  border: none;
  background-color: var(--secondary);
  color: var(--white);
  transition: background-color 0.3s ease;
}

.testimonial-sec__button-prev {
  margin-right: 20px; /* 增加右侧边距来分开按钮 */
}

.swiper-button-disabled {
  background-color: var(--green-300);
}

.testimonial-sec__footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 2rem;
}

@media screen and (min-width: 992px) {
  .testimonial-sec {
    padding-top: 4rem;
    padding-bottom: 4rem;
  }

  .testimonial-sec__title {
    font-size: 2.5rem;
  }

  .testimonial__quote {
    font-size: 1.25rem;
  }

  .testimonial__name {
    font-size: 1.25rem;
  }
}
</style>
