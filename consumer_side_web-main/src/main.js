import Vue from "vue";
import App from "./App.vue";
import router from "./router";

import { Toast,Lazyload,Swipe, SwipeItem,Button,Tab,Tabs,NavBar,Dialog,NumberKeyboard,Icon,Image as VanImage,ActionSheet,Field,Uploader,Cell, CellGroup,List  } from 'vant'

Vue.use(Toast);
Vue.use(Swipe);
Vue.use(SwipeItem);
Vue.use(Button);
Vue.use(Tab);
Vue.use(Tabs);
Vue.use(NavBar);
Vue.use(Dialog);
Vue.use(NumberKeyboard);
Vue.use(Icon);
Vue.use(VanImage);
Vue.use(ActionSheet);
Vue.use(Field);
Vue.use(Uploader);
Vue.use(Cell);
Vue.use(CellGroup);
Vue.use(Lazyload);
Vue.use(List);

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})


Vue.config.productionTip = false;

Vue.directive('title', {
  inserted: function (el, binding) {
    document.title = el.dataset.title
  }
})

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
