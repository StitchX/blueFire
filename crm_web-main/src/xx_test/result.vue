<template>
    <div class="result">
        <!-- <webview id="foo" :src="info" style="display:inline-flex; width:640px; height:480px"></webview> -->
    </div>
</template>

<script>
export default {
    data(){
        return{
            result:"",    //订单号
            code:"",     //失败或者成功
            // info:""
            num:1
        }
    },
    beforeCreate(){
      this.$toast.loading({
        message: '支付中...',
        forbidClick: true
      });
    },
    created(){
        // this.info=JSON.parse(localStorage.getItem('code'));
    // 获取clickid
      let name, value
      let str = location.href // 获取整个地址栏
      let num = str.indexOf('?')
      str = str.substr(num + 1) // 取得所有参数   18982071814  
      let arr = str.split('&') // 各个参数放到数组里
      for (let i = 0; i < arr.length; i++) {
        num = arr[i].indexOf('=')
        if (num > 0) {
          name = arr[i].substring(0, num)
          value = arr[i].substr(num + 1)
          this.result = value   //截取到订单号
          console.log(value)
        }
      }           
        this.onlond();
    //     if(this.code ==true){
    //         this.$toast.success('支付成功');
    //         this.time()
    //         TDAPP.onEvent("支付成功返回主页");
    //     }  
    //    else if(this.code == false){                                                                                                                              
    //         for(let i=0;i<3;i++){
    //         setTimeout(()=>{
    //              this.onlond();
    //         },2000)        
    //         }                          
    //     }  
    },
    methods:{
        onlond(){
                this.$api.orderId({
                    orderId:this.result
                }).then((res)=>{
                    let info = res.data;
                    console.log(info)
                    if(info.code == 200){
                        this.code=info.result
                        if(info.result == true){
                        this.$toast.success('支付成功');
                        this.time();
                        }else if(info.result ==false){
                         this.$toast({message: '支付失败',icon: 'cross'});//
                         this.time();                   
                        }
                        TDAPP.onEvent("支付后返回主页");
                    }else{
                        this.$toast(res.data.msg);
                        this.time();  
                    }    
                }) 
        },
        // info(){
        //     for(let i=0;i<3;i++){
        //     setTimeout(()=>{
        //      this.onlond();
        //     },2000)  
        //     this.$toast({message: '支付中。。'});//,icon: 'cross'
        //     this.time()     
        //     }
        // },
        time(){                     //显示状态完成后跳转
            setTimeout(()=>{
            //    this.$router.replace({path: '/'});
            // window.opener=null
            // window.open('','_self')
            // window.close()
            // window.history.back()
            //   this.$router.go(-1);
            window.history.go(-2);
            },2000)
        }
    }
}
</script>


<style scoped>

</style>
