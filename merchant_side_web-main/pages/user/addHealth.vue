<template>
	<view name="addhealth" class="addhealth">
		<view v-if="current==0">
			<!-- 新增 -->
			<view class="form1" >
				<view class="top-title">证件信息</view>
				<view class="liencese">
					<u--form>
						<view class="class">
							<u-form-item required label="健康证" prop="userInfo.name" labelWidth="200" ref="item2">
							</u-form-item>
							<u-upload :fileList="fileList" @afterRead="afterRead" @delete="deletePic" name="1" multiple
								:maxCount="1" :previewFullImage="true"></u-upload>
						</view>
					</u--form>
				</view>
			</view>
			<view class="form">
				<u--form labelPosition="left" :model="model1"  ref="form1" 
					borderBottom>
					<u-form-item required :labelWidth="65" label="姓名" prop="model1.name" borderBottom >
						<u--input v-model="model1.name" inputAlign="right" :adjustPosition="false" clearable type="text"
							placeholder="请输入姓名" @clear="model1.name=''"></u--input>
					</u-form-item>
					<u-form-item required :labelWidth="65" label="电话" prop="model1.phone" borderBottom >
						<u--input v-model="model1.phone" inputAlign="right" :adjustPosition="false" type="number" clearable
							placeholder="请输入电话"  @clear="model1.phone=''"></u--input>
					</u-form-item>
			
					<u-form-item required :labelWidth="65" label="身份证" prop="model1.identityCard" borderBottom >
			
						<u--input v-model="model1.identityCard" inputAlign="right" :adjustPosition="false" clearable
							type="idcard" placeholder="请输入身份证"  @clear="model1.identityCard=''"></u--input>
					</u-form-item>
			
					<u-form-item required label="职位" prop="receiveDate" borderBottom :labelWidth="65"
						@click="showPosition = true" >
						<u--input v-model="positionName" disabled disabledColor="#ffffff" placeholder="请选择职位"
							 inputAlign="right"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<!-- 取证时间 -->
					<u-form-item required label="取证时间" prop="receiveDate" borderBottom :labelWidth="65"
						@click="showDate = true" >
						<u--input v-model="model1.receiveDate" disabled disabledColor="#ffffff" placeholder="请选择取证时间"
							 inputAlign="right"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<!-- 有效期 -->
					<u-form-item required label="有效期" prop="model1.validPeriod" borderBottom :labelWidth="65"
						@click="showValidPeriod = true" >
						<u--input v-model="model1.validPeriod" disabled disabledColor="#ffffff" placeholder="请选择有效期"
							 inputAlign="right"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
				</u--form>
				
				<!-- 职位  下拉 -->
				<u-picker :show="showPosition" :value="model1.position" :columns="columns" @confirm="selectPosition"
					keyName="name" @cancel="showPosition=false"></u-picker>
				<!-- 取证时间 下拉-->
				<u-datetime-picker :show="showDate" :value="model1.receiveDate" mode="date" closeOnClickOverly
					@confirm="onConfirm" @cancel="showDate=false" :formatter="formatter" :maxDate="maxDate">
				</u-datetime-picker>
				<!-- 有效期  下拉-->
				<u-datetime-picker :show="showValidPeriod" :value="model1.validPeriod" mode="date" closeOnClickOverly
					@confirm="onValidConfirm" @cancel="showValidPeriod=false" :formatter="formatter" :minDate="minDate">
				</u-datetime-picker>
			</view>
			<!-- 提交 -->
			<view class="footer-btn">
				<u-button type="primary" @click="submit" :disabled="disabled" shape="circle" text="提交"></u-button>
			</view>
		</view>
		<view v-if="current==1">
			<!-- 新增 -->
			<view class="form1" >
				<view class="top-title">证件信息</view>
				<view class="liencese">
					<u--form>
						<view class="class">
							<u-form-item required label="从业证"  labelWidth="200" ref="item2">
							</u-form-item>
							<u-upload :fileList="fileEmployList" @afterRead="afterReadEmploy" @delete="deletePic" name="1" multiple
								:maxCount="1" :previewFullImage="true"></u-upload>
						</view>
					</u--form>
				</view>
			</view>
			<view class="form">
				<u--form labelPosition="left" :model="model2" 
					borderBottom>
					<u-form-item required :labelWidth="65" label="姓名" prop="model2.name" borderBottom >
						<u--input v-model="model2.name" inputAlign="right" :adjustPosition="false" clearable type="text"
							placeholder="请输入姓名"  @clear="model2.name=''"></u--input>
					</u-form-item>
					<u-form-item required :labelWidth="65" label="电话" prop="model2.phone" borderBottom >
						<u--input v-model="model2.phone" inputAlign="right" :adjustPosition="false" type="number" clearable
							placeholder="请输入电话"  @clear="model2.phone=''"></u--input>
					</u-form-item>
			
					<u-form-item required :labelWidth="65" label="身份证" prop="model2.identityCard" borderBottom >
			
						<u--input v-model="model2.identityCard" inputAlign="right" :adjustPosition="false" clearable
							type="idcard" placeholder="请输入身份证"  @clear="model2.identityCard=''"></u--input>
					</u-form-item>
			
					<u-form-item required label="证照类型" prop="receiveDate" borderBottom :labelWidth="65"
						@click="showType = true" >
						<u--input v-model="typeName" disabled disabledColor="#ffffff" placeholder="请选择证照类型"
							 inputAlign="right"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<!-- 取证时间 -->
					<u-form-item required label="取证时间" prop="receiveDate" borderBottom :labelWidth="65"
						@click="showEmployDate = true" >
						<u--input v-model="model2.receiveDate" disabled disabledColor="#ffffff" placeholder="请选择取证时间"
							 inputAlign="right"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
					<!-- 有效期 -->
					<u-form-item required label="有效期" prop="model2.validPeriod" borderBottom :labelWidth="65"
						@click="showEmployValid = true" >
						<u--input v-model="model2.validPeriod" disabled disabledColor="#ffffff" placeholder="请选择有效期"
							 inputAlign="right"></u--input>
						<u-icon slot="right" name="arrow-right"></u-icon>
					</u-form-item>
				</u--form>
				<!-- 证照类型 下拉 -->
				<u-picker :show="showType" :value="model2.type" :columns="columns1" @confirm="selectPosition"
					keyName="name" @cancel="showType=false"></u-picker>
				<!-- 取证时间 下拉-->
				<u-datetime-picker :show="showEmployDate" :value="model2.receiveDate" mode="date" closeOnClickOverly
					@confirm="onConfirm" @cancel="showEmployDate=false" :formatter="formatter" :maxDate="maxDate">
				</u-datetime-picker>
				<!-- 有效期  下拉-->
				<u-datetime-picker :show="showEmployValid" :value="model2.validPeriod" mode="date" closeOnClickOverly
					@confirm="onValidConfirm" @cancel="showEmployValid=false" :formatter="formatter" :minDate="minDate"
                >
				</u-datetime-picker>
			</view>
			<!-- 提交 -->
			<view class="footer-btn">
				<u-button type="primary" @click="submit" :disabled="disabled" shape="circle" text="提交"></u-button>
			</view>
		</view>
	</view>

