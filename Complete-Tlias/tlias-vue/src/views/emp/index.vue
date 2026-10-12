<script setup>
import { ref, watch, onMounted } from 'vue'
import { queryEmpApi, addEmpApi, batchDeleteEmpApi, updateEmpApi} from "@/api/emp";
import { queryDeptListApi } from '@/api/dept';
import { queryJobListApi } from '@/api/job';
import { ElMessage, ElMessageBox } from 'element-plus'


// ------------ 搜索表单 ------------ 
// ------------ 员工列表查询 ------------ 
const searchInfo = ref({ name: '', gender: '', job: '', date: '', begin: '', end: '' ,pageSize:5 ,currentPage:1}) // 搜索表单项
// 侦听searchInfo中的date属性
watch(() => searchInfo.value.date, (newVal, oldVal) => {
    // 可选链表达式，处理null和undefined
    if (Array.isArray(newVal)) {
        searchInfo.value.begin = newVal[0]
        searchInfo.value.end = newVal[1]
    } else {
        searchInfo.value.begin = ''
        searchInfo.value.end = ''
    }
})

const empList = ref([]) // 表格
const search = async () => {
    const res = await queryEmpApi(searchInfo.value.name, searchInfo.value.gender, searchInfo.value.job, searchInfo.value.begin, searchInfo.value.end, searchInfo.value.pageSize, searchInfo.value.currentPage)

    // console.log(res);

    if (res.code) {
        empList.value = res.data.empList
        empList.value.forEach(emp => {
            if(emp.exprList.length > 0)
            emp.exprList.forEach(expr => {
                expr.date = []
                expr.date.push(expr.begin)
                expr.date.push(expr.end)
            })
        })
        total.value = res.data.total
        // console.log(empList.value);
        // console.log(total.value);
    } else {
        ElMessage.error(res.msg)
    }
}

const clear = () => {
    searchInfo.value = { name: '', gender: '', job: '', date: '', begin: '', end: '' ,pageSize:5 ,currentPage:1}
    search()
}


// ------------ 对话框显示和按钮 ------------ 
const dialogFormVisible = ref(false) // 对话框

const cancel = () => {
    dialogFormVisible.value = false
    isAddEmp.value = false
    isEditEmp.value = false
    emp.value = { name: '', username: '', gender: '', avatar: '', deptId: '', jobId: '', exprList: [] }
    jobList.value = []
    deptList.value = []
}
const confirm = async () => { // 将emp传递到服务器
    // 先对整个表单（包括动态的工作经历表单项）进行校验，校验不通过则不提交
    // 如果校验通过validate就直接返回true不会执行catch，如果失败validate就直接抛出异常然后catch捕获返回false
    const valid = await empFormRef.value.validate().catch(() => false)
    if (!valid) {
        ElMessage.warning('请合法填写所有必填项！')
        return
    }

    let res;
    if(isAddEmp.value) {
        res = await addEmpApi(emp.value)
        isAddEmp.value = false
    }

    if(isEditEmp.value) {
        res = await updateEmpApi(emp.value)
        isEditEmp.value = false
    }
    

    if (res.code) { // 如果添加成功，就可以直接关掉对话框了
        dialogFormVisible.value = false
        ElMessage.success(res.msg)
        emp.value = { name: '', username: '', gender: '', avatar: '', deptId: '', jobId: '', exprList: [] }
        search()
    } else { // 如果没有成功，就保留对话框内容，让用户修改

        ElMessage.error(res.msg)
    }
}



// ------------ 新增员工 ------------ 
// emp向服务器传递员工对象所需要的数据，entryDate，updateTime可以不用传递
// emp最终是传递给服务器，然后服务器更具emp添加员工到数据库
// emp是emp对象但是可以改变指向的对象，指向拷贝出来的对象，这样如果取消修改就不会影响原来的数据，所以设置为let变量
let emp = ref({ name: '', username: '', gender: '', avatar: '', deptId: '', jobId: '', exprList: [] })
const handleDateChange = (expr, val) => {
    if (Array.isArray(val)) {
        expr.begin = val[0]
        expr.end = val[1]
    } else {
        expr.begin = ''
        expr.end = ''
    }
}

