package com.yi.service.utils;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.HashMap;

public class Ocr {
    public static final String APP_ID = "15044103";
    public static final String API_KEY = "fnqsyRzuG2mUsyvPSqGMzWa8";
    public static final String SECRET_KEY = "EgqWGQPKGskQCeAOW4FeI3FKuplETfbg";
    public static String toString(String path) {
        // ��ʼ��һ��AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // ��ѡ�������������Ӳ���
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // ��ѡ�����ô����������ַ, http��socket��ѡһ�����߾�������
//        client.setHttpProxy("proxy_host", proxy_port);  // ����http����
//        client.setSocketProxy("proxy_host", proxy_port);  // ����socket����

        // ��ѡ������log4j��־�����ʽ���������ã���ʹ��Ĭ������
        // Ҳ����ֱ��ͨ��jvm�����������ô˻�������
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");


        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));
        return res.toString(2);


    }
}
