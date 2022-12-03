<template>
  <div class="hello">
    <!-- 标题头 -->
    <!-- <van-nav-bar title="话费充值"   @click-left="onClickLeft" :fixed="true" :border="false" /> -->
    <!-- right-icon="friends-o" -->
  <a :href="'tel:' + 18982071814">call me</a>
        <van-field
            class="cad"
            v-model="phone"
            type="digit"
            @touchstart.native.stop="show1 = true"
            placeholder="请输入金额"
            clearable
            @blur="iptblur"
            @click="infolist"
            @keyup="throttle"
            :border="false"
            maxlength="11"
            ref="input"
          />
         <van-number-keyboard
          :show="show1"
          theme="custom"
          extra-key="."
          close-button-text="完成"
          @blur="show1 = false"
          @input="onInput"
        />
    <!-- 确认按钮 -->
    <van-button class="butn" @click="alertMenu">立即充值</van-button>

  </div>
</template>


<script>

export default {
  name: "homepage",
  data() {
    return {
      phone: "",          //手机
      state:false,
      statu:true,
      facePrice:0,     //面额
      payPrice:0,      //售后价
      btnList: [],
      show: false,    //控制上拉框
      onshow:false,  //弹框
      show1:true,
      onfo:false,
      font:-1,     //控制点击红色
      id:{
        clickId:"",       //获取的值
      },
      styleColor:"",
      discounts:false,       //折扣的弹框
      inexpensive:false,       //优惠券得弹框
      worth:true,             //使用优惠券的卷券
      info:[],       //折扣数据
      dataList:[],    
      list:[],
      work:[],    //优惠券
      worklist:[],//花费充值中的优惠券
      isOriginHei: true,
      screenHeight: document.documentElement.clientHeight,        //此处也可能是其他获取方法
      originHeight: document.documentElement.clientHeight,
      userId:"",
      minFacePrice:"",
      mockUserId:"",
      mockid:"",
      states:'',
      overlay:false,
      envyoumi:process.env.ENV_CONFIG,
       //url:"https%3a%2f%2fwxpay.zhouyujunlin.com%2fmeitu%2f%23%2fresult",      //美图 和 挖财  记得da版本号
        // url: "https%3a%2f%2fwxpay.zhouyujunlin.com%2f%23%2fresult"
     // url:"https%3a%2f%2fwxpay.zhouyujunlin.com%2fshansong%2f%23%2fresult",      //美图 和 挖财  记得da版本号
    };
  },
  created(){
      // console.log("美图")
      // console.log("挖财")
      // console.log("闪送")
      // this.$router.replace({path:"/",query:{"td_channelid":"meitu"}})
      window.addEventListener('pageshow', function(e) {
          //如果检测到页面是从“往返缓存”中读取的，刷新页面
          if (e.persisted) {
              window.location.reload();
          }
      });
     _MEIQIA('init');
     _MEIQIA('hidePanel');
     console.log(process.env.PACK_PATH)
    if (process.env.ENV_CONFIG == 'wacai') {
      this.wacai()  
    } else if (process.env.ENV_CONFIG == 'meitu'){
      this.meitu()
    }else if (process.env.ENV_CONFIG == 'shansong'){
      this.shansong()
    }else if(process.env.ENV_CONFIG == 'zhongtong'){
      this.zhongtong()
    }else if(process.env.ENV_CONFIG == 'youmi'){
      this.youmi()
    }else if(process.env.ENV_CONFIG == 'dida'){
      this.dida()
    }
         　          //美图 和 挖财  记得打版本号
// console.log(process.env.INDEX)

     if(window.name == ""){
        TDAPP.onEvent("从App进入主页");
        window.name="前端牛逼"
      }
  },
  watch:{
    screenHeight (val) {
        if(this.originHeight > val + 100) {        //加100为了兼容华为的返回键
            this.isOriginHei = false;
        }else{
            this.isOriginHei = true;
        }
    }
  },
  mounted() {
             _MEIQIA('init');
    
    let self = this;
    window.onresize = function() {
        return (function(){
            self.screenHeight = document.documentElement.clientHeight;
        })()
    }
  },
  methods: {  
    onInput(){

    },
    //闪送
    uuId(){
      let s = [];
        let hexDigits = "0123456789abcdef";
        for (let i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";
      
        let clickid = s.join("")+new Date().getSeconds()+new Date().getSeconds()+new Date().getMonth() ;
        return clickid
    },
    //中通
      zhongtong(){
        this.states=this.$route.query.coupon
        let clickid=this.uuId();
       // let clickid=uuid;
        console.log(clickid)
        let localMockUserId = localStorage.getItem('userid');
        if( localMockUserId == null || localMockUserId == "" ) {
          let s = [];
          let hexDigits = "0123456789abcdef";
          for (let i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
          }
          s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
          s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
          s[8] = s[13] = s[18] = s[23] = "-";
        
          let userId = s.join("")+new Date().getMonth()+new Date().getSeconds()+new Date().getSeconds();
            let newMockUserId=userId;
            localStorage.setItem("userid", newMockUserId);
            this.mockUserId = newMockUserId;
        } else {
          this.mockUserId =  localMockUserId;
        }
          //  clickid 接口请求
        this.$api.clickId(
            clickid,
            this.mockUserId,
            {
              appId:"zhongtong",
              partnerId:"zhongtong"
            }
          ).then((res)=>{
            if(res.data.code==200){
              let id=res.data.result;  // 把获取的参数传入到id里
              this.id=id
              console.log(this.id)
              localStorage.setItem('id', JSON.stringify(id)); 
              // this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
              this.onmoey()   //获取用户可领的优惠卷
            }else{
              this.$toast(res.data.msg); 
            }        
          }) 
     },
    //闪送
    shansong(){
      	 this.states=this.$route.query.coupon
          let clickid=this.uuId();
        // let clickid=uuid;
            console.log(clickid)
           let localMockUserId = localStorage.getItem('userid');
           if( localMockUserId == null || localMockUserId == "" ) {
             let s = [];
             let hexDigits = "0123456789abcdef";
              for (let i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
              }
              s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
              s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
              s[8] = s[13] = s[18] = s[23] = "-";
            
              let userId = s.join("")+new Date().getMonth()+new Date().getSeconds()+new Date().getSeconds();
               let newMockUserId=userId;
               localStorage.setItem("userid", newMockUserId);
               this.mockUserId = newMockUserId;
           } else {
             this.mockUserId =  localMockUserId;
           }
             //  clickid 接口请求
            this.$api.clickId(
                clickid,
                this.mockUserId,
                {
                  appId:"shansong",
                  partnerId:"shansong"
                }
              ).then((res)=>{
                if(res.data.code==200){
                  let id=res.data.result;  // 把获取的参数传入到id里
                  this.id=id
                  console.log(this.id)
                  localStorage.setItem('id', JSON.stringify(id)); 
                  // this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
                  this.onmoey()   //获取用户可领的优惠卷
                }else{
                  this.$toast(res.data.msg); 
                }        
              }) 
     },
    // 美图
     meitu(){
       this.states=this.$route.query.coupon
          let clickid=this.uuId();
        // let clickid=uuid;
            console.log(clickid)
           let localMockUserId = localStorage.getItem('userid');
           if( localMockUserId == null || localMockUserId == "" ) {
             let s = [];
             let hexDigits = "0123456789abcdef";
              for (let i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
              }
              s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
              s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
              s[8] = s[13] = s[18] = s[23] = "-";
              let userId = s.join("")+new Date().getMonth()+new Date().getSeconds()+new Date().getSeconds();
               let newMockUserId=userId;
               localStorage.setItem("userid", newMockUserId);
               this.mockUserId = newMockUserId;
           } else {
             this.mockUserId =  localMockUserId;
           }
             //  clickid 接口请求
            this.$api.clickId(
                clickid,
                this.mockUserId,
                {
                  appId:"meituxiuxiu",
                  partnerId:"meituxiuxiu"
                }
              ).then((res)=>{
                if(res.data.code==200){
                  let id=res.data.result;  // 把获取的参数传入到id里
                  this.id=id
                  console.log(this.id)
                  localStorage.setItem('id', JSON.stringify(id)); 
                  // this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
                  this.onmoey()   //获取用户可领的优惠卷
                }else{
                  this.$toast(res.data.msg); 
                }        
              }) 
     },
      //挖财
      wacai(){
        let name, value
        let str = location.href // 获取整个地址栏
        let num = str.indexOf('?')
        str = str.substr(num + 1) // 取得所有参数   
        let arr = str.split('&') // 各个参数放到数组里
        let json =[]
        for (let i = 0; i < arr.length; i++) {
          num = arr[i].indexOf('=')
            name = arr[i].substring(0, num);
            value = arr[i].substr(num + 1);
            json.push(value)
            // this.id.clickId = value
           
        }
         
				 this.states=json[1]
				 console.log(this.json)
                      //  clickid 接口请求
             //wacai1212
          localStorage.setItem('wacaiValue',json[0]);
          if (num > 0) {       
            this.$api.wacai(
              json[0],{
              appId:"wacai",
              partnerId:"wacai",
              }).then((res)=>{
                if(res.data.code==200){
                  let id=res.data.result;  // 把获取的参数传入到id里
                  this.id=id
                  localStorage.setItem('wacaiId', JSON.stringify(id)); 
                  // this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
                  this.onmoey()   //获取用户可领的优惠卷
                }else{
                  this.$toast(res.data.msg); 
                }        
              }) 　
          }else{
             this.id.clickId=localStorage.getItem('wacaiValue')
             this.id=JSON.parse(localStorage.getItem('wacaiId'));
            //  this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
              this.onmoey()   //获取用户可领的优惠卷
          }
      },
      //youmi
      youmi(){
       this.states=this.$route.query.coupon
          let clickid=this.uuId();
        // let clickid=uuid;
            console.log(clickid)
           let localMockUserId = localStorage.getItem('userid');
           if( localMockUserId == null || localMockUserId == "" ) {
             let s = [];
             let hexDigits = "0123456789abcdef";
              for (let i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
              }
              s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
              s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
              s[8] = s[13] = s[18] = s[23] = "-";
              let userId = s.join("")+new Date().getMonth()+new Date().getSeconds()+new Date().getSeconds();
               let newMockUserId=userId;
               localStorage.setItem("userid", newMockUserId);
               this.mockUserId = newMockUserId;
           } else {
             this.mockUserId =  localMockUserId;
           }
             //  clickid 接口请求
            this.$api.clickId(
                clickid,
                this.mockUserId,
                {
                  appId:"youmi01",
                  partnerId:"youmi01"
                }
              ).then((res)=>{
                if(res.data.code==200){
                  let id=res.data.result;  // 把获取的参数传入到id里
                  this.id=id
                  console.log(this.id)
                  localStorage.setItem('id', JSON.stringify(id)); 
                  // this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
                  this.onmoey()   //获取用户可领的优惠卷
                }else{
                  this.$toast(res.data.msg); 
                }        
              }) 
     },
     //嘀嗒
      dida(){
       this.states=this.$route.query.coupon
          let clickid=this.uuId();
        // let clickid=uuid;
            console.log(clickid)
           let localMockUserId = localStorage.getItem('userid');
           if( localMockUserId == null || localMockUserId == "" ) {
             let s = [];
             let hexDigits = "0123456789abcdef";
              for (let i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
              }
              s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
              s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
              s[8] = s[13] = s[18] = s[23] = "-";
              let userId = s.join("")+new Date().getMonth()+new Date().getSeconds()+new Date().getSeconds();
               let newMockUserId=userId;
               localStorage.setItem("userid", newMockUserId);
               this.mockUserId = newMockUserId;
           } else {
             this.mockUserId =  localMockUserId;
           }
             //  clickid 接口请求
            this.$api.clickId(
                clickid,
                this.mockUserId,
                {
                  appId:"dida",
                  partnerId:"dida"
                }
              ).then((res)=>{
                if(res.data.code==200){
                  let id=res.data.result;  // 把获取的参数传入到id里
                  this.id=id
                  console.log(this.id)
                  localStorage.setItem('id', JSON.stringify(id)); 
                  // this.userId=(((1+Math.random())*0x10000)|0).toString(16).substring(1)
                  this.onmoey()   //获取用户可领的优惠卷
                }else{
                  this.$toast(res.data.msg); 
                }        
              }) 
     },
    //获取活动的状态
    onmoey(){
      // console.log(this.userId)
      //this.id.userId
    if(this.states == 1 || this.states == undefined){  
        this.$api.coupon(this.id.userId,{
          appId:this.id.appId,
          partnerId:this.id.partnerId,
          }).then((res)=>{
            // window.location.reload();  //刷新       
              if(res.data.result == null){
                this.inexpensive=false
                this.worth=false
                this.amount()
              }
              else if(res.data.result.own){
                this.work=res.data.result.own
                this.worklist=res.data.result.own
                console.log(this.worklist)
                this.amount()
                this.inexpensive=false
                this.worth=true
              }else{
                this.work=res.data.result.available
                //选择优惠券
                this.worth=true
                this.inexpensive=true
                this.amount()
              }    
          }) 
      }else{
          this.inexpensive=false
          this.worth=false
          this.amount()
      }
    },
    //领取优惠券的信息
    openwork(val,id){
      console.log(id)
      this.$api.getCoupon(this.id.userId,{
        activityCouponId:val,
        appId:this.id.appId,  
        partnerId:this.id.partnerId,
      }).then((res)=>{
        console.log(res)
        console.log("我被点击了")
        this.worklist=res.data.result;
        this.worth=true
        this.inexpensive=false
        // this.font=res.data.result.minFacePrice;   //默认红色
        // this.facePrice=res.data.result.minFacePrice;
        this.amount()
      })
      // this.inexpensive=false
    },
       //优惠券的立即使用按钮
    open(val,index){
      console.log(val)
      console.log("最低面额"+index)
      this.worth=false
      if(this.facePrice==""){
        this.font=index;   //默认红色
        this.facePrice=index;
        console.log("我是最低面额")
        let payPrice=this.worklist[0].minFacePrice-this.worklist[0].coupon
        this.payPrice=payPrice;   //券后价
        console.log(this.worklist[0].minFacePrice-this.worklist[0].coupon)
      }
      this.alertMenu()        //调用立即充值按钮
      // this.$api.getCoupon(this.id.userId,{
      //   activityCouponId:val,
      //   appId:"wacai1212",  
      //   partnerId:"wacai1212",
      // }).then((res)=>{
      //   console.log(res)
      //   this.inexpensive=true
      // })
    },
    //支付下拉框的取消事件
    inclick(){
      this.worth=true;
    },
    //变化的面额价格
    amount(){
      this.$api.price(this.id.userId,{
        appId:this.id.appId,  
        partnerId:this.id.partnerId,
      }).then((res)=>{
        // if(this.worklist[0].minFacePrice){
        //   this.minFacePrice=this.worklist[0].minFacePrice
        // }
        if(res.data.code == 200){
          let code=res.data.result;
          this.btnList=code.price;
        }else{
          this.$toast(res.data.msg);
        }
      })
    },
    // 活动知道了
    onOk(){
      this.discounts=false
    },
    onClickLeft() {
      this.$router.go(-1);  //返回上一页
      // window.history.back()
    },
    //文本框@keyup事件
        //节流函数
    throttle(){
      //保持this的指向始终指向vue实例
      var that=this;
      if(!that.statu){
          return;
      }    
      that.statu=false;
      setTimeout(function(){
          that.search();
          that.statu=true;
      },100)
    },
    //文本框点击事件
    infolist(){
      TDAPP.onEvent("输入了手机号");
      this.show1 = true
      //  this.dataList.push(this.data);
      // let that=this;
      // let arr1=that.dataList;
      // that.state = !that.state;
      // that.$api.onphone(that.id.userId,{
      //   appId :that.id.appId,
      //   partnerId :that.id.partnerId,
      // }).then((res)=>{
      //   that.dataList=res.data.result;
      //   that.list=res.data.result;
      // })
    },
    //手机号输入后的下拉框
    search(){
　　　　　//这个变量主要是用来测试节流后和不节流的区别　　　　
        var i=0;　　
    //定义的新数组存放筛选之后的数据
        this.list=[];
        //拿到当前input输入框输入的值
        this.phone=this.$refs.input.value;
        //判断展示ul列表，如果输入了就展示没输入就不展示
        if(this.phone.length>0){
            this.state=true;
        }else{
            this.state=false;
        }
        //循环模拟数据的数组
        this.dataList.map((msg)=>{
            //拿当前json的phone去分别跟输入的值进行比较
            //indexOf 如果在检索的字符串中没有出现要找的值是会返回-1的，所以我们这里不等于-1就是假设输入框的值在当前json里面找到的情况
            if(msg.phoneNumber.indexOf(this.phone)!=-1){
                //然后把当前json添加到list数组中
                this.list.push(msg);
            }
        })
    },
    clitem(e){
      this.phone=e;
      this.state=false;
    },
    //九宫格
    onChange(index,a,b,c){
      TDAPP.onEvent("点击面额");
      console.log(index)
      console.log(a)
      console.log(b)
      console.log(c)
      // this.worth=false;
      //facePrice    面额
      // payPrice     售后价
      this.facePrice=a;
      if(c){
         this.payPrice=c;
      }else{
        this.payPrice=b;
      }
      
      // 下标赋值 点击后变红色
      this.font=a;
    },
    //立即充值按钮
    alertMenu() {
      TDAPP.onEvent("点击立即充值");
      this.state=false;  
      let phone=this.phone;
      let facePrice=this.facePrice;
      // 手机为空
      if (phone == "") {
        this.$notify({ type: "warning", message: "请填写手机号" });
        // this.worth=true
      }else if(!(/^1\d{10}$/).test(phone)){
          //判断手机格式 
          this.$notify({type:"warning",message:"手机格式有误"});
          // this.worth=true
          // return false
      }else if(facePrice == ""){
        // 如果面额没被点击
         this.$notify({type:"warning",message:"请选择金额"});
      }else{
        // 成功了调起上拉框
        // this.show = true;
        this.payment()
      }
    },
    iptblur(){      //文本框失去焦点时
      let that=this
      setTimeout(function(){
        that.state=false
      },100)
    },
    //点击支付
    payment(){
      this.show=false;    //隐藏上拉框
      let database={      //传入接口的数据
        appId :this.id.appId,
        clickId :this.id.clickId,
        facePrice:this.facePrice,
        partnerId :this.id.partnerId,
        payPrice:this.payPrice,
        phoneNumber:this.phone,
        userId:this.id.userId,
        // orderTime:stat,
        // signKey:singKey,
      }
      this.$api.onLoad(database).then((res)=>{ 
        console.log(res)
        // if (process.env.ENV_CONFIG == 'wacai') {
        //   let url="https%3a%2f%2fwxpay.zhouyujunlin.com%2f%23%2fresult" 
        // } else if (process.env.ENV_CONFIG == 'meitu'){
        //   let url="https%3a%2f%2fwxpay.zhouyujunlin.com%2fmeitu%2f%23%2fresult"
        // }else if (process.env.ENV_CONFIG == 'shansong'){
        //   let url="https%3a%2f%2fwxpay.zhouyujunlin.com%2fshansong%2f%23%2fresult"
        // }
        let url=process.env.GET_BACK
        TDAPP.onEvent("点击付款");
        // 跳转支付页面
        if(res.data.code==200){
          window.location.href = res.data.result.mweb_url + "&redirect_url=" + url  + "%3forderId=" + res.data.result.order_id;
          //  localStorage.setItem('code', res.data.result.web_url);
          //  this.$router.replace({path: '/result'});
        }else{
          this.$toast(res.data.msg);
        }        
      }) 
     }
  },

  //充值记录
  // record(){
  //   this.$router.push({name: "/detail", params: {id: e}})
  //   TDAPP.onEvent('查看充值记录')
  // },
  // //充值说明
  // explain(){
  //   TDAPP.onEvent('查看充值说明')
  // },

   beforeDestroy: function (){
     console.log(11)
    _MEIQIA('hidePanel');
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

/* 全局 */
.hello{
  text-align: center;
}
.van-nav-bar {
  background: #fafafa;
  color: #000;
}


/* 手机文本框 */
.cad {
  border: 0.03rem solid #fff;
  border-radius: 0.3rem 0.3rem 0.3rem 0.3rem;
  -moz-box-shadow: 1px 3px 8px #ebe6e6;
  -webkit-box-shadow: 1px 3px 8px #ebe6e6;
  box-shadow: 1px 3px 8px #ebe6e6;
  line-height: 1rem;
  width: 90%;
  margin: 0.85rem auto 0.3rem;
  padding: 0 0.2rem 0 0.2rem;
  font-size: 0.39rem;
}
.int{
  position: relative;
  left: 0;
  top: 0;
}
.date{
    position: absolute;
    top: 1rem;
    left: 0.5rem;
    background: #fff;
    width: 87%;
    /* height: 200px; */
    margin: 0 auto;
    z-index: 100;
    border-radius: 10px;
    -webkit-box-shadow: 1px 3px 8px #ebe6e6;
    box-shadow: 1px 1px 4px #ebe6e6;
    overflow: hidden;
    animation: myfirst 0.2s;
}

.dateLi{
  padding: 10px;
  font-size: 16px;
  color: #000;
  border-bottom: 2px solid rgba(243, 238, 238, 0.5);
}
.mint-field-core {
  font-size: 20px;
}
/* 九宫格 */
.flex {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  /* width: 100%; */
  margin: 5px auto 20px;
  padding-left: 19px;

}
.flexTop{
  position: relative;
  left: 0;
  top: 0;
}

.flexAuto{
  position: absolute;
  left: 0;
  top: 2px;
  background: rgba(221, 216, 216, 0.479);
  width: 100%;
  height: 100%;
  border-radius: 0.3rem 0.3rem 0.3rem 0.3rem;
  box-shadow: 1px 3px 8px #ebe6e6;
   width: 2.1rem;
  height: 1.5rem;
   margin-right: 0.25rem;
  margin-top: 0.25rem;
  overflow: hidden;
  z-index: 50;
}


.grid {
  /* border: 0.1rem solid #fff; */
  border-radius: 0.3rem 0.3rem 0.3rem 0.3rem;
  background: #fff;
  /* padding: 0.2rem 0.2rem 0.3rem 0.2rem; */
  -moz-box-shadow: 1px 3px 8px #ebe6e6;
  -webkit-box-shadow: 1px 3px 8px #ebe6e6;
  box-shadow: 1px 3px 8px #ebe6e6;
  width: 2.1rem;
  height: 1.5rem;
  margin-right: 0.25rem;
  margin-top: 0.25rem;
  color: #000;
  overflow: hidden;
  position: relative;
  left: 0;
}
 .red{
   color: #fff ;
   background: #f30c3e;
   transition: 0.5s;
   /* border: 0.1rem solid rgb(247, 25, 25); */
 }

 .img_src{
   position: absolute;
   top: 0;
   left: 0;
   width: 50px;
   height: 50px;
 }

 .inat{
   color: #fff !important;
   transition: 0.5s;
 }

.grid .grid_number {
  font-size: 0.35rem;
  font-weight: bold;
}
.grid .grid_text {
  font-size: 0.25rem;
  margin-top: 5px;
  color: rgb(247, 11, 11);
}
/* 确认按钮 */
.butn {
  width: 40%;
  height: 0.85rem;
  border-radius: 0.3rem 0.3rem 0.3rem 0.3rem;
  background: #f30c3e;
  font-weight: bold;
  color: #fff;
  font-size: 0.3rem;
  margin-top: 0.7rem;
  z-index:10;
}

.botm{
  position: absolute;
  bottom: 0.5rem;
  /* left:  25%; */
  left:50%;
  margin-left: -113px
}
/* 充值记录 */
.url {
  font-size: 0.3rem;
  margin-top: 1.5rem;
  color: rgb(98, 72, 192);
}
/* 付款方式 */
.van-cell__title {
    display: flex;
    flex-direction: row;
}
.payment{
  background: rgb(68, 187, 119);
  width: 50%;
  margin-bottom: 25px;
}
/* 公司 */
.font_size{
  margin-top:20px;
  padding:5px 20px 10px 20px;
  font-size: 0.25rem;
  text-align: center;
  color: rgb(157, 158, 158);
}

 /* 活动 */
.van-popup--center.van-popup--round {
    border-radius: 7px;
    width: 300px;
}
.size{
  color: #000;
  /* text-shadow: 0px 0px #000; */
  font-size: 20px;
  font-weight:515;
  margin-top: 50px;
}
.width{
  width: 75%;
  display: flex;
  justify-content: space-between;
  border:1px solid #eee;
  margin:18px auto;
  font-size: 20px;
  border-radius: 7px;
  padding: 15px;
  -webkit-box-shadow: 0px 0px 3px #d8d8d8;
  box-shadow: 0px 0px 3px #d8d8d8;
}
.discount{
  color: #dd1d2d;
  font-weight: bold
}
.zero{
  margin-top: 5px;
  font-size: 12px;
  color:#dd1d2d; 
  /* text-shadow: 0px 0px #dd1d2d; */
}
.W_left{
  padding-left: 3px;
}
.W_right{
  text-align: left;
}
.discounts{
  color: #000;
  font-size: 20px;
}
.date_zero{
  margin-top: 5px;
  font-size: 12px;
  color: #a09d9d;
}
.btn{
  background: #dd1d2d;
  border: 0;
}

/* 领取优惠券 */
.van-overlay{
  z-index:100
}
.wrapper{
    display: flex;
    align-items: center; /*定义body的元素垂直居中*/
    flex-wrap: nowrap;
    height: 100%; 
    margin: 0 auto;
}

.block{
  width: 100%;
}

.felic{
  margin-bottom: 15px;
  font-size: 24px;
  color: #ffffff;
}

.wrapper_info{
  display: flex;
  /* align-items: center; */
  width: 85%;
  margin: 0 auto;
  /* margin: 0 auto; */
  flex-wrap:wrap;
  justify-content: space-around;
}

.overall{
  width: 2.5rem;
  background:#fff;
  border-radius: 10px;
  padding: 10px;
  margin-top: 15px;
  background: linear-gradient(to bottom, #fd6d46 0%,#ec494a 100%);
  box-shadow: 0px 0px 2px #fd6d46;
}


.change{
  border-radius: 5px;
  color: #fff;
  font-size: 20px;
  padding: 0px 10px 0 10px;
}
/* .change_font{
  padding-top: 10px;
} */

.one{
  font-size: 45px;
}
.work{
  font-size: 14px;
  color: #860d1d;
}
.change_btn{
  border-radius: 5px;
  border: #f7e602;
  background: #f7e602;
  width: 100%;
  height: 35px;
  line-height: 35px;
  color: #fd6d46;
}


/* 立即使用的优惠券 */
.code_work{
  /* border:1px solid red; */
  position: relative;
  height: 1.8rem;
  width: 95%;
  top: 0;
  left: 0;
  margin: 0 auto;
  z-index: 10;
  color: #fff;
  display: flex;
  flex-direction: row;
}
.img_icon{
  width: 100%;
  height: 1.8rem;
  position: absolute;
  top: 0;
  left: 0;
  z-index: -10;
}
.percent{
  width: 30%;
  /* border:1px solid #dd1d2d; */
  padding-top: 0.25rem;
  /* padding: 0.42rem 0 0.42rem 0; */
}
.percent_onmey{
  font-size: 15px;  
}
.percent_num{
  font-size: 35px;
}
.percent_font{
  font-size: 15px;
}
.right{
  width: 70%;
  display: flex;
  flex-direction: row;
  padding-left: 20px;
  padding-top: 0.55rem;
}
.right_font{
  font-size: 16px;
}
.right_date{
  font-size: 10px;
  padding-top: 5px;
}
.right_info{
  padding-top: 5px;
  padding-left: 13px;
}
</style>
