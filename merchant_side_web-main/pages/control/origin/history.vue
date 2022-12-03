<template>
  <view>
    <u-modal :show="show" @cancel="cancel" :showCancelButton="true" @close="cancel" @confirm="confirm" title="删除溯源单"
      content='你确定删除此溯源单吗？' confirmText="确认" cancelText="取消"></u-modal>
    <mescroll-body @init="mescrollInit" @down="downCallback" @up="upCallback" :up="option" :down="{ auto: false }">
      <view class="card" v-for="(i, k) in list" :key="k">
        <view class="card_top">
          <view class="left">
            <span class="t1">溯源单</span>
            <span class="t2">{{ i.create_time }}</span>
          </view>
          <u-icon size="20" name="trash" @click="deleteItem(k)" color="#1890FF"></u-icon>
        </view>
        <view class='line'></view>
        <view class='imgBox'>
          <u-album :urls="i.images" rowCount="4" space="6" maxCount="8" previewFullImage></u-album>
        </view>
      </view>
      <view class="null" v-if="list.length === 0">
        <img class="nullData" src="@/static/暂无数据.png" alt="" />
        <view class="text">暂无数据</view>
      </view>
    </mescroll-body>

  </view>
</template>

<script>
import MescrollMixin from "@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js";
import {
  deleteSource,
  fetchSourceList,
} from "@/config/api.js";
export default {
  mixins: [MescrollMixin], // 使用mixin
  data() {
    return {
      selectIndex: "",
      show: false,
      list: [],
      params: {
        current: 1,
        size: 5
      },
      option: {
        auto: true,
        noMoreSize: 3,
        textNoMore: "没有更多了",
        page: {
          size: 1,//如果列表请求的数据项小于此值 则插件将会判定不会有下一页
        },
        empty: {
          use: false
        }
      },
    };
  },
  onLoad() {
    // this.getList()
  },
  methods: {
    downCallback() {
      this.mescroll.resetUpScroll();
    },
    upCallback(page) {
      // page.num会自动递增
      this.params.current = page.num;
      fetchSourceList(this.params)
        .then((res) => {
          let arr = res.data.records;
          if (page.num === 1) this.list = [];
          setTimeout(() => {
            this.list = this.list.concat(arr);
            this.mescroll.endByPage(arr.length, res.data.pages);
          }, 200);
        })
        .catch((err) => {
          this.mescroll.endErr();
        });
    },
    cancel() {
      this.show = false
    },
    confirm() {
      deleteSource(this.list[this.selectIndex]).then(res => {
        if (res.status == 200) {
          this.list.splice(this.selectIndex, 1)
          this.show = false
          uni.showToast({
            title: '删除成功！',
            duration: 500
          });
        }
      })
    },
    // 获取数据
    getList() {
      uni.showLoading({
        title: '加载中...'
      })
      fetchSourceList(this.params).then(res => {
        if (res.status == 200) {
          this.list = res.data.records
          uni.hideLoading();
        }
      })
    },
    deleteItem(k) {
      this.show = true
      this.selectIndex = k
    }
  }
};
</script>
<style scoped>
</style>

<style lang="scss">
page {
  background: #F5F6F7;
}

/deep/ .u-modal__content__text {
  text-align: center;
}

.card {
  width: 100%;
  margin-top: 15px;
  background: #FFFFFF;
  box-shadow: 0px 0px 8px 0px rgba(81, 82, 84, 0.08);
  border-radius: 12px;
  margin: 16px 0;
  padding-bottom: 1px;

  .card_top {
    height: 44px;
    display: flex;
    align-items: center;
    margin: 0 16px;
    justify-content: space-between;

    .left {

      .t1 {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #262626;
        line-height: 20px;
      }

      .t2 {
        margin-left: 10px;
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #8C8C8C;
        line-height: 20px;
      }
    }
  }

  .line {
    height: 1px;
    background: #F5F5F5;
    width: calc(100% - 32px);
    margin: 0 auto;
  }

  .imgBox {
    width: calc(100% - 32px);
    margin: 20px auto;
  }
}

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
