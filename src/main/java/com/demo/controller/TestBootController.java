package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.annotation.AdviceAspect;
import com.demo.entity.Pagination;
import com.demo.entity.RespJson;
import com.demo.entity.ResponseResult;
import com.demo.entity.User;
import com.demo.service.UserService;
import com.demo.util.Constant;
import com.demo.util.LoggerUtil;
import com.demo.util.Utils;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class TestBootController {

    private static final Logger logger = LoggerUtil.getInstance(TestBootController.class);

    @Resource
    private UserService userService;

    @RequestMapping("/showUser")
    @ResponseBody
    @AdviceAspect(description = "我只是想获得用户的信息")
    public RespJson showUserInfo(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String tel = request.getParameter("tel");
            String address = request.getParameter("address");
            User user = new User(id, name, tel, address);
            Map<String, Integer> pageNumAndPageSize = Utils.getPageNumAndPageSize(request);
            Pagination<User> userInfoList = userService.getUserById(user, pageNumAndPageSize.get("offset"), pageNumAndPageSize.get("pageSize"));
            return new RespJson(true, "获取用户信息成功!", Constant.SUCCESS, userInfoList);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return new RespJson(false, "执行操作失败!", Constant.ERROR, null);
        }
    }

    @RequestMapping("/addUserInfo")
    @ResponseBody
    @AdviceAspect(description = "我只是想批量加点用户的信息")
    public void addUserInfo() {
        try {
            List<User> users = Arrays.asList(
                    new User(null, "abcd", "tel-abcd-1", "addr-abcd"),
                    new User(null, "abcd1", "tel-abcd-2", "addr-abcd"),
                    new User(null, "abcd2", "tel-abcd-3", "addr-abcd"),
                    new User(null, "abcd3", "tel-abcd-4", "addr-abcd"),
                    new User(null, "abcd4", "tel-abcd-6", "addr-abcd"));
            userService.addUserInfo(users);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    @RequestMapping("/sendMsg2Server")
    @ResponseBody
    @AdviceAspect(description = "我只是想向服务器发送一条消息")
    public RespJson sendMsg2Server(HttpServletRequest request) {
        try {
            String url = "http://localhost:8088/callback"; // 请求的URL
            logger.info("向接口 " + url + " 发起了请求");
            HttpHeaders headers = new HttpHeaders();  // 创建请求头
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(request.getParameter("id"), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            ResponseResult resPr = JSON.parseObject(responseEntity.getBody(), ResponseResult.class);

            // 记录返回的信息
            if (resPr != null && "0".equals(resPr.getRes_code().trim())) {
                return new RespJson(true, "执行操作成功!", Constant.SUCCESS, null);
            } else {
                return new RespJson(false, "执行操作失败!", Constant.ERROR, null);
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return new RespJson(false, "执行操作失败!", Constant.ERROR, null);
        }
    }

    @RequestMapping("/callback")
    @ResponseBody
    @AdviceAspect(description = "我只是服务器回复的一条消息")
    public void callback(HttpServletResponse response, HttpEntity<byte[]> requestEntity) {
        try {
            // 请求接收传递过来的参数
            String requestJson = requestEntity.getBody() == null ? "" : new String(requestEntity.getBody(), "UTF-8");

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
