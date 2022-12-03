<template>
  <div class="risk">
    <van-nav-bar
      title="风险等级说明"
      left-text=""
      left-arrow
      @click-left="onClickLeft"
    />
    <h4 style="padding-left:20px;">放心码风险等级仅针对<span class="fontColor">食品流通</span> 和<span class="fontColor">餐饮行业</span> </h4>
    <div class="contain">
        <p>1、风险等级从低到高划分为A级风险、B级风险、C级风险、D级风险四个等级。</p>
        <p>2、风险等级得分为静态风险因素分值加动态风险因素分值之和，满分100分。</p>
        <p>1、风险等级从低到高划分为A级风险、B级风险、C级风险、D级风险四个等级。</p>
        <p>2、风险等级得分为静态风险因素分值加动态风险因素分值之和，满分100分。</p>
        <p>1、风险等级从低到高划分为A级风险、B级风险、C级风险、D级风险四个等级。</p>
        <p>2、风险等级得分为静态风险因素分值加动态风险因素分值之和，满分100分。</p>
        <p>1、风险等级从低到高划分为A级风险、B级风险、C级风险、D级风险四个等级。</p>
        <p>2、风险等级得分为静态风险因素分值加动态风险因素分值之和，满分100分。</p>
    </div>
  </div>
</template>

<script>
import { list } from '@/api/app'
export default {
  data() {
    return {}
  },
  beforeCreate() {},
  created() {},
  mounted() {},
  methods: {
    //返回
   onClickLeft(){
    this.$router.go(-1)
    }
  },
}
</script>

<style scoped>
@import "./css/style.scss";
.risk{
background: #fff;
position: fixed;
top:0;
bottom: 0;
}
.contain{
 margin:0 20px;
 background: #F5F6F7;
 color: #595959;
}
.fontColor{
    color: #FF7800;
}
</style>
