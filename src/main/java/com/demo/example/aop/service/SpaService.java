package com.demo.example.aop.service;

/**
 * Created with IntelliJ IDEA.
 * SpaService
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public interface SpaService {
    /**
     * 精油保健
     *
     * @param customer customer
     */
    void aromaOilMessage(String customer);

    /**
     * 重设
     */
    void rest();
}
