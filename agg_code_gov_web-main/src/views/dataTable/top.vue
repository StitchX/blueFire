<template>
  <div class="divFlex">
    <div class="divBorder top">
      <div class="topTitle">
        <svg-icon icon-class="topicon1" class="iconClass" />
        纳入监管商户总数
      </div>
      <div  class="number" >
        {{ totalRegulatoryMerchantCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")
        }}<span class="tag">&nbsp;个</span>
      </div>
      <!-- <div v-if="userNumber>=10000" class="number">
        {{ (userNumber/10000)
        }}<span class="tag">万个</span>
      </div> -->
      <div class="topMini">
        <div>
          当日纳入监管商户数 &nbsp; <span>{{todayRegulatoryMerchantCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;家</span></span>
        </div>
      </div>
    </div>
    <div class="divBorder top">
      <div class="topTitle">
        <svg-icon icon-class="topicon2" class="iconClass" /> 信用信息公示总数
      </div>
      <div  class="number">
        {{ totalPubulicInformationCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span class="tag">&nbsp;个</span>
      </div>
      <!-- <div v-if="userNumber>=10000" class="number">
        {{(userNumber/10000).toFixed(2)
        }}<span class="tag">万个</span>
      </div> -->
      <div class="topMini">
        <div>
          <span>当日信用信息公示总数 &nbsp; </span>
          <span>{{todayPubilicInformationCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;次</span></span>
        </div>
      </div>
    </div>
    <div class="divBorder top">
      <div class="topTitle">
        <svg-icon icon-class="topicon3" class="iconClass" /> 本年监管通知次数
      </div>
      <!-- <div v-if="userNumber != null && userNumber != ''&&userNumber<10000" class="number"> -->
      <div  class="number">
        {{ yearRegulatoryRemindCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")
        }}<span class="tag">&nbsp;个</span>
      </div>
      <!-- <div v-if="userNumber>=10000" class="number">
        {{ (userNumber/10000).toFixed(2)
        }}<span class="tag">万个</span>
      </div> -->
      <div class="topMini">
        <div>
          <span>政策宣讲 &nbsp; </span> <span>{{yearPolicyPreachCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;次</span></span>
        </div>

        <div>
          <span>监管提醒 &nbsp; </span> <span>{{yearRegulatoryRemindCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;次</span></span>
        </div>
      </div>
    </div>
    <div class="divBorder top">
      <div class="topTitle">
        <svg-icon icon-class="topicon4" class="iconClass" /> 经营场所风险提醒
      </div>
      <div  class="number">
        <!-- 默认绿🐎的数量 -->
        {{ greenCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")
        }}<span class="tag">&nbsp;个</span>
      </div>
      <!-- <div v-if="userNumber>=10000" class="number">
        {{ (userNumber/10000).toFixed(2)
        }}<span class="tag">万个</span>
      </div> -->
      <div class="topMini">
        <div>
          <span>绿码 &nbsp; </span>
          <span style="color:rgba(47, 194, 91, 1);padding-right:10px"
            >{{greenCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;家</span></span
          >
        </div>

        <div>
          <span>黄码 &nbsp; </span>
          <span style="color:rgba(251, 144, 27, 1)">{{yellowCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;家</span></span>
        </div>

        <div>
          <span>红码 &nbsp; </span>
          <span style="color:rgba(229, 19, 19, 1)">{{redCount.toString().replace(/(\d)(?=(\d{3})+$)/g, "$1,")}}<span>&nbsp;家</span></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { titleData } from "@/api/dataTable.js";
export default {
  components: {},
  data() {
    return {
        totalRegulatoryMerchantCount:0,
        todayRegulatoryMerchantCount:0,
        totalPubulicInformationCount:0,
        todayPubilicInformationCount:0,
        yearRegulatoryRemindCount:0,
        yearPolicyPreachCount:0,
        yearRegulatoryRemindCount:0,
        redCount:0,
        yellowCount:0,
        greenCount:0
    };
  },
  cerated(){
    let timer=setTimeout(res=>{
       this.getData()
    },2000)
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      titleData().then(res => {
        // let data=res.data.data
        // dataList.totalRegulatoryMerchantCount=data.totalRegulatoryMerchantCount,
        // const {totalRegulatoryMerchantCount,todayRegulatoryMerchantCount,totalPubulicInformationCount,todayPubilicInformationCount,yearRegulatoryRemindCount,yearPolicyPreachCount,activeMerchantCount}=res.data.data
        let datas=res.data.data
        console.log(datas,"datas")
     this.totalRegulatoryMerchantCount=datas.totalRegulatoryMerchantCount
     this.todayRegulatoryMerchantCount=datas.todayRegulatoryMerchantCount
     this.totalPubulicInformationCount=datas.totalPubulicInformationCount
     this.todayPubilicInformationCount=datas.todayPubilicInformationCount
     this.yearRegulatoryRemindCount=datas.yearRegulatoryRemindCount
     this.yearPolicyPreachCount=datas.yearPolicyPreachCount
     this.yearRegulatoryRemindCount=datas.yearRegulatoryRemindCount
     this.redCount=datas.redCount
     this.yellowCount=datas.yellowCount
     this.greenCount=datas.greenCount

     console.log(this.totalPubulicInformationCount,"totalRegulatoryMerchantCount");
    //  const {data:data}=res.data.data
    //   let datas=res.data.data
    // let datas=this.dataList
    // datas.map(res=>{
    //    console.log(res,"res111")
    //     if(res!= null && res != ''){
    //       return  res.replace(/(\d)(?=(\d{3})+$)/g, "$1,")
    //     }
    //   }) 
    //   console.log(data,"data")
      // this.dataList=res.data.data.dataList
        console.log(res,"res");
      });
    }
  }
};
</script>

<style scoped>
@import "./style.scss";
.topTitle {
  color: rgba(140, 140, 140, 1);
}
.topMini {
  border-top: 1px solid rgba(217, 217, 217, 1);
  width: 100%;
  padding: 15px 15px 0 15px;
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}
.number {
  font-size: 32px;
  font-weight: 550;
  padding: 15px 15px 0 30px;
}
.tag {
  font-size: 12px;
  color: black;
  font-family: not specified;
}
</style>
