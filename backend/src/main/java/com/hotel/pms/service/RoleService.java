package com.hotel.pms.service;

import com.hotel.pms.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> listAll();
    Role getById(Long id);
    Role getByRoleName(String roleName);
    boolean add(Role role);
    boolean update(Role role);
    boolean delete(Long id);
}