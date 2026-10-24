<script setup>
import { ref, onMounted, watch } from 'vue'
import { queryStudentListApi,addStudentApi,editStudentApi,batchDeleteStudentApi } from '@/api/stu'
import { queryClazzListApi } from '@/api/clazz'
import { ElMessage,ElMessageBox } from 'element-plus'


// ------------- 条件查询学生列表 ----------------
// 初始化类型不准确，导致后端会出现类型比较异常，所以我们就初始化为null而不是空字符串
// const searchInfo = ref({ name: '', clazzName:'', gender: '', age: '', status: '', date: '', begin: '', end: '' })
const searchInfo = ref({ name: null, clazzName: null, gender: null, strAge: null, age: null, status: null, date: null, begin: null, end: null, pageSize:10, currentPage:1 })
watch(() => searchInfo.value.date, (newVal, oldVal) => {
    if (Array.isArray(newVal)) {
        searchInfo.value.begin = newVal[0]
        searchInfo.value.end = newVal[1]
    } else {
        searchInfo.value.begin = ''
        searchInfo.value.end = ''
    }
})
watch(() => searchInfo.value.strAge, (newVal, oldVal) => {
    if (newVal) {
        searchInfo.value.age = Number(newVal)
    } else {
        searchInfo.value.age = null
    }
})
const studentList = ref('')
const queryStudentList = async () => {
    // 表单校验是异步操作，需要使用await变为同步
    const valid = await searchFormRef.value?.validate().catch(() => false)

    if (!valid) {
        ElMessage.warning('请合法填写搜索信息')
        return
    }

    const res = await queryStudentListApi(searchInfo.value)
    if (res.code) {
        total.value = res.data.total
        studentList.value = res.data.studentList
    } else {
        ElMessage.error(res.msg)
    }
}
const clear = () => {
    searchInfo.value = { name: null, clazzName: null, gender: null, strAge: null, age: null, status: null, date: null, begin: null, end: null }
    queryStudentList()
}


// ------------- 表单校验 ----------------
const searchFormRef = ref(null)
const validateAge = (_rule, value, callback) => {
    // callback回调函数无论哪一种情况都需要调用，这样才能正常允许不然会卡死
    // callback必须在每条路径上都调用，否则校验Promise永远pending，await会卡死
    if (value && !/^[0-9]+$/.test(value)) {
        callback(new Error('年龄只能为数字'))
    } else {
        callback()
    }
}
const rules = {
    name:[{required: true, message: '姓名为必填项', trigger:'blur'}],
    strAge: [{ required: false, validator: validateAge, trigger: 'blur' }],
    clazzId:[{required: true, message:'班级为必选项', trigger:'blur'}],
    gender:[{required: true,message:'性别为必选项', trigger:'blur'}]
}


// ------------- 新增学生 ----------------
const isAddStudent = ref(false)
const studentInfoFormRef = ref(null)
const showStudentInfoDialog = ref(false)
const studentInfo = ref({name:null,gender:null,strAge:null,age:null,status:null,perfromance:null,clazzId:null,phone:null,homePhone:null})
const clazzList = ref(null)
watch(() => studentInfo.value.strAge,(newVal,oldVal) => {
    if(newVal) {
        studentInfo.value.age = Number(newVal)
    } else {
        studentInfo.value.age = null
    }
})
const addStudent = async () => { 
    isAddStudent.value = true

    const res = await queryClazzListApi()
    if(res.code) {
        clazzList.value = res.data
    } else {
        ElMessage.error(res.msg)
        return
    }

    showStudentInfoDialog.value = true
}


const cancel = () => {
    // 添加学生和编辑学生都会打开对话框，所以在取消的时候清除残留信息
    isAddStudent.value = false
    isEditStudent.value = false
    showStudentInfoDialog.value = false

    // 重置studentInfo并清除残留的校验信息
    studentInfo.value = {name:null,gender:null,strAge:null,age:null,status:null,performance:null,clazzId:null,phone:null,homePhone:null}
    studentInfoFormRef.value?.clearValidate()
}
const confirm = async () => {
    // confirm后可能执行：添加学生/修改学生

    // 表单校验validate()方法是异步方法，需要使用await关键字同步化
    const valid = await studentInfoFormRef.value?.validate().catch(() => false)

    if(!valid) {
        ElMessage.error('请合法填写所有必填项！')
        return
    }

    let res;

    if(isAddStudent.value) {
        res = await addStudentApi(studentInfo.value)
    }

    if(isEditStudent.value) {
        res = await editStudentApi(studentInfo.value);
    }


    if(res.code) {
        queryStudentList()
        showStudentInfoDialog.value = false
        ElMessage.success(res.msg)
    } else {
        ElMessage.error(res.msg)
    }
}



