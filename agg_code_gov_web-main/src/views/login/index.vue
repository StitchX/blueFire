<template>
  <div
    v-if="isShow"
    v-loading="socialLoading"
    class="login-container"
    :element-loading-text="'现在进行'+currentPath+'第三方登录,请稍等'"
  >
    <div class="login-right">
      <div class="login-center">
        <div class="title-container">
          <div class="title-logo">
            <img
              src="./../../assets/backLogo.png"
              width="72px"
              height="72px"
            >
          </div>
          <div>
            <h3>
              互联网+智慧监管系统
            </h3>
          </div>
        </div>
        <!-- <h5 class="title-hello">欢迎登录</h5> -->
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-form
            ref="loginForm"
            :model="loginForm"
            :rules="loginRules"
            class="login-form"
            label-position="left"
          >
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入账户"
                name="username"
                type="text"
                autocomplete="off"
                @keyup.enter.native="handleLogin"
              >
                <i slot="prefix" style="color:#1890FF">
                  <svg-icon icon-class="user1" />
                </i>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                placeholder="请输入密码"
                name="password"
                type="password"
                @keyup.enter.native="handleLogin"
              >
                <i slot="prefix" style="color:#1890FF">
                  <svg-icon icon-class="密码" />
                </i>
              </el-input>
            </el-form-item>
            <el-form-item />

            <el-button
              :loading="loading"
              type="primary"
              style="width:100%;background:#1C92DC"
              @click.native.prevent="handleLogin"
            >
              登录
            </el-button>
          </el-form>
        </el-tabs>
      </div>

      <div class="copyright">Copyright ©  2022蓝色火焰科技有限公司出品</div>

    </div>
  </div>
</template>

<script>
import { formatData, getUrlKey } from '@/utils/webUtils'
import { isvalidPhone } from '@/utils/validate'
import { getImgCode } from '@/api/login'
import { sendSms } from '@/api/user'

