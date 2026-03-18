package com.hotel.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.pms.entity.Customer;
import com.hotel.pms.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Tag(name = "客户管理", description = "客户相关接口")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    @Operation(summary = "获取所有客户", description = "获取酒店所有客户信息")
    public List<Customer> list() {
        return customerService.list();
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取客户", description = "分页获取酒店客户信息")
    public IPage<Customer> page(
            @Parameter(description = "当前页码", example = "1") @RequestParam int current,
            @Parameter(description = "每页大小", example = "10") @RequestParam int size) {
        return customerService.page(new Page<>(current, size));
    }

    @PostMapping("/save")
    @Operation(summary = "保存客户", description = "新增或保存客户信息")
    public boolean save(@Parameter(description = "客户信息") @RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PutMapping("/update")
    @Operation(summary = "更新客户", description = "更新客户信息")
    public boolean update(@Parameter(description = "客户信息") @RequestBody Customer customer) {
        return customerService.updateById(customer);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    public boolean delete(@Parameter(description = "客户ID", example = "1") @PathVariable Long id) {
        return customerService.removeById(id);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取客户", description = "根据ID获取客户信息")
    public Customer get(@Parameter(description = "客户ID", example = "1") @PathVariable Long id) {
        return customerService.getById(id);
    }
}