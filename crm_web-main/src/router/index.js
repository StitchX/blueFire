import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login'
import index from "@/components/index"
import merchant from "@/components/merchant"
import enterPage from "@/components/enterPage"
import enterOne from "@/components/enterOne"
import search from "@/components/search"
import enterFished from "@/components/enterFished"
import details from "@/components/details"
import baiduMap from "@/components/baiduMap.vue"




Vue.use(Router)



const router= new Router({
    routes: [     
        
        {
            path: '/',
            name: 'Login',
            component: login,
            meta:{
                needLogin:true,//需要加校检判断的路由
                title:"蓝色火焰CRM系统"
            }
        },
        {
            path: '/index',
            name: 'Index',
            component: index,
            meta:{
                keepAlive:true,
                title:"首页"
            }
        },
        {
            path: '/merchant',
            name: 'Merchant',
            component: merchant,
            meta:{
                title:"商户列表",
                keepAlive: true,
            } 
        },{path: '/details',//以“/”开头的嵌套路径会被当作根路径，所以子路由上不用加“/”;在生成路由时，主路由上的path会被自动添加到子路由之前，所以子路由上的path不用在重新声明主路由上的path了。
            name: 'Details',
            component: details,
            meta:{
                // keepAlive:true,
                title:"商户详情"
            },
        },
        {
            path: '/enterPage',
            name: 'EnterPage',
            component: enterPage,
            meta:{
                // keepAlive:true,
                title:"放心码入驻"
            }
        },
        {
            path: '/enterOne',
            name: 'EnterOne',
            component: enterOne,
            meta:{
                // keepAlive:true,
                title:"放心码入驻"
            }
        },
        {
            path: '/enterFished',
            name: 'EnterFished',
            component: enterFished,
            meta:{
                title:"放心码入驻"
            }
        }, 
        {
            path: '/search',
            name: 'Search',
            component: search,
            meta:{
                title:"扫一扫", 
            }
        },
        {
            path: '/baiduMap',
            name: 'BaiduMap',
            component: baiduMap,
            meta:{
                title:"地图"
            }
        },
        
    ]
});
// 导航守卫
// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
    console.log(to,'登')  
    //to到哪儿 from从哪儿离开 next跳转 为空就是放行 
if (to.name=='Login') {
     //如果跳转为登录，就放行 
     console.log(to,'登录chenggong1')  
     if(localStorage.getItem('token')){
        console.log("to2  from2")
        next({name:'Index'}); 
     }else{
         next()
     }
}else{    
     //取出localStorage判断
      let token = localStorage.getItem('token');   
     console.log(token,"Token222")
      if (token==null||token=='') {  
        next({name:'Login'}); 
       } else {
        next()
         console.log('登录chenggong')  
       } 
    }});
    
// 导航守卫
// 在组件实例中，来判断路由是从哪里跳转的，如果是从详情页跳转的，则将当前路由对象的meta.isBack 设置为true,否则设为false
// router.beforeEach((to, from, next) => {
//     //to到哪儿 from从哪儿离开 next跳转 为空就是放行 
//    if(from.path=='/details'){
//     to.meta.isBack=true;
//     }else{
//         to.meta.isBack=false;
//     }
//     next();
// });
 

 
// router.beforeEach ((to, from, next)=> {
//     //跳转到首页页面时，Index为搜索页面名称
//         if (to.name == 'Index') {
//             to.meta.isUseCache = true;
//         }
//         next();
//     });
//     router.beforeEach ((to, from, next)=> {
//         //跳转到首页页面时，Index为搜索页面名称
//             if (to.name == 'Merchant') {
//                 to.meta.isUseCache = true;
//             }
//             next();
//         });
  export default router;


  