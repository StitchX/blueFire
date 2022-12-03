<template>
  <u-transition mode="fade" duration="500" :show="true">
    <mescroll-body @init="mescrollInit" @down="downCallback" @up="upCallback" :down="{ auto: false }"
      :up="{ use: false }">
      <view class="topMerchant">
        <!-- 顶部名称，二维码 -->
        <view class="merchant">
          <img class="merQrCode" @click="toQrCode(merchantInfo.payCodeUrl)" src="@/static/homeImages/商户二维码.png">
          <view class="name">{{ merchantInfo.storeName }}</view>
        </view>
        <!-- 当日营业数据展示 -->
        <view class="account">
          <view class="analysis" @click="toChart">
            <span>统计分析</span>
            <u-icon size="12" color="#217ce5" name="arrow-right"></u-icon>
          </view>
          <view class="top">
            <view class="title">
              <span>今日营业额（元）</span>
              <u-icon size="16" class="hideIcon" :name="activeIcon" color="#8FFFFFF" @click="hideData"></u-icon>
            </view>
            <section v-show="showFlag" @click="toTradeDetail(merchantCount.turnover)">
              <u-loading-icon color="white" :show="showCount"></u-loading-icon>
              <section v-show="!showCount">
                <view class="number">
                  <u-count-to :startVal="0" color="#fff" fontSize="26px" :endVal="merchantCount.turnover" :decimals="2">
                  </u-count-to>
                </view>
                <view class="compare">
                  <span>较昨日{{ checkCount(merchantCount.turnoverChange) }}</span>
                  <img class="upOrDown" :src="changeUpOrDown(merchantCount.turnoverChange)" alt="">
                </view>
              </section>
            </section>
            <section v-show="!showFlag">
              <view class="number">
                *******
              </view>
              <view class="compare">
                <span>*******</span>
              </view>
            </section>
          </view>
          <view class="bottom" @click="toTradeDetail(merchantCount.turnover)">
            <view class="bottomItem">
              <view class="title">当日交易笔数(笔)</view>
              <section v-show="showFlag" @click="toTradeDetail(merchantCount.turnover)">
                <u-loading-icon color="white" :show="showCount"></u-loading-icon>
                <section v-show="!showCount">
                  <view class="number">{{ merchantCount.transaction }}</view>
                  <view class="compare">
                    <span>较昨日{{ checkCount(merchantCount.transactionChange) }}</span>
                    <img class="upOrDown" :src="changeUpOrDown(merchantCount.transactionChange)" alt="">
                  </view>
                </section>
              </section>
              <section v-show="!showFlag">
                <view class="number">
                  *******
                </view>
                <view class="compare">
                  <span>*******</span>
                </view>
              </section>
            </view>
            <view class="bottomItem">
              <view class="title">当日扫码数量(次)</view>
              <section v-show="showFlag" @click="toTradeDetail(merchantCount.turnover)">
                <u-loading-icon color="white" :show="showCount"></u-loading-icon>
                <section v-show="!showCount">
                  <view class="number">{{ merchantCount.scan }}</view>
                  <view class="compare">
                    <span>较昨日{{ checkCount(merchantCount.scanChange) }}</span>
                    <img class="upOrDown" :src="changeUpOrDown(merchantCount.scanChange)" alt="">
                  </view>
                </section>
              </section>
              <section v-show="!showFlag">
                <view class="number">
                  *******
                </view>
                <view class="compare">
                  <span>*******</span>
                </view>
              </section>
            </view>
          </view>
        </view>
      </view>
      <view class="bottomOption">
        <!-- 消息通知列表 -->
        <view class="optionBox" @click="toNotice">
          <view class="nullMsg" v-if="msgList.length === 0">暂无新通知</view>
          <view class="leftList" v-else>
            <view class="msgItem" v-for="item in msgList" :key="item.noticeId">
              <span class="point"></span>
              <span class="msgTitle">{{ exchangeNoticeType(item.noticeType, item.noticeTitle) }}</span>
              <span class="msgTime">{{ $u.timeFormat(item.msgSendTime, 'mm-dd') }}</span>
            </view>
          </view>
          <view class="toMoreMsg">
            <span class="morePoint" v-if="msgList.length > 0"></span>
            <u-icon name="arrow-right"></u-icon>
          </view>
        </view>
        <!-- 绿码 -->
        <view class="optionBox">
          <view class="left">
            <img src="@/static/homeImages/绿码经营商户.png" alt="">
            <span>绿码经营商户</span>
          </view>
          <view class="right">
            <img class="greenCode" src="@/static/homeImages/绿码.png" alt="">
          </view>
        </view>
        <!-- 强制险 -->
        <view class="optionBox optionBoxSafe">
          <view class="left">
            <img src="@/static/homeImages/食品安全.png" alt="">
            <span>食品安全强制保险</span>
            <u-tag v-if="merchantInfo.foodSafetyStatus === 2" size="mini" text="已开通" type="success" plain plainFill>
            </u-tag>
          </view>
          <view class="right">
            <button @click="join('safe')" v-if="merchantInfo.foodSafetyStatus !== 2" type="primary" :class="merchantInfo.foodSafetyStatus === 0
          ? 'custom'
          : merchantInfo.foodSafetyStatus === 1
            ? 'custom inProcess'
            : 'isDone'" :disabled="merchantInfo.foodSafetyStatus !== 0"></button>
            <img class="safe" v-else src="@/static/homeImages/强制险.png" alt="">
          </view>
        </view>
        <!-- 银行支付 -->
        <view class="optionBox optionBoxBank">
          <view class="left">
            <img src="@/static/homeImages/银行支付.png" alt="">
            <span>银行支付</span>
            <u-tag v-if="merchantInfo.payStatus === 2" size="mini" text="已开通" type="success" plain plainFill></u-tag>
          </view>
          <view class="right">
            <button @click="join('bank')" :disabled="merchantInfo.payStatus !== 0" v-if="merchantInfo.payStatus !== 2"
              type="primary" :class="merchantInfo.payStatus === 0
            ? 'custom'
            : merchantInfo.payStatus === 1
              ? 'custom inProcess'
              : 'isDone'"></button>
            <!-- @/static/homeImages/银行.png -->
            <img class="bank" v-else :src="changeBank(merchantInfo.bankType)" alt="">
          </view>
        </view>
        <u-modal :show="joinShow" @confirm="confirm" title="欢迎入驻三码合一" :content='content'>

        </u-modal>
      </view>
    </mescroll-body>

    <guideStep v-if="token" :step="step"></guideStep>
  </u-transition>
