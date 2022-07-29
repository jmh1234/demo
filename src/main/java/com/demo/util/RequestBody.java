package com.demo.util;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * 请求实体类
 *
 * @author Ji MingHao
 * @since 2022-07-29 17:44
 */
@Data
public class RequestBody {

    /**
     * 当前页数
     */
    int pageNo;

    /**
     * 每页数量
     */
    int pageSize;

    /**
     * nmi 码
     */
    String nmiCode;

    /**
     * id,不同接口的表示不同,详见文档
     */
    Long id;

    /**
     * 电站 id
     */
    Long stationId;

    /**
     * EPM sn
     */
    String sn;

    /**
     * 电站货币单位
     */
    String money;

    /**
     * 时间,不同接口要求的时间格式化不一样,详见文档
     */
    String time;

    /**
     * 电站时区
     */
    Integer timeZone;

    /**
     * 时间
     */
    String month;

    /**
     * 时间
     */
    String year;

    /**
     * 是否删除逆变器
     */
    Integer deleteInvert;

    /**
     * 逆变器 SN
     */
    String alarmDeviceSn;

    /**
     * 报警开始时间
     */
    String alarmBeginTime;

    /**
     * 报警结束时间
     */
    String alarmEndTime;

    /**
     * 详见文档
     */
    String searchinfo;
}
