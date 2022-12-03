<template>
  <view style="position:relative">
    <image src="@/static/homeImages/downloadBg.png" mode="scaleToFill" />
    <u-navbar
      title="店铺收款码"
      :autoBack="true"
      bgColor="transparent"
      titleStyle="color:white;font-weight:bolder"
      leftIconColor="white"
    >
    </u-navbar>
    <view class="box">
			<img v-if="qrCodeUrl !==''" :src="qrCodeUrl" :style="activeHeight" class="qrCode" alt="">
      <view v-else class="qrCode">
        <img src="@/static/homeImages/二维码失效.png" alt="">
      </view>
      <view class="button" @click="saveImage">下载店铺收款码</view>
		</view>
  </view>
</template>

<script>
export default {
  onLoad: function (option) {
    //option为object类型，会序列化上个页面传递的参数
    this.qrCodeUrl = option.qrCode; //打印出上个页面传递的参数。
  },
  data() {
    return {
      qrCodeUrl: null,
      activeHeight: "height:50vh",
    };
  },
  created() {
    let systemInfo = uni.getSystemInfoSync();
    if (systemInfo.windowWidth > 400) {
      this.activeHeight = "height:420px"
    }else if(systemInfo.windowWidth < 350){
      this.activeHeight = "height:300px"
    }else{
      this.activeHeight = "height:370px"
    }
  },
  methods: {
    saveImage() {
      uni.showLoading({
        title: "保存中...",
        mask: true,
      });
      if (this.qrCodeUrl) {
        wx.downloadFile({
          url: this.qrCodeUrl,
          success: function (res) {
            if (res.statusCode === 200) {
              let img = res.tempFilePath;
              wx.saveImageToPhotosAlbum({
                filePath: img,
                success(res) {
                  uni.showToast({
                    title: "保存成功",
                    icon: "success",
                    duration: 2000,
                  });
                },
                fail(res) {
                  uni.showToast({
                    title: "保存失败",
                    icon: "error",
                    duration: 2000,
                  });
                },
              });
            }
          },
        });
      } else {
        uni.showToast({
          title: "保存失败",
          icon: "error",
          duration: 2000,
        });
      }
    },
  },
};
</script>

<style lang="scss">
image {
  display: block;
  height: 100vh;
  width: 100vw;
  will-change: transform;
}
.box {
  top: 0;
  width: 100vw;
  height: 100vh;
  position: absolute;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
}
.qrCode {
  width: calc(100vw - 110px);
  display: flex;
  justify-content: center;
  align-items: center;
  img {
    width: 50px;
    height: 50px;
  }
}
.button {
  margin-top: 31px;
  width: calc(100vw - 85px);
  height: 44px;
  background: #1890ff;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #ffffff;
  line-height: 44px;
  text-align: center;
  border-radius: 15px;
  &:active {
    color: #99ccfc;
  }
}
</style>
