<template>
  <section class="services-sec">
    <div class="services-sec__inner">
      <base-container>
        <h2 class="services-sec__title">为什么选择智慧图书馆？</h2>
        <div class="row">
          <div class="col--12 col--lg-6">
            <article class="service-card">
              <div class="service-card__icon service-card__icon--green">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#rocket"></use>
                </svg>
              </div>
              <h3 class="service-card__title">强大的搜索引擎</h3>
              <p class="service-card__desc">
                不同类别、不同出版社、不同版次...不论需要什么，眨眼间送上
              </p>
            </article>
          </div>
          <div class="col--12 col--lg-6">
            <article class="service-card">
              <div class="service-card__icon service-card__icon--red">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#chart-one"></use>
                </svg>
              </div>
              <h3 class="service-card__title">动态的内容优化</h3>
              <p class="service-card__desc">
                每分每秒都在变得更好，这归功于海量用户的满意度调查和评分机制
              </p>
            </article>
          </div>
          <div class="col--12 col--lg-6">
            <article class="service-card">
              <div class="service-card__icon service-card__icon--blue">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#chart-two"></use>
                </svg>
              </div>
              <h3 class="service-card__title">精准的决策分析</h3>
              <p class="service-card__desc">
                多种多样的数据可视化图表助力图书馆决策分析，还有强大的机器学习预测模型，如虎添翼
              </p>
            </article>
          </div>
          <div class="col--12 col--lg-6">
            <article class="service-card">
              <div class="service-card__icon service-card__icon--orange">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#gear"></use>
                </svg>
              </div>
              <h3 class="service-card__title">可靠的系统架构</h3>
              <p class="service-card__desc">
                采用最流行、最可靠的技术框架搭建而成的网站，流畅性、稳定性超乎想象！
              </p>
            </article>
          </div>
        </div>
      </base-container>
    </div>
  </section>
</template>

<style scoped>
.services-sec {
  margin-top: 2rem;
  margin-bottom: 2rem;
  background-color: var(--white);
}

.services-sec__inner {
  padding-top: 3rem;
  padding-bottom: 2rem;
}

.services-sec::before,
.services-sec::after {
  content: "";
  display: block;
  height: 3rem;
  width: 100%;
}

.services-sec::before {
  top: 0;
  background: url("../../assets/index/curve-gray.svg") center bottom/cover no-repeat;
}

.services-sec::after {
  bottom: 0;
  background: url("../../assets/index/curve-gray.svg") center bottom/cover no-repeat;
  transform: rotate(180deg);
}

.services-sec__title {
  text-align: center;
  font-size: 2rem;
  line-height: 1.5;
  color: var(--blue-900);
  margin-bottom: 3.5rem;
}

.service-card {
  padding: 3rem 2rem;
  border-radius: 1rem;
  background-color: var(--gray);
  text-align: left;
  position: relative;
  overflow: hidden;
  margin-bottom: 1.5rem;
}

.service-card__title {
  font-size: 1.125rem;
  line-height: 1.2;
  margin-bottom: 1rem;
  color: var(--blue-800);
}

.service-card__desc {
  font-size: 1rem;
  line-height: 1.5;
  color: var(--gray-700);
}

.service-card__icon {
  margin-bottom: 1.5rem;
  color: var(--blue-500);
}

@media screen and (min-width: 992px) {
  .services-sec {
    margin-top: 4rem;
    margin-bottom: 4rem;
  }

  .services-sec__inner {
    padding-top: 6rem;
    padding-bottom: 5rem;
  }

  .services-sec::before,
  .services-sec::after {
    height: 5rem;
  }

  .services-sec__title {
    font-size: 2.5rem;
  }

  .service-card__title {
    font-size: 1.5rem;
  }

  .service-card__desc {
    font-size: 1.125rem;
  }
}
</style>
