<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>酒店管理系统</h2>
          <p>用户登录</p>
        </div>
      </template>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loginFormRef = ref(null)
    const loading = ref(false)
    const loginForm = ref({
      username: '',
      password: ''
    })
    const loginRules = ref({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    })

    const handleLogin = async () => {
      if (!loginFormRef.value) return
      await loginFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            const response = await fetch('/api/user/login', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(loginForm.value)
            })
            const data = await response.json()
            if (data.success) {
              localStorage.setItem('user', JSON.stringify(data.user))
              ElMessage.success('登录成功')
              router.push('/')
            } else {
              ElMessage.error(data.message)
            }
          } catch (error) {
            ElMessage.error('登录失败，请检查网络连接')
          } finally {
            loading.value = false
          }
        }
      })
    }

    return {
      loginForm,
      loginRules,
      loginFormRef,
      loading,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  overflow: hidden;
}

.login-header {
  text-align: center;
  padding: 20px 0;
}

.login-header h2 {
  margin: 0 0 10px 0;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-color);
}

.login-header p {
  margin: 0;
  font-size: 16px;
  color: var(--text-color-secondary);
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  margin-top: 20px;
}

.el-form {
  padding: 0 30px 30px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  height: 48px;
}

.el-input__inner {
  font-size: 16px;
  padding: 0 15px;
}

.el-input__prefix-icon {
  font-size: 20px;
  color: var(--text-color-secondary);
}
</style>