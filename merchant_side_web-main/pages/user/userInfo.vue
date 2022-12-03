<template>
	<view  >
		<u-loading-page :loading="loading" bg-color="#e8e8e8"></u-loading-page>

		<u-gap height="12" bgColor="#F5F6F7"></u-gap>
		<u--form :model="model1" :rules="rules" ref="form1">
			<view class="u-form-div">
				<view class="info_title">证件信息</view>
				  <u-row>
						<u-col span="3" v-if="fileList[0].url != ''">
							<view class="demo-layout bg-purple-light" >
								<view class="fifle_img">营业执照</view>
								<u-upload
									:fileList="fileList"
									:deletable="false"
									:disabled = "true"
									name="5"
									width="76"
									height="52"
									multiple
									:maxCount="1"
								></u-upload>
							</view>
						</u-col>
						<u-col span="3" offset="1">
							<view class="demo-layout bg-purple">
								<view class="fifle_img">许可证</view>
								<u-upload
									:fileList="fileList1"
									:deletable="false"
									:disabled = "true"
									name="5"									
									width="76"									
									height="52"
									multiple
									:maxCount="1"
								></u-upload>
							</view>
						</u-col>
						<u-col span="3" offset="1">
							<view class="demo-layout bg-purple">
								<view class="fifle_img">备案证</view>
								<u-upload
									:fileList="fileImageUrl"
									:deletable="false"
									:disabled = "true"
									name="5"									
									width="76"									
									height="52"
									multiple
									:maxCount="1"
								></u-upload>
							</view>
						</u-col>
					</u-row>
					<u-row>
											<u-col span="3">
												<view class="demo-layout bg-purple-light" >
													<view class="fifle_img">卫生许可证</view>
													<u-upload
														:fileList="hygieneImageUrl"
														:deletable="false"
														:disabled = "true"
														name="5"
														width="76"
														height="52"
														multiple
														:maxCount="1"
													></u-upload>
												</view>
											</u-col>
											<u-col span="3" offset="1">
												<view class="demo-layout bg-purple">
													<view class="fifle_img">店招照片</view>
													<u-upload
														:fileList="shopHeadPhotoUrl"
														:deletable="false"
														:disabled = "true"
														name="5"									
														width="76"									
														height="52"
														multiple
														:maxCount="1"
													></u-upload>
												</view>
											</u-col>
										</u-row>
					<u-form-item label="店铺名称" :labelWidth="labelWidth"   prop="storeName" borderBottom ref="item1">
						<view class="textRight" >{{model1.businessLicense.storeName}}</view>
					</u-form-item>
					<u-form-item label="经营类型" :labelWidth="labelWidth"   prop="storeName" borderBottom ref="item1">
						<view class="textRight" >{{model1.businessLicense.businessType}}</view>
					</u-form-item>
					<u-form-item label="法人姓名" :labelWidth="labelWidth" type="text" prop="representName" borderBottom ref="item1">
						<u--input
								v-model="model1.businessLicense.representName" type="text" :readonly ="true" border="none" inputAlign="right" :disabled = "true"
						></u--input>
					</u-form-item>
					<u-form-item label="信用代码" :labelWidth="labelWidth" prop="socialCreditCode" borderBottom ref="item1">
						<u--input
								v-model="model1.businessLicense.socialCreditCode" type="text" :readonly ="true" border="none" inputAlign="right" :disabled = "true"
						></u--input>
					</u-form-item>
					<u-form-item label="地　　址" :labelWidth="labelWidth" prop="detailedAddress" borderBottom ref="item1">
						<view class="textRight" v-if="disabled">{{model1.businessLicense.detailedAddress}}</view>
						<u--input v-else
								v-model="model1.businessLicense.detailedAddress" type="text" :readonly ="true" border="none" inputAlign="right" :disabled = "true"
						></u--input>
					</u-form-item>
					<u-form-item label="有 效 期" :labelWidth="labelWidth" prop="validPermanent" borderBottom ref="item1">
						<view class="textRight">
							<text v-if="model1.businessLicense.validPermanent">永久有效</text>
							<text v-else>{{model1.businessLicense.validPeriod}}</text>
						</view>
					</u-form-item>
		
			</view>	
			<u-gap height="12" bgColor="#F5F6F7"></u-gap>
			<!-- <view class="u-form-div"> -->
				<u-collapse accordion ref="refData" :value="value" :duration="100"  @change="collapseName=collapseName == '收起' ? '展开' : '收起' ">
				<u-collapse-item  name="1"  title="经营信息" :disabled="!disabled" :isLink="disabled" :value="collapseName" >
					<u-form-item label="店招名称" :labelWidth="labelWidth" :required="!disabled" prop="model1.name" borderBottom ref="item1">
						<u--input
								v-model="model1.storeName" type="text" :readonly ="readonly" border="none" inputAlign="right" :disabled = "disabled"
						></u--input>
					</u-form-item>
					<u-form-item label="所在行业" :labelWidth="labelWidth" prop="model1.name" borderBottom ref="item1">
						<view class="textRight">
							<text>{{model1.categoryInfoMap[1].categoryName}}</text>
							<text v-if="model1.categoryInfoMap[2].categoryName" style="padding: 0 10px;">-</text>
							<text>{{model1.categoryInfoMap[2].categoryName}}</text>							
						</view>
					</u-form-item>
					<u-form-item label="实际经营人姓名" :labelWidth="labelWidth" :required="!disabled" prop="model1.name" borderBottom ref="item1">
						<u--input
								v-model="model1.operatorName" type="text" :readonly ="readonly" border="none" inputAlign="right" :disabled = "disabled"
						></u--input>
					</u-form-item>
					<u-form-item label="电话号码"  :labelWidth="labelWidth" :required="!disabled" prop="model1.representName" borderBottom ref="item1">
						<u--input
								v-model="model1.operatorPhone" type="text"  :readonly ="readonly" border="none" inputAlign="right" disabled
						></u--input>
					</u-form-item>

					
					<u-form-item label="实际经营面积" :labelWidth="labelWidth" prop="model1.name" borderBottom ref="item1">
						<!-- 只读的时候展示 -->
						<view class="textRight">
							<text v-if="model1.storeArea == 1">150平方米以下</text>
							<text v-if="model1.storeArea == 2">151-500平方米</text>
							<text v-if="model1.storeArea == 3">501-1000平方米</text>
							<text v-if="model1.storeArea == 4">1001-3000平方米</text>
							<text v-if="model1.storeArea == 5">3001以上平方米</text>
						</view>
						<!-- 编辑得时候 -->
					</u-form-item>
					
					<u-form-item label="是否学校周边50m" :labelWidth="155"  prop="model1.name" borderBottom ref="item1">
						<view class="textRight">
							<text v-if="model1.awayFromSchool">是</text>
							<text v-else>否</text>
						</view>
					</u-form-item>
					
					<u-form-item label="是否是奶茶店" :labelWidth="155"  prop="model1.name" borderBottom ref="item1">
						<view class="textRight">
							<text v-if="model1.isMilkyTea">是</text>
							<text v-else>否</text>
						</view>
					</u-form-item>
					
					<u-form-item label="是否提供外卖" :labelWidth="155"  prop="model1.name" borderBottom ref="item1">
						<view class="textRight">
							<text v-if="model1.isTakeOut">是</text>
							<text v-else>否</text>
						</view>
					</u-form-item>
			
				</u-collapse-item>
				</u-collapse >
			<!-- </view>	 -->
			
			<u-gap height="12" bgColor="#F5F6F7"></u-gap>
			<!-- <view class="u-form-div"> -->
				<u-collapse accordion :value="2" v-if="disabled" :duration="100" @change="collapseName1=collapseName1 == '收起' ? '展开' : '收起'">
				<u-collapse-item  name="2"  title="监管信息" :disabled="!disabled" :isLink="disabled"  :value="collapseName1" >
					<u-form-item label="省市区" :labelWidth="labelWidth" prop="model1.name" borderBottom ref="item1">
						<view class="textRight" >
							<text v-for="(item,index) in model1.addressInfoMap" :key="index">{{item.name}}</text>
						</view>
					</u-form-item>
					
					<u-form-item label="实际地址" :labelWidth="labelWidth" prop="model1.name" borderBottom ref="item1">
						<view class="textRight">{{model1.address}}</view>
					</u-form-item>
					
					<u-form-item label="监管所" :labelWidth="labelWidth" prop="model1.representName" borderBottom ref="item1">
						<u--input
								v-model="model1.supervisionName" border="none" inputAlign="right" :disabled = "disabled"
						></u--input>
					</u-form-item>
					
					<u-form-item label="所属社区" :labelWidth="labelWidth" prop="model1.representName" borderBottom ref="item1">
						<!-- <view class="textRight" v-if="disabled">{{model1.businessPlace}}</view> -->
						<u--input 
								v-model="model1.communityName" border="none" inputAlign="right" :disabled = "disabled"
						></u--input>
					</u-form-item>
			
				</u-collapse-item>
				</u-collapse >
		</u--form>			
	
				<view class="BD">
					<span>负责BD：{{model1.bd}}</span>
					<span style="margin-left: 15px;">负责BDM：{{model1.bdm}}</span>
				</view>
		<!-- </view>	 -->
	<!-- </u--form>			 -->
		<view class="Ubutton">
			<u-button type="primary" @click="onDite" size="small">{{butnText}}</u-button>
		</view>
	</view>
