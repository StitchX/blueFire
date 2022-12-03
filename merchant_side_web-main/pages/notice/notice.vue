<template>
	<view>
		<u-sticky>
		<view style="display: flex;padding-top: 10px;">
			<view class="notice_img" style="position: relative" @click="onclick({index:0})">
				<u--image :src='require("../../static/tabsNotice/icon.png")' width="48px" height="48px"></u--image>
				<u-badge absolute offset=[-1,-1] :isDot="false" shape="circle" bgColor="#F45E33" type="warning" max="99" :value="unNoticeRead"></u-badge>
			</view>
			<view class="notice_img" style="position: relative" @click="onclick({index:1})">
				<u--image :src='require("../../static/tabsNotice/icon1.png")'  width="48px" height="48px"></u--image>
				<u-badge absolute offset=[-1,-1] :isDot="false" shape="circle" bgColor="#F45E33" type="warning" max="99" :value="infoRead"></u-badge>
			</view>
			<view class="notice_img" @click="onclick({index:2})">
				<u--image :src='require("../../static/tabsNotice/icon2.png")'  width="48px" height="48px"></u--image>
			</view>
		</view>
		<u-tabs :list="list1" lineWidth="80" linColor="#1890FF" :current ="current" @click="onclick">
		</u-tabs>
		</u-sticky>
		<view v-if="current == 0" >
			<u-list
					@scrolltolower="scrolltolower"
					lowerThreshold="80"
					height="81vh"
					showScrollbar
				>
				<view class="supervise" v-for="(item,index) in dataInfo" :key="index" @click="onRouter(item)">
					<view style="width: 48px;height: 48px;">
						<u--image :src="require('../../static/tabsNotice/user.png')" width="48px" height="48px"></u--image>
					</view>
					<view style="width: 86%;">
						<view class="supervise_flex">
							<view style="color: #333333;font-size: 16px;font-weight: 550;">
							<view v-if="item.noticeType == 1">政策法规</view>
							<view v-if="item.noticeType == 2">培训学习</view>
							<view v-if="item.noticeType == 3">考试通知</view>
							<view v-if="item.noticeType == 4">开会通知</view>
							<view v-if="item.noticeType == 5">处罚通知</view>
							<view v-if="item.noticeType == 6">监管信息</view>
							</view>
							<view style="color:#8C8C8C;font-size: 12px;">
							   {{item.msgSendTime}}
							</view>
						</view>
						<view class="supervise_flex" >
							<view class="supervise_text">
							{{item.noticeTitle}}
							</view>
							<view class="supervise_icon">
								<u-badge v-if="item.readStatus == 0" :isDot="true" bgColor="#F45E33" type="warning"></u-badge>
							</view>
						</view>
						<view class="border_botm"></view>
					</view>
				</view>
				<!-- // 加载状态  uview-->
				<u-loadmore :status="finished" />
				<view class="" style="height: 150rpx;">
					
				</view>
			</u-list>
		</view>
		<view v-if="current == 1" class="">
			<!-- 通知消息 -->
			<u-list
					@scrolltolower="scrolltolower1"
					lowerThreshold="80"
					height="81vh"
					showScrollbar
				>
				<view class="supervise" v-for="(item,index) in dataInfo1" :key="index" @click="onInfoRouter(item)">
					<view style="width: 48px;height: 48px;">
						<u--image :src="require('../../static/tabsNotice/user.png')" width="48px" height="48px"></u--image>
					</view>
					<view style="width: 86%;">
						<view class="supervise_flex">
							<view style="color: #333333;font-size: 16px;font-weight: 550;">
							<view>{{item.warningName}}</view>
							</view>
							<view style="color:#8C8C8C;font-size: 12px;">
							   {{item.createTime}}
							</view>
						</view>
						<view class="supervise_flex" >
							<view class="supervise_text">
								<!-- <u-parse :content="item.warningContent"  id="gundong">
								</u-parse> -->
							{{item.warningContent}}
							</view>
							<view class="supervise_icon">
								<u-badge v-if="item.readFlag == 0" :isDot="true" bgColor="#F45E33" type="warning"></u-badge>
							</view>
						</view>
						<view class="border_botm"></view>
					</view>
				</view>
				<!-- // 加载状态  uview-->
			<!-- 	<view class="boxList"  v-if="dataInfo1.length === 0"  >
					<view class="null">
					  <img class="nullData" src="@/static/暂无数据.png" alt="">
					  <view class="text">暂无数据</view>
					</view>
				</view> -->
				<u-loadmore :status="finished1"/>
				<view class="" style="height: 150rpx;">
					
				</view>
			</u-list>
		</view>
		<view v-if="current == 2" class="">
			<!-- 放心 -->
			<view class="boxList">
			<view class="null">
			  <img class="nullData" src="@/static/暂无数据.png" alt="">
			  <view class="text">暂无数据</view>
			</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { apinNotice,batchRead,apiList } from "@/config/notice.js";
	export default {
		data() {
			return {
				list1: [{
					name: "监管消息",
				}, {
					name: "消息通知",
				}, {
					name:"放心码通知"
				}],
				current: 0,
				page:{
					current: 1,
					size: 10,					
				},
				total:1,
				dataInfo:[],
				 finished: 'nomore',    // 加载状态
				 unNoticeRead:0,
				 
				 // 消息通知
				 dataInfo1:[
					 {
						 noticeType:"2",
						 msgSendTime:"2021-21-23",
						 noticeTitle:"标题",
						 readStatus:0
					 }
				 ],
				 page1:{
				 	current: 1,
				 	size: 10,					
				 },
				 total1:1,
				 finished1: 'nomore',    // 加载状态
				 infoRead:0
			};
		},
		// created(){
		// 	this.dataList()
		// },
		onShow(){
			console.log("显示")
			// 每次进入先清空
			this.dataInfo = []
			this.page.current = 1
			// 监管通知
			this.dataList()
			
			
			// 预警消息通知
			this.dataInfo1 = []
			// this.page1.current = 1
			// // 监管通知
			// this.dataList1()
			
		},
		methods: {
			scrolltolower(e){
				if(this.page.current >= this.total){
					 this.finished = 'nomore'; //已经滑到底的提醒
					 return false;
				 }else{
					 this.page.current++;
					 this.dataList();
				 }
			},
				dataList(){
					apinNotice(this.page).then((res)=>{
						console.log(res)
					   let data = res.data.noticeVoIPage.records
					   this.unNoticeRead = res.data.unNoticeRead
					   if(data.length>0){
						   //         3                9/3
						   this.total = Math.ceil(res.data.noticeVoIPage.total / this.page.size);
						   // console.log(this.total)
						   // 拼接数组
						   this.dataInfo = this.dataInfo.concat(data);
						   // 加载状态
						   this.finished='loading'
						   if (this.page.current >= this.total) {
							   this.finished = 'nomore';
						   }
					   }else{
						   this.finished = 'nomore';
					   }
					})
				},
				
				// 预警消息通知
				scrolltolower1(e){
					if(this.page1.current >= this.total1){
						 this.finished1 = 'nomore'; //已经滑到底的提醒
						 return false;
					 }else{
						 this.page1.current++;
						 this.dataList1();
					 }
				},
					dataList1(){
						apiList(this.page1).then((res)=>{
							console.log(res)
						   let data = res.data.records
						   this.infoRead = res.data.unreadCount
						   if(data.length>0){
							   //         3                9/3
							   this.total1 = Math.ceil(res.data.total / this.page1.size);
							   // console.log(this.total)
							   // 拼接数组
							   this.dataInfo1 = this.dataInfo1.concat(data);
							   // 加载状态
							   this.finished1='loading'
							   if (this.page1.current >= this.total1) {
								   this.finished1 = 'nomore';
							   }
						   }else{
							   this.finished1 = 'nomore';
						   }
						})
					},
					
				onclick(item) {
					console.log('item', item);
					this.current = item.index;
				},
				// 监管消息跳转
				onRouter(id){
					uni.navigateTo({
						url: '/pages/notice/page?id=' + JSON.stringify(id)
					});
					console.log("跳转")
				},
				//消息通知跳转
				onInfoRouter(id){
					uni.navigateTo({
						url: '/pages/notice/infoPage?id=' + JSON.stringify(id)
					});
				}
		}
	}
</script>

<style lang="scss">
/deep/ .u-tabs{
	border-bottom:8px solid #F5F6F7;
}
/deep/ .u-tabs__wrapper__nav__item{
		width: 27vw;
	}
	.supervise{
		padding: 15px;
		display: flex;
		flex-direction:row;
	}
	.supervise_flex{
		display: flex;
		flex-direction:row;
		justify-content:space-between;
		padding-bottom: 7px;
	}
	.supervise_icon{
		position: relative;
		top: 6px;
	}
	.supervise_text{
		color:#666666;
		font-size: 14px;
		width: 44%;
		display:block;
		overflow:hidden;
		word-break:keep-all;
		white-space:nowrap;
		text-overflow:ellipsis;
	}
	.notice_img{
		width:27vw;
		display: flex;
		justify-content:center;
		padding: 0 11px;
	}
	.border_botm{
	    border-bottom: 0.5px solid #ECECEC;
		position: relative;
		top: 10px;
	}
	/deep/.u-badge.data-v-13728ffe {
		line-height: 11px;
		left: 76px;
		text-align: center;
		max-width: 150px;
		font-size: 11px;
		color: #FFFFFF;
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
		
		.boxList {
		  width: 100%;
		  height: 282px;
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
</style>
