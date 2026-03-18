package com.hotel.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.pms.entity.Order;
import com.hotel.pms.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @Operation(summary = "获取所有订单", description = "获取酒店所有订单信息")
    public List<Order> list() {
        return orderService.list();
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取订单", description = "分页获取酒店订单信息")
    public IPage<Order> page(
            @Parameter(description = "当前页码", example = "1") @RequestParam int current,
            @Parameter(description = "每页大小", example = "10") @RequestParam int size) {
        return orderService.page(new Page<>(current, size));
    }

    @PostMapping("/save")
    @Operation(summary = "保存订单", description = "新增或保存订单信息")
    public boolean save(@Parameter(description = "订单信息") @RequestBody Order order) {
        return orderService.save(order);
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单", description = "更新订单信息")
    public boolean update(@Parameter(description = "订单信息") @RequestBody Order order) {
        return orderService.updateById(order);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除订单", description = "根据ID删除订单")
    public boolean delete(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return orderService.removeById(id);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取订单", description = "根据ID获取订单信息")
    public Order get(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return orderService.getById(id);
    }
}