<script setup>
import { ref, onMounted } from 'vue'
import { echarts } from '@/utils/echarts'
import { empGenderCountApi,queryJobDataApi } from '@/api/emp'
import { ElMessage } from 'element-plus'

// 用模板 ref 代替 document.getElementById
const mainRef = ref(null)
let myChart



// 获取职位列表和人数列表
// 确保职位和人数对应，我们让后端传递对象数组{job,girlCounts,boyCounts}
// 单独获取性别统计{girlCounts,boyCounts}
const genderData = ref(null)
const genderCount = async () => {
    const res =await empGenderCountApi()
    if(res.code) {
        genderData.value = res.data
    } else {
        ElMessage.error(res.msg)
    }
}
// const jobList = ref(['Brazil', 'Indonesia', 'USA', 'India', 'China', 'World', '性别统计'])
// const boyCounts = ref([18203, 23489, 29034, 104970, 131744, 630230, 0])
// const girlCounts = ref([19325, 23438, 31000, 121594, 134141, 681807, 0])
const jobList = ref([])
const boyCounts = ref([])
const girlCounts = ref([])

const addGenderData = () => {
    if (!genderData.value) return // 性别统计接口失败时跳过，避免读取 null 的属性报错
    jobList.value.push('性别统计')
    boyCounts.value.push(genderData.value.oneCounts)
    girlCounts.value.push(genderData.value.twoCounts)
}

const handleJobData = (jobData) => {
    for(const data of jobData) {
        jobList.value.push(data.name)
        boyCounts.value.push(data.boyCounts)
        girlCounts.value.push(data.girlCounts)
    }

    addGenderData()
}

const queryJobData = async () => {
    const res = await queryJobDataApi()
    if(res.code) {
        handleJobData(res.data)
    } else {
        ElMessage.error(res.msg)
    }
}

const setChartOption = () => {
    const option = {
        title: {
            text: '职位统计'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {},
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: jobList.value
        },
        series: [
            {
                name: '男',
                type: 'bar',
                data: boyCounts.value
            },
            {
                name: '女',
                type: 'bar',
                data: girlCounts.value
            }
        ]
    }
    myChart.setOption(option)
}



// 等 DOM 挂载完成后才能初始化图表
onMounted(async () => {
    myChart = echarts.init(mainRef.value, 'dark')

    // 先把数据拿到，再渲染：await 等两个请求都返回、数组填好之后，才执行 setOption
    // async方法会自动返回一个Promise对象
    // 传递给Promise.all或者Promise.allSettled方法的参数是一个Promise数组
    await Promise.allSettled([genderCount(), queryJobData()])

    setChartOption()
})

</script>

<template>
    <!-- {{ genderData }} -->
    <!-- {{ jobList }} -->
    <!-- {{ boyCounts }} -->
    <!-- {{ girlCounts }} -->
    <div ref="mainRef" class="main"></div>
</template>

<style scoped>
.main {
    width: 100%;
    height: 100%;
}
</style>