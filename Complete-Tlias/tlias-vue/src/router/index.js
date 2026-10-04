import { createRouter, createWebHistory } from 'vue-router'

// @/表示...src/
// 引入页面组件
// vite构建工具导入时，如果指定路径到文件夹，那么它会自动找该文件夹下面的index.js文件但是.vue没有配置的话就不行，当前是没有配置过的，所以不行
import ClazzView from '@/views/clazz/index.vue'
import DeptView from '@/views/dept/index.vue'
import EmpView from '@/views/emp/index.vue'
import HomeView from '@/views/index/index.vue'
import LogView from '@/views/log/index.vue'
import LoginView from '@/views/login/index.vue'
import StuView from '@/views/stu/index.vue'
import StuReportView from '@/views/report/stu-report.vue'
import EmpReportView from '@/views/report/emp-report.vue'
import LayoutView from '@/views/layout/index.vue'

// 定义路由规则
// 当el-menu-item被点击的时候就会跳转岛路由请求，即为当前路径下添加/index,然后组件会自动加载到router-view标签所在位置
// 当routes里面实例path路径和el-menu-item中的index属性相同是就匹配成功，就调用routes对应View组件填充到router-view标签
const routes = [
  {
    path: '/login',
    name: 'login',// 自定义router实例名称，不可重复
    component: LoginView
  },
  // 嵌套路由，外层用于App.vue内层用于Layout/index.vue
  {
    path: '/',
    name: 'homePage',
    component: LayoutView,
    redirect: '/home', // 当前端访问路径是/结束，那么匹配到/就完成router匹配了，那就重定向到首页
    children: [
      {
        // 父路径有了‘/’那么子路径就可以不加/了（加了/表示绝对路径忽视父路径的相对性）
        path: 'home',
        name: 'home',
        component: HomeView
      },
      {
        path: 'dept',
        name: 'dept',
        component: DeptView
      },
      {
        path: 'clazz',
        name: 'clazz',
        component: ClazzView
      },
      {
        path: 'emp',
        name: 'emp',
        component: EmpView
      },
      {
        path: 'log',
        name: 'log',
        component: LogView
      },
      {
        path: 'stu',
        name: 'stu',
        component: StuView
      },
      {
        path: 'stu-report',
        name: 'stu-report',
        component: StuReportView
      },
      {
        path: 'emp-report',
        name: 'emp-report',
        component: EmpReportView
      }
    ]
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),  // 使用 history 模式（URL 没有 #）
  routes  
})

export default router