</template>

<script>
import MescrollMixin from "@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
import {
  fetchMerchantData,
  fetchCountData,
  fetchFoodSafety,
  fetchPay,
  sendBankMsgBd,
  sendSafeMsgBd,
} from "@/config/api.js";
import { apinNotice } from "@/config/notice.js";
import guideStep from "@/components/guideStep/guideStep.vue";
export default {
  mixins: [MescrollMixin], // 使用mixin
  components: {
    guideStep,
  },
  data() {
    return {
      // 新手导航列表(未完成)
      step: {
        name: "guideFlag",
        guideList: [
          {
            step: 1,
            el: ".top",
            tips: "点击“今日营业额”可查看交易明细",
            viewStyle:
              "border-radius:50%;width:163px;height:102px;left:28vw;top:90px",
            arrowStyle: "top:95px;right:35px",
            tipStyle: "top:140px",
          },
          {
            step: 2,
            el: ".analysis",
            tips: "点击“统计分析”可查看不同纬度的分析",
            viewStyle:
              "border-radius:50%;height:48px;width:92px;right:-10px;top:95px",
            arrowStyle: "top:47px;",
            tipStyle: "top:90px;left:-200px",
          },
          {
            step: 3,
            el: ".merQrCode",
            tips: "点击“收款码”可下载店铺收款二维码",
            viewStyle:
              "border-radius:50%;height:60px;width:60px;left:0;top:38px",
            arrowStyle: "top:46px;left:45px;",
            tipStyle: "top:91px;left:0;right:-201px",
          },
          {
            step: 4,
            el: ".optionBoxSafe",
            tips: "点击“食品安全险”开通食品安全险， 百万保障更放心！",
            viewStyle:
              "border-radius:15px;height:60px;width:calc(100vw - 24px);left:12px;top:470px",
            arrowStyle: "top:-51px;left:4px;",
            tipStyle: "top:-144px;right:-30px;left:0;",
          },
          {
            step: 5,
            el: ".optionBoxBank",
            tips: "点击“银行支付”开通银行支付， 可选择不同的银行支付哦！",
            viewStyle:
              "border-radius:15px;height:60px;width:calc(100vw - 24px);left:12px;top:552px",
            arrowStyle: "top:-51px;left:4px;",
            tipStyle: "top:-144px;right:-30px;left:0;",
          },
        ],
      },
      status: 0,
      activeStyle: "custom", //状态样式切换
      activeIcon: "eye", //是否隐藏数据图标切换
      showFlag: true, //是否隐藏数据,
      msgList: [], //通知列表
      merchantCount: "", //商户营业额数据
      merchantInfo: "", //商户基本信息
      joinShow: false,
      joinType: "",
      showCount: true, //数据加载效果
      upOrDown: require("@/static/homeImages/比较增加.png"), //增加减少标识路径
      content: "",
      token: null
    };
  },
  created() {
    this.token = uni.getStorageSync("token");
    if (this.token) {
      this.getCountData();
      this.getNotice();
      this.getMerchantData();
    } else {
      uni.showModal({
        title: "",
        showCancel: false,
        confirmColor: "#03A9F4",
        content: "仅限放心码入驻商户使用，请前往登录。",
        success: function (res) {
          if (res.confirm) {
            uni.redirectTo({
              url: "/pages/login/login",
            });
          }
        },
      });
      return;
    }
  },
  onShow() {
    this.getNotice();
  },
  methods: {
    changeBank(type) {
      switch (type) {
        case 1:
          return '../../static/homeImages/3.png';
        case 2:
          return '../../static/homeImages/5.png';
        case 3:
          return '../../static/homeImages/6.png';
        case 4:
          return '../../static/homeImages/1.png';
        case 5:
          return '../../static/homeImages/2.png';
        case 6:
          return '../../static/homeImages/4.png';
        default:
          break;
      }
    },
    downCallback() {
      this.getCountData();
      this.getNotice();
      this.getMerchantData();
    },
    async confirm() {
      if (this.joinType === "safe") {
        await fetchFoodSafety().then((result) => {
          if (result.status === "200") {
            this.merchantInfo.foodSafetyStatus = 1;
          }
        });
        await sendSafeMsgBd().then(() => {
          this.joinShow = false;
        });
      } else {
        await fetchPay().then((res) => {
          if (res.status === "200") {
            this.merchantInfo.payStatus = 1;
          }
        });
        await sendBankMsgBd().then(() => {
          this.joinShow = false;
        });
      }
    },
    join(e) {
      this.joinShow = true;
      this.joinType = e;
    },
    exchangeNoticeType(e, k) {
      switch (e) {
        case 1:
          return "政策法规:" + k;
        case 2:
          return "培训学习:" + k;
        case 3:
          return "考试通知:" + k;
        case 4:
          return "开会通知:" + k;
        case 5:
          return "处罚通知:" + k;
        case 6:
          return "监管信息:" + k;

        default:
          break;
      }
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
      this.showCount = true;
      await fetchCountData({}).then((res) => {
        this.showCount = false;
        this.merchantCount = res.data;
      });
    },
    //获取通知数据
    async getNotice() {
      const page = {
        current: 1,
        size: 2,
        readStatus: 0,
      };
      await apinNotice(page).then((res) => {
        this.msgList = res.data.noticeVoIPage.records;
      });
    },
    // 获取商户数据
    async getMerchantData() {
      await fetchMerchantData()
        .then((res) => {
          this.merchantInfo = res.data;
          this.content =
            "欢迎入驻三码合一，稍后工作人员会主动联系您，请耐心等待！您的专属客户经理电话：" +
            this.merchantInfo.bdPhone;
          this.mescroll.endSuccess();
        })
        .catch((err) => { });
    },
    // 是否隐藏数据
    hideData() {
      this.activeIcon = this.activeIcon === "eye" ? "eye-off" : "eye";
      this.showFlag = this.showFlag ? false : true;
    },
    // 前往图表
    toChart() {
      uni.navigateTo({
        url: "./chart/chart",
      });
    },
    // 前往下载店铺收款码
    toQrCode(qr) {
      uni.navigateTo({
        url: "./downloadQRcode/downloadQRcode?qrCode=" + qr,
      });
    },
    // 前往交易明细
    toTradeDetail(e) {
      uni.navigateTo({
        url: "./tradeDetail/tradeDetail?turnover=" + e,
      });
    },
    // 前往消息通知
    toNotice() {
      uni.switchTab({
        url: "/pages/notice/notice",
      });
    },
  },
};
</script>

