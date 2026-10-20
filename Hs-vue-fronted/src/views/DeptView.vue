<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addDeptApi, deleteDeptApi, getDeptListApi, updateDeptApi } from '../api'
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = reactive({ id: null, name: '' })

const formatTime = (dt) => {
  if (!dt) return '-'
  return dt.replace('T', ' ').substring(0, 19)
}

const loadData = async () => {
  loading.value = true
  try {
    const data = await getDeptListApi()
    list.value = (data || []).slice().sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0))
  } finally {
    loading.value = false
  }
}

const openAdd = () => {
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

const openEdit = (row) => {
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

const submit = async () => {
  if (!form.name) return ElMessage.warning('请输入部门名称')
  if (form.id) await updateDeptApi(form)
  else await addDeptApi({ name: form.name })
  ElMessage.success('操作成功')
  dialogVisible.value = false
  await loadData()
}

const remove = async (row) => {
  await ElMessageBox.confirm(`确认删除部门「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteDeptApi(row.id)
  ElMessage.success('删除成功')
  await loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="pro-page">
    <div class="pro-card">
      <div class="pro-card-header">
        <h2 class="pro-card-title">部门管理</h2>
        <el-space>
          <el-button v-if="authStore.role !== 3" type="primary" @click="openAdd">新增部门</el-button>
          <el-button @click="loadData">刷新</el-button>
        </el-space>
      </div>
      <div class="pro-card-body">
    <el-table :data="list" border stripe v-loading="loading" empty-text="暂无部门数据" style="width: 100%">
      <el-table-column type="index" label="序号" width="65" />
      <el-table-column prop="name" label="部门名称" min-width="200" />
      <el-table-column label="创建时间" min-width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="状态" min-width="90">
        <template #default>
          <el-tag type="success" effect="light">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="authStore.role !== 3" label="操作" width="160">
        <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
      </div>
    </div>
  </div>

  <el-dialog v-model="dialogVisible" :title="form.id ? '编辑部门' : '新增部门'" width="420px">
    <el-form label-width="90px">
      <el-form-item label="部门名称">
        <el-input v-model="form.name" placeholder="请输入部门名称" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-dialog>
</template>
