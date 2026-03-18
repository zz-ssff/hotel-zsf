package com.hotel.pms.service.impl;

import com.hotel.pms.service.HardwareService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class HardwareServiceImpl implements HardwareService {

    @Override
    public Map<String, String> readIdCard() {
        // 模拟身份证读卡器读取信息
        Map<String, String> idCardInfo = new HashMap<>();
        idCardInfo.put("name", "张三");
        idCardInfo.put("idCard", "110101199001011234");
        idCardInfo.put("gender", "男");
        idCardInfo.put("birthDate", "1990-01-01");
        idCardInfo.put("address", "北京市朝阳区");
        return idCardInfo;
    }

    @Override
    public boolean issueRoomCard(Long roomId, String customerName, String idCard) {
        // 模拟制发门卡
        System.out.println("制发门卡：房间" + roomId + "，客户" + customerName + "，身份证" + idCard);
        // 模拟成功
        return true;
    }

    @Override
    public boolean openDoor(Long roomId, String cardId) {
        // 模拟开门
        System.out.println("开门：房间" + roomId + "，门卡" + cardId);
        // 模拟成功
        return true;
    }
}