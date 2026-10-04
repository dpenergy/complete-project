import './assets/main.css'

import { createApp } from 'vue'
import ElementPlus from 'element-plus' // 导入elementplus组件
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // 导入图标
import App from './App.vue'
import router from './router' // 导入路由（导入js文件可以省略index.js它能自动查找补全，导入.vue就不行）

const app = createApp(App)
app.use(ElementPlus) // 使用ElementPlus组件
app.use(router) // 使用路由

// 注册图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 先让组件注册渲染完成，然后再进行挂载
// 挂载一定要最后再来，如果提前挂载了，那么有些内容可能就没有成功添加上挂载上
app.mount('#app')
