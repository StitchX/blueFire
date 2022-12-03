<template>
  <view class="guide" v-if="showGuide">
    <view :style="guideStyle" class="guide-box">
      <view class="tips" :style="tipPosition">
        <view class="text">{{ guideInfo.tips }}</view>
        <view class="tool-btn">
          <view class="next" @click="next">我知道啦</view>
          <!-- <text @click="skip">跳过</text> -->
          <!-- <view class="next" style="" @click="next">下一步</view> -->
        </view>
      </view>

      <img
        class="arrow-icon"
        :style="arrowPosition"
        src="@/static/homeImages/编组.png"
        alt=""
      />
    </view>
    <!-- 遮罩层，防止点击 -->
    <view class="v-model"></view>
  </view>
</template>

<script>
export default {
  name: "guideStep",
  props: {
    step: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      stepName: "step", //该提示步骤的名称，用于不在重复展示
      guideList: [],
      transForm: "", //翻转图片
      index: 0, // 当前展示的索引
      showGuide: true, // 是否显示引导
      guideStyle: "", // 默认样式
      arrowPosition: "", //步骤提示箭头的定位
      tipPosition: "", //步骤提示的定位
      systemInfo: "", //屏幕宽度高度等信息
      tipWidth: 200, //步骤提示默认的宽度
    };
  },
  computed: {
    guideInfo() {
      return this.guideList[this.index];
    },
  },
  mounted() {
    console.log(this.step, "step");
    this.guideList = this.step.guideList;
    this.stepName = this.step.name;
    // const systemInfo = uni.getSystemInfoSync();
    // this.systemInfo = systemInfo;
    const guide = uni.getStorageSync(this.stepName);
    if (!guide) {
      this.getDomInfo();
    } else {
      this.showGuide = false;
    }
  },
  methods: {
    start() {},
    // 展示新手提示
    viewTips(data, scrollTop) {
      if (data) {
        console.log(data);
        // 如果dom宽度大于或者等于窗口宽度,需要重新调整dom展示宽度
        // let newWidth = this.systemInfo.windowWidth - 20;
        // if (data.width >= newWidth) {
        //   data.width = newWidth;
        // }
        // // 如果距离左边为0,自动增加一点左边距
        // if (data.left == 0) {
        //   data.left = 10;
        // }
        // let domRW = this.systemInfo.windowWidth - data.left;
        // let left = 0;
        // 如果dom距离右边没有tips的宽度大的话,就要让tips向左偏移
        // if (domRW < this.tipWidth) {
        //   left = domRW - this.tipWidth - 100;
        // }
        const index = this.index;
        // // 步骤条展示的高度需要加上屏幕滚动的高度
        // data.top += scrollTop;
        // // 根据实际情况需要滚动到展示区域
        // uni.pageScrollTo({
        //   scrollTop: data.top > 20 ? data.top - 20 : 0,
        //   duration: 100,
        // });
        let obj = Object.assign(this.guideList[index], data);
        // 设置引导线的位置
        // let arrowTop = data.height + 10;
        // if (domRW < this.tipWidth) {
        //   this.arrowPosition = "top:" + arrowTop + "px;left:" + 16 + "px;";
        // } else {
        //   this.arrowPosition = "top:" + arrowTop + "px;";
        // }
        // 设置提示框定位
        // this.tipPosition = "top:" + (arrowTop + 50) + "px;left:" + left + "px;";
        // 重新设置guideList的值
        this.guideList.splice(index, 1, obj);
        this.guideStyle = this.getStyle();
      } else {
        this.index += 1;
        this.getDomInfo();
      }
    },
    // 获取步骤提示的主要样式
    getStyle() {
      // console.log(this.guideInfo)
      const { width, height, left, top, viewStyle, arrowStyle, tipStyle } =
        this.guideInfo;
      let newstyle = "width:" + width + "px;";
      newstyle += "height:" + height + "px;";
      newstyle += "left:" + left + "px;";
      newstyle += "top:" + top + "px;";
      newstyle +=
        "box-shadow: rgb(33 33 33 / 80%) 0px 0px 0px 0px, rgb(33 33 33 / 70%) 0px 0px 0px 5000px;";
      newstyle += viewStyle;
      if (this.guideInfo.step === 3) {
        this.arrowPosition = arrowStyle + "transform:rotateY(180deg)";
      } else if (this.guideInfo.step === 4) {
        this.arrowPosition = arrowStyle + "transform:rotate(170deg)";
      } else if (this.guideInfo.step === 5) {
        this.arrowPosition = arrowStyle + "transform:rotate(180deg)";
      } else {
        this.arrowPosition = arrowStyle;
      }
      this.tipPosition = tipStyle;
      return newstyle;
    },
    // 获取dom信息
    getDomInfo() {
      this.guideStyle = this.getStyle();
      // const { el } = this.guideInfo;
      // const query = uni.createSelectorQuery().in(this.$root);
      // setTimeout(() => {
      //   query.select(el).boundingClientRect();
      //   query.selectViewport().scrollOffset();
      //   var _this = this;
      //   query.exec(function (res) {
      //     // console.log("打印demo的元素的信息", res);
      //     let data = res[0]; // #the-id节点的上边界坐标
      //     let scrollTop = res[1].scrollTop; // 显示区域的竖直滚动位置
      //     _this.viewTips(data, scrollTop);
      //   });
      // }, 10);
    },
    // 跳过新手提示
    skip() {
      this.showGuide = false;
      uni.setStorageSync(this.stepName, "true");
    },
    // 下一步
    next() {
      if (this.index === this.guideList.length - 1) {
        this.showGuide = false;
        uni.setStorageSync(this.stepName, "true");
      } else {
        this.index += 1;
        this.getDomInfo();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.v-model {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
}
.guide {
  z-index: 1001;
  .guide-box {
    position: absolute;
    z-index: 10001;
    transition: all 0.2s;
    &::before {
      content: "";
      height: 100%;
      width: 100%;
      // border: 1px dashed #fff;
      border-radius: 8rpx;
      position: absolute;
      top: -8rpx;
      left: -8rpx;
      padding: 7rpx;
    }
    .arrow-icon {
      height: 50px;
      width: 50px;
      position: absolute;
    }
    .tips {
      left: -100px;
      // background: linear-gradient(180deg, #1cbbb4, #0081ff);
      // box-shadow: 0px 2px 9px 0px rgba(0, 0, 0, 0.1);
      color: #fff;
      position: absolute;
      padding: 15rpx 20rpx;
      font-size: 28rpx;
      border-radius: 12rpx;
      .text {
        height: 20px;
        font-size: 14px;
        font-family: AlibabaPuHuiTi-Medium, AlibabaPuHuiTi;
        font-weight: 500;
        color: #ffffff;
        line-height: 20px;
      }
      .tool-btn {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 0rpx;
        margin-top: 20rpx;
        .next {
          border: 1px solid white;
          border-radius: 20px;
          color: white;
          margin-top: 10px;
          font-weight: 600;
          padding: 5px 10px;
          text-align: center;
          line-height: 48rpx;
          font-size: 14px;
        }
      }
    }
  }
}
</style>
