<template>
  <div class="divBorder pieTopFlex">
    <div class="disFlex">
      <span class="chartText">信用信息公示数据分析</span>
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
import { publicInfo } from '@/api/dataTable.js'
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
      chartTwo: null,
      dataS: [],
      fontSize: '30',
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
    clearInterval(this.timer, console.log('我已经销毁了客户毛利'))
  },
  mounted() {
    this.getDate()
    // this.getEcharts();
  },
  methods: {
    async getEcharts() {
      this.chartTwo = echarts.init(this.$refs.rank_ref, null, {
        renderer: 'svg'
      })
      const body = this.body
      const { data: data } = await publicInfo(body)
      this.dataS = data.data
      var dataX = []
      var dataY = []
      var nameArr = []
      this.dataS.map(res => {
        const nameRes = res.supervisionName
        dataX.push(nameRes)
        dataY.push(res.publicInformationCount)
      })
      console.log(this.dataS, '后端datadata')
      if (this.dataS.length == 0) {
        this.dataS = []
        dataX = []
        dataY = []
        const fontSize = this.fontSize
        var option = {

          xAxis: {
            data: dataX
          },
          series: [
            {
              data: dataY
            }
          ]
        }
      } else {
        option = {
          title: {
            text: ''
          },
          tooltip: {
            trigger: 'axis',
            // backgroundColor: "rgba(255, 255, 255, 1)",
            formatter: function(params) {
              var relVal =
                '<span style = "color:rgba(255, 255, 255, 1)">' +
                params[0].name +
                '</span>'
              for (var i = 0, l = params.length; i < l; i++) {
                const tt = params[i].data
                  .toString()
                  .replace(/(\d)(?=(\d{3})+$)/g, '$1,')
                relVal +=
                  '<br/>' +
                  '<span style = "color:rgba(255, 255, 255, 1)">' +
                  '公示次数' +
                  '&nbsp;&nbsp;&nbsp;&nbsp;' +
                  tt +
                  '&nbsp;' +
                  '次' +
                  '</span>'
              }
              return relVal
            }
          },
          grid: {
            x: '2%', // x 偏移量
            x2: '1%',
            bottom: '2%',
            y: '7%', // y 偏移量
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: dataX,
            axisLabel: {
              formatter: function(value) {
                return value.length > 4
                  ? value.substring(0, 4) + '...'
                  : value
              },
              interval: 0 // 展示所有得时间  y轴
              // rotate: 38 //x文字倾斜
            }
          },
          yAxis: {
            show: true,
            type: 'value',
            max: null,
            splitLine: {
              lineStyle: {
                type: 'dashed'
              }
            }
            // axisLine: {  隐藏y轴线
            //   lineStyle: {
            //     width: 0, // 这里是为了突出显示加上的
            //
            //   }
            // }
          },
          series: [
            {
              data: dataY,
              type: 'line',
              symbol: 'circle', // 拐点实心圆
              color: 'rgba(40, 139, 216, 1)',
              smooth: true,
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: 'rgba(79, 177, 251, .8)' // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: 'rgba(255, 255, 255, .1)' //   100% 处的颜色
                    }
                  ],
                  global: false // 缺省为 false
                }
              }
            }
          ]
        }
      }
      // this.chartTwo.clear();
      this.chartTwo.setOption(option)
      // window.onresize = this.chartTwo.resize; // 让echarts图表的尺寸随着屏幕变化而变化（window.resize）
    },
    screenAdapter() {
      this.titleFontSize = (this.$refs.rank_ref.rank_ref / 100) * 3.6
      this.selectWidth = (this.$refs.rank_ref.rank_ref / 100) * 30
      // 和分辨率大小相关的配置项
      const adapterOption = {
        title: {
          textStyle: {
            fontSize: this.fontSize
          }
        }
      }
      this.chartTwo.setOption(adapterOption)
      // 手动的调用图表对象的resize 才能产生效果
      this.chartTwo.resize()
    },
    // 设置当前时间
    getDate() {
      // 引入获取当前时间函数  parseTime
      var now = parseTime(new Date()) // 当前时间
      console.log(now, 'now')
      // 发送后端时间
      // this.body.startTime = "2022-01-01" + " 00:00:00";
      this.body.endTime = now
      // 页面显示时间
      const systemStartTime = now.slice(0, 8) + '01' // 当前月  先默认一个时间
      const systemEndTime = now.slice(0, 10)
      this.body.startTime = systemStartTime + ' 00:00:00'

      this.weekTime = [systemStartTime, systemEndTime]
    },
    // 选择时间
    selectTime() {
      const weekTime = this.weekTime
      if (this.weekTime !== null) {
        this.body.startTime = weekTime[0] + ' ' + '00:00:00'
        this.body.endTime = weekTime[1] + ' ' + '23:59:59'
        this.getEcharts()
      }
    }
  }
}
</script>

<style scoped>
@import "./style.scss";
.com-charts {
  border-top: 1px solid #c2c2c28e;
  height: 300px;
  width: 100%;
}
</style>
