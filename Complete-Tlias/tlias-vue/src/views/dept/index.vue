<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

// deptList是响应式数据，它式一个被包装了的对象，里面的值需要调用value属性
const deptList = ref([]);

const query = async () => {
    // 变量前需要添加变量类型：var,let,const
    const result = await axios.get('http://localhost:8080/depts');
    if (result.data.code) {
        deptList.value = result.data.data;
    } else {
        console.log('code=0响应失败');
    }
}

// 钩子函数-onMounted也需要导入
onMounted(() => {
    query();
})

</script>

<template>
    <h1>部门管理</h1>
    <div class="top-button"><el-button type="primary"> + 新增部门</el-button></div>
    <div class="dept-table">
        <el-table :data="deptList" style="width: 100%">
            <el-table-column type="index" label="ID" width="150" align="center" />
            <el-table-column prop="deptName" label="DeptName" width="250" align="center" />
            <el-table-column prop="createTime" label="CreateTime" width="300" align="center" />
            <el-table-column prop="updateTime" label="UpdateTime" width="300" align="center" />
            <el-table-column label="Operation" align="center">
                <template #default>
                    <el-button type="primary" size="samll"><el-icon>
                            <EditPen />
                        </el-icon>编辑</el-button>
                    <el-button type="danger" size="samll"><el-icon>
                            <Delete />
                        </el-icon>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<style scope>
.top-button {
    margin: 20px 0;
}
</style>