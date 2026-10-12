import request from "@/utils/request";

// 查询员工列表
// 真实请求： `/emps?name=${name}&gender=${gender}&job=${job}&begin=${begin}&end=${end}`
// 测试请求： 'emps?apifoxApiId=521625074'
export const queryEmpApi =  (name,gender,job,begin,end,pageSize,currentPage) => request.get(`/emps?name=${name}&gender=${gender}&job=${job}&begin=${begin}&end=${end}&pageSize=${pageSize}&currentPage=${currentPage}`)

export const addEmpApi = (emp) => request.post('/emps',emp)

export const batchDeleteEmpApi = (ids) => { // ids是员工id数组,单条数据和多条数据的删除都由它实现
    const params = new URLSearchParams()
    ids.forEach(id => params.append('ids',id))
    return request.delete(`/emps?${params.toString()}`)
}

export const updateEmpApi = (emp) => request.put('/emps',emp)