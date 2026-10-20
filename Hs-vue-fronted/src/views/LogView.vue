<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getLogPageApi } from '../api'

const loading = ref(false)
const total = ref(0)
const rows = ref([])

const query = reactive({
  page: 1,
  pageSize: 10,
})

const formatTime = (dt) => {
  if (!dt) return '-'
  return dt.replace('T', ' ').substring(0, 19)
}

const loadData = async () => {
  loading.value = true
  try {
    const data = await getLogPageApi(query)
    rows.value = data.rows || []
    total.value = data.total || 0
  } finally {
    loading.value = false
  }
}

const formatJsonLike = (value) => {
  if (!value) return '-'
  if (typeof value !== 'string') return String(value)
  try {
    return JSON.stringify(JSON.parse(value), null, 2)
  } catch {
    return value
  }
}

onMounted(loadData)
</script>

<template>
  <div class="pro-page">
    <div class="pro-card">
      <div class="pro-card-header">
        <h2 class="pro-card-title">日志管理</h2>
        <el-space>
          <el-button @click="loadData">刷新</el-button>
        </el-space>
      </div>
      <div class="pro-card-body">
        <el-table :data="rows" border stripe v-loading="loading" empty-text="暂无日志数据" style="width: 100%">
          <el-table-column prop="operateEmpId" label="操作人ID" width="100" />
          <el-table-column prop="operateEmpName" label="操作人" width="120" />
          <el-table-column label="操作时间" width="180">
            <template #default="{ row }">{{ formatTime(row.operateTime) }}</template>
          </el-table-column>
          <el-table-column prop="className" label="类名" min-width="220" show-overflow-tooltip />
          <el-table-column prop="methodName" label="方法名" width="140" show-overflow-tooltip />
          <el-table-column prop="costTime" label="耗时(ms)" width="100" />
          <el-table-column label="方法参数" min-width="260">
            <template #default="{ row }">
              <el-tooltip :content="formatJsonLike(row.methodParams)" placement="top" :show-after="300">
                <span class="ellipsis">{{ formatJsonLike(row.methodParams) }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="返回值" min-width="260">
            <template #default="{ row }">
              <el-tooltip :content="formatJsonLike(row.returnValue)" placement="top" :show-after="300">
                <span class="ellipsis">{{ formatJsonLike(row.returnValue) }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <div class="pager-wrap">
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
</template>

<style scoped>
.pager-wrap {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.ellipsis {
  display: inline-block;
  max-width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
