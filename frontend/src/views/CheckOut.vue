<template>
  <div class="checkout">
    <el-container>
      <AppHeader title="酒店PMS系统 - 退房结账" />

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
          <h2>退房结账</h2>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>办理退房</span>
                  </div>
                </template>
                <el-form :model="checkOutForm" label-width="80px">
                  <el-form-item label="选择订单">
                    <el-select v-model="checkOutForm.orderId" placeholder="选择已入住订单">
                      <el-option
                        v-for="order in checkedInOrders"
                        :key="order.id"
                        :label="order.orderNo + ' - ' + getCustomerName(order.customerId)"
                        :value="order.id"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="支付方式">
                    <el-radio-group v-model="checkOutForm.paymentMethod">
                      <el-radio label="cash">现金</el-radio>
                      <el-radio label="alipay">支付宝</el-radio>
                      <el-radio label="wechat">微信</el-radio>
                      <el-radio label="card">银行卡</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="总金额">
                    <el-input v-model="totalAmount" disabled />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="calculateAmount">计算金额</el-button>
                    <el-button type="success" @click="processCheckOut">办理退房</el-button>
                    <el-button @click="resetForm">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>退房列表</span>
                  </div>
                </template>
                <el-table :data="completedOrders" style="width: 100%">
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
                  <el-table-column prop="paymentMethod" label="支付方式" width="100" />
                  <el-table-column prop="status" label="状态" width="100">
                    <template #default="scope">
                      <el-tag type="success">
                        {{ getStatusText(scope.row.status) }}
                      </el-tag>
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
import { roomApi, customerApi, orderApi, checkOutApi } from '../api'
import AppHeader from '../components/Header.vue'

export default {
  name: 'CheckOut',
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
      activeIndex: '/checkout',
      user: JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } },
      checkOutForm: {
        orderId: '',
        paymentMethod: 'cash'
      },
      totalAmount: 0,
      roomList: [],
      customerList: [],
      orderList: [],
      checkedInOrders: [],
      completedOrders: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadRooms(),
        this.loadCustomers(),
        this.loadOrders()
      ])
      this.filterOrders()
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
      this.checkedInOrders = this.orderList.filter(order => 
        order.status === 'checkin'
      )
      this.completedOrders = this.orderList.filter(order => 
        order.status === 'completed'
      )
      this.total = this.completedOrders.length
    },
    async calculateAmount() {
      if (!this.checkOutForm.orderId) {
        this.$message.error('请选择订单')
        return
      }
      try {
        const response = await checkOutApi.calculate(this.checkOutForm.orderId)
        if (response.status === 200) {
          this.totalAmount = response.data
        } else {
          this.$message.error(response.message || '计算金额失败')
        }
      } catch (error) {
        console.error('计算金额失败:', error)
        this.$message.error('计算金额失败')
      }
    },
    async processCheckOut() {
      if (!this.checkOutForm.orderId) {
        this.$message.error('请选择订单')
        return
      }
      try {
        // 先处理支付
        const paymentResponse = await checkOutApi.payment(this.checkOutForm.orderId, this.checkOutForm.paymentMethod)
        if (paymentResponse.status === 200) {
          // 再办理退房
          const processResponse = await checkOutApi.process(this.checkOutForm.orderId)
          if (processResponse.status === 200) {
            this.$message.success('退房办理成功')
            this.resetForm()
            this.loadData()
          } else {
            this.$message.error(processResponse.message || '退房办理失败')
          }
        } else {
          this.$message.error(paymentResponse.message || '支付处理失败')
        }
      } catch (error) {
        console.error('退房办理失败:', error)
        this.$message.error('退房办理失败')
      }
    },
    resetForm() {
      this.checkOutForm = {
        orderId: '',
        paymentMethod: 'cash'
      }
      this.totalAmount = 0
    },
    getStatusText(status) {
      const statuses = {
        completed: '已完成'
      }
      return statuses[status] || status
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
    }
  }
}
</script>

<style scoped>
.checkout {
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