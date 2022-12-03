<template>
  <div class="divBorder pieTopFlex">
    <div class="disFlex">
      <span class="chartText">三码合一活跃商户分析</span>
      <div class="block">
        <el-date-picker
          v-model="body.weekTime"
          type="month"
          placeholder="选择月"
          :clearable="false"
          value-format="yyyy-MM"
          size="mini"
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
import { activeMerch } from '@/api/dataTable.js'
export default {
  components: {},
  data() {
    return {
      pickerOptions: {
        // 只能选择今天和今天之前得日期
        disabledDate(time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      },
      chartThree: null,
      body: {
        // startTime: "",
        // endTime: ""
        weekTime: ''
      },
      dataList: [],
      fontSize: '30',
      timer: ''
    }
  },
  activated() {
    window.addEventListener('resize', this.screenAdapter)
    this.getEchartData()

    this.screenAdapter()
    this.timer = setInterval(() => {
      window.addEventListener('resize', this.screenAdapter)
      this.getEchartData()
      this.screenAdapter()
    }, 1000 * 60 * 2)
  },
  deactivated() {
    // 在组件销毁的时候, 需要将监听器取消掉
    window.removeEventListener('resize', this.screenAdapter)
    clearInterval(this.timer, console.log('我已经销毁饼图'))
  },
  mounted() {
    this.getDate()
    // this.getEchartData();
    // this.selectTime();
  },
  methods: {
    async getEchartData() {
      this.chartThree = echarts.init(this.$refs.rank_ref, null, {
        renderer: 'svg'
      })
      const body = this.body
      // let weekTime = this.body.weekTime;
      console.log(this.body.weekTime, 'weekTimeweekTime')
      let dataX = [] // x数据
      let dataY1 = [] // y柱状数据
      let dataY2 = [] // y折线数据
      // //获取后端接口
      const { data: data } = await activeMerch(body)
      this.dataList = data.data
      // console.log(data.data,"后端数据")

      this.dataList.map(res => {
        const nameRes = res.supervisionName
        // if (nameRes.length > 5) {
        //   dataX.push(nameRes.substring(0, nameRes.length - 3));
        // } else {
        dataX.push(nameRes)
        // }
        dataY1.push(res.totalMerchantCount)
        dataY2.push(res.activeMerchantCount)
      })

      console.log(dataX, dataY1, dataY2, '后端数据')
      if (this.dataList.length == 0) {
        this.dataList = []
        dataX = [] // x数据
        dataY1 = [] // y柱状数据
        dataY2 = [] // y折线数据
        const fontSize = this.fontSize
        var option = {
          xAxis: {
            data: dataX
          },
          series: [{ data: dataY1 }, { data: dataY2 }]
        }
      } else {
        option = {
          title: {
            text: ''
          },
          tooltip: {
            trigger: 'axis',
            // backgroundColor:"rgba(255, 255, 255, 1)",
            // extraCssText:'width:15%;border:1px solid grey',
            axisPointer: {
              type: 'line',
              lineStyle: {
                color: 'rgba(47, 162, 248, .12)',
                type: 'solid',
                width: 50
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
                  '家' +
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
            left: '1%',
            right: '1%',
            bottom: '2%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: dataX,
              //  boundaryGap: false,
              axisTick: {
                alignWithLabel: true
              },
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
              max: null,
              //  boundaryGap: [0, 0],
              splitLine: {
                lineStyle: {
                  type: 'dashed' // y轴分隔线
                }
              }
            }
          ],

          series: [
            {
              name: '商户数量',
              type: 'bar',
              barWidth: '30%',
              data: dataY1,
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
                      }
                    ],
                    false
                  ),
                  barBorderRadius: [8, 8, 0, 0] // 柱状圆角
                }
              }
            },

            {
              name: '活跃商户',
              type: 'line',
              data: dataY2,
              symbol: 'circle', // 拐点实心圆
              // symbolSize: 7,
              itemStyle: {
                color: 'rgba(23, 110, 227, .6)'
              }
            }
          ]
        }
      }
      this.chartThree.setOption(option)
      // window.onresize = this.chartThree.resize; // 让echarts图表的尺寸随着屏幕变化而变化（window.resize）
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
      this.chartThree.setOption(adapterOption)
      // 手动的调用图表对象的resize 才能产生效果
      this.chartThree.resize()
      // this.chartNumber.resize()
    },
    // 选择时间
    selectTime() {
      const weekTime = this.body.weekTime
      console.log(weekTime, 'weekTime')
      // if (this.weekTime !== null) {
      //   this.body.startTime = weekTime[0] +" "+ "00:00:00";
      //   this.body.endTime = weekTime[1]+" " + "23:59:59";
      this.getEchartData()
      // }
    },
    // 设置当前时间
    getDate() {
      // 引入获取当前时间函数  parseTime
      var now = parseTime(new Date()) // 当前时间
      //  console.log(now, "now");
      // 发送后端时间
      // this.body.startTime = "2022-01-01" + " 00:00:00";
      // this.body.endTime = now;
      // 页面显示时间
      // const systemStartTime = now.slice(0, 8) + "01"; //当前月  先默认一个时间
      // const systemEndTime = now.slice(0, 10);
      const systemStartTime = now.slice(0, 7) // 当前月  先默认一个时间
      // this.weekTime = [systemStartTime, systemEndTime];
      this.body.weekTime = systemStartTime
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
