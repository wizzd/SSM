package com.yi.controller;
//import org.apache.log4j.Logger;
import com.yi.service.ImageService;
import com.yi.service.ImageServiceImpl;
import com.yi.service.utils.Analysis;
import com.yi.service.utils.Ocr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@RestController
@Controller
public class UpdataImageController {
//    private Logger logger = Logger.getLogger(GoodsController.class);
    private ImageService imageService;
    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }



    @ResponseBody
    @RequestMapping("upload")
    public void upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        imageService.addImage(request,file);
    }

}
