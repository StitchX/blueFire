<template>
  <div id="record">
     <!-- 标题头 -->
    <van-nav-bar  title="话费充值记录" left-arrow @click-left="onClickLeft" :fixed="true" :border="false" />
    <!-- 滚动条-->
    <van-tabs  v-model="value" sticky :offset-top="45" @click="click(value)">
      <!-- 全部话费记录 -->
      <van-tab title="全部" name="200" >
         <!-- 分页数据 -->
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
            >         
            <div class="padin">
              <div class="circle" v-for="(item,index) in list" :key="index">  
                <div class="single">
                  <div class="year">{{item.orderTime | dateFormat}}</div> 
                  <div  class="height">{{item.status == 2 || item.status == 3 || item.status == 4 ? "充值成功" : item.status == 0 || item.status == 1 ? "充值中" : item.status == -3 ? "退款成功" : "充值失败"}}</div>
                </div>      
                    <div class="left">
                      <img src="../../build/phone.png" class="image">
                      <div class="black">
                        <div class="blod">充值中心 话费{{item.facePrice}}元</div>
                        <div class="gray"> <span>{{item.address}}{{item.rechargeType==1 ? "移动" : item.rechargeType==2 ? "电信" : "联通"}}</span> {{item.phoneNumber}} </div>
                      </div>
                    </div>
                    <van-divider hairline />
                    <div class="size">
                      <span class="font">售后价：</span> 
                      <span class="blod">{{item.payPrice}} </span>
                      <span class="font">元</span>
                    </div>
                    
               </div>
            </div>
          </van-list>
      </van-tab>
      <!-- 成功话费记录 -->
      <van-tab title="成功" name="2" >
         <!-- 分页记录 -->
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
            >
            <div class="padin">
              <div class="circle" v-for="(item,index) in list" :key="index">  
                <div class="single">
                  <div class="year">{{item.orderTime | dateFormat}}</div> 
                  <div  class="height">{{item.status == 2 || item.status == 3 || item.status == 4 ? "充值成功" : item.status == 0 || item.status == 1 ? "充值中" : item.status == -3 ? "退款成功" : "充值失败"}}</div>
                </div>      
                    <div class="left">
                      <img src="../../build/phone.png" class="image" >
                      <div class="black">
                        <div class="blod">充值中心 话费{{item.facePrice}}元</div>
                        <div class="gray"> <span>{{item.address}}{{item.rechargeType==1 ? "移动" : item.rechargeType==2 ? "电信" : "联通"}}</span> {{item.phoneNumber}} </div>
                      </div>
                    </div>
                    <van-divider hairline />
                    <div class="size">
                      <span class="font">售后价：</span> 
                      <span class="blod">{{item.payPrice}} </span>
                      <span class="font">元</span>
                    </div>
                    
               </div>
            </div>
          </van-list>
      </van-tab>
          <!-- 失败话费记录 -->
      <van-tab title="充值中" name="1" >
         <!-- 分页记录 -->
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
            >
            <div class="padin">
              <div class="circle" v-for="(item,index) in list" :key="index">  
                <div class="single">
                  <div class="year">{{item.orderTime | dateFormat}}</div> 
                  <div  class="height">{{item.status == 2 || item.status == 3 || item.status == 4  ? "充值成功" : item.status == 0 || item.status == 1 ? "充值中" : item.status == -3 ? "退款成功" : "充值失败"}}</div>
                </div>      
                    <div class="left">
                      <img src="../../build/phone.png" class="image">
                      <div class="black">
                        <div class="blod">充值中心 话费{{item.facePrice}}元</div>
                        <div class="gray"> <span>{{item.address}}{{item.rechargeType==1 ? "移动" : item.rechargeType==2 ? "电信" : "联通"}}</span> {{item.phoneNumber}} </div>
                      </div>
                    </div>
                    <van-divider hairline />
                    <div class="size">
                      <span class="font">售后价：</span> 
                      <span class="blod">{{item.payPrice}} </span>
                      <span class="font">元</span>
                    </div>         
               </div>
            </div>
          </van-list>
      </van-tab>
      <!-- 失败话费记录 -->
      <van-tab title="失败" name="-2" >
         <!-- 分页记录 -->
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
            >
            <div class="padin">
              <div class="circle" v-for="(item,index) in list" :key="index">  
                <div class="single">
                  <div class="year">{{item.orderTime | dateFormat}}</div> 
                  <div  class="height">{{item.status == 2 || item.status == 3 || item.status == 4 ? "充值成功" : item.status == 0 || item.status == 1 ? "充值中" : item.status == -3 ? "退款成功" : "充值失败"}}</div>
                </div>      
                    <div class="left">
                      <img src="../../build/phone.png" class="image" >
                      <div class="black">
                        <div class="blod">充值中心 话费{{item.facePrice}}元</div>
                        <div class="gray"> <span>{{item.address}}{{item.rechargeType==1 ? "移动" : item.rechargeType==2 ? "电信" : "联通"}}</span> {{item.phoneNumber}} </div>
                      </div>
                    </div>
                    <van-divider hairline />
                    <div class="size">
                      <span class="font">售后价：</span> 
                      <span class="blod">{{item.payPrice}} </span>
                      <span class="font">元</span>
                    </div>         
               </div>
            </div>
          </van-list>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script>
