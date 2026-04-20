<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addClazzApi, deleteClazzApi, getClazzPageApi, getEmpListApi, updateClazzApi } from '../api'

const loading = ref(false)
const rows = ref([])
const total = ref(0)
const empOptions = ref([])
const subjectMap = {
  1: 'Java',
  2: '前端',
  3: '大数据',
  4: 'Python',
}

const query = reactive({
  page: 1,
  pageSize: 10,
  name: '',
  begin: '',
  end: '',
})

const dialogVisible = ref(false)
const form = reactive({
  id: null,
  name: '',
  room: '',
  beginDate: '',
  endDate: '',
  masterId: '',
  subject: 1,
})

const loadData = async () => {
  loading.value = true
  try {
    const data = await getClazzPageApi(query)
    rows.value = (data.rows || []).slice().sort((a, b) => (Number(a.id) || 0) - (Number(b.id) || 0))
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const loadEmp = async () => {
  empOptions.value = await getEmpListApi()
}

const resetForm = () => {
  Object.assign(form, { id: null, name: '', room: '', beginDate: '', endDate: '', masterId: '', subject: 1 })
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
  if (!form.name || !form.room || !form.beginDate || !form.endDate || !form.masterId) {
    return ElMessage.warning('请完整填写班级信息')
  }
  if (form.id) await updateClazzApi(form)
  else await addClazzApi(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  await loadData()
}

const remove = async (id) => {
  await ElMessageBox.confirm('确认删除班级吗？', '提示', { type: 'warning' })
  await deleteClazzApi(id)
  ElMessage.success('删除成功')
  await loadData()
}

onMounted(async () => {
  await Promise.all([loadData(), loadEmp()])
})
</script>

<template>
  <div class="pro-page">
    <div class="pro-card">
      <div class="pro-card-header">
        <h2 class="pro-card-title">班级管理</h2>
        <el-button type="primary" @click="openAdd">新增班级</el-button>
      </div>
      <div class="pro-card-body">
    <div class="pro-toolbar">
      <el-input v-model="query.name" placeholder="班级名" style="width: 180px" clearable />
      <el-date-picker v-model="query.begin" type="date" value-format="YYYY-MM-DD" placeholder="开班开始" />
      <el-date-picker v-model="query.end" type="date" value-format="YYYY-MM-DD" placeholder="开班结束" />
      <el-button type="primary" @click="query.page = 1; loadData()">查询</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading" empty-text="暂无班级数据">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="班级名称" width="160" />
      <el-table-column prop="room" label="教室" width="120" />
      <el-table-column prop="masterName" label="班主任" width="120" />
      <el-table-column label="学科" width="110">
        <template #default="{ row }">
          <el-tag effect="light">{{ subjectMap[row.subject] || `学科${row.subject}` }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === '未开班' ? 'info' : row.status === '在读中' ? 'success' : 'warning'" effect="light">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="beginDate" label="开课日期" width="120" />
      <el-table-column prop="endDate" label="结课日期" width="120" />
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

  <el-dialog v-model="dialogVisible" :title="form.id ? '编辑班级' : '新增班级'" width="580px">
    <el-form label-width="100px">
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="班级名称"><el-input v-model="form.name" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="教室"><el-input v-model="form.room" /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="班主任">
            <el-select v-model="form.masterId" style="width: 100%">
              <el-option v-for="item in empOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12"><el-form-item label="学科"><el-input-number v-model="form.subject" :min="1" style="width: 100%" /></el-form-item></el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="开课日期"><el-date-picker v-model="form.beginDate" value-format="YYYY-MM-DD" type="date" style="width: 100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="结课日期"><el-date-picker v-model="form.endDate" value-format="YYYY-MM-DD" type="date" style="width: 100%" /></el-form-item></el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-dialog>
</template>
