<template>
  <div class="map-maker-wrapper" v-if="stopSlideFlag">
    <div class="button">
      <van-button
        size="small"
        class="btn1"
        icon="arrow-left"
        type="info"
        @click="returnPage"
        >返回</van-button
      >
      <span style="font-weight: bold">选择地址</span>
      <van-button size="small" class="btn2" type="info" @click="handleSubmit"
        >确定</van-button
      >
    </div>
    <baidu-map
      class="bm-view"
      :ak="ak"
      :center="mapCenter"
      :zoom="mapZoom"
      :scroll-wheel-zoom="true"
      @ready="onReady"
      @click="clickBmap"
    >
      <bm-navigation anchor="BMAP_ANCHOR_BOTTOM_LEFT"></bm-navigation>
      <bm-geolocation
        anchor="BMAP_ANCHOR_BOTTOM_RIGHT"
        :showAddressBar="true"
        :autoLocation="true"
      ></bm-geolocation>
      <bm-marker
        :position="point"
        :dragging="true"
        animation="BMAP_ANIMATION_BOUNCE"
      ></bm-marker>
    </baidu-map>
    <van-search
      v-model="keyword"
      placeholder="搜索地点"
      input-align="left"
      shape="round"
      @input="handleSearch"
    />

    <!-- <div class="search">
        <input class="search-input" type="text" v-model="keyword" />
        <span class="search-btn" @click="handleSearch">搜索</span>
      </div> -->
    <!-- 检索结果 -->
    <div class="search-result">
      <div
        v-for="(item, index) in searchResult"
        class="item"
        :key="index"
        @click="handleSelect(item)"
      >
        <div>
          <div class="title">{{ item.title }}</div>
          <div class="address">{{ item.address }}</div>
        </div>
        <div>
          <span v-if="item.show" class="gou" style="color: #347ff1"></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Toast } from "vant";
