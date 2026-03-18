<template>
  <div class="room">
    <el-container>
      <AppHeader title="酒店PMS系统 - 客房管理" />

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
          <h2>客房管理</h2>
          <!-- 房间列表 -->
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <span>房间列表</span>
                <el-button type="primary" size="small" @click="addRoom">添加房间</el-button>
              </div>
            </template>
            
            <!-- 筛选栏 -->
            <div class="filter-section">
              <div class="filter-row">
                <div class="filter-item">
                  <label>房间类型：</label>
                  <el-select v-model="filterForm.roomType" placeholder="选择房间类型" class="filter-select">
                    <el-option label="单人间" value="single" />
                    <el-option label="双人间" value="double" />
                    <el-option label="豪华间" value="luxury" />
                    <el-option label="套房" value="suite" />
                  </el-select>
                </div>
                <div class="filter-item">
                  <label>房态：</label>
                  <el-select v-model="filterForm.status" placeholder="选择房态" class="filter-select">
                    <el-option label="空闲" value="1" />
                    <el-option label="已预订" value="2" />
                    <el-option label="已入住" value="3" />
                    <el-option label="打扫中" value="4" />
                  </el-select>
                </div>
                <div class="filter-item filter-buttons">
                  <el-button type="primary" @click="filterRooms">筛选</el-button>
                  <el-button @click="resetFilter">重置</el-button>
                </div>
              </div>
            </div>
            
            <el-table :data="roomList" style="width: 100%">
              <el-table-column prop="roomNumber" label="房间号" width="120" />
              <el-table-column prop="roomType" label="房间类型" width="120">
                <template #default="scope">
                  {{ getRoomTypeText(scope.row.roomType) }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="房态" width="150">
                <template #default="scope">
                  <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="price" label="价格" width="100" />
              <el-table-column prop="description" label="描述" />
              <el-table-column label="操作" min-width="150">
                <template #default="scope">
                  <div class="operation-buttons">
                    <el-button type="primary" size="small" @click="editRoom(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="deleteRoom(scope.row.id)">删除</el-button>
                  </div>
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
                page-size-text="条/页"
                total-text="共 {total} 条"
                sizes-text="每页显示"
              />
            </div>
          </el-card>
        </el-main>
      </el-container>
    </el-container>

    <!-- 房间编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form :model="currentRoom" label-width="80px">
        <el-form-item label="房间号">
          <el-input v-model="currentRoom.roomNumber" placeholder="房间号" />
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="currentRoom.roomType" placeholder="选择房间类型">
            <el-option label="单人间" value="single" />
            <el-option label="双人间" value="double" />
            <el-option label="豪华间" value="luxury" />
            <el-option label="套房" value="suite" />
          </el-select>
        </el-form-item>
        <el-form-item label="房态">
          <el-select v-model="currentRoom.status" placeholder="选择房态">
            <el-option label="空闲" value="1" />
            <el-option label="已预订" value="2" />
            <el-option label="已入住" value="3" />
            <el-option label="打扫中" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model.number="currentRoom.price" placeholder="价格" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="currentRoom.description" placeholder="描述" />
        </el-form-item>
        <el-form-item label="楼层">
          <el-input v-model.number="currentRoom.floor" placeholder="楼层" />
        </el-form-item>
        <el-form-item label="朝向">
          <el-input v-model="currentRoom.orientation" placeholder="朝向" />
        </el-form-item>
        <el-form-item label="设施">
          <el-input v-model="currentRoom.facilities" placeholder="设施" />
        </el-form-item>
        <el-form-item label="最大入住人数">
          <el-input v-model.number="currentRoom.maxOccupancy" placeholder="最大入住人数" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveRoom">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { HomeFilled, House, Ticket, UserFilled, Goods, Star, DataAnalysis, User } from '@element-plus/icons-vue'
import { roomApi } from '../api'
import AppHeader from '../components/Header.vue'

export default {
  name: 'Room',
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
    const activeIndex = ref('/room')
    const user = JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } }
    const filterForm = ref({
      roomType: '',
      status: ''
    })
    const roomList = ref([])
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    const dialogVisible = ref(false)
    const dialogTitle = ref('添加房间')
    const currentRoom = ref({
      id: '',
      roomNumber: '',
      roomType: '单人间',
      status: '1',
      price: 0,
      description: '',
      hotelId: 1,
      floor: 1,
      orientation: '南',
      facilities: '空调,电视,WiFi',
      maxOccupancy: 1
    })

    // 加载房间列表
    const loadRooms = async () => {
      try {
        const response = await roomApi.list()
      
        if (response.status === 200) {
          roomList.value = response.data || []
          total.value = response.data ? response.data.length : 0
        }
        return Promise.resolve()
      } catch (error) {
        console.error('加载房间失败:', error)
        ElMessage.error('加载房间失败')
        return Promise.reject(error)
      }
    }

    // 获取房间类型文本
    const getRoomTypeText = (type) => {
      const types = {
        single: '单人间',
        double: '双人间',
        luxury: '豪华间',
        suite: '套房'
      }
      return types[type] || type
    }

    // 获取状态文本
    const getStatusText = (status) => {
      const statuses = {
        1: '空闲',
        2: '已预订',
        3: '已入住',
        4: '打扫中'
      }
      return statuses[status] || status
    }

    // 获取状态类型
    const getStatusType = (status) => {
      const types = {
        1: 'success',
        2: 'warning',
        3: 'danger',
        4: 'info'
      }
      return types[status] || ''
    }

    // 筛选房间
    const filterRooms = () => {
      // 筛选逻辑
      console.log('筛选条件:', filterForm.value)
      
      // 从后端重新获取所有房间数据
      loadRooms().then(() => {
        // 根据筛选条件过滤房间列表
        let filteredRooms = [...roomList.value]
        
        if (filterForm.value.roomType) {
          filteredRooms = filteredRooms.filter(room => room.roomType === filterForm.value.roomType)
        }
        
        if (filterForm.value.status) {
          // 将字符串类型的状态值转换为数字类型进行比较
          const status = parseInt(filterForm.value.status)
          filteredRooms = filteredRooms.filter(room => room.status === status)
        }
        
        // 更新房间列表和总数
        roomList.value = filteredRooms
        total.value = filteredRooms.length
      })
    }

    // 重置筛选
    const resetFilter = () => {
      filterForm.value = {
        roomType: '',
        status: ''
      }
      // 重置后重新加载所有房间数据
      loadRooms()
    }

    // 添加房间
    const addRoom = () => {
      dialogTitle.value = '添加房间'
      currentRoom.value = {
        id: '',
        roomNumber: '',
        roomType: '单人间',
        status: '1',
        price: 0,
        description: '',
        hotelId: 1,
        floor: 1,
        orientation: '南',
        facilities: '空调,电视,WiFi',
        maxOccupancy: 1
      }
      dialogVisible.value = true
    }

    // 编辑房间
    const editRoom = (room) => {
      dialogTitle.value = '编辑房间'
      currentRoom.value = { ...room }
      dialogVisible.value = true
    }

    // 保存房间
    const saveRoom = async () => {
      try {
        if (currentRoom.value.id) {
          const response = await roomApi.update(currentRoom.value)
          if (response.status === 200) {
            ElMessage.success('房间更新成功')
          } else {
            ElMessage.error(response.message)
          }
        } else {
          const response = await roomApi.save(currentRoom.value)
          if (response.status === 200) {
            ElMessage.success('房间添加成功')
          } else {
            ElMessage.error(response.message)
          }
        }
        dialogVisible.value = false
        loadRooms()
      } catch (error) {
        console.error('保存房间失败:', error)
        ElMessage.error('保存房间失败')
      }
    }

    // 删除房间
    const deleteRoom = async (id) => {
      try {
        const response = await roomApi.delete(id)
        if (response.status === 200) {
          ElMessage.success('房间删除成功')
          loadRooms()
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        console.error('删除房间失败:', error)
        ElMessage.error('删除房间失败')
      }
    }

    // 处理分页大小变化
    const handleSizeChange = (size) => {
      pageSize.value = size
    }

    // 处理当前页码变化
    const handleCurrentChange = (current) => {
      currentPage.value = current
    }

    // 组件挂载时加载房间列表
    onMounted(() => {
      loadRooms()
    })

    return {
      activeIndex,
      user,
      filterForm,
      roomList,
      currentPage,
      pageSize,
      total,
      dialogVisible,
      dialogTitle,
      currentRoom,
      loadRooms,
      getRoomTypeText,
      getStatusText,
      getStatusType,
      filterRooms,
      resetFilter,
      addRoom,
      editRoom,
      saveRoom,
      deleteRoom,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>
.room {
  min-height: 100vh;
  padding-top: 60px;
}

.el-aside {
  background-color: #f5f7fa;
}

.el-main {
  padding: 20px;
  background-color: #ffffff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.filter-section {
  margin-bottom: 20px;
  padding: 20px;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.filter-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  width: 100%;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 200px;
}

.filter-item label {
  font-weight: 500;
  color: #333333;
  white-space: nowrap;
  min-width: 80px;
}

.filter-select {
  flex: 1;
  min-width: 150px;
}

/* 确保下拉菜单显示完整文本 */
.el-select .el-input__wrapper {
  overflow: visible !important;
}

.el-select-dropdown {
  min-width: 150px !important;
}

.el-select-dropdown__item {
  white-space: nowrap !important;
  padding: 0 20px !important;
}

.filter-buttons {
  flex: none;
  display: flex;
  gap: 10px;
}

.filter-buttons .el-button {
  white-space: nowrap;
}

.operation-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
}

.operation-buttons .el-button {
  white-space: nowrap;
}

.pagination {
  margin-top: 20px;
  text-align: right;
  padding: 10px 0;
}

/* 表格样式增强 */
.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table th {
  font-weight: 600;
  padding: 12px 8px;
}

.el-table td {
  padding: 12px 8px;
}

/* 按钮样式增强 */
.el-button {
  border-radius: 6px;
  font-weight: 500;
}

.el-button--primary {
  background-color: #409EFF;
  border-color: #409EFF;
}

.el-button--primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

/* 选择器样式增强 */
.el-select {
  border-radius: 6px;
}

.el-select .el-input__wrapper {
  border-radius: 6px;
}

/* 卡片样式增强 */
.el-card {
  border-radius: 8px;
  overflow: hidden;
}

.el-card__header {
  padding: 16px;
  font-size: 16px;
  font-weight: 600;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .filter-item {
    width: 100%;
  }
  
  .el-select {
    flex: 1;
  }
}

/* 确保标签不显示省略号 */
.el-tag {
  white-space: nowrap !important;
  overflow: visible !important;
  text-overflow: clip !important;
  max-width: none !important;
  padding: 0 12px !important;
}

/* 确保表格单元格中的标签有足够的空间 */
.el-table__cell {
  overflow: visible !important;
}

.el-table__cell .el-tag {
  max-width: none !important;
  white-space: nowrap !important;
}

/* 针对状态标签的特定样式，确保不显示省略号 */
.status-tag {
  white-space: nowrap !important;
  overflow: visible !important;
  text-overflow: clip !important;
  max-width: none !important;
  padding: 0 12px !important;
  line-height: 22px !important;
  height: 22px !important;
}


</style>