module.exports = {
  plugins: [
    ["import", {"libraryName": "antd", "libraryDirectory": "es", "style": "css"}],
    [
      'babel-plugin-module-resolver',
      {
        alias: {
          components: './src/components',
        },
      },
    ],
  ],
};
