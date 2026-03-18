<template>
  <div class="member">
    <el-container>
      <AppHeader title="酒店PMS系统 - 会员管理" />

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
            <el-menu-item index="/report">
              <el-icon><DataAnalysis /></el-icon>
              <span>数据报表</span>
            </el-menu-item>
            <el-menu-item index="/user" v-if="user.role.roleName === 'admin'">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <h2>会员管理</h2>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>添加会员</span>
                  </div>
                </template>
                <el-form :model="memberForm" label-width="80px">
                  <el-form-item label="会员号">
                    <el-input v-model="memberForm.memberId" placeholder="会员号" />
                  </el-form-item>
                  <el-form-item label="姓名">
                    <el-input v-model="memberForm.name" placeholder="姓名" />
                  </el-form-item>
                  <el-form-item label="电话">
                    <el-input v-model="memberForm.phone" placeholder="联系电话" />
                  </el-form-item>
                  <el-form-item label="邮箱">
                    <el-input v-model="memberForm.email" placeholder="邮箱" />
                  </el-form-item>
                  <el-form-item label="会员等级">
                    <el-select v-model="memberForm.level" placeholder="选择会员等级">
                      <el-option label="普通" value="普通" />
                      <el-option label="银卡" value="银卡" />
                      <el-option label="金卡" value="金卡" />
                      <el-option label="钻石卡" value="钻石卡" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="积分">
                    <el-input v-model.number="memberForm.points" placeholder="积分" />
                  </el-form-item>
                  <el-form-item label="加入日期">
                    <el-date-picker
                      v-model="memberForm.joinDate"
                      type="date"
                      placeholder="选择加入日期"
                      style="width: 100%"
                    />
                  </el-form-item>
                  <el-form-item label="过期日期">
                    <el-date-picker
                      v-model="memberForm.expireDate"
                      type="date"
                      placeholder="选择过期日期"
                      style="width: 100%"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="saveMember">保存会员</el-button>
                    <el-button @click="resetForm">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>会员列表</span>
                  </div>
                </template>
                <el-table :data="memberList" style="width: 100%">
                  <el-table-column prop="memberId" label="会员号" width="150" />
                  <el-table-column prop="name" label="姓名" width="100" />
                  <el-table-column prop="phone" label="电话" width="150" />
                  <el-table-column prop="email" label="邮箱" width="200" />
                  <el-table-column prop="level" label="会员等级" width="100">
                    <template #default="scope">
                      <el-tag :type="getLevelType(scope.row.level)">
                        {{ scope.row.level }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="points" label="积分" width="100" />
                  <el-table-column prop="joinDate" label="加入日期" width="150" />
                  <el-table-column prop="expireDate" label="过期日期" width="150" />
                  <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                      <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
                        {{ scope.row.status === 'active' ? '活跃' : '过期' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="180">
                    <template #default="scope">
                      <el-button type="primary" size="small" @click="editMember(scope.row)">编辑</el-button>
                      <el-button type="danger" size="small" @click="deleteMember(scope.row.id)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="pagination">
                  <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    prev-text="上一页"
                    next-text="下一页"
                    jumper-text="前往"
                    page-sizes-text="每页条数"
                    total-text="共 {total} 条"
                  />
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { HomeFilled, House, Ticket, UserFilled, Goods, Star, DataAnalysis, User } from '@element-plus/icons-vue'
import { memberApi } from '../api'
import AppHeader from '../components/Header.vue'

export default {
  name: 'Member',
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
  data() {
    return {
      activeIndex: '/member',
      user: JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } },
      memberForm: {
        memberId: '',
        name: '',
        phone: '',
        email: '',
        level: '普通',
        points: 0,
        joinDate: '',
        expireDate: '',
        status: 'active'
      },
      memberList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  mounted() {
    this.loadMembers()
  },
  methods: {
    async loadMembers() {
      try {
        const response = await memberApi.list()
        if (response.status === 200) {
          this.memberList = response.data || []
          this.total = response.data ? response.data.length : 0
        }
      } catch (error) {
        console.error('加载会员失败:', error)
      }
    },
    async saveMember() {
      try {
        let response
        if (this.memberForm.id) {
          response = await memberApi.update(this.memberForm)
        } else {
          response = await memberApi.save(this.memberForm)
        }
        if (response.status === 200) {
          this.$message.success(this.memberForm.id ? '会员更新成功' : '会员添加成功')
          this.resetForm()
          this.loadMembers()
        } else {
          this.$message.error(response.message || '保存会员失败')
        }
      } catch (error) {
        console.error('保存会员失败:', error)
        this.$message.error('保存会员失败')
      }
    },
    async deleteMember(id) {
      try {
        const response = await memberApi.delete(id)
        if (response.status === 200) {
          this.$message.success('会员删除成功')
          this.loadMembers()
        } else {
          this.$message.error(response.message || '删除会员失败')
        }
      } catch (error) {
        console.error('删除会员失败:', error)
        this.$message.error('删除会员失败')
      }
    },
    editMember(member) {
      this.memberForm = { ...member }
    },
    resetForm() {
      this.memberForm = {
        memberId: '',
        name: '',
        phone: '',
        email: '',
        level: '普通',
        points: 0,
        joinDate: '',
        expireDate: '',
        status: 'active'
      }
    },
    getLevelType(level) {
      const types = {
        '普通': 'info',
        '银卡': 'primary',
        '金卡': 'warning',
        '钻石卡': 'danger'
      }
      return types[level] || 'info'
    },
    handleSizeChange(size) {
      this.pageSize = size
    },
    handleCurrentChange(current) {
      this.currentPage = current
    }
  }
}
</script>

<style scoped>
.member {
  min-height: 100vh;
  padding-top: 60px;
}

.el-aside {
  background-color: #f5f7fa;
}

.el-main {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>