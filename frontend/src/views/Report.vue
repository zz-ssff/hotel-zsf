<template>
  <div class="report">
    <el-container>
      <AppHeader title="酒店PMS系统 - 数据报表" />

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
          <h2>数据报表</h2>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>每日营收趋势</span>
                  </div>
                </template>
                <div class="chart-container">
                  <div ref="revenueChart" class="chart"></div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>客房出租率</span>
                  </div>
                </template>
                <div class="chart-container">
                  <div ref="occupancyChart" class="chart"></div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>平均房价(ADR)</span>
                  </div>
                </template>
                <div class="chart-container">
                  <div ref="priceChart" class="chart"></div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>会员消费分析</span>
                  </div>
                </template>
                <div class="chart-container">
                  <div ref="memberChart" class="chart"></div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="24">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>详细报表</span>
                    <el-date-picker
                      v-model="dateRange"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      @change="loadReport"
                    />
                  </div>
                </template>
                <el-table :data="reportData" style="width: 100%">
                  <el-table-column prop="date" label="日期" width="150" />
                  <el-table-column prop="revenue" label="营收" width="120" />
                  <el-table-column prop="occupancyRate" label="出租率" width="120" />
                  <el-table-column prop="averagePrice" label="平均房价" width="120" />
                  <el-table-column prop="orderCount" label="订单数" width="100" />
                  <el-table-column prop="memberCount" label="会员数" width="100" />
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
import { reportApi } from '../api'
import * as echarts from 'echarts'
import AppHeader from '../components/Header.vue'

