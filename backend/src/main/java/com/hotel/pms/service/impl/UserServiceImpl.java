package com.hotel.pms.service.impl;

import com.hotel.pms.entity.User;
import com.hotel.pms.mapper.UserMapper;
import com.hotel.pms.service.UserService;
import com.hotel.pms.service.RoleService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Override
    public List<User> listAll() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            user.setRole(roleService.getById(user.getRoleId()));
        }
        return users;
    }

    @Override
    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setRole(roleService.getById(user.getRoleId()));
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            user.setRole(roleService.getById(user.getRoleId()));
        }
        return user;
    }

    @Override
    public boolean add(User user) {
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean update(User user) {
        user.setUpdateTime(new Date());
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getStatus() == 1) {
            user.setRole(roleService.getById(user.getRoleId()));
            return user;
        }
        return null;
    }
}