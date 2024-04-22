<template>
  <div>
    <the-header></the-header>
    <hero-section></hero-section>
    <about-section></about-section>
    <values-section></values-section>
    <services-section></services-section>
    <testimonials-section></testimonials-section>
    <newsletter-section></newsletter-section>
    <the-footer></the-footer>
    <theme-picker />
  </div>
</template>

<script>
import TheHeader from '../components/newIndex/TheHeader.vue';
import HeroSection from '../components/newIndex/HeroSection.vue';
import AboutSection from '../components/newIndex/AboutSection.vue';
import ValuesSection from "../components/newIndex/ValuesSection.vue";
import ServicesSection from "../components/newIndex/ServicesSection.vue";
import TestimonialsSection from "../components/newIndex/TestimonialsSection.vue";
import NewsletterSection from "../components/newIndex/NewsletterSection.vue";
import TheFooter from '../components/newIndex/TheFooter';
import ThemePicker from "../components/ThemePicker";

export default {
  name: "Home",
  components: {
    NewsletterSection,
    TestimonialsSection,
    ServicesSection,
    ValuesSection,
    TheHeader, HeroSection, AboutSection, TheFooter, ThemePicker
  }
};
</script>

<style>
@import '../indexStyle/normalize.css';
@import '../indexStyle/global.css';
</style>
