<template>
  <div class="hello">
    <!-- <img
      class="bg"
      style="width: 100%; min-height: 100%"
      src="../assets/png/login.png"
      alt=""
    /> -->
    <!-- <img  style="width:100%;min-height:100%" src="../assets/png/login.png" alt=""> -->

    <div class="out" >
      <div class="img">
        <img :src="logo" alt="" />
      </div>
      <h2 class="logoFont">蓝色火焰CRM系统</h2>

      <div class="int">
        <van-form class="form" @submit="onSubmit" ref="loginForm">
          <!-- <van-cell-group> -->
          <van-field
          
            class="cad"
            name="username"
            :border="false"
            v-model="loginForm.username"
            placeholder="请输入账号"
            clearable
            @input="checked"
            :rules="[{ required: true, trigger: 'onBlur' }]"
          />
          <van-field
            class="cad"
            :border="false"
            name="password"
            style="border-bottom: 1px solid rgba(221, 221, 221, 0.2)"
            v-model="loginForm.password"
            :type="password_eye ? 'text' : 'password'"
            placeholder="请输入密码"
            clearable
            :rules="[{ required: true, trigger: 'onBlur' }]"
            @blur="iptblur"
            @input="checked"
            @keyup="throttle"
            
            :right-icon="password_eye ? 'eye-o' : 'closed-eye'"
            @click-right-icon="password_fn"
          />
          <!-- <van-field v-if="isNot" :placeholder="msg" class="msgcalss">

            </van-field> -->
          <div v-if="isNot" class="msgclass">{{ msg }}</div>
          <!-- </van-cell-group> -->
          <div style="margin-top: 16px">
            <van-button round block native-type="submit" class="loginBtn" :class="btnBackground==true?'loginBtnActive':''"
              >登录</van-button
            >
          </div>
        </van-form>
      </div>
    </div>

    <div class="footer"></div>
  </div>
</template>

