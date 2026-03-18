<template>
  <div class="checkin">
    <el-container>
      <AppHeader title="酒店PMS系统 - 入住登记" />

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
          <h2>入住登记</h2>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>办理入住</span>
                  </div>
                </template>
                <el-form :model="checkInForm" label-width="80px">
                  <el-form-item label="入住方式">
                    <el-radio-group v-model="checkInForm.mode">
                      <el-radio label="withBooking">有预定</el-radio>
                      <el-radio label="withoutBooking">无预定</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  
                  <!-- 有预定入住 -->
                  <el-form-item v-if="checkInForm.mode === 'withBooking'" label="选择订单">
                    <el-select v-model="checkInForm.orderId" placeholder="选择待入住订单">
                      <el-option
                        v-for="order in pendingOrders"
                        :key="order.id"
                        :label="order.orderNo + ' - ' + getCustomerName(order.customerId)"
                        :value="order.id"
                      />
                    </el-select>
                  </el-form-item>
                  
                  <!-- 无预定入住 -->
                  <div v-else>
                    <el-form-item label="选择房间">
                      <el-select v-model="checkInForm.roomId" placeholder="选择可用房间">
                        <el-option
                          v-for="room in availableRooms"
                          :key="room.id"
                          :label="room.roomNumber + ' - ' + getRoomTypeText(room.roomType)"
                          :value="room.id"
                        />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="客户姓名">
                      <el-input v-model="checkInForm.customerName" placeholder="请输入客户姓名" />
                    </el-form-item>
                    <el-form-item label="身份证号">
                      <el-input v-model="checkInForm.idCard" placeholder="请输入身份证号" />
                    </el-form-item>
                    <el-form-item label="联系电话">
                      <el-input v-model="checkInForm.phone" placeholder="请输入联系电话" />
                    </el-form-item>
                    <el-form-item label="入住日期">
                      <el-date-picker
                        v-model="checkInForm.checkInDate"
                        type="datetime"
                        placeholder="选择入住日期"
                        style="width: 100%"
                      />
                    </el-form-item>
                    <el-form-item label="退房日期">
                      <el-date-picker
                        v-model="checkInForm.checkOutDate"
                        type="datetime"
                        placeholder="选择退房日期"
                        style="width: 100%"
                      />
                    </el-form-item>
                    <el-form-item label="天数">
                      <el-input v-model.number="checkInForm.days" placeholder="入住天数" />
                    </el-form-item>
                    <el-form-item label="总价">
                      <el-input v-model.number="checkInForm.totalPrice" placeholder="总价" />
                    </el-form-item>
                  </div>
                  
                  <el-form-item>
                    <el-button type="info" @click="readIdCard">读取身份证</el-button>
                    <el-button type="primary" @click="processCheckIn">办理入住</el-button>
                    <el-button @click="resetForm">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
              <el-card shadow="hover" style="margin-top: 20px">
                <template #header>
                  <div class="card-header">
                    <span>续住/换房</span>
                  </div>
                </template>
                <el-form :model="extendForm" label-width="80px">
                  <el-form-item label="选择订单">
                    <el-select v-model="extendForm.orderId" placeholder="选择已入住订单">
                      <el-option
                        v-for="order in checkedInOrders"
                        :key="order.id"
                        :label="order.orderNo + ' - ' + getCustomerName(order.customerId)"
                        :value="order.id"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="续住天数">
                    <el-input v-model.number="extendForm.additionalDays" placeholder="续住天数" />
                  </el-form-item>
                  <el-form-item label="换房">
                    <el-select v-model="extendForm.newRoomId" placeholder="选择新房间">
                      <el-option
                        v-for="room in availableRooms"
                        :key="room.id"
                        :label="room.roomNumber + ' - ' + getRoomTypeText(room.roomType)"
                        :value="room.id"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="extendStay">办理续住</el-button>
                    <el-button type="success" @click="changeRoom">办理换房</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>入住列表</span>
                  </div>
                </template>
                <el-table :data="checkedInOrders" style="width: 100%">
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
                  <el-table-column label="操作" width="180">
                    <template #default="scope">
                      <el-button type="primary" size="small" @click="handleExtend(scope.row.id)">续住</el-button>
                      <el-button type="success" size="small" @click="handleChangeRoom(scope.row.id)">换房</el-button>
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
import { roomApi, customerApi, orderApi, checkInApi, hardwareApi, bookingApi } from '../api'
import AppHeader from '../components/Header.vue'

