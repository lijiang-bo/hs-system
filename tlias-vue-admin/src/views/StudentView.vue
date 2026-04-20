<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addStudentApi, deleteStudentApi, getClazzListApi, getStudentPageApi, updateStudentApi, violationStudentApi } from '../api'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const clazzOptions = ref([])

const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
  degree: '',
  clazzId: '',
})

const dialogVisible = ref(false)
const violationVisible = ref(false)
const currentId = ref(null)
const violationScore = ref(1)
const degreeMap = {
  1: '初中',
  2: '高中',
  3: '大专',
  4: '本科',
  5: '硕士',
  6: '博士',
}

const form = reactive({
  id: null,
  name: '',
  no: '',
  gender: 1,
  phone: '',
  idCard: '',
  isCollege: 1,
  address: '',
  degree: 3,
  graduationDate: '',
  clazzId: '',
  violationCount: 0,
  violationScore: 0,
})

const loadData = async () => {
  loading.value = true
  try {
    const data = await getStudentPageApi(query)
    rows.value = (data.rows || []).slice().sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0))
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const loadClazz = async () => {
  clazzOptions.value = await getClazzListApi()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    name: '',
    no: '',
    gender: 1,
    phone: '',
    idCard: '',
    isCollege: 1,
    address: '',
    degree: 3,
    graduationDate: '',
    clazzId: '',
    violationCount: 0,
    violationScore: 0,
  })
}

const openAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  resetForm()
  Object.assign(form, row)
  dialogVisible.value = true
}

const submit = async () => {
  if (!form.name || !form.phone || !form.no || !form.clazzId) return ElMessage.warning('请完整填写学员信息')
  if (form.id) await updateStudentApi(form)
  else await addStudentApi(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  await loadData()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该学员吗？', '提示', { type: 'warning' })
  await deleteStudentApi(id)
  ElMessage.success('删除成功')
  await loadData()
}

const openViolation = (id) => {
  currentId.value = id
  violationScore.value = 1
  violationVisible.value = true
}

const submitViolation = async () => {
  await violationStudentApi(currentId.value, violationScore.value)
  ElMessage.success('违纪处理成功')
  violationVisible.value = false
  await loadData()
}

onMounted(async () => {
  await Promise.all([loadData(), loadClazz()])
})
</script>

<template>
  <div class="pro-page">
    <div class="pro-card">
      <div class="pro-card-header">
        <h2 class="pro-card-title">学员管理</h2>
        <el-button type="primary" @click="openAdd">新增学员</el-button>
      </div>
      <div class="pro-card-body">
    <div class="pro-toolbar">
      <el-input v-model="query.name" placeholder="学员姓名" clearable style="width: 180px" />
      <el-select v-model="query.degree" placeholder="学历" clearable style="width: 120px">
        <el-option label="初中" :value="1" />
        <el-option label="高中" :value="2" />
        <el-option label="大专" :value="3" />
        <el-option label="本科" :value="4" />
        <el-option label="硕士" :value="5" />
        <el-option label="博士" :value="6" />
      </el-select>
      <el-select v-model="query.clazzId" placeholder="班级" clearable style="width: 160px">
        <el-option v-for="item in clazzOptions" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button type="primary" @click="query.page = 1; loadData()">查询</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading" empty-text="暂无学员数据">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="clazzName" label="班级" width="140" />
      <el-table-column prop="phone" label="手机号" width="140" />
      <el-table-column label="学历" width="100">
        <template #default="{ row }">
          <el-tag effect="light">{{ degreeMap[row.degree] || row.degree }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="违纪次数" width="100">
        <template #default="{ row }">
          <el-tag :type="row.violationCount > 0 ? 'danger' : 'success'" effect="plain">{{ row.violationCount }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="violationScore" label="违纪扣分" width="100" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="warning" @click="openViolation(row.id)">违纪处理</el-button>
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

  <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学员' : '新增学员'" width="680px">
    <el-form label-width="100px">
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="序号"><el-input v-model="form.no" /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="性别"><el-select v-model="form.gender" style="width: 100%"><el-option label="男" :value="1" /><el-option label="女" :value="2" /></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="身份证"><el-input v-model="form.idCard" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="最高学历"><el-select v-model="form.degree" style="width: 100%"><el-option label="初中" :value="1" /><el-option label="高中" :value="2" /><el-option label="大专" :value="3" /><el-option label="本科" :value="4" /><el-option label="硕士" :value="5" /><el-option label="博士" :value="6" /></el-select></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="毕业时间"><el-date-picker v-model="form.graduationDate" value-format="YYYY-MM-DD" type="date" style="width: 100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="班级"><el-select v-model="form.clazzId" style="width: 100%"><el-option v-for="item in clazzOptions" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item></el-col>
      </el-row>
      <el-form-item label="院校生源">
        <el-select v-model="form.isCollege" style="width: 100%">
          <el-option label="是" :value="1" />
          <el-option label="否" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="联系地址"><el-input v-model="form.address" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="violationVisible" title="违纪处理" width="360px">
    <el-form label-width="100px">
      <el-form-item label="扣分值">
        <el-input-number v-model="violationScore" :min="1" :max="20" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="violationVisible = false">取消</el-button>
      <el-button type="primary" @click="submitViolation">提交</el-button>
    </template>
  </el-dialog>
</template>
