<template>
  <section class="hero-sec">
    <div class="hero-sec__inner">
      <base-container>
        <div class="row">
          <div class="col--12 col--lg-6">
            <div class="hero-sec__content">
              <h1 class="hero-sec__title">
                随时随地
                借阅无界限
                高效运作
                管理更智能
              </h1>
              <p class="hero-sec__desc">
                智慧图书馆的技术平台提供了从亲临图书馆到邮寄借阅的多种选择，满足了读者们对于高效和灵活借阅解决方案的需求。
                <br>
                图书馆管理员通过平台可以更有效地处理借阅申请，监控藏书状态，利用数据可视化和机器学习技术，优化图书馆的服务和运营。
              </p>
            </div>
          </div>
          <div class="col--12 col--lg-6">
            <img class="d-none d-lg-block" src="../../assets/index/hero.png" alt="people working">
          </div>
        </div>
      </base-container>
    </div>
  </section>
</template>

<style scoped>
.hero-sec {
  padding-top: 4rem;
  color: var(--white);
  position: relative;
  margin-bottom: 1rem;
}

.hero-sec__inner {
  padding-top: 2rem;
  padding-bottom: 2rem;
  background-color: var(--primary);
}

.hero-sec::after {
  content: "";
  display: block;
  height: 8rem;
  background: url('../../assets/index/curve-blue.svg') bottom/cover no-repeat;
}

.hero-sec__content {
  text-align: center;
}

.hero-sec__title {
  font-size: 2rem;
  line-height: 130%;
}

.hero-sec__desc {
  font-size: 1rem;
  line-height: 170%;
  margin-bottom: 2.5rem;
}

@media screen and (min-width: 992px) {
  .hero-sec {
    padding-top: 5rem;
    margin-bottom: 2rem;
  }

  .hero-sec__inner {
    display: flex;
    align-items: center;
    min-height: calc(100vh - 5rem);
  }

  .hero-sec__content {
    text-align: left;
  }

  .hero-sec__title {
    font-size: 3rem;
    line-height: 130%;
    max-width: 29rem;
  }

  .hero-sec__desc {
    text-align: justify;
    font-size: 1.25rem;
  }

  .hero-sec::after {
    height: 13rem;
  }
}
</style>
