<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi } from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const passwordVisible = ref(false)

const form = reactive({ username: '', password: '' })

const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  loading.value = true
  try {
    const data = await loginApi(form)
    authStore.setLogin(data)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch {
    // handled by axios interceptor
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- Left: brand canvas -->
    <div class="login-left">
      <div class="left-bg-layer"></div>
      <div class="left-dots"></div>

      <!-- Geometric shapes -->
      <div class="geo geo-circle coral"></div>
      <div class="geo geo-square"></div>
      <div class="geo geo-circle-sm"></div>
      <div class="geo geo-diamond coral"></div>
      <div class="geo geo-ring"></div>

      <div class="left-content">
        <div class="brand-name">Hs Pro</div>
        <div class="brand-sub">教育培训综合管理平台</div>
        <div class="brand-line"></div>
        <div class="brand-desc">让管理回归专注，让数据驱动成长</div>
      </div>

      <div class="left-meta">
        <span>Privacy Policy</span>
        <span class="sep">·</span>
        <span>Terms</span>
        <span class="sep">·</span>
        <span>Contact</span>
      </div>
    </div>

    <!-- Right: login form -->
    <div class="login-right">
      <div class="login-card">
        <h1>欢迎回来</h1>
        <p class="subtitle">登录到你的工作台</p>

        <el-form @keyup.enter="handleLogin" class="login-form">
          <div class="field-group">
            <label class="field-label">账号</label>
            <el-input
              v-model="form.username"
              placeholder="请输入账号"
              size="large"
              :prefix-icon="User"
            />
          </div>

          <div class="field-group">
            <label class="field-label">密码</label>
            <el-input
              v-model="form.password"
              :type="passwordVisible ? 'text' : 'password'"
              placeholder="请输入密码"
              size="large"
              :prefix-icon="Lock"
            >
              <template #suffix>
                <el-icon class="toggle-eye" @click="passwordVisible = !passwordVisible">
                  <View v-if="!passwordVisible" />
                  <Hide v-else />
                </el-icon>
              </template>
            </el-input>
          </div>

          <div class="form-extra">
            <label class="remember">
              <input type="checkbox" checked />
              <span>记住账号</span>
            </label>
            <a href="javascript:void(0)" class="forgot">忘记密码？</a>
          </div>

          <button
            class="btn-primary"
            type="button"
            :disabled="loading"
            @click="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else class="loading-dots">
              <i></i><i></i><i></i>
            </span>
          </button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* ---- layout ---- */
.login-page {
  display: grid;
  grid-template-columns: 48fr 52fr;
  height: 100vh;
  overflow: hidden;
}

/* ---- left panel ---- */
.login-left {
  position: relative;
  background: var(--hs-primary);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.left-bg-layer {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 30% 30%, oklch(0.62 0.18 230 / 0.5), transparent 70%),
              radial-gradient(ellipse at 70% 70%, oklch(0.58 0.19 20 / 0.25), transparent 55%);
}

.left-dots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(oklch(1 0 0 / 0.08) 1px, transparent 1px);
  background-size: 28px 28px;
  background-position: 0 0;
}

/* geometric shapes */
.geo {
  position: absolute;
  pointer-events: none;
}

.geo-circle {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  top: 12%;
  right: -40px;
  background: oklch(0.58 0.19 20 / 0.55);
  animation: float 8s ease-in-out infinite;
}

.geo-circle.coral {
  top: 12%;
  right: -40px;
}

.geo-square {
  width: 120px;
  height: 120px;
  border-radius: 20px;
  background: oklch(0.70 0.12 230 / 0.35);
  bottom: 18%;
  left: 8%;
  transform: rotate(15deg);
  animation: float 10s ease-in-out infinite reverse;
}

.geo-circle-sm {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  top: 20%;
  left: 20%;
  background: oklch(1 0 0 / 0.2);
  animation: float 7s ease-in-out infinite 2s;
}

.geo-diamond {
  width: 80px;
  height: 80px;
  transform: rotate(45deg);
  border-radius: 16px;
  bottom: 30%;
  right: 15%;
  background: oklch(0.58 0.19 20 / 0.4);
  animation: float 9s ease-in-out infinite 1s;
}

.geo-ring {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  border: 3px solid oklch(1 0 0 / 0.18);
  top: 40%;
  left: -40px;
  animation: float 11s ease-in-out infinite 3s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(var(--r, 0deg)); }
  50% { transform: translateY(-14px) rotate(var(--r, 0deg)); }
}

