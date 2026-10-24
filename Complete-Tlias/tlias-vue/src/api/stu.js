import request from "@/utils/request";

// axios能够自动将searchInfo序列化，并以简单参数的方式拼接到/students后面
// 例如：/students?name=wudd&age=21&time=2026-01-22
// 这样也实现了解耦
export const queryStudentListApi = (searchInfo) => request.get('/students', { params: searchInfo })

export const queryClazzStudentApi = (id) => request.get(`/students/${id}`) // 采用路径参数的方式查询班级学生

export const addStudentApi = (studentInfo) => request.post('/students',studentInfo)

// 直接用批量删除替代掉单个删除
// export const deleteStudentApi = (id) => request.delete(`/students/${id}`)
export const batchDeleteStudentApi = (ids) => {
    const params = new URLSearchParams() // 内置类
    ids.forEach(id => params.append('ids',id))
    return request.delete(`/students?${params.toString()}`)
}

export const editStudentApi = (studentInfo) => request.put('/students',studentInfo)