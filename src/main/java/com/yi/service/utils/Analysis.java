package com.yi.service.utils;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analysis {
    //     Pattern.compile("(样本编号|检验日期|采样日期|采样时间|采集时间|接收时间):?;?[\\d]{6,8}"),
    private static Pattern[] patterns = new Pattern[]{
            Pattern.compile("(姓名:?;?[\\u4e00-\\u9fa5]{1,3})"),
            Pattern.compile("(科室|科别|室):?;?[\\u4e00-\\u9fa5]{1,9}"),
            Pattern.compile("(样本类型|标本种类|样本种类|标本类型|检验科:):?;?[\\u4e00-\\u9fa5]{2,4}"),
            Pattern.compile("(样本编号|检验日期|采样日期|采样时间|采集时间|接收时间|送检时间|报告时间)\\D?[\\d|-]{8,10}"),
            Pattern.compile("([\\u4e00-\\u9fa5\\d]{4,20}院)")
    };
    private static Pattern test = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static String[] s = new String[]{"姓名", "科室", "样本类型", "采样日期", "医院"};

    public static final boolean testchinese(char c, int i) {
        if (i == 3)
            if (c >= '0' && c <= '9')
                return true;
            else
                return false;
        if ((c >= '\u4e00') && (c <= '\u9fa5'))
            return true;
        else
            return false;
    }

    public static final String getPoint(String data) {

        System.out.println("图片数据如下");
        System.out.println(data);
        System.out.println("进行图片信息分析");
        String[] str = new String[5];
        for (int i = 0; i < 5; i++) {
            try {
                Matcher matcher = patterns[i].matcher(data);
                if (matcher.find()) {
                    str[i] = matcher.group();
//                    System.out.println(str[i]);
                } else {
                    str[i] = "null";
                    System.out.println(" 未匹配到" + s[i]);
                }
            } catch (Exception e) {
                System.out.println("匹配异常");
            }
        }
// 仅能针对可以识别出冒号的化验单

        int[] a = new int[]{1, 2};
        for (int i = 0; i < 4; i++) {
            if (str[i].contains(":")) {
                String[] split = str[i].split(":");
                str[i] = split[1];
            } else {
                char c = str[i].charAt(a[i / 2] * 2);
                if (testchinese(c, i)) {
                    str[i] = str[i].substring(a[i / 2] * 2);
                } else {
                    str[i] = str[i].substring(a[i / 2] * 2 + 1);
                }
            }
        }
        JSONObject json = new JSONObject();
        if (str[3].length() == 10 && str[3].contains("-")) {
            str[3] = str[3].replaceAll("-", "");
            str[3] = str[3].substring(0, 8);
        }
        if (str[3].length() == 10){

            str[3] = str[3].substring(0, 8);
        }
//        System.out.println(str[3]);
        json.put("name", str[0]);
        boolean l = str[1].equals("l");
        if (l) {
            json.put("type", str[2]);
        } else {

            json.put("type", str[1] + "/" + str[2]);
        }
        json.put("time", str[3]);
        json.put("hospital", str[4]);

//        System.out.println(json.toString());
        return json.toString();


    }
}
