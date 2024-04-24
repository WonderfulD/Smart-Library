<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin'

export default {
  name: 'Dashboard',
  components: { adminDashboard},
  data() {
    return {
      currentRole: 'adminDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    // 非超级管理员，非图书馆管理员跳转为editorDashboard
    // console.log(this.roles);
    if (!this.roles.includes('admin') && !this.roles.includes('libraryAdmin')) {
    }
  }
}
</script>
