package com.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * http请求工具类
 *
 * @author cheng sn
 * @since 2021-04-15 16:10
 */
public class HttpUtils {

    private static final String KEY_ID = "1300319277300425948";
    private static final String KEY_SECRET = "be19b863fe06420a8a730e8bc04f265f";
    private static final String REQUEST_IP_PORT_INFO = "https://api.ginlong.com:13333";

    private HttpUtils() {

    }

    /**
     * 发送httpPost请求
     *
     * @param request 请求的接口名称
     * @param body    请求体
     * @return http请求结果
     * @throws InvalidKeyException      InvalidKeyException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws IOException              IOException
     */
    public static String sendPostRequest(String request, String body) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        final String date = getGmtTime();
        final String contentMd5 = getDigest(body);
        String encryptText = "POST\n" + contentMd5 + "\napplication/json\n" + date + "\n" + request;
        final String sign = HttpUtils.hMacSha1Encrypt(encryptText, KEY_SECRET);
        String url = REQUEST_IP_PORT_INFO + request;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        String authorization = "API " + KEY_ID + ":" + sign;
        return HttpUtils.executeHttpRequest(httpPost, authorization, contentMd5, date);
    }

    /**
     * 将http的返回报文转化为实体类对象
     *
     * @param content https请求内容
     * @param clazz   实体类
     * @param <T>     范型
     * @return 处理后的数据
     */
    public static <T> List<T> handleResponseMessage(String content, Class<T> clazz) {
        final JSONObject jsonObject = JSON.parseObject(content);
        final JSONArray records = jsonObject.getJSONObject("data").getJSONObject("page").getJSONArray("records");
        List<T> resultList = new ArrayList<>();
        for (Object object : records) {
            resultList.add(JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(object)), clazz));
        }
        return resultList;
    }

    /**
     * 获取请求的所需的标准时间
     *
     * @return 标准时间
     */
    public static String getGmtTime() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cd.getTime());
    }

    /**
     * 1.先计算MD5加密的二进制数组(128位)。
     * 2.再对这个二进制进行 base64 编码(而不是对 32 位字符串编码)
     *
     * @param plainText 加密明文
     * @return 加密密文
     */
    public static String getDigest(String plainText) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            final byte[] encode = Base64.getEncoder().encode(b);
            result = new String(encode);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 请求体加密
     *
     * @param encryptText 需加密的文本
     * @param keySecret   签名所需的密钥
     * @return 加密后的签名
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws InvalidKeyException      InvalidKeyException
     */
    public static String hMacSha1Encrypt(String encryptText, String keySecret) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = keySecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(StandardCharsets.UTF_8);
        byte[] result = mac.doFinal(text);
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 执行http请求
     *
     * @param httpPost      httpPost对象
     * @param authorization 签名
     * @param contentMd5    加密密文
     * @param date          日期
     * @return http执行结果
     * @throws IOException IOException
     */
    public static String executeHttpRequest(HttpPost httpPost, String authorization, String contentMd5, String date) throws IOException {
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", authorization);
        httpPost.addHeader("Content-MD5", contentMd5);
        httpPost.addHeader("Date", date);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        }
    }
}
