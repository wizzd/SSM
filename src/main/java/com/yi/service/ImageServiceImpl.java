package com.yi.service;

import com.yi.dao.ImageDao;
import com.yi.entity.ImageDo;
import com.yi.service.utils.Analysis;
import com.yi.service.utils.Ocr;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {
    private ImageDao imageDao;
    private String realPath;
    private static String url = "http://localhost:8080//";
    @Autowired
    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }
    @Override
    public String addImage(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        request.setCharacterEncoding("UTF-8");
//        System.out.println("执行图片上传");
        String user = request.getParameter("user");
        if (!file.isEmpty()) {
//            System.out.println("成功获取照片");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
//            System.out.println("图片初始名称为：" + fileName + " 类型为：" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    realPath = request.getSession().getServletContext().getRealPath("/");
                    File file1 = new File(realPath + "uploads");
                    file1.mkdir();

                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
                    // 设置存放图片文件的路径
                    path = realPath + "uploads\\" + trueFileName;
                    String imgPath = url + "uploads//" + trueFileName;
                    System.out.println("文件成功上传到指定目录下 存放图片文件的路径:" + path);
                    file.transferTo(new File(path));
                    //Analysis 识别图片中的信息并转换成json字符  Ocr 进行图片识别
                    String s = Ocr.toString(path);
//                    不能去"-" 对于 2015-1-12 来说 去杠后 难以分别2015 11 2 还是2015 1 12
                    s = s.replaceAll("/", "-");
                    s = s.replaceAll("_", ":");
                    String json = Analysis.getPoint(s);
//                    System.out.println("Analysis.getPoint的结果：  " + json);
                    JSONObject jsonObject = new JSONObject(json);
                    String jsontime = jsonObject.get("time").toString();
                    LocalDate time = LocalDate.parse("10000101",
                            DateTimeFormatter.ofPattern("yyyyMMdd"));
                    if (jsontime.contains("-")) {
                        if (jsontime.length() == 8) {
                            time = LocalDate.parse(jsontime,
                                    DateTimeFormatter.ofPattern("yyyy-M-d"));
                        }
                        if (jsontime.length() == 9) {
                            try {
                                time = LocalDate.parse(jsontime,
                                        DateTimeFormatter.ofPattern("yyyy-MM-d"));
                            } catch (Exception e) {
                                try {
                                    time = LocalDate.parse(jsontime,
                                            DateTimeFormatter.ofPattern("yyyy-M-dd"));
                                } catch (Exception e1) {
                                    try {
                                        time = LocalDate.parse(jsontime,
                                                DateTimeFormatter.ofPattern("yyyyMM-dd"));
                                    } catch (Exception e2) {
                                        time = LocalDate.parse(jsontime,
                                                DateTimeFormatter.ofPattern("yyyy-MMdd"));
                                    }
                                }
                            }
                        }
                    } else {
                        if (jsontime.length() >= 8) {
                            jsontime = jsontime.substring(0, 8);
//                            System.out.println(jsontime);
                            time = LocalDate.parse(jsontime,
                                    DateTimeFormatter.ofPattern("yyyyMMdd"));
                        } else {
                            time = LocalDate.parse(jsontime,
                                    DateTimeFormatter.ofPattern("yyyyMMdd"));
                        }

                    }
//                    System.out.println(time.toString());
                    System.out.println("-------------");
                    System.out.println(user);
                    System.out.println(imgPath);
                    System.out.println(jsonObject.get("name").toString());
                    System.out.println(jsonObject.get("hospital").toString());
                    System.out.println(jsonObject.get("type").toString());
                    System.out.println(time);
                    imageDao.addImage(user, imgPath,
                            jsonObject.get("name").toString(),
                            jsonObject.get("hospital").toString(),
                            jsonObject.get("type").toString(),
                            time);
                    System.out.println("已存入数据库");
                } else {
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    return "typeerror";
                }
            } else {
                System.out.println("文件类型为空");
                return "filenull";
            }
        } else {
            System.out.println("没有找到相对应的文件");
            return "notfind";
        }
        return "success";

    }
    @Override
    public ImageDo getImage(String openId, String img) {
        return imageDao.getImage(openId, img);
    }
    @Override
    public void delectImage(String openId, String img) {
        imageDao.delectImage(openId, img);
        String real = img.replace(url, realPath);
        File file = new File(real);
        if (file.isFile() && file.exists()) {
            Boolean succeedDelete = file.delete();
            if (succeedDelete) {
                System.out.println("成功删除目标图片！");
            }
        }
    }

    private String listToString(List<Map<String, Object>> maps) {
        Iterator<Map<String, Object>> iterator = maps.iterator();
        StringBuffer s = new StringBuffer(200);
        int count = 0;
        s.append("[");
        while (iterator.hasNext()) {

            count++;
            Map<String, Object> next = iterator.next();
            String s1 = next.toString().replaceAll("=", "\":\"");
            s1 = s1.replaceAll("\\\\", "\\\\\\\\");
            s1 = s1.replaceAll("\\{", "\\{\"");
            s1 = s1.replaceAll("\\}", "\"\\}");
            s1 = s1.replaceAll(",", "\",\"");
            s1 += ",";
            s.append(s1);
        }
        String s1 = s.substring(0, s.length() - 1) + "]";
        s1 = s1.replaceAll("\\s", "");
        if (s1.equals("]")) {
            return "null";
        }
        return s1;
    }
    @Override
    public String selectListMap(String openId) {
        List<Map<String, Object>> maps = imageDao.selectByOpenId(openId);
        if (maps == null) {
            return "null";
        }
        return listToString(maps);
    }

    private  void   listSortByPinyin(String property, List<Map<String, Object>> maps,int i){
        Collections.sort(maps, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (i==0){
                    return Collator.getInstance(Locale.CHINESE).compare(o1.get(property),o2.get(property));
                }
                else{
                    return Collator.getInstance(Locale.CHINESE).compare(o2.get(property),o1.get(property));
                }            }
        });
    }
    @Override
    public String order(String openId, String id, int i) {
//        System.out.println(imageDao.orderByTime("osqKW5Mcw8Nc24zfu4MvQ1X6qDLQ", 0));
        System.out.println("order");
        String message = "";
        switch (id) {
            case "time":
                List<Map<String, Object>> maps = imageDao.orderByTime(openId, i);
                System.out.println("openId:  "+openId);
                System.out.println(maps.toString());
               message= listToString(maps);
                break;
                default:
                    List<Map<String, Object>> maps1 = imageDao.selectByOpenId(openId);
                    listSortByPinyin(id, maps1,i);
                    message = listToString( maps1);
        }
        return message;
    }


}
