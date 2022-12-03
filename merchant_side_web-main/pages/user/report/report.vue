<template>
	<view>
		<u-toast ref="uToast"></u-toast>
		<view class='topTip'>
			防疫快速通道为商户提供快速进入场地的防疫
		</view>
		<view class='merchantInfoSty'>
			<view class='rowSty'>
				<view class='right'>申报手机号</view>
				<view class='left'>{{ phone }}</view>
			</view>
			<view class='rowSty'>
				<view class='right'><span style="color:red">*</span>姓名</view>
				<view class='left'>
					<u--input type="text" inputAlign="right"  @blur="checkInput('name')" holdKeyboard placeholderClass="null"
						v-model="merchantInfo.name" placeholder="请输入" border="none"></u--input>
				</view>
			</view>
			<view class='rowSty'>
				<view class='right'><span style="color:red">*</span>身份证号</view>
				<view class='left'>
					<u--input type="idcard" inputAlign="right" maxlength="18" @blur="checkInput('idCard')" holdKeyboard placeholderClass="null"
						v-model="merchantInfo.idCard" placeholder="请输入" border="none"></u--input>
				</view>
			</view>
		</view>
		<view class='centerTip'>
			图片上传小贴士：截图请使用标准字体，否则无法识别。
		</view>
		<view class='uploadCard'>
			<view class='top'>
				<view class='left'>上传通信行程卡截图 <span>（限今日）</span></view>
				<view class='right' @click="toPass">打开“通信行程卡”></view>
			</view>
			<view class='bottom'>
				<view class='left'>
					<img src="@/static/tabsUser/2.png" alt="">
					<view class='imgTip'>示意图</view>
				</view>
				<view class='right'>
					<u-upload :fileList="merchantInfo.fileList1" :sizeType="['compressed']" :previewFullImage="true"
						@delete="deletePic" @afterRead="afterRead" name="1">
						<view class="uploadIcon" v-if="merchantInfo.fileList1.length === 0">
							<u-icon name="camera" v-if="!loading1" size="34"></u-icon>
							<u-loading-icon :show="loading1"></u-loading-icon>
						</view>
					</u-upload>
					<view class='imgTip' v-if="warningMsg1 === ''">上传截图</view>
					<view class='imgTip red' v-else>请重新上传</view>
				</view>
			</view>
			<view class='warningMsg' v-if="warningMsg1 !== ''"> {{ warningMsg1 }} </view>
		</view>
		<view class='uploadCard'>
			<view class='top'>
				<view class='left'>上传天府健康通截图 <span>（限今日）</span></view>
				<view class='right' @click="toHealth">打开“天府健康通”></view>
			</view>
			<view class='bottom'>
				<view class='left'>
					<img src="@/static/tabsUser/1.png" alt="">
					<view class='imgTip'>示意图</view>
				</view>
				<view class='right'>
					<u-upload :fileList="merchantInfo.fileList2" :sizeType="['compressed']" :previewFullImage="true"
						@delete="deletePic" @afterRead="afterRead" name="2">
						<view class="uploadIcon" v-if="merchantInfo.fileList2.length === 0">
							<u-icon name="camera" v-if="!loading2" size="34"></u-icon>
							<u-loading-icon :show="loading2"></u-loading-icon>
						</view>
					</u-upload>
					<view class='imgTip' v-if="warningMsg2 === ''">上传截图</view>
					<view class='imgTip red' v-else>请重新上传</view>
				</view>
			</view>
			<view class='warningMsg' v-if="warningMsg2 !== ''"> {{ warningMsg2 }} </view>
		</view>
		<view class='bottomTip'>
			请确保上传信息真实有效，如故意隐瞒或作假需承担法律责任
		</view>
		<view class="submitSty">
			<button :class="activeSub" :disabled="formCheckDis || warningFlag1 || warningFlag2" @click="getResult">
				查看通行结果
			</button>
		</view>
	</view>
</template>

