// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";
import axios from "axios";
import api from "./request/api";
import md5 from "js-md5";
// import MuseUI from 'muse-ui';
import 'muse-ui/dist/muse-ui.css';
import 'amfe-flexible/index.js'
import BaiduMap from 'vue-baidu-map'

Vue.use(BaiduMap, {
  // ak 是在百度地图开发者平台申请的密钥 
  ak: 'Oxeddd2FrQwmsf0vzIPoSh8jWcAx09Dc'
})

// Vue.use(MuseUI);
// import '@vant/touch-emulator';
import {
  DatetimePicker,
  Tag,
  Image as VanImage,
  NavBar,
  PullRefresh,
  Search,
  Icon,
  Sticky,
  Tabbar,
  Collapse,
  CollapseItem,
  TabbarItem,
  Calendar,
  CheckboxGroup,
  Radio,
  RadioGroup,
  Switch,
  Field,
  Grid,
  GridItem,
  Col,
  Row,
  Button,
  ActionSheet,
  Notify,
  Dialog,
  Cell,
  CellGroup,
  DropdownMenu,
  DropdownItem,
  Card,
  List,
  Tab,
  Tabs,
  Divider,
  NoticeBar,
  Toast,
  Popup,
  Overlay,
  NumberKeyboard,
  Form,
  Uploader,
  Picker,
  Area,
  Loading
} from "vant";

Vue.prototype.$axios = axios;

Vue.prototype.$md5 = md5;
//定义全局变量
Vue.prototype.$api = api;

Toast.setDefaultOptions({ duration: 3000 });

//自动给同一个vue项目的所有请求添加请求头
axios.interceptors.request.use(function(config) {
  let token = localStorage.getItem("token");
  console.log(config.url,"config")
  if (config.url == "/auth/login") {
    delete config.headers["Authorization"]
    return config;
  } else {
    if (token) {
        config.headers["Authorization"] = "Bearer " + token;
      // console.log(config.headers.Authorization,"config.headers")
    }
    return config;
  }
});
// console.log(token ,"2211")

//设置axios请求头加入token
axios.interceptors.request.use(config => {
  if (config.push == '/') {
   } else {
    if (localStorage.getItem('token')) {
      //在请求头加入token，名字要和后端接收请求头的token名字一样
      config.headers.token=localStorage.getItem('token');
    }
   }
    return config;
  },
  error => {
   return Promise.reject(error);
  });


  // response interceptor（接收拦截器）
axios.interceptors.response.use(function (response) {
  // let response = error.response;
  const status = response.data.status;
  console.log(status,"status6666")
  if (status == 402) {
      // 判断状态码是402 跳转到登录
      router.replace({ path: "/" });
      window.localStorage.removeItem('token'); //清除文件
      Toast(response.data.message);
  }
  return response
});



Vue.use(PullRefresh);
Vue.use(Search);
Vue.use(Collapse);
Vue.use(CollapseItem);
Vue.use(Calendar);
Vue.use(Sticky);
Vue.use(Icon);
Vue.use(Switch);
Vue.use(Card);
Vue.use(CheckboxGroup);
Vue.use(RadioGroup);
Vue.use(Radio);
Vue.use(TabbarItem);
Vue.use(VanImage);
Vue.use(Loading);
Vue.use(Area);
Vue.use(Tabbar);
Vue.use(Picker);
Vue.use(Uploader);
Vue.use(Form);
Vue.use(NumberKeyboard);
Vue.use(Overlay);
Vue.use(Popup);
Vue.use(Toast);
Vue.use(Divider);
Vue.use(NoticeBar);
Vue.use(Tab);
Vue.use(Tabs);
Vue.use(List);
Vue.use(Cell);
Vue.use(CellGroup);
Vue.use(Dialog);
Vue.use(ActionSheet);
Vue.use(NavBar);
Vue.use(Field);
Vue.use(Grid);
Vue.use(GridItem);
Vue.use(Col);
Vue.use(Row);
Vue.use(Button);
Vue.use(Notify);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Tag);
Vue.use(DatetimePicker );

Vue.config.productionTip = false;

Vue.filter("dateinfo", dataStr => {
  var time = new Date(dataStr);
  function timeAdd0(str) {
    if (str < 10) {
      str = "0" + str;
    }
    return str;
  }
  var y = time.getFullYear();
  var m = time.getMonth() + 1;
  var d = time.getDate();
  var h = time.getHours();
  var mm = time.getMinutes();
  var s = time.getSeconds();
  return (
    y +
    "-" +
    timeAdd0(m) +
    "-" +
    timeAdd0(d) +
    " " +
    timeAdd0(h) +
    ":" +
    timeAdd0(mm) +
    ":" +
    timeAdd0(s)
  );
});

//时间过滤器
Vue.filter("dateFormat", dataStr => {
  let str = dataStr.toString();
  let a = str.substring(0, 4);
  let b = str.substr(4, 2);
  let c = str.substr(6, 2);
  let d = str.substr(8, 2);
  let e = str.substr(10, 2);
  let f = str.substr(12, 2);
  return a + "-" + b + "-" + c + " " + d + ":" + e + ":" + f;
  // console.log(a+"-"+b +"-"+c +" " +d +":" +e +":" +f)
  // var time = new Date(dataStr);
  // function timeAdd0(str) {
  //     if (str < 10) {
  //         str = '0' + str;
  //     }
  //     return str
  // }
  // var y = time.getFullYear();
  // var m = time.getMonth() + 1;
  // var d = time.getDate();
  // var h = time.getHours();
  // var mm = time.getMinutes();
  // var s = time.getSeconds();
  // return y + '-' + timeAdd0(m) + '-' + timeAdd0(d) + ' ' + timeAdd0(h) + ':' + timeAdd0(mm) + ':' + timeAdd0(s);
});

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  components: { App },
  template: "<App/>"
});
