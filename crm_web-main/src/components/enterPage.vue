<template>
  <div class="enter">
    <van-nav-bar
      title="放心码入驻"
      left-text=""
      left-arrow
       @click-left="$router.back(-1)"
    />
    <h2 class="select">请选择入驻商家的行业</h2>
    
    <div class="box">
      <van-radio-group v-model="parentCategoryId" @change="onChange">
        <van-cell-group>
          <van-cell clickable data-name="1"  class="cell-box">
            <div>
              <img class="cell-img" src="../assets/png/canyin.png" />
              <span class="cell-info">餐饮行业</span>
            </div>
            <div class="cell-small">生产、流通、餐饮等</div>
            <van-radio slot="right-icon" name="1" icon-size="30" />
          </van-cell>
          <van-cell
            class="cell-box feicanyin"
            clickable
            data-name="2"
          >
            <div>
              <img class="cell-img" src="../assets/png/feicanyin.png" />
              <span class="cell-info fei">非餐饮行业</span>
            </div>
            <div class="cell-small ">休闲娱乐、酒店旅游、文体教培等</div>
            <van-radio slot="right-icon" name="2" icon-size="30" />
          </van-cell>
        </van-cell-group>
      </van-radio-group>
              <van-button
                round
                block
                type="info"
                @click="next"
               
                style="font-size:16px;"
                >选好了，下一步</van-button
              >
    </div> 
  </div>
</template>
<script>
export default {
  data() {
    return {
      parentCategoryId: "1",//1---餐饮   2---非餐饮
    };
  },
  methods: {

    onChange(e){
      this.parentCategoryId=e
        console.log(e,"e");
        // 默认选择餐饮行业  跳转到入驻页面
    },
    //返回
    onClickRight() {
window.history.go(-1);
    },
    next(){
      console.log(22222222222222222)
      let id=this.$route.query.id
      // this.$router.push({name:'EnterOne',params:{parentCategoryId:this.parentCategoryId}})
      this.$router.push({path:'/enterOne',query:{parentCategoryId:this.parentCategoryId,id }})
    } 
}
}
</script>

<style scoped>
.enter {
  color: rgba(38, 38, 38, 1);
  background: #fff;
  position: fixed;
  top:0;
  left:0;right:0;
  bottom:0;
  /* text-align: center; */
}
.navBar {
  padding: 5px 10px;
  vertical-align: middle;
  text-align: center;
}
/* 头部文字 */
/deep/ .van-nav-bar__title {
  font-size: 18px;
  font-weight: bold;
  color: rgba(38, 38, 38, 1);
}
/* 返回键 */
/deep/ .van-icon-arrow-left::before {
  font-size: 18px;
  content: "\F008";
  color: rgba(38, 38, 38, 1);
}
.select {
  padding: 30px 10px 10px 30px;
  font-size: 24px;
  font-weight: 800;
  color: #262626;
  line-height: 33px;
}
.box {
  margin: 32px 20px ;
}
.cell-box {
  width: 100%;
  padding: 20px 20px 20px 20px;
  margin-bottom: 16px;
  background: rgba(254, 246, 242, 1);
  border-radius: 10px;
}
.cell-img {
  width: 32px;
  height: 32px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 8px;
}
.cell-info {
  display: inline-block;
  font-size: 20px;
  color: rgba(255, 98, 6, 1);
}
.cell-small {
  padding: 0 0 10px 47px;
  color: rgba(89, 89, 89, 1);
  font-size: 13px;
}
.fei {
  color: rgba(39, 139, 250, 1);
}
.feicanyin {
  background: rgba(243, 248, 254, 1);
}

</style>
