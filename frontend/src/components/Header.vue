<template>
  <div class="header">
    <div class="header-left">{{ title }}</div>
    <div class="header-right">
      <span class="user-info">{{ user.name }}</span>
      <el-button type="primary" plain @click="handleLogout">退出登录</el-button>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'AppHeader',
  props: {
    title: {
      type: String,
      default: '酒店PMS系统'
    }
  },
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user')) || { name: '', role: { roleName: '' }, roleName: '' }
    }
  },
  methods: {
    handleLogout() {
      localStorage.removeItem('user')
      ElMessage.success('退出登录成功')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.header {
  background-color: #409EFF;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  font-size: 20px;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: 60px;
  line-height: 60px;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-left {
  flex: 1;
  text-align: left;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  font-size: 14px;
  font-weight: normal;
}
</style>