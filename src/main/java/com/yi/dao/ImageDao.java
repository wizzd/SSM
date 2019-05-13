package com.yi.dao;

import com.yi.entity.ImageDo;

import java.time.LocalDate;

public interface ImageDao {
//   void addImage(String openId, String img, String name, String hospital, String type, LocalDate time);
   void addImage(String openId, String img, String name, String hospital, String type);
   ImageDo getImage(String openId, String img);
   void delectImage(String openId,String img);
}
