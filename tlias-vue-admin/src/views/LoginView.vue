<script setup>
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginApi } from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const remember = ref(true)
const passwordVisible = ref(false)
const sceneRef = ref(null)
const look = reactive({ x: 0, y: 0 })
const blink = reactive({ purple: false, black: false })

const form = reactive({
  username: '',
  password: '',
})

let purpleBlinkTimer = null
let blackBlinkTimer = null

const randomDelay = (min = 1500, max = 3400) => Math.floor(Math.random() * (max - min + 1)) + min

const loopBlink = (name) => {
  const timer = window.setTimeout(() => {
    blink[name] = true
    window.setTimeout(() => {
      blink[name] = false
      loopBlink(name)
    }, 180)
  }, randomDelay())
  return timer
}

const handleMouseMove = (e) => {
  if (!sceneRef.value) return
  const rect = sceneRef.value.getBoundingClientRect()
  const nx = ((e.clientX - rect.left) / rect.width - 0.5) * 2
  const ny = ((e.clientY - rect.top) / rect.height - 0.5) * 2
  look.x = Math.max(-1, Math.min(1, nx))
  look.y = Math.max(-1, Math.min(1, ny))
}

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
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  purpleBlinkTimer = loopBlink('purple')
  blackBlinkTimer = loopBlink('black')
})

onBeforeUnmount(() => {
  clearTimeout(purpleBlinkTimer)
  clearTimeout(blackBlinkTimer)
})
</script>

<template>
  <div class="login-page">
    <div class="left-panel" ref="sceneRef" @mousemove="handleMouseMove">
      <div class="brand">CareerCompass</div>
      <div class="left-noise"></div>
      <div class="characters">
        <div class="char purple" :class="{ blink: blink.purple }">
          <span class="eye" :style="{ transform: `translate(${look.x * 3}px, ${look.y * 2}px)` }"></span>
          <span class="eye" :style="{ transform: `translate(${look.x * 3}px, ${look.y * 2}px)` }"></span>
        </div>
        <div class="char black" :class="{ blink: blink.black }">
          <span class="eye" :style="{ transform: `translate(${look.x * 3}px, ${look.y * 2}px)` }"></span>
          <span class="eye" :style="{ transform: `translate(${look.x * 3}px, ${look.y * 2}px)` }"></span>
        </div>
        <div class="char orange">
          <span class="eye dark" :style="{ transform: `translate(${look.x * 2}px, ${look.y * 1.5}px)` }"></span>
          <span class="eye dark" :style="{ transform: `translate(${look.x * 2}px, ${look.y * 1.5}px)` }"></span>
        </div>
        <div class="char yellow">
          <span class="eye dark" :style="{ transform: `translate(${look.x * 2}px, ${look.y * 1.5}px)` }"></span>
          <span class="eye dark" :style="{ transform: `translate(${look.x * 2}px, ${look.y * 1.5}px)` }"></span>
          <span class="mouth"></span>
        </div>
      </div>
      <div class="footer-links">
        <span>Privacy Policy</span>
        <span>Terms of Service</span>
        <span>Contact</span>
      </div>
    </div>

    <div class="right-panel">
      <div class="login-card">
        <div class="star">*</div>
        <h2>gpc的系统</h2>
        <p>请输入你的账号信息</p>

        <el-form @keyup.enter="handleLogin">
          <el-form-item label="账号">
            <el-input v-model="form.username" placeholder="请输入账号" />
          </el-form-item>

          <el-form-item label="密码">
            <el-input
              v-model="form.password"
              :type="passwordVisible ? 'text' : 'password'"
              placeholder="请输入密码"
            >
              <template #suffix>
                <el-icon class="toggle-eye" @click="passwordVisible = !passwordVisible">
                  <View v-if="!passwordVisible" />
                  <Hide v-else />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>

          <div class="row">
            <el-checkbox v-model="remember">7天内记住我</el-checkbox>
            <a href="javascript:void(0)">忘记密码？</a>
          </div>

          <button
            class="submit-btn"
            type="button"
            :disabled="loading"
            @click="handleLogin"
          >
            <span class="label">{{ loading ? '登录中...' : '登录' }}</span>
            <span class="hover-layer">登录</span>
            <span class="arrow">-></span>
          </button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
  background: #fff;
}

.left-panel {
  position: relative;
  overflow: hidden;
  background: #cfd0d6;
}

.brand {
  position: absolute;
  top: 18px;
  left: 20px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 11px;
  z-index: 3;
}

