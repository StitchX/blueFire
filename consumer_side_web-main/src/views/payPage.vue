<template>
  <div v-if="isshow">
    <div class="pay">
      <van-button type="info" round size="large" class="payBtn" @click="showPay = true">去支付</van-button>
    </div>
    <van-action-sheet @closed="money=''"  v-model="showPay" :duration="0.1">
      <div class="box">
        <span class="fukuan">付款金额</span>
        <span v-show="isInsurance=='1'" class="foodHealth">✔食品安全险百万保障商家</span>
          <div class="codeImg" >
            <van-image
            v-if="healthCodeType == '1' ? true : healthCodeType == '2' ? true : healthCodeType == '3' ? true : false"
               fit="cover"
              :src="healthCodeUrl"
            />
          </div>
        <van-field @click="noBomBox" ref="refField" :class="healthCodeType == '1' ? 'input' : healthCodeType == '2' ? 'inputYellow' : healthCodeType == '3' ? 'inputRed' : 'input'" :readonly="false" clickable label="￥" type="number" input-align="left" :value="money" @touchstart.native.stop="showPay = true" />
        <van-number-keyboard class="keyBox" theme="custom" show :hide-on-click-outside="false"  extra-key="." @input="handleInput" @delete="handleDelete" @close="handleClose" close-button-text="付款" :transition="false"></van-number-keyboard>
        <div class="footIcon">
          <div class="bankInfo">收款服务方</div>
          <div class="bankIcon">
            <van-image
              style="width:100%"
               fit="cover"
              :src="bankLogoUrl"
              alt
            />
          </div>
          <div class="bankInfo">{{bankName}}</div>
        </div>
      </div>
    </van-action-sheet>
    <div id="father"></div>
  </div>
