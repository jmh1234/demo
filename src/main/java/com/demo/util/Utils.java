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

        int pageNum;
        if (request.getParameter("pageNum") == null) pageNum = 1;
        else pageNum = Integer.valueOf(request.getParameter("pageNum"));
        int offset = pageSize == 1 ? pageNum * pageSize : (pageNum - 1) * pageSize;
        resultMap.put("offset", offset);
        resultMap.put("pageSize", pageSize);
        return resultMap;
    }
}
