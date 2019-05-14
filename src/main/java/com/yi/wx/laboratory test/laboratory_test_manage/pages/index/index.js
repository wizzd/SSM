//获取应用实例
const app = getApp()
Page({
    data: {
    //  userInfo: app.globalData.userInfo,
        //判断小程序的API，回调，参数，组件等是否在当前版本可用。
        canIUse: wx.canIUse('button.open-type.getUserInfo'),
        isHide: false,
        results:new Array(2),
    },
  // 触摸开始时间
  touchStartTime: 0,
  // 触摸结束时间
  touchEndTime: 0,
  // 最后一次单击事件点击发生时间
  lastTapTime: 0,
  // 单击事件点击后要触发的函数
  lastTapTimeoutFunc: null, 
  // 按钮触摸开始触发的事件
  touchStart: function (e) {
    this.touchStartTime = e.timeStamp
  },

  // 按钮触摸结束触发的事件
  touchEnd: function (e) {
    this.touchEndTime = e.timeStamp
  },
 onShow: function(){
    var that = this;
    if(app.globalData.ishasdata)
      that.getMessage()
 },
  getMessage: function () {
    var that = this;  
    // 若已授权
    if (app.globalData.ishasdata){
      console.log("进入getMessage")
      wx.request({
        url: 'http://localhost:8080/imges/getMessage',
        data: {
          openId: app.globalData.openId,
        },
        header: {
          'content-type': 'application/json' //默认值
        },
        success: res => {  
          this.setData({
            results : res["data"]
          });      
          // console.log(that.data.results[0]) 
        }
      });
      app.globalData.ishasdata = false
    }
  },
  /// 长按--删除
  longTap: function (event) {
    var that = this;
    var id = event.currentTarget.dataset.id;
    wx.showModal({
      title: '长按',
      content: '是否要删除该图片',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: 'http://localhost:8080/imges/delect',
            data: {
              openId: app.globalData.openId,
              img: that.data.results[id].img
            },
            header: {
              'content-type': 'application/json' //默认值
            },
            success: res => {
              this.setData({
                results: res["data"]
              });
            }
          });
          getApp().globalData.ishasdata = true
        }
        that.onShow()
      }
    })
  
    
  },
  // 双击--查看图片
  doubleTap :function(event){
    var that = this
    // 控制点击事件在350ms内触发，加这层判断是为了防止长按时会触发点击事件
    if (that.touchEndTime - that.touchStartTime < 350) {
      // 当前点击的时间
      var currentTime = event.timeStamp
      var lastTapTime = that.lastTapTime
      // 更新最后一次点击时间
      that.lastTapTime = currentTime
      // 如果两次点击时间在300毫秒内，则认为是双击事件
      if (currentTime - lastTapTime < 300) {
        console.log("double tap")
        // 成功触发双击事件时，取消单击事件的执行
        clearTimeout(that.lastTapTimeoutFunc);
        var id = event.currentTarget.dataset.id;
        console.log(this.data.results[id].img)
        wx.previewImage({
          current: this.data.results[id].img, // 当前显示图片的http链接
          urls: [] // 需要预览的图片http链接列表
        })
      }
    }
  },
    onLoad: function() {
        var that = this;  
        console.log('授权');     
        // 查看是否授权
        wx.getSetting({     
            success: function(res) {    
              console.log(res);          
                if (res.authSetting['scope.userInfo']) {                
                    wx.getUserInfo({                      
                        success: function(res) {                  
                            // 用户已经授权过,不需要显示授权页面,所以不需要改变 isHide 的值
                            // 在用户授权成功后，调用微信的 wx.login 接口，从而获取code
                            wx.login({ 
                                success: res => {                                   
                                    wx.request({
                                      url: 'http://localhost:8080/wxlogin',
                                      data:{
                                        code:res.code,
                                      },
                                      header: {
                                        'content-type': 'application/json' //默认值
                                      },
                                        success: res => {
                                            console.log("用户的openid:" + res);
                                        }
                                    });
                                }
                            });
                        }
                    });
                
                } else {
                    // 用户没有授权 改变 isHide 的值，显示授权页面
                    that.setData({
                        isHide: true
                    });
                }
            }
        });
    },
    bindGetUserInfo:function(res) {
        if (res.detail.userInfo) {
            app.globalData.userInfo=res.detail.userInfo            
            var that = this;
            app.globalData.userInfo = res.detail.userInfo           
            console.log("用户的信息如下：");
            console.log(res.detail.userInfo);
          wx.login({
             success: function (res) {
               console.log("code: "+res.code) 
               wx.request({
                 url: 'http://localhost:8080/wxlogin',
                 data: {
                   code: res.code,
                   name: app.globalData.userInfo.nickName,
                   avatarUrl: app.globalData.userInfo.avatarUrl
                 },
                 header: {
                   'content-type': 'application/json' //默认值
                 },            
                success: res => {
                  console.log(res)                 
                  app.globalData.openId = res.data.openid        
                  app.globalData.ishasdata = true
                  that.getMessage()
                  }
                });
              } 
            });
            //授权成功后,通过改变 isHide 的值，让实现页面显示出来，把授权页面隐藏起来
            that.setData({
                isHide: false
            });
        } else {
            //用户按了拒绝按钮
            wx.showModal({
                title: '警告',
                content: '您点击了拒绝授权，将无法进入小程序，请授权之后再进入!!!',
                showCancel: false,
                confirmText: '返回授权',
                success: function(res) {
                    // 用户没有授权成功，不需要改变 isHide 的值
                    if (res.confirm) {
                        console.log('用户点击了“返回授权”');
                    }
                }
            });
        }
    }
})