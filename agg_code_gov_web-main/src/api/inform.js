import request from "@/utils/request"


// 通知列表
export function getList(data){
    return request({
        url:"/notice/list",
        method:"post",
        data:data
    })
}

// 通知h5详情
export function getnoticeId(data){
    return request({
        url:"/notice/noticeId",
        method:"post",
        data:data
    })
}
// 通知删除
export function getnoticeDelete(data){
    return request({
        url:"/notice/delete" ,
        method:"PUT",
        data:data
    })
}

// 通知列表详情
export function getNoticeList(data){
    return request({
        url:"/notice/details/list",
        method:"post",
        data:data
    })
}
