<template>
    <div>
       <div class="info_top">注：所有纳入监管商户请慎重填写，如有不清信息请核实清楚再进行填写。</div>
        <div class="info_img">
               <van-cell-group inset>
                  <div class="info_up">证照上传</div>
                    <van-field name="uploader" error required  label="营业执照">
                    <template #input >
                        <!-- <van-loading size="24px" v-model="loading" vertical>{{loadingText}}</van-loading> -->
                        <van-uploader v-model="value" :max-count="1" :after-read="afterRead" />
                    </template>
                    </van-field>
                    <div class="info_tag">注意：请上传最新或者长期有效的营业执照正本或副本</div>
               </van-cell-group>
        </div>
         <div class="info_data">
         <van-form @submit="onSubmit">
             <div class="info_up">基本信息</div>
            <van-field
                v-model="dataForm.dianpu_name"
                 required 
                :label-width="labelWidth"
                name="店铺/企业名称"
                label="店铺/企业名称"
                placeholder="请填写店铺/企业名称"
            />
            <van-field
                v-model="dataForm.name"
                 required 
                name="法人/负责人"
                :label-width="labelWidth"
                label="法人/负责人"
                placeholder="请填写法人/负责人"
            />
            <van-field
                v-model="dataForm.code"
                required
                name="社会信用代码"
                :label-width="labelWidth"
                label="社会信用代码"
                placeholder="请填写统一社会信用代码"
            />
             <van-field
                readonly
                clickable
                is-link
                :label-width="labelWidth"
                required
                name="picker"
                :value="dataForm.hangyeType"
                label="行业类别"
                placeholder="请选择行业类别"
                @click="showPickerCategory = true"
                />
                <van-popup v-model="showPickerCategory" position="bottom">
                <van-picker
                    value-key="title"
                    show-toolbar
                    :columns="columnsCategory"
                    @confirm="onConfirm"
                    @cancel="showPickerCategory = false"
                />
             </van-popup>
             <!-- 地区 -->
             <van-field
                readonly
                :label-width="labelWidth"
                :value="dataForm.diqu_id"
                label="经营地址"
                required
                name="city"
                placeholder="请选择省市区"
                >
                <template #input>
                   <van-row style="width:100%">
                    <van-col span="8" @click="showProvince = true">{{region.province}}</van-col>
                        <van-popup v-model="showProvince" position="bottom">
                            <van-picker
                                value-key="name"
                                show-toolbar
                                :columns="listProvince"
                                @confirm="onProvince"
                                @cancel="showProvince = false"
                            />
                        </van-popup>
                    <van-col span="8" @click="showCity = true">{{region.city}}</van-col>
                        <van-popup v-model="showCity" position="bottom">
                                <van-picker
                                    value-key="name"
                                    show-toolbar
                                    columns-num
                                    :columns="listCity"
                                    @confirm="onCity"
                                    @cancel="showCity = false"
                                />
                            </van-popup>
                    <van-col span="8" @click="showArea = true">{{region.area}}</van-col>
                          <van-popup v-model="showArea" position="bottom">
                                <van-picker
                                    value-key="name"
                                    show-toolbar
                                    :columns="listArea"
                                    @confirm="onArea"
                                    @cancel="showArea = false"
                                />
                            </van-popup>
                   </van-row>
                </template>
             </van-field>
                <van-field
                v-model="dataForm.address"
                :label-width="labelWidth"
                rows="2"
                autosize
                required
                label="详细地址"
                type="textarea"
                placeholder="请填写详细地址"
                />
                <van-field
                v-model="dataForm.zongheti"
                :label-width="labelWidth"
                label="综合体(所在商圈，例如：世豪广场)："
                placeholder="输入"
            />
             <van-field
                v-model="dataForm.shequxinxi"
                :label-width="labelWidth"
                label="社区信息"
                placeholder="输入"
            />
            <van-field
                readonly
                clickable
                is-link
                :label-width="labelWidth"
                name="picker"
                required
                :value="dataForm.jianguansuoId"
                label="所属监管所"
                placeholder="请选择店铺所属监管所"
                @click="showgov = true"
                />
                <van-popup v-model="showgov" position="bottom">
                <van-picker
                    value-key="name"
                    show-toolbar
                    :columns="listGov"
                    @confirm="onGov"
                    @cancel="showgov = false"
                />
             </van-popup>
                <van-field
                readonly
                clickable
                is-link
                :label-width="labelWidth"
                name="picker"
                required
                :value="dataForm.jiedaoId"
                label="所属街道"
                placeholder="请选择所属街道"
                @click="showStreet = true"
                />
                <van-popup v-model="showStreet" position="bottom">
                <van-picker
                    value-key="name"
                    show-toolbar
                    :columns="listStreet"
                    @confirm="onStreet"
                    @cancel="showStreet = false"
                />
             </van-popup>
                     <van-field
                readonly
                clickable
                is-link
                :label-width="labelWidth"
                name="picker"
                :value="dataForm.marketId"
                label="所属市场"
                placeholder="请选择所属市场"
                @click="showMarket = true"
                />
                <van-popup v-model="showMarket" position="bottom">
                <van-picker
                    value-key="address"
                    show-toolbar
                    :columns="listMarket"
                    @confirm="onMarket"
                    @cancel="showMarket = false"
                />
             </van-popup>
                     <van-field
                readonly
                clickable
                is-link
                :label-width="labelWidth"
                name="picker"
                required
                :value="dataForm.jianguanyuan"
                label="我的监管员"
                placeholder="请选择我的监管员"
                @click="showAdmin = true"
                />
                <van-popup v-model="showAdmin" position="bottom">
                <van-picker
                    value-key="name"
                    show-toolbar
                    :columns="listAdmin"
                    @confirm="onAdmin"
                    @cancel="showAdmin = false"
                />
             </van-popup>
              <van-field
                v-model="dataForm.BD"
                :label-width="labelWidth"
                required
                label="维护BD"
                placeholder="请填写"
            />
             <van-field
                v-model="dataForm.BDM"
                :label-width="labelWidth"
                required
                label="BDM"
                placeholder="请填写"
            />
             <van-field
                v-model="dataForm.phone"
                required
                type="tel" 
                 maxlength="11"
                :label-width="labelWidth"
                label="手机号"
                placeholder="请填写"
            />
            <div style="padding: 16px 16px 30px 16px;">
                <van-button round block type="info" native-type="submit" :class="isChecked?'':''">提交</van-button>
            </div>
            </van-form>
        </div>
    </div>
