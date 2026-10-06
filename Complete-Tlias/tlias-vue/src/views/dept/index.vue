<script setup>
import { ref, onMounted, nextTick } from "vue";
import { queryDeptListApi, addDeptApi } from '@/api/dept'
import { ElMessage } from 'element-plus'

// === 响应式变量 ===
// deptList是响应式数据，它式一个被包装了的对象，里面的值需要调用value属性
const deptList = ref([])

const showDialog = ref(false)

const dialogName = ref('')

const dept = ref({ name: '' })

// 表单校验的两条规则：如果没有填写提示……，如果填写不规范提示……
const rules = ref({
    // 这个name是属性，后面的校验规则是去校验name里面的值是否符合，所以不能随便取，这个name对应dept.value.name
    // trigger(扳机)表示触发条件，blur表示消聚焦（模糊）：就是鼠标离开时触发校验
    name: [
        { required: true, message: 'Please input dept name', trigger: 'blur' },
        { min: 3, max: 10, message: 'Length should be 3 to 10', trigger: 'blur' }
    ]
})

// el-form 组件实例的引用：模板里 ref="formRef" 与这里变量同名，Vue 会把组件实例注入进来
// 拿到实例才能调用 el-form 暴露的 validate（主动校验）、resetFields（重置）、clearValidate（清提示）等方法
const formRef = ref(null)



// === 函数 ===

// 利用el-table-column的format属性来格式化时间
// 定义了一个箭头函数，用来替换掉时间里面的T,让时间显示和后端返回的样式一样，但是还是不清楚为什么中间它自动加了T
const formatTime = (_row, _column, cellValue) => {
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
    // nextTick：等对话框(表单)渲染完再清掉上一次残留的校验提示，比如用户点右上角×关闭的情况
    // ?.是可选链操作符，作用是安全的访问可能为null或者undefined的对象
    // nextTick()是vue的Dom更新调度器，更新这个formRef.value(表单对象)
    nextTick(() => formRef.value?.clearValidate())
}

const save = async () => {
    // 主动触发整个表单的校验：validate() 不传回调时返回 Promise
    // 校验通过 → resolve 继续往下走；不通过 → reject，且 el-form 会自动把红色提示显示在表单项下方
    try {
        // validate()校验失败就会抛出异常，成功返回true
        await formRef.value.validate()  // 最外层包裹的响应式，value才是对象本身，表单项的实例对象所以有validate()校验方法
    } catch (error) {
        return // 校验没通过或者对象根本不存在(判断对象是否存在需要.value，应为响应式这个模式的实现是需要内容的所以响应式对象本身一定是非null,但是value属性是有则有)，直接结束，不向后端发请求
    }

    const result = await addDeptApi(dept.value)  // 对象外又封装了一层为响应式对象，html里面能自动解包多以不用.value但是script里面需要

    // 判断是否成功
    if (result.code) {
        ElMessage.success(dept.value.name + ' add success!')
        showDialog.value = false
        // resetFields：把 dept.name 恢复成初始值(空字符串)，同时清掉校验状态，替代之前的手动置空
        formRef.value.resetFields()
        // 重新查询一遍部门列表
        query()
    } else {
        // 提示具体信息msg
        ElMessage.error(result.msg);
    }
}

// 钩子函数-onMounted也需要导入
onMounted(() => {
    query()
})

</script>

<template>
    <!-- {{deptList}} -->
    <!-- {{dept.name}} -->
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
            <!-- 表单 -->
            <!-- @submit.prevent防止默认提交行为按下enter后自动提交，我们是按下enter调用save提交所以需要防止默认提交 -->
            <!-- 当这个 <el-form> 组件渲染完成后，把它的组件实例赋值给 script 中名为 formRef 的变量 -->
            <el-form ref="formRef" :model="dept" @submit.prevent :rules="rules" hide-required-asterisk>
                <!-- prop中填写的属性值对应表单项的校验规则，同时被校验的值是:model.prop里面的属性值，这里是dept.name所以prop这个值既要和当前表单项值得存储对象属性名称相同，也要和校验规则里面的校验对象名称相同 -->
                <el-form-item label="DeptName:" label-width="100px" prop="name">
                    <!-- 表单中按下enter,默认走浏览器中的第一个按钮，这里使用@keyup.enter就是enter键绑定监听 -->
                    <!-- 按键绑定v-on:keyup. -->
                    <!-- 由于在el-form中enter可能会触发表单提交的行为导致页面刷新 -->
                    <el-input v-model="dept.name" @keyup.enter="save" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-btn">
                    <!-- resetFields：恢复初始值并清掉红色提示，比手动 dept.name='' 更彻底 -->
                    <el-button @click="showDialog = false; formRef?.resetFields()">Cancel</el-button>
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

.dialog :deep(.el-form-item__error) {
    font-size: 16px;
}
</style>