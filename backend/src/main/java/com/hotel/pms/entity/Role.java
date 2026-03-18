package com.hotel.pms.entity;

import lombok.Data;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "角色实体")
public class Role {
    @Schema(description = "角色ID")
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
    @Schema(description = "角色描述")
    private String description;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "更新时间")
    private Date updateTime;
}