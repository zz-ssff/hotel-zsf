/**
 * API请求工具类
 * 封装了所有与后端交互的API接口
 */
import axios from 'axios'

/**
 * 创建axios实例
 * @type {AxiosInstance}
 */
const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

/**
 * 客房相关API
 */
export const roomApi = {
  /**
   * 获取所有房间列表
   * @returns {Promise<Array>} 房间列表
   */
  list: () => api.get('/room/list'),
  /**
   * 分页获取房间列表
   * @param {number} current - 当前页码
   * @param {number} size - 每页大小
   * @returns {Promise<Object>} 分页房间数据
   */
  page: (current, size) => api.get(`/room/page?current=${current}&size=${size}`),
  /**
   * 保存房间信息
   * @param {Object} room - 房间信息
   * @returns {Promise<boolean>} 保存结果
   */
  save: (room) => api.post('/room/save', room),
  /**
   * 更新房间信息
   * @param {Object} room - 房间信息
   * @returns {Promise<boolean>} 更新结果
   */
  update: (room) => api.put('/room/update', room),
  /**
   * 删除房间
   * @param {number} id - 房间ID
   * @returns {Promise<boolean>} 删除结果
   */
  delete: (id) => api.delete(`/room/delete/${id}`),
  /**
   * 根据ID获取房间信息
   * @param {number} id - 房间ID
   * @returns {Promise<Object>} 房间信息
   */
  get: (id) => api.get(`/room/get/${id}`)
}

/**
 * 客户相关API
 */
export const customerApi = {
  /**
   * 获取所有客户列表
   * @returns {Promise<Array>} 客户列表
   */
  list: () => api.get('/customer/list'),
  /**
   * 分页获取客户列表
   * @param {number} current - 当前页码
   * @param {number} size - 每页大小
   * @returns {Promise<Object>} 分页客户数据
   */
  page: (current, size) => api.get(`/customer/page?current=${current}&size=${size}`),
  /**
   * 保存客户信息
   * @param {Object} customer - 客户信息
   * @returns {Promise<boolean>} 保存结果
   */
  save: (customer) => api.post('/customer/save', customer),
  /**
   * 更新客户信息
   * @param {Object} customer - 客户信息
   * @returns {Promise<boolean>} 更新结果
   */
  update: (customer) => api.put('/customer/update', customer),
  /**
   * 删除客户
   * @param {number} id - 客户ID
   * @returns {Promise<boolean>} 删除结果
   */
  delete: (id) => api.delete(`/customer/delete/${id}`),
  /**
   * 根据ID获取客户信息
   * @param {number} id - 客户ID
   * @returns {Promise<Object>} 客户信息
   */
  get: (id) => api.get(`/customer/get/${id}`)
}

/**
 * 订单相关API
 */
export const orderApi = {
  /**
   * 获取所有订单列表
   * @returns {Promise<Array>} 订单列表
   */
  list: () => api.get('/order/list'),
  /**
   * 分页获取订单列表
   * @param {number} current - 当前页码
   * @param {number} size - 每页大小
   * @returns {Promise<Object>} 分页订单数据
   */
  page: (current, size) => api.get(`/order/page?current=${current}&size=${size}`),
  /**
   * 保存订单信息
   * @param {Object} order - 订单信息
   * @returns {Promise<boolean>} 保存结果
   */
  save: (order) => api.post('/order/save', order),
  /**
   * 更新订单信息
   * @param {Object} order - 订单信息
   * @returns {Promise<boolean>} 更新结果
   */
  update: (order) => api.put('/order/update', order),
  /**
   * 删除订单
   * @param {number} id - 订单ID
   * @returns {Promise<boolean>} 删除结果
   */
  delete: (id) => api.delete(`/order/delete/${id}`),
  /**
   * 根据ID获取订单信息
   * @param {number} id - 订单ID
   * @returns {Promise<Object>} 订单信息
   */
  get: (id) => api.get(`/order/get/${id}`)
}

/**
 * 会员相关API
 */
export const memberApi = {
  /**
   * 获取所有会员列表
   * @returns {Promise<Array>} 会员列表
   */
  list: () => api.get('/member/list'),
  /**
   * 分页获取会员列表
   * @param {number} current - 当前页码
   * @param {number} size - 每页大小
   * @returns {Promise<Object>} 分页会员数据
   */
  page: (current, size) => api.get(`/member/page?current=${current}&size=${size}`),
  /**
   * 保存会员信息
   * @param {Object} member - 会员信息
   * @returns {Promise<boolean>} 保存结果
   */
  save: (member) => api.post('/member/save', member),
  /**
   * 更新会员信息
   * @param {Object} member - 会员信息
   * @returns {Promise<boolean>} 更新结果
   */
  update: (member) => api.put('/member/update', member),
  /**
   * 删除会员
   * @param {number} id - 会员ID
   * @returns {Promise<boolean>} 删除结果
   */
  delete: (id) => api.delete(`/member/delete/${id}`),
  /**
   * 根据ID获取会员信息
   * @param {number} id - 会员ID
   * @returns {Promise<Object>} 会员信息
   */
  get: (id) => api.get(`/member/get/${id}`)
}

/**
 * 预订相关API
 */
export const bookingApi = {
  /**
   * 创建预订
   * @param {Object} order - 订单信息
   * @returns {Promise<Object>} 创建的订单
   */
  create: (order) => api.post('/booking/create', order),
  /**
   * 取消预订
   * @param {number} id - 订单ID
   * @returns {Promise<boolean>} 取消结果
   */
  cancel: (id) => api.put(`/booking/cancel/${id}`),
  /**
   * 确认预订
   * @param {number} id - 订单ID
   * @returns {Promise<boolean>} 确认结果
   */
  confirm: (id) => api.put(`/booking/confirm/${id}`)
}

