<template>
  <div id="app">
     <keep-alive>
          <router-view v-if="$route.meta.keepAlive" class="router-view">
          </router-view>
      </keep-alive>
     <router-view v-if="!$route.meta.keepAlive" class="router-view" ></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  data(){
    return{
   nowToken:''
    }
  },
  mounted(){

    //验证是否有token    有就直接跳到首页
     this.nowToken=localStorage.getItem('token')
     console.log(this.$route.path ,252525)
     if(this.nowToken==''||this.nowToken==null){
        this.$router.push('/')
     }else{
       if(this.$route.path==="/"){
          this.$router.push('/index')
       }
     }
     
    //  console.log($route.to(),888888)
  // window.addEventListener('touchmove',this.stopScrolling,false)
    
  },
  watch:{
//  $route(to,from){
//    this.nowToken=localStorage.getItem('token')
//    console.log(this.nowToken,'nowToken')
//    if(this.nowToken){
//      if(from.name=="Login"){
//        this.$router.push('/index')
//      }
//       if(from.name=='EnterPage'){
//            this.$router.push('/enterPage')
//          }
//    }    
//   }
  },
  methods:{
    // stopScrolling(e){
    //    e.preventDefault()
    // }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  font-size: 20px;
  padding: 0;
  margin: 0;
}


  html,body,#app{
    background-color: #ececec;

  }

</style>
