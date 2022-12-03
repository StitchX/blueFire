<template>
  <div class="enter">
    <div class="topBox" v-if="open == false">
      <!-- 顶部 -->
      <van-nav-bar
        title="商户列表"
        left-text=""
        left-arrow
        @click-left="$router.back(-1)"
      >
        <van-icon name="search" slot="right" @click="open = true" />
      </van-nav-bar>
      <!-- 状态选择 class="statusStyle" -->
      <!-- <div class="option"> -->
      <van-dropdown-menu class="selectBox"  :close-on-click-overlay="false">
        <van-dropdown-item id="item" title="状态" ref="item">
          <div class="statusStyle">
            <van-button
              style="width: 1.5rem;margin-right:10px;"
              v-for="(item, index) in List"
              :key="index"
              class="selectBtn"
              :class="item.ischeck == true ? 'activeBtn' : 'inactiveBtn'"
              size="small"
              @click="buttonSelected(index, item, List)"
              >{{ item.name }}</van-button
            >
          </div>
          <div class="flexBtn">
            <van-button class="footerBtn" type="primary" @click="handleReset"
              >重置</van-button
            >
            <van-button class="footerOk" type="info" @click="handleOk"
              >确定</van-button
            >
          </div>
        </van-dropdown-item>
        <van-dropdown-item id="item" title="资料" ref="resourceItem">
          <div class="statusStyle">
            <van-button
              style="width: 1.5rem;margin-right:10px;"
              v-for="(item, index) in resourceList"
              :key="index"
              class="selectBtn"
              :class="item.ischeck == true ? 'activeBtn' : 'inactiveBtn'"
              size="small"
              @click="resourceSelected(index, item, resourceList)"
              >{{ item.name }}</van-button
            >
          </div>
          <div class="flexBtn">
            <van-button class="footerBtn" type="primary" @click="resourceReset"
              >重置</van-button
            >
            <van-button class="footerOk" type="info" @click="resourceOk"
              >确定</van-button
            >
          </div>
        </van-dropdown-item>
        <van-dropdown-item id="item" title="银行" ref="itemBank">
          <div class="statusStyle">
            <van-button
              style="width:90px"
              v-for="(item, index) in bankList"
              :key="index"
              class="selectBtn"
              :class="item.isSelect == true ? 'activeBtn' : 'inactiveBtn'"
              size="small"
              @click="selectedBank(index, item, bankList)"
              >{{ item.bankName }}</van-button
            >
          </div>
          <div class="flexBtn">
            <van-button
              class="footerBtn"
              type="primary"
              style="width: 100%"
              @click="bankReset"
              >重置</van-button
            >
            <van-button
              class="footerOk"
              type="info"
              style="width: 100%"
              @click="handleBankOk"
              >确定</van-button
            >
          </div>
        </van-dropdown-item>
        <van-dropdown-item
          id="item"
          title="入驻时间"
          :value="date"
          @click="show = true"
          ref="itemTime"
          :lazy-render="false"
          :close-on-click-outside="false"
        >
          <van-calendar
            v-model="show"
            v-if="showDate"
            :show-subtitle="false"
            ref="alltime"
            :poppable="false"
            type="range"
            :style="{ height: '400px' }"
            :show-confirm="false"
            :allow-same-day="true"
            color="#1989fa"
            position="top"
            @select="onSelected"
            @formatter="formatDate"
            @confirm="onConfirm"
            :min-date="minDate"
            :max-date="maxDate"
            :default-date="defaultDate"
          >
            <template slot="footer">
              <div class="flexBtn">
                <van-button class="footerBtn" type="primary" @click="timeClear"
                  >重置</van-button
                >
                <van-button
                  type="info"
                  class="footerOk"
                  @click="timeOk"
                  :disabled="timedisabled"
                  >确定</van-button
                >
              </div>
            </template>
          </van-calendar>
        </van-dropdown-item>
      </van-dropdown-menu>
      <!-- </div> -->
    </div>
    <!-- 搜索 -->
    <div v-if="open == true">
      <van-popup v-model="open" position="top" :style="{ height: '30%' }">
        <form action="/">
          <van-search
            v-model="dataList.keywords"
            show-action
            placeholder="请输入搜索关键词"
            @search="onSearch"
            @cancel="onCancel"
          />
        </form>
      </van-popup>
    </div>
    <!-- 列表 -->
    <!-- <div  class="listBox"> -->
    <van-pull-refresh
      class="scroll_wrap"
      id="pullRefresh"
      v-model="refreshing"
      @refresh="onRefresh"
    >
      <van-list
        ref="list"
        :loading="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
        class="left_list_box"
      >
        <div v-for="(item, index) in myList" :key="index">
          <van-cell class="card" @click="lookDetails(item.merchantId,item.createTime)">
            <div
              class="rightTop"
              style="background: rgba(17, 161, 224, 0.1); color: #1066dc"
              v-if="item.status == '1'"
            >
              已入驻
            </div>
            <div
              class="rightTop"
              style="background: rgba(0, 209, 223, 0.1); color: #00d1df"
              v-if="item.status == '2'"
            >
              已预约
            </div>
            <div
              class="rightTop"
              style="background: rgba(255, 150, 0, 0.1); color: #ff9600"
              v-if="item.status == '3'"
            >
              已开卡
            </div>
            <div
              class="rightTop"
              style="background: rgba(0, 166, 75, 0.1); color: #00a64b"
              v-if="item.status == '4'"
            >
              已使用
            </div>
            <div class="titleFont" >{{ item.storeName }}</div>
              <!-- <van-tag v-if="over" type="primary">已完善</van-tag>
              <van-tag v-else type="warning">待完善</van-tag> -->
            <div class="timeFont">
              入驻时间：<span>{{ item.createTime.slice(0, 10) }}</span>
            </div>
          </van-cell>
        </div>
      </van-list>
    </van-pull-refresh>
    <!-- </div> -->

    <!-- 添加商户 图标 -->
    <div class="footer">
      <div class="gotop" v-if="flag_scroll" @click="backTop">
        <img src="../assets/png/toTop.png" alt="" />
      </div>
      <div class="gotop" v-else route>
        <router-link :to="{ path: '/enterPage' }">
          <img src="../assets/png/add.png" alt=""
        /></router-link>
      </div>
    </div>
    <router-view />
    <van-tabbar
      placeholder
      safe-area-inset-bottom
      class="tab"
      v-model="active"
      route
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
  </div>
