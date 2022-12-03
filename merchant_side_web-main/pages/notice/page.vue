<template>
	<view  style="padding: 0 10px;" ref="dataForm" >
		<scroll-view :scroll-top="150" >
		<view class="page_title">{{data.noticeTitle}} </view>
		<view class="page_text">
		  <view>
			<span style="padding-right: 10px;"> {{data.createTime}}  </span>
				<span v-if="data.noticeType == 1">政策法规</span>
				<span v-if="data.noticeType == 2">培训学习</span>
				<span v-if="data.noticeType == 3">考试通知</span>
				<span v-if="data.noticeType == 4">开会通知</span>
				<span v-if="data.noticeType == 5">处罚通知</span>
				<span v-if="data.noticeType == 6">监管信息</span>
		  </view>
		  <view>
			<span>{{data.issuingAgency}}</span>
		  </view>
		</view>
		<u-divider></u-divider>
		<view style="padding-bottom: 60px;" >
			<u-parse :content="data.content"  id="gundong">
			</u-parse>
			<view v-if="data.adjunctFile.length > 0">
			   <view>附件：</view>
			   <view v-for="(code,index) in data.adjunctFile" :index="index">
				   <view style="color: #1890FF" @click="onfile(code.ossFilePath)">{{code.fileName}}</view>
				   <!-- <navigator :href="code.ossFilePath" class="btn">{{code.fileName}}</navigator> -->
			   </view>
			</view>
		</view>
		<view class="page_butn" v-if="readStatus == 0 && data.readFlag == 1">
			<u-button type="primary" disabled  v-if="showButton" color="#8C8C8C" size="small" text="请上滑看完本条通知"></u-button>
			<u-button type="primary" @click="onclick" v-else size="small" text="我已阅读完本条通知"></u-button>
		</view>
		</scroll-view>
	</view>
</template>

<script>
	import { apinInfo,apread } from "@/config/notice.js";
	export default {
		data() {
			return {
				content: `
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									<p>露从今夜白，月是故乡明</p>
									<img src="https://cdn.uviewui.com/uview/swiper/2.jpg" />
									<p>12344556789920202022</p>
								`,
				showButton:true,
				data:{
					noticeTitle:"",
					createTime:"",
					noticeType:"",
					issuingAgency:"",
					content:"",
					adjunctFile:[],
					readFlag:0,
					noticeDetailsId:"",
					
				},
				readStatus:""
			}
		},
		onLoad(list){
			let data = JSON.parse(decodeURIComponent(list.id))
			this.noticeDetailsId = data.noticeDetailsId
			this.readStatus = data.readStatus
			console.log(data.readStatus)
			let form =  {
			  noticeDetailsId: data.noticeDetailsId,  // 通知详情编号
			  noticeId: data.noticeId,  // 通知编号
			  readFlag: data.readFlag  // // 是否必读, 0:非必读 1：必读
			}
			apinInfo(form).then((res)=>{
				this.data = res.data
			})
			
			
		},

		mounted() {
				this.showButton = false
		},
		
		methods: {
			onfile(item){
				let url = encodeURI(item);  //中文文件名的网络地址需要encodeURI
				uni.downloadFile({
						url: url,//下载地址接口返回
						success:function(data) {
							if (data.statusCode === 200) {
								//文件保存到本地
								uni.saveFile({
									tempFilePath: data.tempFilePath, //临时路径
									success: function(res) {
										// uni.showToast({
										// 	icon: 'none',
										// 	mask: true,
										// 	title: '文件已保存：' + res.savedFilePath, //保存路径
										// 	duration: 3000,
										// });
										console.log(res.savedFilePath)
										setTimeout(() => {
											//打开文档查看
											uni.openDocument({
												filePath: res.savedFilePath,
												success: function(res) {
													// console.log('打开文档成功');
												}
											});
										}, 300)
									}
								});
							}
						},
						fail: (err) => {
							console.log(err);
							uni.showToast({
								icon: 'none',
								mask: true,
								title: '失败请重新下载',
							});
						},
					});
			},
			onPageScroll(res){
				console.log(res.scrollTop);//距离页面顶部距离
			},
			onReachBottom() {
				this.showButton = false
			},
			
			onclick(){
				console.log("我被点击了")
				let data ={
					noticeDetailsId:this.noticeDetailsId
				}
				apread(data).then((res)=>{
					console.log(res)
					// if(res.status == "200"){
						uni.switchTab({
							url: '/pages/notice/notice'
						});						
					// }
				})
			}	
		}
	}
</script>

<style>
	.page_title{
	  padding: 10px 0;
	  color: #292929;
	  font-weight: 550;
	  letter-spacing: .05em;
	  font-size: 16px;
	  min-height: 50px;
	}
	.page_text{
	      display: flex;
	      flex-direction: row;
	      justify-content: space-between;
	      /* padding: 0px 10px; */
	      color: #8C8C8C;
	      font-size: 14px;
	  }
	  .page_butn{
		  background: #FFFFFF;
		  position: fixed;
		  bottom: 0;
		  left: 0;
		  right: 0;
		  padding: 10px 30px;
	  }
	
    /deep/ .u-button--square{
	      border-radius: 20px !important;
	  }
    /deep/ .u-button--primary {
	      border-width: 0px !important;
	  }
    /deep/ .u-button--small{
	      padding: 20px 8px !important;
	  }
	/deep/ .u-button__text{
		font-size: 14px !important;
		position: relative;
		bottom: 2px;
	}
</style>
