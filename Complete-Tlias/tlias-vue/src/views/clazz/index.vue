<script setup>
import { ref, onMounted, watch } from 'vue'
import { queryClazzListApi, addClazzApi, deleteClazzApi, editClazzApi } from '@/api/clazz'
import { queryHeaderListApi } from '@/api/emp'
import { queryClazzStudentApi,addStudentApi,editStudentApi,batchDeleteStudentApi } from '@/api/stu'
import { ElMessage, ElMessageBox } from 'element-plus'

const clazzList = ref([])
const queryClazzList = async () => {
    const res = await queryClazzListApi()
    if (res.code) {
        clazzList.value = res.data
    } else {
        ElMessage.error(res.msg)
    }
}

const showDialogForm = ref(false)
const dialogForm = ref({ name: '', headerId: '' })

const headerList = ref([]) // 完整的emp对象
const queryHeaderList = async () => {
    const res = await queryHeaderListApi();
    if (res.code) {
        headerList.value = res.data
    } else {
        ElMessage.error(res.msg)
    }
}

const clazzFormRef = ref(null) // ref()自动关联ref属性，进行对象传递。必须初始化为null：首次点击时表单还未挂载，若为''则clearValidate()会抛TypeError导致对话框打不开

const addClazz = () => {
    // 只需要在每次打开表单的时候清除掉表单残留的信息

    // 重置表单(重置表单的过程中也可能产生校验信息)
    dialogForm.value = { name: '', headerId: '' }

    // 首次点击的时候为null就不会执行，但是如果是初始化为''则会执行并且报错
    clazzFormRef.value?.clearValidate() // 清除残留的校验提示

    showDialogForm.value = true
}

const cancel = () => {
    showDialogForm.value = false
}
const confirm = async () => {
    // 进行表单校验
    const valid = await clazzFormRef.value.validate().catch(() => false)

    if (!valid) {
        ElMessage.error('请合法填写所有必填项！')
        return
    }

    // 将数据发送给服务器
    const res = await addClazzApi(dialogForm.value)
    if (res.code) { // 如果添加成功，就可以直接关掉对话框了
        showDialogForm.value = false
        ElMessage.success(res.msg)
        queryClazzList()
    } else { // 如果没有成功，就保留对话框内容，让用户修改
        ElMessage.error(res.msg)
    }
}

// 定义表单校验规则：定义rules，将rules绑定到el-form，在el-form-item中prop属性的指定规则
const validateClazzName = (_rule, value, callback) => {
    if (!value) {
        callback(new Error('必填项'))
    } else if (/\s/.test(value)) {
        callback(new Error('不能包含空格'))
    } else if (/[^一-龥a-zA-Z0-9]/.test(value)) {
        callback(new Error('不能包含符号'))
    } else {
        callback()
    }
}
const clazzRules = ref({
    name: [{ required: true, validator: validateClazzName, trigger: 'blur' }],
    headerId: [{ required: true, message: '必选项', trigger: 'change' }],
    status: [{ required: true, message: '必选项', trigger: 'change' }]
})



// ------------ 删除班级 -------------
const deleteClazz = async (clazz) => {
    // 消息确认框
    ElMessageBox.confirm(
        `确定删除${clazz.name}？`,
        'Warning',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            const res = await deleteClazzApi(clazz.id)
            if (res.code) {
                queryClazzList() // 重新加载班级信息
                ElMessage.success(res.msg)
            } else {
                ElMessage.error(res.msg)
            }
        })
        .catch(() => {
            ElMessage.info('取消操作！')
        })
}


// ------------ 编辑班级 -------------
const theClazz = ref('') // 点击编辑后，对应栏的clazz对象赋值给theClazz,不能直接赋值，得拷贝，直接赋值会直接改变
const showClazzInfoDialog = ref(false)
const editClazzFormRef = ref(null)
const edit = (clazz) => {
    theClazz.value = JSON.parse(JSON.stringify(clazz)) // 使用JSON对象的stringify和parse方法实现拷贝
    editClazzFormRef.value?.clearValidate()
    showClazzInfoDialog.value = true;
}
const cancelEdit = () => {
    showClazzInfoDialog.value = false;
}
const confirmEdit = async () => {
    // 将theClazz这个对象的内容提交到服务器
    const res = await editClazzApi(theClazz.value)
    if (res.code) {
        ElMessage.success(res.msg)
        queryClazzList()
        showClazzInfoDialog.value = false
    } else {
        ElMessage.error(res.msg)
    }
}

