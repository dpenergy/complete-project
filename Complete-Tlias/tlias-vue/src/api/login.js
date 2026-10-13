import request from "@/utils/request";

// 前端的admi包含usernmae和password
export const loginApi = (admi) => request.post('/login',admi)