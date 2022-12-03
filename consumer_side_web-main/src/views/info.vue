<template>
  <div class="page">
    <van-nav-bar title="公示信息" left-text="" left-arrow @click-left="onClickLeft" :fixed="true" :placeholder="true"/>
    <van-tabs style="margin-bottom:200px;" sticky offset-top="12.26667vw">
      <van-tab title="经营信息">
        <div class="contain">
          <div class="titleImg">
            <span class="shopImg"><van-image
              lazy-load
               fit="cover"
              :src="require('../assets/shop.png')"
            /></span>
            <span>{{dataList.merchantName}}</span>
          </div>
          <van-cell-group>
            <van-field label="社会信用码" :value="dataList.socialCreditCode" readonly />
            <van-field label="法人负责人" :value="dataList.representName" readonly />
            <van-field label="联系电话" :value="dataList.linkedPhone" readonly />
            <van-field label="行业分类" :value="dataList.industryClassification" readonly />
            <van-field label="经营地址" name="businessAddress" readonly>
              <template #input>
                <div>{{dataList.businessAddress}}</div>
              </template>
            </van-field>
            <van-field label="所属监管所" :value="dataList.supervisionName" readonly />
            <!-- <van-field label="所属监管员" :value="dataList.supervisor" readonly /> -->
            <van-field label="营业执照" name="businessUrl" readonly>
              <template #input>
                <van-uploader v-model="dataList.businessUrl" readonly :deletable="false" :max-count="1" />
              </template>
            </van-field>
          </van-cell-group>
        </div>
      </van-tab>
      <van-tab title="许可证/备案证">
       
        <div v-if="xukezheng">
             <div class="contain" v-show="foodLicenseUrl[0].url">
              <van-field label="许可证" name="foodLicenseUrl" readonly style="padding-bottom: 20px">
                <template #input>
                  <van-uploader v-model="foodLicenseUrl" readonly :deletable="false" :max-count="1" />
                </template>
              </van-field>
            </div>
            <div class="contain" v-show="certificatesUrl[0].url">
              <van-field label="备案证" name="foodLicenseUrl" readonly style="padding-bottom: 20px">
                <template #input>
                  <van-uploader v-model="certificatesUrl" readonly :deletable="false" :max-count="1" />
                </template>
              </van-field>
            </div>
        </div>
        <div class="empty" v-else>
          <van-image
              lazy-load
               fit="cover"
              :src="require('../assets/empty.png')"
            />
          <div>暂无数据</div>
        </div>
      </van-tab>
      <van-tab title="健康证">
        <div class="contain" v-for="(item,index) in healthList" :key="index">
           <div class="user">
            <span class="userName">员工{{index+1}}：{{item.name}}</span>
            <span class="validTime">有效期至：{{item.validPeriod}}</span>
          </div>
          <van-cell-group>
            <van-field label="员工职位" :value="item.positionName" readonly />
            <van-field label="联系电话" :value="item.phone" readonly />
            <van-field label="身 份 证" :value="item.identityCard" readonly /> 
            <van-field label="取证日期" :value="item.receiveDate" readonly />
            <van-field label="健 康 证" readonly>
              <template #input>
                 <van-image
                :key="index"
                 @click="onChange(item.imageUrl)"
                  class="source_imge"
                  fit="cover"
                  lazy-load
                  :src="item.imageUrl"
                />
              </template>
            </van-field>
          </van-cell-group>
        </div>
        <div class="empty" v-show="healthList.length == 0">
          <van-image
               fit="cover"
              :src="require('../assets/empty.png')"
            />
          <div>暂无数据</div>
        </div>
      </van-tab>
      <van-tab title="从业证">
        <div class="contain" v-for="(item,index) in merchantList" :key="index">
          <div class="user">
            <span class="userName">员工{{index + 1}}：{{item.name}}</span>
            <span class="validTime">有效期至：{{item.validPeriod}}</span>
          </div>
          <van-cell-group>
            <!-- <van-field label="证照类型" value="从业资格证" readonly /> -->
            <van-field label="联系电话" :value="item.phone" readonly />
            <van-field label="身 份 证" :value="item.identityCard" readonly /> 
            <van-field label="取证日期" :value="item.receiveDate" readonly />
            <van-field label="从 业 证" readonly>
              <template #input>
                 <van-image
                :key="index"
                 @click="onChange(item.imageUrl)"
                  class="source_imge"
                  fit="cover"
                  lazy-load
                  :src="item.imageUrl"
                />
              </template>
            </van-field>
          </van-cell-group>
        </div>
        <div class="empty" v-show="merchantList.length == 0">
          <van-image
              lazy-load
               fit="cover"
              :src="require('../assets/empty.png')"
            />
          <div>暂无数据</div>
        </div>
      </van-tab>
    </van-tabs>

     <van-image-preview v-model="show" :images="images">
    </van-image-preview>


    <div class="goPay">
        <pay-page ref="pay"></pay-page>
    </div>
  </div>
