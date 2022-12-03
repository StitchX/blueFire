<template>
	<view style="padding: 0 10px;">
		<u-list
				@scrolltolower="scrolltolower"
				showScrollbar
			>
		<view class="page_title">{{data.title}} </view>
		<view class="page_text">
		  <view>
			<span> {{data.date}}</span>
		  </view>
		</view>
		<u-divider></u-divider>
		<view style="padding-bottom: 60px;" >
			<view v-for="(item,index) in data.qeury" :key="index"  >
				<u-parse style="font-size: 13px;" :content="item.center">
				</u-parse>
				<view class="image_img">
					<image v-if="item.UsImg" mode="widthFix" :src="item.UsImg"></image>
				</view>
			</view>
		</view>
		</u-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				data:{}
				}
		},
		onLoad(list){
			let data = JSON.parse(decodeURIComponent(list.data))
			this.data = data
			console.log(data)
		},
		methods: {
				
			onclick(){
				console.log("我被点击了")
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
	      text-align: center;
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
	.image_img{
		display: flex;
		justify-content: center;
		margin: 5px 0;
	}

</style>