/* left text */
.left-content {
  position: relative;
  z-index: 2;
  text-align: center;
}

.brand-name {
  font-size: 48px;
  font-weight: 800;
  color: oklch(1 0 0);
  letter-spacing: -0.025em;
  line-height: 1;
}

.brand-sub {
  margin-top: 8px;
  font-size: 16px;
  color: oklch(1 0 0 / 0.7);
  font-weight: 400;
  letter-spacing: 0.04em;
}

.brand-line {
  width: 40px;
  height: 3px;
  border-radius: 3px;
  background: oklch(1 0 0 / 0.5);
  margin: 18px auto;
}

.brand-desc {
  font-size: 14px;
  color: oklch(1 0 0 / 0.55);
  line-height: 1.6;
}

.left-meta {
  position: absolute;
  bottom: 20px;
  display: flex;
  gap: 10px;
  font-size: 11px;
  color: oklch(1 0 0 / 0.35);
  z-index: 2;
}

.left-meta .sep { opacity: 0.4; }

/* ---- right panel ---- */
.login-right {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--hs-bg);
  padding: 40px;
}

.login-card {
  width: 100%;
  max-width: 380px;
}

h1 {
  font-size: 28px;
  font-weight: 700;
  color: var(--hs-ink);
  margin: 0;
  letter-spacing: -0.02em;
}

.subtitle {
  color: var(--hs-ink-muted);
  font-size: 14px;
  margin: 6px 0 32px;
}

.login-form { display: flex; flex-direction: column; gap: 20px; }

.field-group { display: flex; flex-direction: column; gap: 6px; }

.field-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--hs-ink);
}

.toggle-eye {
  cursor: pointer;
  color: var(--hs-ink-muted);
  transition: color 0.15s;
}

.toggle-eye:hover { color: var(--hs-ink-secondary); }

.form-extra {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: -8px;
}

.remember {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--hs-ink-secondary);
  cursor: pointer;
}

.remember input[type="checkbox"] {
  accent-color: var(--hs-primary);
  width: 15px;
  height: 15px;
}

.forgot {
  font-size: 13px;
  color: var(--hs-ink-muted);
  text-decoration: none;
  transition: color 0.15s;
}

.forgot:hover { color: var(--hs-primary); }

/* ---- primary button ---- */
.btn-primary {
  width: 100%;
  height: 44px;
  border: none;
  border-radius: var(--hs-radius-md);
  background: var(--hs-primary);
  color: oklch(1 0 0);
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.02em;
  cursor: pointer;
  transition: background 0.2s ease, transform 0.15s ease;
  margin-top: 4px;
}

.btn-primary:hover:not(:disabled) {
  background: var(--hs-primary-hover);
  transform: translateY(-1px);
}

.btn-primary:active:not(:disabled) {
  transform: translateY(0);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* ---- loading dots ---- */
.loading-dots {
  display: inline-flex;
  gap: 5px;
  align-items: center;
}

.loading-dots i {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: oklch(1 0 0);
  animation: dotBounce 0.8s ease-in-out infinite;
}

.loading-dots i:nth-child(2) { animation-delay: 0.15s; }
.loading-dots i:nth-child(3) { animation-delay: 0.3s; }

@keyframes dotBounce {
  0%, 80%, 100% { transform: scale(0.5); opacity: 0.4; }
  40% { transform: scale(1); opacity: 1; }
}

/* ---- responsive ---- */
@media (max-width: 800px) {
  .login-page { grid-template-columns: 1fr; }
  .login-left { min-height: 180px; }
  .brand-name { font-size: 32px; }
  .login-right { padding: 24px; }
}

@media (prefers-reduced-motion: reduce) {
  .geo { animation: none !important; }
  .loading-dots i { animation: none !important; }
}
</style>
