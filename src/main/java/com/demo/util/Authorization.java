package com.demo.util;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Authorization {
    public static void main(String[] args) {
        try {
            String key = "1300319277300425948";
            String keySecret = "be19b863fe06420a8a730e8bc04f265f";
            Map<String, Object> map = new HashMap();
            map.put("pageNo", 1);
            map.put("pageSize", 10);
            String body = JSON.toJSONString(map);
            String ContentMd5 = getDigest(body);
            String Date = getGMTTime();
            String path = "/v1/api/userStationList";
            String param = "POST" + "\n" + ContentMd5 + "\n" + "application/json" + "\n" + Date + "\n" + path;
            String sign = HmacSHA1Encrypt(param, keySecret);
            String url = "https://api.ginlong.com:13333" + path;
            OkHttpClient client = new OkHttpClient();
            MediaType xmlType = MediaType.parse("application/json;charset=UTF-8");
            okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(xmlType, body);
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .addHeader("Content-type", "application/json;charset=UTF-8")
                    .addHeader("Authorization", "API " + key + ":" + sign)
                    .addHeader("Content-MD5", ContentMd5)
                    .addHeader("Date", Date)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String HmacSHA1Encrypt(String encryptText, String KeySecret) throws Exception {
        byte[] data = KeySecret.getBytes("UTF-8");
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes("UTF-8");
        //完成 Mac 操作
        byte[] result = mac.doFinal(text);
        return Base64.encodeBase64String(result);
    }

    public static String getGMTTime() {

        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String str = sdf.format(cd.getTime());
        return str;
    }

    public static String getDigest(String test) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(test.getBytes());
            byte[] b = md.digest();
            result = java.util.Base64.getEncoder().encodeToString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
