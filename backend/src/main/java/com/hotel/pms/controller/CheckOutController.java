package com.hotel.pms.controller;

import com.hotel.pms.service.CheckOutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@Tag(name = "退房管理", description = "退房相关接口")
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;

    @PutMapping("/process/{id}")
    @Operation(summary = "处理退房", description = "根据订单ID处理退房")
    public Map<String, Object> processCheckOut(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return checkOutService.checkOut(id);
    }

    @GetMapping("/calculate/{id}")
    @Operation(summary = "计算总金额", description = "根据订单ID计算总金额")
    public double calculateTotalAmount(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return checkOutService.calculateTotalAmount(id);
    }

    @PutMapping("/payment/{id}")
    @Operation(summary = "处理支付", description = "根据订单ID处理支付")
    public boolean processPayment(
            @Parameter(description = "订单ID", example = "1") @PathVariable Long id,
            @Parameter(description = "支付方式", example = "微信支付") @RequestParam String paymentMethod) {
        return checkOutService.processPayment(id, paymentMethod);
    }
}