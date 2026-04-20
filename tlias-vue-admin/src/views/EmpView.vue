<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addEmpApi, deleteEmpApi, getDeptListApi, getEmpPageApi, updateEmpApi } from '../api'

const loading = ref(false)
const total = ref(0)
const rows = ref([])
const deptOptions = ref([])
const selectedIds = ref([])

const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
  gender: '',
  begin: '',
  end: '',
})

const dialogVisible = ref(false)
const form = reactive({
  id: null,
  username: '',
  password: '',
  name: '',
  gender: 1,
  phone: '',
  job: 1,
  salary: 8000,
  image: '',
  entryDate: '',
  deptId: '',
  exprList: [],
})

const loadData = async () => {
  loading.value = true
  try {
    const data = await getEmpPageApi(query)
    rows.value = (data.rows || []).slice().sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0))
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const loadDept = async () => {
  deptOptions.value = await getDeptListApi()
}

const handleSearch = () => {
  query.page = 1
  loadData()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    username: '',
    password: '',
    name: '',
    gender: 1,
    phone: '',
    job: 1,
    salary: 8000,
    image: '',
    entryDate: '',
    deptId: '',
    exprList: [],
  })
}

const openAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  form.password = ''
  dialogVisible.value = true
}

const submit = async () => {
  if (!form.name || !form.username || (!form.id && !form.password) || !form.entryDate || !form.deptId) {
    return ElMessage.warning('请完整填写必填项')
  }
  if (form.id) await updateEmpApi(form)
  else await addEmpApi(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  await loadData()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该员工吗？', '提示', { type: 'warning' })
  await deleteEmpApi(id)
  ElMessage.success('删除成功')
  await loadData()
}

const removeBatch = async () => {
  if (!selectedIds.value.length) return ElMessage.warning('请先选择员工')
  await ElMessageBox.confirm(`确认批量删除 ${selectedIds.value.length} 名员工吗？`, '提示', { type: 'warning' })
  await deleteEmpApi(selectedIds.value)
  ElMessage.success('批量删除成功')
  selectedIds.value = []
  await loadData()
}

onMounted(async () => {
  await Promise.all([loadData(), loadDept()])
})
</script>

<template>
  <div class="pro-page">
    <div class="pro-card">
      <div class="pro-card-header">
        <h2 class="pro-card-title">员工管理</h2>
        <el-space>
          <el-button @click="openAdd">新增员工</el-button>
          <el-button type="danger" @click="removeBatch">批量删除</el-button>
        </el-space>
      </div>
      <div class="pro-card-body">
    <div class="pro-toolbar">
      <el-input v-model="query.name" placeholder="姓名" clearable style="width: 180px" />
      <el-select v-model="query.gender" placeholder="性别" clearable style="width: 120px">
        <el-option label="男" :value="1" />
        <el-option label="女" :value="2" />
      </el-select>
      <el-date-picker v-model="query.begin" type="date" value-format="YYYY-MM-DD" placeholder="入职开始" />
      <el-date-picker v-model="query.end" type="date" value-format="YYYY-MM-DD" placeholder="入职结束" />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading" @selection-change="(v) => selectedIds = v.map(i => i.id)">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column label="性别" width="80">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column prop="job" label="职位" width="100" />
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="salary" label="薪资" width="100" />
      <el-table-column prop="entryDate" label="入职日期" width="120" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 12px; display: flex; justify-content: flex-end;">
      <el-pagination
        v-model:current-page="query.page"
        v-model:page-size="query.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @current-change="loadData"
        @size-change="loadData"
      />
    </div>
      </div>
    </div>
  </div>

  <el-dialog v-model="dialogVisible" :title="form.id ? '编辑员工' : '新增员工'" width="640px">
    <el-form label-width="100px">
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="密码"><el-input v-model="form.password" show-password /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-select v-model="form.gender" style="width: 100%">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="职位"><el-input-number v-model="form.job" :min="1" :max="5" style="width: 100%" /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="薪资"><el-input-number v-model="form.salary" :min="0" style="width: 100%" /></el-form-item></el-col>
        <el-col :span="12">
          <el-form-item label="入职日期">
            <el-date-picker v-model="form.entryDate" value-format="YYYY-MM-DD" type="date" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="部门">
            <el-select v-model="form.deptId" style="width: 100%">
              <el-option v-for="item in deptOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12"><el-form-item label="头像URL"><el-input v-model="form.image" /></el-form-item></el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-dialog>
</template>
