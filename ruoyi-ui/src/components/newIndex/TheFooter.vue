<template>
  <footer class="footer">
    <div class="footer__inner">
      <base-container>

        <ul class="list list--inline socilas">
          <li class="social__item">
            <a class="socila__link" href="">
              <img src="../../assets/index/favicon.svg" width="24" height="24" alt="Icon Description">
            </a>
          </li>
        </ul>
        <p class="copyright">&copy;2024 All rights reserved.</p>
      </base-container>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  position: relative;
  margin-top: -10rem;
}

.footer::before {
  content: "";
  display: block;
  height: 5rem;
  background: url('../../assets/index/curve-white.svg') bottom/cover no-repeat;
  width: 100%;
}

.footer__inner {
  padding-bottom: 1.5rem;
  background-color: var(--white);
}

.footer__nav {
  justify-content: center;
  margin-bottom: 3rem;
}

.nav__link {
  color: var(--blue-700);
  font-size: 1rem;
  padding: 0.5rem;
  text-decoration: none !important;
}

.socilas {
  justify-content: center;
  margin-bottom: 2rem;
}

.social__item {
  margin-right: 0.25rem;
  margin-left: 0.25rem;
}

.socila__link {
  color: var(--primary);
  padding: 0.75rem;
  border: 1px solid var(--primary);
  border-radius: 50%;
  display: flex;
}

.copyright {
  font-size: 0.875rem;
  text-align: center;
  color: var(--primary);
}

@media screen and (min-width: 768px) {
  .footer::before {
    height: 10rem;
  }

  .nav__link {
    font-size: 1.5rem;
    padding: 0.5rem 1rem;
  }

  .socila__link {
    padding: 1rem;
  }

  .socila__link:hover {
    background-color: var(--primary);
    color: var(--white);
    transition: background-color 0.3s ease;
  }

}
</style>
