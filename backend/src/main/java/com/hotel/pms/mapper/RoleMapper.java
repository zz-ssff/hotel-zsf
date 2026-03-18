package com.hotel.pms.mapper;

import com.hotel.pms.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface RoleMapper extends BaseMapper<Role> {
    Role selectByRoleName(String roleName);
}