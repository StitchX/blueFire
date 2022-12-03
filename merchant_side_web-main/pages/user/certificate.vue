<template>
  <view class="body">
    <u-tabs :list="list1" :current="current" @click="click"> </u-tabs>
    <view v-if="current == 0 && showHealth == false">
      <u-list @scrolltolower="scrolltolowerHealth" height="81vh" showScrollbar>
        <view class="card" v-for="(item, index) in healthList" :key="index">
          <view class="card-title">
            <!-- <view class="user user-name"
              >员工{{ index + 1 }}：{{ item.name }}</view
            > -->
            <view class="name-flex">
              <view class="user user-name">员工{{ index + 1 }}：</view>
              <view class="name11">
                {{ item.name }}
              </view>
            </view>
            <view class="user">有效期至：{{ item.validPeriod }}</view>
          </view>
          <view class="form">
            <u--form labelPosition="left" ref="form1">
              <u-form-item
                :labelWidth="100"
                label="员工职位"
                prop="positionName"
                ref="item1"
              >
                <u--input
                  v-model="item.positionName"
                  readonly
                  border="none"
                ></u--input>
              </u-form-item>
              <u-form-item :labelWidth="100" label="联系电话" prop="phone">
                <u--input
                  v-model="item.phone"
                  readonly
                  border="none"
                ></u--input>
              </u-form-item>
              <u-form-item
                :labelWidth="100"
                label="身 份 证"
                prop="identityCard"
              >
                <u--input
                  v-model="item.identityCard"
                  readonly
                  border="none"
                ></u--input>
              </u-form-item>
              <u-form-item
                :labelWidth="100"
                label="取证日期"
                prop="receiveDate"
              >
                <u--input
                  v-model="item.receiveDate"
                  readonly
                  border="none"
                ></u--input>
              </u-form-item>
              <u-form-item :labelWidth="100" label="健 康 证">
                <u-upload
                  :fileList="item.imageUrl"
                  multiple
                  :deletable="false"
                  :maxCount="1"
                >
                </u-upload>
              </u-form-item>
            </u--form>
            <view class="del-btn">
              <u-button
                text="删除"
                plain
                type="primary"
                shape="circle"
                @click="deleteData(item.id)"
                size="small"
              >
              </u-button>
            </view>
          </view>
        </view>
        <u-loadmore v-if="toBottom" :status="status" />
      </u-list>
      <view class="footer-btn">
        <u-button @click="addHealth" type="primary" shape="circle"
          >新增</u-button
        >
      </view>
    </view>
    <!-- 删除 弹框 -->
    <u-modal
      :show="show"
      title="确认删除"
      showCancelButton
      confirmColor="#1890FF"
      confirmText="确认删除"
      @confirm="onConfirm"
      @cancel="show = false"
    >
      <view class="slot-content">
        <rich-text :nodes="content"></rich-text>
      </view>
    </u-modal>

    <!--健康证   新增 -->
    <view v-if="current == 0 && showHealth == true">
      <add-health :current="current" @child-event="parentEvent"></add-health>
    </view>
    <!--从业证  -->
    <view v-if="current == 1 && showEmploy == false">
      <u-list @scrolltolower="scrolltolower" height="81vh" showScrollbar>
        <view class="card" v-for="(itm, index) in employList" :key="index">
          <view class="card-title">
            <view class="name-flex">
              <view class="user user-name">员工{{ index + 1 }}：</view>
              <view class="name11">
                {{ itm.name }}
              </view>
            </view>
            <view class="user">有效期至：{{ itm.validPeriod }}</view>
          </view>

          <view class="form">
            <u--form labelPosition="left" ref="form1">
              <u-form-item
                labelWidth="100px"
                label="证照类型"
                prop="userInfo.name"
                ref="item1"
              >
                <u--input v-model="itm.name" readonly border="none"></u--input>
              </u-form-item>
              <u-form-item
                labelWidth="100px"
                label="联系电话"
                prop="userInfo.name"
                ref="item1"
              >
                <u--input v-model="itm.phone" readonly border="none"></u--input>
              </u-form-item>
              <u-form-item
                labelWidth="100px"
                label="身 份 证"
                prop="userInfo.name"
                ref="item1"
              >
                <u--input
                  v-model="itm.identityCard"
                  readonly
                  border="none"
                ></u--input>
              </u-form-item>
              <u-form-item
                labelWidth="100px"
                label="取证日期"
                prop="userInfo.name"
                ref="item1"
              >
                <u--input
                  v-model="itm.receiveDate"
                  readonly
                  border="none"
                ></u--input>
              </u-form-item>
              <u-form-item
                labelWidth="100px"
                label="从 业 证"
                prop="userInfo.name"
                ref="item1"
              >
                <u-upload
                  :fileList="itm.imageUrl"
                  multiple
                  :deletable="false"
                  :maxCount="1"
                ></u-upload>
              </u-form-item>
            </u--form>
            <view class="del-btn">
              <u-button
                text="删除"
                plain
                type="primary"
                shape="circle"
                @click="deleteData(itm.id)"
                size="small"
              >
              </u-button>
            </view>
          </view>
        </view>
        <u-loadmore v-if="toBottom" :status="status" />
      </u-list>
      <view class="footer-btn">
        <u-button @click="addEmploy" type="primary" shape="circle"
          >新增</u-button
        >
      </view>
    </view>
    <!-- 从业证   新增 -->
    <view v-if="current == 1 && showEmploy == true">
      <add-health :current="current" @child-events="parentEvents"></add-health>
    </view>
  </view>
