<template>
  <view style="background-color: #f5f6f7">
    <u-modal :show="show" showCancelButton @cancel="show = false" @confirm="logoutSub" confirmText="确认退出" title="退出登录"
      content='您确定想退出当前账户吗?'></u-modal>
    <u-popup :show="onshow" mode="bottom" :closeOnClickOverlay="true" @close="onshow = false">
      <view @click="toTrouch" style="margin: 20px auto; width: 60vw; text-align: center;color:#1890FF">
        呼叫 18123399598
      </view>
      <view @click="onshow = false" style="margin: 20px auto; width: 60vw; text-align: center;color: #1890FF;">
        取消
      </view>
    </u-popup>

    <view id="dataForm">
      <view class="user_title">发现</view>
      <view class="user_imge">
        <u--image width="100vw" :src="require('../../static/tabsUser/bg.png')"></u--image>
      </view>
      <view class="user_head">
        <!-- 		  <u-avatar
		    size="60"
			v-if="dataInfo.payCodeUrl"
		    :src="dataInfo.payCodeUrl"
			@click="show = true"
		    shape="circle"
		  ></u-avatar> -->
        <u-avatar @click="show = true" size="60" :src="require('../../static/tabsUser/store.png')" shape="circle">
        </u-avatar>
        <view class="right_info">
          <view class="name" @click="show = true">{{
              dataInfo.storeName
          }}</view>
          <!-- <view style="font-size: 14px; margin-top: 10px">风险等级：A</view> -->
        </view>
        <!-- <u-icon color="white" name="arrow-down"></u-icon> -->
      </view>
      <view class="user_width">
        <view class="user_conter">
          <u-grid :border="false" col="4">
            <u-grid-item v-for="(baseListItem, baseListIndex) in baseList" :key="baseListIndex"
              @click="onclick(baseListItem)">
              <u--image width="48px" height="48px" :src="baseListItem.name"></u--image>
              <text class="grid-text">{{ baseListItem.title }}</text>
            </u-grid-item>
          </u-grid>
        </view>
      </view>
    </view>
    <view class="user_all">
      <u-sticky>
        <view class="user_new">
          <view>新闻动态</view>
          <view>
            <u--text size="14" type="primary" text="查看更多>"></u--text>
          </view>
        </view>
      </u-sticky>
      <view class="supervise" v-for="(item, index) in data" :key="index" @click="onRouter(item)">
        <view style="width: 48px; height: 48px">
          <!-- <u--image :src="require('../../static/tabsUser/leftImg.png')" width="48px" height="48px"></u--image> -->
          <image :src="item.UsImg" style="width: 48px; height: 48px"></image>
        </view>
        <view style="width: 85%">
          <view class="supervise_flex">
            <view class="supervise_title">
              {{ item.title }}
            </view>
            <view style="color: #8c8c8c; font-size: 14px; font-weight: 350">
              {{ item.date }}
            </view>
          </view>
          <view class="supervise_flex">
            <view class="supervise_text">
              {{ item.content }}
            </view>
          </view>
          <view class="border_botm"></view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import list from "./list.json";
