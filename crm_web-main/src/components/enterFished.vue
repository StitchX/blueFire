<template>
  <div class="img-info">
      <!-- 成功 -->
    <div v-if="isSuccess">
      <div>
        <img src="../assets/png/success.png" alt="" />
      </div>
      <h5 class="ruzhu">入驻成功</h5>
      <van-button
        round
        block
        class="loginBtn"
        native-type="submit"
        @click="next"
        to="/merchant"
        >返回商户列表</van-button
      >
    </div>
    <!-- 失败 -->
    <div v-else>
      <div>
        <img src="../assets/png/failed.png" alt="" />
      </div>
      <h5 class="ruzhu">入驻失败</h5> 
      <p class="faildfont" >{{errorMessage}}</p>
      <van-button
        round
        block
        class="loginBtn"
        native-type="submit"
        @click="next"
        to="/enterOne"
        >返回重新提交</van-button
      >
    </div>
  </div>
</template>
<script>
export default {
    data() {
       return{
           isSuccess:true,
           errorMessage:'可能因为网络原因，请检查您的网络'
       } 
    },
    mounted(){
     this.isSuccess=this.$route.params.isSuccess
     console.log(this.isSuccess,"是否提交成功")
    }
}
</script>

<style scoped>
.img-info {
  padding: 120px 20px 0 20px;
  background: #fff;
  height: 100%;
  text-align: center;
  position: fixed;
  top: 0px;
  right: 0;
  left: 0;
}
.loginBtn {
  background: #347ff1;
  color: #fff;
  font-size: 16px;
  padding: 0px !important;
}
.faildfont{
font-size:13px;
color:#8C8C8C;
margin-bottom:30px;
}
.ruzhu{
    margin:0px 5px 20px 5px;
    font-size:20px;
}
</style>
