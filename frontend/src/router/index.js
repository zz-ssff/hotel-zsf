import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/room',
    name: 'Room',
    component: () => import('../views/Room.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/booking',
    name: 'Booking',
    component: () => import('../views/Booking.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/checkin',
    name: 'CheckIn',
    component: () => import('../views/CheckIn.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/checkout',
    name: 'CheckOut',
    component: () => import('../views/CheckOut.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/member',
    name: 'Member',
    component: () => import('../views/Member.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/report',
    name: 'Report',
    component: () => import('../views/Report.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('../views/User.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const user = localStorage.getItem('user')
  
  if (requiresAuth && !user) {
    next('/login')
  } else {
    next()
  }
})

export default router