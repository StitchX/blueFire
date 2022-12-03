<template>
  <div class="page">
    <van-nav-bar
      title="明厨亮灶"
      left-text=""
      left-arrow
      @click-left="onClickLeft"
    />
    <div class="empty">
        <img src="../assets/empty.png" alt="">
        <div>暂无数据</div>
    </div>
        <pay-page ref="pay"></pay-page>
  </div>
</template>

<script>
import { list } from '@/api/app'
import payPage from './payPage.vue'
export default {
    components:{payPage},
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
</style>