// import Header from '@/components/Header'
import BaiduMap from "vue-baidu-map/components/map/Map.vue";
// import { BmLocalSearch } from 'vue-baidu-map'
const defaultInfo = {
  lng: 0,
  lat: 0,
  addressStr: "",
  title: "",
  province: "", // 省
  city: "", // 市
  district: "", // 区
};
export default {
  name: "MapMaker",
  components: {
    // Header,
    BaiduMap,
  },
  props: {
    addressInfo: {
      default() {
        return [];
      },
    },
  },

  data() {
    return {
      stopSlideFlag: true, //禁止页面滚动
      // ak: "EE1O1pxjgabzULGQD7P1cvOB95GeE2YW", //wnn
      ak: "Oxeddd2FrQwmsf0vzIPoSh8jWcAx09Dc",//公司
      BMap: null,
      map: null,
      mapZoom: 19,
      mapCenter: { lng: 104.07274727, lat: 30.57899372 },
      keyword: "", //搜索关键词
      searchResult: [], // 检索结果列表
      isSelect: false, //是否选择
      point: { lng: 0, lat: 0 },
      myGeo: null,
      address: "", //地点
    };
  },
  created() {},

  mounted() {
    // this.$router.afterEach((to, from, next) => {
    //   window.scrollTo(0, 0);
    // });
    // this.monitoring(); // 注册监听事件
    this.address = this.addressInfo;
    // console.log(this.address,"this.addressInfo")
  },
  methods: {
    onReady({ BMap, map }) {
      this.BMap = BMap;
      this.map = map;
      let _this = this;
      _this.mapZoom = 19;
      // console.log(_this.address, " _this.address55");
      // 获取自动定位方法
      var geolocation = new BMap.Geolocation();
      // console.log(geolocation.SW,"sw")
      // console.log(geolocation.getCurrentPosition(),"12")
      // 获取逆解析方法实例
      _this.myGeo = new BMap.Geocoder();
      // 获取自动定位获取的坐标信息
      geolocation.getCurrentPosition(function (r) {
        // console.log(r, "r");
        let po = r.point;
        if (_this.address) {
          _this.myGeo.getPoint(_this.address, function (point) {
            console.log(point, "chengdu");
            po = point;
          });
        }
        let geoc = new _this.BMap.Geocoder();
        geoc.getLocation(po, function (res) {
          // console.log(res, "res");
          map.centerAndZoom(po, 19); //重新设置中心点
          _this.map.clearOverlays(); //清除地图上所有覆盖物F
          _this.map.addOverlay(new BMap.Marker(po));
          _this.mapZoom = 18;
          _this.searchResult = res.surroundingPois;
        });
      });
    },

    //输入框 搜索地点
    handleSearch() {
      let self = this;
      console.log("111111111");
      let local = new this.BMap.LocalSearch(this.map, {
        renderOptions: {
          map: this.map,
          selectFirstResult: true,
        },
        onSearchComplete: function (res) {
          // console.log(res, "results");
          if (res && res.Kr) {
            self.searchResult = [...res.Kr];
          }
        },
      });
      local.search(self.keyword);
    },

    // 选择结果列表
    handleSelect(item) {
      let self = this;
      console.log("item", item);
      let title = item.title;
      let { lng, lat } = item.point;
      // console.log(title, "title");
      self.keyword = title;
      self.point = item.point;
      let geoc = new this.BMap.Geocoder();
      geoc.getLocation(self.point, function (res) {
        console.log("res111", res);
        // console.log(res.addressComponents.street == true, "panduan");
        // let street = res.addressComponents.street;//街道
        // let streetNumber = res.addressComponents.streetNumber;//号
        // if (street || streetNumber) {
        //   self.address = street + streetNumber + "-" + title;
        // } else {
        //   self.address = title;
        // }

        self.map.clearOverlays(); //清除地图上所有覆盖物
        self.map.addOverlay(new self.BMap.Marker({ lng, lat }));
        self.mapCenter.lng = lng;
        self.mapCenter.lat = lat;
        self.mapZoom = 15;
        res.surroundingPois.map((res) => {
          console.log(res,"title")
          // console.log(self.keyword,"self.keyword")
          if (self.keyword == res.title) {
            res.show = true;
          } else {
            res.show = false;
          }
        });
        self.searchResult = res.surroundingPois;

        if (res.surroundingPois.length > 0) {
          self.address =
            res.address + "-" +title;
          // self.searchResult[0].show = true;
        } else {
          // self.searchResult.show = false;
          self.address = res.address;
        }
        console.log(res.surroundingPois, "地址");
        console.log(self.address, "拼接地址");
      });
    },
    //确定
    handleSubmit() {
      let _this = this;
      console.log(_this.address, "_this.address2");
      let data = {
        point: _this.point,
        address: _this.address,
        enter: false,
      };
      this.$emit("buttonEvent", data);
    },
    clickBmap({ type, target, point, pixel, overlay }) {
      const _this = this;
      _this.mapZoom = 18;
      _this.map.centerAndZoom(point, 17); //重新设置中心点
      _this.map.clearOverlays(); //清除地图上所有覆盖物
      _this.map.addOverlay(new _this.BMap.Marker(point));
      // 根据坐标逆解析获取地址详细描述
      this.myGeo.getLocation(point, function (result) {
        if (result) {
          _this.searchResult = result.surroundingPois;
          console.log(_this.address, "地址地址");
        }
      });
    },
    onClick(e) {
      console.log(e, "选择");
    },
    // 返回
    returnPage() {
      this.$emit("buttonEvent", "1");
    },
  },
};
</script>

<style  scoped>
.map-maker-wrapper {
  position: absolute;
  height: 100%;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}
.btn-confrim {
  width: 120px;
  height: 56px;
  line-height: 56px;
  background-color: #5ac9d4;
  border-radius: 8px;
  color: #ffffff;
  text-align: center;
}
.bm-view {
  width: 100%;
  height: 56.9vh;
  /* z-index: -1; */
  background: #fff;
}
/* 去除百度地图标志 */
/deep/ .bm-view .anchorBL {
  display: none;
}
.search-result {
  background-color: #fff;
  padding: 2px 15px;
  overflow: scroll;
  width: 100%;
  height: 35vh;
}
.item {
  border-bottom: 1px solid #ebeef2;
  padding: 10px 0;
  display: flex;
  /* max-height: 320px; */
  justify-content: space-between;
}
.item:last-child {
  border-bottom: none !important;
}
.item:first-child {
  border-top: none !important;
}
.title {
  font-size: 14px;
  /* font-weight: 600; */
  color: #313233;
}
.address {
  font-size: 12px;
  /* font-weight: 400; */
  color: #9ca5b3;
  /* margin-top: 8px; */
}
.button {
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 20px 10px;
  z-index: 999;
  align-items: center;
  font-size: 14px;
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0));
  /* opacity: 0.1 */
}
.btn1 {
  background: transparent !important;
  border: none !important;
  border-radius: 5px;
  padding: 0 15px;
}
.btn2 {
  border-radius: 5px;
  padding: 0 15px;
}
.icon {
  text-align: center;
  vertical-align: middle;
  align-items: center;
  padding: 16px 0;
}
.gou {
  background: url("../assets/png/g.png") center no-repeat;
  width: 25px;
  height: 25px;
  display: inline-block;
}
</style>
