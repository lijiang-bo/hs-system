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

// 定义各路径允许的角色（1=超级管理员, 2=管理员, 3=普通员工）
const routeRoleMap = {
  '/emps': [1, 2],
  '/logs': [1, 2],
  '/depts': [1, 2, 3],
  '/clazzs': [1, 2, 3],
  '/students': [1, 2, 3],
  '/dashboard': [1, 2, 3],
}

router.beforeEach((to) => {
  const authStore = useAuthStore()
  if (to.path !== '/login' && !authStore.isLogin) return '/login'
  if (to.path === '/login' && authStore.isLogin) return '/dashboard'

  // 角色权限校验：如果当前路径有角色限制且不满足，则重定向到首页
  const allowedRoles = routeRoleMap[to.path]
  if (allowedRoles && !allowedRoles.includes(authStore.role)) {
    return '/dashboard'
  }
  return true
})

export default router