<script>
import { getPassResult } from "@/config/merchant.js";
export default {
	data() {
		return {
			activeSub: "notSubmit",
			loading1: false,
			loading2: false,
			phone: "",
			merchantInfo: {
				number: "15828571726",
				name: "",
				idCard: "",
				fileList1: [],
				fileList2: [],
			},
			currentUploadName: "",
			pass: {},
			health: "",
			warningMsg1: "",//通行证错误信息
			warningMsg2: "",//健康通错误信息
			formCheckDis: true,//表单验证
			warningFlag1: true,//通行证错误标记
			warningFlag2: true,//健康通错误标记
		};
	},
	computed: {
		showOrNot() {
			const { formCheckDis, warningFlag1, warningFlag2 } = this
			return { formCheckDis, warningFlag1, warningFlag2 }
		}
	},
	watch: {
		merchantInfo: {
			handler(val) {
				if (val.name !== "" && val.idCard !== "" && val.fileList1.length > 0 && val.fileList2.length > 0) {
					this.formCheckDis = false;
				} else {
					this.formCheckDis = true;
				}
			},
			deep: true,
		},
		showOrNot(val) {
			if (val.formCheckDis || val.warningFlag1 || val.warningFlag1) {
				this.activeSub = "notSubmit"
			} else {
				this.activeSub = "submit"
			}
		}
	},
	onShow() {
		// 赋值手机
		// this.pass = {
		// 	telephone:uni.getStorageSync('telPhone')
		// }
		this.phone = uni.getStorageSync('telPhone') 
	},
	onUnload: function () {
		uni.navigateBack({
			delta: 2
		});
	},
	methods: {
		// 跳转通行证小程序
		toPass() {
			wx.navigateToMiniProgram({
				appId: 'wx8f446acf8c4a85f5',
				envVersion: 'release',
			})
		},
		// 跳转健康码小程序
		toHealth() {
			wx.navigateToMiniProgram({
				appId: 'wx34b9f47827e4801d',
				envVersion: 'release',
			})
		},
		// 查看结果
		getResult() {
			console.log(this.pass.telephone,"pass")
			console.log(this.merchantInfo)
			console.log(this.health)
			let riskArea = []
			let reachedCity = []
			if(this.pass.reachedCity.length > 0){	
				console.log("1")
				this.pass.reachedCity.map(i => {
					reachedCity.push(i.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g, '"$1":"$2"'))
				})
			}
			if(this.pass.riskArea.length > 0){	
				console.log("2")
				this.pass.riskArea.map(i => {
					riskArea.push(i.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g, '"$1":"$2"'))
				})
			}
			const params = {
				riskArea: riskArea,
				reachedCity: reachedCity,
				telephone: this.pass.telephone,
				time: this.pass.time,
				passableColor: this.pass.passableColor,
				healthColor: this.health.healthColor,
				name: this.merchantInfo.name,
				idCard: this.merchantInfo.idCard,
				passableImage: this.pass.imagesUrl,
				healthImage: this.health.imagesUrl
			}
			// 填写的身份证
			let idCard = this.merchantInfo.idCard.substring(this.merchantInfo.idCard.length-2)
			// 填写的名字
			let name = this.merchantInfo.name.substring(this.merchantInfo.name.length-1)
			// 识别名称
			let name1 = this.health.name.substring(this.health.name.length-1)
			console.log(idCard,name,name1)
			if(name != name1){
				this.toastShow({
					message: "姓名与健康码姓名不一致",
					duration: 2000,
					type: "error",
				});
			}else{
				getPassResult(params).then(res => {
					if (res.data) {
						uni.navigateTo({
							url: '/pages/user/report/result/result'
						});
					}
				})
			}

		},
		// 弹框提示
		toastShow(parmas) {
			this.$refs.uToast.show({
				type: parmas.type,
				duration: "2000",
				message: parmas.message,
				iconUrl: `https://cdn.uviewui.com/uview/demo/toast/${parmas.type}.png`,
			});
		},
		// 输入验证
		checkInput(type) {
			switch (type) {
				case 'name':
					let reg = /^[\u4E00-\u9FA5]+$/
					if (this.merchantInfo.name === "") {
						this.toastShow({
							message: "请输入姓名",
							duration: 2000,
							type: "error",
						});
					} else if (!reg.test(this.merchantInfo.name)) {
						this.toastShow({
							message: "仅支持中文汉字",
							duration: 2000,
							type: "error",
						});
						this.formCheckDis = true
					}
					return false;
				case 'idCard':
					if (this.merchantInfo.idCard === "") {
						this.toastShow({
							message: "请输入身份证号码",
							type: "error",
						});
					} else if (!this.$validate.isIdCard(this.merchantInfo.idCard)) {
						this.toastShow({
							message: "身份证格式有误,请检查",
							duration: 2000,
							type: "error",
						});
						this.formCheckDis = true
					}
					return false;

				default:
					return true;
			}
		},
		// 新增图片
		async afterRead(event) {
			event.name === "1" ? this.loading1 = true : this.loading2 = true
			this.currentUploadName = event.name
			// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
			let lists = [].concat(event.file)
			for (let i = 0; i < lists.length; i++) {
				this.uploadFilePromise(lists[i])
				// 如果超过500k进行压缩
				// if (lists[i].size > 1024 * 500) {
				// 	uni.compressImage({
				// 		src: lists[i].url,
				// 		quality: 20,
				// 		success: async (resCompress) => {
				// 			lists[i].url = resCompress.tempFilePath
				// 			await this.uploadFilePromise(lists[i])
				// 		}, fail: res => {

				// 		}
				// 	})
				// } else {
				// 	await this.uploadFilePromise(lists[i])
				// }
			}
		},
		// 上传文件
		uploadFilePromise(item) {
			return new Promise((resolve, reject) => {
				let link = this.currentUploadName === "1" ? "/upload/passable" : "/upload/health"
				uni.uploadFile({
					url: uni.$u.http.config.baseURL + link,
					filePath: item.url,
					name: 'file',
					header: {
						token: "Bearer " + uni.getStorageSync('token')
					},
					success: (result) => {
						const res = JSON.parse(result.data)
						this.loading1 = false
						this.loading2 = false
						if (res.status === "200") {
							// 通行证
							if (this.currentUploadName === "1") {
								this.pass = res.data
								// this.phone = this.pass.telephone
								this.warningFlag1 = res.data.warningFlag
								this.warningMsg1 = res.data.warningMsg.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g, '"$1":"$2"')
								this.merchantInfo.fileList1.push({
									size: item.size,
									thumb: res.data.imagesUrl,
									type: "image",
									url: res.data.imagesUrl
								})
							} else {
								// 健康码
								this.health = res.data
								this.warningFlag2 = res.data.warningFlag
								this.warningMsg2 = res.data.warningMsg.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g, '"$1":"$2"')
								this.merchantInfo.fileList2.push({
									size: item.size,
									thumb: res.data.imagesUrl,
									type: "image",
									url: res.data.imagesUrl
								})
							}
							resolve(res.data.imagesUrl)
						} else {
							let str=res.message.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g,'"$1":"$2"')
							console.log(str)
							       // let obj = JSON.parse(str).data
							       // console.log(obj)
							uni.$u.toast(`上传失败！${str}`)
						}

					},
					fail: (err) => {
						this.loading1 = false
						this.loading2 = false
						uni.$u.toast("上传失败，请重新上传！")
					}
				});
			})
		},
		// 删除图片
		deletePic(event) {
			if (event.name === "1") {
				this.merchantInfo.fileList1.splice(event.index, 1)
			} else {
				this.merchantInfo.fileList2.splice(event.index, 1)
			}
		},
	},
}
</script>
<style scoped>
</style>
<style lang="scss">
page {
	background: #F5F6F7;
}

