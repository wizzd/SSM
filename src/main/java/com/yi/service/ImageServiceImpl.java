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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                     realPath = request.getSession().getServletContext().getRealPath("/");
                    // �Զ�����ļ�����
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
                    // ���ô��ͼƬ�ļ���·��
                    path = realPath + "uploads\\" + trueFileName;
                    String imgPath = url+"uploads//" + trueFileName;
                    System.out.println("�ļ��ɹ��ϴ���ָ��Ŀ¼�� ���ͼƬ�ļ���·��:" + path);
                    file.transferTo(new File(path));
                    //Analysis ʶ��ͼƬ�е���Ϣ��ת����json�ַ�  Ocr ����ͼƬʶ��
                    String s = Ocr.toString(path);
//                    ����ȥ"-" ���� 2015-1-12 ��˵ ȥ�ܺ� ���Էֱ�2015 11 2 ����2015 1 12
                    s = s.replaceAll("/", "-");
                    s = s.replaceAll("_", ":");
                    String json = Analysis.getPoint(s);
//                    System.out.println("Analysis.getPoint�Ľ����  " + json);
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
                    System.out.println("�Ѵ������ݿ�");
                } else {
                    System.out.println("����������Ҫ���ļ�����,�밴Ҫ�������ϴ�");
                    return "typeerror";
                }
            } else {
                System.out.println("�ļ�����Ϊ��");
                return "filenull";
            }
        } else {
            System.out.println("û���ҵ����Ӧ���ļ�");
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
        String real = img.replace(url,realPath);
        File file = new File(real);
        if (file.isFile() && file.exists()) {
            Boolean succeedDelete = file.delete();
            if (succeedDelete) {
                System.out.println("�ɹ�ɾ��Ŀ��ͼƬ��");
            }
        }
    }

    @Override
    public String selectListMap(String openId) {
        List<Map<String, Object>> maps = imageDao.selectByOpenId(openId);
        if (maps == null){
            return "null";
        }
        Iterator<Map<String, Object>> iterator = maps.iterator();
        StringBuffer s = new StringBuffer(200);
        int count = 0;
        s.append("[");
        while (iterator.hasNext()) {

            count++;
            Map<String, Object> next = iterator.next();
            String s1 = next.toString().replaceAll("=", "\":\"") ;
            s1=s1.replaceAll("\\\\","\\\\\\\\");
             s1 = s1.replaceAll("\\{", "\\{\"");
             s1 = s1.replaceAll("\\}", "\"\\}");
            s1 = s1.replaceAll(",", "\",\"");
            s1+=",";
            s.append(s1);
        }
        String s1 = s.substring(0, s.length() - 1)+"]";
         s1 = s1.replaceAll("\\s", "");
        if (s1.equals("]")) {
            return "null";
        }
        return s1;
    }
}