// ------------ 班级细节（学员情况） -------------
const showDetailsDialog = ref(false)
const studentList = ref([])
const currentClazz = ref({}) // 记录当前正在查看学员详情的班级，对话框内增删改学员后靠它重新拉取列表
const showClazzDetails = async (clazz) => {
    currentClazz.value = clazz

    // 获取学员信息
    const res = await queryClazzStudentApi(clazz.id)
    if (res.code) {
        studentList.value = res.data
        showDetailsDialog.value = true // 展示对话框
    } else {
        ElMessage.error(res.msg)
    }
}

// 对话框内对学员做过增/删/改之后，重新拉取当前班级的学员列表
const refreshStudentList = async () => {
    const res = await queryClazzStudentApi(currentClazz.value.id)
    if (res.code) {
        studentList.value = res.data
    } else {
        ElMessage.error(res.msg)
    }
}


// ------------- 新增学生 ----------------
const isAddStudent = ref(false)
const studentInfoFormRef = ref(null)
const showStudentInfoDialog = ref(false)
const studentInfo = ref({name:null,gender:null,strAge:null,age:null,status:null,performance:null,clazzId:null,phone:null,homePhone:null})
watch(() => studentInfo.value.strAge,(newVal,oldVal) => {
    if(newVal) {
        studentInfo.value.age = Number(newVal)
    } else {
        studentInfo.value.age = null
    }
})
const addStudent = () => {
    isAddStudent.value = true

    // 班级详情里新增学员，班级直接默认为当前班级，无需再请求班级列表（不提供其它选项，这样才合理）
    studentInfo.value = {name:null,gender:null,strAge:null,age:null,status:null,performance:null,clazzId:currentClazz.value.id,phone:null,homePhone:null}
    studentInfoFormRef.value?.clearValidate()

    showStudentInfoDialog.value = true
}


const cancelStudent = () => {
    // 添加学生和编辑学生都会打开对话框，所以在取消的时候清除残留信息
    isAddStudent.value = false
    isEditStudent.value = false
    showStudentInfoDialog.value = false

    // 重置studentInfo并清除残留的校验信息
    studentInfo.value = {name:null,gender:null,strAge:null,age:null,status:null,performance:null,clazzId:null,phone:null,homePhone:null}
    studentInfoFormRef.value?.clearValidate()
}
const confirmStudent = async () => {
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
        refreshStudentList()
        showStudentInfoDialog.value = false
        ElMessage.success(res.msg)
    } else {
        ElMessage.error(res.msg)
    }
}





// ------------ 编辑学生 -------------
const isEditStudent = ref(false)
const editStudent = (stu) => {
    studentInfo.value = JSON.parse(JSON.stringify(stu))
    studentInfo.value.strAge = String(studentInfo.value.age)

    isEditStudent.value = true
    showStudentInfoDialog.value = true
}


// ------------ 删除学生 -------------
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
            refreshStudentList()
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
            refreshStudentList()
            ElMessage.success(res.msg)
        } else {
            ElMessage.error(res.msg)
        }
    })
 }


// ------------- 表单校验 ----------------
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


// 格式化时间
const formatTime = (_row, _column, cellValue) => {
    // 这里可以确保cellValue是一个字符串所以可以直接使用replace这个内置的String原型链上的方法
    return cellValue ? cellValue.replace('T', ' ') : ''
}

// 页面挂载完成后，调用钩子函数获取班级信息
onMounted(() => {
    queryClazzList()
    queryHeaderList()
})
</script>

