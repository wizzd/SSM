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
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Service
public class ImageServiceImpl implements ImageService {
    private ImageDao imageDao;

    @Autowired
    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public String addImage(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        request.setCharacterEncoding("UTF-8");
//        System.out.println("ִ��ͼƬ�ϴ�");
        String user = request.getParameter("user");
        if (!file.isEmpty()) {
//            System.out.println("�ɹ���ȡ��Ƭ");
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = null;
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
//            System.out.println("ͼƬ��ʼ����Ϊ��" + fileName + " ����Ϊ��" + type);
            if (type != null) {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                    // ��Ŀ��������ʵ�ʷ������еĸ�·��
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // �Զ�����ļ�����
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
                    ;
                    // ���ô��ͼƬ�ļ���·��
                    path = realPath + "uploads\\" + trueFileName;
                    System.out.println("�ļ��ɹ��ϴ���ָ��Ŀ¼�� ���ͼƬ�ļ���·��:" + path);
                    file.transferTo(new File(path));
                    //Analysis ʶ��ͼƬ�е���Ϣ��ת����json�ַ�  Ocr ����ͼƬʶ��
                    String json = Analysis.getPoint(Ocr.toString(path).toString());
                    System.out.println("Analysis.getPoint�Ľ����  "+json);
                    JSONObject jsonObject = new JSONObject(json);
                    String timeStr = jsonObject.get("time").toString();
                    LocalDate time =null;
                    if (timeStr.contains("-")) {
                         time = LocalDate.parse(jsonObject.get("time").toString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }else {
                         time = LocalDate.parse(jsonObject.get("time").toString(),
                                DateTimeFormatter.ofPattern("yyyyMMdd"));
                    }
                    imageDao.addImage(user, path,
                            jsonObject.get("name").toString(),
                            jsonObject.get("hospital").toString(),
                            jsonObject.get("type").toString(),
                            time);
////                    LocalDate time = LocalDate.parse(str[3]);
//                    LocalDate.parse(jsonObject.get("time").toString(),
//                            DateTimeFormatter.ofPattern("yyyyMMdd"))
                } else {
                    System.out.println("����������Ҫ���ļ�����,�밴Ҫ�������ϴ�");
                    return "error";
                }
            } else {
                System.out.println("�ļ�����Ϊ��");
                return "error";
            }
        } else {
            System.out.println("û���ҵ����Ӧ���ļ�");
            return "error";
        }
        System.out.println("�Ѵ������ݿ�");
        return "success";

    }

    @Override
    public ImageDo getImage(String openId, String img) {
        return imageDao.getImage(openId, img);
    }

    @Override
    public void delectImage(String openId, String img) {
        imageDao.delectImage(openId, img);
    }


}
