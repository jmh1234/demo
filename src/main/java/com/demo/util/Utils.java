package com.demo.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private Utils() {

    }

    public static Map<String, Integer> getPageNumAndPageSize(HttpServletRequest request) {
        Map<String, Integer> resultMap = new HashMap<>();
        int pageSize;
        if (request.getParameter("pageSize") == null) pageSize = 1;
        else pageSize = Integer.valueOf(request.getParameter("pageSize"));

        int num;
        if (request.getParameter("pageNum") == null) num = 1;
        else num = Integer.valueOf(request.getParameter("pageNum"));
        int pageNum = pageSize == 1 ? num * pageSize : (num - 1) * pageSize;
        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        return resultMap;
    }
}
