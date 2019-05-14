// index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    photos: "",
    disabled: false,
    tmp : "false",
  },
  setDisabled: function (e) {
    this.setData({
      disabled: !this.data.disabled
    })
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
    var flag = true
    var da = getApp().globalData.openId;
    var photopath = that.data.photos[0]
    if (!photopath){
      flag = false
      wx.showToast({
        title: '未选择图片',
        icon: 'none',
        duration: 2000
      });
      console.log("文件不能为空");
    }
    else{
       if (that.data.tmp == "false") {
         that.setData({
           tmp: photopath
         })
       } 
       else {
         if (that.data.tmp != photopath){
           that.setData({
             tmp: photopath
           })
          }
          else{
          flag = false
           wx.showToast({
             title: '请不要重复提交',
             icon: 'none',
             duration: 2000
           });
          }
        }
     }
    if(flag){
      wx.uploadFile({
        url: 'http://localhost:8080/upload', //仅为示例，非真实的接口地址
        filePath: that.data.photos[0],
        name: 'file',
        formData: {
          'user': da
          //  'user':'yi'
        },
        success: function (res) {
          console.log(res);
          var data = res.data
          console.log(data)
          //do something
        }
      });
    }
    }
})