</template>
<script>
import { Html5Qrcode } from "html5-qrcode";
export default {
  data() {
    return {
      timedisabled: false, //时间确定按钮
      saoyisao: require("../assets/png/saoyisao.png"), //扫一扫按钮
      dataList: {
        startTime: "",
        endTime: "",
        page: 1,
        size: 10,
        status: [], //状态选择按钮
        bankType: [], //银行选择按钮
        keywords: "", //搜索关键词
       complete:'',//资料完善都
      },
      pageSum: null,
      show: false, //搜索弹框
      active: "index",
      refreshing: false, //刷新
      iconShouye: {
        active: require("../assets/png/shouyeActive.png"),
        inactive: require("../assets/png/shouye.png"),
      },
      iconShanghu: {
        active: require("../assets/png/shanghuActive.png"),
        inactive: require("../assets/png/shanghu.png"),
      },
      loading: false,
      finished: false,
      List: [
        { val: 1, ischeck: false, name: "已入驻" },
        { val: 2, ischeck: false, name: "已预约" },
        { val: 3, ischeck: false, name: "已开卡" },
        { val: 4, ischeck: false, name: "已使用" },
      ],
      resourceList:[
        { val: 1, ischeck: false, name: "已完善" },
        { val: 2, ischeck: false, name: "待完善" },
      ],
      open: false, //打开搜索框
      myList: [], //商户列表
      isSelected: "", //状态选择
      bankList: [], //银行list
      // 日历
      date: "",
      show: false,
      minDate: new Date(2021, 10, 1),
      defaultDate: "",
      maxDate: "",
      today: "", //今天
      scrollTop: "",
      flag_scroll: false, //滚动条
      showDate: false,
      bankSelect: false, //时间选择ok键
      // over:false,//资料是否完善    false待完善  true已完善
    };
  },

  created() {
    console.log("created")
    this.onLoad();
  },
   activated() {
     console.log(this.$route.meta,"activated");
    const pos = JSON.parse(window.sessionStorage.getItem('position'))
    document.getElementsByClassName("scroll_wrap")[0].scrollTop= pos
  },
  beforeRouteEnter(to, from, next) {
  // console.log(to,from,"详情");
    next()
  },
  beforeRouteLeave(to, from, next) {
    // 保存离开页面时的位置
     window.sessionStorage.setItem('position', JSON.stringify(this.scrollTop))
     next();
  },
  mounted() {
    this.showDate = true;
    this.getBankInfo(); //银行信息
    window.addEventListener("scroll", this.handleScroll, true);
    this.getToday(); //获取当前时间
    this.minDate = new Date(2021, 10, 1);
     console.log("mounted")
  },
  methods: {
    onLoad() {
      this.loading = true;
      let timeout = setTimeout(() => {
        this.ongetLoad();
        clearTimeout(timeout);
      }, 1000);
    },
    //获取数据
    ongetLoad() {
      let dataList = this.dataList;
      console.log(this.myList.length == this.pageSum, "panduan");
      if (this.myList.length == this.pageSum) {
        this.finished = true;
        console.log(this.myList, "this.myList88882");
      } else {
        this.$api
          .mechInfo(dataList)
          .then((res) => {
            if (res.data.status == 200) {
              this.dataList.page += 1;
              this.pageSum = res.data.data.total;
              // 如果加载完毕，显示finished
              console.log(this.myList.length, "this.myList.length");
              if (this.myList.length == 0) {
                this.myList = res.data.data.records;
              } else if (this.myList.length >= this.pageSum) {
                this.finished = true;
                console.log(this.myList, "this.myList1111");
              } else if (this.myList.length <= this.pageSum) {
                this.myList = this.myList.concat(res.data.data.records);
                this.loading = false;
                console.log(this.myList.length, "mylist9999");
              }
            }
            this.loading = false;
          })
          .catch((err) => {
            this.finished = true;
            this.loading = false;
          });
      }
    },
    //刷新
    onRefresh() {
      this.myList = [];
      this.pageSum = null;
      this.refreshing = true;
      this.dataList.page = 1;
      this.finished = false;
      // 请求信息
      this.onLoad();
      this.refreshing = false;
    },

    //查看详情
    lookDetails(val,time) {
      this.$router.push({ name: "Details", query: { id: val, code: "2",time:time } });
    },
    // 搜索
    onSearch(val) {
      this.myList = [];
      this.pageSum = null;
      console.log(val, "val");
      this.dataList.keywords = val;
      this.onRefresh();
      this.show = false;
      this.open = false;
    },
    onCancel() {
      this.open = false;
      this.show = false;
    },

    // 状态   多选按钮
    buttonSelected(index, item) {
      this.myList = [];
      this.pageSum = null;
      if (!this.dataList.status.includes(item.val)) {
        item.ischeck = true;
        this.dataList.status.push(item.val);
      } else {
        item.ischeck = false;
        var index = this.dataList.status.indexOf(item.val, 0);
        if (index > -1) {
          this.dataList.status.splice(index, 1);
        }
      }
      console.log(this.dataList.status, "this.status");
    },
      // 资料   单选按钮
    resourceSelected(index, item) {
      this.myList = [];
      this.pageSum = null;
      this.resourceList.map(res=>{
            if(res.val==item.val){
              res.ischeck=true
              this.dataList.complete=item.val
            }else{
              res.ischeck=false
            }
      })
      console.log(this.dataList.complete,"this.ziliao154")
    },
    //银行选择按钮
    selectedBank(index, item) {
      this.myList = [];
      this.pageSum = null;
      if (!this.dataList.bankType.includes(item.bankCode)) {
        item.isSelect = true;
        this.dataList.bankType.push(item.bankCode);
      } else {
        item.isSelect = false;
        var index = this.dataList.bankType.indexOf(item.bankCode, 0);
        if (index > -1) {
          this.dataList.bankType.splice(index, 1);
        }
      }
      console.log(this.dataList.bankType, "this.bankType");
    },

    //状态确认弹框
    handleOk() {
      this.myList = [];
      this.pageSum = null;
      this.loading = true;
      this.$refs.item.toggle();
      console.log(this.dataList, "确认");
      this.onRefresh();
    },
    //状态重置
    handleReset() {
      this.myList = [];
      this.pageSum = null;
      this.dataList.status = [];
      console.log(this.dataList, "cz");
      this.List.map((res) => {
        res.ischeck = false;
        console.log(res, "res");
      });
    },
   //资料确认弹框
    resourceOk() {
      this.myList = [];
      this.pageSum = null;
      this.loading = true;
      this.$refs.resourceItem.toggle();
      console.log(this.dataList, "确认");
      this.onRefresh();
    },
    //资料重置
    resourceReset() {
      this.myList = [];
      this.pageSum = null;
      this.dataList.complete ='';
      console.log(this.dataList, "资料cz");
      this.resourceList.map((res) => {
        res.ischeck = false;
        console.log(res, "资料res");
      });
    },
    //银行确认弹框
    handleBankOk() {
      this.loading = true;
      this.$refs.itemBank.toggle();
      this.onRefresh();
    },
    //银行重置
    bankReset() {
      this.myList = [];
      this.pageSum = null;
      this.dataList.bankType = [];
      this.bankList.map((res) => {
        res.isSelect = false;
      });
    },
    //获取银行数据
    getBankInfo() {
      this.$api.bankList().then((res) => {
        console.log(res.data.data);
        let bankList = res.data.data;
        bankList.map((res) => {
          res.isSelect = false;
        });
        this.bankList = bankList;
      });
    },

    //获取当前时间
    getToday() {
      let date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth();
      let day = date.getDate();
      this.maxDate = new Date(year, month, day);
      this.defaultDate = [
        new Date(year, month, day),
        new Date(year, month, day),
      ];
      let toMonth =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      console.log(this.maxDate, "new");
      let toDay = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      this.today = `${year}-${toMonth}-${toDay}`;
    },
    //时间格式化
    formatDate(date) {
      console.log(date, "date");
      if (date !== null) {
        let year = date.getFullYear();
        let month =
          date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1;
        let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        return `${year}-${month}-${day}`;
      }
    },
    //时间重置
    timeClear() {
      this.myList = [];
      this.pageSum = null;
      console.log(this.today, "today");
      let date = this.maxDate;
      this.$refs.alltime.reset(date);
      console.log(date, "new");
      //也可以用实例方法reset()
      this.dataList.startTime = this.today + " " + "00:00:00";
      this.dataList.endTime = this.today + " " + "23:59:59";
      this.timedisabled = false;
    },
    onSelected() {
      this.bankSelect = true;
      let date = this.$refs.alltime.currentDate;
      console.log(date, "pppdate");
      const [start, end] = date;
      if (start == null || end == null) {
        this.timedisabled = true;
      } else {
        this.timedisabled = false;
      }
      console.log(start, end, "pppdate22");
      this.dataList.startTime = `${this.formatDate(start)} 00:00:00`;
      this.dataList.endTime = `${this.formatDate(end)} 23:59:59`;
      console.log(this.dataList.startTime, this.dataList.endTime, "time");
    },
    //时间确认按钮
    onConfirm() {
      this.show = false;
    },
    timeOk() {
      this.myList = [];
      if (!this.bankSelect) {
        this.dataList.startTime = this.today + " " + "00:00:00";
        this.dataList.endTime = this.today + " " + "23:59:59";
      }
      this.$refs.itemTime.toggle(); //时间确认弹框   关闭日历
      this.onRefresh();
    },
    // 回到顶部
    backTop() {
      let scroll = document.getElementsByClassName("scroll_wrap")[0].scrollTop;
      // 实现滚动效果
      let speed = scroll / 5; // 每次滚动多少 （步长值）
      let timer = setInterval(() => {
        if (document.getElementsByClassName("scroll_wrap")[0].scrollTop !== 0) {
          document.getElementsByClassName("scroll_wrap")[0].scrollTop -= speed;
        } else {
          clearInterval(timer);
          this.flag_scroll = false;
        }
      }, 5);
    },
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
    //  状态选择 按钮
    handleReseted(val) {
      this.isSelected = val;
    },
    // 开始扫描
    getCameras() {
      Html5Qrcode.getCameras()
        .then((devices) => {
          console.log(22222);
          if (devices) {
            if (devices.length == 1) {
              this.cameraId = devices[0].id;
            } else {
              this.cameraId = devices[1].id;
            }
            this.start();
          }
        })
        .catch((err) => {
          this.$notify({
            type: "warning",
            message: "调用摄像头失败 : " + err,
          });
        });
    },
    // 启动相机
    start() {
      this.isScan = false;
      this.html5QrCode = new Html5Qrcode("qr-reader");
      this.html5QrCode
        .start(
          this.cameraId,
          {
            fps: 10,
            qrbox: { width: 250, height: 250 },
          },
          (qrCodeMessage) => {
            if (qrCodeMessage) {
              this.stop();
            }
          }
        )
        .catch((err) => {
          console.log(`Unable to start scanning, error: ${err}`);
        });
    },
    // 关闭相机
    stop() {
      this.isScan = true;
      this.html5QrCode
        .stop()
        .then((ignore) => {
          console.log("QR Code scanning stopped.");
        })
        .catch((err) => {
          console.log("Unable to stop scanning.");
        });
    },
  },
};
</script>

