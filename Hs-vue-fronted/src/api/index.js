import http from './http'

export const loginApi = (data) => http.post('/login', data)

export const getEmpPageApi = (params) => http.get('/emps', { params })
export const addEmpApi = (data) => http.post('/emps', data)
export const updateEmpApi = (data) => http.put('/emps', data)
export const deleteEmpApi = (ids) => http.delete('/emps', { params: { ids: ids.join(',') } })
export const getEmpByIdApi = (id) => http.get(`/emps/${id}`)
export const getEmpListApi = () => http.get('/emps/list')

export const getDeptListApi = () => http.get('/depts')
export const addDeptApi = (data) => http.post('/depts', data)
export const updateDeptApi = (data) => http.put('/depts', data)
export const deleteDeptApi = (id) => http.delete(`/depts/${id}`)

export const getClazzPageApi = (params) => http.get('/clazzs', { params })
export const getClazzListApi = () => http.get('/clazzs/list')
export const addClazzApi = (data) => http.post('/clazzs', data)
export const updateClazzApi = (data) => http.put('/clazzs', data)
export const deleteClazzApi = (id) => http.delete(`/clazzs/${id}`)
export const getClazzByIdApi = (id) => http.get(`/clazzs/${id}`)

export const getStudentPageApi = (params) => http.get('/students', { params })
export const addStudentApi = (data) => http.post('/students', data)
export const updateStudentApi = (data) => http.put('/students', data)
export const deleteStudentApi = (id) => http.delete(`/students/${id}`)
export const getStudentByIdApi = (id) => http.get(`/students/${id}`)
export const violationStudentApi = (id, score) => http.put(`/students/violation/${id}/${score}`)

export const uploadImageApi = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
}

export const getLogPageApi = (params) => http.get('/log/page', { params })

export const getDisciplinePageApi = (params) => http.get('/disciplines', { params })

export const getEmpJobDataApi = () => http.get('/report/empJobData')
export const getEmpGenderDataApi = () => http.get('/report/empGenderData')
export const getStudentCountDataApi = () => http.get('/report/studentCountData')
export const getStudentDegreeDataApi = () => http.get('/report/studentDegreeData')
