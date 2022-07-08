package com.demo.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Ji MingHao
 * @since 2022-07-07 17:27
 */
public class Test {

    private static final String KEY_ID = "2424";
    private static final String KEY_SECRET = "6680182547";

    public static void main(String[] args) {
        String request = "/v1/api/userStationList";
        String body = "{\"pageNo\":1,\"pageSize\":10}";
        try {
            final String response = HttpUtils.senPostRequest(KEY_ID, KEY_SECRET, request, body);
            System.out.println(response);
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}
