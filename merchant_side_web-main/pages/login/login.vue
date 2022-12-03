<template>
  <view class="loginBox">
  <!-- 开发环境切换按钮 -->
    <view class="model" v-if="false">
      <u-icon size="30" name="more-dot-fill" @click="show = true"></u-icon>
    </view>
    <u-popup :show="show" mode="center">
      <view class="box">
        <button @click="dev">开发环境</button>
        <button @click="test">测试环境</button>
      </view>
    </u-popup>
  <!--  -->
    <u-toast ref="uToast"></u-toast>
    <view class="topTitle">
      <view class="text"> 手机号登录 </view>
      <view class="blueLine"></view>
    </view>
    <view class="formContent">
      <u--form labelPosition="left" :model="loginForm">
        <u-form-item prop="loginForm.phone">
          <u-input
            :focus="true"
            :custom-style="u_inputStyle"
            v-model="loginForm.phone"
            placeholderStyle="font-weight:400"
            placeholder="请输入手机号"
            type="number"
            @blur="checkInput"
          >
            <u--text text="+86" slot="prefix" margin="0 3px 0 0"></u--text>
          </u-input>
        </u-form-item>
        <u-form-item prop="loginForm.code">
          <view class="codeContent">
            <u-input
              :readonly="inputCodeDis"
              v-model="loginForm.code"
              placeholderStyle="font-weight:400"
              :placeholder="inputCodeDis ? '' : '请输入验证码'"
              type="number"
            ></u-input>
            <button
              :class="activeStyle"
              @tap="$u.throttle(getCodeSubmit, 2000)"
            >
              {{ text }}
            </button>
          </view>
        </u-form-item>
        <u-form-item prop="loginForm.readPrivacy">
          <u-checkbox-group v-model="loginForm.readPrivacy">
            <u-checkbox
              name="privacy"
              label="我已阅读并同意"
              shape="circle"
            ></u-checkbox>
            <a url="/pages/login/privacy/privacy">《隐私政策》</a>
          </u-checkbox-group>
        </u-form-item>
      </u--form>
    </view>
    <view class="loginBt">
      <button
        :class="activeSub"
        :disabled="disabled"
        @tap="$u.throttle(loginSubmit, 700)"
      >
        登录
      </button>
    </view>
  </view>
</template>

