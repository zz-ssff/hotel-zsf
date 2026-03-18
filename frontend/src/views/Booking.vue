<template>
  <div class="booking">
    <el-container>
      <AppHeader title="酒店PMS系统 - 预订管理" />

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
          <h2>预订管理</h2>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>创建预订</span>
                  </div>
                </template>
                <el-form :model="bookingForm" label-width="80px">
                  <el-form-item label="房间">
                    <el-select v-model="bookingForm.roomId" placeholder="选择房间">
                      <el-option
                        v-for="room in roomList"
                        :key="room.id"
                        :label="room.roomNumber + ' - ' + getRoomTypeText(room.roomType)"
                        :value="room.id"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="客户">
                    <el-select v-model="bookingForm.customerId" placeholder="选择客户">
                      <el-option
                        v-for="customer in customerList"
                        :key="customer.id"
                        :label="customer.name + ' - ' + customer.phone"
                        :value="customer.id"
                      />
                    </el-select>
                    <el-button type="primary" plain @click="showAddCustomerDialog = true" style="margin-left: 10px;">新增客户</el-button>
                  </el-form-item>
                  <el-form-item label="入住日期">
                    <el-date-picker
                      v-model="bookingForm.checkInDate"
                      type="datetime"
                      placeholder="选择入住日期"
                      style="width: 100%"
                    />
                  </el-form-item>
                  <el-form-item label="退房日期">
                    <el-date-picker
                      v-model="bookingForm.checkOutDate"
                      type="datetime"
                      placeholder="选择退房日期"
                      style="width: 100%"
                    />
                  </el-form-item>
                  <el-form-item label="天数">
                    <el-input v-model.number="bookingForm.days" placeholder="入住天数" />
                  </el-form-item>
                  <el-form-item label="总价">
                    <el-input v-model.number="bookingForm.totalPrice" placeholder="总价" />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="createBooking">创建预订</el-button>
                    <el-button @click="resetForm">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>预订列表</span>
                  </div>
                </template>
                <el-table :data="bookingList" style="width: 100%">
                  <el-table-column prop="orderNo" label="订单号" width="180" />
                  <el-table-column prop="roomId" label="房间" width="120">
                    <template #default="scope">
                      {{ getRoomNumber(scope.row.roomId) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="customerId" label="客户" width="150">
                    <template #default="scope">
                      {{ getCustomerName(scope.row.customerId) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="checkInDate" label="入住日期" width="180" />
                  <el-table-column prop="checkOutDate" label="退房日期" width="180" />
                  <el-table-column prop="totalPrice" label="总价" width="100" />
                  <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                      <el-tag :type="getStatusType(scope.row.status)">
                        {{ getStatusText(scope.row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="180">
                    <template #default="scope">
                      <el-button v-if="scope.row.status === 'pending'" type="primary" size="small" @click="confirmBooking(scope.row.id)">确认</el-button>
                      <el-button v-if="scope.row.status === 'pending' || scope.row.status === 'confirmed'" type="danger" size="small" @click="cancelBooking(scope.row.id)">取消</el-button>
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

    <!-- 新增客户对话框 -->
    <el-dialog
      v-model="showAddCustomerDialog"
      title="新增客户"
      width="500px"
    >
      <el-form :model="customerForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="customerForm.name" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="customerForm.phone" placeholder="请输入客户电话" />
        </el-form-item>
        <el-form-item label="身份证">
          <el-input v-model="customerForm.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="customerForm.address" placeholder="请输入客户地址" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddCustomerDialog = false">取消</el-button>
          <el-button type="primary" @click="addCustomer">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { HomeFilled, House, Ticket, UserFilled, Goods, Star, DataAnalysis, User } from '@element-plus/icons-vue'
import { roomApi, customerApi, bookingApi, orderApi } from '../api'
import AppHeader from '../components/Header.vue'

export default {
  name: 'Booking',
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
      activeIndex: '/booking',
      user: JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } },
      bookingForm: {
        roomId: '',
        customerId: '',
        checkInDate: '',
        checkOutDate: '',
        days: 1,
        totalPrice: 0
      },
      roomList: [],
      customerList: [],
      bookingList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      showAddCustomerDialog: false,
      customerForm: {
        name: '',
        phone: '',
        idCard: '',
        address: ''
      }
    }
  },
  mounted() {
    this.loadRooms()
    this.loadCustomers()
    this.loadBookings()
  },
  watch: {
    'bookingForm.checkInDate': function() {
      this.calculateDays()
      this.calculateTotalPrice()
    },
    'bookingForm.checkOutDate': function() {
      this.calculateDays()
      this.calculateTotalPrice()
    },
    'bookingForm.roomId': function() {
      this.calculateTotalPrice()
    },
    'bookingForm.days': function() {
      this.calculateTotalPrice()
    }
  },
  methods: {
    async loadRooms() {
      try {
        const response = await roomApi.list()
        if (response.status === 200) {
          this.roomList = response.data || []
        }
      } catch (error) {
        console.error('加载房间失败:', error)
      }
    },
    async loadCustomers() {
      try {
        const response = await customerApi.list()
        if (response.status === 200) {
          this.customerList = response.data || []
        }
      } catch (error) {
        console.error('加载客户失败:', error)
      }
    },
    async loadBookings() {
      try {
        const response = await orderApi.list()
        if (response.status === 200) {
          this.bookingList = response.data || []
          this.total = response.data ? response.data.length : 0
        }
      } catch (error) {
        console.error('加载预订失败:', error)
      }
    },
    async createBooking() {
      try {
        // 转换数据类型和日期格式
        const formatDate = (date) => {
          if (!date) return ''
          const d = new Date(date)
          const year = d.getFullYear()
          const month = String(d.getMonth() + 1).padStart(2, '0')
          const day = String(d.getDate()).padStart(2, '0')
          return `${year}-${month}-${day}`
        }
        
        const bookingData = {
          ...this.bookingForm,
          roomId: parseInt(this.bookingForm.roomId),
          customerId: parseInt(this.bookingForm.customerId),
          checkInDate: formatDate(this.bookingForm.checkInDate),
          checkOutDate: formatDate(this.bookingForm.checkOutDate),
          days: parseInt(this.bookingForm.days),
          totalPrice: parseFloat(this.bookingForm.totalPrice)
        }
        const response = await bookingApi.create(bookingData)
        if (response.status === 200) {
          this.$message.success('预订创建成功')
          this.resetForm()
          this.loadBookings()
        } else {
          this.$message.error(response.message || '预订创建失败')
        }
      } catch (error) {
        console.error('创建预订失败:', error)
        this.$message.error('预订创建失败')
      }
    },
    async confirmBooking(id) {
      try {
        const response = await bookingApi.confirm(id)
        if (response.status === 200) {
          this.$message.success('预订确认成功')
          this.loadBookings()
        } else {
          this.$message.error(response.message || '预订确认失败')
        }
      } catch (error) {
        console.error('确认预订失败:', error)
        this.$message.error('预订确认失败')
      }
    },
    async cancelBooking(id) {
      try {
        const response = await bookingApi.cancel(id)
        if (response.status === 200) {
          this.$message.success('预订取消成功')
          this.loadBookings()
        } else {
          this.$message.error(response.message || '预订取消失败')
        }
      } catch (error) {
        console.error('取消预订失败:', error)
        this.$message.error('预订取消失败')
      }
    },
    resetForm() {
      this.bookingForm = {
        roomId: '',
        customerId: '',
        checkInDate: '',
        checkOutDate: '',
        days: 1,
        totalPrice: 0
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
        pending: '待确认',
        confirmed: '已确认',
        cancelled: '已取消',
        checkin: '已入住',
        completed: '已完成'
      }
      return statuses[status] || status
    },
    getStatusType(status) {
      const types = {
        pending: 'info',
        confirmed: 'success',
        cancelled: 'danger',
        checkin: 'warning',
        completed: 'success'
      }
      return types[status] || ''
    },
    getRoomNumber(roomId) {
      const room = this.roomList.find(r => r.id === roomId)
      return room ? room.roomNumber : roomId
    },
    getCustomerName(customerId) {
      const customer = this.customerList.find(c => c.id === customerId)
      return customer ? customer.name : customerId
    },
    handleSizeChange(size) {
      this.pageSize = size
    },
    handleCurrentChange(current) {
      this.currentPage = current
    },
    async addCustomer() {
      try {
        const response = await customerApi.save(this.customerForm)
        if (response.status === 200) {
          this.$message.success('客户添加成功')
          this.showAddCustomerDialog = false
          this.loadCustomers() // 重新加载客户列表
          // 清空客户表单
          this.customerForm = {
            name: '',
            phone: '',
            idCard: '',
            address: ''
          }
        } else {
          this.$message.error(response.message || '客户添加失败')
        }
      } catch (error) {
        console.error('添加客户失败:', error)
        this.$message.error('客户添加失败')
      }
    },
    calculateDays() {
      if (this.bookingForm.checkInDate && this.bookingForm.checkOutDate) {
        const checkIn = new Date(this.bookingForm.checkInDate)
        const checkOut = new Date(this.bookingForm.checkOutDate)
        const diffTime = Math.abs(checkOut - checkIn)
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
        this.bookingForm.days = diffDays
      }
    },
    calculateTotalPrice() {
      if (this.bookingForm.roomId && this.bookingForm.days) {
        const room = this.roomList.find(r => r.id === this.bookingForm.roomId)
        if (room && room.price) {
          this.bookingForm.totalPrice = room.price * this.bookingForm.days
        }
      }
    }
  }
}
</script>

<style scoped>
.booking {
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