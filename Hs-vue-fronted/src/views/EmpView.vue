<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '../stores/auth'
import { addEmpApi, deleteEmpApi, getDeptListApi, getEmpByIdApi, getEmpPageApi, updateEmpApi } from '../api'

const authStore = useAuthStore()
const loading = ref(false)
const total = ref(0)
const rows = ref([])
const deptOptions = ref([])
const selectedIds = ref([])

// 根据当前角色决定显示的标签页列表
const tabItems = computed(() => {
  if (authStore.role === 1) {
    return [
      { label: '普通员工', value: '3' },
      { label: '管理员', value: '2' },
      { label: '超级管理员', value: '1' },
    ]
  }
  // 管理员只能管理普通员工，普通员工只能查看自己角色的数据
  return [{ label: '普通员工', value: String(authStore.role === 2 ? 3 : authStore.role) }]
})

// 角色标签页：管理员默认只能查看普通员工(role=3)
const roleTab = ref(String(authStore.role === 2 ? 3 : authStore.role))

const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
  gender: '',
  begin: '',
  end: '',
  role: authStore.role === 2 ? 3 : authStore.role,
})

watch(roleTab, (val) => {
  query.role = Number(val)
  query.page = 1
  loadData()
})

const dialogVisible = ref(false)
const form = reactive({
  id: null,
  username: '',
  password: '',
  name: '',
  gender: null,
  phone: '',
  job: null,
  role: 3,
  salary: 0,
  image: '',
  entryDate: '',
  deptId: '',
  exprList: [],
})

// 新增工作经历的临时表单
const newExpr = reactive({
  company: '',
  job: '',
  begin: '',
  end: '',
})

const addExpr = () => {
  if (!newExpr.company || !newExpr.job || !newExpr.begin || !newExpr.end) {
    return ElMessage.warning('请完整填写工作经历')
  }
  form.exprList.push({ ...newExpr })
  Object.assign(newExpr, { company: '', job: '', begin: '', end: '' })
}

const removeExpr = (index) => {
  form.exprList.splice(index, 1)
}

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
    gender: null,
    phone: '',
    job: null,
    role: 3, // 新增员工默认普通员工
    salary: 0,
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

const openEdit = async (row) => {
  resetForm()
  const emp = await getEmpByIdApi(row.id)
  // 后端不返回密码，删除空密码字段避免提交到后端
  delete emp.password
  Object.assign(form, emp)
  form.password = undefined
  dialogVisible.value = true
}

const submitting = ref(false)

const submit = async () => {
  if (submitting.value) return
  if (!form.name || !form.username || (!form.id && !form.password) || form.gender === null || form.job === null || !form.entryDate || !form.deptId) {
    return ElMessage.warning('请完整填写必填项')
  }
  submitting.value = true
  try {
    if (form.id) await updateEmpApi(form)
    else await addEmpApi(form)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    await loadData()
  } catch {
    // 错误已在 axios 响应拦截器中统一提示
  } finally {
    submitting.value = false
  }
}

