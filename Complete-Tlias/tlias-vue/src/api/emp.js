import request from "@/utils/request";

// 查询员工列表
// 真实请求： `/emps?name=${name}&gender=${gender}&job=${job}&begin=${begin}&end=${end}`
// 测试请求： 'emps?apifoxApiId=521625074'
const queryEmpApi =  (name,gender,job,begin,end) => request.get(`/emps?name=${name}&gender=${gender}&job=${job}&begin=${begin}&end=${end}`)



export {queryEmpApi}