<script setup>
import avatarUrl from '@/assets/img/avatar.jpg'
import { ref } from 'vue'
import { useRoute } from 'vue-router'

const username = ref('wudd');
const route = useRoute();// 获取当前路由信息
</script>

<template>
    <!-- 全局布局header-aside-main -->
    <div class="global-layout">
        <el-container class="outer-container">
            <!-- Header -->
            <el-header class="header">
                <el-page-header title="Exit">
                    <template #content>
                        <div class="ph-flex-center">
                            <el-avatar :size="32" shape="circle" class="avatar" v-bind:src="avatarUrl" />
                            <span class="username">{{ username }}</span>
                            <span class="title">TLIAS LEARN SYSTEM - v1.0</span>
                        </div>
                    </template>
                    <template #extra>
                        <div class="ph-flex-center">
                            <el-button type="primary" class="setting-btn">Setting</el-button>
                        </div>
                    </template>
                </el-page-header>
            </el-header>

            <!-- 内嵌 container 实现 aside + main 左右布局 -->
            <!-- 因为一旦使用了el-header或者el-footer当前el-container就会采用垂直的流向布局 -->
            <el-container class="inner-container">
                <el-aside class="aside">
                    <!-- 左侧菜单栏 -->
                    <!-- el-menu中添加router属性（默认为true）后子标签的index就是跳转的相对路径 -->
                    <!-- default-active：页面加载时默认激活的index -->
                    <el-menu class="aside-menu" router="true" :default-active="route.name">
                        <el-menu-item index="home">
                            <el-icon>
                                <Promotion />
                            </el-icon>
                            <span>Home</span>
                        </el-menu-item>
                        <!-- 如果el-sum-menu标签没有index属性，那么所有的el-sub-menu就会共享同一个展开和收起开关 -->
                        <el-sub-menu index="part-twuo">
                            <!-- 这个部分有一个插槽，插槽的名字刚好就是#title -->
                            <!-- 这里的template标签就是放入这个插槽里面的东西 -->
                            <template #title>
                                <el-icon>
                                    <Menu />
                                </el-icon>
                                <span>Clazz And Student</span>
                            </template>
                            <el-menu-item index="clazz"><el-icon>
                                    <HomeFilled />
                                </el-icon>Clazz</el-menu-item>
                            <el-menu-item index="stu"><el-icon>
                                    <UserFilled />
                                </el-icon>Student</el-menu-item>
                        </el-sub-menu>
                        <el-sub-menu index="part-three">
                            <template #title>
                                <el-icon>
                                    <Setting />
                                </el-icon>
                                <span>System Info</span>
                            </template>
                            <el-menu-item index="dept"><el-icon>
                                    <HelpFilled />
                                </el-icon>Dept</el-menu-item>
                            <el-menu-item index="emp"><el-icon>
                                    <UserFilled />
                                </el-icon>Emp</el-menu-item>
                        </el-sub-menu>
                        <el-sub-menu index="part-four">
                            <template #title>
                                <el-icon>
                                    <Finished />
                                </el-icon>
                                <span>Data</span>
                            </template>
                            <el-menu-item index="emp-report"><el-icon>
                                    <UserFilled />
                                </el-icon>Emp Info</el-menu-item>
                            <el-menu-item index="stu-report"><el-icon>
                                    <UserFilled />
                                </el-icon>Student Info</el-menu-item>
                            <el-menu-item index="log"><el-icon>
                                    <Clock />
                                </el-icon>Log</el-menu-item>
                        </el-sub-menu>
                    </el-menu>
                </el-aside>
                <el-main class="main">
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped>
/* 外层布局容器：撑满整个视口 */
.global-layout {
    height: 100%;
    /* 继承 #app 的 100% 高度 */
    width: 100%;
}

/* 最外层 el-container：纵向 flex，占满高度 */
.outer-container {
    height: 100%;
    /* 继承 global-layout 的高度 */
    width: 100%;
    flex-direction: column;
    /* 垂直排列：header 在上，内嵌 container 在下 */
}


.header {
    background-color: #4e8e2f;
    display: flex;
    align-items: center;
    flex-shrink: 0;
    /* 防止被压缩 */
}

.setting-btn {
    background-color: #4e8e2f;
    color: #b7b0a7;
    border: none;
    box-shadow: none;
}

/* 内嵌 el-container：左右布局，撑满剩余高度 */
.inner-container {
    height: calc(100% - 60px);
    /* 减去 header 的高度 */
    width: 100%;
}

/* aside 侧边栏 */
.aside {
    background-color: #545c64;
    height: 100%;
}

/* ---- aside 侧边栏 el-menu 样式 ---- */
.aside-menu {
    background-color: #545c64;
    /* 去掉 el-menu 默认右边框 */
    border-right: none;
}

/* sub-menu 标题栏背景色 */
/* 在 .aside-menu 这个容器内部，找到所有 .el-sub-menu__title 元素，给它们设置背景色和文字颜色 */
/* :deep() 的作用，穿透scope样式的边界，修改组件内部元素的样式 */
.aside-menu :deep(.el-sub-menu__title) {
    background-color: #545c64;
    color: #f5f6f6;
}

/* sub-menu 标题栏 hover */
/* hover:鼠标点击移动到上面就设置生效，当鼠标移开就设置失效 */
.aside-menu :deep(.el-sub-menu__title:hover) {
    background-color: #434a50;
    color: #f5f6f6;
}

/* el-menu-item 背景色 & 字体色 */
.aside-menu :deep(.el-menu-item) {
    background-color: #545c64;
    color: #f5f6f6;
}

/* el-menu-item hover */
.aside-menu :deep(.el-menu-item:hover) {
    background-color: #434a50;
    color: #f5f6f6;
}

/* 展开的 sub-menu 弹出层背景色 */
.aside-menu :deep(.el-sub-menu .el-menu) {
    background-color: #545c64;
}

/* ---- 点击激活（选中）状态 ---- */
/* el-menu-item和sub被点中时都会变成is-active状态然后鼠标点击其它地方时会删除，所以在is-active层添加样式设置背景即可 */
/* el-menu-item 被点击选中时，背景色保持 #434a50，鼠标移开不恢复 */
.aside-menu :deep(.el-menu-item.is-active) {
    background-color: #434a50;
    color: #f5f6f6;
}

/* 隐藏没有子菜单项的 sub-menu 右侧展开箭头 */
/* 当 .el-menu 的子元素只有 .el-sub-menu（即没有 .el-menu-item）时，隐藏箭头 */
/* :has() 是 CSS4 的关系伪类，意思是：这个元素内部是否包含指定的后代元素 */
.aside-menu :deep(.el-sub-menu:not(:has(.el-menu-item)) .el-sub-menu__icon-arrow) {
    display: none;
}

/* main 主内容区 */
.main {
    background-color: #363637;
    height: 100%;
}

.avatar {
    margin-right: 10px;
}

.username {
    margin-right: 500px;
    color: #acbda4;
}

.title {
    margin-right: 590px;
    color: #acbda4;
}



.ph-flex-center {
    /* flex流式布局，默认为row方向布局，即为flex的轴方向 */
    display: flex;
    /* align-items是交叉轴方向的居中 */
    align-items: center;
}
</style>