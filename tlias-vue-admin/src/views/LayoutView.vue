<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const activeMenu = computed(() => route.path)
const breadcrumbName = computed(() => {
  const map = {
    '/dashboard': '总览看板',
    '/emps': '员工管理',
    '/depts': '部门管理',
    '/clazzs': '班级管理',
    '/students': '学员管理',
    '/logs': '日志管理',
  }
  return map[route.path] || '首页'
})

const handleSelect = (index) => {
  router.push(index)
}

const handleLogout = async () => {
  await ElMessageBox.confirm('确认退出登录吗？', '提示', { type: 'warning' })
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo-wrap">
        <div class="logo-dot"></div>
        <div class="logo">TLIAS Pro</div>
      </div>
      <el-menu :default-active="activeMenu" class="menu" @select="handleSelect">
        <el-menu-item index="/dashboard"><el-icon><DataAnalysis /></el-icon>总览看板</el-menu-item>
        <el-menu-item index="/emps"><el-icon><User /></el-icon>员工管理</el-menu-item>
        <el-menu-item index="/depts"><el-icon><OfficeBuilding /></el-icon>部门管理</el-menu-item>
        <el-menu-item index="/clazzs"><el-icon><Reading /></el-icon>班级管理</el-menu-item>
        <el-menu-item index="/students"><el-icon><School /></el-icon>学员管理</el-menu-item>
        <el-menu-item index="/logs"><el-icon><Document /></el-icon>日志管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div>
          <div class="header-title">后端业务管理台</div>
          <div class="breadcrumb">首页 / {{ breadcrumbName }}</div>
        </div>
        <div class="right">
          <el-input placeholder="搜索页面功能" class="header-search" clearable>
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
          <el-badge :value="3" class="msg-badge">
            <el-button circle>
              <el-icon><Bell /></el-icon>
            </el-button>
          </el-badge>
          <el-avatar :size="30">{{ (authStore.user?.name || authStore.user?.username || 'U').slice(0, 1) }}</el-avatar>
          <span class="welcome">{{ authStore.user?.name || authStore.user?.username }}</span>
          <el-button link type="danger" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  position: relative;
}

.layout::after {
  content: '';
  position: absolute;
  right: 40px;
  top: 90px;
  width: 220px;
  height: 220px;
  background: radial-gradient(circle, rgba(22, 119, 255, 0.14), rgba(22, 119, 255, 0));
  pointer-events: none;
  z-index: 0;
}

.aside {
  background: var(--pro-sidebar);
  color: #fff;
}

.logo-wrap {
  height: 60px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 16px;
}

.logo-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #1677ff;
  box-shadow: 0 0 16px #1677ff;
}

.logo {
  font-size: 20px;
  font-weight: 700;
}

.menu {
  border-right: none;
  background: transparent;
}

.menu :deep(.el-menu-item) {
  color: #d5def5;
}

.menu :deep(.el-menu-item:hover),
.menu :deep(.el-menu-item.is-active) {
  background: var(--pro-sidebar-hover);
  color: #fff;
}

.header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #e9eef5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  backdrop-filter: blur(6px);
}

.header-title {
  font-size: 16px;
  font-weight: 700;
}

.breadcrumb {
  font-size: 12px;
  color: #667085;
  margin-top: 3px;
}

.right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-search {
  width: 220px;
}

.msg-badge :deep(.el-button) {
  border-color: #d9e2f2;
}

.welcome {
  color: #344054;
  font-weight: 500;
}

.el-main {
  position: relative;
  z-index: 1;
}
</style>
