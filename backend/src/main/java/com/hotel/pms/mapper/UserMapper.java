package com.hotel.pms.mapper;

import com.hotel.pms.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(String username);
}