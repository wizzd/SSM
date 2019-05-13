package com.yi.service.utils;

import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  Analysis {
    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("(姓名:[\\u4e00-\\u9fa5]{1,3})"),
            Pattern.compile("(科室:|科别:)[\\u4e00-\\u9fa5]{1,4}"),
            Pattern.compile("(检验科:|标本种类:|样本种类:)[\\u4e00-\\u9fa5]{1,4}"),
            Pattern.compile("(样本编号:|检验日期:|采样时间:)[\\d|-]{8,10}?"),
            Pattern.compile("([\\u4e00-\\u9fa5]{1,9}医院)")
    };

    //name
    public static final String getPoint(String data) {

        System.out.println("图片数据如下");
        System.out.println(data);
        System.out.println("进行图片信息分析");
        String[] str = new String[5];
        for (int i = 0; i < 5; i++) {
            try{
                Matcher matcher = patterns[i].matcher(data);
                matcher.find();
                str[i] = matcher.group();
            }catch (Exception e){
                System.out.println("i的值：  "+i);
//                throw e;
            }
        }

        for (int i = 0; i < 4; i++) {
            String[] split = str[i].split(":");
            str[i] = split[1];
        }

        JSONObject json = new JSONObject();

        json.put("name", str[0]);
        json.put("type", str[1] + "/" + str[2]);
        json.put("time", str[3]);
        json.put("hospital", str[4]);
//        System.out.println(json.toString());
        return json.toString();
//        LocalDate time = LocalDate.parse(str[3]);

    }
}