<style scoped>
.enter {
  color: rgba(38, 38, 38, 1);
  height: 100%;
  overflow: hidden;
  /* text-align: center; */
  /* min-height: 100%; */
}
.topBox {
  position: fixed;
  top: 0;
  height: 120px;
  width: 100%;
  z-index: 10;
  /* border: 1px solid red; */
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
  /* margin-top: 5px; */
}
/* 搜索键 */
/deep/ .van-icon-search::before {
  content: "\F0AF";
  font-size: 18px;
  color: rgba(38, 38, 38, 1);
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

.statusStyle {
  position: relative;
  padding: 5px 5px 5px 10px;
}
/* button 选中状态和未选中状态 */
.selectBtn {
  margin-right: 3px;
  margin-bottom: 10px;
  border-radius: 8px;
}
.activeBtn {
  width: 55px;
  background: rgba(52, 127, 241, 1);
  border: 1px solid rgba(52, 127, 241, 1);
  color: #fff;
}
.inactiveBtn {
  width: 55px;
  border: 1px solid #d4d4d4;
  background: #d4d4d4;
  color: #262626;
}
.flexBtn {
  display: flex;
  margin-top: 30px;
}
.footerBtn {
  margin-right: 0px !important;
  background: #fff;
  color: #262626;
  border: 1px solid #fff;
  width: 100%;
}
.footerOk {
  background: rgba(52, 127, 241, 1);
  border: 1px solid rgba(52, 127, 241, 1);
  color: #fff;
  width: 100%;
}
/* 添加按钮    回到顶部 */
.footer .gotop {
  position: fixed;
  right: 10px;
  bottom: 80px;
  cursor: pointer;
  padding: 13px;
  border-radius: 50%;
  color: #fff;
  text-align: center;
  width: 60px;
  height: 60px;
  background: linear-gradient(180deg, #347ff1 0%, #6ca1f2 100%);
  box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.22);
}
.gotop:active {
  background: linear-gradient(180deg, #8db4ee 0%, #6ca1f2 100%);
}
/* 日历页脚 */
/deep/ .van-calendar__footer {
  padding: 0;
}
/* 顶部导航 */
.tab {
  padding: 10px 0;
  border-top: 5px solid rgb(236, 236, 236);
}
.tabbar-icon {
  width: 24px;
  height: 22px !important;
}
.tabbar {
  font-size: 14px;
}
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

/* 不显示日历标题 */
/deep/ .van-calendar__header-title {
  font-size: 16px;
  display: none;
}
/deep/ .van-calendar__day--start-end {
  color: #fff;
  background-color: rgba(52, 127, 241, 1);
}

/* 固定列表滚动位置 */
.scroll_wrap {
  height: calc(99.5vh - 22.5vh);
  margin-top: 95px;
  overflow: scroll;
  /* position:static; */
  -webkit-overflow-scrolling: touch; /* ios5+ */
  z-index: 9;
}
.card {
  margin-top: 10px;
  /* position: absolute; */
  padding: 0px 0px 20px 20px;
  background: #fff;
  border-radius: 15px;
  box-shadow: 0px 0px 8px 0px rgba(81, 82, 84, 0.08);
}
.left_list_box {
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 190px);
  /* min-height:calc(100% + 1px) */
}

.selectBox {
  position: relative;
  left: 0;
  right: 0;
  z-index: 1;
}

/deep/ .van-calendar__popup.van-popup--bottom,
.van-calendar__popup.van-popup--top {
  height: 70% !important;
}
/deep/ .van-popup {
  position: relative;
  max-height: 104%;
  /* overflow-y: auto; */
  /* background-color: #fff; */
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
