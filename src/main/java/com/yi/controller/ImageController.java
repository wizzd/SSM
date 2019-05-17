package com.yi.controller;

import com.yi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Controller
@RequestMapping("/imges")
public class ImageController {
    private ImageService imageService;
    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }
    @ResponseBody
    @RequestMapping("/delect")
    public void  delect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openId");
        String img = request.getParameter("img");
        imageService.delectImage(openId, img);
    }
    @RequestMapping("/getMessage")
    public void getMessage(HttpServletRequest request,HttpServletResponse response)throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        String openId = request.getParameter("openId");
        String message = imageService.selectListMap(openId);
//        System.out.println(message);
        response.getWriter().print(message);
//        response.setHeader("data",message);
//        Now you can provide attr `wx:key` for a `wx:for` to improve performance.
    }
    @RequestMapping("/sortMessage")
    public void sortMessage(HttpServletRequest request,HttpServletResponse response)throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        String openId = request.getParameter("openId");
        String id = request.getParameter("id");
        String order = request.getParameter("order");
        String message = imageService.order(openId,id,Integer.parseInt(order));
        System.out.println(message);
        response.getWriter().print(message);
//        response.setHeader("data",message);
//        Now you can provide attr `wx:key` for a `wx:for` to improve performance.
    }
}