</template>

<script>
    import { apiDetail,apiUploadConfig,apiUpload,apiupdate,apiOption,apibank } from "@/config/merchant.js";
	export default {
		data() {
			return {
				rules: {
				},
				model1:{
					  appointmentBank:{
						  appointmentTime:Number(new Date())
					  },
					  businessLicense:{
						  validPeriod:"",
						  // imagesUrl:""
					  },	  
					  timeDate:Number(new Date()),
					  bd:"",
					  bdm:""
				},
				collapseName:"收起",
				collapseName1:"收起",
				labelWidth:'120',
				minDate:new Date(new Date()).getTime()	,
				fileList: [],
				fileList1: [],
				disabled:true,
				timeDate:false,
				pickershow:false,
				tmentshow:false,
				readonly:false,
				loading:false,
				butnText:"编辑",
				validPeriod:Number(new Date()),
				categoryType:{
					value:"请选择",
					name:"请选择"
				},
				uPickerShow:false,
				columns:[],
				uPickerShow1:false,
				columns1:[],
				value:"1",
				bankDataList:[],
				fileImageUrl:[],
				hygieneImageUrl:[],
				shopHeadPhotoUrl:[],
				bankShow:false,
				bankName:"",
				storeName:"请选择",
				storeAreaShow:false,
				columns2:[
					[
					{id:1,name:"150平方米以下"},
					{id:2,name:"151-500平方米"},
					{id:3,name:"501-1000平方米"},
					{id:4,name:"1001-3000平方米"},
					{id:5,name:"3001以上平方米"},
					]
				]
				}
		},
		// onReady() {
		// 			// 微信小程序需要用此写法
		// 			this.$refs.datetimePicker.setFormatter(this.formatter)
		// 		},
		onLoad(list){
			let data = JSON.parse(decodeURIComponent(list.data))
			console.log(data)
			this.dataList()
			this.bankList()
		},
		methods: {
			bankList(){
				apibank().then((res)=>{
					console.log(res)
					this.bankDataList = [res.data]
				})
			},
			dataList(){
				this.loading = true
				apiDetail().then((res)=>{
					console.log(res)
					this.loading = false
					this.model1 = res.data
					// 营业执照
					if(res.data.businessLicense.imagesUrl){
						let arry = [
							{
							   url: res.data.businessLicense.imagesUrl
							}
						]
						this.fileList= arry
					}
					// 许可证
					if(res.data.foodBusinessLicense.imagesUrl){
						let list = [
							{
								url: res.data.foodBusinessLicense.imagesUrl
							}
						]
						this.fileList1 = list
					}
					if(res.data.fileImageUrl){
						let list1 = [
							{
								url: res.data.fileImageUrl
							}
						]
						this.fileImageUrl = list1
					}
					if(res.data.hygieneImageUrl){
						let list2 = [
							{
								url: res.data.hygieneImageUrl
							}
						]
						this.hygieneImageUrl = list2
					}
					
					if(res.data.shopHeadPhotoUrl){
						let list3 = [
							{
								url: res.data.shopHeadPhotoUrl
							}
						]
						this.shopHeadPhotoUrl = list3
					}
					
					if(res.data.hygieneImageUrl){
						let list2 = [
							{
								url: res.data.hygieneImageUrl
							}
						]
						this.hygieneImageUrl = list2
					}
					
					if(res.status != "200"){
						this.loading = false
						uni.showToast({
						  title: "加载失败！",
						  duration: 2000,
						  icon: "error"
						});
					}
				}).catch((res)=>{
					this.loading = false
				})
			},
			// 删除图片
			deletePic(){
				// 本地清空
				this.fileList=[]
				// 表单里清空
				this.model1.businessLicense.imagesUrl = ""
			},
			deletePic1(){
				// 本地清空
				this.fileList1=[]
				// 表单里清空
				this.model1.foodBusinessLicense.imagesUrl = ""
			},
			// 营业执照
		  async	afterRead(event){
				console.log(event.file,"file")
				let lists = [].concat(event.file)
			    let cot = await this.uploadFilePromise(lists[0].url)
				console.log(cot)
			},
			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: '上传图片中...'
					});
					let a = uni.uploadFile({
						url: uni.$u.http.config.baseURL + '/upload/businessLicense', // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						formData: {
							user: 'test'
						},
						success: (res) => {
							// 取消加载
							setTimeout(function () {
								uni.hideLoading();
							}, 100);
							// 处理  "data:"{a:"1"}""
						    let str=res.data.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g,'"$1":"$2"')
							let obj = JSON.parse(str).data
							console.log(obj)
				
							console.log(obj.imagesUrl)
							// this.model1.businessLicense = {
							// 		imagesUrl:obj.imagesUrl,
							// 		storeName:obj.storeName,
							// 		representName:obj.representName,
							// 		detailedAddress:obj.detailedAddress,
							// 		socialCreditCode:obj.socialCreditCode,
							// }
							// 营业执照
							this.model1.businessLicense.imagesUrl = obj.imagesUrl
							// 店铺名称
							this.model1.businessLicense.storeName = obj.storeName
							// 法人
							this.model1.businessLicense.representName = obj.representName
							// 地址
							this.model1.businessLicense.detailedAddress = obj.detailedAddress
							//信用code
							this.model1.businessLicense.socialCreditCode = obj.socialCreditCode
							// 经营范围
							this.model1.businessLicense.businessType =obj.businessType
							let arry = [
								{
								   url: obj.imagesUrl
								}
							]
							console.log(arry)
							this.fileList= arry
						},
						fail:(res)=>{
							uni.hideLoading();
							uni.$u.toast("网络请求超时，请重新加载！")
						}
					});
				})
			},
			// 许可证
			async afterRead1(event){
							console.log(event.file,"file")
							let lists = [].concat(event.file)
						    let cot = await this.uploadFilePromise1(lists[0].url)
							console.log(cot)
						},
						uploadFilePromise1(url) {
							return new Promise((resolve, reject) => {
								uni.showLoading({
									title: '上传图片中...'
								});
								let a = uni.uploadFile({
									url: uni.$u.http.config.baseURL + '/upload/foodBusinessLicense', // 仅为示例，非真实的接口地址
									filePath: url,
									name: 'file',
									formData: {
										user: 'test'
									},
									success: (res) => {
										// 处理  "data:"{a:"1"}""
									    let str=res.data.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g,'"$1":"$2"')
										var obj = JSON.parse(str)
										// 取消加载
										setTimeout(function () {
											uni.hideLoading();
										}, 100);
										// 营业执照
										this.model1.foodBusinessLicense.imagesUrl = obj.data.imagesUrl
										let arry = [
											{
											   url: obj.data.imagesUrl
											}
										]
										this.fileList1= arry
									},
									fail:(res)=>{
										uni.hideLoading();
										uni.$u.toast("网络请求超时，请重新加载！")
									}
								});
							})
						},
			// 实际面积
			confirmName2(value){
				console.log(value)
				let id = value.value[0].id
				let name = value.value[0].name
				this.model1.storeArea = id
				this.storeName = name
				this.storeAreaShow = false
			},
			// 行业父
			onName(){
				// this.columns = []
				this.uPickerShow = true
				apiOption().then((res)=>{
				  this.columns = [res.data]
				  console.log(this.columns)
				})
			},
			// 行业父确定按钮
			confirmName(value){
				console.log(value.value)
				let categoryId = value.value[0].categoryId
				let categoryName = value.value[0].categoryName
				this.model1.industryType = categoryId
				this.categoryType.name = categoryName
				this.categoryType.value = "请选择"
				this.uPickerShow = false
				let data = {
					parentCategoryId:value.value[0].categoryId
				}
				this.showData(data)
			},
			onbank(){
				this.bankShow = true
			},
			confirmbank(value){
				this.model1.appointmentBank.bankName = value.value[0].bankName
				this.model1.appointmentBank.bankId = value.value[0].bankCode
				this.bankName = value.value[0].bankName
				this.bankShow = false
			},
			showData(data){
				apiOption(data).then((res)=>{
				  this.columns1 = [res.data]
				})
			},
			confirmName1(value){
					let categoryId = value.value[0].categoryId
					let categoryName = value.value[0].categoryName
					this.uPickerShow1 = false
					this.categoryType.value = categoryName
					this.model1.industryType = categoryId
					// this.showData(value.value[0])
			},
			// 行业子
			onValue(){
			  this.uPickerShow1 = true
			},
			onDite(){
				if(this.disabled){
					// 编辑
					this.butnText = "保存"
					// 初始化
					this.$nextTick((res)=>{
						this.$refs.refData.init()
					})
					// 初始化后赋值
					this.value = "1"
					// 解锁文本框
					this.disabled = false	
					// 赋值折叠文字 “收起” “展开”
					this.collapseName = ""
					if(this.model1.storeArea == 1) this.storeName = "150平方米以下"
					if(this.model1.storeArea == 2) this.storeName = "151 - 500平方米"
					if(this.model1.storeArea == 3) this.storeName = "501 - 1000平方米"
					if(this.model1.storeArea == 4) this.storeName = "1001 - 3000平方米"
					if(this.model1.storeArea == 5) this.storeName = "3001以上平方米"
					console.log(this.model1.appointmentBank)
					let bankName = this.model1.appointmentBank.bankName
					if(bankName != null && bankName != ""){
						this.bankName = this.model1.appointmentBank.bankName
					}else{
						this.bankName = ""
					}
					// if(this.model1.wechatCompanyFlag == false){
					// 	this.timeDate = true
					// }
					if(this.model1.businessLicense.validPermanent == false){
						this.timeDate = true
					}
					// 行业类型
					let categoryInfoMap = JSON.parse(JSON.stringify(this.model1.categoryInfoMap)) 
					let categoryName = categoryInfoMap[1].categoryName
					if(categoryName != null || categoryName != undefined ){
						this.categoryType.name = categoryInfoMap[1].categoryName
						let data = {
							parentCategoryId:categoryInfoMap[1].categoryId
						}
						this.showData(data)
					}
					let categoryName1 = categoryInfoMap[2]
					if(categoryName1 !=null || categoryName1 != undefined){
						this.categoryType.value = categoryName1.categoryName
					}
				}else{
					let mode = this.model1
					if(mode.storeName == ""){
					  uni.showToast({
					    title: "店招名称为空！",
					    duration: 2000,
					    icon: "error"
					  });
					} 
					if(mode.operatorName == ""){
						uni.showToast({
						  title: "实际经营人为空！",
						  duration: 2000,
						  icon: "error"
						});
					}else{
						this.updata()
					}
					// 保存
					console.log(this.model1)
				
				}
			},
			updata(){
				uni.showLoading({
					title: '保存中...'
				});
				apiupdate(this.model1).then((res)=>{
					if(res.status == "200"){
						console.log(res)
						// 取消加载
						setTimeout(function () {
							uni.hideLoading();
						}, 100);
						this.disabled = true
						this.dataList()
						this.butnText = "编辑"
						// 指定时间
						this.timeDate = false
						// 赋值折叠文字 “收起” “展开”
						this.collapseName = "收起"
					}else{
						uni.showToast({
						  title:res.message,
						  duration: 2000,
						  icon: "error"
						});
					}
					
				})
			},
			radioChange(value){
				// 指定时间
				if(value == false){
					this.timeDate = true
					// 默认当天
					let date = Number(new Date())
					let timeFormat = uni.$u.timeFormat
					this.model1.businessLicense.validPeriod = timeFormat(date.value, 'yyyy-mm-dd')
				}else{
					this.timeDate = false
					this.model1.businessLicense.validPeriod = ""
				}
			},
			onpickershow(e){
				this.readonly = false
				let timeFormat = uni.$u.timeFormat
				this.pickershow = false
			    console.log(timeFormat(e.value, 'yyyy-mm-dd'))
				setTimeout(()=>{
				this.model1.businessLicense.validPeriod = timeFormat(e.value, 'yyyy-mm-dd')
				console.log(this.model1.businessLicense.validPeriod)
				},10)
			},
			// 预约开通时间
			onAppointmentTime(e){
				console.log(e)
				this.readonly = false
				let timeFormat = uni.$u.timeFormat
				this.tmentshow = false
				console.log(timeFormat(e.value, 'yyyy-mm-dd'))
				setTimeout(()=>{
				this.model1.appointmentBank.appointmentTime = timeFormat(e.value, 'yyyy-mm-dd')
				console.log(this.model1.appointmentBank.appointmentTime)
				},10)
			}
		}
	}
