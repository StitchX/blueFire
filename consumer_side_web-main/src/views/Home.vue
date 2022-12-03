<template>
  <div class="home">
    <div v-title :data-title="title" v-if="title != ''"></div>
    <van-swipe :autoplay="3000" style="minHeight:120px" >
      <van-swipe-item v-for="(image, index) in dataList.shufflingUrls" :key="index">
        <van-image :src="image" style="width:100%;height100%" alt/>
      </van-swipe-item>
    </van-swipe>
    <div class="bg_position">
        <div class="name_bg">
           <div>
             <van-image
              class="image"
              :lazy-load="true"
               fit="cover"
               style="marginRight:5px"
              :src="require('./../assets/store.png')"
              alt
            />
             <span class="store_name">{{dataList.merchantInfo.merchantName}}</span>
           </div>
             <div class="yellow hidden" v-if="dataList.merchantInfo.isInsurance =='0'">
              <van-icon name="warn-o"  />未加入食品安全险百万保障计划
            </div>
             <div class="green hidden" v-if="dataList.merchantInfo.isInsurance =='1'">
              <van-icon name="success" />已加入食品安全险百万保障计划
            </div>
            <div class="hidden">
             <span style="color:#8C8C8C">社会信用代码：</span> {{dataList.merchantInfo.socialCreditCode}}
            </div>
            <div class="hidden">
             <span style="color:#8C8C8C">法人/负责人：</span> {{dataList.merchantInfo.representName}}
            </div>
            <div class="hidden ">
             <span style="color:#8C8C8C">监管所：</span> {{dataList.merchantInfo.supervisionName}}
             <span @click="onclick" class="detil" >详情 ></span>
            </div>
        </div>
        <div class="na_padding">
          <div class="padding_left" :style="bg" @click="onclick">
             <div class="merchat">更了解商家</div>
             <div class="merchat_text">安全监管·放心交易</div>
             <van-image
              class="bg1"
              :lazy-load="true"
               fit="cover"
               style="marginRight:5px"
              :src="require('./../assets/bg1.png')"
            />
            <div class="merchat_info" >
              <div class="merchat_center">
                查看公示信息
                <van-icon name="play" />
              </div>
            </div>
          </div>
          <div class="padding_right"  >
            <div class="padding_flex" @click="onComplaint" :style="complaint">
             <div class="complaintStyle">
                去投诉
                <van-icon name="play" class="vantIcon" />
             </div>
                <div>
                <div class="merchat_text">提供更好的服务</div>
                </div>
            </div>
            <div class="padding_flex1 flex_row" v-if="dataList.isShowKitchen">
                <div class="blox_flex" :style="info" @click="onDataInfo" >
                  <div>
                    <van-image
                      class="iconInfoImg"
                      :lazy-load="true"
                      fit="cover"
                      :src="require('./../assets/iconInfo.png')"
                    />
                  </div>
                    <div class="info_text" >
                      溯源信息
                      <van-icon name="play" />
                    </div>
                </div>
                <div class="blox_flex" :style="kitchen" @click="onKitchen">
                  <div>
                    <van-image
                      class="iconInfoImg"
                      :lazy-load="true"
                      fit="cover"
                      :src="require('./../assets/kitchenImg.png')"
                    />
                  </div>
                    <div class="kitchen_text" >
                      明厨亮灶
                      <van-icon name="play" />
                    </div>
                </div>
            </div>
            <div class="padding_flex1 flex_row" v-else>
                <div class="goInfo" @click="onDataInfo"  >
                <div class="info_B">
                    <div>
                      <div class="complaintStyle info_C">
                          溯源信息
                        <van-icon name="play" class="vantIcon" />
                      </div>
                        <div class="merchat_text info_D">提升食品安全管理</div>
                      </div>
                    <van-image
                        class="infoImg_B"
                        :lazy-load="true"
                        fit="cover"
                        :src="require('./../assets/iconInfo.png')"
                      />
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
    <pay-page v-if="payshow" ref="pay"></pay-page>
  </div>
</template>

<script>

import { list,consumerId,makeCount } from "@/api/home";
import backGroundImg from '@/assets/bg.png';
import complaintImg from '@/assets/complaint.png';
import infoImg from '@/assets/dataInfo.png';
import kitchenImg from '@/assets/kitchen.png';
import payPage from './payPage'

