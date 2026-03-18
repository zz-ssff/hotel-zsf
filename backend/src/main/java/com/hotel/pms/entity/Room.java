package com.hotel.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@TableName("room")
@Schema(description = "房间信息实体")
public class Room {
    @TableId(type = IdType.AUTO)
    @Schema(description = "房间ID", example = "1")
    private Long id;
    
    @Schema(description = "房间号", example = "101")
    private String roomNumber;
    
    @Schema(description = "房间类型", example = "标准间")
    private String roomType;
    
    @Schema(description = "房间状态", example = "1")
    private Integer status;
    
    @Schema(description = "价格", example = "300.00")
    private Double price;
    
    @Schema(description = "描述", example = "舒适的标准间，配有空调和电视")
    private String description;
    
    @Schema(description = "酒店ID", example = "H123456")
    private String hotelId;
    
    @Schema(description = "楼层", example = "1")
    private Integer floor;
    
    @Schema(description = "朝向", example = "朝南")
    private String orientation;
    
    @Schema(description = "设施", example = "空调,电视,独立卫浴")
    private String facilities;
    
    @Schema(description = "最大入住人数", example = "2")
    private Integer maxOccupancy;
    
    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private Date createTime;
    
    @Schema(description = "更新时间", example = "2024-01-01T00:00:00")
    private Date updateTime;
}