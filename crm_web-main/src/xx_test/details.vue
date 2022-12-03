<template>
    <div class="details">
        <van-nav-bar title="充值说明"  left-arrow @click-left="onClickLeft" :fixed="true" :border="false" />
        <div class="body">
            <div>
                <h5 class="black">
                    话费未到账的处理方式
                </h5>
                <van-divider hairline />
                <div class="font_size">
                    <div class="postion">  <span>手机话费充值是由成都蓝色火焰科技有限公司提供服务，一般在6小时内到账（月初/月末充值高峰期或运营商维护期，充值有所延迟，预计24小时内可发货充值到账），请您耐心等待。</span> </div>
                    <!-- <div  class="postion"><span>2、</span> <span>若话费未到账，请您咨询人工客服[400XXXXXX]，我们会在48小时内解答您的问题。</span></div> -->
                </div>
            </div>
        </div>
        <div class="body">
            <div>
                <h5 class="black">
                    话费充错号码怎么办
                </h5>
                <van-divider hairline />
                <div class="font_size">
                    <div class="postion"> <span>1、</span> <span>交易一旦成功，话费会马上入账，资金也会及时支付到运营商，请先核实充值订单状态。</span> </div>
                    <div  class="postion"><span>2、</span> <span>如果订单状态是"充值成功"，请您优先自行联系充错的号码，和对方协商能否将资金转回，话费充值成功，交易就已经完成，系统无法给您办理退款。</span></div>
                    <!-- <div  class="postion"><span>3、</span> <span>如果订单状态一直处理"发货中"或者充值手机是无效手机号码，请您咨询人工客服[400XXXXXX]，我们会在48小时内解答您的问题。</span></div> -->
                </div>
            </div>
        </div>
        <div class="body">
            <div>
                <h5 class="black">
                    索取发票的方式
                </h5>
                <van-divider hairline />
                <div class="font_size">
                    <div > 我司作为网络充值平台，无权提供通信充值发票，发票(纸质/电子)由运营商提供，请您联系运营商线下直营网点开具"消费发票"，感谢您的支持与理解。 </div>
                </div>
            </div>
        </div>
        <div class="font_size center">
            <div>@CopyRight 成都蓝色火焰</div> 
            <!-- <div>由本公司负责</div> -->
            <!-- <div>地址：中国（四川）自由贸易试验区成都高新区天府二街151号1栋1单元21层2102号</div> 
            <div>备案：蜀ICP备 19040730号</div> -->
        </div>
        <!-- <span @click="info()">联系客服</span>
        <span @click="code()">ssssssss</span> -->
    </div>
</template>

<script>
export default {
    data(){
        return{
            
        }
    },
    created(){
      TDAPP.onEvent("点击充值说明");
            
    },
    methods:{
        onClickLeft() {
         this.$router.go(-1);  //返回上一页
        },
        onfont(){
            // window.location.href="https://temp-chat.mstatik.com/widget/standalone.html?eid=201365"
         },
         info(){
             _MEIQIA('init');
    //            (function(m, ei, q, i, a, j, s) {
    //     m[i] = m[i] || function() {
    //         (m[i].a = m[i].a || []).push(arguments)
    //     };
    //     j = ei.createElement(q),
    //         s = ei.getElementsByTagName(q)[0];
    //     j.async = true;
    //     j.charset = 'UTF-8';
    //     j.src = 'https://static.meiqia.com/dist/meiqia.js?_=t';
    //     s.parentNode.insertBefore(j, s);
    // })(window, document, 'script', '_MEIQIA');
    // _MEIQIA('entId', '201365');

    // 在这里开启无按钮模式（常规情况下，需要紧跟在美洽嵌入代码之后）
    // _MEIQIA('withoutBtn');
         },
         code(){
            
             
         }
    }
}
</script>


<style scoped>
    .body{
        width: 95%;
        border: 1px solid rgb(228, 225, 225);
        background: #ffffff;
        border-radius: 5px;
        /* height: 5rem; */
        margin:0 auto;
        align-content: center;
        margin-bottom: 15px;
    }
    .details:first-child{
        margin-top: 55px;
    }
    .details:last-child{
        margin-bottom: 10px;
    }
    .black{
        padding:20px 20px 0 20px;
        margin: 0;
    }
    .font_size{
        padding:5px 20px 10px 20px;
        font-size: 0.25rem;
    }
    .postion{
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
    }
    .center{
        text-align: center;
        color: rgb(157, 158, 158);
        margin-top: 45px;
    }
</style>

