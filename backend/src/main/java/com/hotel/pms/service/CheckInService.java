package com.hotel.pms.service;

import com.hotel.pms.entity.Order;

public interface CheckInService {
    /**
     * 办理入住
     */
    boolean checkIn(Long orderId);

    /**
     * 续住
     */
    boolean extendStay(Long orderId, int additionalDays);

    /**
     * 换房
     */
    boolean changeRoom(Long orderId, Long newRoomId);
}