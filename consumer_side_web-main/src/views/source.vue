<template>
  <div class="page">
    <van-nav-bar
      title="溯源信息"
      left-text=""
      left-arrow
      :fixed="true" :placeholder="true"
      @click-left="onClickLeft"
    />
    <div class="empty" v-if="listshow">
        <img src="../assets/empty.png" alt="">
        <div>暂无数据</div>
    </div>
      <van-list
         v-else
            v-model="loading"
            :finished="finished"
            finished-text="- 没有更多了 -"
            @load="onRefresh"
            :offset="130"
          >
        <div class="source_border" v-for="(item,index) in myList" :key="index">
            <div class="source_date">
              {{item.create_time}}
            </div>
            <div style="padding-left:12px">
              <van-image
                v-for="(img,index) in item.images"
                :key="index"
                @click="onChange(item.images,index)"
                  class="source_imge"
                  fit="cover"
                  lazy-load
                  :src="img"
                />
            </div>
        </div>
      </van-list>

    <van-image-preview v-model="show" :startPosition='index' @change="onChange1" :images="images">
      <template v-slot:index>第{{ index +1 }}页</template>
    </van-image-preview>

        <pay-page ref="pay"></pay-page>
  </div>
</template>

<script>
import { ImagePreview } from 'vant';
import { datalist } from '@/api/app'
import payPage from './payPage'
export default {
  components:{
    payPage,
    [ImagePreview.Component.name]: ImagePreview.Component,
  },
  data() {
    return {
      show:false,
      index:0,
       images: [],
      page:{
          merchantId:"",
					current: 1,
					size: 10,
        },
      refreshing:false,
      loading:false,
      finished:false,
      total:0,
      myList:[],
      merchantId:"",
      listshow:false
    }
  },
  beforeCreate() {
  },
  created() {
    this.page.merchantId = localStorage.getItem('id')
    this.merchantId = localStorage.getItem('id')
  },
  mounted() {
  },
  methods: {
    onRefresh(){
     setTimeout(() => {
        this.onLoad()
        this.loading = true
      }, 500)
    },
    onLoad(){
      datalist(this.page,this.merchantId).then(res => {
        // 当请求成功
        if (res.status == "200") {
          let data = res.data.records
          this.loading = false
          this.total = Math.ceil(res.data.total / this.page.size);
          this.myList = this.myList.concat(data)
          this.page.current++
          // 如果没有数据，显示暂无数据
           if (this.myList.length == 0) {
            this.listshow = true
          }
          // 如果加载完毕，显示没有更多了
          if (data.length == 0) {
            this.finished = true
          }
        }
      })
      .catch(error => {
          this.finished = true
      });
    },
    //返回
   onClickLeft(){
    this.$router.go(-1)
    },
      onChange(data,index){
        if(data){
          data.map((res)=>{
            return Number(res)
          })
         this.images = data
        }
        this.index= index
        this.show = true
    },
    onChange1(index){
        this.index= index
    }
  },
}
</script>

<style scoped>
@import "./css/style.scss";
.page{
  padding-bottom: 80px;
}
.source_border{
  border-radius: 12px;
  background: #FFFFFF;
  margin-top: 12px;
  box-shadow: 0 2px 1px 0 rgba(0, 0, 0, 0.05);
  padding-bottom: 12px;
}
.source_date{
  padding: 12px;
  color: #262626;
  font-size: 16px;
  border-bottom: 0.5px solid #f1f0f0;
}
.source_imge{
  width: 78px;
  height: 78px;
  margin: 8px 12px 0px 0px;
}
</style>