.topTip {
	height: 42px;
	font-size: 13px;
	font-family: PingFangSC-Regular, PingFang SC;
	font-weight: 400;
	color: #262626;
	line-height: 42px;
	text-align: center;
}

.centerTip {
	height: 40px;
	background: #FEF4E8;
	text-align: center;
	line-height: 40px;
	font-size: 13px;
	font-family: PingFangSC-Regular, PingFang SC;
	font-weight: 500;
	color: #fc9300;
	margin: 11px 0;
}

.merchantInfoSty {
	.rowSty {
		height: 48px;
		background: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.right {
		margin: 0 16px;
		font-size: 16px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 400;
		color: #595959;
	}

	.left {
		margin: 0 16px
	}
}

.uploadCard {
	margin-bottom: 12px;
	background: #FFFFFF;
	box-shadow: 0px 0px 8px 0px rgba(81, 82, 84, 0.08);
	border-radius: 12px;
	padding-bottom: 16px;

	.top {
		display: flex;
		justify-content: space-between;

		.left {
			margin: 12px 0 12px 16px;
			font-size: 16px;
			font-family: PingFangSC-Medium, PingFang SC;
			font-weight: 500;
			color: #262626;
		}

		.right {
			margin: 16px 13px 0 0;
			font-size: 12px;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;
			color: #1890FF;
		}

		span {
			height: 20px;
			font-size: 12px;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;
			color: #8C8C8C;
			line-height: 20px;
		}
	}

	.bottom {
		margin-top: 16px;
		display: flex;

		.uploadIcon {
			width: 50px;
			height: 80px;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 4px;
		}

		/deep/ .u-upload__wrap__preview__image {
			width: 50px !important;
			height: 80px !important;
		}

		/deep/ .u-upload__wrap__preview{
			margin: 0 0 4px 0;
		}

		img {
			width: 50px;
			height: 80px;
		}

		.imgTip {
			width: 100%;
			text-align: center;
			// margin-top: 6px;
			margin: 0 auto;
			font-size: 12px;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;
			color: #8C8C8C;
		}

		.red {
			color: red;
		}

		.left {
			margin: 0 32px;
		}

		.right {
			text-align: center;
		}
	}

	.warningMsg {
		margin: 16px auto 0 auto;
		text-indent: 5px;
		width: calc(100% - 32px);
		height: 28px;
		background: rgba(228, 25, 25, 0.09);
		border-radius: 4px;
		font-size: 13px;
		line-height: 28px;
		font-family: PingFangSC-Regular, PingFang SC;
		font-weight: 400;
		color: #E41919;
	}
}

.bottomTip {
	margin-top: 12px;
	height: 20px;
	font-size: 13px;
	font-family: PingFangSC-Regular, PingFang SC;
	font-weight: 400;
	color: #8C8C8C;
	text-align: center;
}

.submitSty {
	width: 300px;
	margin: 20px auto 0 auto;
	padding-bottom: 40px;
	font-size: 16px;

	.notSubmit {
		background: rgba(24, 144, 255, 0.3) !important;
		border-radius: 22px;
		color: #f3f3f3 !important;

		&::after {
			border: unset;
		}
	}

	.submit {
		background: #1890ff;
		border-radius: 22px;
		color: white;
	}
}
</style>
