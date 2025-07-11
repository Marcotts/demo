const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8084',
        changeOrigin: true
      }
    }
  },
  productionSourceMap: false,
  css: {
    sourceMap: true
  }
})