</template>

<script>
// import { areaList } from '@vant/area-data';
import Exif from 'exif-js'

export default {
    data(){
        return{
            value:[],
            labelWidth:"7.5em",
            // columns: [
            //   {value:1,lable:"药品类"},
            //   {value:2,lable:"餐饮业态分类"},
            //   {value:3,lable:"服务行业"},
            //   {value:4,lable:"教育培训"},
            // ],
            columnsCategory:[],
            showPickerCategory: false,  //行业类别
            showPicker:false,
            showArea: false,
            areaList:[],
            dataForm:{
                code:"",   //社会信用代码
                dizhi:"",   //详细地址
                faren:"",   //法人/负责人
                name:"",    //企业名称
            },
            region :{
                city:"请选择市",     //市级
                province:"请选择省",  //省级
                area:"请选择区"     //区
            },
            showProvince:false,    //省级弹框
            listProvince:[],     //省级弹框数据
            showCity:false,    //市级弹框
            listCity:[],     //市级弹框数据
            showArea:false,    //区级弹框
            listArea:[],     //区级弹框数据
            showgov:false,   //所属监管所下拉
            listGov:[],        //所属监管所下拉数据
            showStreet:false,  //所属街道
            listStreet:[],      //街道数据
            showMarket:false,   //市场下拉
            listMarket:[],       //市场下拉数据
            showAdmin:false,   //监管员下拉
            listAdmin:[],       //监管员下拉数据
            files: {   //压缩图片
                name: "",
                type: ""
            },
            dataImg:"",
            loading:false,
            loadingText:""
        }
    },
    mounted(){
        // 行业类别
        this.onIndustryCategory()
        // 经营地址
        this.postPlace()
        this.dataForm.id=this.$route.query.id
        console.log("ck")
    },
    methods: {
        //   行业
        onConfirm(value) {
            this.dataForm.hangye_type = value.id;
            this.dataForm.hangyeType = value.title;
            console.log(value)
            this.showPickerCategory = false;
        },
        // 行业下拉数据
        onIndustryCategory(){
            this.$api.postSort().then((res)=>{
                this.columnsCategory = res.data.data
                console.log(this.columnsCategory)
            })
        },
        // 经营地址API
        postPlace(){
            this.$api.postPlace().then((res)=>{
                let data = res.data.data.diqu
                this.listProvince = data
                console.log(data)
            })
        },
        // 省级确认下拉数据
        onProvince(values) {
            this.showProvince = false;
            // 文本框赋值
            this.region.province = values.name
            this.region.city = "请选择市"
            this.region.area = "请选择区"
            // 遍历删除区级，然后把res.children变成字符串，市级点击后解除
            values.children_one.map((res)=>{
                res.children_one= JSON.stringify(res.children)
                delete res.children
            })
            // 赋值市下拉数据
            this.listCity = values.children_one
            console.log(values)
        },
        // 市级下拉数据
        onCity(values) {
            this.showCity = false;
            this.region.city = values.name;
            this.region.area = "请选择区";
            // 解除字符串变回数组
            values.value = JSON.parse(values.children_one)
            this.listArea = values.value
            console.log(values)
        },
        // 区级下拉数据
        onArea(values) {
            this.showArea = false;
            // input显示
            this.region.area = values.name
            // 赋值到表单里面
            this.dataForm.diqu_id = values.id
            console.log(values)
            this.govGetList(values.id)
            // 监管所
            this.dataForm.jianguansuo_id = ""
            this.dataForm.jianguansuoId = ""
        },
        // 所属监管所API
        govGetList(id){
            let govId = {
                diqu_id : id
            }
            // 获取监管所API
            this.$api.postGov(govId).then((res)=>{
                this.listGov = res.data.data
            })
        },
        // 所属监管所方法
        onGov(values){
            console.log(values)
            this.dataForm.jianguansuo_id = values.id
            this.dataForm.jianguansuoId = values.name
            // 监管所
             this.dataForm.jiedao_id = ""
             this.dataForm.jiedaoId = ""
            //  市场
             this.dataForm.market_id = ""
            this.dataForm.marketId = ""
            // 监管员
            this.dataForm.jianguanyuan = ""
            this.dataForm.jianguanyuan_id = ""
            this.streetListGet(values.id)
            this.showgov = false
            console.log("dianji")
        },
        // 街道接口
        streetListGet(id){
            let streetId = {
                jianguansuo_id : id
            }
            this.$api.postStreet(streetId).then((res)=>{
                this.listStreet=res.data.data.shichang
            })
        },
        // 街道数据
        onStreet(values){
            console.log(values)
            this.dataForm.jiedao_id = values.id
            // 
            this.listAdmin = values.jianguanyuan
            this.dataForm.jiedaoId = values.name
            // 监管员
            this.marketGetList(values.id)
            this.showStreet = false
        },
        marketGetList(id){
            let data = {
                market_id : id
            }
            this.$api.postMarket(data).then((res)=>{
                this.listMarket = res.data.data
                console.log(res)
            })
        },
        onMarket(values){
            console.log(values)
            this.dataForm.market_id = values.id
            this.dataForm.marketId = values.address
            this.showMarket = false
        },
        // 监管员下拉确定
        onAdmin(values){
            this.dataForm.jianguanyuan = values.name
            this.dataForm.jianguanyuan_id = values.id
            this.showAdmin = false
            console.log(values)
        },
        // 组件方法 获取 流
        afterRead(file) {
        this.files.name = file.file.name // 获取文件名
        this.files.type = file.file.type // 获取类型
        this.imgPreview(file.file)
        },
        // 处理图片
        imgPreview(file) {
        let self = this
        let Orientation
        //去获取拍照时的信息，解决拍出来的照片旋转问题   npm install exif-js --save   这里需要安装一下包
        Exif.getData(file, function() {
            Orientation = Exif.getTag(this, 'Orientation')
        })
        // 看支持不支持FileReader
        if (!file || !window.FileReader) return
        if (/^image/.test(file.type)) {
            // 创建一个reader
            let reader = new FileReader()
            // 将图片2将转成 base64 格式
            reader.readAsDataURL(file)
            // 读取成功后的回调
            reader.onloadend = function() {
            let result = this.result
            let img = new Image()
            img.src = result
            //判断图片是否大于500K,是就直接上传，反之压缩图片
            if (this.result.length <= 500 * 1024) {
                // 上传图片
                self.postImg(this.result);
            } else {
                img.onload = function() {
                let data = self.compress(img, Orientation)
                // 上传图片
                self.postImg(data);
                }
            }
            }
        }
        },
        // 压缩图片
        compress(img, Orientation) {
        let canvas = document.createElement('canvas')
        let ctx = canvas.getContext('2d')
        //瓦片canvas
        let tCanvas = document.createElement('canvas')
        let tctx = tCanvas.getContext('2d')
        // let initSize = img.src.length;
        let width = img.width
        let height = img.height
        //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
        let ratio
        if ((ratio = (width * height) / 4000000) > 1) {
            console.log("大于400万像素");
            ratio = Math.sqrt(ratio)
            width /= ratio
            height /= ratio
        } else {
            ratio = 1
        }
        canvas.width = width
        canvas.height = height
        //    铺底色
        ctx.fillStyle = '#fff'
        ctx.fillRect(0, 0, canvas.width, canvas.height)
        //如果图片像素大于100万则使用瓦片绘制
        let count
        if ((count = (width * height) / 1000000) > 1) {
            // console.log("超过100W像素");
            count = ~~(Math.sqrt(count) + 1) //计算要分成多少块瓦片
            //      计算每块瓦片的宽和高
            let nw = ~~(width / count)
            let nh = ~~(height / count)
            tCanvas.width = nw
            tCanvas.height = nh
            for (let i = 0; i < count; i++) {
            for (let j = 0; j < count; j++) {
                tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh)
                ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh)
            }
            }
        } else {
            ctx.drawImage(img, 0, 0, width, height)
        }
        //修复ios上传图片的时候 被旋转的问题
        if (Orientation != '' && Orientation != 1) {
            switch (Orientation) {
            case 6: //需要顺时针（向左）90度旋转
                this.rotateImg(img, 'left', canvas)
                break
            case 8: //需要逆时针（向右）90度旋转
                this.rotateImg(img, 'right', canvas)
                break
            case 3: //需要180度旋转
                this.rotateImg(img, 'right', canvas) //转两次
                this.rotateImg(img, 'right', canvas)
                break
            }
        }
        //进行最小压缩
        let ndata = canvas.toDataURL('image/jpeg', 0.8)
        tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
        console.log(ndata)
        return ndata
        },
        // 旋转图片
        rotateImg(img, direction, canvas) {
        //最小与最大旋转方向，图片旋转4次后回到原方向
        const min_step = 0
        const max_step = 3
        if (img == null) return
        //img的高度和宽度不能在img元素隐藏后获取，否则会出错
        let height = img.height
        let width = img.width
        let step = 2
        if (step == null) {
            step = min_step
        }
        if (direction == 'right') {
            step++
            //旋转到原位置，即超过最大值
            step > max_step && (step = min_step)
        } else {
            step--
            step < min_step && (step = max_step)
        }
        //旋转角度以弧度值为参数
        let degree = (step * 90 * Math.PI) / 180
        let ctx = canvas.getContext('2d')
        switch (step) {
            case 0:
            canvas.width = width
            canvas.height = height
            ctx.drawImage(img, 0, 0)
            break
            case 1:
            canvas.width = height
            canvas.height = width
            ctx.rotate(degree)
            ctx.drawImage(img, 0, -height)
            break
            case 2:
            canvas.width = width
            canvas.height = height
            ctx.rotate(degree)
            ctx.drawImage(img, -width, -height)
            break
            case 3:
            canvas.width = height
            canvas.height = width
            ctx.rotate(degree)
            ctx.drawImage(img, -width, 0)
            break
        }
        },
        //将base64转换为文件
        dataURLtoFile(dataurl) {
            var arr = dataurl.split(','),
                bstr = atob(arr[1]),
                n = bstr.length,
                u8arr = new Uint8Array(n)
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n)
            }
            return new File([u8arr], this.files.name, {
                type: this.files.type
            })
        },
        // 提交图片到后端
        postImg(base64) {
            let file = this.dataURLtoFile(base64)
            console.log(file)
        let formData = new window.FormData()
        formData.append('api', file)
        console.log(formData)
        const toast = this.$toast.loading({
            duration:0,
            forbidClick:true,
            message:"上传图片中..."
        })
        // 上传图片
                this.$api.upImage(formData).then((res)=>{
                console.log(res.data)
                let dataImg = res.data.data
                // 赋值在表单里面
                this.dataForm.yingye_zhizhao_new = dataImg
                // 清除加载
                toast.clear()
                let pic = {
                  pic: dataImg
                }
                 const uptoast = this.$toast.loading({
                    duration:0,
                    forbidClick:true,
                    message:"识别图片中..."
                })
            //  图片识别文字
                    this.$api.upPic(pic).then((res)=>{
                        let data = res.data.data.data
                            // 社会信用代码
                            this.dataForm.code = data.code
                            // 详细地址
                            this.dataForm.address = data.dizhi
                            // 负责人/法人
                            this.dataForm.name = data.faren
                            // 企业名称
                            this.dataForm.dianpu_name = data.name
                            // 关闭识别图片加载
                            uptoast.clear()
                            console.log(data)
                        })
            })
        },

        onSubmit(){
            console.log(this.yingye_zhizhao_new)
            console.log(this.dataForm)
            // 如果market_id没有的话就默认值
  
            if(this.dataForm.yingye_zhizhao_new == "" || this.dataForm.yingye_zhizhao_new == null){
                console.log("为空")
                this.$toast.fail('请上传营业执照');
            }else if(this.region.area == "" || this.region.area == "请选择区"){
                this.$toast.fail('请选择经营地址');
            }else if(this.dataForm.jianguansuoId == "" || this.dataForm.jianguansuoId == null){
                this.$toast.fail('请选择监管所');
            }else if(this.dataForm.jiedaoId == "" || this.dataForm.jiedaoId == null){
                this.$toast.fail('请选择街道');
            }else if(this.dataForm.jianguanyuan == "" || this.dataForm.jianguanyuan == null){
                this.$toast.fail('请选择监管员');
            }else if(this.dataForm.BD == "" || this.dataForm.BD == null){
                this.$toast.fail('请输入维护BD');
            }else if(this.dataForm.BDM == "" || this.dataForm.BDM == null){
                this.$toast.fail('请输入BDM');
            }else if(this.dataForm.phone == "" || this.dataForm.phone == null){
                this.$toast.fail('请填写手机号');
            }else if(this.dataForm.market_id == "" || this.dataForm.market_id == null){
                this.dataForm.market_id = this.dataForm.jiedao_id
            }else{
                const upDataForm = this.$toast.loading({
                    duration:0,
                    forbidClick:true,
                    message:"提交中..."
                })
                this.$api.upDataForm(this.dataForm).then((res)=>{
                   upDataForm.clear()
                   if(res.data.code == 200){
                       this.$toast.success('提交成功');
                       window.location.href = "https://credit.supervision.bluefire.top/danye/index/index1.pythasp?id="+ this.dataForm.id
                    //    this.$route
                    //    this.dataForm = {}
                    //    图片地址
                       this.value = []
                    //    经营地址
                     this.region ={
                        city:"请选择市",     //市级
                        province:"请选择省",  //省级
                        area:"请选择区"     //区
                    }
                   }else{
                       this.$toast.fail(res.data.msg);
                   }
                }).catch(function (error) {
                    upDataForm.clear()
                    this.$toast.fail("上传失败");
                })
            }
        },
    },
}
</script>

<style scoped>
    .info_top{
        /* width: 100%; */
        color:rgb(3, 104, 235);
        font-size:14px;
        padding: 10px;
        background: #ffffff;
    }
    .info_up{
        padding: 10px 16px;
        font-weight: 500;
        font-size: 17px;
    }
    .info_img{
        margin: 10px 0px 10px 0px;
        background: #ffffff;
    }
    .info_data{
        background: #ffffff;
    }
    .info_tag{
        font-size: 12px;
        padding: 10px 16px;
        color: #a7a7a7;
    }
</style>