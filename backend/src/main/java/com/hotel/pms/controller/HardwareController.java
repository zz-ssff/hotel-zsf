package com.hotel.pms.controller;

import com.hotel.pms.service.HardwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hardware")
@Tag(name = "硬件管理", description = "硬件相关接口")
public class HardwareController {

    @Autowired
    private HardwareService hardwareService;

    @GetMapping("/read-idcard")
    @Operation(summary = "读取身份证", description = "读取身份证信息")
    public Map<String, String> readIdCard() {
        return hardwareService.readIdCard();
    }

    @PostMapping("/issue-card")
    @Operation(summary = "发放房卡", description = "发放房卡")
    public boolean issueRoomCard(
            @Parameter(description = "房间ID", example = "1") @RequestParam Long roomId,
            @Parameter(description = "客户姓名", example = "张三") @RequestParam String customerName,
            @Parameter(description = "身份证号", example = "110101199001011234") @RequestParam String idCard) {
        return hardwareService.issueRoomCard(roomId, customerName, idCard);
    }

    @PostMapping("/open-door")
    @Operation(summary = "开门", description = "根据房间ID和卡号开门")
    public boolean openDoor(
            @Parameter(description = "房间ID", example = "1") @RequestParam Long roomId,
            @Parameter(description = "卡号", example = "1234567890") @RequestParam String cardId) {
        return hardwareService.openDoor(roomId, cardId);
    }
}