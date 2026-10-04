// 自定义axios实例，简化data数据获取，统一管理URL维护
import axios from 'axios'

// request发起的请求都是需要请求服务器端的请求
// 创建自定义axios实例,创建方法是axios.create({参数配置，如果没有配置就按照默认配置，一般配置baseURL和timeout})
const request = axios.create({
    // baseURL: 'https://m1.apifoxmock.com/m1/8736517-8523696-default',
    // baseURL: 'http://localhost:8080',
    baseURL: '/api', // 交给代理服务器重新指向目标服务器
    timeout: 600000
})

// axios实例对象有两套拦截器：interceptors.request（请求发出前拦截）和interceptors.response（响应返回后）
// 拦截器的use(onFulFilled, onRejected)方法是注册拦截逻辑接收两个函数变量,onFulFilled(HTTP 响应状态码为2xx时触发，非2xx时触发onRejected回调方法)
request.interceptors.response.use(
    (response) => { // 成功回调
        return response.data // 这样的结果就是服务器响应的结果了
    },
    (error) => { // 失败回调
        return Promise.reject(error)
    }
)

// 写了这行外界才可以使用import导入request实例来使用
// 如果不写这行，request 就只是 request.js 文件内部的私有变量，外部完全无法访问，整个封装就失去了意义
// export default是一个组合语法，一个js文件只能有一个export default
export default request