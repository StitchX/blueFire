<template>
  <!-- <div class="app-container"> -->
  <div>
    <div class="grid-content1 bg-purple">
      <div style="min-height:60px;">
        <el-image
          style="width: 100%; height: 68px;z-index:99"
          :src="url"
          fit="contain"
        />
      </div>
      <div ref="scroll" :style="'height:' + height  +'px'" class="overflow" @scroll.passive="getScroll($event)">
      <!-- <div ref="scroll" :style="'height:' + 480  +'px'" class="overflow" @scroll.passive="getScroll($event)"> -->
        <el-divider />
        <div class="info">
          {{ titel }}
        </div>
        <div class="des_text">
          <div>
            <span>{{ date }}</span>
            <span>{{ type }}</span>
          </div>
          <div>
            <span>{{ issuingAgency }}</span>
          </div>
        </div>
        <el-divider />
        <div class="des_conet" v-html="data">
        </div>
         <div v-if="adjunctFile">
            附件：
          </div>
          <div style="color:#1890FF;margin-left:20px" v-for="item in adjunctFile" :key="item.length">
            {{item.fileName}}
          </div>
      </div>
        <div style="width:100%;text-align:center" v-if="show" >
          <el-button type="info" v-if="buton" style="padding:10px 90px;" round>请上滑看完本条通知</el-button>
          <el-button type="primary" v-else style="padding:10px 90px;" round>我已阅读完本条通知</el-button>
        </div>

    </div>
  </div>
  <!-- </div>   -->
</template>

<script>
export default {
  props: {
    // "data":String,
    data: {
      type: String,
      require: true
    },
    titel: {
      type: String,
      require: true
    },
    type: {
      type: String,
      require: true
    },
    date: {
      type: String,
      require: true
    },
    issuingAgency: {
      type: String,
      require: true
    },
    show:{
      type:Number,
      require:true
    },
    height:{
      type:String,
      requiret:true
    },
    adjunctFile:{
      type:String,
      requiret:true
    }
  },
  data() {
    return {
      url: require('./../../assets/head.png'),
      buton:true
    }
  },
//   mounted(){
//     let obj = this.$refs.scroll
//     if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
//       console.log("有")
//       } else{
//        this.buton = false
//       }
//   },
  activated(){
 let obj = this.$refs.scroll
    if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
      console.log("有")
      } else{
       this.buton = true
      }
  },
  methods:{
    getScroll(event) {
      // 滚动条距离底部的距离scrollBottom
      let scrollBottom =event.target.scrollHeight - event.target.scrollTop - event.target.clientHeight
        console.log(scrollBottom)
      if ( scrollBottom < 14 || scrollBottom == 0) {
        console.log("到底了")
       this.buton = false
      }
    }
  },
}
</script>

<style scoped>
   .grid-content1 {
     box-shadow: 0 2px 4px rgba(0, 0, 0, 0.11), 0 10px 16px rgba(0, 0, 0, 0.11);
     position: relative;
     top: 0;
     left: 0;
     width: 375px;
     height: 600px;
     background: #ffff;
     border-radius: 5px;
     padding: 0 10px;
     /* overflow-x :scroll; */
  }
  ::-webkit-scrollbar{
      width: 0 !important;
  }
  ::-webkit-scrollbar{
      width: 0 !important;height: 0;
  }
  .row-bg {
    /* padding: 10px 0; */
    background-color: #f9fafc;
  }
  .el-divider--horizontal{
      margin: 10px 0px;

  }
  .overflow{
     overflow-x :scroll;
     /* padding: 0px 10px; */
  }
  .info{
      padding: 10px 0;
      color: #292929;
      font-weight: 550;
      letter-spacing: .05em;
      font-size: 16px;
      min-height: 50px;
  }
  .des_text{
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      /* padding: 0px 10px; */
      color: #8C8C8C;
      font-size: 14px;
  }
  .des_conet{
      /* padding:10px; */
      font-size:14px;
      letter-spacing: .07em;
      line-height:  1.5em;

  }
</style>
