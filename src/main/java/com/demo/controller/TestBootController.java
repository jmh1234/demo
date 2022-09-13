package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.annotation.AdviceAspect;
import com.demo.constant.ProgramConstant;
import com.demo.entity.Pagination;
import com.demo.entity.RespJson;
import com.demo.entity.ResponseResult;
import com.demo.entity.User;
import com.demo.service.UserService;
import com.demo.util.LoggerUtil;
import com.demo.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Test启动类
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@Api(tags = "1 - 接口测试")
@ApiSort(value = 1)
@RestController
public class TestBootController {

    private static final String ERR_MSG = "执行操作失败!";

    private static final Logger logger = LoggerUtil.getInstance(TestBootController.class);

    @Resource
    private UserService userService;

    @GetMapping("/showUser")
    @ResponseBody
    @AdviceAspect(description = "我只是想获得用户的信息")
    @ApiOperation(value = "1 - 获得用户信息")
    @ApiOperationSupport(order = 1)
    public RespJson showUserInfo(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String tel = request.getParameter("tel");
            String address = request.getParameter("address");
            User user = new User(id, name, tel, address);
            Map<String, Integer> pageNumAndPageSize = Utils.getPageNumAndPageSize(request);
            Pagination<User> userInfoList = userService.getUserById(user, pageNumAndPageSize.get("offset"), pageNumAndPageSize.get("pageSize"));
            return new RespJson(true, "获取用户信息成功!", ProgramConstant.SUCCESS, userInfoList);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return new RespJson(false, ERR_MSG, ProgramConstant.ERROR, null);
        }
    }

    @GetMapping("/addUserInfo")
    @ResponseBody
    @AdviceAspect(description = "我只是想批量加点用户的信息")
    @ApiOperation(value = "2 - 批量添加用户信息")
    @ApiOperationSupport(order = 2)
    public void addUserInfo() {
        try {
            String address = "addr-abcd";
            List<User> users = Arrays.asList(
                    new User(null, "abcd", "tel-abcd-1", address),
                    new User(null, "abcd1", "tel-abcd-2", address),
                    new User(null, "abcd2", "tel-abcd-3", address),
                    new User(null, "abcd3", "tel-abcd-4", address),
                    new User(null, "abcd4", "tel-abcd-6", address));
            userService.addUserInfo(users);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    @PostMapping("/sendMsg2Server")
    @ResponseBody
    @AdviceAspect(description = "我只是想向服务器发送一条消息")
    @ApiOperation(value = "3 - 向服务器发送一条消息")
    @ApiOperationSupport(order = 3)
    public RespJson sendMsg2Server(HttpServletRequest request) {
        try {
            String url = "http://localhost:8088/callback";
            logger.info("向接口 {} 发起了请求", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(request.getParameter("id"), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            ResponseResult resPr = JSON.parseObject(responseEntity.getBody(), ResponseResult.class);

            // 记录返回的信息
            if (resPr != null && ProgramConstant.ZERO.equals(resPr.getResCode().trim())) {
                return new RespJson(true, "执行操作成功!", ProgramConstant.SUCCESS, null);
            } else {
                return new RespJson(false, ERR_MSG, ProgramConstant.ERROR, null);
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return new RespJson(false, ERR_MSG, ProgramConstant.ERROR, null);
        }
    }

    @PostMapping("/callback")
    @ResponseBody
    @AdviceAspect(description = "我只是服务器回复的一条消息")
    @ApiOperation(value = "4 - 服务器回复的一条消息")
    @ApiOperationSupport(order = 4)
    public void callback(HttpServletResponse response, HttpEntity<byte[]> requestEntity) {
        try {
            // 请求接收传递过来的参数
            String requestJson = requestEntity.getBody() == null ? "" : new String(requestEntity.getBody(), StandardCharsets.UTF_8);

            // 对接收到的数据的处理
            logger.info("数据接收成功! 接收的数据为: {}", requestJson);

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

    @Resource
    RestHighLevelClient elasticsearchClient;

    @PostMapping("/addIndex")
    @ResponseBody
    @AdviceAspect(description = "添加索引")
    @ApiOperation(value = "5 - 添加索引")
    @ApiOperationSupport(order = 5)
    public void addIndex(HttpServletResponse response) {
        try {
            logger.info("数据接收成功! 接收的数据为: {}", elasticsearchClient);
            JSONObject outObj = new JSONObject();
            outObj.put("res_code", "0");
            outObj.put("res_msg", "SUCCESS");
            output(response, outObj);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }
}
