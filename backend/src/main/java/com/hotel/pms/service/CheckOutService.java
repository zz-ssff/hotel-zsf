package com.hotel.pms.service;

import com.hotel.pms.entity.Order;

import java.util.Map;

public interface CheckOutService {
    /**
     * 办理退房
     */
    Map<String, Object> checkOut(Long orderId);

    /**
     * 计算费用
     */
    double calculateTotalAmount(Long orderId);

    /**
     * 处理支付
     */
    boolean processPayment(Long orderId, String paymentMethod);
}