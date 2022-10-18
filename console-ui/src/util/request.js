import axios from 'axios'
import {ElMessage} from "element-plus";

// 创建axios实例
const request = axios.create({
    baseURL: '/api',
    timeout: 10000
})


// response 拦截器
request.interceptors.response.use(
    response => {
        return response.data
    },
    error => {
        let code = 0
        try {
            code = error.response.data.status
        } catch (e) {
            if (error.toString().indexOf('Error: timeout') !== -1) {
                ElMessage.error({
                    title: '网络请求超时',
                    duration: 5000
                })
                return Promise.reject(error)
            }
        }
        console.log(code)

        ElMessage.error({
            title: '接口请求失败',
            duration: 5000
        })
        return Promise.reject(error)
    }
)
export default request
