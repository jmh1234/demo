package com.demo.entity;

import com.alibaba.fastjson.JSONObject;
import com.demo.util.Constant;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户后台向前台返回的JSON对象
 */
@Setter
@Getter
public class RespJson implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger Logger = LoggerFactory.getLogger(RespJson.class);

    private Integer code;
    private String msg = "";
    private Object data = null;
    private boolean success = false;

    public RespJson() {
    }

    public RespJson(Boolean success, String msg, int code, Object obj) {
        this.success = success;
        this.msg = msg;
        this.code = code;
        this.data = obj;
    }

    /**
     * 将传递过来的base64参数转jsonObject
     *
     * @param req 页面请求参数
     * @return RespJson
     */
    public static RespJson convertJson(HttpServletRequest req) {
        RespJson json = new RespJson();
        try {
            String sign = req.getParameter("sign");
            if (StringUtils.isEmpty(sign)) {
                json.setMsg("解析请求参数异常，请求参数为空");
                json.setSuccess(false);
                json.setCode(Constant.ERROR);
                Logger.error("解析请求参数异常，请求参数为空");
                return json;
            }
            JSONObject o = JSONObject.parseObject(sign);
            json.setData(o);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setMsg("解析请求参数异常..." + e.getMessage());
            json.setSuccess(false);
            json.setCode(Constant.ERROR);
            Logger.error("解析请求参数异常", e);
        }
        return json;
    }

    @Override
    public String toString() {
        return "RespJson [success=" + success + ", msg=" + msg + ", data=" + data + ", code=" + code + "]";
    }
}
