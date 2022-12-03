<template>
	<view>
		<u-list
				@scrolltolower="scrolltolower"
				lowerThreshold="80"
				showScrollbar
			>
		  <view class="box">
			  <!-- </u-empty> -->
			 <view class="card" v-for="(item, index) in dataList" :key="index">
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
			   
		  </view>		
		<!-- // 加载状态  uview-->
		<view class="boxList"  v-if="listshow"  >
			<view class="null">
			  <img class="nullData" src="@/static/暂无数据.png" alt="">
			  <view class="text">暂无数据</view>
			</view>
		</view>
		 <u-loadmore :status="finished" v-else />
		 <view class="" style="height: 150rpx;">
		 	
		 </view>
		</u-list>
	</view>

</template>

<script>
import {platformList} from "@/config/api.js"
export default {
  data() {
    return {
		list: [
		      {
		        name: "阳光新业海底捞1",
		        time: "2022-01-16",
		        type: "存在违规操作",
		        result:
		          "依据《食品安全法》对该饭店进行为期8天的闭店歇业整顿护额哈哈哥哥哥哥发。",
		      },
		      {
		        name: "阳光新业海底捞2",
		        time: "2022-01-16",
		        type: "存在违规操作",
		        result:
		          "依据《食品安全法》对该饭店进行为期8天的闭店歇业整顿护额哈哈哥哥哥哥发。依据品安全法》对该饭店进行为期8天的闭店歇业整顿护额哈哈哥哥哥哥发。",
		      }
		    ],
			current: 0,
			input:"",
			finished: 'nomore',    // 加载状态
			dataList:[],
			page:{
				current: 1,
				size: 10,					
			},
			listshow:false,
			total:0,
	};
  },
  mounted(){
	  
	 // this.getAllapi() 
  },
  methods:{
	  // 全部商户
	  scrolltolower(e){
	  	if(this.page.current >= this.total){
	  		 this.finished = 'nomore'; //已经滑到底的提醒
	  		 return false;
	  	 }else{
	  		 this.page.current++;
	  		 this.getAllapi();
	  	 }
	  },
	  // 全部商户
	  getAllapi(){
	  	platformList(this.page).then((res)=>{
	  		let data = res.data.records
	  		// this.unNoticeRead = res.data.unNoticeRead
	  		if(data.length>0){
	  		   //         3                9/3
	  		   this.total = Math.ceil(res.data.total / this.page.size);
	  		   // console.log(this.total)
	  		   // 拼接数组
	  		   this.dataList = this.dataList.concat(data);
	  		   // 加载状态
	  		   this.finished='loading'
	  		   if (this.page.current >= this.total) {
	  			   this.finished = 'nomore';
	  		   }
	  		}else{
	  		   this.finished = 'nomore';
	  		}
	  		if(data.length === 0){
	  		   this.listshow = true
	  		}
	  	}).catch((err)=>{
	  		this.finished = 'nomore';
	  		this.listshow = true
	  	})
	  },
	  
  }
};
</script>

<style lang="scss">
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
	    margin: 12px auto;
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
	    .bottomHandle {
	      width: 100%;
	      display: flex;
	      justify-content: space-around;
	      margin-top: 14px;
	      .handel {
	        display: flex;
	        font-size: 14px;
	        font-family: PingFangSC-Regular, PingFang SC;
	        font-weight: 400;
	        color: #3565ff;
	        line-height: 20px;
	      }
	    }
	  }
	}
	// 暂无数据
.box {
  width: 100vw;
  border: 1px solid #f7f7f7;
  // height: 100vh;
  background: #f7f7f7;
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
}
.null {
	  text-align: center;
	  margin-top: 120px;
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
	.fanwei{
		    width: 44%;
		    display: block;
		    overflow: hidden;
		    word-break: keep-all;
		    white-space: nowrap;
		    text-overflow: ellipsis;
	}
</style>
