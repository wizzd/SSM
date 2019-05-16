//获取应用实例
const app = getApp()
Page({
    data: {
    //  userInfo: app.globalData.userInfo,
        //判断小程序的API，回调，参数，组件等是否在当前版本可用。
        canIUse: wx.canIUse('button.open-type.getUserInfo'),
      touch_status_code:0,
      sort_status_code:0,
      isHide: false,
      results:new Array(2),
      sorticon:"/images/icon/sort.png",
      sortdownicon:"/images/icon/sort-down.png",
      sortupicon:"/images/icon/sort-up.png",
      imageurl: [],
      array:["name","time","hospital","type"]
      
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
  parese_sort_img_sort_status : function(e){
    var that = this
    //e == 0即初始化
    if (e == 0) 
      for(var i = 0; i < 4; i++){
        var a1 = "imageurl[" + i + "]"
        that.setData({
          [a1]: that.data.sorticon,
        })
    } 
    else{ 
       var i = Math.log2(e)      
       var newImg = "imageurl[" + i + "]"
       if (e == that.data.sort_status_code){
        //记录该位置已经是第二次变化了
            that.setData({
              sort_status_code: ~e&15,
              [newImg]: that.data.sortdownicon,
           })
       }
        else{
            if ((~e & 15) != that.data.sort_status_code){
                var d = that.data.sort_status_code
              if (d){
                 var c = (d & 1 )+ ((d >> 1) & 1) + ((d >> 2) & 1)
                  if(c>1)
                    var previous = "imageurl[" + Math.log2(15 - d) + "]" 
                  else 
                    var previous = "imageurl[" + Math.log2( d) + "]"
                  that.setData({
                     [previous]: that.data.sorticon,
                 })
               }         
               that.setData({
                 sort_status_code: e ,
                [newImg]: that.data.sortupicon,
                })
             }
              else {
               //  记录上一次变化的位置
                that.setData({
                 sort_status_code: 0,
                 [newImg]: that.data.sorticon,
               })
             }
          } 
      }    
    },
  // 单击
  tap: function (e) {
    var that = this
    var tmp = e.currentTarget.dataset.id
    var sub = that.data.array[Math.log2(tmp)]
    if (that.data.imageurl[Math.log2(tmp)] == that.data.sortupicon){
      var order = "0"
    }
    else
    var order = "1"
     
    wx.request({
      url: 'http://localhost:8080/imges/sortMessage',
      data: {
        openId: app.globalData.openId,
        id:sub,
        order : order
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
    that.lastTapTimeoutFunc = setTimeout(function () {
      
      that.parese_sort_img_sort_status(tmp)
    }, 200);
  },

 onShow: function(){
   var that = this;
   setTimeout(function () {
     that.getMessage()
   }, 1500);
    
    that.getMessage()
    
 },
  getMessage: function () {
    var that = this;  
    // 若已授权
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
  },
  // 长按--删除
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
              that.getMessage()
              
              that.onShow()
            }
          });
        }
        that.onShow()
      }
    })
    that.onShow()
    
  },
  // 双击时touch_status_code状态码为2
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
        that.data.touch_status_code=2
      }
    }
  },
  // showImg 显示图片
  showImg:function(event){
    var that = this
    that.doubleTap(event)
    if (that.data.touch_status_code ==2){
      var id = event.currentTarget.dataset.id;
      console.log(this.data.results[id].img)
      wx.previewImage({
        // current: "images/wx_login.png", //因为是静态文件的路径 根据文档要求需要http连接所以不能显示 但可以掩饰
        // urls: ["images/wx_login.png"], 
        current: this.data.results[id].img, // 当前显示图片的http链接
        urls: [this.data.results[id].img], // 需要预览的图片http链接列表

      })
      that.data.touch_status_code = 0

    }
  },
    onLoad: function() {
        var that = this;
      that.parese_sort_img_sort_status(0)
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
      var that = this
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