package com.hotel.pms.controller;

import com.hotel.pms.service.CheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkin")
@Tag(name = "入住管理", description = "入住相关接口")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @PutMapping("/process/{id}")
    @Operation(summary = "处理入住", description = "根据订单ID处理入住")
    public boolean processCheckIn(@Parameter(description = "订单ID", example = "1") @PathVariable Long id) {
        return checkInService.checkIn(id);
    }

    @PutMapping("/extend/{id}")
    @Operation(summary = "延长入住", description = "根据订单ID延长入住时间")
    public boolean extendStay(
            @Parameter(description = "订单ID", example = "1") @PathVariable Long id,
            @Parameter(description = "额外天数", example = "2") @RequestParam int additionalDays) {
        return checkInService.extendStay(id, additionalDays);
    }

    @PutMapping("/change-room/{id}")
    @Operation(summary = "更换房间", description = "根据订单ID更换房间")
    public boolean changeRoom(
            @Parameter(description = "订单ID", example = "1") @PathVariable Long id,
            @Parameter(description = "新房间ID", example = "102") @RequestParam Long newRoomId) {
        return checkInService.changeRoom(id, newRoomId);
    }
}