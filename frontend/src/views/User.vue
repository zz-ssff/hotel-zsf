<template>
  <div class="user">
    <el-container>
      <AppHeader title="酒店PMS系统 - 用户管理" />

      <el-container>
        <el-aside width="200px">
          <el-menu
            :default-active="activeIndex"
            class="el-menu-vertical-demo"
            router
          >
            <el-menu-item index="/">
              <el-icon><HomeFilled /></el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/room">
              <el-icon><House /></el-icon>
              <span>客房管理</span>
            </el-menu-item>
            <el-menu-item index="/booking">
              <el-icon><Ticket /></el-icon>
              <span>预订管理</span>
            </el-menu-item>
            <el-menu-item index="/checkin">
              <el-icon><UserFilled /></el-icon>
              <span>入住登记</span>
            </el-menu-item>
            <el-menu-item index="/checkout">
              <el-icon><Goods /></el-icon>
              <span>退房结账</span>
            </el-menu-item>
            <el-menu-item index="/member">
              <el-icon><Star /></el-icon>
              <span>会员管理</span>
            </el-menu-item>
            
          </el-menu>
        </el-aside>
        <el-main>
          <h2>用户管理</h2>
          <el-card class="user-card">
            <template #header>
              <div class="user-header">
                <h3>用户列表</h3>
                <el-button type="primary" @click="handleAddUser">添加用户</el-button>
              </div>
            </template>
            <el-table :data="userList" style="width: 100%">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="username" label="用户名" width="120" />
              <el-table-column prop="name" label="姓名" width="120" />
              <el-table-column prop="phone" label="电话" width="150" />
              <el-table-column prop="email" label="邮箱" />
              <el-table-column label="角色" width="100">
                <template #default="scope">
                  {{ scope.row.role ? scope.row.role.description : '无' }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                    {{ scope.row.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button type="primary" size="small" @click="handleEditUser(scope.row)">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDeleteUser(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>

          <!-- 添加/编辑用户对话框 -->
          <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
            <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="userForm.username" placeholder="请输入用户名" />
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
              </el-form-item>
              <el-form-item label="姓名" prop="name">
                <el-input v-model="userForm.name" placeholder="请输入姓名" />
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="userForm.phone" placeholder="请输入电话" />
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="userForm.email" placeholder="请输入邮箱" />
              </el-form-item>
              <el-form-item label="角色" prop="roleId">
                <el-select v-model="userForm.roleId" placeholder="请选择角色">
                  <el-option v-for="role in roleList" :key="role.id" :label="role.description" :value="role.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="状态" prop="status">
                <el-select v-model="userForm.status" placeholder="请选择状态">
                  <el-option label="启用" :value="1" />
                  <el-option label="禁用" :value="0" />
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
              </span>
            </template>
          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { HomeFilled, House, Ticket, UserFilled, Goods, Star, DataAnalysis, User } from '@element-plus/icons-vue'
import AppHeader from '../components/Header.vue'
import { userApi } from '../api'

export default {
  name: 'User',
  components: {
    HomeFilled,
    House,
    Ticket,
    UserFilled,
    Goods,
    Star,
    DataAnalysis,
    User,
    AppHeader
  },
  setup() {
    const activeIndex = ref('/user1')
    const userList = ref([])
    const roleList = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('添加用户')
    const userForm = ref({
      id: null,
      username: '',
      password: '',
      name: '',
      phone: '',
      email: '',
      roleId: '',
      status: 1
    })
    const userRules = ref({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      roleId: [
        { required: true, message: '请选择角色', trigger: 'blur' }
      ],
      status: [
        { required: true, message: '请选择状态', trigger: 'blur' }
      ]
    })
    const userFormRef = ref(null)

    // 获取用户列表
    const getUserList = async () => {
      try {
        console.log('开始获取用户列表')
        const response = await userApi.list()
        console.log('获取用户列表响应:', response)
        if (response && response.status === 200) {
          // 检查 response.data 的结构
          console.log('response.data:', response.data)
          console.log('response.data 类型:', typeof response.data)
          console.log('response.data 是否为数组:', Array.isArray(response.data))
          
          // 尝试不同的数据结构
          let userData = []
          if (Array.isArray(response.data)) {
            userData = response.data
          } else if (response.data && Array.isArray(response.data.data)) {
            userData = response.data.data
          } else if (response.data && response.data.list) {
            userData = response.data.list
          }
          
          // 深拷贝数据，避免循环引用
          userList.value = JSON.parse(JSON.stringify(userData)) || []
          console.log('用户列表数据:', userList.value)
          console.log('用户列表长度:', userList.value.length)
        } else {
          userList.value = []
          console.log('用户列表数据: 空数组')
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
        ElMessage.error('获取用户列表失败')
        userList.value = []
        console.log('用户列表数据: 空数组（错误）')
      }
    }

    // 获取角色列表
    const getRoleList = async () => {
      try {
        console.log('开始获取角色列表')
        const response = await userApi.roles()
        console.log('获取角色列表响应:', response)
        if (response && response.status === 200) {
          // 确保数据是数组
          const data = response.data.data || []
          // 深拷贝数据，避免循环引用
          roleList.value = Array.isArray(data) ? JSON.parse(JSON.stringify(data)) : []
          console.log('角色列表数据:', roleList.value)
        } else {
          roleList.value = []
        }
      } catch (error) {
        console.error('获取角色列表失败:', error)
        ElMessage.error('获取角色列表失败')
        roleList.value = []
      }
    }

    // 添加用户
    const handleAddUser = () => {
      console.log('添加用户')
      dialogTitle.value = '添加用户'
      userForm.value = {
        id: null,
        username: '',
        password: '',
        name: '',
        phone: '',
        email: '',
        roleId: '',
        status: 1
      }
      dialogVisible.value = true
    }

    // 编辑用户
    const handleEditUser = (row) => {
      console.log('编辑用户:', row)
      dialogTitle.value = '编辑用户'
      // 复制行数据到表单
      userForm.value = { ...row }
      // 确保角色ID正确设置
      if (row.role && row.role.id) {
        userForm.value.roleId = row.role.id
      }
      dialogVisible.value = true
    }

    // 删除用户
    const handleDeleteUser = async (id) => {
      try {
        console.log('删除用户:', id)
        const response = await userApi.delete(id)
        console.log('删除用户响应:', response)
        if (response && response.status === 200) {
          ElMessage.success('删除成功')
          getUserList()
        } else if (response && response.message) {
          ElMessage.error(response.message)
        } else {
          ElMessage.error('删除失败')
        }
      } catch (error) {
        console.error('删除用户失败:', error)
        ElMessage.error('删除失败')
      }
    }

    // 提交表单
    const handleSubmit = async () => {
      if (!userFormRef.value) return
      console.log('提交表单:', userForm.value)
      await userFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const response = userForm.value.id ? await userApi.update(userForm.value) : await userApi.save(userForm.value)
            console.log('提交表单响应:', response)
            if (response && response.status === 200) {
              ElMessage.success(userForm.value.id ? '更新成功' : '添加成功')
              dialogVisible.value = false
              getUserList()
            } else if (response && response.message) {
              ElMessage.error(response.message)
            } else {
              ElMessage.error('操作失败')
            }
          } catch (error) {
            console.error('操作失败:', error)
            ElMessage.error('操作失败')
          }
        }
      })
    }

    // 组件挂载时加载数据
    onMounted(() => {
      console.log('组件挂载')
      getUserList()
      getRoleList()
    })

    return {
      activeIndex,
      userList,
      roleList,
      dialogVisible,
      dialogTitle,
      userForm,
      userRules,
      userFormRef,
      getUserList,
      getRoleList,
      handleAddUser,
      handleEditUser,
      handleDeleteUser,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.user {
  min-height: 100vh;
  padding-top: 60px;
}

.el-aside {
  background-color: #f5f7fa;
}

.el-main {
  padding: 20px;
}

.user-card {
  margin-bottom: 20px;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #333333;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>