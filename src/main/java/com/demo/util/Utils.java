package com.demo.util;

import com.demo.constant.ProgramConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 工具类
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public class Utils {

    private Utils() {

    }

    public static Map<String, Integer> getPageNumAndPageSize(HttpServletRequest request) {
        String pageSize = "pageSize";
        String pageNum = "pageNum";
        Map<String, Integer> resultMap = new HashMap<>(ProgramConstant.MAP_INIT_CAPACITY);
        int size = request.getParameter(pageSize) == null ? 1 : Integer.parseInt(request.getParameter(pageSize));
        int num = request.getParameter(pageNum) == null ? 1 : Integer.parseInt(request.getParameter(pageNum));
        int offset = size == 1 ? num * size : (num - 1) * size;
        resultMap.put("offset", offset);
        resultMap.put("pageSize", size);
        return resultMap;
    }
}
