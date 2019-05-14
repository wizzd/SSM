package com.yi.controller;

import com.yi.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public void  delect(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String openId = request.getParameter("openId");
        String img = request.getParameter("img");
        imageService.delectImage(openId,img);
    }
    @RequestMapping("/getMessage")
    public void getMessage(HttpServletRequest request,HttpServletResponse response)throws Exception{
        String openId = request.getParameter("openId");

    }

}