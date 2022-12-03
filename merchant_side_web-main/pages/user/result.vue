<template>
	<view>
<!-- 成功 -->
		<view v-if="success==1" class="result">
			<view class="img">
				<u--image :showLoading="true" :src="require('../../static/result/success.png')" width="70px"
					height="70px" @click="click"></u--image>
			</view>
			<view class="middle-font">
				提交成功
			</view>
			<view>
				<u-button class="btn-font" @click="goBack" type="primary" shape="circle">返回列表</u-button>
			</view>
		</view>
<!-- 失败 -->
		<view v-if="success==2" class="result">
				<view class="img">
					<u--image :showLoading="true" :src="require('../../static/result/fail.png')" width="70px"
						height="70px" @click="click"></u--image>
				</view>
				<view class="middle-font">
					提交失败
				</view>
				<view>
					<u-button class="btn-font" @click="goCommit" type="primary" shape="circle" >返回重新提交</u-button>
				</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				success: '1',
				current:'',
			}
		},
		onLoad(data){
			console.log(data,"resultdata")
			this.success=data.data
			this.current=data.current
			console.log(this.success,"this.success")
		},
		methods:{
			goBack(){
				let current=this.current
				uni.navigateTo({
					url: `/pages/user/certificate?current=${current}`
				});
			},
			goCommit(){
				uni.navigateBack();
			},
		}
	}
</script>

<style lang="scss">
	.result {
		height: 500px;
		vertical-align: middle;
		justify-content: center;
		text-align: center;
		padding: 0 25px;
	}

	.img {
		display: block;
		width: 70px;
		height: 70px;
		margin: 80px auto 0;
	}

	.middle-font {
		font-size: 20px;
		font-weight: bolder;
		margin: 16px auto 34px;
	}
</style>
