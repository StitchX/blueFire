<template>
  <view>
    <view class="card">
      <view class="card_top">
        <span class="left">上传溯源单</span>
        <span class="right">（最多6张，每张图片大小500KB以内）</span>
      </view>
      <view class='line'></view>
      <view class='uploadBox'>
        <u-upload :fileList="fileList" :sizeType="['compressed']" :previewFullImage="true" @delete="deletePic"
          @afterRead="afterRead" name="1" :multiple="true" :maxCount="6">
          <view class="uploadIcon">
            <u-icon name="camera" size="34"></u-icon>
            <view class='text'>
              上传图片
            </view>
          </view>
        </u-upload>
      </view>
    </view>
    <view class='confirm' @click="clickUpload">
      确认上传
    </view>
    <view class='check' @click="toHistory">
      查看上传历史列表
    </view>
  </view>
</template>

<script>
import { submitSource } from '@/config/api.js'
export default {
  data() {
    return {
      fileList: [],
    };
  },
  methods: {
    toHistory() {
      uni.navigateTo({
        url: '/pages/control/origin/history'
      });
    },
    // 点击上传按钮
    async clickUpload() {
      if (this.fileList.length === 0) {
        return
      }
      uni.showLoading({
        title: '上传中...'
      })
      const images = []
      this.fileList.map(i => {
        images.push(i.url)
      })
      submitSource({ images }).then(res => {
        if (res.status == 200) {
          uni.hideLoading();
          uni.showToast({
            title: '提交成功！',
            duration: 1000
          });
          this.fileList = []
        }
      })
    },
    // 上传文件
    uploadFilePromise(item) {
      console.log(item)
      return new Promise((resolve, reject) => {
        uni.uploadFile({
          url: uni.$u.http.config.baseURL + '/upload/merchantSource',
          filePath: item.url,
          name: 'file',
          formData: {
            user: 'test'
          },
          success: (result) => {
            const res = JSON.parse(result.data)
            console.log(res)
            if (res.status === "200") {
              this.fileList.push({
                size:item.size,
                thumb:res.data,
                type:"image",
                url:res.data
              })
              resolve(res.data)
            } else {
              uni.$u.toast(`上传失败！${res.status}`)
            }

          },
          fail: (err) => {
            uni.$u.toast("上传失败，请重新上传！")
          }
        });
      })
    },
    // 删除图片
    deletePic(event) {
      this.fileList.splice(event.index, 1)
    },
    // 新增图片
    async afterRead(event) {
      // 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
      let lists = [].concat(event.file)
      for (let i = 0; i < lists.length; i++) {
        // 如果超过500k进行压缩
        if (lists[i].size > 1024 * 500) {
          uni.compressImage({
            src: lists[i].url,
            quality: 20,
            success: async (resCompress) => {
              lists[i].url = resCompress.tempFilePath
              await this.uploadFilePromise(lists[i])
            }, fail: res => {

            }
          })
        } else {
          await this.uploadFilePromise(lists[i])
        }
      }
    },
    handCompressPic(url) {
      uni.compressImage({
        src: url,
        quality: 50,
        success: res => {
          return res.tempFilePath
        }, fail: res => {

        }
      })
    }
  },
};
</script>

<!--设置单个页面背景颜色 -->
<style scoped>
</style>

<style lang="scss">
page {
  background: #F5F6F7;
}

.line {
  height: 1px;
  background: #F5F5F5;
  width: calc(100% - 32px);
  margin: 0 auto;
}

.uploadBox {
  width: calc(100% - 32px);
  margin: 20px auto;

  .uploadIcon {
    width: 70px;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;

    .text {
      margin-top: 3px;
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #8C8C8C;
      line-height: 16px;
    }
  }
}


.card {
  width: 100%;
  margin-top: 15px;
  background: #FFFFFF;
  box-shadow: 0px 0px 8px 0px rgba(81, 82, 84, 0.08);
  border-radius: 12px;
  margin-bottom: 32px;
  padding-bottom: 1px;

  .card_top {
    height: 44px;
    display: flex;
    align-items: center;
    margin-left: 16px;

    .left {
      font-size: 16px;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #262626;
      line-height: 20px;
    }

    .right {
      font-size: 13px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #8C8C8C;
      line-height: 20px;
    }
  }
}

.confirm {
  width: calc(100% - 56px);
  margin: 0 auto;
  height: 44px;
  background: #1890FF;
  border-radius: 22px;
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #FFFFFF;
  text-align: center;
  line-height: 44px;
  margin-bottom: 25px;
}

.check {
  width: 100%;
  height: 22px;
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890FF;
  line-height: 22px;
  text-align: center;

}
</style>
