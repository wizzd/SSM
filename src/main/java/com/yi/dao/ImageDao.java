package com.yi.dao;

import com.yi.entity.ImageDo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ImageDao {
//   void addImage(String openId, String img, String name, String hospital, String type, LocalDate time);
   void addImage(String openId, String img, String name, String hospital, String type, LocalDate time);
   ImageDo getImage(String openId, String img);
   void delectImage(String openId,String img);
   List<Map<String,Object>> selectByOpenId(String openId);
   List<Map<String,Object>> orderByTime(String openId,int i);

}
