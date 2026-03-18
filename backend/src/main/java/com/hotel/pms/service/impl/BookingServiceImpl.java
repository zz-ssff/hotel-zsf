package com.hotel.pms.service.impl;

import com.hotel.pms.entity.Order;
import com.hotel.pms.entity.Room;
import com.hotel.pms.service.BookingService;
import com.hotel.pms.service.OrderService;
import com.hotel.pms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @Override
    @Transactional
    public Order createBooking(Order order) {
        // 生成订单号
        order.setOrderNo(generateOrderNo());
        // 设置初始状态
        order.setStatus("pending");
        order.setPaymentStatus("unpaid");
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());

        // 保存订单
        orderService.save(order);

        // 更新房间状态为已预订
        Room room = roomService.getById(order.getRoomId());
        if (room != null) {
            room.setStatus(2); // 2: 已预订
            roomService.updateById(room);
        }

        return order;
    }

    @Override
    @Transactional
    public boolean cancelBooking(Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }

        // 更新订单状态
        order.setStatus("cancelled");
        order.setUpdateTime(new Date());
        orderService.updateById(order);

        // 更新房间状态为空闲
        Room room = roomService.getById(order.getRoomId());
        if (room != null) {
            room.setStatus(1); // 1: 空闲
            roomService.updateById(room);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean confirmBooking(Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }

        // 更新订单状态
        order.setStatus("confirmed");
        order.setUpdateTime(new Date());
        orderService.updateById(order);

        return true;
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}