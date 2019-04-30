package com.xcbeyond.common;

import org.junit.Test;

/**
 * com.xcbeyond.common.DateUtils测试类
 * @Auther: xcbeyond
 * @Date: 2019/4/30 15:33
 */
public class DateUtilsTest {

    @Test
    public void getCurrentDate() {
        System.out.println(DateUtils.getCurrentDate());

        //指定时间格式，获取当前时间
        System.out.println(DateUtils.getCurrentDate("yyyy-MM-dd HH"));
    }

    /**
     * 取得某年某月有多少天
     */
    @Test
    public void getDaysOfMonth() {
        String ym = "201904";
        System.out.println(DateUtils.getDaysOfMonth(ym));
    }
}