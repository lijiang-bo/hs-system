import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

http.interceptors.request.use((config) => {
  const authStore = useAuthStore()
  if (authStore.token) {
    config.headers.token = authStore.token
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 1) {
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || 'request fail'))
    }
    return res.data
  },
  (error) => {
    const authStore = useAuthStore()
    if (error.response?.status === 401) {
      authStore.logout()
      location.href = '/#/login'
      ElMessage.error('登录已失效，请重新登录')
    } else {
      ElMessage.error(error.message || '网络异常')
    }
    return Promise.reject(error)
  },
)

export default http