// ------------- 编辑学生信息 ----------------
const isEditStudent = ref(false) // ...这里false写错一个字母前端就直接黑屏了，nb......
const edit = async (stu) => { 
    const res = await queryClazzListApi()
    if(res.code) {
        clazzList.value = res.data
    } else {
        ElMessage.error(res.msg)
        return
    }

    studentInfo.value = JSON.parse(JSON.stringify(stu))
    studentInfo.value.strAge = String(studentInfo.value.age)

    isEditStudent.value = true
    showStudentInfoDialog.value = true
}


// ------------- 删除学生 ----------------
const deleteStudent = (name,id) => { 
    ElMessageBox.confirm(
    `你确定要删除${name}吗？`,
    'Warning',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
        // 执行删除操作  
        const ids = []
        ids.push(id)
        const res = await batchDeleteStudentApi(ids)
        if(res.code) {
            queryStudentList()
            ElMessage.success(res.msg)
        } else {
            ElMessage.error(res.msg)
        }
    })
}

// ------------- 批量删除学生 ----------------
const selectedRows = ref([])
const handleSelectionChange = (rows) => {
    selectedRows.value = rows
}
const batchDelete = () => { 
    let name = ''
    const ids = []
    for (const row of selectedRows.value) {
        name = name + ' ' +row.name
        ids.push(row.id)
    }

    ElMessageBox.confirm(
    `你确定要删除${name}吗？`,
    'Warning',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
        // 执行删除操作  
        const res = await batchDeleteStudentApi(ids)
        if(res.code) {
            queryStudentList()
            ElMessage.success(res.msg)
        } else {
            ElMessage.error(res.msg)
        }
    })
 }



// 分页条
const total = ref(0)




onMounted(() => {
    queryStudentList()
})

</script>