.left-noise {
  position: absolute;
  inset: 0;
  background-image:
    radial-gradient(rgba(132, 146, 255, 0.35) 1px, transparent 1px),
    radial-gradient(rgba(255, 223, 112, 0.32) 1px, transparent 1px);
  background-size: 36px 36px, 36px 36px;
  background-position: 0 0, 18px 18px;
  opacity: 0.45;
}

.characters {
  position: absolute;
  left: 50%;
  top: 58%;
  transform: translate(-50%, -50%);
  width: 280px;
  height: 220px;
  z-index: 2;
}

.char {
  position: absolute;
}

.purple {
  width: 70px;
  height: 118px;
  left: 34px;
  top: 40px;
  border-radius: 6px;
  background: #6146f8;
  transform: skewX(-6deg);
}

.black {
  width: 62px;
  height: 108px;
  left: 88px;
  top: 58px;
  border-radius: 6px;
  background: #14161f;
}

.orange {
  width: 98px;
  height: 72px;
  left: 6px;
  top: 112px;
  border-radius: 60px 60px 10px 10px;
  background: #ff9559;
}

.yellow {
  width: 82px;
  height: 88px;
  left: 130px;
  top: 94px;
  border-radius: 24px 24px 8px 8px;
  background: #ece24f;
}

.eye {
  position: absolute;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #fff;
  top: 18px;
  transition: transform 0.2s ease;
}

.eye::after {
  content: '';
  position: absolute;
  width: 3px;
  height: 3px;
  border-radius: 50%;
  background: #111827;
  left: 2px;
  top: 2px;
}

.eye.dark {
  background: #111827;
}

.eye.dark::after {
  display: none;
}

.purple .eye:nth-child(1),
.black .eye:nth-child(1) {
  left: 22px;
}

.purple .eye:nth-child(2),
.black .eye:nth-child(2) {
  left: 42px;
}

.orange .eye:nth-child(1) {
  left: 42px;
  top: 28px;
}

.orange .eye:nth-child(2) {
  left: 60px;
  top: 28px;
}

.yellow .eye:nth-child(1) {
  left: 44px;
  top: 18px;
}

.yellow .eye:nth-child(2) {
  left: 61px;
  top: 18px;
}

.yellow .mouth {
  position: absolute;
  width: 26px;
  height: 2px;
  background: #111827;
  left: 28px;
  top: 40px;
  border-radius: 2px;
}

.blink .eye {
  height: 2px;
  border-radius: 2px;
}

.footer-links {
  position: absolute;
  left: 20px;
  bottom: 14px;
  display: flex;
  gap: 14px;
  color: rgba(17, 24, 39, 0.56);
  font-size: 10px;
  z-index: 3;
}

.right-panel {
  display: grid;
  place-items: center;
}

.login-card {
  width: 310px;
}

.star {
  text-align: center;
  color: #1f2937;
  margin-bottom: 8px;
}

h2 {
  text-align: center;
  margin: 0;
  color: #111827;
  font-size: 28px;
}

p {
  text-align: center;
  color: #6b7280;
  margin: 6px 0 24px;
  font-size: 12px;
}

:deep(.el-form-item__label) {
  font-size: 11px;
  color: #111827;
  padding-bottom: 4px;
}

:deep(.el-input__wrapper) {
  box-shadow: none;
  border-bottom: 1px solid #e5e7eb;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

:deep(.el-input__wrapper.is-focus) {
  border-bottom-color: #6d28d9;
}

.toggle-eye {
  cursor: pointer;
  color: #6b7280;
}

.row {
  margin-top: 2px;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 11px;
}

.row a {
  color: #111827;
  text-decoration: none;
}

.submit-btn {
  width: 100%;
  height: 38px;
  border: none;
  border-radius: 999px;
  background: #121827;
  color: #fff;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.submit-btn:disabled {
  opacity: 0.75;
  cursor: not-allowed;
}

.label,
.hover-layer,
.arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  transition: all 0.25s ease;
}

.label {
  left: 50%;
  transform: translate(-50%, -50%);
}

.hover-layer {
  left: -40%;
  background: #6d28d9;
  padding: 7px 16px;
  border-radius: 999px;
}

.arrow {
  right: 18px;
  opacity: 0;
}

.submit-btn:hover .label {
  transform: translate(-180%, -50%);
  opacity: 0;
}

.submit-btn:hover .hover-layer {
  left: 14px;
}

.submit-btn:hover .arrow {
  right: 14px;
  opacity: 1;
}

@media (max-width: 900px) {
  .login-page {
    grid-template-columns: 1fr;
  }

  .left-panel {
    min-height: 240px;
  }

  .right-panel {
    padding: 20px;
  }
}
</style>
