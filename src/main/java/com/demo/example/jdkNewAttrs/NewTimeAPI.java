package com.demo.example.jdkNewAttrs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class NewTimeAPI {
    private static void testLocalDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前的日期时间
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("当前时间: " + dtf.format(currentTime));

        LocalDate date1 = currentTime.toLocalDate();
        System.out.println("date1: " + date1);

        int month = currentTime.getMonthValue();
        int day = currentTime.getDayOfMonth();
        int seconds = currentTime.getSecond();
        System.out.println("月: " + month + ", 日: " + day + ", 秒: " + seconds);

        // 自定义日期
        LocalDateTime date2 = currentTime.withYear(2012).withMonth(5).withDayOfMonth(10).withHour(5).withMinute(20).withSecond(40);
        System.out.println("date2: " + dtf.format(date2));

        // 12 december 2014
        LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
        System.out.println("date3: " + date3);

        // 22 小时 15 分钟
        LocalTime date4 = LocalTime.of(22, 15, 40);
        System.out.println("date4: " + date4);

        // 解析字符串
        LocalTime date5 = LocalTime.parse("20:15:30");
        System.out.println("date5: " + date5);
    }

    public static void main(String args[]) {
        testLocalDateTime();
    }
}
