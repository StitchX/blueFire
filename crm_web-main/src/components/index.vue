<template>
  <div class="index" id="id">
    <div class="topBox">
      <!-- 个人信息 -->
      <div class="user">
        <div class="info">
          <img
            :src="user"
            alt=""
            style="vertical-align: middle; padding-right: 10px"
          />
          <span class="sfont">{{ bdList.name }}&nbsp;&nbsp;</span>
          <span class="sfont"
            >{{ bdList.deptName }}&nbsp;&nbsp; {{ bdList.roleName }}</span
          >
        </div>
      </div>
      <!--状态   状态:1.已入驻 2.已预约 3.已开卡 4.已使用 -->
      <div class="btnDiv">
        <van-button
          size="small"
          @click="handleReseted(0)"
          class="activeBtn"
          :class="isSelected == '0' ? 'activeBtn' : 'inactiveBtn'"
          >全部</van-button
        >
        <van-button
          size="small"
          @click="handleReseted(1)"
          :class="isSelected == 1 ? 'activeBtn' : 'inactiveBtn'"
          >已入驻</van-button
        >
        <van-button
          size="small"
          @click="handleReseted(2)"
          :class="isSelected == 2 ? 'activeBtn' : 'inactiveBtn'"
          >已预约</van-button
        >
        <van-button
          size="small"
          @click="handleReseted(3)"
          :class="isSelected == 3 ? 'activeBtn' : 'inactiveBtn'"
          >已开卡</van-button
        >
        <span style="padding-left: 10px">共{{ pageSum }}个商户</span>
      </div>
    </div>

    <div class="scroll_wrap">
      <!-- 商户列表 -->
      <!-- status==1  全部     2---已入驻   3---已预约   4---已开卡 -->
      <!-- 全部 -->
      <van-pull-refresh
        class="left_list_box"
        id="pullRefresh"
        v-model="refreshing"
        @refresh="onRefresh"
      >
        <van-list
          :loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div v-for="(item, index) in myList" :key="index" ref="block">
            <!-- <router-link  :to="{path:'/details/'+item.merchantId}"> -->
            <van-cell class="card" @click="lookDetails(item.merchantId,item.createTime)" >
              <!-- <van-cell class="card" > -->
              <div
                class="rightTop"
                style="background: rgba(17, 161, 224, 0.1); color: #1066dc"
                v-show="item.status == '1'"
              >
                已入驻
              </div>
              <div
                class="rightTop"
                style="background: rgba(0, 209, 223, 0.1); color: #00d1df"
                v-show="item.status == '2'"
              >
                已预约
              </div>
              <div
                class="rightTop"
                style="background: rgba(255, 150, 0, 0.1); color: #ff9600"
                v-show="item.status == '3'"
              >
                已开卡
              </div>
              <div
                class="rightTop"
                style="background: rgba(0, 166, 75, 0.1); color: #00a64b"
                v-show="item.status == '4'"
              >
                已使用
              </div>
              <div class="titleFont" >{{ item.storeName }}</div>
            <div class="timeFont">
              入驻时间：<span>{{ item.createTime.slice(0, 10) }}</span>
            </div>
            </van-cell>
            <!-- </router-link> -->
          </div>
        </van-list>
      </van-pull-refresh>
    </div>

    <div class="footer"></div>
    <router-view />
    <van-tabbar
      class="tab"
      v-model="active"
      route
      placeholder
      safe-area-inset-bottom
    >
      <van-tabbar-item to="/index">
        <span class="tabbar">首页</span>
        <template #icon="props">
          <img
            :src="props.active ? iconShouye.active : iconShouye.inactive"
            class="tabbar-icon"
          />
        </template>
      </van-tabbar-item>
      <van-tabbar-item to="/search">
        <div class="sao">
          <div class="saoImg">
            <img :src="saoyisao" />
          </div>
        </div>
      </van-tabbar-item>
      <van-tabbar-item to="/merchant">
        <span class="tabbar">商户列表</span>
        <template #icon="props">
          <img
            :src="props.active ? iconShanghu.active : iconShanghu.inactive"
            class="tabbar-icon"
          />
        </template>
      </van-tabbar-item>
    </van-tabbar>
    <div class="footer"></div>
  </div>
</template>

