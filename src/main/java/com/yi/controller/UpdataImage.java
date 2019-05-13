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
    //TODO  �ָ�ְ��
    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("(����:[\\u4e00-\\u9fa5]{1,3})"),
            Pattern.compile("(����:|�Ʊ�:)[\\u4e00-\\u9fa5]{1,4}"),
            Pattern.compile("(�����:|�걾����:|��������:)[\\u4e00-\\u9fa5]{1,4}"),
            Pattern.compile("(��������:[\\d]{8})"),
            Pattern.compile("([\\u4e00-\\u9fa5]{1,9}ҽԺ)")
    };
//    private Logger logger = Logger.getLogger(GoodsController.class);
    @ResponseBody
    @RequestMapping("upload")
    public String upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        System.out.println("ִ��upload");
        String s = file.getClass().toString();
        System.out.println("-------------   "+s+"   ----------------");
        request.setCharacterEncoding("UTF-8");
        System.out.println("ִ��ͼƬ�ϴ�");
        String user = request.getParameter("user");
        if(!file.isEmpty()) {
            System.out.println("�ɹ���ȡ��Ƭ");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            System.out.println("ͼƬ��ʼ����Ϊ��" + fileName + " ����Ϊ��" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
                    // ��Ŀ��������ʵ�ʷ������еĸ�·��
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // �Զ�����ļ�����
                    String trueFileName =   String.valueOf(System.currentTimeMillis()) + fileName;;
                    // ���ô��ͼƬ�ļ���·��
                    path = realPath + "uploads\\" + trueFileName;

                    System.out.println("���ͼƬ�ļ���·��:" + path);
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
                    //�Ʊ� ����  ����� �걾���� ��������
                    //ҽԺ
                    System.out.println("�ļ��ɹ��ϴ���ָ��Ŀ¼��");
                }else {
                    System.out.println("����������Ҫ���ļ�����,�밴Ҫ�������ϴ�");
                    return "error";
                }
            }else {
                System.out.println("�ļ�����Ϊ��");
                return "error";
            }
        }else {
            System.out.println("û���ҵ����Ӧ���ļ�");
            return "error";
        }
        return "success";
    }

}
