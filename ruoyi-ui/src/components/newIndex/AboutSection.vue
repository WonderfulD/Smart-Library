<template>
  <section class="about-sec">
    <base-container>
    <div class="about-card">
      <div class="row">
        <div class="col--12 col--lg-6">
          <figure class="about-card__figure">
            <img src="../../assets/index/about-one.png" alt="people talking">
          </figure>
        </div>
        <div class="col--12 col--lg-6">
          <div class="about-card__text">
            <h2 class="about-card__title">每本书，每一刻，现在触手可及</h2>
            <p class="about-card__desc">
              平台通过技术创新，集成多家线下图书馆资源，致力于解决传统图书馆服务中存在的访问限制、资源分散和流程繁琐等问题。
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="about-card">
      <div class="row">
        <div class="col--12 col--lg-6">
          <div class="about-card__text">
            <h2 class="about-card__title">书随需动，架不生尘</h2>
            <p class="about-card__desc">
              得益于最新的数据可视化和机器学习技术，本平台得以提供高效的信息管理和智能化的决策辅助，通过鼓励用户通过满意度调查和评分机制参与服务和资源的评价，使得本平台在提供科学管理支持和优化用户体验方面遥遥领先
            </p>
          </div>
        </div>
        <div class="col--12 col--lg-6">
          <figure class="about-card__figure">
            <img src="../../assets/index/about-two.png" alt="people climbing">
          </figure>
        </div>
      </div>
    </div>
    </base-container>
  </section>
</template>

<style scoped>
.about-sec {
  padding-top: 2rem;
  padding-bottom: 1rem;
}

.about-card {
  margin-bottom: 3rem;
}

.about-card__text {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.about-card__title {
  text-align: left;
  font-size: 2rem;
  color: var(--blue-900);
  margin-bottom: 2rem;
}

.about-card__desc {
  font-size: 1rem;
  line-height: 200%;
  color: var(--gray-700);
}

.about-card__figure {
  margin-bottom: 1.5rem;
}

@media screen and (min-width: 992px) {
  .about-card__figure {
    margin-bottom: 0;
  }

  .about-card__title {
    font-size: 2.5rem;
  }

  .about-card__desc {
    font-size: 1.25rem;
  }
}
</style>