export default {
  name: 'Login',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 2) {
        callback(new Error('The password can not be less than 6 digits'))
      } else {
        callback()
      }
    }
    // 验证手机号格式
    const validPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入手机号'))
      } else if (!isvalidPhone(value)) {
        callback(new Error('请输入正确的11位手机号码'))
      } else {
        callback()
      }
    }
    return {
      tenantList: [],
      loginForm: {
        username: '',
        password: '',
        code: '',
        token: '',
        key: ''
      },
      src: '',
      phoneForm: {
        phone: '',
        code: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入账户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      phoneRules: {
        phone: [{ required: true, trigger: 'blur', validator: validPhone }],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]

      },
      passwordType: 'password',
      loading: false,
      showDialog: false,
      redirect: undefined,
      token: '',
      isShow: true,
      activeName: 'loginForm',
      buttonName: '发送验证码',
      isDisabled: false,
      codeLoading: false,
      time: 60,
      socialLoading: false,
      currentPath: '',
      active: ''
    }
  },
  created() {
    // this.refreshCaptcha()
    this.socialLogin()
  },
  mounted() {
    // 自动加载indexs方法

  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    // 用户名 密码登录
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('LoginByUsername', this.loginForm).then(() => {
            this.loading = false
            this.$router.push({ path: this.redirect || '/merchant/index' })
          }).catch(() => {
            this.loading = false
            // this.refreshCaptcha()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },

    // 手机号短信登录
    phoneLogin() {
      this.$refs.phoneForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('LoginByUserPhone', this.phoneForm).then(() => {
            this.loading = false
            this.$router.push({ path: this.redirect || '/' })
          }).catch(() => {
            this.loading = false
            this.refreshCaptcha()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    refreshCaptcha: function() {
      getImgCode().then(res => {
        console.log(res)
        this.src = res.data.data.img
        this.loginForm.key = res.data.data.key
      })
    },
    // 社交登录
    socialLogin() {
      const _this = this
      _this.loginForm.token = getUrlKey('token')
      if (this.loginForm.token != null && this.loginForm.token !== '') {
        _this.isShow = false
        this.$store.dispatch('LoginByUsername', this.loginForm).then(() => {
          this.loading = false
          this.$router.push({ path: this.redirect || '/' })
        }).catch(() => {
          this.loading = false
          this.refreshCaptcha()
        })
      }
    },
    // 发送短信验证码
    sendCode() {
      if (this.phoneForm.phone !== '' && isvalidPhone(this.phoneForm.phone)) {
        this.codeLoading = true
        this.buttonName = '发送中'
        const _this = this
        sendSms(this.phoneForm.phone).then(res => {
          if (res.data.status == '200') {
            this.$message({
              showClose: true,
              message: '发送成功，验证码有效期2分钟',
              type: 'success'
            })
            this.codeLoading = false
            this.isDisabled = true
            this.buttonName = this.time-- + '秒'
            this.timer = window.setInterval(function() {
              _this.buttonName = _this.time + '秒'
              --_this.time
              if (_this.time < 0) {
                _this.buttonName = '重新发送'
                _this.time = 60
                _this.isDisabled = false
                window.clearInterval(_this.timer)
              }
            }, 1000)
          }
        }).catch(err => {
          this.resetForm()
          this.codeLoading = false
          console.log(err.data.message)
        })
      } else {
        this.$message({
          showClose: true,
          message: '请输入手机号',
          type: 'error'
        })
      }
    },
    handleClick(tab, event) {
      this.$refs[tab.paneName].resetFields()
    },
    handleSocial(path) {
      this.currentPath = path
      this.socialLoading = true
      window.location.href = 'http://localhost:8081/auth/' + path
    },
    gotoRegister() {
      this.$router.push({
        path: '/register'
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
 .copyright{
   color:#FFFFFF;
   font-size: 0.6rem;
   display: flex;
   align-items: flex-end;
   justify-content: center;
   position: absolute;
   bottom: 20px;
   left: 50%;
		transform:translate(-50%,-20px);
//    left: 41%;
 }
  .login-right .el-button--primary {
    color: #FFFFFF;
    background-color: #2B5FF4;
    border-color: #2B5FF4;
}
.title-container{
  display: flex;
  justify-content: center;
  align-items: center;
}
.title-hello{
  color:#FFFFFF;
  font-weight: normal !important;
  text-align: center;
}
.title-container h3{
  color: #FFFFFF;
  font-size: 26px;
}
.title-logo {
  position: absolute;
  top: -30px;
  right: 45%;
// padding-right: 10px;
}
.login-center{
  width: 50%;
  background-image: url('../../assets/backpage.png');
  background-size: 100% 100%;
  position: absolute;
  top: 50%;
  margin-top: -13em;
  padding: 50px 170px 80px;
}
  // #2B5FF4
  .login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    /*background-image: url(https://img2018.cnblogs.com/blog/1211637/201908/1211637-20190809112720089-1507550740.png);*/
    background-image: url('../../assets/BG@2x.png');
    /*background: red;*/
    background-size: cover;

    .login-right {
      width: 50%;
      height: 100%;
      border-radius: 6px;

      .title {
        margin: 0 auto 30px auto;
        text-align: center;
        color: #505458;
      }

      .login-form {
        height: 50%;

      }

      .el-form-item {
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 5px;
        color: #454545;
      }

      .other-login {
        margin-top: 3vh;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
      }

      .other-icon {
        cursor: pointer;
        margin-left: 5px;
        fill: rgba(0, 0, 0, .2);
      }

      .other-icon:hover {
        fill: rebeccapurple;
      }

      .other-login .other-way {
        font-size: 14px;
        color: #515a6e;
        width: calc(100% - 56px)
      }

      .register {
        float: right;
        color: #1ab2ff;
        font-size: 14px;
        cursor: pointer;
        text-align: right;
      }

      .login-select {
        margin-left: 100px;
        margin-bottom: 13px;

        input {
          color: #333;
          font-size: 14px;
          font-weight: 400;
          border: none;
        }
      }
    }

  }
</style>
