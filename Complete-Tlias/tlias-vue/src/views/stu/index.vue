<script setup>
import { ref } from 'vue'


// 搜索表单
const searchInfo = ref({ name: '', gender: '', age: '', status: '', date: [], entryDate: '', leaveDate: '' })

const studentList = [
{ id: 1, name: '王王澜', age: 18, clazzName: '26级Java01班', phone: '11111111111', homePone: '22222222222', punishCounts: 0, performance: 1, status: 1, entryDate: '2026-10-20', leave_date: '2026-10-20' },
{ id: 2, name: '可天乐', age: 18, clazzName: '25级java01班', phone: '', homePone: '', punishCounts: 0, performance: 1, status: 1, entryDate: '2026-10-20', leave_date: '2026-10-20' },
{ id: 3, name: '张熊志', age: 18, clazzName: '25级java01班', phone: '', homePone: '', punishCounts: 0, performance: 1, status: 2, entryDate: '2026-10-20', leave_date: '2026-10-20' },
{ id: 4, name: '万维旺', age: 19, clazzName: '25级java01班', phone: '123456789101', homePone: '21215678912', punishCounts: 1, performance: 3, status: 1, entryDate: '2026-10-20', leave_date: '2026-10-20' },
{ id: 5, name: '吴佳嫁', age: 17, clazzName: '26级Java01班', phone: '213456789012', homePone: '32124354354', punishCounts: 0, performance: 2, status: 1, entryDate: '2026-10-20', leave_date: '2026-10-20' },
{ id: 6, name: '吴乐', age: 18, clazzName: '25级java01班', phone: '321323434546', homePone: '22436547678', punishCounts: 0, performance: 4, status: 1, entryDate: '2026-10-20', leave_date: '2026-10-20' },
{ id: 7, name: '王乐乐', age: 18, clazzName: '26级java02班', phone: '未添加', homePone: '未添加', punishCounts: 0, performance: 1, status: 1, entryDate: '2026-10-20', leave_date: '' },
]

const edit = (stu) => { }
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
            <el-form-item label="姓名">
                <el-input v-model="searchInfo.name" placeholder="输入姓名" clearable />
            </el-form-item>
            <el-form-item label="性别">
                <el-select v-model="searchInfo.gender" placeholder="请选择" clearable>
                    <el-option label="男" value="1" />
                    <el-option label="女" value="2" />
                </el-select>
            </el-form-item>
            <el-form-item label="年龄">
                <el-input v-model="searchInfo.age" placeholder="输入年龄" clearable />
            </el-form-item>
            <el-form-item label="状态">
                <el-select v-model="searchInfo.status" placeholder="请选择" clearable>
                    <el-option label="在读" value="1" />
                    <el-option label="毕业" value="2" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-date-picker v-model="searchInfo.date" type="daterange" range-separator="To" start-placeholder="入学日期"
                    end-placeholder="毕业日期" />
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
        <el-button type="primary" @click="">+ 新增学员</el-button>
        <el-button type="danger" @click="">- 批量删除</el-button>
    </div>

    <!-- 表格 -->
    <div class="student-table">
        <el-table :data="studentList" style="width: 100%">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="name" label="姓名" width="120" align="center" />
            <el-table-column prop="age" label="年龄" width="120" align="center" />
            <el-table-column prop="clazzName" label="班级" width="200" align="center" />
            <el-table-column prop="performance" label="表现" width="180" align="center">
                <template #default="scope">
                    <span v-if="scope.row.performance == 1">正常</span>
                    <span v-else-if="scope.row.performance == 2" class="green">很好</span>
                    <span v-else-if="scope.row.performance == 3" class="orange">糟糕</span>
                    <span v-else-if="scope.row.performance == 4" class="red">警告</span>
                    <span v-else>未知</span>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="180" align="center">
                <template #default="scope">
                    <span v-if="scope.row.status == 1" class="green">在读</span>
                    <span v-else-if="scope.row.status == 2" class="red">毕业</span>
                    <span v-else>未知</span>
                </template>
            </el-table-column>
            <el-table-column label="详情" width="180" align="center">
                <template #default="scope">
                    <el-button type="info" size="small" @click="showDetails(scope.row.id)">更多<el-icon>
                            <More />
                        </el-icon></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="edit(scope.row)"><el-icon><EditPen /></el-icon>修改</el-button>
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