<script>
export default {
  
  data() {
    return {
      saoyisao: require("../assets/png/saoyisao.png"),
      user: require("../assets/png/user.png"), //头像
      isSelected: 0, //按钮 0--全部   1--已入驻  2--已预约  3--已开卡
      myList: [], //商户列表
      loading: false, //加载loading
      finished: false, //是否加载完成
      data: {
        size: 10, //size
        page: 1, //page
      },
      pageSum: null, //共多少页 total/size
      pageStatus: false,
      tips: "", //底部提示
      isOriginHei: true,
      screenHeight: document.documentElement.clientHeight, //此处���可能是其他获取方法
      originHeight: document.documentElement.clientHeight,
      active: "index",
      iconShouye: {
        active: require("../assets/png/shouyeActive.png"),
        inactive: require("../assets/png/shouye.png"),
      },
      iconShanghu: {
        active: require("../assets/png/shanghuActive.png"),
        inactive: require("../assets/png/shanghu.png"),
      },
      refreshing: false, //刷新
      bdList: {
        roleName: "",
        name: "",
        deptName: "",
      }, //bd信息
      scrollTop:'',//滚动条位置
    };
  },

  created() {
    console.log("我是第1次进入");
    console.log(this.isSelected, "status");
    this.onLoad();
    this.getBdInfo();
  },
  activated() {
    console.log(this.$route.meta,"我是第2次进入");
    const position = JSON.parse(window.sessionStorage.getItem('position1'))
    console.log(position,"position进来")
    document.getElementsByClassName("scroll_wrap")[0].scrollTop= position
  },

beforeRouteEnter(to, from, next) {
    next()
  },
  beforeRouteLeave(to, from, next) {

    // 保存离开页面时的位置
     window.sessionStorage.setItem('position1', JSON.stringify(this.scrollTop))
     next();
  },
  watch:{
    $route(to,from){
      console.log(to,from,"watch")
      if(from.name=='Login'){
        this.$router.go(0)
      }
    }
  },
  mounted() {
    console.log("我是mounted");
    // let self = this;
    // window.onresize = function () {
    //   return (function () {
    //     self.screenHeight = document.documentElement.clientHeight;
    //   })();
    // };
     window.addEventListener("scroll", this.handleScroll, true);
  },
  //  deactivated() {
  //   console.log("我是deactivated");
  //   window.removeEventListener("scroll", this.handleScroll, true);
  // },
  methods: {
    //获取BD信息
    getBdInfo() {
      this.$api.bdInfo().then((res) => {
        this.bdList = res.data.data;
        console.log(res, "bd");
      });
    },
    onLoad() {
      console.log("20");
      this.loading = true;
      setTimeout(() => {
        this.ongetLoad();
      }, 1000);
    },
    //加载列表信息
    ongetLoad() {
      let data = this.data;
      if (this.myList.length == this.pageSum) {
        this.finished = true;
        console.log(this.myList.length, "this.myList.length");
        console.log(this.pageSum, "this.pageSum");
      } else {
        this.$api
          .mechInfo(data)
          .then((res) => {
            console.log(res, 99999);
            if (res.data.status == 200) {
              this.data.page += 1;
              this.pageSum = res.data.data.total;
              // let list = res.data.data.records;
              console.log(this.myList.length);
              if (this.myList.length == 0) {
                this.myList = res.data.data.records;
              } else if (this.myList.length >= this.pageSum) {
                // this.myList = res.data.data.records;
                this.finished = true;
                console.log(this.myList, "this.myList1111");
                // this.loading = false;
              } else if (this.myList.length <= this.pageSum) {
                this.myList = this.myList.concat(res.data.data.records);
                // this.myList.push(...res.data.data.records)
                // this.finished=true
                this.loading = false;

                console.log(this.myList, "this.myList222");
              }
              // 如果加载完毕，显示finished
            }
            if (res.data.status == 402) {
              console.log(222222);
              this.$router.push("/");
            }
            this.loading = false;
          })
          .catch((err) => {
            this.finished = true;
            this.loading = false;
          });
      }
    },

    onRefresh() {
      this.myList = [];
      this.refreshing = true;
      this.data.page = 1;
      // 清空列表数据
      this.finished = false;
      this.ongetLoad();
      this.refreshing = false;
      console.log(this.myList, "list");
    },
    //tab点击事件
    handleReseted(val) {
      this.myList = [];
      this.pageSum = null;
      this.isSelected = val;
      // this.loading=true
      if (this.isSelected == "0") {
        // 全部
        this.loading = true;
        this.data = {
          page: 1,
          size: 10,
        };
        this.onRefresh();
      } else if (this.isSelected == "1") {
        // 已入驻
        this.loading = true;
        this.data = {
          page: 1,
          size: 10,
          status: ["1"],
        };
        this.onRefresh();
      } else if (this.isSelected == "2") {
        // 已预约
        this.loading = true;

        this.data = {
          page: 1,
          size: 10,
          status: ["2"],
        };
        this.onRefresh();
        // this.refreshing=true
        // this.loading=false
      } else if (this.isSelected == "3") {
        // 已开卡
        this.loading = true;
        this.data = {
          page: 1,
          size: 10,
          status: ["3"],
        };
        this.onRefresh();
      }
    },

    //查看详情
    lookDetails(val,time) {
      console.log(val,'val2')
      this.$router.push({ name: "Details", query: { id: val, code: "1",time:time } });
    },
    //滚动条
      handleScroll(e) {
      this.flag_scroll = false;
      this.scrollTop =
        document.getElementsByClassName("scroll_wrap")[0].scrollTop;
      if (this.scrollTop > 100) {
        this.flag_scroll = true;
      } else {
        this.flag_scroll = false;
      }
    },
  },
};
</script>

