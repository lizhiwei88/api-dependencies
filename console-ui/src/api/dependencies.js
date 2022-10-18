import request from '@/util/request'

// 获取依赖关系
export const listDependencies = () => {
    return request({
        url: '/dependencies',
        method: 'get'
    })
}
