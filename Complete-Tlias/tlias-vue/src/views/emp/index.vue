<script setup>
import { ref, watch, onMounted } from 'vue'
import { queryEmpApi } from "@/api/emp";
import {ElMessage} from 'element-plus'

// -------------- 响应式数据 -------------
const searchInfo = ref({ name: '', gender: '', job:'', date: '', begin: '', end: '' }) // 搜索表单项数据

const empList = ref([]) // 表格数据

const currentPage = ref(1)  // 分页条内的响应式数据
const pageSize = ref(5)
const total = ref(0)


// -------------- 函数 ---------------
// 侦听searchInfo中的date属性
watch(() => searchInfo.value.date, (newVal, oldVal) => {
    // 可选链表达式，处理null和undefined
    searchInfo.value.begin = newVal?.[0]
    searchInfo.value.end = newVal?.[1]
})

// 钩子函数
onMounted(() => search() )


// 查询
const search = async () => { 
    const res = await queryEmpApi(searchInfo.value.name, searchInfo.value.gender, searchInfo.value.job, searchInfo.value.begin,searchInfo.value.end)

    console.log(res);
    

    if(res.code) {
        empList.value = res.data.empList
        total.value = res.data.total
        console.log(empList.value);
        console.log(total.value);
    } else {
        ElMessage.error(res.msg)
    }
}

// 增删员工
const deleteEmp = () => {

}
const deleteEmps = () => { // 批量删除员工

}


// 分页条
const handleCurrentChange = () => {

}
const handleSizeChange = () => {

}

const formatTime = (_row, _column, cellValue) => {
    // 这里可以确保cellValue是一个字符串所以可以直接使用replace这个内置的String原型链上的方法
    return cellValue ? cellValue.replace('T', ' ') : ''
}

</script>

<template>
    <!-- 标题 -->
    <div class="head">
        <h1>员工管理</h1>
    </div>

    <!-- 搜索栏 -->
    <div class="search-box">
        <el-form :inline="true" :model="searchInfo" class="search-form">
            <el-form-item label="姓名">
                <el-input v-model="searchInfo.name" placeholder="输入姓名" clearable />
            </el-form-item>
            <el-form-item label="性别">
                <!-- value-on-clear：指定点击清除按钮后回写的值，默认是undefined，这里改为空字符串 -->
                <el-select v-model="searchInfo.gender" placeholder="请选择" clearable :value-on-clear="''">
                    <el-option label="男" value="1" />
                    <el-option label="女" value="2" />
                </el-select>
            </el-form-item>
            <!-- 升级为从数据库中查到种类，然后显示选择下拉列表 -->
            <el-form-item label="职位">
                <el-input v-model="searchInfo.job" placeholder="输入职位" clearable />
            </el-form-item>
            <el-form-item label="入职日期">
                <el-date-picker v-model="searchInfo.date" type="daterange" range-separator="To" start-placeholder="起始日期"
                    end-placeholder="结束日期" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="search"><el-icon>
                        <Search />
                    </el-icon>search</el-button>
            </el-form-item>
        </el-form>
    </div>

    <!-- 操作 -->
    <div class="outter-btn">
        <el-button type="primary" @click="addEmp"><el-icon>
                <CirclePlusFilled />
            </el-icon>新增员工</el-button>
        <el-button type="danger" @click="delteEmp"><el-icon>
                <RemoveFilled />
            </el-icon>批量删除</el-button>
    </div>

    <!-- 表格 -->
    <!-- {{ empList }} -->
    {{ searchInfo }}
    <div class="table">
        <el-table :data="empList" style="width: 100%">
            <el-table-column type="selection" :selectable="selectable" width="55" align="center" class="check-box"/>
            <el-table-column prop="name" label="姓名" width="120" align="center" />
            <el-table-column label="性别" width="120" align="center">
                <template #default="scope">
                    {{ scope.row.gender == 1 ? '男' : '女' }}
                </template>
            </el-table-column>
            <el-table-column prop="avatar" label="头像" width="140" align="center">
                <template #default="scope">
                    <img :src="scope.row.avatar" height="40px">
                </template>
            </el-table-column>
            <el-table-column label="部门" width="140" align="center">
                <template #default="scope">
                    {{ (scope.row.dept == null || scope.row.dept == '') ? '暂无' : scope.row.dept }}
                </template>
            </el-table-column>
            <el-table-column label="职位" width="120" align="center">
                <template #default="scope">
                    {{ (scope.row.job == null || scope.row.job == '') ? '暂时打杂' : scope.row.job }}
                </template>
            </el-table-column>
            <el-table-column prop="entryDate" label="入职日期" width="170" align="center" />
            <el-table-column prop="updateTime" label="更新时间" width="170" align="center" :formatter="formatTime"/>
            <el-table-column label="操作" min-width="120" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click=""><el-icon><EditPen /></el-icon>编辑</el-button>
                    <el-button type="danger" size="small" @click=""><el-icon><Delete /></el-icon>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 分页栏 -->
    <div class="pagination-bar">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[5, 10, 20, 50]"
            :background="primary"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
    </div>

</template>

<style scoped>
.head {
    margin-bottom: 15px;
}

.search-box {
    margin-bottom: 15px;
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
    width: 180px;
}

/* gender 下拉选择器宽度需在 .el-select 上单独设置 */
.search-form :deep(.el-select) {
    color: #66645f;
    width: 180px;
}

.search-form :deep(.el-date-editor--daterange) {
    width: 260px;
}

/* 设置输入框、下拉选择框、日期选择器的背景色 */
.search-form :deep(.el-input__wrapper),
.search-form :deep(.el-select__wrapper) {
    background-color: #181a1b;
}

.outter-btn {
    margin-bottom: 20px;
}

.table {
    margin-bottom: 10px;
}
</style>