package com.hotel.pms.service;

import com.hotel.pms.entity.User;
import java.util.List;

public interface UserService {
    List<User> listAll();
    User getById(Long id);
    User getByUsername(String username);
    boolean add(User user);
    boolean update(User user);
    boolean delete(Long id);
    User login(String username, String password);
}