export default {
  name: "Home",
  components:{payPage},
  data() {
    return {
      text:"",
      authId:"",
      id:"",
      terminalType:'',
      dataList:{
        merchantInfo:{},
        shufflingUrls:[
        ]
      },
      images: [
      ],
      bg:{
        background:`url(${backGroundImg})`,
        backgroundSize:'100% 100%'
      },
      complaint:{
        background:`url(${complaintImg})`,
        backgroundSize:'100% 100%'
      },
      info:{
        background:`url(${infoImg})`,
        backgroundSize:'100% 100%'
      },
      kitchen:{
        background:`url(${kitchenImg})`,
        backgroundSize:'100% 100%'
      },
      title:"",
      payshow:false
    };
  },
  created(){
        // 判断url上面的商户id
      if(this.getUrlKey("id")){
          let code = parseInt(this.getUrlKey("id"))
          localStorage.setItem('id', code)
      }
      if(this.getUrlKey("openid")){
          localStorage.setItem('openid',this.getUrlKey("openid"))
      }
    // 判断微信还是支付宝授权
    if (/MicroMessenger/.test(window.navigator.userAgent)) {
        this.getWX()
      } else if (/AlipayClient/.test(window.navigator.userAgent)) {
        this.getAlipay()
      } else {
            this.id = localStorage.getItem('id');
            this.terminalType = 3
            let authId = localStorage.getItem('authId')
            if(authId == null || authId == ""){
              consumerId(this.id).then((res)=>{
                let data = res.data
                localStorage.setItem('authId',data)
                this.authId = data
                this.onList()
              })
            }else{
              this.authId = localStorage.getItem('authId')
               this.onList()
            }
      }
  },
  mounted() {
       let data={
        authId: this.authId,
        merchantId: this.id,
        terminalType: this.terminalType
      }
      let that = this
         window.onpageshow = null;
         window.onpageshow = function(event){
          makeCount(data,that.id).then((res)=>{
            window.name = "扫码首次进入"
          })
         }
  },
  methods: {
    getAlipay(){
        let authId = localStorage.getItem('authId');
        this.terminalType = 2
              this.id = localStorage.getItem('id');
        if(authId == null || authId == ""){
          consumerId(this.id).then((res)=>{
              let data = res.data
              localStorage.setItem('authId',data)
              this.authId =  data
              this.onList()
            })
        }else{
           this.authId = localStorage.getItem('authId')
            // 调用首页数据
            this.onList()
        }
    },
     getWX(){
       let authId = localStorage.getItem('authId');
        this.terminalType = 1
        this.id = localStorage.getItem('id');
        if(authId == null || authId == ""){
            consumerId(this.id).then((res)=>{
              let data = res.data
              localStorage.setItem('authId',data)
              this.authId = data
              this.onList()
            })
        }else{
           this.authId =  localStorage.getItem('authId')
            // 调用首页数据
            this.onList()
        }
    },
    // 截取url参数
    getUrlKey: function (name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20'))
    },
    // 首页数据
    onList(){
      // 保存type微信或者支付宝
      localStorage.setItem('terminalType', this.terminalType)
      let data={
        authId: this.authId,
        merchantId: this.id,
        terminalType: this.terminalType
      }
      list(data,this.id).then((res)=>{
        if(res.status == 200){
          this.dataList = res.data
          let data = res.data
          if(data.bankInfo.bankName == "天府银行"){
            if(this.terminalType == 1){
              this.text = localStorage.getItem('openid')
              if(localStorage.getItem('openid') == null || localStorage.getItem('openid') == ""){
                let appId = 'wxd1f74c65b47c2cbd'
                // let url ='http%3A%2F%2Fmyptgq.natappfree.cc'
                let url = 'https%3A%2F%2Fcredit.supervision.bluefire.top'
                // let url = 'https%3A%2F%2Fcredit.supervision.bluefire.top%3A555'
                let id = localStorage.getItem('id');
                window.location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${url}%2Fconsumer%2Fcallback%2Fwechat&response_type=code&scope=snsapi_base&state=${id}#wechat_redirect`
              }
            }
          }
          localStorage.setItem('name',data.merchantInfo.merchantName)
          this.title = data.merchantInfo.merchantName
          localStorage.setItem('data',JSON.stringify(data))
          // 支付按钮打开
          this.payshow = true
          setTimeout(() => {
           this.$refs.pay.showPay = true
          //  判断url上面是否有 accessToken 和userId
           if(this.getUrlKey("accessToken")){
              this.$refs.pay.accessToken = this.getUrlKey("accessToken")
           }
           if(this.getUrlKey("userId")){
              this.$refs.pay.userId = this.getUrlKey("userId")
           }
           if(this.getUrlKey("userCertLevel")){
             this.$refs.pay.userCertLevel = this.getUrlKey("userCertLevel")
           }
          }, 100);
        }else{
          this.$toast.fail(res.message);
        }
      })
    },
    onclear(){
      localStorage.clear();
    },
    onclick(){
      this.$router.push({path:'/info'});
    },
    // 投诉
    onComplaint(){
      this.$router.push({path:'/complaint'});

    },
    // 朔源信息
    onDataInfo(){
      this.$router.push({path:"/source"})
    },
    // 明厨亮灶
    onKitchen(){
      this.$router.push({path:"/mc"})
    }
  },
};
</script>

<style  scoped>
  @import "./css/home.scss";
  .goInfo{
    padding: 5px;
    width: 100%;
  
  }
  .info_B{
    width: 100%;
    border-radius: 10px;
    background-image: linear-gradient(to right, #FDFFFF , #F5FDF1);
    box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.055);
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    padding: 5PX 0 14PX 0;
}
  .info_C{
    color: #67AD59 !important;
    margin-right: 6px;
    margin-left: 15px !important;
  }
  .info_D{
    margin-left: 15px !important;
  }
  .infoImg_B{
    width: 55px;
    height: 37px;
    margin-left: 16px;
    margin-top: 16px;
  }
  .vantIcon{
    position: relative;
    top: 2px;
  }
</style>
