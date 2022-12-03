<template>
  <div class="divBorder pieTopFlex">
    <div class="disFlex">
      <span class="chartText">监管通知下达次数分析</span>
      <div class="block">
        <el-date-picker
          v-model="weekTime"
          size="mini"
          type="daterange"
          range-separator="→"
          value-format="yyyy-MM-dd"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :clearable="false"
          :picker-options="pickerOptions"
          style="width:100%;float:right"
          @change="selectTime"
          @clear="getEcharts"
        />
      </div>

    </div>
    <!-- 图表 -->
    <div class="dataLinePosition">
      <div ref="rank_ref" class="com-charts" />
    </div>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { parseTime } from '@/utils/index.js'
import { policyPreach } from '@/api/dataTable.js'
export default {
  components: {},
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          }
        ],
        // 只能选择今天和今天之前得日期
        disabledDate(time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      },
      weekTime: '',
      body: {
        startTime: '',
        endTime: ''
      },
      // value: '',
      chartFour: null,
      fontSize: '30',
      dataList: [],
      timer: ''
    }
  },
  activated() {
    window.addEventListener('resize', this.screenAdapter)
    this.getEcharts()
    this.screenAdapter()
    this.timer = setInterval(() => {
      window.addEventListener('resize', this.screenAdapter)
      this.getEcharts()
      this.screenAdapter()
    }, 1000 * 60 * 2)
  },
  deactivated() {
    // 在组件销毁的时候, 需要将监听器取消掉
    window.removeEventListener('resize', this.screenAdapter)
    clearInterval(this.timer, console.log('我已经销毁饼图'))
  },
  mounted() {
    // this.getEcharts()
    this.getDate()
  },
  methods: {
    async  getEcharts() {
      this.chartFour = echarts.init(this.$refs.rank_ref, null, {
        renderer: 'svg'
      })
      // var xData=['桂溪监管所', '石羊监管所', '武侯监管所', '芳草监管所', '西园监管所', '中和监管所', '合作监管所']

      //  console.log(xData,"222223")
      const body = this.body
      const weekTime = this.body.weekTime
      const dataX = [] // x数据
      const dataY1 = [] // y柱状数据
      const dataY2 = [] // y折线数据
      // 获取后端接口
      const { data: data } = await policyPreach(body)
      console.log(data, 'res')
      this.dataList = data.data
      this.dataList.map(res => {
        dataX.push(res.supervisionName)
        dataY1.push(res.policyPreachCount)
        dataY2.push(res.regulatoryRemindCount)
      })
      var option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            lineStyle: {
              color: 'rgba(47, 162, 248, .12)',
              type: 'solid',
              width: 80
            }
          },
          // 给提示框加单位
          formatter: function(params) {
            var relVal =
                '<span style = "color:rgba(255, 255, 255, 1)">' +
                params[0].name +
                '</span>'
            for (var i = 0, l = params.length; i < l; i++) {
              const value = params[i].value.toString()
              relVal +=
                  '<br/>' +
                  '<span style = "color:rgba(255, 255, 255, 1)">' +
                  params[i].seriesName +
                  '&nbsp;&nbsp;&nbsp;&nbsp;' +
                  value.replace(/(\d)(?=(\d{3})+$)/g, '$1,') +
                  '&nbsp;' +
                  '次' +
                  '</span>'
            }
            return relVal
          }
        },
        legend: {
          x: '5%', // 可设定图例在左、右、居中
          // y: "top", //可设定图例在上、下、居中
          y: '5%',
          itemGap: 30
        },
        grid: {
          x: '1%', // x 偏移量
          x2: '1%',
          bottom: '2%',
          // y: "7%", // y 偏移量
          containLabel: true
        },

        xAxis: [
          {
            type: 'category',
            // data: ['桂溪监管所', '石羊监管所', '武侯监管所', '芳草监管所', '西园监管所', '中和监管所', '合作监管所'],
            data: dataX,
            // axisPointer: {
            //   type: 'shadow'
            // },
            axisLabel: {
              formatter: function(value) {
                return value.length > 4
                  ? value.substring(0, 4) + '...'
                  : value
              },
              interval: 0 // 展示所有得时间  y轴
              // rotate: 38 //x文字倾斜
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            splitLine: {
              lineStyle: {
                type: 'dashed' // y轴分隔线
              }
            }
          }
        ],
        series: [
          {
            name: '政策宣讲',
            type: 'bar',
            barWidth: '25%',
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: 'rgba(16, 149, 250, 1)' // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: 'rgba(113, 191, 249, 1)' // 100% 处的颜色
                    }]) }},
            data: dataY1
          },
          {
            name: '监管提醒',
            type: 'bar',
            barWidth: '25%',
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: 'rgba(36, 100, 207, 1)' // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: 'rgba(134, 179, 254, 1)' // 100% 处的颜色
                    }]) }},
            data: dataY2
          }
        ]
      }
      this.chartFour.setOption(option)
      // window.onresize = this.chartFour.resize; // 让echarts图表的尺寸随着屏幕变化而变化（window.resize）
    },
    screenAdapter() {
      this.titleFontSize = this.$refs.rank_ref.rank_ref / 100 * 3.6
      this.selectWidth = this.$refs.rank_ref.rank_ref / 100 * 30
      // 和分辨率大小相关的配置项
      const adapterOption = {
        title: {
          textStyle: {
            fontSize: this.fontSize
          }
        }
      }
      this.chartFour.setOption(adapterOption)
      // 手动的调用图表对象的resize 才能产生效果
      this.chartFour.resize()
      // this.chartNumber.resize()
    },
    // 选择时间
    selectTime() {
      const weekTime = this.weekTime
      if (this.weekTime !== null) {
        this.body.startTime = weekTime[0] + ' ' + '00:00:00'
        this.body.endTime = weekTime[1] + ' ' + '23:59:59'
        this.getEcharts()
      }
    },
    // 设置当前时间
    getDate() {
      // 引入获取当前时间函数  parseTime
      var now = parseTime(new Date())// 当前时间
      console.log(now, 'now')
      // 发送后端时间
      this.body.startTime = '2022-01-01' + ' 00:00:00'
      this.body.endTime = now
      // 页面显示时间
      const systemStartTime = '2022-01-01' // 先默认一个时间  从本年开始
      // const systemStartTime=now.slice(0,8)+"01"//当前月  先默认一个时间
      const systemEndTime = now.slice(0, 10)
      this.weekTime = [
        systemStartTime,
        systemEndTime
      ]
    }
  }
}
</script>

<style scoped>
@import "./style.scss";
.com-charts{
  border-top: 1px solid #c2c2c28e;
  height: 300px;
  width: 100%;
}
</style>
