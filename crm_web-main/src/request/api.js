import http from './http.js'
export default {
    // 登录
    login(data){
        return http.post("/auth/login",data)
    },
    // bd信息
    bdInfo(data){
        return http.post("/auth/info",data)
    },
    //商户信息 列表
    mechInfo(data){
        return http.post("/merchant/list",data)
    },
    // 商户详情     
    mechDetails(data){
        return http.post("/merchant/detail",data)  
    },
    // 商户编辑   
    mechEdit(data){
        return http.post("/merchant/update",data)  
    },
     // 上传营业执照  
     uploadImg(data){
        return http.post("/picture/businessLicense",data)  
    },
    // 上传食品许可证   
    uploadFoodImg(data){
        return http.post("/picture/foodBusinessLicense",data)  
    },
     // 商户新增    /area/list
     merchAdd(data){
        return http.post("/merchant/add",data)  
    },
    // 地区     
    areaList(data){
        return http.post("/area/list",data)  
    },
    //监管所     
    supervisionList(data){
        return http.post("/supervisionbureau/options",data)  
    },
    //行业选择     
    industryList(data){
        return http.post("/category/option",data)  
    },
    //银行列表     
    bankList(data){
        return http.post("/bank/options",data)  
    },
    //空码验证      
    checkCode(data){
        return http.post("/merchant/verifyCode",data)  
    },
    // 上传其他图片   
    uploadOtherImg(data){
        return http.post("/picture/otherCertificates",data)  
    },
    // 上传店铺图片    
    uploadShopImg(data){
        return http.post("/picture/shopHeadPhoto",data)  
    },
     //所属社区    
     communitList(data){
        return http.post("/supervisionbureau/community/options",data)  
    },
    //删除备案证    
    delBeian(data){
        return http.post("/picture/otherCertificates/file/delete",data)  
    },
    //删除卫生证   
    delHygiene(data){
        return http.post("/picture/otherCertificates/hygiene/delete",data)  
    },
}