export default {
  name: 'App',
  data(){
     return{
        value:"",            //滚动条的状态值   全部：200  成功：2  充值中：1 失败：-2
        list: [],            //页面数据
        loading: false,       //加载
        finished: false,      //没有更多
        limit: 0,             //页码
        page: 1,              //第1页
        id:{}                
     }
  },
  created() {
      TDAPP.onEvent("点击充值记录");    
    //跳转获取到uuid..
    this.id=this.$route.query
  },
  methods:{
    //滚动条与底部距离小于 offset 时触发
    onLoad() {
      // 进入页面显示10条数据，往上拉会相加
      this.limit+=10
      // 加后调用请求 1秒的加载中
      setTimeout(() => { 
        this.info() 
      }, 
      1000);
    },
    //请求的数据
    info(){
       this.$api.getUserInfo(this.page,this.limit,this.id.userId,this.value,{
        partnerId:this.id.partnerId,
        appId:this.id.appId,
      }).then((res)=>{
        if(res.data.code == 200){
          // 加载状态
          this.loading = true;
          // 数据
          let code=res.data.result.records;
          // 状态
          let value=res.data.result.records.status;
          // 总页数
          let total=res.data.result.total; 
          // 赋值
          this.list=code
          // 加载状态结束
          this.loading = false;
          // 数据全部加载完成
          console.log(this.limit,total)
          // 如果页数大于或者等于总页数就停止加载
          if (this.limit >= total) {
            this.finished = true;
          }
        }else if(res.data.code == 10006){
          // 清空数据刷新页面
          this.list=[]
          this.finished = true;
        }else{
          this.$toast(res.data.msg);
        }
      })
    },
    // 左上角返回
    onClickLeft() {
      this.$router.go(-1);
    },
    // 滚动条的点击事件
    click(value){
     //页码等于10，让info请求接口
      this.limit=10;
      this.finished=false;
      this.loading=false;
      this.info()
    }
  }
}
</script>
 
<style scoped>
  .van-tab__pane, .van-tab__pane-wrapper {
    margin-top: 45px;
  }
  /* .van-list{
    padding-top:92px;
  } */

  .padin{
    padding: 0.3rem 0.3rem 0rem 0.3rem;
  }
  .year{
    font-size: 0.25rem;
    margin-bottom: 0.3rem;
    color:darkgrey;
  }
  .circle{
    border-radius: 0.1rem ;
    background: #fff;
    padding: 0.4rem;
    margin-bottom: 0.25rem;
  
  }
  .circle:last-child{ 
    margin-bottom: 0;
  }
  .single{
    display: flex;
    justify-content: space-between;
  }
  .left{
    display: flex;
    flex-wrap: nowrap;
  }
  .image{
    width: 0.8rem;
    height: 0.8rem;
    margin-right: 0.2rem;
  }
  /* .black{
    font-size: 0.2rem;
  } */
  .blod{
    font-size: 0.3rem;
    font-weight: bold;
  }
  .size{
    display: flex;
    justify-content: flex-end;
  }
  .font{
    font-size: 0.26rem;
    position: relative;
    left: 0.06rem;
    bottom: 0.02rem;
  }
  
  .gray{
    color: gray;
    margin-top: 0.05rem;
    font-size: 0.25rem;
  }
  .height{
    color: rgb(132, 139, 241);
    font-size: 0.26rem; 
  }

</style>