export default {
  name: 'Report',
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
      activeIndex: '/report',
      user: JSON.parse(localStorage.getItem('user')) || { role: { roleName: '' } },
      dateRange: [],
      reportData: [
        { date: '2024-01-01', revenue: 12800, occupancyRate: '75%', averagePrice: 320, orderCount: 12, memberCount: 8 },
        { date: '2024-01-02', revenue: 13500, occupancyRate: '80%', averagePrice: 330, orderCount: 14, memberCount: 9 },
        { date: '2024-01-03', revenue: 11200, occupancyRate: '65%', averagePrice: 310, orderCount: 10, memberCount: 7 },
        { date: '2024-01-04', revenue: 14200, occupancyRate: '85%', averagePrice: 340, orderCount: 16, memberCount: 10 },
        { date: '2024-01-05', revenue: 12500, occupancyRate: '70%', averagePrice: 325, orderCount: 11, memberCount: 8 }
      ],
      currentPage: 1,
      pageSize: 10,
      total: 5,
      charts: {}
    }
  },
  mounted() {
    // 初始化日期范围为最近7天
    const endDate = new Date()
    const startDate = new Date()
    startDate.setDate(startDate.getDate() - 6)
    this.dateRange = [startDate, endDate]
    this.loadReport()
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  methods: {
    initCharts() {
      this.initRevenueChart()
      this.initOccupancyChart()
      this.initPriceChart()
      this.initMemberChart()
    },
    initRevenueChart() {
      const chartDom = this.$refs.revenueChart
      if (!chartDom) return
      this.charts.revenue = echarts.init(chartDom)
      this.updateRevenueChart()
    },
    initOccupancyChart() {
      const chartDom = this.$refs.occupancyChart
      if (!chartDom) return
      this.charts.occupancy = echarts.init(chartDom)
      this.updateOccupancyChart()
    },
    initPriceChart() {
      const chartDom = this.$refs.priceChart
      if (!chartDom) return
      this.charts.price = echarts.init(chartDom)
      this.updatePriceChart()
    },
    initMemberChart() {
      const chartDom = this.$refs.memberChart
      if (!chartDom) return
      this.charts.member = echarts.init(chartDom)
      this.updateMemberChart()
    },
    updateRevenueChart() {
      if (!this.charts.revenue) return
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: ¥{c}'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.reportData.map(item => item.date),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '营收',
            type: 'line',
            stack: 'Total',
            data: this.reportData.map(item => item.revenue),
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgba(59, 130, 246, 0.5)'
                },
                {
                  offset: 1,
                  color: 'rgba(59, 130, 246, 0.1)'
                }
              ])
            },
            lineStyle: {
              color: '#3b82f6'
            },
            itemStyle: {
              color: '#3b82f6'
            }
          }
        ]
      }
      this.charts.revenue.setOption(option)
    },
    updateOccupancyChart() {
      if (!this.charts.occupancy) return
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: {c}%'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.reportData.map(item => item.date),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}%'
          },
          max: 100
        },
        series: [
          {
            name: '出租率',
            type: 'bar',
            data: this.reportData.map(item => parseInt(item.occupancyRate)),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: '#f59e0b'
                },
                {
                  offset: 1,
                  color: '#fbbf24'
                }
              ])
            },
            barWidth: '60%'
          }
        ]
      }
      this.charts.occupancy.setOption(option)
    },
    updatePriceChart() {
      if (!this.charts.price) return
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: ¥{c}'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.reportData.map(item => item.date),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '平均房价',
            type: 'line',
            data: this.reportData.map(item => item.averagePrice),
            lineStyle: {
              color: '#10b981'
            },
            itemStyle: {
              color: '#10b981'
            },
            symbol: 'circle',
            symbolSize: 8
          }
        ]
      }
      this.charts.price.setOption(option)
    },
    async updateMemberChart() {
      if (!this.charts.member) return
      try {
        const startDate = this.formatDate(this.dateRange[0])
        const endDate = this.formatDate(this.dateRange[1])
        const response = await reportApi.memberConsumption(startDate, endDate)
        if (response.status === 200) {
          const memberData = response.data || []
          
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{b}: ¥{c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [
              {
                name: '会员消费',
                type: 'pie',
                radius: '60%',
                data: memberData.map(item => ({
                  value: item.consumption,
                  name: item.level + '会员'
                })),
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                },
                itemStyle: {
                  borderRadius: 10,
                  borderColor: '#fff',
                  borderWidth: 2
                }
              }
            ]
          }
          this.charts.member.setOption(option)
        } else {
          // 使用默认数据
          const option = {
            tooltip: {
              trigger: 'item',
              formatter: '{b}: ¥{c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left'
            },
            series: [
              {
                name: '会员消费',
                type: 'pie',
                radius: '60%',
                data: [
                  { value: 35000, name: '普通会员' },
                  { value: 25000, name: '银卡会员' },
                  { value: 18000, name: '金卡会员' },
                  { value: 12000, name: '钻石会员' }
                ],
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                },
                itemStyle: {
                  borderRadius: 10,
                  borderColor: '#fff',
                  borderWidth: 2
                }
              }
            ]
          }
          this.charts.member.setOption(option)
        }
      } catch (error) {
        console.error('加载会员消费数据失败:', error)
        // 使用默认数据
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: ¥{c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '会员消费',
              type: 'pie',
              radius: '60%',
              data: [
                { value: 35000, name: '普通会员' },
                { value: 25000, name: '银卡会员' },
                { value: 18000, name: '金卡会员' },
                { value: 12000, name: '钻石会员' }
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              },
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              }
            }
          ]
        }
        this.charts.member.setOption(option)
      }
    },
    async loadReport() {
      if (!this.dateRange || this.dateRange.length !== 2) {
        return
      }
      const startDate = this.formatDate(this.dateRange[0])
      const endDate = this.formatDate(this.dateRange[1])
      try {
        // 加载关键指标
        const indicatorsResponse = await reportApi.keyIndicators()
        if (indicatorsResponse.status === 200) {
          const indicators = indicatorsResponse.data
          console.log('关键指标:', indicators)
        }
        
        // 加载详细报表数据
        const reportResponse = await reportApi.dailyRevenue(startDate, endDate)
        if (reportResponse.status === 200) {
          this.reportData = reportResponse.data || []
          this.total = this.reportData.length
          
          // 更新图表
          this.$nextTick(() => {
            this.updateRevenueChart()
            this.updateOccupancyChart()
            this.updatePriceChart()
            this.updateMemberChart()
          })
        } else {
          this.$message.error(reportResponse.message || '加载报表数据失败')
        }
      } catch (error) {
        console.error('加载报表数据失败:', error)
        this.$message.error('加载报表数据失败')
      }
    },
    formatDate(date) {
      return date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0')
    },
    handleSizeChange(size) {
      this.pageSize = size
    },
    handleCurrentChange(current) {
      this.currentPage = current
    }
  },
  beforeUnmount() {
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  }
}
</script>

<style scoped>
.report {
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

.chart-container {
  height: 300px;
  position: relative;
}

.chart {
  width: 100%;
  height: 100%;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>