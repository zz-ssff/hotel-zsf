package com.hotel.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("member")
@Schema(description = "会员信息实体")
public class Member {
    @TableId(type = IdType.AUTO)
    @Schema(description = "会员ID", example = "1")
    private Long id;
    
    @Schema(description = "会员编号", example = "M123456")
    private String memberId;
    
    @Schema(description = "会员姓名", example = "李四")
    private String name;
    
    @Schema(description = "电话号码", example = "13900139000")
    private String phone;
    
    @Schema(description = "邮箱", example = "lisi@example.com")
    private String email;
    
    @Schema(description = "会员等级", example = "银卡")
    private String level;
    
    @Schema(description = "积分", example = "1000")
    private Integer points;
    
    @Schema(description = "加入日期", example = "2024-01-01")
    private Date joinDate;
    
    @Schema(description = "到期日期", example = "2025-01-01")
    private Date expireDate;
    
    @Schema(description = "状态", example = "活跃")
    private String status;
    
    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private Date createTime;
    
    @Schema(description = "更新时间", example = "2024-01-01T00:00:00")
    private Date updateTime;
}