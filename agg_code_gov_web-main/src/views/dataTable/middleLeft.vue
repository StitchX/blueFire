<template>
  <div class="divBorder  pieTopFlex">
    <div class="disFlex">
      <span class="chartText">纳入监管商户分析</span>
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
import { regulartory } from '@/api/dataTable.js'
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
      fontSize: '30',
      value: '',
      chartOne: null,
      dataS: [],
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
    // this.getEcharts();
    this.getDate()
    // this.selectTime();
  },
  methods: {
    // 搜索

    // 客户利润占比
    async getEcharts() {
      this.chartOne = echarts.init(this.$refs.rank_ref, null, {
        devicePixelRatio: 2.5
      })
      const body = this.body
      console.log(body, 'body')
      var list = []
      var name = []
      var id = []
      // var list = [{ name: "随手记", value: "0"},{ name: "小米金融", value: "0"},{ name: "湖南新生", value: "0"},{ name: "就拍天下", value: "0"},{ name: "今日头条", value: "0"},{ name: "西瓜买单", value: "0"},{ name: "同程网络", value: "0"},{ name: "北京微博", value: "0"},{ name: "字节跳动", value: "0"},{ name:"美图" , value: "0"},{ name:"美图1" , value: "0"},{ name:"美图2" , value: "0"}]
      // const name = ["随手记","小米金融","湖南新生","就拍天下","今日头条","西瓜买单","同程网络","北京微博","字节跳动","美图"]
      // const id = [0,0,0,0,0,0,0,0,0,0]
      const { data: data } = await regulartory(body)
      var getData = data.data
      console.log(getData, '11112')
      var total = 0
      if (getData == '') {
        this.chartOne.clear()
        this.chartOne.setOption({
          series: [{
            radius: ['60%', '85%'],
            center: ['50%', '50%'],
            legend: {
              show: false
            },
            label: {
              rich: {
                color1: {
                  color: '#464369',
                  fontSize: 20
                },
                color2: {
                  color: '#87CEFA',
                  fontSize: 21
                }
              },
              formatter:
                '{color1|' +
                '商户总数' +
                '}' +
                '\n' +
                '\n' +
                '{color2|' +
                total +
                '家' +
                '}',
              // show: false,
              position: 'center'
            },
            data: [0], // 这里设置了默认值
            type: 'pie',
            hoverOffset: 0, // 鼠标移上放大
            itemStyle: {
              color: 'rgb(148,226,255)',
              normal: { color: '#34A5FB' }, // 鼠标移上不显示高亮 normal和emphasis颜色一样
              emphasis: { color: '#34A5FB' }

            }
          }]
        })
      } else {
        this.chartOne.clear()
        this.dataS = data.data.eachMerchants
        if (this.dataS.length > 0) {
          console.log(this.dataS, '11112')
          this.dataS.map(res => {
            list.push({ name: res.supervisionName, value: res.merchantCount })
            id.push(res.merchantCount)
            name.push(res.supervisionName)
          })

          for (var y = 0; y < id.length; y++) {
            total = Number(total + id[y])
          }
          this.chartOne.setOption({
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b} : {c} 家 ( {d}% )'
            },
            grid: {
              left: '10%',
              right: '1%',
              bottom: '2%',
              containLabel: true
            },
            legend: {
              type: 'scroll',
              orient: 'vertical',
              right: '5%',
              top: '20%',
              bottom: 0,
              icon: 'circle',
              itemWidth: 8,
              itemHeight: 8,
              itemGap: 18,
              // pageIconColor: "#39365F",//激活的分页按钮颜色
              // pageIconInactiveColor: "#eeee",//没激活的分页按钮颜色
              // pageTextStyle: {
              //     color:"#464369"
              // },
              textStyle: {
                fontWeight: 'normal', // 标题颜色
                rich: {
                  a: {
                    fontSize: 13,
                    color: '#333',
                    padding: 1,
                    width: 120
                  },
                  b: {
                    fontSize: 13,
                    color: '#333',
                    width: 50
                  },
                  c: {
                    fontSize: 13,
                    color: '#333'
                  },
                  d: {
                    fontSize: 13,
                    color: 'rgba(217, 217, 217, 1)'
                  }
                }
              },
              formatter: function(code) {
                // if(this.datas!==""){
                var index = 0
                var clientlabels = name
                var clientcounts = id
                // console.log(total, "total");
                var ratio = 0
                clientlabels.forEach(function(value, i) {
                  // console.log(value, "value");
                  if (value == code) {
                    index = i
                  }
                })
                if (clientcounts[index] > 0) {
                  ratio = ((clientcounts[index] / total) * 100).toFixed(2)
                } else {
                  ratio = 0
                  clientcounts[index] = 0
                }
                return (
                  '{a|' +
              // (code.length > 8 ? code.slice(0, 8) + "..." : code) +
              code +
              '}' +
              '{d|' +
              '   |   ' +
              '}' +
              '{b|' +
              ratio +
              '%' +
              '}' +
              '      ' +
              '{c|' +
              clientcounts[index] +
              '家' +
              '}'
                )
                // }
              }
            },
            color: [
              'rgba(52, 165, 251, 1)',
              'rgba(41, 189, 169, 1)',
              'rgba(219, 243, 192, 1)',
              'rgba(63, 207, 255, 1)',
              'rgba(197, 231, 252, 1)',
              '#FFAA72',
              '#FF7B85',
              '#F5A5FF',
              '#A3FF9D',
              '#FFD700',
              '#696969',
              '#F4A460',
              '#FFA500',
              '#808000',
              '#00FA9A',
              '#48D1CC',
              '#3A15D3',
              '#AA45D2',
              '#BA95D1',
              '#FFD210',
              '#FFD501',
              '#FFD920',
              '#FF5730'
            ],

            series: [
              {
                name: '纳入监管商户分析',
                type: 'pie',
                radius: ['60%', '85%'],
                hoverOffset: 10, // 鼠标移上放大
                minAngle: 2,
                legendHoverLink: true, // 鼠标经过legend时，显示的数值
                center: ['25%', '50%'],
                avoidLabelOverlap: false,
                itemStyle: {
                  borderRadius: 6,
                  borderColor: '#fff',
                  borderWidth: 3
                },
                label: {
                  rich: {
                    color1: {
                      color: '#464369',
                      fontSize: 20
                    },
                    color2: {
                      color: '#87CEFA',
                      fontSize: 21
                    }
                  },
                  formatter:
                '{color1|' +
                '商户总数' +
                '}' +
                '\n' +
                '\n' +
                '{color2|' +
                total +
                '家' +
                '}',
                  // show: false,
                  position: 'center'
                },
                emphasis: {
                  label: {
                    show: true
                    // fontSize: '10',
                    // fontWeight: 'bold'
                  }
                },
                labelLine: {
                  show: false
                },
                // label: {
                //   show: false,
                //   position: 'center'
                // },
                // emphasis: {
                //   label: {
                //     show: true,
                //     fontSize: '10',
                //     fontWeight: 'bold'
                //   }
                // },
                // labelLine: {
                //   show: false
                // },
                data: list
              }
            ]
          })
        }
      }

      // this.chartOne.clear();
      // this.chartOne.setOption(dataOption);
      this.chartOne.on('mouseover', e => {
        // console.log(e);
        if (e.dataIndex != 0) {
          // 取消默认高亮
          this.chartOne.dispatchAction({
            type: 'downplay',
            seriesIndex: 0,
            dataIndex: 0
          })
        }
        if (e.dataIndex == 0) {
          // //鼠标悬停位置高亮
          this.chartOne.dispatchAction({
            type: 'highlight',
            seriesIndex: 0,
            dataIndex: 0
          })
        }
      })
      // 鼠标点击legend事件
      this.chartOne.on('legendselectchanged', params => {
        // setTimeout((res)=>{
        this.chartOne.dispatchAction({
          type: 'highlight',
          seriesIndex: 0,
          dataIndex: 0
        })
        // },1000)
      })
      // 鼠标移出后默认高亮
      this.chartOne.on('mouseout', e => {
        this.chartOne.dispatchAction({
          type: 'highlight',
          seriesIndex: 0,
          dataIndex: 0
        })
      })
      this.chartOne.dispatchAction({
        type: 'highlight',
        seriesIndex: 0,
        dataIndex: 0
      })
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
      this.chartOne.setOption(adapterOption)
      // 手动的调用图表对象的resize 才能产生效果
      // this.getEcharts();
      this.chartOne.resize()
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
      var now = parseTime(new Date()) // 当前时间
      // console.log(now, "now");
      // 发送后端时间
      this.body.startTime = '2022-01-01' + ' 00:00:00'
      this.body.endTime = now
      // 页面显示时间
      const systemStartTime = '2022-01-01' // 先默认一个时间
      const systemEndTime = now.slice(0, 10) // 去掉时分秒
      this.weekTime = [systemStartTime, systemEndTime]
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
  display: flex;
  justify-content: center;
}
.pieTopFlex {
  width: 100%;
  height: 100%;
  background: #ffffff;
  padding: 10px;
  /* border-radius: 10px; */
  overflow: hidden;
}
.dataLinePosition {
  position: relative;
  top: 0;
  left: 0;
}
/* .block{
  width:50%
} */
/*将日期里输入框icon隐藏*/
/* .prefix-icon-class{
  display: none;
} */
</style>
