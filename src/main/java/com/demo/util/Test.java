package com.demo.util;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Ji MingHao
 * @since 2022-07-07 17:27
 */
public class Test {

    public static void main(String[] args) {
        String request = "/v1/api/inveterList";
        try {
            RequestBody requestBody = new RequestBody();
            requestBody.setPageNo(1);
            requestBody.setPageSize(10);
            final String response = HttpUtils.sendPostRequest(request, JSON.toJSONString(requestBody));
            System.out.println(response);
            final List<StationStatusVo> stationStatusVos = HttpUtils.handleResponseMessage(response, StationStatusVo.class);
            for (StationStatusVo stationStatusVo : stationStatusVos) {
                System.out.println(stationStatusVo.getId());
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}
