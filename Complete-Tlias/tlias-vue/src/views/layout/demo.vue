<!-- 看看能不能自己实现这样的页面布局 -->
<script setup>
</script>

<template>
    <!-- 全局布局 -->
    <div class="global-layout">
        <el-container class="outer-container">
            <!-- header部分 -->
            <el-header class="header">
                Header
            </el-header>

            <el-container class="inner-container">
                <el-aside width="200px" class="aside">
                    Aside
                </el-aside>
                <el-main class="main">
                    Main
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scope>
/* 全局布局继承父容器#app的所有界面空间 */
.global-layout {
    width: 100%;
    height: 100%;
}

/* 外层容器继承全局div的空间 */
.outer-container {
    width: 100%;
    height: 100%;
}

/* elementplus有内置的css样式，通过全局注入的方式加载进来，也就是最后加载进来会覆盖掉你的scope设置的样式，并且优先级高于自己设置的css */
/* header 区域 */
/* 头部分固定60个像素点避免被压缩 */
.header {
    /* height: 60px; */
    /* 使用height */
    --el-header-height: 200px
}

/* 内层container再继承父空间所有剩下的空间 */
.inner-container {
    width: calc(100% - 60px);
    height: 100%;
}

/* 固定内层container中的左侧部分 */
/* aside 侧边栏 */
.aside {
    background-color: #262727;
    height: 100%;
}

/* main 主内容区 */
.main {
    background-color: #363637;
    height: 100%;
}

</style>