</template>

<script>
import addHealth from "./addHealth.vue";
import {
  getHealth,
  getEmployment,
  delEmployment,
  delHealth,
} from "@/config/health.js";
export default {
  components: {
    "add-health": addHealth,
  },
  data() {
    return {
      data: {
        current: 1,
        size: 10,
      },
      healthList: [], //健康证列表
      employList: [], //从业证列表
      list1: [
        {
          name: "健康证",
        },
        {
          name: "行业培训从业证",
        },
      ],
      current: 0,
      fileList: [],
      show: false, //打开删除框
      content: "确认删除该员工的健康证吗？", //删除文本
      showHealth: false, //健康证新增
      showEmploy: false, //从业证新增
      healthFiles: [], //上传健康证
      delId: "", //删除的id
      status: "loadmore",
      toBottom: false,
      total: "",
    };
  },
  onLoad: function (data) {
    console.log(data.current, "结果页面返回");
    if(data.current!==undefined){
      this.current = data.current;
    }
    this.getList();
    this.getEmployList();
    console.log("从业人员");
  },
  onUnload: function () {
    if (this.current === "0" || this.current === "1") {
      uni.reLaunch({
        url: "/pages/user/user",
      });
    }
  },
  onShow() {},

  methods: {
    parentEvent(data) {
      console.log(data, "data222");
      this.showHealth = data;
      this.current = 0;
      this.getList();
      console.log(this.healthList, "list");
    },
    parentEvents(data) {
      this.current = 1;
      this.showEmploy = data;
      this.getEmployList();
      console.log(this.employList, "list");
      console.log(data, "data2");
    },
    scrolltolowerHealth() {
      this.toBottom = true;
      this.status = "loading";
      this.data.size += 10;
      this.getList();
    },
    scrolltolower() {
      this.toBottom = true;
      this.data.size += 10;
      this.status = "loading";
      this.getEmployList();
    },
    getList() {
      let data = this.data;
      getHealth(data).then((res) => {
        this.healthList = res.data.records;
        this.healthList.map((res) => {
          return (res.imageUrl = [
            {
              url: res.imageUrl,
            },
          ]);
        });
        let total = res.data.total;
        if (this.healthList.length >= total) {
          this.status = "nomore";
        } else {
          this.toBottom = false;
        }
        console.log(this.healthList, "this.healthList");
      });
    },
    //从业证列表
    getEmployList() {
      let data = this.data;
      getEmployment(data).then((res) => {
        this.employList = res.data.records;
        this.employList.map((res) => {
          return (res.imageUrl = [
            {
              url: res.imageUrl,
            },
          ]);
        });
        let total = res.data.total;
        if (this.employList.length >= total) {
          this.status = "nomore";
        } else {
          this.toBottom = false;
        }
        console.log(this.employList, "this.employList");
      });
    },
    onRouter() {
      uni.navigateTo({
        url: "/pages/notice/page",
      });
      console.log("跳转");
    },

    // 选项卡
    click(item) {
      this.current = item.index;
      this.showEmploy = false;
      this.showHealth = false;
      console.log(item.index, "item");
    },
    // 健康证   删除
    deleteData(id) {
      this.show = true;
      this.delId = id;
      console.log(this.current);
      if (this.current == 0) {
        this.content = "确认删除该员工的健康证？";
      }
      if (this.current == 1) {
        this.content = "确认删除该员工的从业证？";
      }
    },
    // 确认删除
    onConfirm() {
      console.log(this.delId, "this.delId");
      let id = this.delId;
      if (this.current == null) {
        uni.showToast({
          title: "删除失败",
          icon: "error",
          duration: 1000,
        });
        this.show = false;
      }
      if (this.current == 0) {
        delHealth(id).then((res) => {
          if (res.data > 0) {
            uni.showToast({
              title: "删除成功",
              duration: 1000,
            });
            this.show = false;
            this.getList();
          } else {
            uni.showToast({
              title: "删除失败",
              icon: "error",
              duration: 1000,
            });
            this.show = false;
          }
        });
      }
      if (this.current == 1) {
        delEmployment(id).then((res) => {
          if (res.data > 0) {
            uni.showToast({
              title: "删除成功",
              duration: 1000,
            });
            this.show = false;
            this.getEmployList();
          } else {
            uni.showToast({
              title: "删除失败",
              icon: "error",
              duration: 1000,
            });
            this.show = false;
          }
        });
      }
    },
    addHealth() {
      this.showHealth = true;
      console.log("新增 健康证");
    },
    addEmploy() {
      this.showEmploy = true;
      console.log("新增 从业证");
    },
  },
};
</script>

