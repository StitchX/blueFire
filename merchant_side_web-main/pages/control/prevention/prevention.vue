<template>
  <view>
    <view class="null">
      <img class="nullData" src="@/static/暂无数据.png" alt="" />
      <view class="text">暂无数据</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
};
</script>

<style lang="scss">
.null {
  margin-top: 144px;
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
</style>
