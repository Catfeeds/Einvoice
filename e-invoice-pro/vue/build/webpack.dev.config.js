const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const merge = require('webpack-merge');
const webpackBaseConfig = require('./webpack.base.config.js');
const fs = require('fs');
const package = require('../package.json');

fs.open('./build/env.js', 'w', function (err, fd) {
    const buf = 'export default "development";';
    fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) { });
});

module.exports = merge(webpackBaseConfig, {
    devtool: '#source-map',
    output: {
        publicPath: '/dist/',
        filename: '[name].js',
        chunkFilename: '[name].chunk.js'
    },
    plugins: [
        new ExtractTextPlugin({
            filename: '[name].css',
            allChunks: true
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: ['vender-exten', 'vender-base'],
            minChunks: Infinity
        }),
        new HtmlWebpackPlugin({
            title: 'iView admin v' + package.version,
            filename: '../index.html',
            inject: false
        }),
    ],

    // 开发模式网络请求跨域代理配置
    devServer: {
        proxy: {
            '/e-invoice-pro/*': {
                target: 'http://127.0.0.1:8081/e-invoice-pro',
                // target: 'http://localhost:8080/e-invoice-pro/rest',
                // target: 'http://fapiao.cloud360.com.cn/e-invoice-pro/rest',
                changeOrigin: true,
                secure: false, // 接受 运行在 https 上的服务
                pathRewrite: { '^/e-invoice-pro': '' }
            }
        }
    }
});