<script setup>
import { ref } from 'vue'


// 搜索表单
const searchInfo = ref({ name: '', gender: '', age: '', status: '', date: [], entryDate: '', leaveDate: '' })

const studentList = [
    {admi:'guanlyiyuan1',operation:'新增员工：意林天',type:1,time:'2026-10-20 23:16:32'},
    {admi:'guanlyiyuan1',operation:'删除员工：王丽丽',type:2,time:'2026-10-20 23:16:51'},
    {admi:'guanlyiyuan1',operation:'编辑员工：王丽丽',type:3,time:'2026-10-20 23:17:51'}
]

const deleteStu = (id) => { }



// 分页条
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(10)

</script>

<template>
    <div class="head">
        <h1>学员管理</h1>
    </div>
    <!-- 搜索表单 -->
    <div class="search-box">
        <el-form :inline="true" :model="searchInfo" class="search-form">
            <el-form-item label="管理员">
                <el-input v-model="searchInfo.name" placeholder="输入管理员" clearable />
            </el-form-item>
            <el-form-item>
                <el-date-picker v-model="searchInfo.date" type="daterange" range-separator="To" start-placeholder="起始"
                    end-placeholder="结束" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="search"><el-icon>
                        <Search />
                    </el-icon>查询</el-button>
                <el-button type="danger" @click="clear"><el-icon>
                        <CircleClose />
                    </el-icon>清空</el-button>
            </el-form-item>
        </el-form>
    </div>

    <div class="operation">
        <el-button type="danger" @click="">- 批量删除</el-button>
    </div>

    <!-- 表格 -->
    <div class="student-table">
        <el-table :data="studentList" style="width: 100%">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="admi" label="管理员" width="120" align="center" />
            <el-table-column prop="operation" label="操作" width="200" align="center" />
            <el-table-column prop="type" label="操作类型" width="200" align="center">
                <template #default="scope">
                    <span v-if="scope.row.type==1" class="green">新增</span>
                    <span v-else-if="scope.row.type==2" class="red">删除</span>
                    <span v-else-if="scope.row.type==3" class="orange">修改</span>
                    <span v-else>未知</span>
                </template>
            </el-table-column>
            <el-table-column prop="time" label="操作时间" width="200" align="center" />
            
            <el-table-column label="详情" width="180" align="center">
                <template #default="scope">
                    <el-button type="info" size="small" @click="showDetails(scope.row.id)">更多<el-icon>
                            <More />
                        </el-icon></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="danger" size="small" @click="deleteStu(scope.row.id)"><el-icon><Delete /></el-icon>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 分页条 -->
    <div class="demonstration">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 30, 40]"
            :background="background" layout="total, sizes, prev, pager, next, jumper" :total="total"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

</template>

<!-- 是scoped不是scope，不要写错了哦 -->
<style scoped>
.head {
    margin-bottom: 20px;
}

.search-box {
    margin-bottom: 10px;
}

/* 设置label文字样式 */
.search-form :deep(.el-form-item__label) {
    font-weight: 700;
    font-size: 16px;
    color: #e2e0dc;
}

/* 设置表单中的文字大小 */
.search-form :deep(.el-input .el-date-picker .el-select) {
    font-size: 16px;
}

/* 表单项内输入控件宽度 */
.search-form :deep(.el-input) {
    font-size: 16px;
    width: 140px;
}

/* gender 下拉选择器宽度需在 .el-select 上单独设置 */
.search-form :deep(.el-select) {
    color: #66645f;
    width: 100px;
}

.search-form :deep(.el-date-editor--daterange) {
    width: 260px;
}

/* 设置输入框、下拉选择框、日期选择器的背景色 */
.search-form :deep(.el-input__wrapper),
.search-form :deep(.el-select__wrapper) {
    background-color: #181a1b;
}

.operation {
    margin-bottom: 15px;
}


/* 表现、状态栏的字体颜色 */
.green {
    color: #67c23a;
}

.orange {
    color: #e6a23c;
}

.red {
    color: #f56c6c;
}

.gray {
    color: #909399;
}


.student-table {
    margin-bottom: 20px;
}
</style>