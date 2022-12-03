<template>
  <view class="contentBox">
    <!-- <u-overlay :show="showLoading" @click="showLoading = false">
      <view class="rect">
        <u-loading-icon size="50" color="white"></u-loading-icon>
      </view>
    </u-overlay> -->
    <u-loading-page :loading="showLoading"></u-loading-page>
    <view style="height: 12px"></view>
    <view class="topData">
      <view class="mark-box">
        <uni-datetime-picker type="date" :value="value" start="2000-1-1"
          :end="this.$u.timeFormat(new Date().getTime(), 'yyyy-mm-dd')" @change="getDay">
          <view style="height: 54px" @click="open">
            <view v-show="dateShow">
              <view style="display: flex">
                <view class="dateStyle">{{ value }}</view>
                <u-icon name="arrow-down-fill" size="12"></u-icon>
              </view>
            </view>
          </view>
        </uni-datetime-picker>
      </view>
      <u-subsection inactiveColor="#1890ff" :list="list" @change="getTime" mode="subsection" :current="index">
      </u-subsection>
      <view class="account">
        <view class="top">
          <view class="dataTitle">
            <span>{{ activeText }}营业额（{{ activeUnit }}）</span>
          </view>
          <view class="number">
            {{
                allData.merchantCount.turnover
                  ? allData.merchantCount.turnover
                  : 0
            }}
          </view>
          <view class="compare">
            <span>{{ compareText
            }}{{ checkCount(allData.merchantCount.turnoverChange) }}</span>
            <img class="upOrDown" :src="changeUpOrDown(allData.merchantCount.turnoverChange)" alt="" />
          </view>
        </view>
        <view class="bottom">
          <view class="bottomItem" @click="toTradeDetail">
            <view class="dataTitle">{{ activeText }}交易笔数(笔)</view>
            <view class="number">{{ allData.merchantCount.transaction }}</view>
            <view class="compare">
              <span>{{ compareText
              }}{{
    checkCount(allData.merchantCount.transactionChange)
}}</span>
              <img class="upOrDown" :src="changeUpOrDown(allData.merchantCount.transactionChange)" alt="" />
            </view>
          </view>
          <view class="bottomItem">
            <view class="dataTitle">{{ activeText }}扫码数量(次)</view>
            <view class="number">{{ allData.merchantCount.scan }}</view>
            <view class="compare">
              <span>{{ compareText
              }}{{ checkCount(allData.merchantCount.scanChange) }}</span>
              <img class="upOrDown" :src="changeUpOrDown(allData.merchantCount.scanChange)" alt="" />
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 交易类型占比 -->

    <view class="chart_box">
      <view class="title">交易类型占比</view>
      <view class="chart">
        <qiun-data-charts type="ring" :canvas2d="true" canvasId="scrollringid" :chartData="allData.type"
          background="rgba(0,0,0,0)" :opts="ringTitle" />
      </view>
    </view>
    <!-- 交易金额统计 -->
    <view class="chart_box">
      <view class="title">交易金额统计(单位/{{ activeUnit }})</view>
      <view class="chart">
        <qiun-data-charts type="area" :canvas2d="true" canvasId="scrollareaid" :chartData="allData.money"
          background="rgba(0,0,0,0)" :ontouch="true" />
      </view>
    </view>
    <!-- 交易笔数统计 -->
    <view class="chart_box">
      <view class="title">交易笔数统计</view>

      <view class="chart">
        <qiun-data-charts type="column" :canvas2d="true" canvasId="scrollcolumnid" :chartData="allData.frequency"
          background="rgba(0,0,0,0)" :ontouch="true" />
      </view>
    </view>
    <!-- </mescroll-body> -->
  </view>
</template>

