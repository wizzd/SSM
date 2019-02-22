package com.yi.controller;

import com.yi.entity.UserDo;
import com.yi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
/**
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
@RestController
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/login.html")
    public String login(){
        return "login";
    }
    @RequestMapping("/logining.html")
    public ModelAndView test(HttpServletRequest request,LoginVO loginVO){
        UserDo user = userService.login(loginVO.getName(),loginVO.getPassword());
        if (user != null){
            request.getSession().setAttribute("info",1);
            request.getSession().setAttribute("user",user);
            return new ModelAndView("success");
        } else {
            request.getSession().setAttribute("info",0);
            return new ModelAndView("login");
        }
    }

}
