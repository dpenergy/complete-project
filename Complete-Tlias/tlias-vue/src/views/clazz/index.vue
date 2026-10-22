<script setup>
import { ref, onMounted } from 'vue'
import { queryClazzListApi } from '@/api/clazz'
import { queryHeaderListApi } from '@/api/emp'
import { ElMessage } from 'element-plus'

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

    // 清除表单校验信息
    // 首次点击的时候为null就不会执行，但是如果是初始化为''则会执行并且报错
    clazzFormRef.value?.clearValidate() // 清除残留的校验提示
    // 重置表单
    dialogForm.value = { name: '', headerId: '' }

    
    showDialogForm.value = true
}

const cancel = () => {
    showDialogForm.value = false

    
}
const confirm = () => {

    showDialogForm.value = false
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
const rules = ref({
    clazzName: [{ required: true, validator: validateClazzName, trigger: 'blur' }],
    header: [{ required: true, message:'必选项', trigger: 'blur' }]
})

// 页面挂载完成后，调用钩子函数获取班级信息
onMounted(() => {
    queryClazzList()
    queryHeaderList()
})
</script>

<template>
    <!-- {{ headerList }} -->
    {{ dialogForm }}
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
                    <span v-if="scope.row.status == 1" class="green">在课班级</span>
                    <span v-else-if="scope.row.status == 2" class="gray">毕业班级</span>
                    <span v-else class="red">未知状态</span>
                </template>
            </el-table-column>
            <el-table-column label="班级详情" align="center" width="200">
                <template #default="scope">
                    <el-button type="info" size="small">更多<el-icon>
                            <More />
                        </el-icon></el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small">编辑</el-button>
                    <el-button type="danger" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 对话框 -->
    <div class="dialog">
        <el-dialog v-model="showDialogForm" title="Shipping address" width="500">
            <!-- 这里的ref属性，没有使用v-bind是由于vue的自动匹配机制 -->
            <!-- ref属性会自动与ref()函数关联，然后同名进行DOM对象传递 -->
            <el-form ref="clazzFormRef" :model="dialogForm" :rules="rules">
                <el-form-item label="班级名称" prop="clazzName">
                    <el-input v-model="dialogForm.name" />
                </el-form-item>

                <el-form-item label="班主任" prop="header">
                    <el-select v-model="dialogForm.headerId" placeholder="请选择班主任">
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
</template>

<style scope>
.head {
    margin-bottom: 20px;
}

.operation {
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