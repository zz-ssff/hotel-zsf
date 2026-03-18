package com.hotel.pms.controller;

import com.hotel.pms.entity.Order;
import com.hotel.pms.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@Tag(name = "预订管理", description = "预订相关接口")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    @Operation(summary = "创建预订", description = "创建新的酒店预订")
    public Order createBooking(@Parameter(description = "订单信息") @RequestBody Order order) {
        return bookingService.createBooking(order);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消预订", description = "根据ID取消预订")
    public boolean cancelBooking(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @PutMapping("/confirm/{id}")
    @Operation(summary = "确认预订", description = "根据ID确认预订")
    public boolean confirmBooking(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return bookingService.confirmBooking(id);
    }
}