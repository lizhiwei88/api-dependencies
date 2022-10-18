const { defineConfig } = require('@vue/cli-service')
const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: '../console/src/main/resources/static',
  devServer: {
    port: 80,
    proxy: {
      '/api': {
        target: process.env.VUE_APP_BASE_API,//要代理的本地api地址，也可以换成线上测试地址
        changeOrigin: true,//允许跨域
        pathRewrite:{"^/api":"/api"}//将/api开头替换为/api
      }
    }
  },
  configureWebpack: {
    plugins: [
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
    ],
  }
})
