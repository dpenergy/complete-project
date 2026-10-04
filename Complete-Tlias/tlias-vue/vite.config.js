import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      '/api':{ // 拦截路径的匹配规则
        target: 'http://localhost:8080',
        // target: 'https://m1.apifoxmock.com/m1/8736517-8523696-default', // 目标服务器
        secure: false, // 允许无效证书
        changeOrigin: true, // 修改请求头中的host为目标服务器的地址
        rewrite:(path) => path.replace(/^\/api/,'') // 去掉资源访问路径中的/api
        // /^\/api/这个式javaScript模式的正则表达式
        // /..../这个是定界符
        // ^这个是行首锚点，强制必须从次开始匹配
        // \转义字符，\/表示/
      }
    }
  }
})

