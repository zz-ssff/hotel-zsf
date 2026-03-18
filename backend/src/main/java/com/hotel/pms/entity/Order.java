package com.hotel.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("`order`")
@Schema(description = "订单信息实体")
public class Order {
    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID", example = "1")
    private Long id;
    
    @Schema(description = "订单编号", example = "ORD202401010001")
    private String orderNo;
    
    @Schema(description = "房间ID", example = "101")
    private Long roomId;
    
    @Schema(description = "客户ID", example = "1")
    private Long customerId;
    
    @Schema(description = "入住日期", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date checkInDate;
    
    @Schema(description = "退房日期", example = "2024-01-03")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date checkOutDate;
    
    @Schema(description = "入住天数", example = "2")
    private Integer days;
    
    @Schema(description = "总价格", example = "600.00")
    private Double totalPrice;
    
    @Schema(description = "订单状态", example = "已预订")
    private String status;
    
    @Schema(description = "支付状态", example = "已支付")
    private String paymentStatus;
    
    @Schema(description = "支付方式", example = "微信支付")
    private String paymentMethod;
    
    @Schema(description = "备注", example = "需要安静的房间")
    private String notes;
    
    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private Date createTime;
    
    @Schema(description = "更新时间", example = "2024-01-01T00:00:00")
    private Date updateTime;
}