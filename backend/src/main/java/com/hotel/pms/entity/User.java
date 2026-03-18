package com.hotel.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "用户实体")
public class User {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "联系电话")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "角色ID")
    private Long roleId;
    @Schema(description = "状态：1-启用, 0-禁用")
    private Integer status;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "更新时间")
    private Date updateTime;
    @Schema(description = "关联的角色")
    @TableField(exist = false)
    private Role role;
}