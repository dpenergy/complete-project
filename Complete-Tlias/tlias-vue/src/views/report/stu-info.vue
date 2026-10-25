<script setup>
import { echarts } from '@/utils/echarts'
import { ref, onMounted } from 'vue'
import { stuGenderCountApi,stuGraduateCountApi,stuPerformanceCountApi } from '@/api/stu'
import { ElMessage } from 'element-plus'


const mainRef = ref(null) // 绑定DOM对象
let myChart

// --------------- 学员性别统计 --------------- 
const stuBoyCounts = ref(0)
const stuGirlCounts = ref(0)
const stuGenderCount = async () => {
    const res = await stuGenderCountApi();
    if(res.code) {
        stuBoyCounts.value = res.data.oneCounts
        stuGirlCounts.value = res.data.twoCounts
    } else {
        ElMessage.error(res.msg)
    }
}

// --------------- 学员状态统计 --------------- 
const graduateCounts = ref(0)
const unGraduateCounts = ref(0)
const stuGraduateCount = async () => {
    const res = await stuGraduateCountApi()
    if(res.code) {
        graduateCounts.value = res.data.oneCounts
        unGraduateCounts.value = res.data.twoCounts
    } else {
        ElMessage.error(res.msg)
    }
}

// --------------- 学员表现统计 ---------------
const performance = ref({normal:0,good:0,bad:0,warn:0})
const stuPerformanceCount =async () => {
    const res = await stuPerformanceCountApi()
    if(res.code) {
        performance.value.normal = res.data.oneCounts
        performance.value.good = res.data.twoCounts
        performance.value.bad = res.data.threeCounts
        performance.value.warn = res.data.fourCounts
    } else {
        ElMessage(res.msg)
    }
}


const setChartOption = () => {
    const option = {
        title: {
            text: '学员信息统计',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            left: 'center',
            top: 'bottom',
            data: [
                '在读',
                '毕业',
                '正常',
                '很好',
                '糟糕',
                '警告',
                '男',
                '女'
            ]
        },
        toolbox: {
            show: true,
            feature: {
                mark: { show: true },
                dataView: { show: true, readOnly: false },
                restore: { show: true },
                saveAsImage: { show: true }
            }
        },
        series: [
            {
                name: '状态',
                type: 'pie',
                radius: [20, 140],
                center: ['25%', '25%'],
                roseType: 'radius',
                itemStyle: {
                    borderRadius: 5
                },
                data: [
                    { value: unGraduateCounts.value, name: '在读' },
                    { value: graduateCounts.value, name: '毕业' }
                ]
            },
            {
                name: '表现',
                type: 'pie',
                radius: [20, 140],
                center: ['75%', '50%'],
                roseType: 'area',
                itemStyle: {
                    borderRadius: 5
                },
                data: [
                    { value: performance.value.normal, name: '正常' },
                    { value: performance.value.good, name: '很好' },
                    { value: performance.value.bad, name: '糟糕' },
                    { value: performance.value.warn, name: '警告' }
                ]
            },
            {
                name: '性别统计',
                type: 'pie',
                radius: [80, 140],
                center: ['25%', '75%'],
                // adjust the start and end angle
                startAngle: 180,
                endAngle: 360,
                data: [
                    { value: stuBoyCounts.value, name: '男' },
                    { value: stuGirlCounts.value, name: '女' }
                ]
            }
        ]
    }

    myChart.setOption(option);
}

// 页面挂载完成才能拿到DOM对象
onMounted(async () => {
    // 同步化 获取数据
    await Promise.allSettled([stuGenderCount(),stuGraduateCount(),stuPerformanceCount()])

    // 传递DOM对象，绑定初始化echarts对象
    myChart = echarts.init(mainRef.value, 'dark')

    // setChartOption
    setChartOption()
})

</script>
<template>
    <div class="main" ref="mainRef"></div>
</template>
<style scoped>
/* 使用echarts必须自定宽高 */
.main {
    width: 100%;
    height: 100%;
}
</style>