<style lang="scss">
.body {
  background-color: #f5f6f7;
}
.user {
  color: #000000;
  z-index: 9;
  opacity: 1 !important;
}

.user-name {
  color: #1890ff;
  font-weight: bold;
}
.name11 {
  display: inline-block;
  width: 100px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.name-flex {
  display: flex;
  vertical-align: middle;
  justify-content: space-between;
  color: #1890ff;
  font-weight: bold;
}
/* 改变tabs 选项卡宽度 */
/deep/ .u-tabs__wrapper__nav__item {
  justify-content: space-around;
  width: 44%;
  padding: 0;
  background-color: #fff;
}

/deep/ .u-tabs {
  border-bottom: 12px solid #f5f6f7;
}

.card {
  border-radius: 12px;
  overflow: hidden;
  background-color: #fff;
  padding-bottom: 10px;
  margin-bottom: 12px;
}

.card-title {
  height: 33px;
  line-height: 33px;
  padding: 0 20px;
  background: rgba(24, 144, 255, 0.1);
  display: flex;
  vertical-align: middle;
  justify-content: space-between;
}

.form {
  padding: 0 20px;
}

.del-btn {
  width: 160rpx;
  float: right;
}

.footer-btn {
  padding: 10px 20px;
  background-color: #ffffff;
  position: fixed;
  bottom: 0px;
  right: 0px;
  left: 0px;
}

/deep/ .u-form-item__body.data-v-067e4733 {
  display: flex;
  flex-direction: row;
  padding: 3px 0 !important;
}
</style>
