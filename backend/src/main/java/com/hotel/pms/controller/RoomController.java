package com.hotel.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.pms.entity.Room;
import com.hotel.pms.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@Tag(name = "房间管理", description = "房间相关接口")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    @Operation(summary = "获取所有房间", description = "获取酒店所有房间信息")
    public List<Room> list() {
        return roomService.list();
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取房间", description = "分页获取酒店房间信息")
    public IPage<Room> page(
            @Parameter(description = "当前页码", example = "1") @RequestParam int current,
            @Parameter(description = "每页大小", example = "10") @RequestParam int size) {
        return roomService.page(new Page<>(current, size));
    }

    @PostMapping("/save")
    @Operation(summary = "保存房间", description = "新增或保存房间信息")
    public boolean save(@Parameter(description = "房间信息") @RequestBody Room room) {
        return roomService.save(room);
    }

    @PutMapping("/update")
    @Operation(summary = "更新房间", description = "更新房间信息")
    public boolean update(@Parameter(description = "房间信息") @RequestBody Room room) {
        return roomService.updateById(room);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除房间", description = "根据ID删除房间")
    public boolean delete(@Parameter(description = "房间ID", example = "1") @PathVariable Long id) {
        return roomService.removeById(id);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取房间", description = "根据ID获取房间信息")
    public Room get(@Parameter(description = "房间ID", example = "1") @PathVariable Long id) {
        return roomService.getById(id);
    }
}