import request from "@/utils/request";

export const queryClazzListApi = () => request.get('/clazzs')

export const addClazzApi= (clazz) => request.post('/clazzs',clazz) // clazzName和headerId

// 模板字符串使用${}嵌入内容
export const deleteClazzApi = (id) => request.delete(`/clazzs?id=${id}`)

export const editClazzApi = (clazz) => request.put('/clazzs',clazz)