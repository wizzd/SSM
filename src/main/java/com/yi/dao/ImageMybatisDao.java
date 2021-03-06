package com.yi.dao;

import com.yi.entity.ImageDo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class ImageMybatisDao {
    private SqlSessionTemplate sessionTemplate;

    @Autowired
    public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public void updata(String openId, String img, String name, String hospital, String type,LocalDate time){


            ImageDao imageDao = sessionTemplate.getMapper(ImageDao.class);
            imageDao.addImage(openId,img,name,hospital,type, time);
    }
}
