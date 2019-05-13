package com.yi.controller;

import com.yi.service.UserService;
import com.yi.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RestController
public class LoginController{
    private WxUserService wxuserService;
    private UserService userService;
    @Autowired
    public void setWxuserService(WxUserService wxuserService) {
        this.wxuserService = wxuserService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/wxlogin")
    protected  void opendId(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String avatarUrl = request.getParameter("avatarUrl");
        String openId = wxuserService.getOpenId(code, response);
        System.out.println("name:** "+name+"   opendId: "+openId);
        userService.login(openId,name,avatarUrl);

    }
}