<script>
import { fetchCode, login } from "@/config/api.js";
export default {
  data() {
    return {
      u_inputStyle: {
        height: "30px",
      },
      activeSub: "submit",
      //登录表单
      loginForm: {
        phone: "",
        code: "",
        readPrivacy: "",
      },
      activeStyle: "unClickSty", //倒计时按钮样式
      text: "获取验证码", // 验证码文字
      show: false,
      disabled: true,
      inputCodeDis: true,
    };
  },
  onLoad() {},
  onShow() {
    wx.hideHomeButton(); //隐藏小程序自带的返回首页按钮
  },
  watch: {
    loginForm: {
      handler(val) {
        if (val.phone) {
          this.disabled = false;
        } else {
          this.disabled = true;
        }
      },
      deep: true,
    },
    disabled: {
      handler(val) {
        if (val) {
          this.activeSub = "submit";
        } else {
          this.activeSub = "actSubmit";
        }
      },
    },
  },
  methods: {
    dev() {
      this.show = false;
      let url = "https://businessdev.supervision.bluefire.top";
      uni.$u.http.config.baseURL = url;
      uni.showToast({
        title: "已切换开发环境",
        duration: 2000,
        icon: "error",
      });
      console.log(uni.$u.http.config.baseURL);
    },
    test() {
      this.show = false;
      let url = "https://businesstest.supervision.bluefire.top";
      uni.$u.http.config.baseURL = url;
      uni.showToast({
        title: "已切换测试环境",
        duration: 2000,
        icon: "error",
      });
      console.log(uni.$u.http.config.baseURL);
    },
    focusNum() {
      // console.log(this.$refs.numberInput)
      this.$nextTick(() => {
        let dom = this.$refs.numberInput;
        console.log(dom)
      });
    },
    //登录校验消息弹框
    toastShow(parmas) {
      this.$refs.uToast.show({
        type: parmas.type,
        duration: "2000",
        message: parmas.message,
        iconUrl: `https://cdn.uviewui.com/uview/demo/toast/${parmas.type}.png`,
      });
    },
    // 登录提交验证
    async loginSubmit() {
      this.disabled = true;
      this.timeDis = true;
      if (this.loginForm.code == "") {
        this.toastShow({
          message: "请输入验证码",
          type: "error",
        });
        return;
      }
      if (this.loginForm.readPrivacy == "") {
        this.toastShow({
          message: "请阅读隐私政策并勾选",
          type: "error",
        });
        return;
      }

      uni.showLoading({
        title: "登录中..",
      });
      let parmas = {
        phone: this.loginForm.phone,
        smsCode: this.loginForm.code,
      };
      await login(parmas)
        .then((res) => {
          uni.setStorageSync("token", res.data);
          setTimeout(function () {
            uni.hideLoading();
            uni.switchTab({
              url: "/pages/home/home",
            });
            uni.setStorageSync("phone",this.loginForm.phone)
            this.disabled = false;
          }, 2000);
        })
        .catch((err) => {
          this.disabled = false;
        });
    },
    // 电话号码格式校验
    checkInput() {
      var myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if (!myreg.test(this.loginForm.phone)) {
        this.toastShow({
          message: "请输入11位的电话号码",
          type: "error",
        });
        return false;
      } else {
        return true;
      }
    },
    // 获取验证码
    getCodeSubmit() {
      // --------------接口预留------------//
      if (!this.checkInput()) {
        return;
      }
      uni.showLoading({
        title:"验证码发送中...."
      })
      const parmas = {
        phone: this.loginForm.phone,
      };
      fetchCode(parmas)
        .then((res) => {
          if (res.status === "200") {
            this.inputCodeDis = false;
            uni.hideLoading()
            uni.showToast({
              title: "验证码发送成功！",
              type: "success",
            });
            let time = 60;
            this.text = time + "s";
            this.activeStyle = "timing";
            let timer = setInterval(() => {
              if (time === 1) {
                clearInterval(timer);
                this.text = "重新获取";
                this.activeStyle = "unClickSty";
              } else {
                time--;
                this.text = time + "s";
              }
            }, 1000);
          }
        })
        .catch((err) => {
          uni.hideLoading()
          console.log(err, "err");
        });
      // --------------接口预留------------//
    },
  },
};
</script>

<style lang="scss" scoped>
.loginBox {
  .model {
    position: absolute;
    right: 20px;
  }
  .box {
    width: 200px;
    height: 200px;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    button {
      width: 100px;
      margin-top: 20px;
    }
  }
  .topTitle {
    width: 130px;
    margin: 80px 25px;
    position: relative;

    .text {
      font-size: 24px;
      font-weight: 500;
      color: #12112d;
      line-height: 33px;
      font-family: PingFangSC-Medium, PingFang SC;
      position: absolute;
      top: -20px;
    }

    .blueLine {
      width: 125px;
      height: 8px;
      background-color: #1890ff;
      opacity: 0.6;
    }
  }

  .formContent {
    width: 80%;
    margin: 0 auto;

    .prefNum {
      font-weight: 600;
      font-size: 16px;
      margin: 10px;
    }

    .codeContent {
      width: 100%;
      display: flex;

      .unClickSty {
        width: 120px;
        height: 35px;
        border: 1px solid #79beff;
        text-align: center;
        line-height: 32px;
        border-radius: 40px;
        font-size: 14px;
        font-weight: 400;
        padding: 0 10px;
        color: #79beff;
        background-color: white;
        font-family: PingFangSC-Regular, PingFang SC;
      }

      .timing {
        font-family: PingFangSC-Regular, PingFang SC;
        width: 120px;
        height: 35px;
        background-color: white;
        border: 1px solid gray;
        text-align: center;
        line-height: 32px;
        border-radius: 40px;
        font-size: 14px;
        font-weight: 400;
        padding: 0 10px;
        color: gray;
        pointer-events: none;
      }
    }
  }

  .loginBt {
    width: 300px;
    margin: 20px auto;
    font-size: 16px;
    .submit {
      background: rgba(52, 127, 241, 0.3);
      border-radius: 22px;
      color: #f3f3f3;
      &::after {
        border: unset;
      }
    }
    .actSubmit {
      background: #1890ff;
      border-radius: 22px;
      color: white;
    }
  }

  /deep/.u-input {
    border-bottom: 0.5px solid #dadbde;
    caret-color: #79beff;
    border-radius: 0;
    padding: 5px 0;
  }

  a {
    color: #79beff;
  }
}
</style>