<style lang="scss">
// .step1 {
// 	text-align: center;
// 	line-height: 100rpx;
// 	width: 100rpx;
// 	height: 100rpx;
// }

// .content1 {
// 	border: 1px solid red;
// 	display: flex;
// 	align-items: center;
// 	justify-content: center;
// }

// -------------------------顶部商家当日经营数据-------------------------------//
.topMerchant {
  width: 100%;
  border: 1px solid rgba($color: #000000, $alpha: 0);
  background: linear-gradient(to bottom right, #4db4fc, #0051d3);

  .merchant {
    display: flex;
    margin: 55px 0 24px 0;
    align-items: center;

    .merQrCode {
      height: 20px;
      width: 20px;
      margin: 0 16px;
    }

    .name {
      width: calc(100% - 150px);
      height: 25px;
      font-size: 18px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 25px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .account {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    position: relative;

    .hideIcon {
      position: absolute;
    }

    .analysis {
      position: absolute;
      right: 0;
      top: 0;
      width: 76px;
      height: 24px;
      font-size: 12px;
      background: rgba(255, 255, 255, 0.9);
      box-shadow: 0px 0px 8px 0px rgba(81, 82, 84, 0.08);
      border-radius: 42px 0 0 42px;
      display: flex;
      align-items: center;
      justify-content: space-around;

      span {
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #217ce5;
        margin-left: 7px;
      }
    }

    .title {
      width: 100%;
      font-size: 13px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: rgba(255, 255, 255, 0.56);
      line-height: 16px;
      display: flex;
      justify-content: center;
    }

    .number {
      width: 100%;
      font-size: 22px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      line-height: 30px;
      display: flex;
      justify-content: center;
    }

    .compare {
      display: flex;
      justify-content: center;
      width: 100%;
      font-size: 11px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      line-height: 16px;

      .upOrDown {
        height: 10px;
        width: 9px;
        margin: 0 4px;
      }
    }

    .u-loading-icon {
      margin: 21px 0;
    }

    .top {
      text-align: center;

      .number {
        font-size: 28px;
        margin-top: 8px;
        height: 40px;
      }

      .compare {
        font-size: 13px;
      }
    }

    .bottom {
      margin-top: 27px;
      width: 100%;
      display: flex;
      justify-content: space-around;

      .bottomItem {
        text-align: center;

        .number {
          height: 30px;
          margin-top: 4px;
        }

        .compare {
          margin-bottom: 16px;
        }
      }
    }
  }
}

.bottomOption {
  width: 100%;
  min-height: 330px;
  height: calc(100vh - 296px);
  background: #f5f6f7;
  display: flex;
  align-items: center;
  flex-direction: column;
  box-shadow: inset 0 -5px 5px -6px #8d8d8d;
}

.optionBox {
  width: calc(100vw - 24px);
  height: 68px;
  background: #ffffff;
  margin-top: 12px;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .nullMsg {
    margin: 0 auto;
    color: #8d8d8d;
  }

  // -------------------------------------------通知列表-------------------------//
  .leftList {
    .msgItem {
      display: flex;
      align-items: center;
      margin: 6px 0;

      .point {
        width: 3px;
        height: 3px;
        background: #262626;
        margin: 0 7px;
        border-radius: 50%;
      }

      .msgTitle {
        width: calc(100vw - 35vw);
        height: 18px;
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 550;
        color: #262626;
        line-height: 18px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        word-break: break-all;
      }

      .msgTime {
        width: 40px;
        height: 18px;
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #8c8c8c;
        line-height: 18px;
        margin-left: 7px;
      }
    }
  }

  .toMoreMsg {
    width: 30px;
    height: 100%;
    justify-content: space-around;
    display: flex;
    align-items: center;

    .morePoint {
      width: 8px;
      height: 8px;
      background: #f66329;
      border-radius: 50%;
    }
  }

  // --------------------------------------------开通项样式-------------------------------//
  .left {
    display: flex;
    align-items: center;

    img {
      height: 36px;
      width: 36px;
      margin: 0 16px;
    }

    span {
      font-size: 14px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 900;
      color: #262626;
      line-height: 20px;
      margin-right: 6px;
    }
  }

  .right {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;

    .custom {
      width: 70px;
      height: 28px;
      font-size: 12px;
      border-radius: 16px;
      background: #1890ff;
      margin: 0 16px;

      &::before {
        content: "去开通";
      }
    }

    .inProcess {
      background: #ff9318;

      &::before {
        content: "开通中";
      }
    }

    .isDone {}

    .greenCode {
      position: absolute;
      top: 0;
      right: 0;
      height: 53px;
      width: 63px;
    }

    .safe {
      width: 37px;
      height: 37px;
      margin: 0 12px;
    }

    .bank {
      margin: 0 12px;
      width: 37px;
      height: 37px;
    }
  }
}
</style>
