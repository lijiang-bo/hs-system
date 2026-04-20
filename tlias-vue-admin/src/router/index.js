import { createRouter, createWebHashHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/',
    component: () => import('../views/LayoutView.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'dashboard', component: () => import('../views/DashboardView.vue') },
      { path: 'emps', name: 'emps', component: () => import('../views/EmpView.vue') },
      { path: 'depts', name: 'depts', component: () => import('../views/DeptView.vue') },
      { path: 'clazzs', name: 'clazzs', component: () => import('../views/ClazzView.vue') },
      { path: 'students', name: 'students', component: () => import('../views/StudentView.vue') },
      { path: 'logs', name: 'logs', component: () => import('../views/LogView.vue') },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  if (to.path !== '/login' && !authStore.isLogin) return '/login'
  if (to.path === '/login' && authStore.isLogin) return '/dashboard'
  return true
})

export default router
