package com.hotel.pms.service.impl;

import com.hotel.pms.entity.Order;
import com.hotel.pms.entity.Room;
import com.hotel.pms.service.CheckInService;
import com.hotel.pms.service.OrderService;
import com.hotel.pms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CheckInServiceImpl implements CheckInService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomService roomService;

    @Override
    @Transactional
    public boolean checkIn(Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }

        // 更新订单状态
        order.setStatus("checkin");
        order.setUpdateTime(new Date());
        orderService.updateById(order);

        // 更新房间状态为已入住
        Room room = roomService.getById(order.getRoomId());
        if (room != null) {
            room.setStatus(3); // 3: 已入住
            roomService.updateById(room);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean extendStay(Long orderId, int additionalDays) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }

        // 计算新的退房时间
        Date newCheckOutDate = new Date(order.getCheckOutDate().getTime() + additionalDays * 24 * 60 * 60 * 1000);
        order.setCheckOutDate(newCheckOutDate);
        order.setDays(order.getDays() + additionalDays);

        // 计算额外费用
        Room room = roomService.getById(order.getRoomId());
        if (room != null) {
            double additionalAmount = room.getPrice() * additionalDays;
            order.setTotalPrice(order.getTotalPrice() + additionalAmount);
        }

        order.setUpdateTime(new Date());
        orderService.updateById(order);

        return true;
    }

    @Override
    @Transactional
    public boolean changeRoom(Long orderId, Long newRoomId) {
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }

        // 检查新房间是否可用
        Room newRoom = roomService.getById(newRoomId);
        if (newRoom == null || newRoom.getStatus() != 1) { // 1: 空闲
            return false;
        }

        // 更新原房间状态为空闲
        Room oldRoom = roomService.getById(order.getRoomId());
        if (oldRoom != null) {
            oldRoom.setStatus(1); // 1: 空闲
            roomService.updateById(oldRoom);
        }

        // 更新订单中的房间ID
        order.setRoomId(newRoomId);
        // 更新房间价格差异
        double priceDifference = newRoom.getPrice() - oldRoom.getPrice();
        order.setTotalPrice(order.getTotalPrice() + priceDifference * order.getDays());
        order.setUpdateTime(new Date());
        orderService.updateById(order);

        // 更新新房间状态为已入住
        newRoom.setStatus(3); // 3: 已入住
        roomService.updateById(newRoom);

        return true;
    }
}