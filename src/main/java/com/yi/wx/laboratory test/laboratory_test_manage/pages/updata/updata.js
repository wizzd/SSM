// index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    photos: "",
   
  },
  /**
   * 选择照片
   */
  chooseImg: function () {
    var that = this
    var data =  getApp().globalData.openId;
    wx.chooseImage({
      count: 1, // 默认
      sizeType: ['original'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function (res) {
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths
        that.setData({
          photos: tempFilePaths
        })
        console.log(that.data.photos)
      }
    })
  },
  uploadImg: function () {
    var that = this
    var da = getApp().globalData.openId;
    wx.uploadFile({
      url: 'http://localhost:8080/upload', //仅为示例，非真实的接口地址
      filePath: that.data.photos[0],
      name: 'file',
      formData: {
        'user': da
      //  'user':'yi'
      },
      success: function (res) {
        var data = res.data
        console.log(data)
        //do something
      }
    });
    }
})