<script>
import { mapMutations } from "vuex";
import axios from "axios";
export default {
  name: "login",
  data() {
    // 校验函数返回 true 表示校验通过，false 表示不通过
    const validator = val => /^[A-Za-z0-9]{6}$/.test(val);
    // const validatorPassword=(val)=>/^[A-Za-z0-9]{6}$/.test(val)
    return {
      pattern: /^[A-Za-z0-9]{6}$/,
      loginForm: {
        username: "", //账号
        password: "" //密码
      },
      logo:require("../assets/png/logo.png"),//logo图片
      isNot: false,
      msg: "",
      visible: "closed-eye", //密码眼睛打开
      invisible: "eye-o", //密码眼睛
      password_eye: false,
      // hideshow: true, //显示或者隐藏footer
      docmHeight: document.documentElement.clientHeight, //默认屏幕高度
      showHeight: document.documentElement.clientHeight, //实时屏幕高度
      isResize: false,
      hidshow: true,
      loading: false, //登录button
      userToken: "",//
      btnBackground:false,//登录按钮颜色
    };
  },

  mounted() {
    
    // window.onresize = () => {
    //   return (() => {
    //     if (!this.isResize) {
    //       //默认屏幕高度
    //       this.docmHeight = document.documentElement.clientHeight;
    //       this.isResize = true;
    //     }
    //     //实时屏幕高度
    //     this.showHeight = document.body.clientHeight;
    //     console.log(this.showHeight);
    //   })();
    // }
  },
  created() {
    // this.font = 30;
    // this.$router.replace({path:"/",query:{"td_channelid":"meitu"}})
    window.addEventListener("pageshow", function(e) {
      //如果检测到页面是从“往返缓存”中读取的，刷新页面
      if (e.persisted) {
        window.location.reload();
      }
    });
  },

  watch: {
  //   showHeight() {
  //     if (this.docmHeight >= this.showHeight) {
  //       this.hidshow = false;
  //     } else {
  //       this.hidshow = true;
  //     }
  //     console.log(this.hidshow);
  //   },
  //   // showMsg(){
  //   //    if(this.loginForm.username==''||this.loginForm.password==''){
  //   //      this.msg=""
  //   //    }
  //   // }
  },
  methods: {
    // 用户名 密码登录
    onSubmit(value) {
      this.isNot = false;
      console.log(value, "表单e");
      let params = new URLSearchParams();
      params.append("username", this.loginForm.username);
      params.append("password", this.loginForm.password);
    
       if (this.loginForm.username == "" || this.loginForm.password == "") {
        this.isNot = true;
        this.msg = "账号或密码不能为空";
      } else {
        this.btnBackground=true
        console.log("00000");
        // if(nowToken!==''||nowToken!==null){
        //   this.$router.push("/index"); //首页
        // }else{
         this.$api.login(params,{headers: { "Content-Type": "application/x-www-form-urlencoded" }}).then(res => {
          console.log(res.data.status, "res.status");
          if (res.data.status == 200) {
            this.isNot = false;
            const token = res.data.data.token;
            // localStorage.setItem('username',this.ruleForm.userName);
            // if(token)
  //获取并存储服务器返回的token信息
  localStorage.setItem('token',token);
            // window.localStorage.setItem("token", token);
            this.$router.push("/index"); //首页
            console.log(token, "token");
          }else{
            this.isNot = true;
            this.msg=res.data.message
            console.log(this.msg,"this.msg")
          }
        })
  //  }
      }
    },
    // 密码眼睛
    password_fn() {
      this.password_eye = !this.password_eye;
    },

    //输入框内容发生变化时  msg为空
    checked(){
      this.msg=''
      if(this.loginForm.username!==''&&this.loginForm.password!==''){
         this.btnBackground=true
       }else{
         this.btnBackground=false
       }
    },
    iptblur() {
      //文本框失去焦点时
      let that = this;
      setTimeout(function() {
        that.state = false;

      }, 100);
    },
    //文本框@keyup事件
    //节流函数
    throttle() {
      //保持this的指向始终指向vue实例
      var that = this;
      if (!that.statu) {
        return;
      }
      that.statu = false;
      setTimeout(function() {
        that.search();
        that.statu = true;
      }, 100);
    },

onKeyDown(e) {
    e.preventDefault()
    // 键盘按键判断:左箭头 37;上箭头 38；右箭头 39;下箭头 40
    if(e.keyCode == 38 || e.keyCode == 40 || e.keyCode == 37 || e.keyCode == 39){ 
	    if(e.preventDefault){
		    e.preventDefault();
	    }else{
		    e.returnValue = false;
	    }
    }

  }
  
  // api.send(params).then(res=>{
  }
  // })
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* 全局 */
.hello {
  position: fixed;
  top:0;
  left:0;
  right: 0;
  bottom: 0;
  width: 100%;
  /* min-height: 100% !important; */
  /* text-align: cover; */
  background: url("../assets/png/login.png") no-repeat;
  background-size:100% 100%;
  /* z-index: 100; */
}
.out{
  position: relative;
  height: 100%;
  overflow-y: auto;
  width: 100%;
}
.bg {
  background-size: 100% 100%;
  position: absolute;
  /* top: 0; */
  left: 0;
  right: 0;
  /* bottom: 0; */
  z-index: 0;
}
/* logo*/
.img {
  text-align: center;
  vertical-align: middle;
  z-index: 1;
  position: relative;
  top:160px;
  bottom: 0;
}
/* 公司名称 */
.logoFont {
  position: relative;
  vertical-align: middle;
  margin-bottom: 20px ;
  font-size: 24px;
  font-weight: 600;
  /* z-index: 1; */
  top: 140px;
  text-align:center ;
  bottom: 0;
}
/* 登录按钮 */
.loginBtn {
  background: rgba(52, 127, 241, 0.3);
  color: #fff;
  margin-top: 50px;
  font-size: 20px;
  /* z-index: 1; */
}
.loginBtnActive{
  background: rgba(52, 127, 241, 1);
}
/deep/ .van-field__control {
  padding: 5px !important;
}
/* 密码眼睛 */
/deep/ .van-icon-closed-eye::before {
  content: "\F035";
  font-size: 20px;
}
/deep/ .van-icon-eye-o::before {
  content: "\F054";
  font-size: 20px;
}
/* 输入框 */
.cad {
  line-height: 1rem;
  font-size: 16px;
  z-index: 1;
  background: #fff;
}
.cad:first-child {
  border-bottom: 1px solid rgba(221, 221, 221, 0.3) !important;
  /* padding:10px; */
}
.cad:last-child {
  border-bottom: 1px solid rgba(221, 221, 221, 0.2) !important;
}
.int {
  position: relative;
  left: 0;
  right: 0;
  top:120px;
  /* top: 100px; */
  /* bottom:20px; */
  z-index: 3;
  padding: 3px 20px;
}

.msgclass {
  padding: 8px 0 0 20px;
  color: red;
  font-size: 14px;
  float: left;
}
</style>
