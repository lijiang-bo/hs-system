import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    role: Number(localStorage.getItem('role')) || 3,
  }),
  getters: {
    isLogin: (state) => !!state.token,
    roleName: (state) => ['', '超级管理员', '管理员', '普通员工'][state.role] || '未知',
  },
  actions: {
    setLogin(payload) {
      this.token = payload.token
      this.user = payload
      this.role = payload.role || 3
      localStorage.setItem('token', payload.token)
      localStorage.setItem('user', JSON.stringify(payload))
      localStorage.setItem('role', this.role)
    },
    logout() {
      this.token = ''
      this.user = null
      this.role = 3
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
    },
  },
})
