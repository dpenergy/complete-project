<script setup>
import { ref, onMounted } from "vue";
import { queryDeptListApi,addDeptApi } from '@/api/dept'
import { ElMessage } from 'element-plus'

// deptList是响应式数据，它式一个被包装了的对象，里面的值需要调用value属性
const deptList = ref([])

const showDialog = ref(false)

const dialogName = ref('')

const dept = ref({name:''})

// 利用el-table-column的format属性来格式化时间
// 定义了一个箭头函数，用来替换掉时间里面的T,让时间显示和后端返回的样式一样，但是还是不清楚为什么中间它自动加了T
const formatTime = (row, column, cellValue) => {
    // 这里可以确保cellValue是一个字符串所以可以直接使用replace这个内置的String原型链上的方法
    return cellValue ? cellValue.replace('T', ' ') : ''
}

const query = async () => {
    // 变量前需要添加变量类型：var,let,const
    const result = await queryDeptListApi()// 将前后端交互的内容都抽取到api文件夹中的文件里面统一管理了
    if (result.code) {
        deptList.value = result.data
    } else {
        console.log('code=0响应失败')
    }
}

// 不要忘记操作响应式数据先要拿到value对象在赋值，而不是直接赋值
const addDept = () => {
    dialogName.value = 'Add Dept'
    showDialog.value = true
}

const save = async () => {
    const result = await addDeptApi(dept.value)  // 对象外又封装了一层为响应式对象，html里面能自动解包多以不用.value但是script里面需要
    
    // 判断是否成功
    if(result.code) {
        // nb居然又忘记了调用出value
        showDialog.value = false
        successPromot(dept.value.name +' add success!')
        // 重新查询一遍部门列表
        dept.value.name = ''
        query()
    } else {
        // 提示具体信息msg
        errorPromot(result.msg)
    }
}

const successPromot = (msg) => {
  ElMessage({
    message: msg,
    type: 'success',
  })
}

const errorPromot = (msg) => {
  ElMessage.error(msg)
}

// 钩子函数-onMounted也需要导入
onMounted(() => {
    query()
})

</script>

<template>
    <!-- {{deptList}} -->
    {{dept.name}}
    <h1>部门管理</h1>
    <!-- 按钮 -->
    <div class="top-button"><el-button type="primary" @click="addDept"> + 新增部门</el-button></div>
    <!-- 表格 -->
    <div class="dept-table">
        <el-table :data="deptList" style="width: 100%">
            <el-table-column type="index" label="ID" width="150" align="center" />
            <el-table-column prop="name" label="DeptName" width="250" align="center" />
            <el-table-column prop="createTime" label="CreateTime" width="300" align="center" :formatter="formatTime" />
            <el-table-column prop="updateTime" label="UpdateTime" width="300" align="center" :formatter="formatTime" />
            <el-table-column label="Operation" align="center">
                <template #default>
                    <el-button type="primary" size="small"><el-icon>
                            <EditPen />
                        </el-icon>编辑</el-button>
                    <el-button type="danger" size="small"><el-icon>
                            <Delete />
                        </el-icon>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 对话框 -->
    <div class="dialog">
        <!-- v-model是用于表单的双向数据绑定，v-bind使用与绑定标签属性 -->
        <!-- v-model是双向，v-bind是单向 -->
        <el-dialog v-model="showDialog" :title="dialogName" width="500">
            <el-form :model="dept">
                <el-form-item label="DeptName:" label-width="100px">
                    <el-input v-model="dept.name"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-btn">
                    <el-button @click="showDialog = false;dept.name = ''">Cancel</el-button>
                    <el-button type="success" @click="save">
                        Confirm
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped>
.top-button {
    margin: 20px 0;
}

.dialog :deep(.el-dialog) {
    background-color: #3e6b27;
}

.dialog :deep(.el-dialog__header) {
    background-color: #3e6b27;
}

.dialog :deep(.el-dialog__body) {
    background-color: #3e6b27;
}

.dialog :deep(.el-dialog__footer) {
    background-color: #3e6b27;
}

</style>