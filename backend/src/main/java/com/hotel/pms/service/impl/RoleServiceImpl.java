package com.hotel.pms.service.impl;

import com.hotel.pms.entity.Role;
import com.hotel.pms.mapper.RoleMapper;
import com.hotel.pms.service.RoleService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> listAll() {
        return roleMapper.selectList(null);
    }

    @Override
    public Role getById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public Role getByRoleName(String roleName) {
        return roleMapper.selectByRoleName(roleName);
    }

    @Override
    public boolean add(Role role) {
        Date now = new Date();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        return roleMapper.insert(role) > 0;
    }

    @Override
    public boolean update(Role role) {
        role.setUpdateTime(new Date());
        return roleMapper.updateById(role) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return roleMapper.deleteById(id) > 0;
    }
}