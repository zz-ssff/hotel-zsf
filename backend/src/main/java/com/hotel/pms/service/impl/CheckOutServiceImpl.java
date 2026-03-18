package com.hotel.pms.service.impl;

import com.hotel.pms.entity.Order;
import com.hotel.pms.entity.Room;
import com.hotel.pms.service.CheckOutService;
import com.hotel.pms.service.OrderService;
import com.hotel.pms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CheckOutServiceImpl implements CheckOutService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @Override
    @Transactional
    public Map<String, Object> checkOut(Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return null;
        }

        // 计算总费用
        double totalAmount = calculateTotalAmount(orderId);

        // 更新订单状态
        order.setStatus("completed");
        order.setPaymentStatus("paid");
        order.setUpdateTime(new Date());
        orderService.updateById(order);

        // 更新房间状态为打扫中
        Room room = roomService.getById(order.getRoomId());
        if (room != null) {
            room.setStatus(4);
            roomService.updateById(room);
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", order.getOrderNo());
        result.put("totalAmount", totalAmount);
        result.put("checkInDate", order.getCheckInDate());
        result.put("checkOutDate", order.getCheckOutDate());
        result.put("days", order.getDays());

        return result;
    }

    @Override
    public double calculateTotalAmount(Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return 0;
        }

        // 基础房费
        double roomCharge = order.getTotalPrice();

        // 这里可以添加其他费用计算逻辑，比如服务费、税费等

        return roomCharge;
    }

    @Override
    @Transactional
    public boolean processPayment(Long orderId, String paymentMethod) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }

        // 更新支付状态
        order.setPaymentStatus("paid");
        order.setPaymentMethod(paymentMethod);
        order.setUpdateTime(new Date());
        orderService.updateById(order);

        return true;
    }
}