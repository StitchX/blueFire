<template>
  <view class="box">
    <mescroll-body @init="mescrollInit" @down="downCallback" @up="upCallback" :up="option" :down="{ auto: false }">
      <u-navbar title="交易明细" @leftClick="backHome" bgColor="transparent" titleStyle="color:white;font-weight:bolder"
        leftIconColor="white">
      </u-navbar>
      <view class="todayData">
        <view class="dataShow">
          <view class="title"> 营业额（元） </view>
          <u-count-to :startVal="0" color="#fff" fontSize="26px" :endVal="sumPrice" :decimals="2"></u-count-to>
        </view>
      </view>
      <view class="dateBox">
        <uni-datetime-picker type="date" :value="value" start="2000-1-1"
          :end="this.$u.timeFormat(new Date().getTime(), 'yyyy-mm-dd')" @change="change">
          <view style="display: flex">
            <view class="dateStyle">{{ value }}</view>
            <u-icon name="arrow-down-fill" size="12"></u-icon>
          </view>
        </uni-datetime-picker>
        <view class="tradeList">
          <view class="tradeItem" v-for="(item, index) in tradeList" :key="index">
            <view class="left">
              <img v-if="item.type === 1" src="../../../static/homeImages/微信.png" alt="" />
              <img v-else-if="item.type === 2" src="../../../static/homeImages/支付宝.png" alt="" />
              <img v-else src="../../../static/homeImages/其他.png" alt="" />
              <view class="text">
                <view class="payType">{{ changeType(item.type) }}</view>
                <view class="time">{{ item.callbackTime }}</view>
              </view>
            </view>
            <view class="right">
              <span>+{{ item.price }}</span>
            </view>
          </view>
          <view class="null" v-if="tradeList.length === 0">
            <img class="nullData" src="@/static/暂无数据.png" alt="" />
            <view class="text">暂无数据</view>
          </view>
        </view>
      </view>
    </mescroll-body>
  </view>
</template>

<script>
import MescrollMixin from "@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
import { fetchTradeDetail } from "@/config/api.js";
export default {
  mixins: [MescrollMixin], // 使用mixin
  data() {
    return {
      value: "",
      tradeList: [],
      sumPrice: "",
      query: {
        queryDate: "",
        current: 1,
        size: 10,
      },
      option: {
        auto: true,
        noMoreSize: 3,
        textNoMore: "没有更多了",
        empty: {
          use: false
        },
      },
    };
  },
  onLoad() {
    this.value = this.$u.timeFormat(new Date().getTime(), "yyyy-mm-dd");
    // this.getDetail(this.value);
  },
  methods: {
    backHome() {
      uni.navigateBack({
        delta: 2,
      });
    },
    downCallback() {
      this.mescroll.resetUpScroll();
    },
    upCallback(page) {
      this.query.current = page.num;
      this.query.queryDate = this.value;
      fetchTradeDetail(this.query)
        .then((res) => {
          let arr = res.data.records;
          if (page.num === 1) this.tradeList = [];
          setTimeout(() => {
            this.tradeList = this.tradeList.concat(arr);
            this.mescroll.endSuccess(arr.length);
          }, 200);
        })
        .catch((err) => {
          this.mescroll.endErr();
        });
    },
    async getDetail(e) {
      uni.showLoading({
        title: "加载中",
        mask: true,
        duration: 1000,
      });
      this.query.queryDate = e;
      await fetchTradeDetail(this.query)
        .then((res) => {
          this.tradeList = res.data.records;
          this.sumPrice = res.data.sumPrice;
        })
        .catch((err) => { });
    },
    changeType(i) {
      switch (i) {
        case 1:
          return "微信收款";
        case 2:
          return "支付宝收款";

        default:
          return "其他";
      }
    },
    change(e) {
      this.value = e;
      this.getDetail(e);
    },
  },
};
</script>

<style lang="scss">
.box {
  width: 100%;
  display: flex;
  flex-direction: column;
  background: #f5f6f7;
}

.todayData {
  width: 100%;
  height: 200px;
  background: linear-gradient(to bottom right, #4db4fc, #0051d3);

  .dataShow {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;

    .title {
      margin-top: 60px;
      height: 16px;
      font-size: 13px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: rgba(255, 255, 255, 0.56);
      line-height: 16px;
    }

    .u-count-num {
      margin-top: 15px;
    }
  }
}

.dateBox {
  background: #f5f6f7;
  width: 100%;

  .dateStyle {
    height: 22px;
    font-size: 16px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 500;
    color: #262626;
    line-height: 22px;
    margin: 12px 15px;
  }

  .tradeList {
    .null {
      margin: 30px 0;
      text-align: center;

      .nullData {
        width: 120px;
        height: 120px;
      }

      .text {
        margin-top: 12px;
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #8c8c8c;
        line-height: 20px;
      }
    }

    .tradeItem {
      height: 80px;
      display: flex;
      margin: 1px 0;
      background: white;
      align-items: center;
      justify-content: space-between;

      .left {
        display: flex;
        align-items: center;

        img {
          width: 45px;
          height: 45px;
          margin: 18px;
        }

        .text {
          .payType {
            font-size: 16px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #292929;
          }

          .time {
            height: 20px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #8c8c8c;
            line-height: 20px;
            margin-top: 9px;
          }
        }
      }

      .right {
        height: 80px;
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 700;
        color: #292929;
        position: relative;

        span {
          position: absolute;
          top: 16px;
          right: 22px;
        }
      }
    }
  }
}
</style>
