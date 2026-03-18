package com.hotel.pms.service;

import com.hotel.pms.entity.Order;

public interface BookingService {
    /**
     * 创建预订
     */
    Order createBooking(Order order);

    /**
     * 取消预订
     */
    boolean cancelBooking(Long orderId);

    /**
     * 确认预订
     */
    boolean confirmBooking(Long orderId);
}