export default {
  name: 'CheckIn',
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
      activeIndex: '/checkin',
      user: JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } },
      checkInForm: {
        mode: 'withBooking', // withBooking 或 withoutBooking
        orderId: '',
        roomId: '',
        customerName: '',
        idCard: '',
        phone: '',
        checkInDate: '',
        checkOutDate: '',
        days: 1,
        totalPrice: 0
      },
      extendForm: {
        orderId: '',
        additionalDays: 1,
        newRoomId: ''
      },
      roomList: [],
      customerList: [],
      orderList: [],
      pendingOrders: [],
      checkedInOrders: [],
      availableRooms: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  mounted() {
    this.loadData()
  },
  watch: {
    'checkInForm.checkInDate': function() {
      this.calculateDays()
      this.calculateTotalPrice()
    },
    'checkInForm.checkOutDate': function() {
      this.calculateDays()
      this.calculateTotalPrice()
    },
    'checkInForm.roomId': function() {
      this.calculateTotalPrice()
    },
    'checkInForm.days': function() {
      this.calculateTotalPrice()
    }
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadRooms(),
        this.loadCustomers(),
        this.loadOrders()
      ])
      this.filterOrders()
      this.filterAvailableRooms()
    },
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
    async loadOrders() {
      try {
        const response = await orderApi.list()
        if (response.status === 200) {
          this.orderList = response.data || []
        }
      } catch (error) {
        console.error('加载订单失败:', error)
      }
    },
    filterOrders() {
      this.pendingOrders = this.orderList.filter(order => 
        order.status === 'confirmed' || order.status === 'pending'
      )
      this.checkedInOrders = this.orderList.filter(order => 
        order.status === 'checkin'
      )
      this.total = this.checkedInOrders.length
    },
    filterAvailableRooms() {
      this.availableRooms = this.roomList.filter(room => 
        room.status === 1
      )
    },
    async readIdCard() {
      try {
        const response = await hardwareApi.readIdCard()
        if (response.status === 200) {
          const idCardInfo = response.data
          this.checkInForm.idCard = idCardInfo.idCard
          // 自动填充客户信息
          const customer = this.customerList.find(c => c.idCard === idCardInfo.idCard)
          if (customer) {
            this.checkInForm.phone = customer.phone
          }
          this.$message.success('身份证读取成功')
        } else {
          this.$message.error(response.message || '身份证读取失败')
        }
      } catch (error) {
        console.error('读取身份证失败:', error)
        this.$message.error('读取身份证失败')
      }
    },
    async processCheckIn() {
      if (this.checkInForm.mode === 'withBooking') {
        if (!this.checkInForm.orderId) {
          this.$message.error('请选择订单')
          return
        }
        await this.processWithBooking()
      } else {
        if (!this.checkInForm.roomId || !this.checkInForm.customerName || !this.checkInForm.idCard || !this.checkInForm.phone) {
          this.$message.error('请填写完整的客户信息')
          return
        }
        await this.processWithoutBooking()
      }
    },
    async processWithBooking() {
      try {
        const response = await checkInApi.process(this.checkInForm.orderId)
        if (response.status === 200) {
          // 制发门卡
          const order = this.orderList.find(o => o.id === this.checkInForm.orderId)
          if (order) {
            const issueCardResponse = await hardwareApi.issueCard(order.roomId, this.getCustomerName(order.customerId), this.checkInForm.idCard)
            if (issueCardResponse.status === 200) {
              this.$message.success('门卡制发成功')
            } else {
              this.$message.error(issueCardResponse.message || '门卡制发失败')
            }
          }
          this.$message.success('入住办理成功')
          this.resetForm()
          this.loadData()
        } else {
          this.$message.error(response.message || '入住办理失败')
        }
      } catch (error) {
        console.error('入住办理失败:', error)
        this.$message.error('入住办理失败')
      }
    },
    async processWithoutBooking() {
      try {
        // 1. 查找或创建客户
        let customerId = null
        const existingCustomer = this.customerList.find(c => c.idCard === this.checkInForm.idCard)
        
        if (existingCustomer) {
          customerId = existingCustomer.id
        } else {
          // 创建新客户
          const customerResponse = await customerApi.save({
            name: this.checkInForm.customerName,
            phone: this.checkInForm.phone,
            idCard: this.checkInForm.idCard
          })
          if (customerResponse.status === 200) {
            // 重新加载客户列表
            await this.loadCustomers()
            // 查找新创建的客户
            const newCustomer = this.customerList.find(c => c.idCard === this.checkInForm.idCard)
            if (newCustomer) {
              customerId = newCustomer.id
            }
          } else {
            this.$message.error('客户创建失败')
            return
          }
        }
        
        if (!customerId) {
          this.$message.error('客户信息处理失败')
          return
        }
        
        // 转换日期格式
        const formatDate = (date) => {
          if (!date) return ''
          const d = new Date(date)
          const year = d.getFullYear()
          const month = String(d.getMonth() + 1).padStart(2, '0')
          const day = String(d.getDate()).padStart(2, '0')
          return `${year}-${month}-${day}`
        }
        
        // 2. 创建订单
        const bookingResponse = await bookingApi.create({
          roomId: parseInt(this.checkInForm.roomId),
          customerId: parseInt(customerId),
          checkInDate: formatDate(this.checkInForm.checkInDate),
          checkOutDate: formatDate(this.checkInForm.checkOutDate),
          days: parseInt(this.checkInForm.days),
          totalPrice: parseFloat(this.checkInForm.totalPrice)
        })
        
        if (bookingResponse.status === 200) {
          const orderId = bookingResponse.data.id
          
          // 3. 办理入住
          const checkInResponse = await checkInApi.process(orderId)
          if (checkInResponse.status === 200) {
            // 4. 制发门卡
            const issueCardResponse = await hardwareApi.issueCard(this.checkInForm.roomId, this.checkInForm.customerName, this.checkInForm.idCard)
            if (issueCardResponse.status === 200) {
              this.$message.success('门卡制发成功')
            } else {
              this.$message.error(issueCardResponse.message || '门卡制发失败')
            }
            
            this.$message.success('无预定入住办理成功')
            this.resetForm()
            this.loadData()
          } else {
            this.$message.error(checkInResponse.message || '入住办理失败')
          }
        } else {
          this.$message.error(bookingResponse.message || '订单创建失败')
        }
      } catch (error) {
        console.error('无预定入住办理失败:', error)
        this.$message.error('无预定入住办理失败')
      }
    },
    async extendStay() {
      if (!this.extendForm.orderId) {
        this.$message.error('请选择订单')
        return
      }
      try {
        const response = await checkInApi.extend(this.extendForm.orderId, this.extendForm.additionalDays)
        if (response.status === 200) {
          this.$message.success('续住办理成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '续住办理失败')
        }
      } catch (error) {
        console.error('续住办理失败:', error)
        this.$message.error('续住办理失败')
      }
    },
    async changeRoom() {
      if (!this.extendForm.orderId || !this.extendForm.newRoomId) {
        this.$message.error('请选择订单和新房间')
        return
      }
      try {
        const response = await checkInApi.changeRoom(this.extendForm.orderId, this.extendForm.newRoomId)
        if (response.status === 200) {
          this.$message.success('换房办理成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '换房办理失败')
        }
      } catch (error) {
        console.error('换房办理失败:', error)
        this.$message.error('换房办理失败')
      }
    },
    handleExtend(orderId) {
      this.extendForm.orderId = orderId
    },
    handleChangeRoom(orderId) {
      this.extendForm.orderId = orderId
    },
    resetForm() {
      this.checkInForm = {
        mode: 'withBooking',
        orderId: '',
        roomId: '',
        customerName: '',
        idCard: '',
        phone: '',
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
    calculateDays() {
      if (this.checkInForm.checkInDate && this.checkInForm.checkOutDate) {
        const checkIn = new Date(this.checkInForm.checkInDate)
        const checkOut = new Date(this.checkInForm.checkOutDate)
        const diffTime = Math.abs(checkOut - checkIn)
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
        this.checkInForm.days = diffDays
      }
    },
    calculateTotalPrice() {
      if (this.checkInForm.roomId && this.checkInForm.days) {
        const room = this.roomList.find(r => r.id === this.checkInForm.roomId)
        if (room && room.price) {
          this.checkInForm.totalPrice = room.price * this.checkInForm.days
        }
      }
    }
  }
}
</script>

<style scoped>
.checkin {
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