</template> 
<script>
import {consumerInfo,makeorder} from "@/api/app.js"
export default {
  data() {
    return {
      showPay: false, //打开支付键盘
      money: '', //金额
      maxlength: "", //金额最大长度
      isInsurance:'0',//是否加入食品安全险：0-未加入；1-已加入；空字符
      healthCodeUrl:'',//健康码url，不显示健康码时为空
      healthCodeType:'',//健康码类型：绿码-1；黄码-2；红码-3；0-不显示健康码,此时healthCodeUrl 为空
      bankLogoUrl:'',//银行logo
      bankName:'',//银行名称
      bankInfo:'',//银行信息
      isshow:false,
      userId:"",
      accessToken:"",
      userCertLevel:""
    }
  },
  watch: {
    value: function (v) {
      this.money = v + ''
    },

    money(val) {
      if (/^0[1-9]/.test(val)) {
        this.money = val.substring(1)
      }
    },
  },
  mounted(){
    // setTimeout(() => {
      this.getUserInfo();//获取首页
    // }, 1500);
    // this.isshow = true
    //如果是信用码的话不用支付
  },
  methods: {
     getUrlKey: function (name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20'))
    },
// 1、小数点不能再最前面；
// 2、保留小数点后两位；
// 3、不允许出现多于1位的小数点；
// 4、不允许前面出现两个0
    handleInput(key) {
      this.money = this.money + ''
      if (this.money == '' && key == '.') {
        this.money = '0'
      } else if (this.money.indexOf('.') !== -1 && key == '.') {
        return
      } else if (
        this.money.indexOf('0') !== -1 &&
        this.money.length == 1 &&
        key == 0
      ) {
        return
      } else if (/\.\d{2}$/.test(this.money)) {
        return
      } else if(/^([-+]?\d{10})(\.\d{2})?$/.test(this.money)){
        return
      }
      this.money += key
    },
    handleDelete() {
      this.money = this.money + ''
      if (!this.money) {
        return
      }
      this.money = this.money.substring(0, this.money.length - 1)
    },
    handleClose() {

     if(this.money == 0 || this.money == "0." || this.money == "0.0" || this.money == "0.00"){
       this.$dialog.alert({
          message: '请输入正确金额',
          confirmButtonText:"知道了",
          confirmButtonColor:"#292929"
        }).then(() => {
        });
     } else{
       this.showPay = false
       let authId = localStorage.getItem('authId')
       let id = localStorage.getItem('id')
       let terminalType = localStorage.getItem('terminalType')
       let openid =  localStorage.getItem('openid')
       let data = {
         businessId:id,
         authId:authId,
         orderType:terminalType,
         orderAmt:this.money,
         userId:this.userId,
         accessToken:this.accessToken,
         userCertLevel:this.userCertLevel,
        //  openId:"oj4qquAm9-EqXvdh2-p_irWXze7k"
        openId:openid
       }
       let toast1 = this.$toast.loading({
          message: '加载中...',
          forbidClick: true,
          duration:0
        });
        makeorder(data,id).then((res)=>{
          // 00成功
          if(res.status == "00"){
          // 	回调类型 1.直接返回支付URL 2.返回微信或支付宝参数 3.JS脚本
             let resCentet = res.data.resCentet
              // 如果服务器告诉你是
              if(res.data.resType == "1"){
                // 直接跳转银行
                window.location.href = resCentet
              }else if(res.data.resType == "2"){
                  // this.onBridgeReady(resCentet)
                  // 判断微信（1）还是支付宝（2） 
                if(terminalType == 1){
                  // 调起微信支付
                  this.onBridgeReady(resCentet)
                }
                if(terminalType == 2){
                  // 调起支付宝
                  window.location.href = resCentet
                }
              }else if(res.data.resType == "3"){
                // 银行
                const div = document.getElementById("father");
                div.innerHTML = resCentet
                document.body.appendChild(div)
                document.forms[0].submit()
              }
              // setTimeout(() => {
                  toast1.clear();
              // }, 1000);
          }else{
            toast1.clear();
            this.$toast.fail(res.message);
          }
        }).catch(err => {
              toast1.clear();
        })
      }
    },
    onBridgeReady(data){
      if (typeof WeixinJSBridge == "undefined") {
          if (document.addEventListener) {
              document.addEventListener('WeixinJSBridgeReady', vm.jsApiCall, false)
          } else if (document.attachEvent) {
              document.attachEvent('WeixinJSBridgeReady', vm.jsApiCall)
              document.attachEvent('onWeixinJSBridgeReady', vm.jsApiCall)
          }
      } else {
          var pay_obj = {
              appId: data.appId,
              timeStamp: data.timeStamp,
              nonceStr: data.nonceStr,
              package: data.package,
              signType: data.signType,
              paySign: data.paySign
          }
          let that=this
          WeixinJSBridge.invoke(
              'getBrandWCPayRequest',
              pay_obj,
              function(res){
                  if(res.err_msg == "get_brand_wcpay_request:ok" ){
                    // that.$toast.success('支付成功');
                  } else if(res.err_msg == "get_brand_wcpay_request:cancel" ){
                    // that.$toast({message: '您已取消支付',icon: 'cross'})
                  }else if(res.err_msg ==  "get_brand_wcpay_request:fail"){
                    // that.$toast({message: '支付失败',icon: 'cross'})
                  }else{
                    that.$toast(JSON.stringify(res));
                  }
          });
      }
    },
    // 获取首页信息
    getUserInfo(){
            let info=JSON.parse(localStorage.getItem('data'))
            console.log(info)
            this.bankLogoUrl=info.bankInfo.bankLogoUrl //银行logo
            this.bankName=info.bankInfo.bankName
            this.healthCodeType=info.healthInfo.healthCodeType
            let codeUrl=info.healthInfo.healthCodeUrl
            //设置绿马  红马  黄马icon
              // this.healthCodeUrl=codeUrl
               if(this.getUrlKey("shut")){
                  this.isshow = false
                }else{
                    if(codeUrl!==''||codeUrl!==null){
                      this.isshow= true
                      this.healthCodeUrl=codeUrl
                    }else{
                      this.isshow= false
                    }
                    if(this.bankName =="" || this.bankName == null){
                      this.isshow= false
                    }
                }
            this.isInsurance=info.merchantInfo.isInsurance
    },
    //禁止手机键盘的弹出
noBomBox(e) {
    document.activeElement.blur();
},
  },
}
</script>

