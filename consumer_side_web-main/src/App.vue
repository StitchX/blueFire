<template>
  <div id="app">
    <!-- 开启顶部安全区适配 -->
    <!-- <van-nav-bar safe-area-inset-top /> -->

    <router-view />
    <!-- 开启底部安全区适配 -->
        <!-- <pay-page ref="pay"></pay-page> -->
    <!-- <van-number-keyboard safe-area-inset-bottom /> -->
  </div>
</template>
<script>
import payPage from './views/payPage'
export default {
    components:{payPage},
    data(){
      return{

      }
    },
    watch: {
  // 如果路由有变化，会再次执行clear方法
  // "$route": "clear",
  // $route(to , from){
  //   // 如果路由到首页就吊起键盘
  //   if(to.path == "/"){
  //     this.$refs.pay.showPay = true
  //   }
  // }
},
}

</script>
<style lang="scss">
html,
body {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}
</style>
