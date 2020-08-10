package com.test.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.demo.annotation.AdviceAspect;
import com.test.demo.domain.ResponseResult;
import com.test.demo.domain.User;
import com.test.demo.service.UserService;
import com.test.demo.util.*;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class TestBootController {

    private static Logger logger = LoggerUtil.getInstance(TestBootController.class);

    @Resource
    private UserService userService;

    @RequestMapping("/showUser")
    @ResponseBody
    @AdviceAspect(description = "我只是想获得用户的信息")
    public RespJson showUserInfo(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            String tel = request.getParameter("tel");
            String address = request.getParameter("address");
            User user = new User(id, tel, address);
            Map<String, Integer> pageNumAndPageSize = Utils.getPageNumAndPageSize(request);
            Pagination<User> userInfoList = userService.getUserById(user, pageNumAndPageSize.get("pageNum"), pageNumAndPageSize.get("pageSize"));
            return new RespJson(true, "获取用户信息成功!", ResultCode.SUCCESS, userInfoList);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return new RespJson(false, "执行操作失败!", ResultCode.ERROR, null);
        }
    }

    @GetMapping("/api/validateUser")
    public ResponesCode validate(@RequestParam(name = "token") String token) {
        ResponesCode responesCode = new ResponesCode();
        if (Constant.tokeners.size() > 0) {
            for (JWTTokener tokener : Constant.tokeners) {
                if (tokener.getToken().equals(token)) {
                    responesCode.setStatus(true);
                } else {
                    responesCode.setStatus(false);
                }
            }
        }
        return responesCode;
    }

    @RequestMapping("/sendMsg2Server")
    @ResponseBody
    @AdviceAspect(description = "我只是想向服务器发送一条消息")
    public RespJson sendMsg2Server(HttpServletRequest request) {
        try {
            String url = "http://localhost:8088/callback"; // 请求的URL
            logger.info("向接口 " + url + " 发起了请求");
            HttpHeaders headers = new HttpHeaders();  // 创建请求头
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity<>(request.getParameter("id"), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            ResponseResult resPr = JSON.parseObject(responseEntity.getBody(), ResponseResult.class);

            // 记录返回的信息
            if (!"0".equals(resPr.getRes_code().trim())) {
                return new RespJson(false, "执行操作失败: " + resPr.getRes_msg(), ResultCode.ERROR, null);
            } else {
                return new RespJson(true, "执行操作成功!", ResultCode.SUCCESS, null);
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return new RespJson(false, "执行操作失败!", ResultCode.ERROR, null);
        }
    }

    @RequestMapping("/callback")
    @ResponseBody
    @AdviceAspect(description = "我只是服务器回复的一条消息")
    public void callback(HttpServletResponse response, HttpEntity<byte[]> requestEntity) {
        try {
            // 请求接收传递过来的参数
            String requestJson = new String(requestEntity.getBody(), "UTF-8");

            // 对接收到的数据的处理
            logger.info("数据接收成功! 接收的数据为: " + requestJson);

            // 设置返回的状态
            JSONObject outObj = new JSONObject();
            outObj.put("res_code", "0");
            outObj.put("res_msg", "SUCCESS");
            output(response, outObj);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    private void output(HttpServletResponse response, JSONObject jsonObject) throws IOException {
        response.getOutputStream().write(jsonObject.toJSONString().getBytes());
        response.getOutputStream().flush();
    }
}
