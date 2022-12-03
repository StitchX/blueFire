import request from "@/utils/request"


// 行业分类树
export function treeList(data){
    return request({
        url:"/category/tree",
        method:"post",
        data:data
    })
}
// 区域、行业中的区域
export function quyuList(data){
    return request({
        url:"/supervisionbureau/user/ids",
        method:"post",
        data:data
    })
}

// 上传副文件
export function ossUp(data){
    return request({
        url:"/oss/up",
        method:"post",
        data:data
    })
}
// 通知精准选择获取商户接口
export function noticeInfo(data){
    return request({
        url:"/merchant/notice/info",
        method:"post",
        data:data
    })
}

//提交接口
export function noticeAdd(data){
    return request({
        url:"/notice/add",
        method:"post",
        data:data
    })
}

// 修改接口

export function noticeUpdate(data){
    return request({
        url:"/notice/update",
        method:"post",
        data:data
    })
}