<template>
    <!-- {{ studentList }} -->
    <!-- {{ theClazz }} -->
    <!-- {{ clazzList }} -->
    <!-- {{ headerList }} -->
    <!-- {{ dialogForm }} -->
    <div class="head">
        <h1>班级管理</h1>
    </div>
    <!-- 操作 -->
    <div class="operation">
        <el-button type="primary" @click="addClazz">+ 开新班</el-button>
    </div>

    <!-- 表格 -->
    <div class="clazz-table">
        <el-table :data="clazzList" style="width: 100%">
            <el-table-column label="班级名称" prop="name" width="180" align="center" />
            <el-table-column label="班主任" prop="headerName" width="180" align="center" />
            <el-table-column label="开班日期" prop="createDate" width="180" align="center" />
            <el-table-column label="班级状态" width="180" align="center">
                <template #default="scope">
                    <span v-if="scope.row.status == 1" class="green">在读班级</span>
                    <span v-else-if="scope.row.status == 2" class="gray">毕业班级</span>
                    <span v-else class="red">未知状态</span>
                </template>
            </el-table-column>
            <el-table-column label="更新时间" :formatter="formatTime" prop="updateTime" width="180" align="center" />
            <el-table-column label="班级详情" align="center" width="200">
                <template #default="scope">
                    <el-button type="info" size="small" @click="showClazzDetails(scope.row)">更多<el-icon>
                            <More />
                        </el-icon></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="edit(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="deleteClazz(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 添加班级信息录入框 -->
    <div class="dialog">
        <el-dialog v-model="showDialogForm" title="添加班级" width="500">
            <!-- 这里的ref属性，没有使用v-bind是由于vue的自动匹配机制 -->
            <!-- ref属性会自动与ref()函数关联，然后同名进行DOM对象传递 -->
            <el-form ref="clazzFormRef" :model="dialogForm" :rules="clazzRules">
                <!-- prop规则校验名，要和绑定的数据名一样 -->
                <!-- 输入框绑定的数据是dialogForm.name那么这个规则校验的键名也得是name,不然就会误报 -->
                <el-form-item label="班级名称" prop="name">
                    <el-input v-model="dialogForm.name" clearable />
                </el-form-item>

                <el-form-item label="班主任" prop="headerId">
                    <el-select v-model="dialogForm.headerId" placeholder="请选择班主任" clearable :value-on-clear="''">
                        <!-- elementplus中的选项标签el-option直接添加v-for属性就能循环添加el-option了 -->
                        <el-option v-for="(header, index) in headerList" :key="index" :label="header.name"
                            :value="header.id" />
                    </el-select>
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

    <!-- 编辑班级信息，临时框 -->
    <div class="clazz-info-edit">
        <el-dialog v-model="showClazzInfoDialog" title="编辑班级信息" width="500">
            <el-form ref="editClazzFormRef" :model="theClazz" :rules="clazzRules">
                <el-form-item label="班级名称" prop="name">
                    <el-input v-model="theClazz.name" clearable />
                </el-form-item>

                <el-form-item label="班主任" prop="headerId">
                    <el-select v-model="theClazz.headerId" placeholder="请选择班主任" clearable :value-on-clear="''">
                        <el-option v-for="(header, index) in headerList" :key="index" :label="header.name"
                            :value="header.id" />
                    </el-select>
                </el-form-item>

                <el-form-item label="班级状态" prop="status">
                    <el-select v-model="theClazz.status" placeholder="请选择" clearable :value-on-clear="''">
                        <el-option label="在读班级" :value="1" />
                        <el-option label="毕业班级" :value="2" />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="confirmEdit">提交修改</el-button>
                    <el-button @click="cancelEdit">取消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>

    <!-- 班级的学员信息，对话框承载学员表格 -->
    <div class="clazz-student-info">
        <el-dialog v-model="showDetailsDialog" title="相关学员" width="1400">
            <div class="clazz-student-dialog-operation">
                <el-button type="primary" @click="addStudent">+ 新增学员</el-button>
                <el-button type="danger" @click="batchDelete">- 批量删除</el-button>
            </div>
            <el-table :data="studentList" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="name" label="姓名" width="120" align="center" />
                <el-table-column prop="age" label="年龄" width="120" align="center" />
                <el-table-column prop="phone" label="学生联系方式" width="180" align="center" />
                <el-table-column prop="homePhone" label="家长联系方式" width="180" align="center" />
                <el-table-column prop="performance" label="表现" width="100" align="center">
                    <template #default="scope">
                        <span v-if="scope.row.performance == 1">正常</span>
                        <span v-else-if="scope.row.performance == 2" class="green">很好</span>
                        <span v-else-if="scope.row.performance == 3" class="orange">糟糕</span>
                        <span v-else-if="scope.row.performance == 4" class="red">警告</span>
                        <span v-else>未知</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="120" align="center">
                    <template #default="scope">
                        <span v-if="scope.row.status == 1" class="green">在读</span>
                        <span v-else-if="scope.row.status == 2">毕业</span>
                        <span v-else class="red">未知</span>
                    </template>
                </el-table-column>
                <el-table-column prop="entryDate" label="入学日期" width="120" align="center" />
                <el-table-column label="操作" align="center">
                    <template #default="scope">
                        <el-button type="primary" size="small" @click="editStudent(scope.row)">
                            编辑
                        </el-button>
                        <el-button type="danger" size="small" @click="deleteStudent(scope.row.name,scope.row.id)">
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </div>

    <!-- 内嵌的处理班级内的学生操作的对话框 -->
    <div class="student-info-dialog">
        <!-- close事件在对话框因任何原因关闭时触发（遮罩/X/ESC/代码置false），统一走cancel清理残留数据 -->
        <el-dialog v-model="showStudentInfoDialog" title="学生信息" width="500" @close="cancelStudent">
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
                    <el-button type="primary" @click="confirmStudent">确认</el-button>
                    <el-button @click="cancelStudent">取消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scope>
.head {
    margin-bottom: 20px;
}

.operation {
    margin-bottom: 20px;
}

.clazz-student-dialog-operation {
    margin-bottom: 20px;
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
</style>