<style lang="" scoped>
.index {
  /* background:rgb(245,246,247); */
  /* padding: 0; */
  /* margin: 0; */
  color: rgba(38, 38, 38, 1);
  height: 100%;
  overflow: hidden;
  /* overflow:auto;
   position: static; */
  /* -webkit-overflow-scrolling: touch; */
}
.topBox {
  height: 120px;
  width: 100%;
  position: fixed;
  top: 0;
  z-index: 10;
}

.user {
  /* height: 100px; */
  /* background: url("../assets/png/shouyeBg.png") no-repeat;
  background-size: cover; */
  background: linear-gradient(180deg, #347ff1 0%, #6ca1f2 100%);
  box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.22);
}
.info {
  padding: 20px 20px 20px 20px;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}

.btnDiv {
  padding: 10px 5px 10px 5px;
  flex-wrap: nowrap;
  font-size: 13px;
  background: rgb(236, 236, 236);
}
.activeBtn {
  background: #347ff1;
  border-radius: 20px;
  border: 1px solid #347ff1;
  color: #fff;
}
.inactiveBtn {
  border: 1px solid #d4d4d4;
  background: #d4d4d4;
  color: #262626;
  border-radius: 23px;
}
/* 右侧顶部tag */
/* 已入驻 */
.rightTop {
  position: absolute;
  top: 0;
  right: 0;
  padding: 5px 10px 5px 10px;
  border-radius: 0px 15px 0px 15px;
  font-size: 13px;
  font-weight: 400;
}
.card {
  margin-top: 10px;
  position: relative;
  padding: 0px 0px 20px 20px;
  /* border:1px solid red; */
  background: #fff;
  border-radius: 15px;
  box-shadow: 0px 0px 8px 0px rgba(81, 82, 84, 0.08);
  /* border-radius: 12px; */
}
/* card title字体 */
.titleFont {
  width: 73%;
  font-size: 16px;
  font-weight: bold;
  color: #262626;
  padding:15px 20px 8px 0;
}
.timeFont {
  font-size: 14px;
  font-weight: bold;
  color: #8c8c8c;
  margin-top:5px;
}
.tab {
  border-top: 5px solid rgb(236, 236, 236);
  padding: 10px 0;
}

.tabbar-icon {
  width: 24px;
  height: 22px !important;
}
.tabbar {
  font-size: 14px;
  /* position: absolute; */
}
/* 固定列表滚动位置 */

.scroll_wrap {
  /* position: absolute;
  left: 0;
  right: 0;
  top: 120px;
  bottom: 100px; */
  /* margin-bottom:200px; */
  height: calc(100vh - 190px);
  margin-top: 120px;
  overflow: auto;
  /* position:static; */
  -webkit-overflow-scrolling: touch; /* ios5+ */
  z-index: 9;
  /* border: 1px solid red; */
}

.left_list_box {
  display: flex;
  flex-direction: column;
  /* border: 1px solid green; */
  min-height: calc(100vh - 190px);
}
/* 扫一扫按钮 */
.sao {
  width: 90px;
  height: 90px;
  background: #ffffff;
  border-radius: 50%;
  padding: 10px 15px;
  position: absolute;
  right: 39%;
  bottom: 0px;
  z-index: 999;
}
/* 扫一扫按钮tupian */
.saoImg {
  width: 60px;
  height: 60px;
  background: #347ff1;
  border-radius: 50%;
  padding: 15px 15px;
}
</style>
