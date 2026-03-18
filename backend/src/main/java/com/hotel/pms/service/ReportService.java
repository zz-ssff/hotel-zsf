package com.hotel.pms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 获取每日营收报表
     */
    List<Map<String, Object>> getDailyRevenueReport(Date startDate, Date endDate);

    /**
     * 获取客房出租率报表
     */
    List<Map<String, Object>> getOccupancyRateReport(Date startDate, Date endDate);

    /**
     * 获取平均房价报表
     */
    List<Map<String, Object>> getAveragePriceReport(Date startDate, Date endDate);

    /**
     * 获取会员消费报表
     */
    List<Map<String, Object>> getMemberConsumptionReport(Date startDate, Date endDate);

    /**
     * 获取关键指标汇总
     */
    Map<String, Object> getKeyIndicators();
}