<script>
import MescrollMixin from "@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
import {
  fetchCountData,
  fetchFrequency,
  fetchMoney,
  fetchType,
} from "@/config/api.js";
export default {
  mixins: [MescrollMixin], // 使用mixin
  data() {
    return {
      activeUnit: "元",
      activeText: "今日",
      compareText: "较昨日",
      value: "", //当前选择的日期
      list: ["日报", "周报", "月报", "年报"],
      dateShow: true,
      index: 0,
      // 自定义属性配置  覆盖默认属性
      ringTitle: {
        title: {
          name: "",
          fontSize: 20,
          color: "#262626",
          offsetX: 0,
          offsetY: 0,
        },
      },
      allData: {
        // 交易类型占比
        type: {
          series: [
            {
              data: [],
            },
          ],
        },
        // 交易金额
        money: {
          categories: [],
          series: [
            {
              name: "交易金额",
              data: [],
            },
          ],
        },
        // 交易笔数
        frequency: {
          categories: [],
          series: [
            {
              name: "交易笔数",
              data: [],
            },
          ],
        },
        merchantCount: {}, //数字统计
      },
      showLoading: true,
      params: {},
    };
  },
  computed: {},
  onLoad() {
    this.value = this.$u.timeFormat(new Date().getTime(), "yyyy-mm-dd");
    this.params = {
      type: "day",
    };
    setTimeout(() => {
      this.getCountData();
    }, 500);
  },
  onUnload() { },
  methods: {
    close() {
      console.log(13232);
    },
    open() {
      this.flag = false;
    },
    downCallback() {
      this.mescroll.resetUpScroll();
    },
    //  改变增加减少标识
    changeUpOrDown(e) {
      const n = Number(e);
      if (n > 0) {
        return require("@/static/homeImages/比较增加.png");
      } else if (n === 0) {
        return "";
      } else {
        return require("@/static/homeImages/比较减少.png");
      }
    },
    // 比较数据
    checkCount(num) {
      const n = Number(num);
      if (n > 0) {
        return "+" + n;
      } else if (n === 0) {
        return " " + n;
      } else {
        return "-" + Math.abs(n);
      }
    },
    //获取统计数据
    async getCountData() {
      await fetchCountData(this.params).then((res) => {
        this.allData.merchantCount = res.data;
      });
      await fetchFrequency(this.params).then((res) => {
        this.allData.frequency.categories = res.data.xaxis;
        this.allData.frequency.series[0].data = res.data.yaxis;
      });
      await fetchMoney(this.params).then((res) => {
        this.allData.money.categories = res.data.xaxis;
        this.allData.money.series[0].data = res.data.yaxis;
      });
      await fetchType(this.params).then((res) => {
        const r = [];
        let total = 0;
        res.data.map((i) => {
          i.value = Number(i.value);
          total += Number(i.value);
          r.push(i);
        });
        this.ringTitle.title.name = total;
        this.allData.type.series[0].data = r;
        this.showLoading = false;
      });
      // uni.setStorageSync(this.params.type, this.allData);
    },
    getDay(e) {
      this.flag = true;
      this.params = {
        date: e,
      };
      this.value = e;
      this.getCountData();
    },
    getTime(e) {
      this.index = e;
      this.dateShow = e === 0 ? true : false;
      this.value = this.$u.timeFormat(new Date().getTime(), "yyyy-mm-dd");
      switch (e) {
        case 0:
          this.params = {
            type: "day",
          };
          this.activeText = "今日";
          this.compareText = "较昨日";
          this.activeUnit = "元"
          break;
        case 1:
          this.params = {
            type: "week",
          };
          this.activeText = "本周";
          this.compareText = "较上周";
          this.activeUnit = "元"
          break;
        case 2:
          this.params = {
            type: "month",
          };
          this.activeText = "本月";
          this.compareText = "较上月";
          this.activeUnit = "万"
          break;
        case 3:
          this.params = {
            type: "year",
          };
          this.activeText = "今年";
          this.compareText = "较去年";
          this.activeUnit = "万"
          break;

        default:
          break;
      }
      // if (uni.getStorageSync(this.params.type)) {
      //   this.allData = uni.getStorageSync(this.params.type);
      // } else {
      //   }
      this.getCountData();
    },
  },
};
</script>

<style lang="scss">
.contentBox {
  width: 100%;
  background: #f5f6f7;
  padding-bottom: 20px;

  .rect {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  // 顶部数据
  .topData {
    background: white;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .uni-datetime-picker {
      position: absolute;
    }

    .dateStyle {
      height: 22px;
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 600;
      color: #262626;
      line-height: 22px;
      margin: 16px 2px;
    }

    .u-subsection {
      width: 275px;
    }

    .account {
      margin: 16px 0;
      width: calc(100vw - 30px);
      height: 200px;
      background: #ffffff;
      box-shadow: 0px 2px 7px 0px rgba(177, 175, 175, 0.5);
      border-radius: 6px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;

      .compare {
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #8c8c8c;
        line-height: 16px;

        .upOrDown {
          height: 10px;
          width: 9px;
          margin: 0 4px;
        }
      }

      .top {
        text-align: center;

        .dataTitle {
          height: 16px;
          font-size: 13px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #595959;
          line-height: 16px;
        }

        .number {
          height: 40px;
          font-size: 28px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #262626;
          line-height: 40px;
          margin: 5px 0;
        }

        .compare {
          font-size: 13px;
        }
      }

      .bottom {
        margin-top: 10px;
        width: 90%;
        display: flex;

        .bottomItem {
          width: 50%;
          border-top: 1px solid #f5f6f7;
          padding: 10px 0;
          text-align: center;

          .dataTitle {
            height: 16px;
            font-size: 13px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #595959;
            line-height: 16px;
          }

          .number {
            height: 30px;
            font-size: 22px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #262626;
            line-height: 30px;
            margin: 5px 0;
          }

          .compare {
            font-size: 11px;
          }
        }

        .bottomItem:nth-child(1) {
          border-right: 1px solid #f5f6f7;
        }
      }
    }
  }

  .title {
    width: 100%;
    text-align: center;
    font-size: 14px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #262626;
    margin-top: 20px;
  }

  .mark-box {
    display: flex;
    justify-content: center;
    width: 100%;
    height: 75%;
    z-index: 10000 !important;
  }

  // 图表
  .chart_box {
    width: 100vw;
    height: 300px;
    background: #ffffff;
    margin-top: 12px;
    padding-bottom: 12px;
    display: flex;
    align-items: center;
    flex-direction: column;

    .chart {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
