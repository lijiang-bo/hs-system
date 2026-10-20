<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const searchValue = ref('')

const allPageMenu = [
  { value: '总览看板', path: '/dashboard', icon: 'DataAnalysis', roles: [1, 2, 3] },
  { value: '员工管理', path: '/emps', icon: 'User', roles: [1, 2] },
  { value: '部门管理', path: '/depts', icon: 'OfficeBuilding', roles: [1, 2, 3] },
  { value: '班级管理', path: '/clazzs', icon: 'Reading', roles: [1, 2, 3] },
  { value: '学员管理', path: '/students', icon: 'School', roles: [1, 2, 3] },
  { value: '日志管理', path: '/logs', icon: 'Document', roles: [1, 2] },
]

const pageMenu = computed(() => allPageMenu.filter(m => m.roles.includes(authStore.role)))
const activeMenu = computed(() => route.path)
const breadcrumbName = computed(() => {
  const found = allPageMenu.find(m => m.path === route.path)
  return found ? found.value : '首页'
})

const handleSelect = (index) => router.push(index)

const handleLogout = async () => {
  await ElMessageBox.confirm('确认退出登录吗？', '提示', { type: 'warning' })
  authStore.logout()
  router.push('/login')
}

const querySearch = (queryString, cb) => {
  const results = queryString
    ? pageMenu.value.filter(item => item.value.includes(queryString))
    : pageMenu.value
  cb(results.map(item => ({ value: item.value, path: item.path })))
}

const handleSearchSelect = (item) => {
  router.push(item.path)
  searchValue.value = ''
}
</script>

<template>
  <div class="layout-shell">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-brand">
        <div class="brand-icon">
          <svg width="28" height="28" viewBox="0 0 28 28" fill="none">
            <rect width="28" height="28" rx="8" fill="oklch(0.52 0.165 230)"/>
            <path d="M8 18L14 10L20 18" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <span class="brand-text">Hs Pro</span>
      </div>

      <nav class="sidebar-nav">
        <div
          v-for="menu in pageMenu"
          :key="menu.path"
          class="nav-item"
          :class="{ active: activeMenu === menu.path }"
          @click="handleSelect(menu.path)"
        >
          <component :is="menu.icon" class="nav-icon" v-if="menu.icon" />
          <span class="nav-label">{{ menu.value }}</span>
          <span v-if="activeMenu === menu.path" class="nav-dot"></span>
        </div>
      </nav>

      <div class="sidebar-footer">
        <div class="user-badge">
          <el-avatar :size="32" style="background: oklch(0.52 0.165 230);">
            {{ (authStore.user?.name || authStore.user?.username || 'U').slice(0, 1) }}
          </el-avatar>
          <div class="user-meta">
            <span class="user-name">{{ authStore.user?.name || authStore.user?.username }}</span>
            <span class="user-role">{{ authStore.roleName }}</span>
          </div>
        </div>
        <el-button class="logout-btn" link @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
        </el-button>
      </div>
    </aside>

    <!-- Main -->
    <div class="main-area">
      <header class="topbar">
        <div class="topbar-left">
          <span class="topbar-title">{{ breadcrumbName }}</span>
          <span class="topbar-breadcrumb">首页 / {{ breadcrumbName }}</span>
        </div>
        <div class="topbar-right">
          <el-autocomplete
            v-model="searchValue"
            :fetch-suggestions="querySearch"
            placeholder="搜索页面功能"
            class="topbar-search"
            clearable
            :trigger-on-focus="false"
            @select="handleSearchSelect"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-autocomplete>
        </div>
      </header>

      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
/* ---- shell ---- */
.layout-shell {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

/* ---- sidebar ---- */
.sidebar {
  width: var(--hs-sidebar-width);
  background: var(--hs-sidebar);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  user-select: none;
}

.sidebar-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 18px 20px;
  border-bottom: 1px solid oklch(1 0 0 / 0.06);
}

.brand-icon {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.brand-text {
  font-size: 19px;
  font-weight: 700;
  color: oklch(1 0 0);
  letter-spacing: -0.01em;
}

/* nav */
.sidebar-nav {
  flex: 1;
  padding: 12px 8px;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  margin-bottom: 2px;
  border-radius: 9px;
  cursor: pointer;
  transition: all 0.15s ease;
  color: oklch(1 0 0 / 0.6);
  font-size: 14px;
  position: relative;
}

.nav-item:hover { background: var(--hs-sidebar-hover); color: oklch(1 0 0 / 0.88); }
.nav-item.active { background: var(--hs-sidebar-active); color: oklch(1 0 0); font-weight: 600; }

.nav-icon { width: 18px; height: 18px; flex-shrink: 0; }

.nav-dot {
  position: absolute;
  right: 12px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--hs-primary);
}

/* sidebar footer */
.sidebar-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  border-top: 1px solid oklch(1 0 0 / 0.06);
}

.user-badge { display: flex; align-items: center; gap: 10px; }
.user-meta { display: flex; flex-direction: column; gap: 2px; }
.user-name { font-size: 13px; font-weight: 600; color: oklch(1 0 0 / 0.85); white-space: nowrap; }
.user-role { font-size: 11px; color: oklch(1 0 0 / 0.4); }

.logout-btn {
  color: oklch(1 0 0 / 0.35);
  font-size: 18px;
  transition: color 0.15s;
  padding: 4px;
}

.logout-btn:hover { color: var(--hs-danger); }

/* ---- main area ---- */
.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--hs-bg);
}

/* ---- topbar ---- */
.topbar {
  height: var(--hs-header-height);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: var(--hs-header);
  border-bottom: 1px solid var(--hs-border-light);
  flex-shrink: 0;
}

.topbar-left { display: flex; flex-direction: column; gap: 2px; }

.topbar-title {
  font-size: 17px;
  font-weight: 700;
  color: var(--hs-ink);
  letter-spacing: -0.01em;
}

.topbar-breadcrumb {
  font-size: 12px;
  color: var(--hs-ink-muted);
}

.topbar-right { display: flex; align-items: center; gap: 16px; }
.topbar-search { width: 220px; }

/* ---- content ---- */
.content {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}
</style>
