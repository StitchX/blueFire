<template>
  <view class="contentBox">
    <view class="topModel">
			<view class="modelBox" @click="toOrigin">
				<img src="@/static/controlImages/溯源追踪.png" class="model">
				<span>溯源追踪</span>
			</view>
			<view class="modelBox" @click="toPrevention">
						<img src="@/static/controlImages/疫情防控.png" class="model">
						<span>疫情防控</span>
			</view>
			<view class="modelBox" @click="toLive">
					<img src="@/static/controlImages/直播监控.png" class="model">
					<span>直播监控</span> 
			</view>
		</view>
    <!-- 曝光台 -->
    <view class="box">
      <view class="left">
        <view class="title">
        曝光台
      </view>
      <view class="explain">
        （曝光本辖区内问题企业）
      </view>
      </view>
      <view class="more" @click="toExposure">
        <span>查看更多</span>
        <u-icon name="arrow-right" color="#1890ff"></u-icon>
      </view>
    </view>
    <view class="boxList">
		    <u-empty v-if="list.length === 0" mode="list" icon="http://cdn.uviewui.com/uview/empty/list.png">
		    </u-empty>
		    <view class="card" v-for="(item, index) in list" :key="index">
		      <view class="card-box">
		        <view class="top">
		          <view class="left">
		            <img style="width: 18px;height: 16px;" src="@/static/store.png"/>
		            <view class="name">{{ item.storeName }}</view>
		          </view>
		          <view class="right_time">{{ item.handlingTime }}</view>
		        </view>
		        <view class="main">
		          <view class="title_box">
		            <view class="title">问题范围：</view>
		            <view class="content fanwei">{{ item.problem }}</view>
		          </view>
		          <view class="title_box">
		            <view class="title">处理结果：</view>
		            <u-read-more
		              showHeight="40"
		              closeText="展开"
		              textIndent="0"
		              :toggle="true"
		              :shadowStyle="{ backgroundImage: 'none' }"
		            >
		              <rich-text class="content" :nodes="item.handlingResult"></rich-text>
		            </u-read-more>
		          </view>
		        </view>
		      </view>
		    </view>
  
	 <!-- <view class="null">
        <img class="nullData" src="@/static/暂无数据.png" alt="">
        <view class="text">暂无数据</view>
      </view> -->
    </view>
    <!-- 曝光台 -->
    <view class="box">
      <view class="left">
        <view class="title">
        我的投诉
      </view>
      </view>
      <view class="more" @click="toCompliant">
        <span>查看更多</span>
        <u-icon name="arrow-right" color="#1890ff"></u-icon>
      </view>
    </view>
    <view class="boxList">
      <view class="null">
        <img class="nullData" src="@/static/暂无数据.png" alt="">
        <view class="text">暂无数据</view>
      </view>

    </view>
  </view>
</template>

<script>
	import {platformList} from "@/config/api.js"
export default {
  data() {
    return {
		  list: [
		        // {
		        //   name: "阳光新业海底捞1",
		        //   time: "2022-01-16",
		        //   type: "存在违规操作",
		        //   result:
		        //     "依据《食品安全法》对该饭店进行为期8天的闭店歇业整顿护额哈哈哥哥哥哥发。",
		        // },
		        // {
		        //   name: "阳光新业海底捞2",
		        //   time: "2022-01-16",
		        //   type: "存在违规操作",
		        //   result:
		        //     "依据《食品安全法》对该饭店进行为期8天的闭店歇业整顿护额哈哈哥哥哥哥发。依据品安全法》对该饭店进行为期8天的闭店歇业整顿护额哈哈哥哥哥哥发。",
		        // }
		      ],
			  page:{
				  current: 1,
				  size: 2
			  }
			  
	};
  },
  mounted(){
	this.dataList()
  },
  methods: {
	dataList(){
			this.list = []
		// platformList(this.page).then((res)=>{
		// 	console.log(res)
		// 	this.list = res.data.records
		// })
	},
    toOrigin() {
      uni.navigateTo({
        url: "./origin/origin",
      });
    },
    toLive() {
      uni.navigateTo({
        url: "./live/live",
      });
    },
    toPrevention() {
      uni.navigateTo({
        url: "./prevention/prevention",
      });
    },
	toExposure(){
    return
		uni.navigateTo({
		  url: "./exposure/exposure",
		});
	},
	toCompliant(){
		uni.navigateTo({
		  url: "./complaint/complaint",
		});
	}
  },
};
</script>

<style lang="scss">
.contentBox {
  width: 100%;
  background: #f5f6f7;
  height: 100vh;
  overflow: auto;
  padding-bottom: 10px;
  // 顶部模块
  .topModel {
    display: flex;
    margin-top: 12px;
    width: 100%;
    height: 100px;
    background: #fff;
    border-radius: 12px;
    .modelBox {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      margin-left: 10px;
      width: 60px;
      span {
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #595959;
      }
      .model {
        height: 40px;
        width: 40px;
        margin-bottom: 4px;
      }
    }
  }
  // 模块
  .box {
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .left {
      height: 100%;
      display: flex;
      align-items: center;
      margin-left: 16px;
      .title {
        font-size: 16px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 600;
        color: #262626;
        line-height: 22px;
      }
      .explain {
        font-size: 13px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #8c8c8c;
        line-height: 18px;
      }
    }
    .more {
      height: 100%;
      display: flex;
      align-items: center;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 600;
      color: #1890ff;
      line-height: 20px;
      margin: 0 16px 0 30px;
    }
  }
  // 模块列表
  .boxList {
    width: 100%;
    min-height: 282px;
    background: #ffffff;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    .null {
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
  }
  .card {
    width: 100%;
    background: #ffffff;
    border-radius: 12px;
    // margin-top: 12px;
    overflow: hidden;
	border-bottom: 0.5px solid #E9E9E9;
    .card-box {
      width: calc(100% - 32px);
      height: auto;
      margin: 10px auto;
      .top {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .left {
          display: flex;
          align-items: center;
          img {
            width: 24px;
            height: 24px;
          }
          .name {
            margin-left: 8px;
            font-size: 14px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 600;
            color: #262626;
            line-height: 20px;
          }
        }
        .right_time {
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #595959;
          line-height: 20px;
        }
      }
      .main {
        width: 100%;
        .title_box {
          margin-top: 13px;
          width: 100%;
          display: flex;
          /deep/ .u-read-more {
            width: calc(100% - 70px);
          }
          /deep/ .u-read-more__toggle {
            position: relative;
            height: 25px;
          }
          /deep/ .u-read-more__toggle__text {
            position: absolute;
            right: 0;
          }
          .title {
            width: 70px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #8c8c8c;
            line-height: 20px;
          }
          .content {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #595959;
            line-height: 20px;
          }
        }
      }
	.fanwei{
		    width: 44%;
		    display: block;
		    overflow: hidden;
		    word-break: keep-all;
		    white-space: nowrap;
		    text-overflow: ellipsis;
	}
    }
  }
}
</style>
