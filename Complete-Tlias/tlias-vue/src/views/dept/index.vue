<script setup>
const tableData = [
    {
        "id": 53,
        "name": "暨雨桐",
        "createTime": "2026-03-04 20:16:47",
        "updateTime": "2027-06-14"
    },
    {
        "id": 69,
        "name": "乌雅哲新",
        "createTime": "2026-06-17 00:27:35",
        "updateTime": "2026-03-09"
    },
    {
        "id": 92,
        "name": "繁霞",
        "createTime": "2026-01-27 20:17:01",
        "updateTime": "2027-07-20"
    },
    {
        "id": 83,
        "name": "蒋梓涵",
        "createTime": "2025-11-28 08:14:33",
        "updateTime": "2027-05-17"
    },
    {
        "id": 76,
        "name": "法依诺",
        "createTime": "2027-05-02 11:16:17",
        "updateTime": "2027-09-06"
    },
    {
            "id": 26,
            "name": "禽娜",
            "createTime": "2025-12-09 14:37:48",
            "updateTime": "2027-09-12"
        },
        {
            "id": 92,
            "name": "释斌",
            "createTime": "2027-02-07 16:03:36",
            "updateTime": "2027-02-06"
        },
        {
            "id": 98,
            "name": "贾国平",
            "createTime": "2026-05-29 15:37:45",
            "updateTime": "2025-11-16"
        }
]
</script>

<template>
    <h1>部门管理</h1>
    <div class="top-button"><el-button type="primary"> + 新增部门</el-button></div>
    <div class="dept-table">
        <el-table :data="tableData" style="width: 100%">
            <el-table-column type="index" label="ID" width="150" align="center" />
            <el-table-column prop="name" label="DeptName" width="250" align="center" />
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