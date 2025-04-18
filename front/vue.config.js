const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false  // 暂时关闭 lint 检查，等修复完所有问题后可以重新开启
})

module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8112',
        changeOrigin: true
      }
    }
  }
}

