const path = require('path');

export default {
  entry: 'src/index.js',
  extraBabelPlugins: [['import', {libraryName: 'antd', libraryDirectory: 'es', style: true}]],
  proxy: {
    '/api': {
      target: 'http://127.0.0.1:8088/api',
      changeOrigin: true,
      pathRewrite: {'^/api': ''},
    },
  },
  env: {
    development: {
      extraBabelPlugins: ['dva-hmr'],
    },
  },
  alias: {
    components: path.resolve(__dirname, 'src/components/'),
  },
  ignoreMomentLocale: true,
  theme: './src/theme.js',
  html: {
    template: './src/index.ejs',
  },
  disableDynamicImport: true,
  publicPath: '/',
  hash: true,
  commons: [
    {
      names: ['antd', 'components', 'echarts-for-react'],
      children: true,
      minChunks: Infinity,
    },
  ]
};