/**
 * 入住相关API
 */
export const checkInApi = {
  /**
   * 处理入住
   * @param {number} id - 订单ID
   * @returns {Promise<boolean>} 入住处理结果
   */
  process: (id) => api.put(`/checkin/process/${id}`),
  /**
   * 延长入住
   * @param {number} id - 订单ID
   * @param {number} additionalDays - 额外天数
   * @returns {Promise<boolean>} 延长处理结果
   */
  extend: (id, additionalDays) => api.put(`/checkin/extend/${id}?additionalDays=${additionalDays}`),
  /**
   * 更换房间
   * @param {number} id - 订单ID
   * @param {number} newRoomId - 新房间ID
   * @returns {Promise<boolean>} 更换处理结果
   */
  changeRoom: (id, newRoomId) => api.put(`/checkin/change-room/${id}?newRoomId=${newRoomId}`)
}

/**
 * 退房相关API
 */
export const checkOutApi = {
  /**
   * 处理退房
   * @param {number} id - 订单ID
   * @returns {Promise<Object>} 退房处理结果
   */
  process: (id) => api.put(`/checkout/process/${id}`),
  /**
   * 计算总金额
   * @param {number} id - 订单ID
   * @returns {Promise<number>} 总金额
   */
  calculate: (id) => api.get(`/checkout/calculate/${id}`),
  /**
   * 处理支付
   * @param {number} id - 订单ID
   * @param {string} paymentMethod - 支付方式
   * @returns {Promise<boolean>} 支付处理结果
   */
  payment: (id, paymentMethod) => api.put(`/checkout/payment/${id}?paymentMethod=${paymentMethod}`)
}

/**
 * 硬件接口相关API
 */
export const hardwareApi = {
  /**
   * 读取身份证
   * @returns {Promise<Object>} 身份证信息
   */
  readIdCard: () => api.get('/hardware/read-idcard'),
  /**
   * 发放房卡
   * @param {number} roomId - 房间ID
   * @param {string} customerName - 客户姓名
   * @param {string} idCard - 身份证号
   * @returns {Promise<boolean>} 发放结果
   */
  issueCard: (roomId, customerName, idCard) => api.post(`/hardware/issue-card?roomId=${roomId}&customerName=${customerName}&idCard=${idCard}`),
  /**
   * 开门
   * @param {number} roomId - 房间ID
   * @param {string} cardId - 卡号
   * @returns {Promise<boolean>} 开门结果
   */
  openDoor: (roomId, cardId) => api.post(`/hardware/open-door?roomId=${roomId}&cardId=${cardId}`)
}

/**
 * 报表相关API
 */
export const reportApi = {
  /**
   * 获取日收入报表
   * @param {string} startDate - 开始日期
   * @param {string} endDate - 结束日期
   * @returns {Promise<Array>} 日收入报表数据
   */
  dailyRevenue: (startDate, endDate) => api.get(`/report/daily-revenue?startDate=${startDate}&endDate=${endDate}`),
  /**
   * 获取入住率报表
   * @param {string} startDate - 开始日期
   * @param {string} endDate - 结束日期
   * @returns {Promise<Array>} 入住率报表数据
   */
  occupancyRate: (startDate, endDate) => api.get(`/report/occupancy-rate?startDate=${startDate}&endDate=${endDate}`),
  /**
   * 获取平均价格报表
   * @param {string} startDate - 开始日期
   * @param {string} endDate - 结束日期
   * @returns {Promise<Array>} 平均价格报表数据
   */
  averagePrice: (startDate, endDate) => api.get(`/report/average-price?startDate=${startDate}&endDate=${endDate}`),
  /**
   * 获取会员消费报表
   * @param {string} startDate - 开始日期
   * @param {string} endDate - 结束日期
   * @returns {Promise<Array>} 会员消费报表数据
   */
  memberConsumption: (startDate, endDate) => api.get(`/report/member-consumption?startDate=${startDate}&endDate=${endDate}`),
  /**
   * 获取关键指标
   * @returns {Promise<Object>} 关键指标数据
   */
  keyIndicators: () => api.get('/report/key-indicators')
}

/**
 * 用户相关API
 */
export const userApi = {
  /**
   * 获取所有用户列表
   * @returns {Promise<Array>} 用户列表
   */
  list: () => api.get('/user/list'),
  /**
   * 分页获取用户列表
   * @param {number} current - 当前页码
   * @param {number} size - 每页大小
   * @returns {Promise<Object>} 分页用户数据
   */
  page: (current, size) => api.get(`/user/page?current=${current}&size=${size}`),
  /**
   * 保存用户信息
   * @param {Object} user - 用户信息
   * @returns {Promise<boolean>} 保存结果
   */
  save: (user) => api.post('/user/add', user),
  /**
   * 更新用户信息
   * @param {Object} user - 用户信息
   * @returns {Promise<boolean>} 更新结果
   */
  update: (user) => api.put('/user/update', user),
  /**
   * 删除用户
   * @param {number} id - 用户ID
   * @returns {Promise<boolean>} 删除结果
   */
  delete: (id) => api.post('/user/delete', { id }),
  /**
   * 根据ID获取用户信息
   * @param {number} id - 用户ID
   * @returns {Promise<Object>} 用户信息
   */
  get: (id) => api.get(`/user/get/${id}`),
  /**
   * 获取所有角色列表
   * @returns {Promise<Array>} 角色列表
   */
  roles: () => api.get('/user/roles'),
  /**
   * 用户登录
   * @param {Object} user - 登录信息
   * @returns {Promise<Object>} 登录结果
   */
  login: (user) => api.post('/user/login', user)
}

export default api