import { logout } from "@/config/api.js";
import { apiMerchant, getHealthDetail } from "@/config/merchant.js";
export default {
  data() {
    return {
      baseList: [
        {
          id: 1,
          name: require("../../static/tabsUser/orderList.png"),
          title: "基本信息",
        },
        {
          id: 2,
          name: require("../../static/tabsUser/percen.png"),
          title: "从业人员",
        },
        {
          id: 3,
          name: require("../../static/tabsUser/service.png"),
          title: "在线客服",
        },
        {
          id: 4,
          name: require("../../static/tabsUser/about.png"),
          title: "关于平台",
        },
        {
          id: 5,
          name: require("../../static/tabsUser/report.png"),
          title: "疫情申报",
        },
      ],
      show: false,
      data: {},
      emulator_Height: "",
      dataInfo: {},
      onshow: false
    };
  },
  onShow() {
    this.data = list.capability;
    this.getDataList();
  },
  methods: {
    toTrouch() {
      // 可以先**看是否拿到传进来的电话
      console.log(phone);
      let phone = "18123399598"
      // uni.getSystemInfoSync()是**信息同步对应接口
      const res = uni.getSystemInfoSync();
      // ios系统默认有个模态框
      if (res.platform == 'ios') {
        uni.makePhoneCall({
          phoneNumber: phone,
          success() {
            console.log('拨打成功了');
          },
          fail() {
            console.log('拨打失败了');
          }
        })
      } else {
        // 安卓手机手动设置一个showActionSheet
        uni.showActionSheet({
          itemList: [phone, '呼叫'],
          success: function (res) {
            console.log(res);
            if (res.tapIndex == 1) {
              uni.makePhoneCall({
                phoneNumber: phone,
              })
            }
          }
        })
      }
      // uni.makePhoneCall({
      // 	// 手机号
      // 	phoneNumber: "18123399598",
      // 	// 成功回调
      // 	success: (res) => {
      // 		console.log('调用成功!')
      // 	},
      // 	// 失败回调
      // 	fail: (res) => {
      // 		console.log('调用失败!')
      // 	}

      // });
    },
    logoutSub() {
      logout().then((res) => {
        if (res) {
          uni.removeStorageSync('token');
          uni.reLaunch({
            url: "/pages/login/login",
          });
        }
      });
    },
    close() {
      this.show = false;
    },
    getDataList() {
      apiMerchant().then((res) => {
        this.dataInfo = res.data;
        // 存储
	    uni.setStorageSync('telPhone',res.data.telPhone)
      });
    },
    onRouter(item) {
      console.log(item, "user");
      // 跳转新闻
      uni.navigateTo({
        url:
          "/pages/user/userPage?data=" +
          encodeURIComponent(JSON.stringify(item)),
      });
    },
    onclick(item) {
      console.log(item);
      // 跳转基本信息
      if (item.id == 1) {
        uni.navigateTo({
          url:
            "/pages/user/userInfo?data=" +
            encodeURIComponent(JSON.stringify(item.id)),
        });
      }
      if (item.id == 2) {
        uni.navigateTo({
          url:
            "/pages/user/certificate"
        });
      }
      if (item.id == 3) {
        this.onshow = true
      }
      if (item.id == 4) {
        uni.navigateTo({
          url:
            "/pages/user/platform/platform"
        });
      }
      // 前往疫情申报  跳转前调接口查询是否已经申报过  依据结果判断跳转页面
      if (item.id == 5) {
        getHealthDetail().then((res) => {
          if (res.data) {
            uni.navigateTo({
              url:
                "/pages/user/report/result/result"
            });
          } else {
            uni.navigateTo({
              url:
                "/pages/user/report/report"
            });
          }
        })
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.user_title {
  position: fixed;
  top: 35px;
  width: 100vw;
  text-align: center;
  font-weight: 500;
  font-size: 18px;
  color: #ffffff;
  z-index: 1000;
}

.user_head {
  width: 100%;
  display: flex;
  flex-direction: row;
  position: absolute;
  align-items: flex-start;
  top: 77px;
  left: 17px;

  .right_info {
    color: #ffffff;
    padding-left: 10px;

    .name {
      width: 150px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .u-icon {
    margin-top: 3px;
  }
}

/deep/ .u-grid-item.data-v-5b3a01af {
  margin-top: 15px;
}

.user_width {
  width: 95vw;
  height: 130px;
  position: relative;
  top: -65px;
  margin: 0 auto;
}

.user_conter {
  background: #ffffff;
  padding-bottom: 15px;
  border-radius: 12px;
}

.grid-text {
  font-size: 13px;
  font-weight: 300;
  margin-top: 6px;
}

.user_all {
  width: 95vw;
  height: calc(100vh - 360px);
  margin: 0 auto;
  overflow: auto;
  background: #ffffff;
  border-radius: 12px;
}

.user_new {
  background: #ffffff;
  padding: 15px 15px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: 16px;
}

// list数据
.supervise {
  padding: 15px;
  display: flex;
  flex-direction: row;
}

.supervise_flex {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-bottom: 7px;
  padding-left: 7px;
}

.supervise_title {
  color: #333333;
  font-size: 14px;
  font-weight: 500;
  width: 60%;
  display: block;
  overflow: hidden;
  word-break: keep-all;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.supervise_text {
  color: #595959;
  font-size: 14px;
  width: 90%;
  display: block;
  overflow: hidden;
  word-break: keep-all;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.border_botm {
  border-bottom: 0.5px solid #ececec;
  position: relative;
  top: 10px;
}

/deep/ .u-modal__content__text {
  text-align: center !important;
}

::-webkit-scrollbar {
  display: none;
  width: 0 !important;
  height: 0 !important;
  -webkit-appearance: none;
  background: transparent;
  color: transparent;
}

::-webkit-scrollbar {
  display: none;
  width: 0 !important;
  height: 0 !important;
  -webkit-appearance: none;
  background: transparent;
  color: transparent;
}
</style>