const jobList = ref([])
const deptList = ref([])
const isAddEmp = ref(false) // 对话框中实现新增和修改员工两个操作，用变量来区分操作
const addEmp = async () => {
    empFormRef.value?.clearValidate() // 清除残留的校验提示

    isAddEmp.value = true
    dialogFormVisible.value = true

    const res = await queryDeptListApi() // 获取部门信息
    if (res.code) {
        deptList.value = res.data
    } else {
        ElMessage.error(res.msg) // 这里不能用jobRes.msg，jobRes在下面才声明，会抛ReferenceError
    }

    const jobRes = await queryJobListApi() // 获取职位信息
    if (jobRes.code) {
        jobList.value = jobRes.data
    } else {
        ElMessage.error(jobRes.msg)
    }
}

// ------------ 新增员工表单校验 ------------

const empFormRef = ref() // 表单引用，用于触发表单校验

// 姓名：必填，不能包含空格和符号（不限制字数，允许中文、英文、数字）
// _rule,value,callback都是由element plus底层传递过来的，不用自己定义
const validateName = (_rule, value, callback) => {
    if (!value) {
        callback(new Error('姓名为必填项'))
    } else if (/\s/.test(value)) {
        callback(new Error('姓名不能包含空格'))
    } else if (/[^一-龥a-zA-Z0-9]/.test(value)) {
        callback(new Error('姓名不能包含符号'))
    } else {
        callback()
    }
}

// 用户名：必填，只能由英文字母组成（不能有数字、符号、空格）
const validateUsername = (_rule, value, callback) => {
    if (!value) {
        callback(new Error('用户名为必填项'))
    } else if (/[^a-zA-Z0-9]/.test(value)) {
        callback(new Error('用户名只能由英文字母数字组成'))
    } else {
        callback()
    }
}

const empRules = {
    name: [{ required: true, validator: validateName, trigger: 'blur' }],
    username: [{ required: true, validator: validateUsername, trigger: 'blur' }],
    gender: [{ required: true, message: '性别为必选项', trigger: 'change' }],
    deptId: [{ required: true, message: '部门为必选项', trigger: 'change' }],
    jobId: [{ required: true, message: '职位为必选项', trigger: 'change' }]
}

// 工作经历字段规则：只要添加了工作经历，公司、职位、在职日期就必填
// 在职日期的值是数组，需要指定type为array，否则空数组不会触发required校验
const exprRules = {
    company: [{ required: true, validator: validateName, trigger: 'blur' }],
    job: [{ required: true, validator: validateName, trigger: 'blur' }],
    date: [{ type: 'array', required: true, message: '在职日期为必选项', trigger: 'change' }]
}

// 员工经历
// emp_id属性是emp传递到后端到数据库中查询回显才能获取到的内容
const createExpr = () => ({ company: '', job: '', date: '', begin: '', end: '' })
const addExpr = () => { emp.value.exprList.push(createExpr()) }
const deleteExpr = (index) => { emp.value.exprList.splice(index, 1) } // index:起始索引，1表示删除元素的个数




// ---------------- 删除员工 ---------------- 
const deleteEmp = async (id, name) => {
    try {
        await ElMessageBox.confirm (
            `您确认删除 ${name} 的员工信息？`,
            'Warning',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消', // 点击取消就直接抛出异常了不会再执行后面的内容了
                type: 'warning',
            }
        )

        const ids = []
        ids.push(id)
        const res = await batchDeleteEmpApi(ids)
        showMsg(res)
    } catch (error) {
        ElMessage.info('Delete canceled')
    }
}

const selectedRows = ref([]) // 存放勾选行的数组
const handleSelectionChange = (rows) => { // 复选框勾选状态变化时触发，selection为当前所有勾选的行
    selectedRows.value = rows
}

const batchDelete = async () => { // 批量删除员工
    if(selectedRows.value.length == 0) {
        ElMessage.warning("当前未选中任何数据!")
        return
    }

    // 循环遍历拿到所有name并拼接为字符串
    let names = ''

    for (let i = 0; i < selectedRows.value.length; i++) {
        if(i == selectedRows.value.length-1) names = names + selectedRows.value[i].name
        else names = names + selectedRows.value[i].name + ','
    }

    try {
        await ElMessageBox.confirm (
            `您确认删除 ${names} 的员工信息？`,
            'Warning',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消', // 点击取消就直接抛出异常了不会再执行后面的内容了
                type: 'warning',
            }
        )

        const ids = []
        selectedRows.value.forEach(emp => ids.push(emp.id))
        
        const res = await batchDeleteEmpApi(ids)
        showMsg(res)
    } catch (error) {
        ElMessage.info('Delete canceled')
    }
}



