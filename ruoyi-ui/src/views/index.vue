<template>
  <div>
    <div v-if="loading" class="loader">
      <div class="dot"></div>
      <div class="dot"></div>
      <div class="dot"></div>
      <div class="dot"></div>
      <div class="dot"></div>
      <div class="dot"></div>
      <div class="dot"></div>
      <div class="dot"></div>
    </div>
    <iframe  src="/dist/index.html" style="width:100%; height:100vh; border:none;"></iframe>
  </div>
</template>


<script>
export default {
  data() {
    return {
      loading: true // 初始加载状态为 true
    };
  },
  mounted() {
    window.addEventListener('message', this.handleFrameTasks);
  },
  beforeDestroy() {
    window.removeEventListener('message', this.handleFrameTasks);
  },
  methods: {
    handleFrameTasks(event) {
      console.log(event);
      if (event.data.status === 'loaded') {
        this.loading = false;
      }
    }
  }
}
</script>


<style scoped>
.loader {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80px;
  height: 80px;
}

.loader .dot {
  position: absolute;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: #3498db;
  transform-origin: 40px 40px;
  animation: dot 2.0s infinite ease-in-out;
}

.loader .dot:nth-child(1) {
  top: 32px;
  left: 64px;
  animation-delay: -0.5s;
}

.loader .dot:nth-child(2) {
  top: 18px;
  left: 57px;
  animation-delay: -0.4s;
}

.loader .dot:nth-child(3) {
  top: 9px;
  left: 41px;
  animation-delay: -0.3s;
}

.loader .dot:nth-child(4) {
  top: 18px;
  left: 25px;
  animation-delay: -0.2s;
}

.loader .dot:nth-child(5) {
  top: 32px;
  left: 18px;
  animation-delay: -0.1s;
}

.loader .dot:nth-child(6) {
  top: 49px;
  left: 25px;
  animation-delay: 0s;
}

.loader .dot:nth-child(7) {
  top: 60px;
  left: 41px;
  animation-delay: 0.1s;
}

.loader .dot:nth-child(8) {
  top: 49px;
  left: 57px;
  animation-delay: 0.2s;
}

@keyframes dot {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1.0);
  }
}
</style>

