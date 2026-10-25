<script setup>
import { ref } from "vue";
import { loginApi } from "@/api/login";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

// useRouter() 必须在 <script setup> 顶层同步调用，它直接返回路由实例本身（不是ref），无需用.value访问
const router = useRouter()

const admi = ref({ username: '', password: '' })

const login = async () => {
    const res = await loginApi(admi.value)
    if(res.code) {// 成功了
        // 1. 存储res.data(包含管理员对象和登录下发的令牌)
        // 本地存储的键和值都是字符串
        localStorage.setItem('admi',JSON.stringify(res.data))

        // 2. 跳转到主页面
        router.push('/home')

        // 3. 提示
        ElMessage.success('成功登录！')
    } else {
        ElMessage.error(res.msg)
    }
}
const cancel = () => {admi.value = { username: '', password: '' }}
</script>

<template>
    <div class="login-container">
        <div class="title">
            <h1>登录Tlias管理系统</h1>
        </div>

        <div class="form">
            <el-form :model="admi" class="demo-form-inline">
                <el-form-item label="用户名">
                    <el-input v-model="admi.username" placeholder="请输入用户名" clearable />
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="admi.password" type="password" show-password placeholder="请输入密码"/>
                </el-form-item>
            </el-form>
        </div>
        <div class="btn">
            <el-button type="primary" @click="login">登录</el-button>
            <el-button @click="cancel">取消</el-button>
        </div>
    </div>
</template>

<style scoped>
.login-container {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.title {
    margin-bottom: 20px;
}

.form {
    width: 350px;
}

.btn {
    margin-top: 10px;
}
</style>