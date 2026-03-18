package com.hotel.pms.controller;

import com.hotel.pms.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@Tag(name = "报表管理", description = "报表相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/daily-revenue")
    @Operation(summary = "获取日收入报表", description = "获取指定日期范围内的日收入报表")
    public List<Map<String, Object>> getDailyRevenueReport(
            @Parameter(description = "开始日期", example = "2024-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", example = "2024-01-31") @RequestParam String endDate) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return reportService.getDailyRevenueReport(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/occupancy-rate")
    @Operation(summary = "获取入住率报表", description = "获取指定日期范围内的入住率报表")
    public List<Map<String, Object>> getOccupancyRateReport(
            @Parameter(description = "开始日期", example = "2024-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", example = "2024-01-31") @RequestParam String endDate) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return reportService.getOccupancyRateReport(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/average-price")
    @Operation(summary = "获取平均价格报表", description = "获取指定日期范围内的平均价格报表")
    public List<Map<String, Object>> getAveragePriceReport(
            @Parameter(description = "开始日期", example = "2024-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", example = "2024-01-31") @RequestParam String endDate) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return reportService.getAveragePriceReport(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/member-consumption")
    @Operation(summary = "获取会员消费报表", description = "获取指定日期范围内的会员消费报表")
    public List<Map<String, Object>> getMemberConsumptionReport(
            @Parameter(description = "开始日期", example = "2024-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", example = "2024-01-31") @RequestParam String endDate) {
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            return reportService.getMemberConsumptionReport(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/key-indicators")
    @Operation(summary = "获取关键指标", description = "获取酒店关键经营指标")
    public Map<String, Object> getKeyIndicators() {
        return reportService.getKeyIndicators();
    }
}