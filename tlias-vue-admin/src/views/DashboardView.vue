<script setup>
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import {
  getEmpGenderDataApi,
  getEmpJobDataApi,
  getEmpPageApi,
  getStudentCountDataApi,
  getStudentDegreeDataApi,
  getStudentPageApi,
  getDeptListApi,
  getClazzPageApi,
} from '../api'

const stat = ref({ emp: 0, dept: 0, clazz: 0, student: 0 })
const jobRef = ref(null)
const genderRef = ref(null)
const clazzRef = ref(null)
const degreeRef = ref(null)

const loadStat = async () => {
  const [empPage, deptList, clazzPage, stuPage] = await Promise.all([
    getEmpPageApi({ page: 1, pageSize: 1 }),
    getDeptListApi(),
    getClazzPageApi({ page: 1, pageSize: 1 }),
    getStudentPageApi({ page: 1, pageSize: 1 }),
  ])
  stat.value.emp = empPage.total || 0
  stat.value.dept = deptList.length || 0
  stat.value.clazz = clazzPage.total || 0
  stat.value.student = stuPage.total || 0
}

const drawCharts = async () => {
  const [jobData, genderData, clazzData, degreeData] = await Promise.all([
    getEmpJobDataApi(),
    getEmpGenderDataApi(),
    getStudentCountDataApi(),
    getStudentDegreeDataApi(),
  ])

  const jobChart = echarts.init(jobRef.value)
  jobChart.setOption({
    tooltip: {},
    xAxis: { type: 'category', data: jobData.jobList || [] },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', barWidth: 28, data: jobData.dataList || [], itemStyle: { color: '#4f7cff' } }],
  })

  const genderChart = echarts.init(genderRef.value)
  genderChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: ['40%', '70%'], data: genderData }],
  })

  const clazzChart = echarts.init(clazzRef.value)
  clazzChart.setOption({
    tooltip: {},
    xAxis: { type: 'category', data: clazzData.jobList || [], axisLabel: { rotate: 25 } },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', barWidth: 28, data: clazzData.dataList || [], itemStyle: { color: '#67c23a' } }],
  })

  const degreeChart = echarts.init(degreeRef.value)
  degreeChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: '65%', data: degreeData }],
  })
}

onMounted(async () => {
  await loadStat()
  await drawCharts()
})
</script>

<template>
  <div class="pro-page">
    <el-row :gutter="12">
      <el-col :span="6"><el-card shadow="never"><div style="color:#667085">员工总数</div><div class="pro-stat-number">{{ stat.emp }}</div><div style="color:#98a2b3;font-size:12px;">较昨日 +2.1%</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="never"><div style="color:#667085">部门总数</div><div class="pro-stat-number">{{ stat.dept }}</div><div style="color:#98a2b3;font-size:12px;">组织结构稳定</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="never"><div style="color:#667085">班级总数</div><div class="pro-stat-number">{{ stat.clazz }}</div><div style="color:#98a2b3;font-size:12px;">本月新增 1 个</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="never"><div style="color:#667085">学员总数</div><div class="pro-stat-number">{{ stat.student }}</div><div style="color:#98a2b3;font-size:12px;">同比 +5.8%</div></el-card></el-col>
    </el-row>
    <el-row :gutter="12">
      <el-col :span="12"><el-card shadow="never"><template #header>员工职位分布</template><div ref="jobRef" style="height:320px;"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="never"><template #header>员工性别分布</template><div ref="genderRef" style="height:320px;"></div></el-card></el-col>
    </el-row>
    <el-row :gutter="12">
      <el-col :span="12"><el-card shadow="never"><template #header>班级人数趋势</template><div ref="clazzRef" style="height:320px;"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="never"><template #header>学员学历结构</template><div ref="degreeRef" style="height:320px;"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<style scoped>
.pro-page {
  position: relative;
}

.pro-page::before {
  content: '';
  position: absolute;
  left: -20px;
  top: -10px;
  width: 180px;
  height: 180px;
  background: radial-gradient(circle, rgba(54, 207, 201, 0.15), rgba(54, 207, 201, 0));
  pointer-events: none;
  z-index: 0;
}

.pro-page :deep(.el-row),
.pro-page :deep(.el-card) {
  position: relative;
  z-index: 1;
}
</style>
