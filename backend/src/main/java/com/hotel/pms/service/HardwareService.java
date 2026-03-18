package com.hotel.pms.service;

import java.util.Map;

public interface HardwareService {
    /**
     * 读取身份证信息
     */
    Map<String, String> readIdCard();

    /**
     * 制发门卡
     */
    boolean issueRoomCard(Long roomId, String customerName, String idCard);

    /**
     * 开门
     */
    boolean openDoor(Long roomId, String cardId);
}