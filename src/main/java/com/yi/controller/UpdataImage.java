package com.yi.controller;
//import org.apache.log4j.Logger;
import com.yi.service.Ocr;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UpdataImage {
    //TODO  分割职能
    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("(姓名:[\\u4e00-\\u9fa5]{1,3})"),
            Pattern.compile("(科室:|科别:)[\\u4e00-\\u9fa5]{1,4}"),
            Pattern.compile("(检验科:|标本种类:|样本种类:)[\\u4e00-\\u9fa5]{1,4}"),
            Pattern.compile("(检验日期:[\\d]{8})"),
            Pattern.compile("([\\u4e00-\\u9fa5]{1,9}医院)")
    };
//    private Logger logger = Logger.getLogger(GoodsController.class);
    @ResponseBody
    @RequestMapping("upload")
    public String upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        System.out.println("执行upload");
        String s = file.getClass().toString();
        System.out.println("-------------   "+s+"   ----------------");
        request.setCharacterEncoding("UTF-8");
        System.out.println("执行图片上传");
        String user = request.getParameter("user");
        if(!file.isEmpty()) {
            System.out.println("成功获取照片");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            System.out.println("图片初始名称为：" + fileName + " 类型为：" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    String trueFileName =   String.valueOf(System.currentTimeMillis()) + fileName;;
                    // 设置存放图片文件的路径
                    path = realPath + "uploads\\" + trueFileName;

                    System.out.println("存放图片文件的路径:" + path);
                    file.transferTo(new File(path));
                    String json = Ocr.toString(path);
                    String [] str = new String[4];
                    for (int i = 0; i < 5; i++) {
                        Matcher matcher = patterns[i].matcher(json);
                        matcher.find();
                        str[i] = matcher.group();
                        System.out.println(str[i]);
                    }
                    String [] tmp = new String[2];
                    for (int i = 0; i < 3; i++) {
                        String[] split = str[i].split(":");
                        str[i] = split[1];
                    }
                    String name = str[0];
                    String typeProperty = str[1]+str[2];
                    LocalDate time = LocalDate.parse(str[3]);
                    String hospital = str[4];
//                    String typeProperty = str[1]+"//"+str[2];
                    //科别 科室  检验科 标本种类 样本种类
                    //医院
                    System.out.println("文件成功上传到指定目录下");
                }else {
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    return "error";
                }
            }else {
                System.out.println("文件类型为空");
                return "error";
            }
        }else {
            System.out.println("没有找到相对应的文件");
            return "error";
        }
        return "success";
    }

}