</template>

<script>
import payPage from './payPage.vue'
import {businessInfo,foodLicenseInfo,certificateInfo,healthCertificate,certificates} from "@/api/app.js"
import { ImagePreview } from 'vant';

export default {
    components:{payPage,
    [ImagePreview.Component.name]: ImagePreview.Component,
    },
  data() {
    return {
      active:'经营信息',//默认经营信息
      xukezheng: false, //有无许可证
      // chongyezheng: false, //有无从业证
      healthy: false, //有无健康证
      businessUrl: [],//营业执照照骗
      merchantId:'',//商户id
      dataList:[],//经营信息列表
      foodLicenseUrl:[{url:('')}],//许可证照片
      uploader:[{url:('')}],
      merchantList:[],
      healthList:[],
      id:"",
      images:[],
      show:false,
      certificatesUrl:[{url:('')}]
    }
  },
  mounted() {
    this.id =localStorage.getItem('id')
     this.getBusiness();//经营信息
     this.getfoodLicense();//许可证
     this.gethealthCertificate(); //健康证
     this.getCertificateInfo(); //从业证
   },
  methods: {
    //   经营信息
    getBusiness(){
        let data={
            merchantId:this.id
        }
        businessInfo(data,this.id).then(res=>{
            let list=res.data
            if(res.status==200){
            this.dataList=list
            let phone=list.linkedPhone
            // 手机号脱敏
            if(phone!==''||phone!==null){
                let pat=/(\d{3})\d*(\d{4})/
                this.dataList.linkedPhone=phone.replace(pat,'$1****$2')
            }
                this.dataList.socialCode=list.socialCreditCode
                this.dataList.businessUrl=[{url:(list.businessUrl)}]
            }
        })
    },
    //许可证
    getfoodLicense(){
        let data={
           merchantId:this.id
        }
        foodLicenseInfo(data,this.id).then(res=>{
            let foodlist=res.data.foodLicenseUrl
            if(res.status==200){
              this.foodLicenseUrl=[{url:(foodlist)}]
              console.log(this.foodlist,"foodLicenseUrl")
              // this.xukezheng = true
              if(foodlist!==null && foodlist!=='' && foodlist != undefined){
                this.xukezheng=true  //有许可证  打开界面
              }
            }
        })
        certificates(data,this.id).then((res)=>{
            let foodlist=res.data.imagesUrl
            if(res.status==200){
              this.certificatesUrl=[{url:(foodlist)}]
              console.log(this.foodlist,"certificatesUrl")
              if(foodlist!==null&& foodlist!=='' && foodlist != undefined){
                this.xukezheng=true  //有许可证  打开界面
              }
            }
        })
    },
    // 健康证
    gethealthCertificate(){
       let data={
           merchantId:this.id
        }
      healthCertificate(data,this.id).then((res)=>{
        console.log(res)
        this.healthList = res.data
      })
    },
    // 从业证
    getCertificateInfo(){
      let data={
           merchantId:this.id
        }
      certificateInfo(data,this.id).then((res)=>{
        console.log(res)
        this.merchantList = res.data
      })
    },
    // 预览
    onChange(item){
      this.images = [item]
      this.show = true
    },
    //返回
   onClickLeft(){
    this.$router.go(-1)
    }
  },
}
</script>

<style scoped>
@import "./css/style.scss";
/* active字体颜色 */
/deep/ .van-tab--active {
  color: #345ae3;

}
/* 下划线颜色 */
/deep/ .van-tabs__line {
  background-color: #345ae3;
  z-index:0;
}

/deep/.van-uploader__preview-image {
    display: block;
    width: 107px;
    height: 76px;
    overflow: hidden;
}

/deep/.van-tabs {
    padding-bottom: 85px;
}

.contain {
  /* border:1px solid red; */
  margin-top: 10px;
  border-radius: 0 0 15px 15px;
  overflow: hidden;
}
.titleImg {
  height: 43px;
  background-image: url("../assets/info.png");
  background-repeat: no-repeat;
  background-size: 100% 43px;
  vertical-align: middle;
  line-height: 43px;
  padding-left: 16px;
  color: #345ae3;
  font-weight: bold;
}
.shopImg {
  display: inline-block;
  margin-right: 10px;
  width:18px;
  height: 16px;
  line-height: 16px;
 position: relative;
 top:2px;
}
/* 健康证 */
.user {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 10px;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  background: #eceffc;
}
.userName {
  display: inline-block;
  color: #345ae3;
  font-weight: bold;
}
.validTime {
  display: inline-block;
  color: #292929;
}
.source_imge{
   width: 107px;
    height: 76px;
}

</style>
