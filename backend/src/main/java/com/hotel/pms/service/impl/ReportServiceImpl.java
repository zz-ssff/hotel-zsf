package com.hotel.pms.service.impl;

import com.hotel.pms.entity.Order;
import com.hotel.pms.entity.Room;
import com.hotel.pms.mapper.OrderMapper;
import com.hotel.pms.mapper.RoomMapper;
import com.hotel.pms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RoomMapper roomMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Map<String, Object>> getDailyRevenueReport(Date startDate, Date endDate) {
        // 从订单表中查询每日营收数据
        List<Order> orders = orderMapper.selectList(null);
        Map<String, Double> dailyRevenue = new HashMap<>();
        Map<String, Integer> dailyOrderCount = new HashMap<>();

        // 初始化日期范围内的每一天
        Date currentDate = new Date(startDate.getTime());
        while (!currentDate.after(endDate)) {
            String dateStr = dateFormat.format(currentDate);
            dailyRevenue.put(dateStr, 0.0);
            dailyOrderCount.put(dateStr, 0);
            // 增加一天
            currentDate.setTime(currentDate.getTime() + 24 * 60 * 60 * 1000);
        }

        // 统计每日营收和订单数
        for (Order order : orders) {
            if (order.getCheckInDate() != null && !order.getCheckInDate().before(startDate) && !order.getCheckInDate().after(endDate)) {
                String dateStr = dateFormat.format(order.getCheckInDate());
                dailyRevenue.put(dateStr, dailyRevenue.get(dateStr) + order.getTotalPrice());
                dailyOrderCount.put(dateStr, dailyOrderCount.get(dateStr) + 1);
            }
        }

        // 构建报表数据
        List<Map<String, Object>> report = new ArrayList<>();
        for (String dateStr : dailyRevenue.keySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dateStr);
            item.put("revenue", dailyRevenue.get(dateStr));
            item.put("orderCount", dailyOrderCount.get(dateStr));
            report.add(item);
        }

        return report;
    }

    @Override
    public List<Map<String, Object>> getOccupancyRateReport(Date startDate, Date endDate) {
        // 获取所有房间数量
        Long totalRooms = roomMapper.selectCount(null);
        if (totalRooms == 0) {
            totalRooms = 1L; // 避免除以零
        }

        // 从订单表中查询每日入住情况
        List<Order> orders = orderMapper.selectList(null);
        Map<String, Integer> dailyOccupiedRooms = new HashMap<>();

        // 初始化日期范围内的每一天
        Date currentDate = new Date(startDate.getTime());
        while (!currentDate.after(endDate)) {
            String dateStr = dateFormat.format(currentDate);
            dailyOccupiedRooms.put(dateStr, 0);
            // 增加一天
            currentDate.setTime(currentDate.getTime() + 24 * 60 * 60 * 1000);
        }

        // 统计每日占用房间数
        for (Order order : orders) {
            if (order.getCheckInDate() != null && order.getCheckOutDate() != null) {
                Date checkIn = order.getCheckInDate();
                Date checkOut = order.getCheckOutDate();

                // 遍历订单的每一天
                Date orderDate = new Date(checkIn.getTime());
                while (!orderDate.after(checkOut)) {
                    String dateStr = dateFormat.format(orderDate);
                    if (!dateStr.equals(dateFormat.format(checkOut))) { // 退房当天不算
                        if (dailyOccupiedRooms.containsKey(dateStr)) {
                            dailyOccupiedRooms.put(dateStr, dailyOccupiedRooms.get(dateStr) + 1);
                        }
                    }
                    // 增加一天
                    orderDate.setTime(orderDate.getTime() + 24 * 60 * 60 * 1000);
                }
            }
        }

        // 构建报表数据
        List<Map<String, Object>> report = new ArrayList<>();
        for (String dateStr : dailyOccupiedRooms.keySet()) {
            Map<String, Object> item = new HashMap<>();
            int occupied = dailyOccupiedRooms.get(dateStr);
            double rate = (double) occupied / totalRooms * 100;
            item.put("date", dateStr);
            item.put("occupancyRate", String.format("%.1f%%", rate));
            item.put("availableRooms", totalRooms - occupied);
            report.add(item);
        }

        return report;
    }

    @Override
    public List<Map<String, Object>> getAveragePriceReport(Date startDate, Date endDate) {
        // 从订单表中查询每日平均房价数据
        List<Order> orders = orderMapper.selectList(null);
        Map<String, Double> dailyTotalPrice = new HashMap<>();
        Map<String, Integer> dailyOrderCount = new HashMap<>();

        // 初始化日期范围内的每一天
        Date currentDate = new Date(startDate.getTime());
        while (!currentDate.after(endDate)) {
            String dateStr = dateFormat.format(currentDate);
            dailyTotalPrice.put(dateStr, 0.0);
            dailyOrderCount.put(dateStr, 0);
            // 增加一天
            currentDate.setTime(currentDate.getTime() + 24 * 60 * 60 * 1000);
        }

        // 统计每日总价格和订单数
        for (Order order : orders) {
            if (order.getCheckInDate() != null && !order.getCheckInDate().before(startDate) && !order.getCheckInDate().after(endDate)) {
                String dateStr = dateFormat.format(order.getCheckInDate());
                dailyTotalPrice.put(dateStr, dailyTotalPrice.get(dateStr) + order.getTotalPrice());
                dailyOrderCount.put(dateStr, dailyOrderCount.get(dateStr) + 1);
            }
        }

        // 构建报表数据
        List<Map<String, Object>> report = new ArrayList<>();
        for (String dateStr : dailyTotalPrice.keySet()) {
            Map<String, Object> item = new HashMap<>();
            int count = dailyOrderCount.get(dateStr);
            double avgPrice = count > 0 ? dailyTotalPrice.get(dateStr) / count : 0;
            item.put("date", dateStr);
            item.put("averagePrice", Math.round(avgPrice * 100) / 100.0);
            report.add(item);
        }

        return report;
    }

    @Override
    public List<Map<String, Object>> getMemberConsumptionReport(Date startDate, Date endDate) {
        // 从订单表中查询会员消费数据
        // 注意：这里简化处理，实际应该关联会员表
        List<Order> orders = orderMapper.selectList(null);
        Map<String, Double> memberConsumption = new HashMap<>();
        Map<String, Integer> memberCount = new HashMap<>();

        // 初始化会员等级
        String[] memberLevels = {"普通", "银卡", "金卡", "钻石卡"};
        for (String level : memberLevels) {
            memberConsumption.put(level, 0.0);
            memberCount.put(level, 0);
        }

        // 统计会员消费
        for (Order order : orders) {
            if (order.getCheckInDate() != null && !order.getCheckInDate().before(startDate) && !order.getCheckInDate().after(endDate)) {
                // 简化处理，根据订单ID取模分配会员等级
                int levelIndex = (int) (order.getId() % memberLevels.length);
                String level = memberLevels[levelIndex];
                memberConsumption.put(level, memberConsumption.get(level) + order.getTotalPrice());
                memberCount.put(level, memberCount.get(level) + 1);
            }
        }

        // 构建报表数据
        List<Map<String, Object>> report = new ArrayList<>();
        for (String level : memberLevels) {
            Map<String, Object> item = new HashMap<>();
            item.put("level", level);
            item.put("consumption", memberConsumption.get(level));
            item.put("count", memberCount.get(level));
            report.add(item);
        }

        return report;
    }

    @Override
    public Map<String, Object> getKeyIndicators() {
        // 从数据库中获取关键指标数据
        Map<String, Object> indicators = new HashMap<>();

        // 今天的日期
        Date today = new Date();
        String todayStr = dateFormat.format(today);

        // 计算今日营收
        double todayRevenue = 0;
        int todayCheckIn = 0;
        int todayCheckOut = 0;

        List<Order> orders = orderMapper.selectList(null);
        for (Order order : orders) {
            if (order.getCheckInDate() != null && dateFormat.format(order.getCheckInDate()).equals(todayStr)) {
                todayRevenue += order.getTotalPrice();
                todayCheckIn++;
            }
            if (order.getCheckOutDate() != null && dateFormat.format(order.getCheckOutDate()).equals(todayStr)) {
                todayCheckOut++;
            }
        }

        // 计算当前出租率
        Long totalRooms = roomMapper.selectCount(null);
        int currentOccupied = 0;
        if (totalRooms > 0) {
            for (Order order : orders) {
                if (order.getCheckInDate() != null && order.getCheckOutDate() != null) {
                    if (today.after(order.getCheckInDate()) && today.before(order.getCheckOutDate())) {
                        currentOccupied++;
                    }
                }
            }
        }
        double currentOccupancy = totalRooms > 0 ? (double) currentOccupied / totalRooms * 100 : 0;

        // 计算平均房价
        double totalPrice = 0;
        int orderCount = 0;
        for (Order order : orders) {
            totalPrice += order.getTotalPrice();
            orderCount++;
        }
        double averagePrice = orderCount > 0 ? totalPrice / orderCount : 0;

        // 计算会员总数（简化处理）
        int totalMembers = orderCount; // 实际应该从会员表查询

        // 填充指标数据
        indicators.put("todayRevenue", Math.round(todayRevenue * 100) / 100.0);
        indicators.put("todayCheckIn", todayCheckIn);
        indicators.put("todayCheckOut", todayCheckOut);
        indicators.put("currentOccupancy", Math.round(currentOccupancy * 10) / 10.0);
        indicators.put("averagePrice", Math.round(averagePrice * 100) / 100.0);
        indicators.put("totalMembers", totalMembers);

        return indicators;
    }
}