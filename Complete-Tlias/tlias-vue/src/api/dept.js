import request from "@/utils/request";

// 查询所有部门
// js(或者说ES)中要避免使用get风格命名，因为http请求已经占用get语义
const queryDeptListApi = () => request.get('/depts')

// 根据id查询部门
// 使用模板字符串
const queryByIdApi = (id) => request.get(`/depts/${id}`)

// 增加部门
const addDeptApi = (dept) => request.post('/depts',dept) // 第二个参数就是Json对象，注意返回值是需要处理的

// 编辑部门
const editDeptApi = (dept) => request.put('/depts',dept)

// 删除部门



export {queryDeptListApi,queryByIdApi,editDeptApi,addDeptApi}