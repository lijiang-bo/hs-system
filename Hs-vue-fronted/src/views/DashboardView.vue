<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue'
import * as echarts from 'echarts'
import {
  getEmpGenderDataApi, getEmpJobDataApi, getEmpPageApi,
  getStudentCountDataApi, getStudentDegreeDataApi, getStudentPageApi,
  getDeptListApi, getClazzPageApi,
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

let chartInstances = []

const drawCharts = async () => {
  const [jobData, genderData, clazzData, degreeData] = await Promise.all([
    getEmpJobDataApi(),
    getEmpGenderDataApi(),
    getStudentCountDataApi(),
    getStudentDegreeDataApi(),
  ])

  // bar: 员工职位分布
  const jobChart = echarts.init(jobRef.value)
  jobChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 10, right: 20, top: 20, bottom: 30 },
    xAxis: { type: 'category', data: jobData.categories || [], axisLabel: { color: '#8c8c8c', fontSize: 12 } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f0f0', type: 'dashed' } } },
    series: [{
      type: 'bar', barWidth: 28, data: jobData.values || [],
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#4f7cff' },
          { offset: 1, color: '#7b9eff' },
        ]),
      },
    }],
  })
  chartInstances.push(jobChart)

  // pie: 员工性别分布
  const genderChart = echarts.init(genderRef.value)
  genderChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['50%', '75%'],
      center: ['50%', '55%'],
      avoidLabelOverlap: false,
      label: { show: true, position: 'outside', formatter: '{b}\n{d}%', fontSize: 12, color: '#666' },
      itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
      data: genderData,
      color: ['#4f7cff', '#ff9559'],
    }],
  })
  chartInstances.push(genderChart)

  // bar: 班级人数趋势
  const clazzChart = echarts.init(clazzRef.value)
  clazzChart.setOption({
    tooltip: {},
    grid: { left: 10, right: 20, top: 20, bottom: 30 },
    xAxis: { type: 'category', data: clazzData.categories || [], axisLabel: { rotate: 25, color: '#8c8c8c', fontSize: 12 } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f0f0', type: 'dashed' } } },
    series: [{
      type: 'bar', barWidth: 28, data: clazzData.values || [],
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#67c23a' },
          { offset: 1, color: '#95d475' },
        ]),
      },
    }],
  })
  chartInstances.push(clazzChart)

  // pie: 学员学历结构
  const degreeChart = echarts.init(degreeRef.value)
  degreeChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: '72%',
      center: ['50%', '55%'],
      label: { show: true, formatter: '{b}\n{d}%', fontSize: 12, color: '#666' },
      itemStyle: { borderRadius: 3, borderColor: '#fff', borderWidth: 1.5 },
      data: degreeData,
      color: ['#4f7cff', '#67c23a', '#ff9559', '#ffc53d', '#a0d911', '#36cfc9'],
    }],
  })
  chartInstances.push(degreeChart)
}

const handleResize = () => chartInstances.forEach(c => c.resize())

onMounted(async () => {
  await loadStat()
  await drawCharts()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstances.forEach(c => c.dispose())
  chartInstances = []
})
</script>

<template>
  <div class="dash">
    <!-- Stat cards -->
    <div class="stat-grid">
      <div class="stat-card">
        <div class="stat-icon stat-icon--blue">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-body">
          <div class="stat-label">员工总数</div>
          <div class="stat-value">{{ stat.emp }}</div>
          <div class="stat-hint">在职人员</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon stat-icon--green">
          <el-icon><OfficeBuilding /></el-icon>
        </div>
        <div class="stat-body">
          <div class="stat-label">部门总数</div>
          <div class="stat-value">{{ stat.dept }}</div>
          <div class="stat-hint">组织结构稳定</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon stat-icon--orange">
          <el-icon><Reading /></el-icon>
        </div>
        <div class="stat-body">
          <div class="stat-label">班级总数</div>
          <div class="stat-value">{{ stat.clazz }}</div>
          <div class="stat-hint">本学期新增 1 个</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon stat-icon--purple">
          <el-icon><School /></el-icon>
        </div>
        <div class="stat-body">
          <div class="stat-label">学员总数</div>
          <div class="stat-value">{{ stat.student }}</div>
          <div class="stat-hint">同比增长 +5.8%</div>
        </div>
      </div>
    </div>

    <!-- Charts row 1 -->
    <div class="chart-row">
      <div class="pro-card">
        <div class="pro-card-header">
          <h2 class="pro-card-title">员工职位分布</h2>
        </div>
        <div class="pro-card-body">
          <div ref="jobRef" style="height: 300px;"></div>
        </div>
      </div>
      <div class="pro-card">
        <div class="pro-card-header">
          <h2 class="pro-card-title">员工性别分布</h2>
        </div>
        <div class="pro-card-body">
          <div ref="genderRef" style="height: 300px;"></div>
        </div>
      </div>
    </div>

    <!-- Charts row 2 -->
    <div class="chart-row">
      <div class="pro-card">
        <div class="pro-card-header">
          <h2 class="pro-card-title">班级人数趋势</h2>
        </div>
        <div class="pro-card-body">
          <div ref="clazzRef" style="height: 300px;"></div>
        </div>
      </div>
      <div class="pro-card">
        <div class="pro-card-header">
          <h2 class="pro-card-title">学员学历结构</h2>
        </div>
        <div class="pro-card-body">
          <div ref="degreeRef" style="height: 300px;"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dash {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* ---- stat grid ---- */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  background: var(--hs-surface);
  border: 1px solid var(--hs-border);
  border-radius: var(--hs-radius-md);
  padding: 20px;
  display: flex;
  align-items: flex-start;
  gap: 16px;
  transition: box-shadow 0.2s ease, border-color 0.2s ease;
}

.stat-card:hover {
  box-shadow: var(--hs-shadow-md);
  border-color: var(--hs-primary);
}

.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: var(--hs-radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.stat-icon--blue { background: oklch(0.90 0.05 230); color: var(--hs-primary); }
.stat-icon--green { background: var(--hs-success-light); color: var(--hs-success); }
.stat-icon--orange { background: oklch(0.90 0.07 70); color: oklch(0.55 0.18 60); }
.stat-icon--purple { background: oklch(0.90 0.06 300); color: oklch(0.50 0.18 290); }

.stat-body { flex: 1; min-width: 0; }
.stat-label { font-size: 13px; color: var(--hs-ink-muted); margin-bottom: 4px; }
.stat-value { font-size: 30px; font-weight: 700; color: var(--hs-ink); letter-spacing: -0.02em; font-variant-numeric: tabular-nums; }
.stat-hint { font-size: 12px; color: var(--hs-ink-muted); margin-top: 2px; }

/* ---- chart rows ---- */
.chart-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

@media (max-width: 1100px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
  .chart-row { grid-template-columns: 1fr; }
}

@media (max-width: 640px) {
  .stat-grid { grid-template-columns: 1fr; }
}
</style>