const handleUploadSuccess = (response) => {
  if (response.code === 1 && response.data) {
    form.image = response.data
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该员工吗？', '提示', { type: 'warning' })
  await deleteEmpApi([id])
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
          <el-button v-if="authStore.role !== 3" @click="openAdd">新增员工</el-button>
          <el-button v-if="authStore.role === 1" type="danger" @click="removeBatch">批量删除</el-button>
        </el-space>
      </div>
      <div class="pro-card-body">
    <el-tabs v-model="roleTab" type="border-card" class="pro-tab-section">
      <el-tab-pane v-for="tab in tabItems" :key="tab.value" :label="tab.label" :name="tab.value" />
    </el-tabs>
    <div class="pro-toolbar">
      <el-input v-model="query.name" placeholder="姓名" clearable class="pro-search-input" />
      <el-select v-model="query.gender" placeholder="性别" clearable class="pro-search-select">
        <el-option label="男" :value="1" />
        <el-option label="女" :value="2" />
      </el-select>
      <el-date-picker v-model="query.begin" type="date" value-format="YYYY-MM-DD" placeholder="入职开始" />
      <el-date-picker v-model="query.end" type="date" value-format="YYYY-MM-DD" placeholder="入职结束" />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading" style="width: 100%" @selection-change="(v) => selectedIds = v.map(i => i.id)">
      <el-table-column v-if="authStore.role === 1" type="selection" width="50" />
      <el-table-column type="index" label="序号" width="65" />
      <el-table-column prop="name" label="姓名" min-width="100" />
      <el-table-column label="性别" min-width="70">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" min-width="130" />
      <el-table-column prop="deptName" label="部门" min-width="110" />
      <el-table-column label="职位" min-width="100">
        <template #default="{ row }">{{ ['', '班主任', '讲师', '学工主管', '教研主管', '咨询师', '管理人员'][row.job] || row.job }}</template>
      </el-table-column>
      <el-table-column v-if="authStore.role !== 3" label="角色" min-width="90">
        <template #default="{ row }">{{ ['', '超级管理员', '管理员', '普通员工'][row.role] || '未知' }}</template>
      </el-table-column>
      <el-table-column v-if="authStore.role !== 3" prop="salary" label="薪资" min-width="90" />
      <el-table-column prop="entryDate" label="入职日期" min-width="110" />
      <el-table-column v-if="authStore.role !== 3" label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pro-pagination">
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
        <el-col :span="12"><el-form-item label="用户名"><el-input v-model="form.username" :disabled="!!form.id" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="密码"><el-input v-model="form.password" :placeholder="form.id ? '不修改请留空' : ''" show-password /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-select v-model="form.gender" class="pro-form-full">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="角色"><el-select v-model="form.role" class="pro-form-full">
          <!-- 超级管理员：新增和编辑均可选择所有角色 -->
          <template v-if="authStore.role === 1">
            <el-option label="超级管理员" :value="1" />
            <el-option label="管理员" :value="2" />
          </template>
          <!-- 管理员：编辑时才允许看到管理员选项 -->
          <template v-else-if="authStore.role === 2 && form.id && form.role === 2">
            <el-option label="管理员" :value="2" />
          </template>
          <el-option label="普通员工" :value="3" />
        </el-select></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="职位"><el-select v-model="form.job" class="pro-form-full">
          <el-option label="班主任" :value="1" />
          <el-option label="讲师" :value="2" />
          <el-option label="学工主管" :value="3" />
          <el-option label="教研主管" :value="4" />
          <el-option label="咨询师" :value="5" />
          <el-option label="管理人员" :value="6" />
        </el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="薪资"><el-input-number v-model="form.salary" :min="0" class="pro-form-full" /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="入职日期">
            <el-date-picker v-model="form.entryDate" value-format="YYYY-MM-DD" type="date" class="pro-form-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门">
            <el-select v-model="form.deptId" class="pro-form-full">
              <el-option v-for="item in deptOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="头像">
            <el-upload
              :action="'/api/upload'"
              :headers="{ token: authStore.token }"
              :show-file-list="false"
              :on-success="handleUploadSuccess"
              :before-upload="(f) => { if (f.size > 10 * 1024 * 1024) { ElMessage.error('图片不能超过10MB'); return false } }"
            >
              <template #trigger>
                <el-button type="primary">选择图片</el-button>
              </template>
              <template v-if="form.image" #tip>
                <div style="margin-top: 8px">
                  <el-image :src="form.image" class="pro-avatar-thumb" />
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider />
      <el-form-item label="工作经历">
        <div style="width: 100%">
          <div class="pro-expr-form">
            <el-input v-model="newExpr.company" placeholder="公司名称" style="width: 160px" size="small" />
            <el-input v-model="newExpr.job" placeholder="职位" style="width: 110px" size="small" />
            <el-date-picker v-model="newExpr.begin" value-format="YYYY-MM-DD" type="date" placeholder="开始时间" style="width: 140px" size="small" />
            <el-date-picker v-model="newExpr.end" value-format="YYYY-MM-DD" type="date" placeholder="结束时间" style="width: 140px" size="small" />
            <el-button type="primary" size="small" @click="addExpr">添加</el-button>
          </div>
          <template v-if="form.exprList && form.exprList.length">
            <el-table :data="form.exprList" size="small" border style="width: 100%">
              <el-table-column prop="company" label="公司名称" min-width="140" />
              <el-table-column prop="job" label="职位" min-width="100" />
              <el-table-column prop="begin" label="开始时间" width="110" />
              <el-table-column prop="end" label="结束时间" width="110" />
              <el-table-column label="操作" width="60">
                <template #default="{ $index }">
                  <el-button link type="danger" size="small" @click="removeExpr($index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
          <span v-else class="pro-empty-hint">暂无工作经历</span>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :disabled="submitting" @click="submit">确定</el-button>
    </template>
  </el-dialog>
</template>