// ---------------- 编辑员工 ---------------- 
const isEditEmp = ref(false)
const editEmp = async (row) => {
    empFormRef.value?.clearValidate() // 重清除残留的校验提示

    // structuredClone无法克隆Vue的响应式代理对象（表格传来的row是Proxy），会抛出DataCloneError，导致后面代码全部不执行
    // row本身就是纯JSON数据，用JSON序列化实现深拷贝，既能脱离empList原数据，又能去掉响应式
    emp.value = JSON.parse(JSON.stringify(row))

    const res = await queryDeptListApi() // 获取部门信息
    if (res.code) {
        deptList.value = res.data
    } else {
        ElMessage.error(res.msg) // 这里不能用jobRes.msg，jobRes在下面才声明，会抛ReferenceError导致对话框打不开
    }

    const jobRes = await queryJobListApi() // 获取职位信息
    if (jobRes.code) {
        jobList.value = jobRes.data
    } else {
        ElMessage.error(jobRes.msg)
    }

    isEditEmp.value = true
    dialogFormVisible.value = true
}

// ---------------- 显示工作经历 ---------------- 
const exprTableVisable = ref(false) // 工作经历表，包裹在对话框中
const exprs = ref([])
const empName = ref('')
const showExpr = (exprList, name) => {
    exprs.value = exprList // 每一次使用都是重新赋值，所以不用清理
    empName.value = name
    exprTableVisable.value = true
}


// ---------------- 分页条 ---------------- 
// 因为currentPage和pageSize在查询的时候要一起和查询信息发送到服务器所以就放到查询信息里面去了
const total = ref(0)
const handleCurrentChange = () => {
    search()
}
const handleSizeChange = () => {
    search()
}


// ---------------- 格式化日期 ----------------
const formatTime = (_row, _column, cellValue) => {
    // 这里可以确保cellValue是一个字符串所以可以直接使用replace这个内置的String原型链上的方法
    return cellValue ? cellValue.replace('T', ' ') : ''
}

// 钩子函数
onMounted(() => search())

// 显示后端响应信息
const showMsg = (res) => {
    if(res.code) {
        ElMessage.success(res.msg)
        search()
    } else {
        ElMessage.error(res.msg)
    }
}

</script>