</template>

<script>
	import {
		addHealth,
		addEmployment,
	} from '@/config/health.js'
	export default {
		name: "addhealth",
		props: ['current'],
		data() {

			return {
				fileList: [],
 
				model1: {
					name: '',
					phone: '',
					position: '', //职位
					identityCard: '', //身份证
					receiveDate: '', //取证日期
					validPeriod: '', //有效期
					imageUrl: '',
				},
				positionName:'',//职位名称
				
				showPosition: false, //职位
				showDate: false, //时间
				showValidPeriod: false, //有效期
				disabled:false,   //按钮禁用
				columns: [
					[{
						id: 1,
						name:"其他"
					}, {
						id: 2,
						name:"合作社-管理人员"
					},{
						id: 3,
						name:"种植户-从业人员"
					},
					{
						id: 4,
						name:"药品安全培训-销售企业-从业人员"
					},{
						id: 5,
						name:"保健食品企业-管理人员"
					},{
						id: 6,
						name:'"三小企业"-从业人员'
					},
					{
						id: 7,
						name:'"三小企业"-管理人员'
					},{
						id: 8,
						name:"生产企业-从业人员"
					},{
						id: 9,
						name:"生产企业-管理人员"
					},{
						id: 10,
						name:"销售企业-从业人员"
					},{
						id: 11,
						name:"销售企业-管理人员"
					},{
						id: 12,
						name:"餐饮企业-从业人员"
					},{
						id: 13,
						name:"餐饮企业-管理人员"
					},{
						id: 14,
						name:"安全员"
					},{
						id: 15,
						name:"生产员"
					},]
				],
				errorType: 'border-bottom', //错误提示方法
				
				// 从业证
				model2: {
					name: '',
					phone: '',
					type: '', //证照类型
					identityCard: '', //身份证
					receiveDate: '', //取证日期
					validPeriod: '', //有效期
					imageUrl: '',
				},
				columns1: [
                      [{
						id: 1,
						name:"从业资格证"
					}],
					],
				fileEmployList:[],//从业证
				typeName: '', //证照类型
				showType: false, //显示证照类型
				showEmployDate:false,//显示从业证取证时间
				showEmployValid:false,//显示从业证有效期
				minDate:'',//限制时间
				maxDate:'',//限制时间
			}
		},
		created() {
			console.log(this.current,"current")
			this.getToday();
		},
		
		methods: {
			//健康证  提交
			submit() {
				this.disabled = true
				if(this.current==null){
					this.disabled = true
				}
				if(this.current==0){
					let data = this.model1
				addHealth(data).then(res => {
					if(res.data>0){
						uni.showToast({
							title: '提交成功',
							duration: 1000
						});
						setTimeout((res)=>{
							this.disabled = false
						},200)
						uni.reLaunch({
							url: '/pages/user/result?data=1&current=0'
						});
					}else{
						this.disabled = false
					}
					console.log(res, "新增")
				}).catch(err=>{
					this.disabled = false
				})
				}
				if(this.current==1){
					let data1 = this.model2
					addEmployment(data1).then(res => {
						if(res.data>0){
							uni.showToast({
								title: '提交成功',
								duration: 1000
							});
							setTimeout((res)=>{
								this.disabled = false
							},200)
							uni.reLaunch({
								url: '/pages/user/result?data=1&current=1'
							});
						}else{
							this.disabled = false
						}
						console.log(res, "新增从业证")
					}).catch(err=>{
					this.disabled = false
				})
				}
			},
			// 删除图片
			deletePic(event) {
				console.log(event,"删除event")
				if(this.current==0){
					this.fileList=''
				    this.model1.imageUrl=''
				}
				if(this.current==1){
					this.fileEmployList=''
				    this.model2.imageUrl=''
				}
			},
			// 新增图片
			async afterRead(event) {
				// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				const result = await this.uploadFilePromise(lists[0].url)
				console.log(result, "result")
			},
			uploadFilePromise(url) {
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: '上传图片中...'
					});
					let a = uni.uploadFile({
						url: uni.$u.http.config.baseURL + '/upload/health/certificate', // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						success: (res) => {
							console.log(res.data, "tu片")
							let str=res.data.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g,'"$1":"$2"')
							var obj = JSON.parse(str).data
							console.log(obj,'obj')
								// 取消加载
								setTimeout(function() {
									uni.hideLoading();
								}, 100);
                               uni.showToast({
                               	title: '上传成功',
                               	duration: 200
                               });
							this.fileList=[{url:obj}]
							this.model1.imageUrl=obj
						console.log(this.fileList,"url")
						},
						fail:(res)=>{
							uni.hideLoading();
    					uni.$u.toast("网络请求超时，请重新加载！")
						}
					});
				})
			},
			// 新增图片  从业证
			async afterReadEmploy(event) {
				// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				const result = await this.uploadEmployFilePromise(lists[0].url)
				console.log(result, "result")
			},
			uploadEmployFilePromise(url) {
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: '上传图片中...'
					});
					let a = uni.uploadFile({
						url: uni.$u.http.config.baseURL + '/upload/employment/certificate', // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						success: (res) => {
							console.log(res.data, "tu片")
							let str=res.data.replace(/(\w+)\s*=\s*\'?([^\'},]+)\'?/g,'"$1":"$2"')
							var obj = JSON.parse(str).data
							console.log(obj,'obj')
			
								// 取消加载
								setTimeout(function() {
									uni.hideLoading();
								}, 100);
			                   uni.showToast({
			                   	title: '上传成功',
			                   	duration: 200
			                   });
							this.fileEmployList=[{url:obj}]
							this.model2.imageUrl=obj
						console.log(this.fileEmployList,"url2")
						},
						fail:(res)=>{
							uni.hideLoading();
							uni.$u.toast("网络请求超时，请重新加载！")
						}
					});
				})
			},
			//职位
			selectPosition(e) {
				console.log(this.current,"current")
				console.log(e.value[0].name, "e")
				if(this.current==0){
					this.showPosition = false
				setTimeout(() => {
					this.model1.position = e.value[0].id
					this.positionName=e.value[0].name
					console.log(this.model1.position, "ppp")
				}, 10)
				}
				if(this.current==1){
					this.showType = false
				setTimeout(() => {
					this.model2.type = e.value[0].id
					this.typeName=e.value[0].name
					console.log(this.model2.position, "ppp")
				}, 10)
				}
			},
			
			formatter(type, value) {
				if (type === 'year') {
					return `${value}年`
				}
				if (type === 'month') {
					return `${value}月`
				}
				if (type === 'day') {
					return `${value}日`
				}
				return value
			},
			//取证时间确认  确定按钮(自定义转换格式)
			onConfirm(e) {
				const timeFormat = uni.$u.timeFormat
				console.log(timeFormat(e.value, 'yyyy-mm-dd'))
				if(this.current==0){
					this.showDate = false
				setTimeout(() => {
					this.model1.receiveDate = timeFormat(e.value, 'yyyy-mm-dd')
				}, 10)
				}
				if(this.current==1){
					this.showEmployDate = false
				setTimeout(() => {
					this.model2.receiveDate = timeFormat(e.value, 'yyyy-mm-dd')
				}, 10)
				}
			},
             //获取当前时间
               getToday() {
				   this.maxDate=new Date().getTime()
				   this.minDate=new Date().getTime()+24*60*60*1000
               },
			//有效期确认  确定按钮(自定义转换格式)
			onValidConfirm(e) {
				const timeFormat = uni.$u.timeFormat
				console.log(timeFormat(e.value, 'yyyy-mm-dd'))
				if(this.current==0){
					this.showValidPeriod = false
				setTimeout(() => {
					this.model1.validPeriod = timeFormat(e.value, 'yyyy-mm-dd')
				}, 10)
				}
				if(this.current==1){
					this.showEmployValid = false
				setTimeout(() => {
					this.model2.validPeriod = timeFormat(e.value, 'yyyy-mm-dd')
				}, 10)
				}
			},

			
		}
	}
</script>

<style lang="scss">
	.addHealth {
		color: #595959;

	}

	.top-title {
		font-size: 16px;
		font-weight: bolder;
	}

	.form {
		padding: 10px 20px;
		background-color: #fff !important;
	}

	.form1 {
		padding: 20px 20px;
		border-bottom: 12px solid #F5F6F7;
		background-color: #fff !important;
	}

	.footer-btn {
		padding: 13px 20px;
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0px;
		right: 0px;
		left: 0px;
		z-index: 999;
	}


	.textRight {
		width: 100%;
		// text-align: right;\
		min-height: 20px;
		min-width: 100px;
		text-align: right;
		// display: flex;
		// justify-content: flex-end;
	}
</style>