<style scoped>
input{
        caret-color:green;  
      }
 .pay {
    /* padding: 15px 25px; */
    position: fixed;
    bottom: 0;
    width: 100vw;
    /* background: #ffffff; */
  }
  .payBtn {
    width: 80vw;
    left:50%;
    top:50%;
    transform:translate(-50%,-50%);
    background: #345ae3;
    border: 1px solid #345ae3;
  }
  .box {
    position: relative;
    z-index: 200;
    /* border: 1px solid #345AE3; */
    height: 400px;
  }
  .input {
    position: absolute;
    /* border:1px solid red; */
    z-index: 200;
    height: 140px;
    line-height: 140px;
    font-size: 50px;
    font-weight: 700;
  background: linear-gradient(186deg, #aeecba 1%, rgba(255, 255, 255, 0.13) 100%);
  }
  .inputRed {
    background: linear-gradient(
      186deg,
      rgba(228, 83, 76, 0.6) 10%,
      rgba(255, 255, 255, 0.13) 100%
    );
  }
  .inputYellow {
    background: linear-gradient(
      186deg,
      #ece29d 1%,
      rgba(255, 255, 255, 0.013) 100%
    );
  }
  .codeImg {
    width: 90px;
    height: 75px;
    position: absolute;
    top: 0;
    right: 0;
    z-index: 201;
    /* background-image: url("../assets/green.png"); */
    background-repeat: no-repeat;
    background-size: 100%;
  }
  .codeImgRed {
    /*// background-image: url("../assets/red.png"); */
    background-repeat: no-repeat;
  }
  .codeImgYellow {
    /*// background-image: url("../assets/yellow.png"); */
    background-repeat: no-repeat;
  }
  /deep/ .van-field__label {
    width: 20px;
    font-size: 40px;
  }
  /* 付款金额 */
  .fukuan {
    position: absolute;
    color: #292929;
    font-weight: bold;
    z-index: 202;
    padding: 15px 20px;
    font-size: 14px;
  }
  /* 食品安全险 */
  .foodHealth{
   position: absolute;
    z-index: 202;
    left:85px;
   top:17px;
    /* padding: 15px 80px; */
    color: #01951E;
    font-size: 12px;
  background: linear-gradient(270deg, rgba(88, 190, 108, 0.12) 0%, rgba(88, 190, 108, 0.35) 100%);
  }
  .keyBox {
    position: absolute;
    bottom:18px;
  }
  .footIcon {
    position: absolute;
    z-index: 200;
    bottom: 0px;
    left: 0;
    right: 0;
    display: flex;
    align-items: center;
    align-content: center;
    justify-content: center;
    background: rgb(242,243,245);
    /* align-items: center; */
  }
  .bankIcon {
    /* padding: 6px 20px; */
    width:100px;
    /* height: 20px; */
    padding:0px 10px;
    /* position: relative;
    top: -10px; */
  }
  .bankInfo {
    font-size: 13px;
    color: #595959;
  }
  
  /* 数字键盘 */

  /deep/ .van-number-keyboard{
    padding-bottom: 17px;
  }
  /deep/ .van-number-keyboard__title {
    display: inline-block;
    font-weight: 400;
    position: absolute;
    left: 20px;
    color: #292929;
    font-size: 42px;
    height: 48px;
    line-height: 58px;
  }
  /* 支付按钮 背景颜色 */
  /deep/ .van-key--blue {
    background-color: #58be6c;
  }
  /*  激活支付按钮 背景颜色 */
  /deep/ .van-key--blue.van-key--active {
    background-color: #82c28f;
  }
  /* 支付按钮 */

  /* 修改鼠标光标 */
  /deep/ .van-field__control {
    padding-left: 10px;
    font-size: 45px;
      /* color: #58BE6C;  */
  }
  /* 删除按钮 */
  /deep/ .van-key--delete {
      font-size: 6.26667vw;
      /* height: 48px; */
  }
  /deep/ .van-key--blue {
      height: 156px;
  }
  /deep/ .van-number-keyboard__sidebar .van-key__wrapper{
      position: relative;
      -webkit-box-flex: 0;
      -webkit-flex: 0;
      -ms-flex: 0;
      flex: 0 ;
      -webkit-flex-basis: 33%;
      -ms-flex-preferred-size: 33%;
      flex-basis: 25%;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      padding: 0 1.8vw 1.8vw 0;
  }
  /deep/ [data-v-29e51ead] .van-key--delete {
      font-size: 2.26667vw;
      height: 48px;
  }
  
  /deep/ .van-overlay {
      background-color: rgba(0, 0, 0, 0);
  }
  /deep/ .van-key {
      border-radius: 2.6px;
      font-weight: 600;
  }
  /* 弹框 */
 /deep/ .van-dialog{
    border-radius: 2.26667px;
  }
 /deep/ .van-dialog__message{
    font-size: 25px !important;
  }
</style>
