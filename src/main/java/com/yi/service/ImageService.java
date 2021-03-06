package com.yi.service;

import com.yi.entity.ImageDo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ImageService {
//    void addImage(String openId, String img, String name, String hospital, String type, LocalDate time);
    String addImage(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException;
    ImageDo getImage(String openId, String img);
    void delectImage(String openId,String img);
    String selectListMap(String openId);
    String order(String openId, String id,int i);
}