<template>
    {{ pageSize }}
    {{ currentPage }}
    <!-- {{ total }} -->
    <!-- {{ emp }} -->
    <!-- {{ exprs }} -->
    <!-- {{ empList }} -->
    <!-- {{ deptList }} -->
    <!-- {{ jobList }} -->
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
                <el-input v-model="searchInfo.job" placeholder="输入职位" clearable :value-on-clear="''" />
            </el-form-item>
            <el-form-item label="入职日期">
                <el-date-picker v-model="searchInfo.date" type="daterange" range-separator="To" start-placeholder="起始日期"
                    end-placeholder="结束日期" value-format="YYYY-MM-DD" :value-on-clear="''" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="search"><el-icon>
                        <Search />
                    </el-icon>搜索</el-button>
                <el-button type="danger" @click="clear"><el-icon><CircleClose /></el-icon>清空</el-button>
            </el-form-item>
        </el-form>
    </div>

    <!-- 操作 -->
    <div class="outter-btn">
        <el-button type="primary" @click="addEmp"><el-icon>
                <CirclePlusFilled />
            </el-icon>新增员工</el-button>
        <el-button type="danger" @click="batchDelete"><el-icon>
                <RemoveFilled />
            </el-icon>批量删除</el-button>
    </div>

    <!-- 表格 -->
    <div class="table">
        <!-- @selection-change这个事件在复选框变化的时候会触发，然后传给函数一个数组 -->
        <el-table :data="empList" @selection-change="handleSelectionChange" style="width: 100%">
            <!-- selectable如果没有自定义就是undefined，然后elementplus底层兜底设置为true -->
            <el-table-column type="selection" :selectable="selectable" width="55" align="center" class="check-box" />
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
            <el-table-column prop="dept" label="部门" width="140" align="center" />
            <el-table-column prop="job" label="职位" width="120" align="center" />
            <el-table-column prop="entryDate" label="入职日期" width="170" align="center" />
            <el-table-column prop="updateTime" label="更新时间" width="170" align="center" :formatter="formatTime" />
            <el-table-column label="工作经历" min-width="60" align="center">
                <template #default="scope">
                    <span v-if="scope.row.exprList.length == 0">无</span>
                    <el-button v-else type="info" size="small" @click="showExpr(scope.row.exprList,scope.row.name)">详情<el-icon>
                            <More />
                        </el-icon></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" min-width="120" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="editEmp(scope.row)"><el-icon>
                            <EditPen />
                        </el-icon>编辑</el-button>
                    <el-button type="danger" size="small" @click="deleteEmp(scope.row.id, scope.row.name)"><el-icon>
                            <Delete />
                        </el-icon>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 分页栏 -->
    <div class="pagination-bar">
        <el-pagination v-model:current-page="searchInfo.currentPage" v-model:page-size="searchInfo.pageSize" :page-sizes="[5, 10, 20, 50]"
            :background="primary" layout="total, sizes, prev, pager, next, jumper" :total="total"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!-- 对话框 -->
    <div class="dialog">
        <el-dialog v-model="dialogFormVisible" title="新增员工" width="500">
            <el-form ref="empFormRef" :model="emp" :rules="empRules">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="emp.name" placeholder="请输入姓名" clearable />
                </el-form-item>
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="emp.username" placeholder="请输入用户名" clearable />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-select v-model="emp.gender" placeholder="请选择" clearable :value-on-clear="''">
                        <el-option label="男" value="1" />
                        <el-option label="女" value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="头像地址">
                    <el-input v-model="emp.avatar" clearable />
                </el-form-item>
                <el-form-item label="部门" prop="deptId">
                    <el-select v-model="emp.deptId" placeholder="请选择" clearable :value-on-clear="''">
                        <!-- 循环获取部门选项 -->
                        <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="(dept.id).toString()" />
                    </el-select>
                </el-form-item>
                <el-form-item label="职位" prop="jobId">
                    <el-select v-model="emp.jobId" placeholder="请选择" clearable :value-on-clear="''">
                        <!-- 循环获取职位选项 -->
                        <el-option v-for="job in jobList" :key="job.id" :label="job.name" :value="(job.id).toString()" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addExpr" style="margin-bottom: 10px;"><el-icon>
                            <CirclePlusFilled />
                        </el-icon>添加工作经历</el-button>
                    <!-- 嵌套一个动态的工作经历表单 -->
                    <!-- 这里不能用el-form嵌套，否则内层表单项会注册到内层el-form上，外层表单的validate()就校验不到它们 -->
                    <!-- :prop的值是相对与el-form中的:model来指定的 -->
                    <!-- :prop必须是表单对象中的属性路径，对于数组的索引这里也是用.index来表示 -->
                    <!-- prop需要写成 exprList.下标.属性 的路径形式，校验规则通过 :rules 单独指定 -->
                    <div v-for="(expr, index) in emp.exprList" :key="index" class="inner-form">
                        <el-form-item label="公司" :prop="`exprList.${index}.company`" :rules="exprRules.company">
                            <el-input v-model="expr.company" placeholder="请输入公司名称" clearable />
                        </el-form-item>
                        <!-- 直接使用prop里面的内容就是内容，使用:prop里面的内容就是变量表达式，变量表达式的结果才是对应的内容 -->
                        <el-form-item label="职位" :prop="`exprList.${index}.job`" :rules="exprRules.job">
                            <el-input v-model="expr.job" placeholder="请输入职位" clearable />
                        </el-form-item>
                        <el-form-item label="在职日期" :prop="`exprList.${index}.date`" :rules="exprRules.date">
                            <el-date-picker v-model="expr.date" type="daterange" range-separator="To"
                                start-placeholder="起始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD"
                                :value-on-clear="''" @change="(val) => handleDateChange(expr, val)" />
                            <!-- @change是选项改变后触发 -->
                        </el-form-item>
                        <el-form-item>
                            <el-button type="danger" @click="deleteExpr(index)"><el-icon>
                                    <Delete />
                                </el-icon></el-button>
                        </el-form-item>
                    </div>
                </el-form-item>
            </el-form>

            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="cancel">取消</el-button>
                    <el-button type="primary" @click="confirm">确认</el-button>
                </div>
            </template>
        </el-dialog>
    </div>

    <!-- 显示工作经历的对话框 -->
    <div class="expr-dialog">
        <el-dialog v-model="exprTableVisable" :title="empName+'——工作经历'" width="800">
            <el-table :data="exprs">
            <el-table-column prop="company" label="公司" width="150" />
            <el-table-column prop="job" label="职位" width="200" />
            <el-table-column prop="begin" label="入职日期" width="200" />
            <el-table-column prop="end" label="离职日期" />
            </el-table>
        </el-dialog>
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

/* 工作经历的动态表单项原本依赖el-form的inline布局，改用div后需要手动恢复行内排列 */
.inner-form :deep(.el-form-item) {
    display: inline-flex;
    margin-right: 10px;
    margin-bottom: 20px;
}
</style>