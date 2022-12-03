<template>
	<view  style="padding: 0 10px;" ref="dataForm" >
		<scroll-view :scroll-top="150" >
		<view class="page_title">{{data.warningName}} </view>
		<view class="page_text">
		  <view>
			<span style="padding-right: 10px;"> {{data.createTime}}  </span>
		  </view>
<!-- 		  <view>
			<span>{{data.issuingAgency}}</span>
		  </view> -->
		</view>
		<u-divider></u-divider>
		<view style="padding-bottom: 60px;" >
			<u-parse :content="data.warningContent"  id="gundong">
			</u-parse>
		</view>
		</scroll-view>
	</view>
</template>

<script>
	import {batchRead } from "@/config/notice.js";
	export default {
		data() {
			return {
				content: `
								`,
				showButton:true,
				data:{
					noticeTitle:"",
					createTime:"",
					noticeType:"",
					issuingAgency:"",
					content:"",
					adjunctFile:[],
					// readFlag:0,
					noticeDetailsId:"",
					
				},
				readStatus:""
			}
		},
		onLoad(list){
			let data = JSON.parse(decodeURIComponent(list.id))
			this.data = data
			console.log(data)
		},

		mounted() {
			this.onreadFlag()
		},
		
		methods: {
			onreadFlag(){
				let data = this.data
				let form =  {
				  id: data.merchantId,  // 通知详情编号
				}
				console.log(data.readFlag,"1234")
				if(data.readFlag == 0){
					batchRead(form).then((res)=>{
						console.log(res)
						// this.data = res.data
					})
				}
				
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
