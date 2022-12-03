<template>
	<view style="padding-bottom: 80px;">
		<view class="topTitle">防疫快速通行证</view>
		<view class="subTitle">疫情防控，人人有责</view>
		<view class='center_box'>
			<image class="bg" :src="imgUrl" mode="scaleToFill" />
			<view class='topInfo'>
				<view class='name'>
					{{ info.name }}
				</view>
				<view class='idCardPhone'>
					<span>{{ info.idCard.replace(/^(.{6})(?:\d+)(.{4})$/, "$1**********$2") }}</span>
					<span class="line">|</span>
					<span>{{ info.phone }}</span>
				</view>
			</view>
			<view class='currentTime'>{{ currentTime }}</view>
			<image class="icon" :src="imgIconUrl" mode="scaleToFill" />
			<view class='text' :style="{ color: activeColor }">{{ showText }}</view>
			<view class='tips'>
				<view>{{ tipText1 }}</view>
				<view>{{ tipText2 }}</view>
				<view class='updateTime'>最新申报时间：{{ info.applyTime }}</view>
			</view>
		</view>
		<view class='bottom'>
			<view class='left' @click="toReport">更新申报</view>
			<view class='right' @click="toHealth">打开健康通</view>
		</view>
	</view>
</template>

<script>
import { getHealthDetail } from "@/config/merchant.js";
export default {
	data() {
		return {
			info: "",
			imgUrl: "",
			imgIconUrl: "",
			currentTime: "",
			showText: "",
			activeColor: "",
			tipText1: "",
			tipText2: ""
		};
	},
	onLoad() {
		getHealthDetail().then((res) => {
			this.info = res.data
			this.changeImage()
		})
		this.timer = setInterval(this.getTime, 1000)
	},
	onUnload: function () {
		uni.navigateBack({
			delta: 2
		});
	},
	methods: {
		toReport() {
			uni.navigateTo({
				url: '/pages/user/report/report'
			});
		},
		toHealth() {
			wx.navigateToMiniProgram({
				appId: 'wx34b9f47827e4801d',
				envVersion: 'release',
			})
		},
		getTime() {
			this.currentTime = this.$u.timeFormat(new Date().getTime(), 'hh:MM:ss yyyy-mm-dd')
		},
		changeImage() {
			switch (this.info.status) {
				case 1:
					this.imgUrl = require("@/static/result/pass.png")
					this.imgIconUrl = require("@/static/result/pass-icon.png")
					this.showText = "可通行"
					this.activeColor = "#1890FF"
					this.tipText1 = "1.健康码：" + this.info.healthColor
					this.tipText2 = "2.行程卡：" + this.info.passableColor + "。途径：" + this.info.reachedCity.toString()
					break;
				case 2:
					this.imgUrl = require("@/static/result/report.png")
					this.imgIconUrl = require("@/static/result/report-icon.png")
					this.showText = "请到社区报备"
					this.activeColor = "#EE7F3C"
					this.tipText1 = "1.请到居住社区设备，"+this.info.riskArea.toString()+"有风险。"
					// this.tipText2 = "最新申报时间：" + this.info.applyTime
					// this.tipText1 = "1.健康码：" + this.info.healthColor
					// this.tipText2 = "2.行程卡：" + this.info.passableColor + this.info.passableColor == "绿色" ? "" :  "。途径：" + this.info.riskArea.toString() + "有风险" 
					break;
				case 3:
					this.imgUrl = require("@/static/result/ban.png")
					this.imgIconUrl = require("@/static/result/ban-icon.png")
					this.showText = "禁止通行"
					this.activeColor = "#EA4139"
					 this.tipText1 = "1.请自行居家隔离，等待防疫中心指示。"
					// this.tipText1 = "1.健康码：" + this.info.healthColor + "。请自行居家隔离，等待防疫中心指示。"
					this.tipText2 = "2.行程卡：" + this.info.passableColor + "。途径：" + this.info.riskArea.toString() + "风险地区"
					break;
				case 4:
					this.imgUrl = require("@/static/result/info.png")
					this.imgIconUrl = require("@/static/result/infor-icon.png")
					this.showText = "信息已失效"
					this.activeColor = "#EE7F3C"
					this.tipText1 = "1.提示：健康码超过48小时"
					this.tipText2 = "2.提示：行程卡超过48小时"
					break;

				default:
					break;
			}
		},
	},

	beforeDestroy() {
		if (this.timer) {
			clearInterval(this.timer); // 在Vue实例销毁前，清除定时器
		}
	}
}
</script>
<style scoped>
</style>
<style lang="scss">
page {
	background: #F5F6F7;
}

.topTitle {
	margin: 12px auto;
	text-align: center;
	height: 30px;
	font-size: 22px;
	font-family: PingFangSC-Medium, PingFang SC;
	font-weight: 500;
	color: #111111;
	line-height: 30px;
}

.subTitle {
	margin: 12px auto;
	text-align: center;
	height: 22px;
	font-size: 16px;
	font-family: PingFangSC-Medium, PingFang SC;
	font-weight: 500;
	color: #595959;
	line-height: 22px;
}

.center_box {
	width: calc(100% - 32px);
	margin: 0 auto;
	background: #FFFFFF;
	border-radius: 12px 12px 0px 0px;
	position: relative;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding-bottom: 44px;

	.bg {
		width: 100%;
		height: 92px;
	}

	.icon {
		height: 117px;
		width: 117px;
		margin-top: 41px;
	}

	.topInfo {
		position: absolute;
		top: 16px;
		left: 24px;

		.name {
			height: 28px;
			font-size: 20px;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 28px;
		}

		.idCardPhone {
			margin-top: 10px;
			height: 22px;
			font-size: 14px;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;
			color: #FFFFFF;
			line-height: 22px;

			.line {
				margin: 0 10px;
			}
		}

	}

	.currentTime {
		width: 100%;
		height: 25px;
		font-size: 16px;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 600;
		color: #111111;
		line-height: 25px;
		text-align: center;
		margin: 16px auto 0 auto;
	}

	.updateTime {
		height: 22px;
		font-size: 16px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 500;
		color: #111111;
		line-height: 22px;
		margin-top: 30px;
	}

	.text {
		margin: 32px 0;
		height: 30px;
		font-size: 22px;
		font-family: PingFangSC-Medium, PingFang SC;
		font-weight: 500;
		line-height: 30px;
	}

	.tips {
		font-size: 16px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 500;
		color: #111111;
		padding: 0 15px;
	}
}

.bottom {
	width: 100%;
	position: fixed;
	bottom: 0;
	height: 80px;
	background: rgba(255, 255, 255, 0.9);
	box-shadow: 0px 2px 15px 0px rgba(0, 0, 0, 0.12);
	backdrop-filter: blur(50px);
	display: flex;
	align-items: center;
	justify-content: space-around;

	.left {
		width: 168px;
		height: 44px;
		border-radius: 22px;
		border: 1px solid #1890FF;
		font-size: 16px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 400;
		color: #1890FF;
		text-align: center;
		line-height: 44px;
	}

	.right {
		width: 168px;
		height: 44px;
		background: #1890FF;
		border-radius: 22px;
		font-size: 16px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 400;
		color: #FFFFFF;
		text-align: center;
		line-height: 44px;
	}
}
</style>
