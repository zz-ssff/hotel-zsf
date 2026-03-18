package com.hotel.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("customer")
@Schema(description = "客户信息实体")
public class Customer {
    @TableId(type = IdType.AUTO)
    @Schema(description = "客户ID", example = "1")
    private Long id;
    
    @Schema(description = "客户姓名", example = "张三")
    private String name;
    
    @Schema(description = "身份证号", example = "110101199001011234")
    private String idCard;
    
    @Schema(description = "电话号码", example = "13800138000")
    private String phone;
    
    @Schema(description = "性别", example = "男")
    private String gender;
    
    @Schema(description = "出生日期", example = "1990-01-01")
    private Date birthDate;
    
    @Schema(description = "地址", example = "北京市朝阳区")
    private String address;
    
    @Schema(description = "会员ID", example = "M123456")
    private String memberId;
    
    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private Date createTime;
    
    @Schema(description = "更新时间", example = "2024-01-01T00:00:00")
    private Date updateTime;
}