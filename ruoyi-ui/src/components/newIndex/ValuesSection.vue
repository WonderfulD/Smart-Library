<template>
  <section class="values-sec">
    <base-container>
      <header class="values-sec__header">
        <h2 class="values-sec__title">阅读至简，管理至精</h2>
        <p class="values-sec__desc">
          EASY To Read
          <br>
          EASY To Manage
        </p>
      </header>
      <div class="values-sec__cards">
        <div class="row">
          <div class="col--12 col--lg-4">
            <article class="value-card">
              <div class="value-card__icon">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#star"></use>
                </svg>
              </div>
              <h3 class="value-card__title">自在阅读，随心所欲</h3>
              <p class="value-card__desc">
                十余家图书馆，百余类图书，千万册藏书尽在指尖
              </p>
            </article>
          </div>
          <div class="col--12 col--lg-4">
            <article class="value-card">
              <div class="value-card__icon">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#lightning"></use>
                </svg>
              </div>
              <h3 class="value-card__title">随需应变，灵活高效</h3>
              <p class="value-card__desc">
                自助延长归还日期，任意选择借归方式，科学调整藏书比例...都交给平台！
              </p>
            </article>
          </div>
          <div class="col--12 col--lg-4">
            <article class="value-card">
              <div class="value-card__icon">
                <svg width="48" height="48">
                  <use href="../../assets/index/icons.svg#fire"></use>
                </svg>
              </div>
              <h3 class="value-card__title">畅通无阻，即刻对话</h3>
              <p class="value-card__desc">
                各大图书馆信息随意查阅，图书馆和读者之间的交流从来没有像现在这样便利
              </p>
            </article>
          </div>
        </div>
      </div>
    </base-container>
  </section>
</template>

<style scoped>
.values-sec {
  padding-bottom: 1rem;
}

.values-sec__header {
  background-color: var(--primary);
  padding: 5rem 1rem 14rem;
  color: var(--white);
  text-align: center;
  margin-right: -15px;
  margin-left: -15px;
}

.values-sec__title {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.values-sec__desc {
  font-size: 1rem;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.values-sec__cards {
  margin-top: -10rem;
  padding-right: 1rem;
  padding-left: 1rem;
}

.value-card {
  padding: 3rem 2rem;
  border-radius: 1rem;
  background-color: var(--white);
  text-align: center;
  position: relative;
  overflow: hidden;
  margin-bottom: 1rem;
}

.value-card__title {
  font-size: 1.125rem;
  line-height: 1.2;
  margin-bottom: 1rem;
  color: var(--blue-900);
}

.value-card__desc {
  font-size: 1rem;
  line-height: 1.5;
  color: var(--gray-700);
}

.value-card__icon {
  margin-bottom: 1.5rem;
  color: var(--secondary);
}

@media screen and (min-width: 992px) {
  .values-sec {
    padding-top: 4rem;
    padding-bottom: 3rem;
  }

  .values-sec__header {
    border-radius: 2rem;
    padding: 5rem 1rem 14rem;
    margin: 0;
  }

  .values-sec__title {
    font-size: 3.5rem;
  }

  .values-sec__desc {
    font-size: 1.25rem;
  }

  .values-sec__cards {
    padding-right: 5rem;
    padding-left: 5rem;
  }
}
</style>
