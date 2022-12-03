import axios from 'axios'
import helper from './helper'
// let qs = require('querystring')
import qs from 'qs'

axios.defaults.withCredentials = true

//console.log( process.env.NODE_ENV)  
//判断环境提供baseURL，注意要与后台地址一致
let root = process.env.API_ROOT

    // 引用axios，设置头文件
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

function apiAxios(method, url, data, token) {
    return axios({
        method: method,
        //拼接参数
        url: method === 'GET' || method === 'DELETE' ? helper.queryString(url, data) : url,
        data: method === 'POST' || method === 'PUT' ? data : null,
        baseURL: root,
        timeout: 8000,
        // headers: { Authorization: `Bearer ${token}` },
        headers: {'Cache-Control': 'no-cache'},
        withCredentials: false
    })
}



  

export default {
    get: function(url, params, token) {
        return apiAxios('GET', url, params, token)
    },
    post: function(url, params, token) {
        return apiAxios('POST', url, params, token)
    },
    put: function(url, params, token) {
        return apiAxios('PUT', url, params, token)
    },
    delete: function(url, params, token) {
        return apiAxios('DELETE', url, params, token)
    },
}