</script>

<style lang="scss">
	/deep/ .u-fade-enter-active, .u-fade-leave-active {
    z-index: 9999;
	}
	/deep/ .u-input--radius.data-v-113bc24f, .u-input--square.data-v-113bc24f {
		border-radius: 4px;
		background: #fff !important;
	}
	.u-form-div{
		padding: 15px 15px 0 15px;
	}
	.u-line{
		    border-bottom-width: 0.9px !important;
		    border-color: #F5F5F5 !important;
	}
	.u-form-item__body__left__content__label{
		color: #595959 !important;
		font-size: 16px !important;
	}
	.info_title{
		color: #000000;
		font-size: 16px;
		font-weight: 500
	}
	.fifle_img{
		color: #595959;
		font-size: 14px;
		padding: 15px 0;
	}
	.demo-layout{
		padding-bottom: 10px;
	}
	.BD{
		height: 50px; 
		color:#8C8C8C;
		padding: 20px 0 55px 0;
		text-align: center;
		font-size: 16px;
	}
	.Ubutton{
	  background: #FFFFFF;
	  position: fixed;
	  bottom: 0;
	  left: 0;
	  right: 0;
	  padding: 13px 30px;
	  z-index: 999;
	}
	/deep/ .u-button--square{
	      border-radius: 30px !important;
	  }
	/deep/ .u-button--primary {
	      border-width: 0px !important;
	  }
	/deep/ .u-button--small{
	      padding: 22px 8px !important;
	  }
	/deep/ .u-button__text{
		font-size: 16px !important;
		position: relative;
		bottom: 2px;
	}
	/deep/ .u-cell__title-text{
		 color: #000000 !important;
		 font-size: 16px !important;
		 font-weight: 500
	 }
	/deep/ .u-cell__value{
		color:#1890FF !important;
	} 
	
	// 手风琴得高度
	/deep/.u-collapse-item__content.data-v-2675fb21{
		max-height: 400px !important;
	}
	.textRight{
		width: 100%;
		// text-align: right;\
		min-height: 15px;
		min-width: 100px;
		text-align: right;
		display: flex;
		justify-content: flex-end;
	}
	/deep/ .u-input__content__field-wrapper__field {
	    line-height: 26px;
	    text-align: left;
	    color: #303133;
	    height: 24px;
	    font-size: 15px;
	    flex: 1;
		// border:1px solid red;
		z-index: 10 !important;
	}
	/deep/ .u-radio-group--row {
		justify-content: flex-end;
	}
	/deep/ .u-radio__text{
		font-size: 14px !important;
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
	

</style>