<template>
    <!-- {{ studentInfo }} -->
    <!-- {{ clazzList }} -->
    <!-- {{ studentList }}  -->
    <!-- {{ searchInfo }} -->

    <div class="head">
        <h1>学员管理</h1>
    </div>
    <!-- 搜索表单 -->
    <div class="search-box">
        <el-form :inline="true" :model="searchInfo" class="search-form" :rules="rules" ref="searchFormRef">
            <el-form-item label="姓名">
                <el-input v-model="searchInfo.name" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="班级">
                <el-input v-model="searchInfo.clazzName" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="性别">
                <el-select v-model="searchInfo.gender" placeholder="请选择" clearable>
                    <el-option label="男" :value="1" />
                    <el-option label="女" :value="2" />
                </el-select>
            </el-form-item>
            <el-form-item label="年龄" prop="strAge">
                <el-input v-model="searchInfo.strAge" placeholder="请输入" clearable />
            </el-form-item>
            <el-form-item label="状态">
                <el-select v-model="searchInfo.status" placeholder="请选择" clearable>
                    <el-option label="在读" value="1" />
                    <el-option label="毕业" value="2" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-date-picker v-model="searchInfo.date" type="daterange" range-separator="To" start-placeholder="入学日期"
                    end-placeholder="毕业日期" value-format="YYYY-MM-DD" :value-on-clear="''" clearable />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="queryStudentList"><el-icon>
                        <Search />
                    </el-icon>查询</el-button>
                <el-button type="danger" @click="clear"><el-icon>
                        <CircleClose />
                    </el-icon>清空</el-button>
            </el-form-item>
        </el-form>
    </div>

    <!-- 操作 -->
    <div class="operation">
        <el-button type="primary" @click="addStudent">+ 新增学员</el-button>
        <el-button type="danger" @click="batchDelete">- 批量删除</el-button>
    </div>

    <!-- 表格 -->
    <div class="student-table">
        <el-table :data="studentList" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column prop="name" label="姓名" width="120" align="center" />
            <el-table-column label="性别" width="100" align="center">
                <template #default="scope">
                    <span v-if="scope.row.gender == 1">男</span>
                    <span v-else-if="scope.row.gender == 2">女</span>
                    <span v-else>未知</span>
                </template>
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="100" align="center" />
            <el-table-column prop="clazzName" label="班级" width="180" align="center" />
            <el-table-column prop="performance" label="表现" width="100" align="center">
                <template #default="scope">
                    <span v-if="scope.row.performance == 1">正常</span>
                    <span v-else-if="scope.row.performance == 2" class="green">很好</span>
                    <span v-else-if="scope.row.performance == 3" class="orange">糟糕</span>
                    <span v-else-if="scope.row.performance == 4" class="red">警告</span>
                    <span v-else>未知</span>
                </template>
            </el-table-column>
            <el-table-column prop="entryDate" label="入学日期" width="140" align="center" />
            <el-table-column prop="status" label="状态" width="100" align="center">
                <template #default="scope">
                    <span v-if="scope.row.status == 1" class="green">在读</span>
                    <span v-else-if="scope.row.status == 2">毕业</span>
                    <span v-else>未知</span>
                </template>
            </el-table-column>
            <el-table-column label="详情" width="120" align="center">
                <template #default="scope">
                    <el-button type="info" size="small" @click="showDetails(scope.row.id)">更多<el-icon>
                            <More />
                        </el-icon></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="edit(scope.row)"><el-icon>
                            <EditPen />
                        </el-icon>修改</el-button>
                    <el-button type="danger" size="small" @click="deleteStudent(scope.row.name,scope.row.id)"><el-icon>
                            <Delete />
                        </el-icon>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 分页条 -->
    <div class="pagination">
        <el-pagination v-model:current-page="searchInfo.currentPage" v-model:page-size="searchInfo.pageSize" :page-sizes="[1,5,10, 20, 30, 40]"
            :background="background" layout="total, sizes, prev, pager, next, jumper" :total="total"
            @size-change="queryStudentList" @current-change="queryStudentList" />
    </div>

    <!-- 添加/编辑学生对话框 -->
    <div class="student-info-dialog">
        <!-- close事件在对话框因任何原因关闭时触发（遮罩/X/ESC/代码置false），统一走cancel清理残留数据 -->
        <el-dialog v-model="showStudentInfoDialog" title="学生信息" width="500" @close="cancel">
            <el-form :model="studentInfo" :rules="rules" ref="studentInfoFormRef">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="studentInfo.name" placeholder="请输入姓名"/>
                </el-form-item>
                <el-form-item label="性别"  prop="gender">
                    <el-select v-model="studentInfo.gender" placeholder="请选择">
                        <el-option label="男" :value="1" />
                        <el-option label="女" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="年龄"  prop="strAge">
                    <el-input v-model="studentInfo.strAge" placeholder="请输入年龄"/>
                </el-form-item>
                <el-form-item label="班级" prop="clazzId">
                    <el-select v-model="studentInfo.clazzId" placeholder="请选择" >
                        <el-option v-for="(clazz, index) in clazzList" :key="index"  :label="clazz.name" :value="clazz.id"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="学业状态">
                    <el-select v-model="studentInfo.status" placeholder="默认为：[在读状态]">
                        <el-option label="在读" :value="1" />
                        <el-option label="毕业" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="表现">
                    <el-select v-model="studentInfo.performance" placeholder="默认为：[正常]">
                        <el-option label="正常" :value="1" />
                        <el-option label="很好" :value="2" />
                        <el-option label="糟糕" :value="3" />
                        <el-option label="警告" :value="4" />
                    </el-select>
                </el-form-item>
                <el-form-item label="学生联系方式">
                    <el-input v-model="studentInfo.phone" placeholder="请输入"/>
                </el-form-item>
                <el-form-item label="家长联系方式">
                    <el-input v-model="studentInfo.homePhone" placeholder="请输入"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="confirm">确认</el-button>
                    <el-button @click="cancel">取消</el-button>
                </div>
            </template>
        </el-dialog>
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
    width: 100px;
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