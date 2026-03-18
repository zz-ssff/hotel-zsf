<template>
  <div class="home">
    <el-container>
      <AppHeader title="酒店PMS系统" />

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
          <h2>欢迎使用酒店PMS系统</h2>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>今日入住</span>
                  </div>
                </template>
                <div class="card-content">
                  <el-statistic :value="todayCheckIn" />
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>今日退房</span>
                  </div>
                </template>
                <div class="card-content">
                  <el-statistic :value="todayCheckOut" />
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>当前房态</span>
                  </div>
                </template>
                <div class="card-content">
                  <el-statistic :value="currentOccupancy" suffix="%" />
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>今日营收</span>
                  </div>
                </template>
                <div class="card-content">
                  <el-statistic :value="todayRevenue" prefix="￥" />
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 房间动态信息 -->
          <el-card shadow="hover" style="margin-top: 20px;">
            <template #header>
              <div class="card-header">
                <span>房间动态信息</span>
              </div>
            </template>
            <div class="room-status-grid">
              <div 
                v-for="room in rooms" 
                :key="room.id"
                class="room-status-item"
                :class="`room-status-${room.status}`"
              >
                <div class="room-number">{{ room.roomNumber }}</div>
                <div class="room-info">
                  <div class="room-type">{{ getRoomTypeText(room.roomType) }}</div>
                  <div class="room-status">{{ getStatusText(room.status) }}</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { HomeFilled, House, Ticket, UserFilled, Goods, Star, DataAnalysis, User } from '@element-plus/icons-vue'
import AppHeader from '../components/Header.vue'
import { reportApi, roomApi } from '../api'

export default {
  name: 'Home',
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
      activeIndex: '/',
      user: JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } },
      todayCheckIn: 0,
      todayCheckOut: 0,
      currentOccupancy: 0,
      todayRevenue: 0,
      rooms: []
    }
  },
  mounted() {
    this.fetchKeyIndicators()
    this.fetchRooms()
  },
  methods: {
    async fetchKeyIndicators() {
      try {
        const response = await reportApi.keyIndicators()
        const data = response.data
        this.todayCheckIn = data.todayCheckIn
        this.todayCheckOut = data.todayCheckOut
        this.currentOccupancy = data.currentOccupancy
        this.todayRevenue = data.todayRevenue
      } catch (error) {
        console.error('获取关键指标失败:', error)
      }
    },
    async fetchRooms() {
      try {
        const response = await roomApi.list()
        this.rooms = response.data
      } catch (error) {
        console.error('获取房间列表失败:', error)
      }
    },
    getRoomTypeText(type) {
      const types = {
        single: '单人间',
        double: '双人间',
        luxury: '豪华间',
        suite: '套房'
      }
      return types[type] || type
    },
    getStatusText(status) {
      const statuses = {
        1: '空闲',
        2: '已预订',
        3: '已入住',
        4: '打扫中'
      }
      return statuses[status] || status
    }
  }
}</script>

<style scoped>
.home {
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

.card-content {
  text-align: center;
  padding: 20px 0;
}

/* 房间状态网格 */
.room-status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 10px;
  margin-top: 10px;
}

.room-status-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
}

.room-status-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.room-number {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.room-info {
  text-align: center;
  font-size: 12px;
}

.room-type {
  margin-bottom: 2px;
}

.room-status {
  font-weight: 500;
}

/* 房间状态颜色 */
.room-status-1 {
  background-color: #f0f9eb;
  border-color: #c2e7b0;
}

.room-status-2 {
  background-color: #ecf5ff;
  border-color: #adc6ff;
}

.room-status-3 {
  background-color: #fef0f0;
  border-color: #ffccc7;
}

.room-status-4 {
  background-color: #fdf6ec;
  border-color: #ffd591;
}
</style>